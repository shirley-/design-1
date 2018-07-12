$(document).ready(function() {
    $('#encashForm').bootstrapValidator({
        message: '请输入提现金额',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            amount: {
                validators: {
                    notEmpty: {
                        message: '请输入提现金额'
                    },
                    callback: {
                        message: '提现金额必须是10的整数倍，小于财富积分',
                        callback:  function(value, validator) {
                            if(value > 0 && value % 10 == 0 && parseFloat(value)<=parseFloat($("input[name='cashCoin']").val()) ) {
                                return true;
                            }else {
                                return {
                                    valid: false,
                                    message: '提现金额必须是10的整数倍，小于财富积分'
                                };
                            }
                        }
                    }
                }
            },
            tradePassword: {
                validators: {
                    notEmpty: {
                        message: '请输入交易密码'
                    },
                    //不使用短信验证码就不校验了
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
    $('#encashForm').bootstrapValidator().on('success.form.bv', function(e) {
        // 阻止默认事件提交
        e.preventDefault();
    });
});

function submitForm() {
    if(!($('input[name=role]').val() && $('input[name=role]').val()=='2')) {
        if(!checkTradeTime()) {
            return;
        }
    }

    $('#encashForm').data('bootstrapValidator').validate();
    if(!$('#encashForm').data('bootstrapValidator').isValid()){
        return ;
    }

    //使用短信验证码
    /*var data = {"action": "encash", "title": "提现请求", "amount" : $('input[name=amount]').val(),
        "tradePassword": $('input[name=tradePassword]').val()};
    showTradeModal("提现请求", data);*/

    //不使用短信验证码
    $.ajax({
        url:"encash",
        type: "post",
        dataType: "json",//预期服务器返回的数据类型
        data: $('#encashForm').serialize(),
        success: function (result) {
            console.log(result);
            if (result.code == 0) {
                showSuccessAlert("提现请求成功! ");
            }else {
                showErrorAlert("提现请求失败! " );
            }
        },
        error : function(e) {
            console.log(e.responseJSON.msg);
            showErrorAlert("提现请求失败!" );
        }
    });
}

function calculate() {
    $('#earnings').val( parseFloat($('#amount').val()* (1-ENCASH_FEE)*ENCASH_RATE).toFixed(3));
    $('#shopPoints').val( parseFloat($('#amount').val()* (1-ENCASH_FEE)*(1-ENCASH_RATE)).toFixed(3));
}


