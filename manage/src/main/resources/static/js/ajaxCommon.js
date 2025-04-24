const LoadingBar = {
  show: function(message) {
    if (!$('#globalLoadingBar').length) {
      this._injectLoadingBar();
    }
    
    if (message) {
      $('#globalLoadingBar .loading-text').text(message);
    }
    
    $('#globalLoadingBar').fadeIn(200);
    $('body').css('overflow', 'hidden');
  },
  
  hide: function() {
    $('#globalLoadingBar').fadeOut(200, () => {
      $('body').css('overflow', 'auto');
    });
  },
  
  _injectLoadingBar: function() {
    if ($('#globalLoadingBar').length) return;
    
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

// 문서 준비 시 한 번만 로딩 바 DOM 준비
$(document).ready(function() {
  LoadingBar._injectLoadingBar();
});

function swal(title, text, icon) {
  Swal.fire({
    title: title,
    text: text,
    icon: icon,
    confirmButtonText: "확인",
  });
}

async function callAjax(target, form, callback) {
  LoadingBar.show("처리 중입니다...");
  
  try {
    const response = await fetch(target, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: new URLSearchParams(form),
      cache: "no-cache",
    });

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }
    
    const data = await response.json();

    if (data.resultCode === "F001") {
      location.replace("/login");
    } else if (data.resultCode === "S000") {
      callback(data);
    } else {
      swal("실패", data.resultMsg, "error");
    }
  } catch (error) {
    console.error("AJAX Error:", error);
    swal("실패", "작업수행에 실패하였습니다.", "error");
  } finally {
    LoadingBar.hide();
  }
}
/*
 * 기본 AJAX - json 호출 함수
 * @param target : 호출 URL
 * @param json 값 : 넘기는 값
 * @param callback : 실행후 호출 함수
 */
async function callAjaxJson(target, params, callback) {
  LoadingBar.show("처리 중입니다...");
  try {
    const response = await fetch(target, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(params), // JSON 변환
    });

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();

    if (data.resultCode === "F001") {
      location.replace("/login");
    } else if (data.resultCode === "S000") {
      callback(data);
    } else {
      swal("실패", data.resultMsg, "error");
    }
  } catch (error) {
    swal("실패", "작업수행에 실패하였습니다.", "error");
    console.error("Fetch error: ", error); 
  } finally {
    LoadingBar.hide();
  }
}


/*
 * 코드 아작스 호출 함수
 * @param target : 호출 URL
 * @param form : 넘기는 값(기본 쿼리스트링)
 */
async function callAjaxCode(target, form) {
  try {
    const response = await fetch(target, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(form),
      cache: "no-cache",
    });

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    const data = await response.json();

    // Handle the response data as needed
  } catch (error) {
    swal("실패", "작업수행에 실패하였습니다.", "error");
  }
}
/*
 * 콤보박스 아작스 호출 함수
 * @param target : 호출 URL
 * @param data : 넘기는 값(기본 쿼리스트링)
 * @param combo : 대상 select, 멀티 호출 시 예)#aa,#bb 단건일 경우 기존과 동일  val과 갯수 맞출것
 * @param type : 1 - 선택, 2 - 전체, 3 - 무조건 선택
 * @param val : 넘기는 값(기본 쿼리스트링), 멀티 호출 시 예)#aa,#bb 단건일 경우 기존과 동일 combo와 갯수 맞출것
 * @multiYn 멀티셀렉트 여부
 */
async function callAjaxCombo(target, param, combo, type, val, multiYn) {
  try {
    const response = await fetch(target, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: new URLSearchParams(param),
      cache: "no-cache",
    });

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    const data = await response.json();

    if (data.resultCode === "F001") {
      location.replace("/login");
    }

    const cnt = data.length;
    const comboArray = combo.split(",");

    comboArray.forEach((comboItem) => {
      const selectElement = document.querySelector(comboItem);
      if (cnt > 0) {
        if (type === "1") {
          selectElement.innerHTML = '<option value="">선 택</option>';
        } else if (type === "2") {
          selectElement.innerHTML = '<option value="">전 체</option>';
        }

        data.forEach((item) => {
          const valArray = val.split(",");
          valArray.forEach((valItem) => {
            if (valItem === item.code) {
              selectElement.innerHTML += `<option value="${item.code}" selected="selected">${item.codeNm}</option>`;
            } else {
              selectElement.innerHTML += `<option value="${item.code}">${item.codeNm}</option>`;
            }
          });
        });
      }
    });
  } catch (error) {
    swal("실패", "작업수행에 실패하였습니다.", "error");
  }
}

/*
 * 라디오, 체크박스 아작스 호출 함수
 * @param target : 호출 URL
 * @param data : 넘기는 값(기본 쿼리스트링)
 * @param combo : 대상 DIV(감싸는 DIV), 멀티 호출 시 예)#aa,#bb 단건일 경우 기존과 동일  val과 갯수 맞출것
 * @param type : radio, check
 * @param val : 넘기는 값(기본 쿼리스트링), 멀티 호출 시 예)#aa,#bb 단건일 경우 기존과 동일 combo와 갯수 맞출것
 * @multiYn 멀티셀렉트 여부
 */
