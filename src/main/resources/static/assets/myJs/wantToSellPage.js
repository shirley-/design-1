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
                callback: {
                    message: '数量错误',
                    callback:  function(value, validator) {
                        /*if( parseFloat(value )> parseFloat($('input[name=tradableCoin]').val()*MAX_SELL_RATE)) {
                            return {
                                valid: false,
                                message: '挂卖数量过大，KMC余额不足；KMC最多可交易90%'
                            };
                        }else*/ if(!(value > 0 && value % 10 == 0)){
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
                remote: {
                    url: '/isTradePwdCorrect',
                    delay: 1000,
                    type: 'POST',
                    message: '交易密码错误',
                    data: {
                        originPwd: function () {
                            return $('input[name=tradePassword]').val();
                        }
                    }
                },
            }
        },
        validateCode:{
            validators: {
                notEmpty: {
                    message: '请输入验证码'
                },
            }
        }
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

function sendSms() {
    if(!checkTradeTime()) {
        return;
    }
    if($('#wantToSellForm').data("bootstrapValidator").validateField('tradePassword')) {
        var vs = $('.help-block[data-bv-for=tradePassword]');
        var flag = 0;
        $.each(vs, function(i, item) {
            if(item.dataset.bvResult=='VALID') {
                flag++;
            }
        })
        //flag
        if(flag == vs.length) {
            $.ajax({
                url: "sendSms",
                type: "post",
                dataType: "json",
                data:  $('#wantToSellForm').serialize(),
                success: function (result) {
                    if(result.code == 0) {
                        alert("短信验证码已发送到手机");
                    }else {
                        //alert("error:"+ result);
                    }
                },
                error: function (e) {
                    //alert("error:"+e);
                    // console.log(e.responseJSON.msg);
                }

            })
            var obj = $("#countdown");
            showCountdownTime(obj);
        }
    }
}
/**
 * 挂卖
 */
function submitForm() {
    if(!checkTradeTime()) {
        return;
    }
    $('#wantToSellForm').data('bootstrapValidator').validate();
    if(!$('#wantToSellForm').data('bootstrapValidator').isValid() ){
        return ;
    }
    $.ajax({
        url:"wantToSell",
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
                /*$('#successAlert').show();
                 if(result.kmtb) {
                 $('#wantToSellTabKmtb').val(result.kmtb);
                 }
                 if(result.tradableCoin != null) {
                 $('#tradableCoin').val(result.tradableCoin);
                 }*/
                // setTimeout("location.href='/tradingHall'",1500);
            }else {
                showErrorAlert("挂卖失败!" );
            }

        },
        error : function(e) {
            console.log(e);
            $('failureAlert').show();
        }
    });
}
