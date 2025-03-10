jQuery(function ($) {
  flatpickr.localize(flatpickr.l10ns.ko);
  flatpickr('mySelector');
  $.fn.serializeObject = function () {
    var obj = null;
    try {
      if (this[0].tagName && this[0].tagName.toUpperCase() == 'FORM') {
        var arr = this.serializeArray();
        if (arr) {
          obj = {};
          jQuery.each(arr, function () {
            obj[this.name] = this.value;
          });
        } // if ( arr ) {
      }
    } catch (e) {
      alert(e.message);
    } finally {
    }

    return obj;
  };
  $.fn.amountInput = function () {
    $(this)
      .keypress(function (e) {
        if (e.which && (e.which < 48 || e.which > 57) && e.which > 31) {
          e.preventDefault();
        }
      })
      .keyup(function () {
        if ($(this).val() != null && $(this).val() != '') {
          var tmps = $(this)
            .val()
            .replace(/[^0-9]/g, '');
          var tmps2 = tmps.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
          $(this).val(tmps2);
        }
      })
      .val(function () {
        if ($(this).val() != null && $(this).val() != '') {
          var tmps = $(this)
            .val()
            .replace(/[^0-9]/g, '');
          var tmps2 = tmps.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
          return tmps2;
        }
      })
      .attr('style', 'ime-mode:disabled');
  };

  $.fn.clearForm = function () {
    var tabindex = 1;
    $(this).each(function () {
      this.reset();
    });
    $(this)
      .find('input,select')
      .each(function () {
        if (this.type != 'hidden') {
          $(this).attr('tabindex', tabindex);
          tabindex++;
        }
      });
  };

  $.fn.validating = function () {
    var result = 0;
    $(':input', this).change(function () {
      if (this.name != '') {
        if ($(this).attr('required') == 'required') {
          if ($(this).val() == '') {
            $(this)
              .parent()
              .append("<label id='" + this.id + "_error' class='error' for='" + this.name + "'>" + $(this).attr('alt') + '는(은) 필수 입력 항목입니다.</label>');
            $(this).focus();
            result = 1;
          }
        }
      }
    });
    if (result == 1) {
      return false;
    } else {
      return true;
    }
  };
  $.fn.autoMapping = function (data) {
    if (data) {
      $(':input', this).each(function () {
        var name = this.name;
        var type = this.type;
        if (data[name]) {
          if (type == 'select-one') {
            $(this).val(data[name]);
          } else if (type == 'radio') {
            $('input:radio[name=' + name + ']:input[value=' + data[name] + ']').attr('checked', true);
          } else if (type == 'checkbox') {
            $('input:checkbox[name=' + name + ']:input[value=' + data[name] + ']').attr('checked', true);
          } else {
            $(this).val(data[name]);
          }
        }
      });
    }
  };
  $.fn.onLoadFunction = function () {
    $(this).each(function () {
      $(this).find("input[istyle='time']").flatpickr({
        enableTime: true,
        noCalendar: true,
        dateFormat: 'H:i',
      });
      $(this).find("input[istyle='clock']").flatpickr({
        enableTime: true,
        noCalendar: true,
        dateFormat: 'H:i',
      });
      $(this)
        .find("input[istyle='date']")
        .blur(function () {
          var date = $(this).val().replace(/-/gi, '');
          var picker = $(this);
          var leng = date.length;
          if (!isValidDate(date)) {
            alert('유효하지않은 날짜입니다.');
            $(picker).val('');
            $(picker).focus();
          }
          if (date != '') {
            if (leng == 8) {
              date = date.substr(0, 4) + '-' + date.substr(4, 2) + '-' + date.substr(6, 2);
              if (date.substr(4, 2) > 12 || date.substr(6, 2) > 31) {
                $(this).focus();
              } else {
                $(this).val(date);
              }
            } else {
              $(this).focus();
            }
          }
        })
        .focus(function () {
          var date = $(this).val();
          var date2 = date.replace(/-/gi, '');
          $(this).val(date2);
        })
        .val(function () {
          var returnVal = $(this).val();
          if (returnVal == '') {
            var dfval = $(this).attr('dfval');
            if (dfval) {
              if (dfval == 'today') {
                var now = new Date();
                var year = now.getFullYear();
                var mon = addZero(eval(now.getMonth() + 1));
                var day = addZero(now.getDate());
                var chan_val = year + '-' + mon + '-' + day;
                returnVal = chan_val;
              } else if (dfval == 'weekStart') {
                var now = new Date();
                var nowDayOfWeek = now.getDay();
                var nowDay = now.getDate();
                var nowMonth = now.getMonth();
                var nowYear = now.getYear();
                nowYear += nowYear < 2000 ? 1900 : 0;
                var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);

                returnVal = converDateString(weekStartDate);
              } else if (dfval == 'weekEnd') {
                var now = new Date();
                var nowDayOfWeek = now.getDay();
                var nowDay = now.getDate();
                var nowMonth = now.getMonth();
                var nowYear = now.getYear();
                nowYear += nowYear < 2000 ? 1900 : 0;
                var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek));
                returnVal = converDateString(weekEndDate);
              } else {
                var newDt = new Date();
                if (dfval.substr(0, 1) == '-') {
                  if (dfval.length == 2) {
                    newDt.setDate(newDt.getDate() - dfval.substr(1, 2));
                  } else if (dfval.length == 4) {
                    newDt.setDate(newDt.getDate() - dfval.substr(1, 4));
                  } else if (dfval.length == 3) {
                    newDt.setDate(newDt.getDate() - dfval.substr(1, 3));
                  }
                } else {
                  newDt.setDate(newDt.getDate() + Number(dfval));
                }
                returnVal = converDateString(newDt);
              }
            }
          } else {
            var leng = returnVal.length;
            if (leng == 8) {
              returnVal = returnVal.substr(0, 4) + '-' + returnVal.substr(4, 2) + '-' + returnVal.substr(6, 2);
            }
          }
          return returnVal;
        })
        .keypress(function (e) {
          if (e.which && (e.which < 48 || e.which > 57) && e.which > 31) {
            e.preventDefault();
          }
          if ($(this).val().length >= 8) e.preventDefault();
        })
        .attr('style', 'ime-mode:disabled')
        .attr('maxlength', '10')
        .wrap('<div class="input-group input-group-sm">')
        .after("<span class='input-group-text'><i class='fa fa-calendar'></i></span>")
        .flatpickr({
          disableMobile: true,
          monthSelectorType: 'static',
          yearSelectorType: 'static',
        });
      $(this)
        .find("input[istyle='date2']")
        .blur(function () {
          var date = $(this).val().replace(/-/gi, '');
          var picker = $(this);
          var leng = date.length;
          if (!isValidDate(date)) {
            alert('유효하지않은 날짜입니다.');
            $(picker).val('');
            $(picker).focus();
          }
          if (date != '') {
            if (leng == 8) {
              date = date.substr(0, 4) + '-' + date.substr(4, 2) + '-' + date.substr(6, 2);
              if (date.substr(4, 2) > 12 || date.substr(6, 2) > 31) {
                $(this).focus();
              } else {
                $(this).val(date);
              }
            } else {
              $(this).focus();
            }
          }
        })
        .focus(function () {
          var date = $(this).val();
          var date2 = date.replace(/-/gi, '');
          $(this).val(date2);
        })
        .val(function () {
          var returnVal = $(this).val();
          if (returnVal == '') {
            var dfval = $(this).attr('dfval');
            if (dfval) {
              if (dfval == 'today') {
                var now = new Date();
                var year = now.getFullYear();
                var mon = addZero(eval(now.getMonth() + 1));
                var day = addZero(now.getDate());
                var chan_val = year + '-' + mon + '-' + day;
                returnVal = chan_val;
              } else if (dfval == 'weekStart') {
                var now = new Date();
                var nowDayOfWeek = now.getDay();
                var nowDay = now.getDate();
                var nowMonth = now.getMonth();
                var nowYear = now.getYear();
                nowYear += nowYear < 2000 ? 1900 : 0;
                var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);

                returnVal = converDateString(weekStartDate);
              } else if (dfval == 'weekEnd') {
                var now = new Date();
                var nowDayOfWeek = now.getDay();
                var nowDay = now.getDate();
                var nowMonth = now.getMonth();
                var nowYear = now.getYear();
                nowYear += nowYear < 2000 ? 1900 : 0;
                var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek));
                returnVal = converDateString(weekEndDate);
              } else {
                var newDt = new Date();
                if (dfval.substr(0, 1) == '-') {
                  if (dfval.length == 2) {
                    newDt.setDate(newDt.getDate() - dfval.substr(1, 2));
                  } else if (dfval.length == 4) {
                    newDt.setDate(newDt.getDate() - dfval.substr(1, 4));
                  } else if (dfval.length == 3) {
                    newDt.setDate(newDt.getDate() - dfval.substr(1, 3));
                  }
                } else {
                  newDt.setDate(newDt.getDate() + Number(dfval));
                }
                returnVal = converDateString(newDt);
              }
            }
          } else {
            var leng = returnVal.length;
            if (leng == 8) {
              returnVal = returnVal.substr(0, 4) + '-' + returnVal.substr(4, 2) + '-' + returnVal.substr(6, 2);
            }
          }
          return returnVal;
        })
        .keypress(function (e) {
          if (e.which && (e.which < 48 || e.which > 57) && e.which > 31) {
            e.preventDefault();
          }
          if ($(this).val().length >= 8) e.preventDefault();
        })
        .attr('style', 'ime-mode:disabled')
        .attr('maxlength', '10')
        .wrap('<div class="input-group input-group-sm" style="width: 40%;float: left;"> ')
        .after("<span class='input-group-text'><i class='fa fa-calendar'></i></span>")
        .flatpickr({
          disableMobile: true,
        });
      $(this)
        .find("input[istyle='amount']")
        .keypress(function (e) {
          if (e.which && (e.which < 48 || e.which > 57) && e.which > 31) {
            e.preventDefault();
          }
        })
        .keyup(function () {
          if ($(this).val() != null && $(this).val() != '') {
            var tmps = $(this)
              .val()
              .replace(/[^0-9]/g, '');
            var tmps2 = tmps.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
            $(this).val(tmps2);
          }
        })
        .val(function () {
          if ($(this).val() != null && $(this).val() != '') {
            var tmps = $(this)
              .val()
              .replace(/[^0-9]/g, '');
            var tmps2 = tmps.replace(/(\d)(?=(\d\d\d)+(?!\d))/g, '$1,');
            return tmps2;
          }
        })
        .css({
          style: 'ime-mode:disabled',
        })
        .wrap('<div class="input-group input-group-sm"> ')
        .before("<span class='input-group-text'><i class='fa fa-krw'></i></span>");

      $(this)
        .find("input[istyle='number']")
        .keypress(function (e) {
          if (e.which && (e.which < 48 || e.which > 57) && e.which > 31) {
            e.preventDefault();
          }
        })
        .css({
          style: 'ime-mode:disabled',
        });

      $(this)
        .find("input[istyle='float']")
        .keypress(function (e) {
          if (!(e.which >= 48 && e.which <= 57) && e.which != 46) {
            e.preventDefault();
          }
        })
        .blur(function () {
          var fVal = $(this).val();
          if (!isValidFloat(fVal)) {
            alert('유효하지않은 형식입니다.\nEX) 000.00');
            $(this).focus();
            eventObject.stopPropagation();
          }
        })
        .css({
          style: 'ime-mode:disabled',
        });
      $(this)
        .find("input[istyle='phone']")
        .keyup(function () {
          var val = $(this)
            .val()
            .replace(/[^0-9]/g, '');
          if (val.length > 3 && val.length < 6) {
            var tmp = val.substring(0, 2);
            if (tmp == '02') {
              $(this).val(val.substring(0, 2) + '-' + val.substring(2));
            } else {
              $(this).val(val.substring(0, 3) + '-' + val.substring(3));
            }
          } else if (val.length > 6) {
            var tmp = val.substring(0, 2);
            var tmp2 = val.substring(0, 4);
            if (tmp == '02') {
              if (val.length == '10') {
                $(this).val(val.substring(0, 2) + '-' + val.substring(2, 6) + '-' + val.substring(6));
              } else {
                $(this).val(val.substring(0, 2) + '-' + val.substring(2, 5) + '-' + val.substring(5));
              }
            } else if (tmp2 == '0505') {
              if (val.length == '12') {
                $(this).val(val.substring(0, 4) + '-' + val.substring(4, 8) + '-' + val.substring(8));
              } else {
                $(this).val(val.substring(0, 4) + '-' + val.substring(4, 7) + '-' + val.substring(7));
              }
            } else {
              if (val.length == '11') {
                $(this).val(val.substring(0, 3) + '-' + val.substring(3, 7) + '-' + val.substring(7));
              } else {
                $(this).val(val.substring(0, 3) + '-' + val.substring(3, 6) + '-' + val.substring(6));
              }
            }
          }
        })
        .keypress(function (e) {
          if (e.which && (e.which < 48 || e.which > 57) && e.which > 31) {
            e.preventDefault();
          }
        })
        .val(function () {
          var val = $(this)
            .val()
            .replace(/[^0-9]/g, '');
          if (val.length > 3 && val.length < 6) {
            var tmp = val.substring(0, 2);
            if (tmp == '02') {
              return val.substring(0, 2) + '-' + val.substring(2);
            } else {
              return val(val.substring(0, 3) + '-' + val.substring(3));
            }
          } else if (val.length > 6) {
            var tmp = val.substring(0, 2);
            var tmp2 = val.substring(0, 4);
            if (tmp == '02') {
              if (val.length == '10') {
                return val.substring(0, 2) + '-' + val.substring(2, 6) + '-' + val.substring(6);
              } else {
                return val.substring(0, 2) + '-' + val.substring(2, 5) + '-' + val.substring(5);
              }
            } else if (tmp2 == '0505') {
              if (val.length == '12') {
                return val.substring(0, 4) + '-' + val.substring(4, 8) + '-' + val.substring(8);
              }
            } else {
              if (val.length == '11') {
                return val.substring(0, 3) + '-' + val.substring(3, 7) + '-' + val.substring(7);
              } else {
                return val.substring(0, 3) + '-' + val.substring(3, 6) + '-' + val.substring(6);
              }
            }
          }
        })
        .wrap('<div class="input-group input-group-sm"> ')
        .before("<span class='input-group-text'><i class='fa fa-phone'></i></span>");
      $(this).find("input[istyle='email']").wrap('<div class="input-group input-group-sm"> ').before("<span class='input-group-text'><i class='fa fa-envelope'></i></span>");
    });
  };
  $.fn.rowspan = function (colIdx, isStats) {
    return this.each(function () {
      var that;
      $('tr', this).each(function (row) {
        $('td:eq(' + colIdx + ')', this)
          .filter(':visible')
          .each(function (col) {
            if ($(this).html() == $(that).html() && (!isStats || (isStats && $(this).prev().html() == $(that).prev().html()))) {
              rowspan = $(that).attr('rowspan') || 1;
              rowspan = Number(rowspan) + 1;

              $(that).attr('rowspan', rowspan);
              $(this).hide();
            } else {
              that = this;
            }

            that = that == null ? this : that;
          });
      });
    });
  };
});

