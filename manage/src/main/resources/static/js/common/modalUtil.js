/**
 * Bootstrap 5 Modal 공통 유틸
 */
const ModalUtil = {

  show(id, options = {}) {
    const el = document.getElementById(id);
    if (!el) return null;

    let instance = bootstrap.Modal.getInstance(el);
    if (!instance) {
      instance = new bootstrap.Modal(el, {
        backdrop: options.backdrop ?? 'static',
        keyboard: options.keyboard ?? false
      });
    }
    instance.show();
    return instance;
  },

  hide(id) {
    const el = document.getElementById(id);
    const instance = bootstrap.Modal.getInstance(el);
    if (instance) instance.hide();
  },

  reset(id) {
    const el = document.getElementById(id);
    if (!el) return;

    el.querySelectorAll('input, textarea').forEach(i => i.value = '');
    el.querySelectorAll('tbody').forEach(tb => tb.innerHTML = '');
  }

};
