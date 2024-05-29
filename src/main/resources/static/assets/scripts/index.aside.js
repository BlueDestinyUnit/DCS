HTMLElement.INVALID_CLASS_NAME = '-invalid';
HTMLElement.VISIBLE_CLASS_NAME = '-visible';

HTMLElement.prototype.hide = function () {
    this.classList.remove(HTMLElement.VISIBLE_CLASS_NAME);
    return this;
}
HTMLElement.prototype.show = function () {
    this.classList.add(HTMLElement.VISIBLE_CLASS_NAME);
    return this;
}

const aside = document.getElementById('aside');

aside.querySelector('[rel="menuButton"]').onclick = () => {
    aside.hide();
}