function converDateString(dt) {
  return dt.getFullYear() + '-' + addZero(eval(dt.getMonth() + 1)) + '-' + addZero(dt.getDate());
}

function addZero(i) {
  var rtn = i + 100;
  return rtn.toString().substr(1, 3);
}

function isValidDate(dateStr) {
  if (dateStr) {
    dateStr = dateStr.replace(/-/gi, '');
    var year = Number(dateStr.substr(0, 4));
    var month = Number(dateStr.substr(4, 2));
    var day = Number(dateStr.substr(6, 2));
    if (month < 1 || month > 12) {
      return false;
    }
    if (day < 1 || day > 31) {
      return false;
    }
    if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
      return false;
    }
    if (month == 2) {
      var isleap = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
      if (day > 29 || (day == 29 && !ispleap)) {
        return false;
      }
    }
  }
  return true;
}

function isValidFloat(floatVal) {
  if (floatVal.indexOf('.') >= 0) {
    var aryVal = floatVal.split('.');
    if (aryVal[0].length > 3 || aryVal[1].length > 2) {
      return false;
    }
  } else {
    if (floatVal.length > 3) {
      return false;
    }
  }
  return true;
}
$.fn.elmentsDisabled = function (disabled) {
  $(':input', this).each(function () {
    $(this).attr('disabled', disabled);
    if (disabled) {
      $(this).css('background-color', '#EFEFEF');
    } else {
      $(this).css('background-color', '#FFFFFF');
    }
  });
};

