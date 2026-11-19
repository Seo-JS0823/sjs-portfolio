export function initHeader() {
  const toggle = document.querySelector('.menu-toggle');
  const nav = document.querySelector('.nav');
  const links = document.querySelectorAll('.nav-link');

  if (!toggle || !nav) return;

  const setOpen = (open) => {
    nav.classList.toggle('is-open', open);
    toggle.setAttribute('aria-expanded', String(open));
    toggle.setAttribute('aria-label', open ? '메뉴 닫기' : '메뉴 열기');
  };

  toggle.addEventListener('click', () => {
    setOpen(!nav.classList.contains('is-open'));
  });

  links.forEach((link) => link.addEventListener('click', () => setOpen(false)));

  const sections = document.querySelectorAll('main section[id]');

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (!entry.isIntersecting) return;

        links.forEach((link) => {
          link.classList.toggle('is-active', link.getAttribute('href') === `#${entry.target.id}`);
        });
      });
    },
    { rootMargin: '-45% 0px -55% 0px' }
  );

  sections.forEach((section) => observer.observe(section));
}
