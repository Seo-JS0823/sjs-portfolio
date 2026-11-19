import { http } from '../api/http.js';
import { bindForm, el, emptyItem, run } from './ui.js';

const API = '/api/linkcard';

export function initLinkAdmin() {
  const form = document.getElementById('link-form');
  const list = document.getElementById('link-list');

  const load = async () => {
    const { ok, data } = await run(() => http.get(API));
    if (!ok) return;
    const items = data.lcs.map(renderItem);
    list.replaceChildren(...(items.length ? items : [emptyItem('등록된 링크가 없습니다.')]));
  };

  const editor = bindForm(form, {
    newLabel: '새 링크',
    editLabel: '링크 수정',
    onSubmit: async (id, body) => {
      const { ok } = await run(
        () => (id ? http.patch(`${API}/${id}`, body) : http.post(API, body)),
        '저장했습니다.'
      );
      if (ok) await load();
      return ok;
    },
  });

  const remove = async (lc) => {
    if (!confirm(`"${lc.title}" 링크를 삭제할까요?`)) return;
    const { ok } = await run(() => http.delete(`${API}/${lc.linkCardId}`), '삭제했습니다.');
    if (!ok) return;
    if (editor.editingId() === String(lc.linkCardId)) editor.reset();
    await load();
  };

  const renderItem = (lc) =>
    el('li', { class: 'admin-item' },
      el('div', { class: 'admin-item-body' },
        el('p', { class: 'admin-item-title', text: lc.title }),
        el('p', { class: 'admin-item-sub', text: lc.url })
      ),
      el('div', { class: 'admin-item-actions' },
        el('button', {
          type: 'button',
          class: 'btn btn-sub btn-sm',
          text: '수정',
          onclick: () => editor.edit({
            id: lc.linkCardId,
            title: lc.title,
            url: lc.url,
            icon: lc.icon,
            description: lc.description,
          }),
        }),
        el('button', { type: 'button', class: 'btn btn-danger btn-sm', text: '삭제', onclick: () => remove(lc) })
      )
    );

  load();
}