function elmentsDisabled(disabled, id) {
  var elementArr = ['input', 'select', 'checkbox', 'textarea', 'radio'];
  $(elementArr).each(function (idx, element) {
    $('#' + id + ' ' + element).each(function () {
      $(this).attr('disabled', disabled);
    });
  });
}
function addComma(value, row, index) {
  var tmpH = '';
  if (value) {
    value += '';
    if (value.charAt(0) == '-') {
      tmpH = value.substring(0, 1);
      value = value.substring(1, value.length);
    }
    tmp = value.split('.');
    var str = new Array();
    var v = tmp[0].replace(/,/gi, '');
    for (var i = 0; i <= v.length; i++) {
      str[str.length] = v.charAt(v.length - i);
      if (i % 3 == 0 && i != 0 && i != v.length) {
        str[str.length] = '.';
      }
    }
    str = str.reverse().join('').replace(/\./gi, ',');
    return tmp.length == 2 ? tmpH + str + '.' + tmp[1] : tmpH + str;
  } else {
    return tmpH + 0;
  }
}
function addPercent(value, row, index) {
  if (value) {
    value += '%';
    return value;
  } else {
    return value;
  }
}

function setPhone(newValue) {
  var val = newValue;
  if (val.length > 3 && val.length < 6) {
    var tmp = val.substring(0, 2);
    if (tmp == '02') {
      return val.substring(0, 2) + '-' + val.substring(2);
    } else {
      return val.substring(0, 3) + '-' + val.substring(3);
    }
  } else if (val.length > 6) {
    var tmp = val.substring(0, 2);
    var tmp2 = val.substring(0, 4);
    if (tmp == '02') {
      if (val.length == '10') {
        return val.substring(0, 2) + '-' + val.substring(2, 6) + '-' + val.substring(6);
      } else {
        return val.substring(0, 2) + '-' + val.substring(2, 5) + '-' + val.substring(5);
      }
    } else if (tmp2 == '0505') {
      if (val.length == '12') {
        return val.substring(0, 4) + '-' + val.substring(4, 8) + '-' + val.substring(8);
      }
    } else {
      if (val.length == '11') {
        return val.substring(0, 3) + '-' + val.substring(3, 7) + '-' + val.substring(7);
      } else {
        return val.substring(0, 3) + '-' + val.substring(3, 6) + '-' + val.substring(6);
      }
    }
  }
}
function returnDate(dfval) {
  if (dfval == 'today') {
    var now = new Date();
    var year = now.getFullYear();
    var mon = addZero(eval(now.getMonth() + 1));
    var day = addZero(now.getDate());
    var chan_val = year + '-' + mon + '-' + day;
    returnVal = chan_val;
  } else if (dfval == 'weekStart') {
    var now = new Date();
    var nowDayOfWeek = now.getDay();
    var nowDay = now.getDate();
    var nowMonth = now.getMonth();
    var nowYear = now.getYear();
    nowYear += nowYear < 2000 ? 1900 : 0;
    var weekStartDate = new Date(nowYear, nowMonth, nowDay - nowDayOfWeek);

    returnVal = converDateString(weekStartDate);
  } else if (dfval == 'weekEnd') {
    var now = new Date();
    var nowDayOfWeek = now.getDay();
    var nowDay = now.getDate();
    var nowMonth = now.getMonth();
    var nowYear = now.getYear();
    nowYear += nowYear < 2000 ? 1900 : 0;
    var weekEndDate = new Date(nowYear, nowMonth, nowDay + (6 - nowDayOfWeek));
    returnVal = converDateString(weekEndDate);
  } else if (dfval == 'yesterday') {
    var now = new Date();
    var year = now.getFullYear();
    var mon = addZero(eval(now.getMonth() + 1));
    var day = addZero(now.getDate() - 1);
    var chan_val = year + '-' + mon + '-' + day;
    returnVal = chan_val;
  } else if (dfval == 'weekAgo') {
    var weekAgo = new Date(Date.parse(new Date()) - 7 * 1000 * 60 * 60 * 24);
    var year = weekAgo.getFullYear();
    var mon = weekAgo.getMonth() + 1;
    var day = weekAgo.getDate();
    var chan_val = year + '-' + mon + '-' + day;
    returnVal = chan_val;
  } else if (dfval == 'monthStart') {
    var now = new Date();
    var year = now.getFullYear();
    var mon = addZero(eval(now.getMonth() + 1));
    var day = addZero(1);
    var chan_val = year + '-' + mon + '-' + day;
    returnVal = chan_val;
  } else if (dfval == 'monthEnd') {
    var now = new Date();
    var year = now.getFullYear();
    var mon = addZero(eval(now.getMonth() + 1));
    var day = new Date(now.getYear(), now.getMonth() + 1, '').getDate();
    var chan_val = year + '-' + mon + '-' + day;
    returnVal = chan_val;
  } else if (dfval == 'thirtyDayAgo') {
    var now = new Date();
    now.setDate(now.getDate() - 30);
    var year = now.getFullYear();
    var mon = addZero(eval(now.getMonth() + 1));
    var day = addZero(now.getDate());
    var chan_val = year + '-' + mon + '-' + day;
    returnVal = chan_val;
  } else if (dfval == 'hundredDayAgo') {
    var now = new Date();
    now.setDate(now.getDate() - 100);
    var year = now.getFullYear();
    var mon = addZero(eval(now.getMonth() + 1));
    var day = addZero(now.getDate());
    var chan_val = year + '-' + mon + '-' + day;
    returnVal = chan_val;
  } else {
    var newDt = new Date();
    if (dfval.substr(0, 1) == '-') {
      if (dfval.length == 2) {
        newDt.setDate(newDt.getDate() - dfval.substr(1, 2));
      } else if (dfval.length == 4) {
        newDt.setDate(newDt.getDate() - dfval.substr(1, 4));
      } else if (dfval.length == 3) {
        newDt.setDate(newDt.getDate() - dfval.substr(1, 3));
      }
    } else {
      newDt.setDate(newDt.getDate() + Number(dfval));
    }
    returnVal = converDateString(newDt);
  }

  return returnVal;
}
function codeSetting(group, data, combo, type, val, multiYn) {
  var cnt = data.length;
  var comboArray = combo.split(',');
  for (var k in comboArray) {
    if (cnt > 0) {
      if (type == '1') {
        $(comboArray[k]).children().remove();
        $(comboArray[k]).append('<option value="">선 택</option>');
      } else if (type == '2') {
        $(comboArray[k]).children().remove();
        $(comboArray[k]).append('<option value="">전 체</option>');
      } else {
        $(comboArray[k]).children().remove();
      }
      for (var i = 0; i < cnt; i++) {
        var valArray = val.split(',');
        if (data[i].group_code == group)
          for (var l in valArray) {
            if (valArray[l] == data[i].code) {
              var appendText = "<option value='" + data[i].code + '\' selected="selected">' + data[i].codeNm + '</option>';
            } else {
              var appendText = "<option value='" + data[i].code + "'>" + data[i].codeNm + '</option>';
            }
            $(comboArray[k]).append(appendText);
          }
      }
      if (multiYn == 'Y') {
        $(comboArray[k]).attr('multiple', 'multiple');
        if (type == '1') {
          $(comboArray[k]).attr('data-placeholder', '선 택');
        } else if (type == '2') {
          $(comboArray[k]).attr('data-placeholder', '전 체');
        }
        $(comboArray[k]).chosen({});
        $('.search-choice-close').click();
      }
    } else {
      $(comboArray[k]).children().remove();
      $(comboArray[k]).append('<option value="">데이터가 없습니다</option>');
    }
  }
}
function checkCodeSetting(group, data, combo, type) {
  var cnt = data.length;
  var comboArray = combo.split(',');
  for (var k in comboArray) {
    if (cnt > 0) {
      if (type == 'radio') {
        for (var i = 0; i < cnt; i++) {
          if (data[i].group_code == group) {
            var appendText = "<label class='checkbox-inline i-checks'><input type='radio' value='" + data[i].code + "' name='" + $(comboArray[k]).attr('id') + "' id='" + $(comboArray[k]).attr('id') + "'> " + data[i].codeNm + '</label>';
            $(comboArray[k]).append(appendText);
          }
        }
      } else if (type == 'check') {
        for (var i = 0; i < cnt; i++) {
          if (data[i].group_code == group) {
            var appendText = "<label class='checkbox-inline i-checks'><input type='checkbox' value='" + data[i].code + "' name='" + $(comboArray[k]).attr('id') + "' id='" + $(comboArray[k]).attr('id') + "'> " + data[i].codeNm + '</label>';
            $(comboArray[k]).append(appendText);
          }
        }
      }
      $('.i-checks').iCheck({
        checkboxClass: 'icheckbox_square-green',
        radioClass: 'iradio_square-green',
      });
    } else {
      $(comboArray[k]).append('데이터가 없습니다');
    }
  }
}
function checkCodeSetting2(group, data, combo, type) {
  var cnt = data.length;
  var comboArray = combo.split(',');
  for (var k in comboArray) {
    var appendText = '';
    if (cnt > 0) {
      if (type == 'radio') {
        for (var i = 0; i < cnt; i++) {
          if (data[i].group_code == group) {
            appendText += "<label class='radio-inline'><input type='radio' value='" + data[i].code + "' name='" + $(comboArray[k]).attr('id') + "' id='" + $(comboArray[k]).attr('id') + "'>" + data[i].codeNm + ' </label>';
          }
        }
      } else if (type == 'check') {
        for (var i = 0; i < cnt; i++) {
          if (data[i].group_code == group) {
            appendText += "<label class='checkbox-inline'><input type='checkbox' value='" + data[i].code + "' name='" + $(comboArray[k]).attr('id') + "' id='" + $(comboArray[k]).attr('id') + "'>" + data[i].codeNm + ' </label>';
          }
        }
      }
      $(comboArray[k]).append(appendText);
    } else {
      $(comboArray[k]).append('데이터가 없습니다');
    }
  }
}
// 문자열 체크 startsWith
if (typeof String.prototype.startsWith != 'function') {
  String.prototype.startsWith = function (str) {
    return this.substring(0, str.length) === str;
  };
}

