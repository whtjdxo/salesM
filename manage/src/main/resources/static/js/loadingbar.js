/**
 * 전역 로딩 바 관리 객체
 */
const LoadingBar = {
    // 로딩 바 표시
    show: function(message) {
      const loadingBar = $('#globalLoadingBar');
      if (!loadingBar.length) {
        this._injectLoadingBar();
      }
      
      if (message) {
        $('#globalLoadingBar .loading-text').text(message);
      }
      
      $('#globalLoadingBar').fadeIn(200);
      $('body').css('overflow', 'hidden');
    },
    
    // 로딩 바 숨기기
    hide: function() {
      $('#globalLoadingBar').fadeOut(200);
      $('body').css('overflow', '');
    },
    
    // DOM에 로딩 바 주입 (없을 경우)
    _injectLoadingBar: function() {
      $('body').append(`
        <div id="globalLoadingBar" class="global-loading-bar" style="display: none;">
          <div class="loading-bar-container">
            <div class="progress">
              <div class="progress-bar progress-bar-striped progress-bar-animated" 
                   role="progressbar" style="width: 100%"></div>
            </div>
            <div class="loading-text">처리 중입니다. 잠시만 기다려 주세요...</div>
          </div>
        </div>
      `);
    }
  };
  
  // 문서 준비 시 자동으로 로딩 바 DOM 준비
  $(document).ready(function() {
    LoadingBar._injectLoadingBar();
  });