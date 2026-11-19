import { http } from '../api/http.js';
import { bindForm, el, emptyItem, run } from './ui.js';

const API = '/api/posts';

const toInput = (date) => (date ? date.replaceAll('-', '') : '');

const formatPeriod = ({ startDate, endDate }) =>
  startDate ? `${startDate} ~ ${endDate ?? '진행 중'}` : '기간 없음';

export function initProjectAdmin() {
  const form = document.getElementById('post-form');
  const list = document.getElementById('post-list');
  const linksBox = document.getElementById('post-links');

  const addLinkRow = (link = {}) => {
    const title = el('input', { class: 'admin-input', placeholder: '이름 (GitHub)', 'data-link': 'title', 'aria-label': '링크 이름' });
    const url = el('input', { class: 'admin-input', type: 'url', placeholder: 'https://', 'data-link': 'url', 'aria-label': '링크 URL' });
    title.value = link.title ?? '';
    url.value = link.url ?? '';
    const row = el('div', { class: 'admin-link-row' },
      title,
      url,
      el('button', {
        type: 'button',
        class: 'admin-link-remove',
        'aria-label': '링크 삭제',
        text: '×',
        onclick: () => row.remove(),
      })
    );
    linksBox.append(row);
    return title;
  };

  const collectLinks = () =>
    [...linksBox.querySelectorAll('.admin-link-row')]
      .map((row) => ({
        title: row.querySelector('[data-link="title"]').value.trim(),
        url: row.querySelector('[data-link="url"]').value.trim(),
      }))
      .filter((link) => link.title || link.url);

  document.getElementById('post-link-add').addEventListener('click', () => addLinkRow().focus());

  const load = async () => {
    const { ok, data } = await run(() => http.get(API));
    if (!ok) return;
    const items = data.posts.map(renderItem);
    list.replaceChildren(...(items.length ? items : [emptyItem('등록된 프로젝트가 없습니다.')]));
  };

  const editor = bindForm(form, {
    newLabel: '새 프로젝트',
    editLabel: '프로젝트 수정',
    onReset: () => linksBox.replaceChildren(),
    onSubmit: async (id, fields) => {
      const body = { ...fields, links: collectLinks() };
      const { ok } = await run(
        () => (id ? http.patch(`${API}/${id}`, body) : http.post(API, body)),
        '저장했습니다.'
      );
      if (ok) await load();
      return ok;
    },
  });

  const edit = async (post) => {
    const { ok, data } = await run(() => http.get(`${API}/${post.postId}`));
    if (!ok) return;
    editor.edit({
      id: post.postId,
      title: data.title,
      description: data.description,
      startDate: toInput(data.startDate),
      endDate: toInput(data.endDate),
      content: data.content,
    });
    (data.links ?? []).forEach((link) => addLinkRow(link));
  };

  const remove = async (post) => {
    if (!confirm(`"${post.title}" 프로젝트를 삭제할까요?`)) return;
    const { ok } = await run(() => http.delete(`${API}/${post.postId}`), '삭제했습니다.');
    if (!ok) return;
    if (editor.editingId() === String(post.postId)) editor.reset();
    await load();
  };

  const renderItem = (post) =>
    el('li', { class: 'admin-item' },
      el('div', { class: 'admin-item-body' },
        el('p', { class: 'admin-item-title', text: post.title }),
        el('p', {
          class: 'admin-item-sub',
          text: [
            formatPeriod(post),
            post.links?.length ? `링크 ${post.links.length}개` : null,
            `조회 ${post.viewCount ?? 0}`,
          ].filter(Boolean).join(' · '),
        })
      ),
      el('div', { class: 'admin-item-actions' },
        el('button', { type: 'button', class: 'btn btn-sub btn-sm', text: '수정', onclick: () => edit(post) }),
        el('button', { type: 'button', class: 'btn btn-danger btn-sm', text: '삭제', onclick: () => remove(post) })
      )
    );

  load();
}