// 문자열 체크 endsWith
if (typeof String.prototype.endsWith != 'function') {
  String.prototype.endsWith = function (str) {
    return this.substring(this.length - str.length, this.length) === str;
  };
}

function codeSetting(group, data, combo, type, val, multiYn) {
  var cnt = data.length;
  var comboArray = combo.split(',');
  for (var k in comboArray) {
    if (cnt > 0) {
      if (type == '1') {
        $(comboArray[k]).children().remove();
        $(comboArray[k]).append('<option value="">선 택</option>');
      } else if (type == '2') {
        $(comboArray[k]).children().remove();
        $(comboArray[k]).append('<option value="">전 체</option>');
      } else {
        $(comboArray[k]).children().remove();
      }
      for (var i = 0; i < cnt; i++) {
        var valArray = val.split(',');
        if (data[i].group_code == group)
          for (var l in valArray) {
            if (valArray[l] == data[i].code) {
              var appendText = "<option value='" + data[i].code + '\' selected="selected">' + data[i].codeNm + '</option>';
            } else {
              var appendText = "<option value='" + data[i].code + "'>" + data[i].codeNm + '</option>';
            }
            $(comboArray[k]).append(appendText);
          }
      }
      if (multiYn == 'Y') {
        $(comboArray[k]).attr('multiple', 'multiple');
        if (type == '1') {
          $(comboArray[k]).attr('data-placeholder', '선 택');
        } else if (type == '2') {
          $(comboArray[k]).attr('data-placeholder', '전 체');
        }
        $(comboArray[k]).chosen({});
        $('.search-choice-close').click();
      }
    } else {
      $(comboArray[k]).children().remove();
      $(comboArray[k]).append('<option value="">데이터가 없습니다</option>');
    }
  }
}
