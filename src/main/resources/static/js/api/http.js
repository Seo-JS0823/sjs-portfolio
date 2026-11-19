async function request(url, { method = 'GET', body } = {}) {
  const options = { method, headers: {} };

  if (body !== undefined) {
    options.headers['Content-Type'] = 'application/json';
    options.body = JSON.stringify(body);
  }

  const res = await fetch(url, options);

  if (!res.ok) {
    const text = await res.text();
    let message = text;
    try {
      message = JSON.parse(text).message ?? text;
    } catch {
    }
    const error = new Error(message || `요청 실패 (${res.status})`);
    error.status = res.status;
    throw error;
  }

  const text = await res.text();
  return text ? JSON.parse(text) : null;
}

export const http = {
  get: (url) => request(url),
  post: (url, body) => request(url, { method: 'POST', body }),
  patch: (url, body) => request(url, { method: 'PATCH', body }),
  delete: (url) => request(url, { method: 'DELETE' }),
};
