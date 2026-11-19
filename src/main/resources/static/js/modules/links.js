export function initLinks() {
  const marquee = document.querySelector('.link-marquee');
  if (!marquee) return;

  const track = marquee.querySelector('.link-track');
  const group = track.querySelector('.link-group');

  const hide = (node) => {
    node.setAttribute('aria-hidden', 'true');
    node.querySelectorAll('a').forEach((a) => a.setAttribute('tabindex', '-1'));
    return node;
  };

  const readSpeed = () => {
    const value = parseFloat(getComputedStyle(document.documentElement).getPropertyValue('--links-speed'));
    return value > 0 ? value : 40;
  };

  const build = () => {
    track.classList.remove('is-running');
    track.querySelectorAll('.link-group.is-clone').forEach((clone) => clone.remove());
    marquee.classList.add('is-static');

    if (group.offsetWidth <= marquee.clientWidth) return;

    marquee.classList.remove('is-static');

    const copy = hide(group.cloneNode(true));
    copy.classList.add('is-clone');
    track.appendChild(copy);

    track.style.setProperty('--links-duration', `${group.offsetWidth / readSpeed()}s`);
    track.classList.add('is-running');
  };

  build();

  let width = marquee.clientWidth;
  let timer;
  window.addEventListener('resize', () => {
    clearTimeout(timer);
    timer = setTimeout(() => {
      if (marquee.clientWidth === width) return;
      width = marquee.clientWidth;
      build();
    }, 200);
  });
}
