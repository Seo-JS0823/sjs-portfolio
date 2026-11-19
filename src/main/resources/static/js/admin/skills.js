import { http } from '../api/http.js';
import { bindForm, el, run, toast } from './ui.js';

const API = '/api/tags';

const normalizeHex = (value) => {
  const hex = (value ?? '').trim().replace(/^#/, '');
  if (/^[0-9a-f]{3}$/i.test(hex)) {
    return `#${[...hex].map((c) => c + c).join('')}`.toUpperCase();
  }
  if (/^[0-9a-f]{6}$/i.test(hex)) {
    return `#${hex}`.toUpperCase();
  }
  return null;
};

export function initSkillAdmin() {
  const groupForm = document.getElementById('group-form');
  const tagForm = document.getElementById('tag-form');
  const groupList = document.getElementById('group-list');
  const groupSelect = tagForm.elements.tagGroupId;
  const preview = document.getElementById('tag-preview');

  const colorFields = [...tagForm.querySelectorAll('[data-color]')].map((wrap) => ({
    picker: wrap.querySelector('input[type="color"]'),
    text: wrap.querySelector('.admin-color-text'),
  }));

  const readColor = (name) => normalizeHex(tagForm.elements[name].value);

  const updatePreview = () => {
    preview.style.backgroundColor = readColor('bgColor') ?? '';
    preview.style.color = readColor('tColor') ?? '';
    preview.textContent = tagForm.elements.tagName.value || '미리보기';
  };

  const syncPickers = () => {
    colorFields.forEach(({ picker, text }) => {
      const hex = normalizeHex(text.value);
      text.classList.toggle('is-invalid', !hex);
      if (hex) {
        picker.value = hex;
        text.value = hex;
      }
    });
    updatePreview();
  };

  colorFields.forEach(({ picker, text }) => {
    picker.addEventListener('input', () => {
      text.value = picker.value.toUpperCase();
      text.classList.remove('is-invalid');
      updatePreview();
    });
    text.addEventListener('input', () => {
      const hex = normalizeHex(text.value);
      text.classList.toggle('is-invalid', !hex && text.value.trim() !== '');
      if (hex) picker.value = hex;
      updatePreview();
    });
    text.addEventListener('blur', () => {
      const hex = normalizeHex(text.value);
      if (hex) text.value = hex;
    });
  });

  const renderSelect = (groups) => {
    const current = groupSelect.value;
    const options = groups.length
      ? groups.map((g) => el('option', { value: g.tagGroupId, text: g.tagGroupName }))
      : [el('option', { value: '', text: '먼저 그룹을 추가하세요', disabled: '' })];
    groupSelect.replaceChildren(...options);
    if (groups.some((g) => String(g.tagGroupId) === current)) groupSelect.value = current;
  };

  const load = async () => {
    const { ok, data } = await run(() => http.get(API));
    if (!ok) return;
    renderSelect(data.tags);
    const groups = data.tags.map(renderGroup);
    groupList.replaceChildren(
      ...(groups.length ? groups : [el('p', { class: 'admin-empty', text: '등록된 그룹이 없습니다.' })])
    );
  };

  const editor = bindForm(tagForm, {
    newLabel: '새 태그',
    editLabel: '태그 수정',
    onReset: () => {
      groupSelect.disabled = false;
      syncPickers();
    },
    onEdit: () => {
      groupSelect.disabled = true;
      syncPickers();
    },
    onSubmit: async (id, body) => {
      const bgColor = normalizeHex(body.bgColor);
      const tColor = normalizeHex(body.tColor);
      if (!bgColor || !tColor) {
        toast('색상 코드는 #RRGGBB 형식으로 입력해 주세요.', true);
        return false;
      }
      const payload = { tagName: body.tagName, bgColor, tColor };
      const { ok } = await run(
        () => (id
          ? http.patch(`${API}/tag/${id}`, payload)
          : http.post(API, { ...payload, tagGroupId: Number(groupSelect.value) })),
        '저장했습니다.'
      );
      if (ok) await load();
      return ok;
    },
  });

  const removeGroup = async (group) => {
    if (!confirm(`"${group.tagGroupName}" 그룹과 안의 태그를 모두 삭제할까요?`)) return;
    const { ok } = await run(() => http.delete(`${API}/group/${group.tagGroupId}`), '삭제했습니다.');
    if (!ok) return;
    editor.reset();
    await load();
  };

  const removeTag = async (tag) => {
    if (!confirm(`"${tag.tagName}" 태그를 삭제할까요?`)) return;
    const { ok } = await run(() => http.delete(`${API}/tag/${tag.tagId}`), '삭제했습니다.');
    if (!ok) return;
    if (editor.editingId() === String(tag.tagId)) editor.reset();
    await load();
  };

  const renderTag = (group, tag) =>
    el('li', { class: 'admin-tag-item' },
      el('button', {
        type: 'button',
        class: 'tag',
        title: '클릭해서 수정',
        style: `background-color:${tag.bgColor};color:${tag.tColor}`,
        text: tag.tagName,
        onclick: () => editor.edit({
          id: tag.tagId,
          tagGroupId: group.tagGroupId,
          tagName: tag.tagName,
          bgColor: tag.bgColor,
          tColor: tag.tColor,
        }),
      }),
      el('button', {
        type: 'button',
        class: 'admin-tag-remove',
        'aria-label': `${tag.tagName} 삭제`,
        text: '×',
        onclick: () => removeTag(tag),
      })
    );

  const renderGroup = (group) =>
    el('div', { class: 'admin-group' },
      el('div', { class: 'admin-group-head' },
        el('h3', { class: 'admin-group-name', text: group.tagGroupName }),
        el('button', {
          type: 'button',
          class: 'btn btn-danger btn-sm',
          text: '그룹 삭제',
          onclick: () => removeGroup(group),
        })
      ),
      group.tagItems.length
        ? el('ul', { class: 'admin-tags' }, group.tagItems.map((tag) => renderTag(group, tag)))
        : el('p', { class: 'admin-empty', text: '태그가 없습니다.' })
    );

  groupForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const { ok, data } = await run(
      () => http.post(`${API}/group`, { name: groupForm.elements.name.value }),
      '그룹을 추가했습니다.'
    );
    if (!ok) return;
    groupForm.reset();
    await load();
    if (!editor.editingId() && data?.tagGroupId) groupSelect.value = data.tagGroupId;
  });

  tagForm.elements.tagName.addEventListener('input', updatePreview);

  syncPickers();
  load();
}
