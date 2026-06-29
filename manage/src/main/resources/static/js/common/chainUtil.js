/**
 * 가맹점 검색 유틸
 */
const ChainUtil = {

  defaultOptions: {
    chainNameId: 'sch_chain_nm',
    chainNoId: 'sch_chain_no',
    searchButtonId: 'btn_chain_find',
    modalId: 'modal_chain_select',
    tableBodySelector: '#tbl_chain_select tbody',
    clearOnInput: true,
    onSelected: null,
    onNotFound: null
  },

  bind(options = {}) {
    const opts = { ...this.defaultOptions, ...options };
    const nameEl = document.getElementById(opts.chainNameId);
    const searchBtn = document.getElementById(opts.searchButtonId);
    const noEl = document.getElementById(opts.chainNoId);

    if (!nameEl) return;

    if (opts.clearOnInput && noEl) {
      nameEl.addEventListener('input', () => {
        noEl.value = '';
      });
    }

    nameEl.addEventListener('keydown', e => {
      if (e.key === 'Enter') {
        e.preventDefault();
        this.search(opts);
      }
    });

    if (searchBtn) {
      searchBtn.addEventListener('click', () => this.search(opts));
    }
  },

  search(options = {}) {
    const opts = { ...this.defaultOptions, ...options };
    const nameEl = document.getElementById(opts.chainNameId);
    const noEl = document.getElementById(opts.chainNoId);

    if (!nameEl) {
      return;
    }

    const keyword = (nameEl.value || '').trim();

    if (!keyword) return alert('가맹점명을 입력하세요.');
    if (keyword.length < 2) return alert('가맹점명은 2자 이상 입력하세요.');

    FetchUtil.get('/common/getChainSchList', { keyword })
      .then(list => {
        const results = list || [];

        if (results.length === 0) {
          alert('검색 결과가 없습니다.');
          if (noEl) {
            noEl.value = '';
          }
          if (typeof opts.onNotFound === 'function') {
            opts.onNotFound();
          }
          return;
        }

        if (results.length === 1) {
          this._applySelected(results[0], opts);
          return;
        }

        this._openSelectModal(results, opts);
      })
      .catch(() => alert('가맹점 검색 중 오류가 발생했습니다.'));
  },

  _applySelected(item, options = {}) {
    const opts = { ...this.defaultOptions, ...options };
    const nameEl = document.getElementById(opts.chainNameId);
    const noEl = document.getElementById(opts.chainNoId);

    if (noEl) {
      noEl.value = item.code;
    }
    if (nameEl) {
      nameEl.value = item.codeNm;
    }

    if (typeof opts.onSelected === 'function') {
      opts.onSelected(item);
    }
  },

  _openSelectModal(results, options = {}) {
    const opts = { ...this.defaultOptions, ...options };
    const tbody = document.querySelector(opts.tableBodySelector);
    if (!tbody) {
      return;
    }
    tbody.innerHTML = '';

    results.forEach(item => {
      const tr = document.createElement('tr');
      tr.style.cursor = 'pointer';
      tr.innerHTML = `
        <td class="text-center">${item.code}</td>
        <td>${item.codeNm}</td>
      `;
      tr.onclick = () => {
        this._applySelected(item, opts);
        ModalUtil.hide(opts.modalId);
      };
      tbody.appendChild(tr);
    });

    ModalUtil.show(opts.modalId);
  }

};
