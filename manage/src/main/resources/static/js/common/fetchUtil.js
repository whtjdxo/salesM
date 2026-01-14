/**
 * 공통 Fetch 유틸
 */
const FetchUtil = {

  get(url, params = {}) {
    const query = new URLSearchParams(params).toString();
    const requestUrl = query ? `${url}?${query}` : url;

    return fetch(requestUrl, {
      method: 'GET',
      headers: { 'Accept': 'application/json' }
    })
      .then(this._handleResponse);
  },

  post(url, body = {}) {
    return fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      body: JSON.stringify(body)
    })
      .then(this._handleResponse);
  },

  _handleResponse(res) {
    if (!res.ok) {
      throw new Error(`HTTP ${res.status}`);
    }
    return res.json();
  }

};
