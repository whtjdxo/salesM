/**
 * 가맹점 검색 유틸
 */
const ChainUtil = {

  search() {
    const nameEl = document.getElementById('sch_chain_nm');
    const noEl = document.getElementById('sch_chain_no');

    const keyword = (nameEl.value || '').trim();

    if (!keyword) return alert('가맹점명을 입력하세요.');
    if (keyword.length < 2) return alert('가맹점명은 2자 이상 입력하세요.');

    FetchUtil.get('/common/getChainSchList', { keyword })
      .then(list => {
        const results = list || [];

        if (results.length === 0) {
          alert('검색 결과가 없습니다.');
          noEl.value = '';
          return;
        }

        if (results.length === 1) {
          noEl.value = results[0].code;
          nameEl.value = results[0].codeNm;
          return;
        }

        this._openSelectModal(results);
      })
      .catch(() => alert('가맹점 검색 중 오류가 발생했습니다.'));
  },

  _openSelectModal(results) {
    const tbody = document.querySelector('#tbl_chain_select tbody');
    tbody.innerHTML = '';

    results.forEach(item => {
      const tr = document.createElement('tr');
      tr.style.cursor = 'pointer';
      tr.innerHTML = `
        <td class="text-center">${item.code}</td>
        <td>${item.codeNm}</td>
      `;
      tr.onclick = () => {
        document.getElementById('sch_chain_no').value = item.code;
        document.getElementById('sch_chain_nm').value = item.codeNm;
        ModalUtil.hide('modal_chain_select');
      };
      tbody.appendChild(tr);
    });

    ModalUtil.show('modal_chain_select');
  }

};
