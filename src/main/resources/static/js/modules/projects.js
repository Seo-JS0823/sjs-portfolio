import { http } from '../api/http.js';

export function initProjectLinks() {
  const toggles = [...document.querySelectorAll('.project-links-toggle')];
  if (toggles.length === 0) return;

  const menuOf = (toggle) => document.getElementById(toggle.getAttribute('aria-controls'));

  const close = (toggle) => {
    toggle.setAttribute('aria-expanded', 'false');
    menuOf(toggle).hidden = true;
  };

  const closeAll = (except) => toggles.forEach((t) => t !== except && close(t));

  toggles.forEach((toggle) => {
    toggle.addEventListener('click', () => {
      const willOpen = toggle.getAttribute('aria-expanded') !== 'true';
      closeAll(toggle);
      toggle.setAttribute('aria-expanded', String(willOpen));
      menuOf(toggle).hidden = !willOpen;
    });
  });

  document.addEventListener('click', (e) => {
    if (!e.target.closest('.project-links')) closeAll();
  });

  document.addEventListener('keydown', (e) => {
    if (e.key !== 'Escape') return;
    const open = toggles.find((t) => t.getAttribute('aria-expanded') === 'true');
    if (!open) return;
    close(open);
    open.focus();
  });
}

export function initProjects() {
  const modal = document.querySelector('.project-modal');
  const items = document.querySelectorAll('.project-item[data-post-id]');
  if (!modal || items.length === 0) return;

  const numEl = modal.querySelector('.project-modal-num');
  const titleEl = modal.querySelector('.project-modal-title');
  const descEl = modal.querySelector('.project-modal-desc');
  const periodEl = modal.querySelector('.project-modal-period');
  const contentEl = modal.querySelector('.project-modal-content');
  const closeBtn = modal.querySelector('.project-modal-close');

  const cache = new Map();
  let currentId = null;

  const setContent = (text, state) => {
    contentEl.textContent = text;
    contentEl.classList.toggle('is-loading', state === 'loading');
    contentEl.classList.toggle('is-error', state === 'error');
  };

  const formatPeriod = ({ startDate, endDate }) =>
    startDate ? `${startDate} ~ ${endDate ?? '진행 중'}` : '';

  const render = (detail) => {
    titleEl.textContent = detail.title;
    descEl.textContent = detail.description;
    periodEl.textContent = formatPeriod(detail);
    contentEl.classList.remove('is-loading', 'is-error');
    contentEl.innerHTML = detail.contentHtml ?? '';
  };

  const open = async (item) => {
    const id = item.dataset.postId;
    currentId = id;

    numEl.textContent = `PROJECT ${item.querySelector('.project-num').textContent}`;
    titleEl.textContent = item.querySelector('.project-title').textContent;
    descEl.textContent = item.querySelector('.project-desc').textContent;
    periodEl.textContent = item.querySelector('.project-period')?.textContent ?? '';
    contentEl.scrollTop = 0;
    modal.showModal();

    http.post(`/api/posts/${id}/views`).catch(() => {});

    if (cache.has(id)) {
      render(cache.get(id));
      return;
    }

    setContent('불러오는 중...', 'loading');

    try {
      const detail = await http.get(`/api/posts/${id}`);
      cache.set(id, detail);
      if (currentId === id) render(detail);
    } catch {
      if (currentId === id) setContent('내용을 불러오지 못했습니다.', 'error');
    }
  };

  items.forEach((item) => {
    item.addEventListener('click', (e) => {
      if (e.target.closest('.project-links')) return;
      if (e.target.closest('.project-actions') && !e.target.closest('.project-detail-btn')) return;
      if (window.getSelection()?.toString()) return;
      open(item);
    });
  });
  closeBtn.addEventListener('click', () => modal.close());
  modal.addEventListener('click', (e) => {
    if (e.target === modal) modal.close();
  });
  modal.addEventListener('close', () => {
    currentId = null;
  });
}