async function callAjaxCheckCode(target, form, callback) {
  try {
    const response = await fetch(target, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(form),
      cache: "no-cache",
    });

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    const data = await response.json();

    if (data.resultCode === "F001") {
      location.replace("/login");
    } else if (data.resultCode === "S000") {
      callback(data);
    } else {
      swal("실패", data.resultMsg, "error");
    }
  } catch (error) {
    swal("실패", "작업수행에 실패하였습니다.", "error");
  }
}

/*
 * 코드 콤보 아작스 호출 함수
 * @param target : 호출 URL
 * @param data : 넘기는 값(기본 쿼리스트링)
 * @param combo : 대상 select, 멀티 호출 시 예)#aa,#bb 단건일 경우 기존과 동일  val과 갯수 맞출것
 * @param type : 1 - 선택, 2 - 전체, 3 - 무조건 선택
 * @param val : 넘기는 값(기본 쿼리스트링) - 없으면 값을 넘기지 않아도 됨, 멀티 호출 시 예)#aa,#bb 단건일 경우 기존과 동일 combo와 갯수 맞출것
 * @param condition : 이외 조건값처리(기본 쿼리스트링) - 없으면 값을 넘기지 않아도 됨 (단, val없이 condition이 존재할 수는 없음)
 */
async function callAjaxComboCode(target, data, combo, type, val, multiYn) {
  try {
    const response = await fetch(target, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: new URLSearchParams(data),
      cache: "no-cache",
    });

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    const data = await response.json();

    if (data.resultCode === "F001") {
      location.replace("/login");
    }

    const cnt = data.length;
    const comboArray = combo.split(",");

    comboArray.forEach((comboItem) => {
      const selectElement = document.querySelector(comboItem);
      if (cnt > 0) {
        if (type === "1") {
          selectElement.innerHTML = '<option value="">선 택</option>';
        } else if (type === "2") {
          selectElement.innerHTML = '<option value="">전 체</option>';
        }

        data.forEach((item) => {
          const valArray = val.split(",");
          valArray.forEach((valItem) => {
            if (valItem === item.code) {
              selectElement.innerHTML += `<option value="${item.code}" selected="selected">${item.codeNm}</option>`;
            } else {
              selectElement.innerHTML += `<option value="${item.code}">${item.codeNm}</option>`;
            }
          });
        });
      }
    });
  } catch (error) {
    swal("실패", "작업수행에 실패하였습니다.", "error");
  }
}

function ConfirmdialogToAjax(text, target, form, callback) {
  if (text == "create") {
    text = "등록하시겠습니까?";
  } else if (text == "update") {
    text = "수정하시겠습니까?";
  } else if (text == "delete") {
    text = "삭제하시겠습니까?";
  } else if (text == "execute") {
    text = "실행하시겠습니까?";  
  }
  Swal.fire({
    title: text,
    showCancelButton: true,
    icon: "question",
    confirmButtonColor: "#DD6B55",
    confirmButtonText: "예",
    cancelButtonText: "아니요",
  }).then(async (result) => {
    if (result.isDismissed) return;
    LoadingBar.show("처리 중입니다...");
    try {
      const response = await fetch(target, {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: new URLSearchParams(form),
        cache: "no-cache",
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const data = await response.json();

      if (data.resultCode === "F001") {
        location.replace("/login");
      } else if (data.resultCode === "S000") {
        // alert("처리되었습니다.");
        callback(data);
      
      } else {
        swal("실패", data.resultMsg, "error");
      }
    } catch (error) {
      swal("실패", "작업수행에 실패하였습니다.", "error");
      console.error("Detailed error:", {
        message: error.message,
        stack: error.stack,
        name: error.name
      });
      throw error;
    } finally {
      LoadingBar.hide();
    }
    window.onkeydown = null;
    window.onfocus = null;
  });
}

HTMLFormElement.prototype.serializeObject = function () {
  const obj = {};
  const formData = new FormData(this);
  formData.forEach((value, key) => {
    obj[key] = value;
  });
  return obj;
};

HTMLFormElement.prototype.amountInput = function () {
  this.addEventListener("keypress", (e) => {
    if (e.which && (e.which < 48 || e.which > 57) && e.which > 31) {
      e.preventDefault();
    }
  });
  this.addEventListener("keyup", function () {
    if (this.value !== null && this.value !== "") {
      const tmps = this.value.replace(/[^0-9]/g, "");
      const tmps2 = tmps.replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
      this.value = tmps2;
    }
  });
  this.value = (() => {
    if (this.value !== null && this.value !== "") {
      const tmps = this.value.replace(/[^0-9]/g, "");
      const tmps2 = tmps.replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
      return tmps2;
    }
  })();

  this.style.imeMode = "disabled";
};

HTMLFormElement.prototype.clearForm = function () {
  var tabindex = 1;
  this.each(function () {
    this.reset();
  });
  this.querySelectorAll("input, select").forEach((element) => {
    if (element.type !== "hidden") {
      element.setAttribute("tabindex", tabindex);
      tabindex++;
    }
  });
};
