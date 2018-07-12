
$(function(){
    if(/Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {//手机端
        $('#sellTable').bootstrapTable('hideColumn', 'orderDate');
        $('#sellTable').bootstrapTable('hideColumn', 'id');
        $('#buyTable').bootstrapTable('hideColumn', 'orderDate');
        $('#buyTable').bootstrapTable('hideColumn', 'id');
        $('div.sellTable.col-xs-6').css('padding-left', '0px');
        $('div.sellTable.col-xs-6').css('padding-right', '0px');
        $('div.buyTable.col-xs-6').css('padding-left', '0px');
        $('div.buyTable.col-xs-6').css('padding-right', '0px');
        $('#tableDiv .box.box-body').css('padding-left', '0px');
        $('#tableDiv .box.box-body').css('padding-right', '0px');
        $('.fixed-table-container').css('font-size', '12px');
        $('#tableDiv').css('padding-left', '0');
        $('#tableDiv').css('padding-right', '0');
        $('.col-xs-12').css('padding-left', '0');
        $('.col-xs-12').css('padding-right', '0');

    } else {//电脑端
        //去掉tab
        $('ul.nav.nav-tabs').remove();
        $('#myTabContent').removeClass('tab-content');
        $('#sellTab').removeClass('tab-pane');
        $('#sellTab').removeClass('fade');
        $('#buyTab').removeClass('tab-pane');
        $('#buyTab').removeClass('fade');
    }
});

var $sellTable = $('#sellTable').bootstrapTable({
    title:"挂卖列表",
    url: "buySellList",
    method: 'post',
    contentType: "application/x-www-form-urlencoded",
    cache: false,
    sidePagination:'server',//指定服务器端分页
    queryParams:function (params) {  //请求服务器时所传的参数
        return {
            type: "sell",
        }
    },
});

var $buyTable = $('#buyTable').bootstrapTable({
    url: "buySellList",
    method: 'post',
    contentType: "application/x-www-form-urlencoded",
    sidePagination:'server',//指定服务器端分页
    queryParams:function (params) {  //请求服务器时所传的参数
        return {
            type: "buy",
        }
    },
    cache: false,
});

$('#wantToSellForm').bootstrapValidator({
    message: '输入错误',
    feedbackIcons: {
        valid: 'glyphicon glyphicon-ok',
        invalid: 'glyphicon glyphicon-remove',
        validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
        amount: {
            validators: {
                notEmpty: {
                    message: '请输入挂卖数量'
                },
                // regexp: {
                //     regexp:/^[0-9]*$/,
                //     message: '请输入正确数量'
                // },
                /*greaterThan: {
                    message: '挂买数量最小为10',
                    value: 10
                },*/
                callback: {
                    message: '数量错误',
                    callback:  function(value, validator) {
                        if( parseFloat(value )> parseFloat($('input[name=tradableCoin]').val())) {//*MAX_SELL_RATE
                            return {
                                valid: false,
                                message: '挂卖数量过大，KTH余额不足'
                            };
                        }else
                            if(!(value >= 10 && value % 10 == 0)){
                            return {
                                valid: false,
                                message: '挂卖数量必须是10的整数倍'
                            };
                        }else {
                            return true;
                        }
                    }
                }
            }
        },
        vc: {
            validators: {
                notEmpty: {
                    message: '请输入挂卖单价'
                },
                regexp: {
                    regexp:/(^[1-9]([0-9]+)?(\.[0-9]{1,3})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
                    message: '请输入正确数值'
                },
            }
        },
        tradePassword: {
            validators: {
                notEmpty: {
                    message: '请输入交易密码'
                },
                /*remote: {
                    url: '/isTradePwdCorrect',
                    delay: 1000,
                    type: 'POST',
                    message: '交易密码错误',
                    data: {
                        originPwd: function () {
                            return $('input[name=tradePassword]').val();
                        }
                    }
                },*/
            }
        },
    }
});
$('#wantToSellForm').bootstrapValidator().on('success.form.bv', function(e) {
    // 阻止默认事件提交
    e.preventDefault();
    //submitForm()可以写在这
});

function calculateCashCoin() {
    $('#wantToSellTabCashCoin').val( parseFloat($('#wantToSellTabAmount').val()*$('#wantToSellTabKmc').val()).toFixed(3));
}
function submitSellForm() {
    if(!($('input[name=role]').val() && $('input[name=role]').val()=='2')) {
        if(!checkTradeTime()) {
            return;
        }
    }
    $('#wantToSellForm').data('bootstrapValidator').validate();
    if(!$('#wantToSellForm').data('bootstrapValidator').isValid() ){
        return ;
    }

    /*var data = {"action": "sell", "title": "提交卖单",
                "vc": $('input[name=vc]').val(), "amount" : $('input[name=amount]').val(),
                "tradePassword": $('input[name=tradePassword]').val()};
    showTradeModal("提交卖单", data);*/

    //防止重复提交
    $('#sellBtn').attr("disabled","disabled"); //设置变灰按钮
    showLoadingModal();
    $.ajax({
        url: 'sell',
        type: 'post',
        // contentType: 'application/json',
        dataType: "json",
        data: $('#wantToSellForm').serialize(),
        success: function (data) {
            hideLoadingModal();
            console.log(data);
            if(data.code == 0) {
                showSuccessAlert('提交卖单成功!');
            }else {
                showErrorAlert('提交卖单失败!' );
            }
        },
        error: function (e) {
            hideLoadingModal();
            // console.log(e.responseJSON.msg);
            showErrorAlert('提交卖单失败!' + e.responseJSON.msg);
        }
    });
    /*$.ajax({
        url:"sell",
        type: "post",
        dataType: "json",//预期服务器返回的数据类型
        data: $('#wantToSellForm').serialize(),
        success: function (result) {
            // console.log(result);
            $('#wantToSellForm')[0].reset();
            $('#wantToSellTabEcoin').val('');
            if (result.code == 0) {//挂卖成功
                showSuccessAlert("挂卖成功!");
                //改为直接刷新页面，后台也不用传更新的值了
                /!*$('#successAlert').show();
                 if(result.kmtb) {
                 $('#wantToSellTabKmtb').val(result.kmtb);
                 }
                 if(result.tradableCoin != null) {
                 $('#tradableCoin').val(result.tradableCoin);
                 }*!/
                // setTimeout("location.href='/tradingHall'",1500);
            }else {
                showErrorAlert("挂卖失败!" + result.msg);
            }

        },
        error : function(e) {
            console.log(e);
            $('failureAlert').show();
        }
    });*/
}
function calculateEcoin() {
    $('#ecoin2').val( parseFloat($('#amount2').val()*$('#kmc2').val()).toFixed(3));
}
$('#wantToBuyForm').bootstrapValidator({
    message: '输入错误',
    feedbackIcons: {
        valid: 'glyphicon glyphicon-ok',
        invalid: 'glyphicon glyphicon-remove',
        validating: 'glyphicon glyphicon-refresh'
    },
    fields: {
        amount2: {
            validators: {
                notEmpty: {
                    message: '请输入求购数量'
                },
                regexp: {
                    regexp:/^[0-9]*$/,
                    message: '请输入正确数量'
                },
                callback: {
                    message: '数量错误',
                    callback:  function(value, validator) {
                        /*if( parseFloat($('input[name=myEcoin]').val() )< parseFloat($('input[name=ecoin2]').val())) {
                            return {
                                valid: false,
                                message: '挂买数量过大，积分余额不足'
                            };
                        }else*/
                        if(!(value > 0 && value % 10 == 0)){
                            return {
                                valid: false,
                                message: '挂买数量必须是10的整数倍，最小为10'
                            };
                        }else {
                            return true;
                        }
                    }
                }
            }
        },
        vc2: {
            validators: {
                notEmpty: {
                    message: '请输入挂买单价'
                },
                regexp: {
                    regexp:/(^[1-9]([0-9]+)?(\.[0-9]{1,3})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
                    message: '请输入正确数值'
                },
            }
        },
        tradePassword2: {
            validators: {
                notEmpty: {
                    message: '请输入交易密码'
                },
                /*remote: {
                    url: '/isTradePwdCorrect',
                    delay: 1000,
                    type: 'POST',
                    message: '交易密码错误',
                    data: {
                        originPwd: function () {
                            return $('input[name=tradePassword2]').val();
                        }
                    }
                },*/
            }
        },
        ruleId: {
            validators: {
                callback: {
                    message: '请选择锁仓套餐',
                    callback:  function(value, validator) {
                        if (value == '') {
                            if(!($('input[name=role]').val() && $('input[name=role]').val()=='2')) {
                                //非admin
                                return {
                                    valid: false,
                                    message: '请选择锁仓套餐'
                                };
                            }else {
                                return true;
                            }
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
    }
});
$('#wantToBuyForm').bootstrapValidator().on('success.form.bv', function(e) {
    // 阻止默认事件提交
    e.preventDefault();
    //submitForm()可以写在这
});

function submitBuyForm() {
    if(!($('input[name=role]').val() && $('input[name=role]').val()=='2')) {
        if(!checkTradeTime()) {
            return;
        }
    }
    $('#wantToBuyForm').data('bootstrapValidator').validate();
    if(!$('#wantToBuyForm').data('bootstrapValidator').isValid() ){
        return ;
    }
    /*var data = {"action": "buy", "title": "提交买单",
        "vc": $('input[name=vc2]').val(), "amount" : $('input[name=amount2]').val(),
        "tradePassword": $('input[name=tradePassword2]').val(), "ruleId": $('select[name=option]').val()  };
    showTradeModal("提交买单", data);*/

    //防止重复提交
    $('#buyBtn').attr("disabled","disabled"); //设置变灰按钮
    showLoadingModal();
    $.ajax({
        url: 'buy',
        type: 'post',
        // contentType: 'application/json',
        dataType: "json",
        data: $('#wantToBuyForm').serialize(),
        success: function (data) {
            hideLoadingModal();
            console.log(data);
            if(data.code == 0) {
                showSuccessAlert('提交买单成功!');
            }else {
                showErrorAlert('提交买单失败!' );
            }
        },
        error: function (e) {
            hideLoadingModal();
            // console.log(e.responseJSON.msg);
            showErrorAlert('提交买单失败!'+ e.responseJSON.msg);
        }
    });
    /*$.ajax({
        url:"buy",
        type: "post",
        dataType: "json",//预期服务器返回的数据类型
        data: $('#wantToBuyForm').serialize(),
        success: function (result) {
            // console.log(result);
            $('#wantToBuyForm')[0].reset();
            $('#wantToSellTabEcoin').val('');
            if (result.code == 0) {//求购成功
                showSuccessAlert("求购成功!");
            }else {
                showErrorAlert("求购失败!" + result.msg);
            }
        },
        error : function(e) {
            console.log(e);
            $('failureAlert').show();
        }
    });*/
}
