// var SELL_RATE = 0.9;//最多只能交易90%的货币 tradableCoin
var ENCASH_RATE = 0.9;//提现90%的财富积分 cashCoin, 10% :shopPoints
var ENCASH_FEE = 0.08; //提现手续费

function displayMonthDate(value,row,index) {
    if(value) {
        var date = new Date(value);
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var hours = date.getHours();
        hours = hours > 9 ? hours:('0'+hours);
        var minutes = date.getMinutes();
        minutes = minutes > 9 ? minutes:('0'+minutes);
        var seconds = date.getSeconds();
        seconds = seconds > 9 ? seconds:('0'+seconds);
        //date.getFullYear() + seperator1 +
        var currentdate = month + seperator1 + strDate
            + " " + hours + seperator2 + minutes
            + seperator2 + seconds;
        return currentdate;
    }else {
        return value;
    }
}
function displayCreatedDate(value,row,index) {
    if(value) {
        var date = new Date(value);
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var hours = date.getHours();
        hours = hours > 9 ? hours:('0'+hours);
        var minutes = date.getMinutes();
        minutes = minutes > 9 ? minutes:('0'+minutes);
        var seconds = date.getSeconds();
        seconds = seconds > 9 ? seconds:('0'+seconds);
        var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + hours + seperator2 + minutes
            + seperator2 + seconds;
        return currentdate;
    }else {
        return value;
    }
}

function showErrorAlert(text, refreshFlag) {
    $('#alertModalError').modal('show');
    $('#alertModalError .modal-body').text(text);
    setTimeout(function(){$("#alertModalError").modal("hide")},3000);
    if(refreshFlag && refreshFlag == 2) {
        return;
    }else {
        setTimeout("location.reload()",3000);
    }
}
function showSuccessAlert(text, refreshFlag) {//2不刷新
    $('#alertModalSuccess').modal('show');
    $('#alertModalSuccess .modal-body').text(text);
    setTimeout(function(){$("#alertModalSuccess").modal("hide")},2000);
    if(refreshFlag && refreshFlag == 2) {
        return;
    }else {
        setTimeout("location.reload()",2000);
    }
}
var $alert = $('.myAlert').hide();
function showAlert(title, type) { //type= success / danger
    $alert.attr('class', 'alert myAlert alert-' + type || 'success')
        .html('<i class="glyphicon glyphicon-check"></i> ' + title).show();
    setTimeout(function () {
        $alert.hide();
    }, 3000);
}

function checkTradeTime(flag) {
    if(!flag) {
        var now = new Date();
        if (now.getHours() < 9 || now.getHours() > 20 || (now.getHours() == 20 && now.getMinutes() > 30)) {
            alert("不在交易时间，交易时间：9:00-20:30");
            return false;
        }
    }
    return true;
}


//发送验证码倒计时
var countdown=120;
function showCountdownTime(obj) { //发送验证码倒计时
    if (countdown == 0) {
        obj.attr('disabled',false);
        obj.text("获取验证码");
        countdown = 120;
        return;
    } else {
        obj.attr('disabled',true);
        obj.text("重新发送(" + countdown + "s)");
        countdown--;
    }
    setTimeout(function() {
            showCountdownTime(obj) }
        ,1000)
}
function resetCountdown() {
    var obj = $("#modalCountdown");
    obj.attr('disabled',false);
    obj.text("获取验证码");
    countdown = 0;
}


Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    var week = {
        "0": "/u65e5",
        "1": "/u4e00",
        "2": "/u4e8c",
        "3": "/u4e09",
        "4": "/u56db",
        "5": "/u4e94",
        "6": "/u516d"
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    if (/(E+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "/u661f/u671f" : "/u5468") : "") + week[this.getDay() + ""]);
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
}

//form data to json
function getFormData($form) {
    var unindexed_array = $form.serializeArray();
    var indexed_array = {};

    $.map(unindexed_array, function (n, i) {
        indexed_array[n['name']] = n['value'];
    });

    return indexed_array;
}

function showLoadingModal() {
    $('#loadingModal').modal('show');
}

function hideLoadingModal() {
    $('#loadingModal').modal('hide');
}
