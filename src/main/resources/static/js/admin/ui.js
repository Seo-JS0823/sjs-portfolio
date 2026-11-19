const toastEl = document.getElementById('admin-toast');
let toastTimer;

export function toast(message, isError = false) {
  clearTimeout(toastTimer);
  toastEl.textContent = message;
  toastEl.classList.toggle('is-error', isError);
  toastEl.classList.add('is-show');
  toastTimer = setTimeout(() => toastEl.classList.remove('is-show'), 2400);
}

export function el(tag, attrs = {}, ...children) {
  const node = document.createElement(tag);

  Object.entries(attrs).forEach(([key, value]) => {
    if (value == null) return;
    if (key === 'class') node.className = value;
    else if (key === 'text') node.textContent = value;
    else if (key === 'style') node.style.cssText = value;
    else if (key.startsWith('on')) node.addEventListener(key.slice(2), value);
    else node.setAttribute(key, value);
  });

  children.flat().forEach((child) => {
    if (child != null) node.append(child);
  });

  return node;
}

export async function run(task, successMessage) {
  try {
    const data = await task();
    if (successMessage) toast(successMessage);
    return { ok: true, data };
  } catch (e) {
    if (e.status === 401) {
      toast('인증이 만료되었습니다. 다시 로그인해 주세요.', true);
      setTimeout(() => {
        location.href = '/po/admin/login';
      }, 1200);
    } else {
      toast(e.message, true);
    }
    return { ok: false };
  }
}

export function bindForm(form, { newLabel, editLabel, onSubmit, onReset, onEdit }) {
  const mode = form.querySelector('[data-mode]');

  const reset = () => {
    form.reset();
    if (form.elements.id) form.elements.id.value = '';
    form.classList.remove('is-editing');
    if (mode) mode.textContent = newLabel;
    onReset?.();
  };

  const edit = (values) => {
    reset();
    Object.entries(values).forEach(([key, value]) => {
      if (form.elements[key]) form.elements[key].value = value ?? '';
    });
    form.classList.add('is-editing');
    if (mode) mode.textContent = editLabel;
    onEdit?.();
    form.scrollIntoView({ behavior: 'smooth', block: 'center' });
  };

  form.querySelector('[data-reset]')?.addEventListener('click', reset);

  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const { id, ...body } = Object.fromEntries(new FormData(form));
    const ok = await onSubmit(id || null, body);
    if (ok) reset();
  });

  return {
    reset,
    edit,
    editingId: () => form.elements.id?.value || null,
  };
}

export function emptyItem(text) {
  return el('li', { class: 'admin-empty', text });
}
