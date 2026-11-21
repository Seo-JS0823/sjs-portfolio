import { http } from '../api/http.js';
import { bindForm, el, emptyItem, run, toast } from './ui.js';

const API = '/api/careers';
const KEYWORD_MAX = 50;

const toInput = (date) => (date ? date.replaceAll('-', '') : '');

const formatPeriod = ({ startDate, endDate }) => `${startDate} ~ ${endDate ?? '재직 중'}`;

const parseKeywords = (value) => [
  ...new Set(
    (value ?? '')
      .split(',')
      .map((keyword) => keyword.trim())
      .filter(Boolean)
  ),
];

export function initCareerAdmin() {
  const form = document.getElementById('career-form');
  const list = document.getElementById('career-list');
  const contentsBox = document.getElementById('career-contents');

  const addContentRow = (value = '') => {
    const textarea = el('textarea', {
      class: 'admin-input',
      rows: '2',
      placeholder: '담당 업무, 성과 등',
      'aria-label': '업무 내용',
    });
    textarea.value = value;
    const row = el('div', { class: 'admin-content-row' },
      textarea,
      el('button', {
        type: 'button',
        class: 'admin-link-remove',
        'aria-label': '내용 삭제',
        text: '×',
        onclick: () => row.remove(),
      })
    );
    contentsBox.append(row);
    return textarea;
  };

  const collectContents = () =>
    [...contentsBox.querySelectorAll('textarea')]
      .map((textarea) => textarea.value.trim())
      .filter(Boolean);

  document.getElementById('career-content-add').addEventListener('click', () => addContentRow().focus());

  const load = async () => {
    const { ok, data } = await run(() => http.get(API));
    if (!ok) return;
    const items = data.careers.map(renderItem);
    list.replaceChildren(...(items.length ? items : [emptyItem('등록된 경력이 없습니다.')]));
  };

  const editor = bindForm(form, {
    newLabel: '새 경력',
    editLabel: '경력 수정',
    onReset: () => contentsBox.replaceChildren(),
    onSubmit: async (id, fields) => {
      const keywords = parseKeywords(fields.keywords);
      if (keywords.some((keyword) => keyword.length > KEYWORD_MAX)) {
        toast(`키워드는 ${KEYWORD_MAX}자 이하로 입력해 주세요.`, true);
        return false;
      }
      const body = {
        title: fields.title,
        startDate: fields.startDate,
        endDate: fields.endDate,
        keywords,
        contents: collectContents(),
      };
      const { ok } = await run(
        () => (id ? http.patch(`${API}/${id}`, body) : http.post(API, body)),
        '저장했습니다.'
      );
      if (ok) await load();
      return ok;
    },
  });

  const edit = (career) => {
    editor.edit({
      id: career.careerId,
      title: career.title,
      startDate: toInput(career.startDate),
      endDate: toInput(career.endDate),
      keywords: career.keywords.join(', '),
    });
    career.contents.forEach((content) => addContentRow(content));
  };

  const remove = async (career) => {
    if (!confirm(`"${career.title}" 경력을 삭제할까요?`)) return;
    const { ok } = await run(() => http.delete(`${API}/${career.careerId}`), '삭제했습니다.');
    if (!ok) return;
    if (editor.editingId() === String(career.careerId)) editor.reset();
    await load();
  };

  const renderItem = (career) =>
    el('li', { class: 'admin-item' },
      el('div', { class: 'admin-item-body' },
        el('p', { class: 'admin-item-title', text: career.title }),
        el('p', {
          class: 'admin-item-sub',
          text: [
            formatPeriod(career),
            career.keywords.length ? career.keywords.join(', ') : null,
            `내용 ${career.contents.length}개`,
          ].filter(Boolean).join(' · '),
        })
      ),
      el('div', { class: 'admin-item-actions' },
        el('button', { type: 'button', class: 'btn btn-sub btn-sm', text: '수정', onclick: () => edit(career) }),
        el('button', { type: 'button', class: 'btn btn-danger btn-sm', text: '삭제', onclick: () => remove(career) })
      )
    );

  load();
}
