function calculateEcoin() {
    $('#ecoin').val( parseFloat($('#amount').val()*$('#kmc').val()).toFixed(3));
}
$('#wantToBuyForm').bootstrapValidator({
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
                    message: '请输入求购数量'
                },
                regexp: {
                    regexp:/^[0-9]*$/,
                    message: '请输入正确数量'
                },
                callback: {
                    message: '数量错误',
                    callback:  function(value, validator) {
                        if( parseFloat($('input[name=myEcoin]').val() )< parseFloat($('input[name=ecoin]').val())) {
                            return {
                                valid: false,
                                message: '求购数量过大，积分余额不足'
                            };
                        }else if(!(value > 0 && value % 10 == 0)){
                            return {
                                valid: false,
                                message: '求购数量必须是10的整数倍'
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
                    message: '请输入求购单价'
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
        },
        option: {
            validators: {
                callback: {
                    message: '请选择锁仓套餐',
                    callback:  function(value, validator) {
                        if (value == '') {
                            return {
                                valid: false,
                                message: '请选择锁仓套餐'
                            };
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

function sendSms() {
    if(!checkTradeTime()) {
        return;
    }
    if($('#wantToBuyForm').data("bootstrapValidator").validateField('tradePassword')) {
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
                data:  $('#wantToBuyForm').serialize(),
                success: function (result) {
                    if(result.code == 0) {
                        alert("短信验证码已发送到手机");
                    }else {
                        //alert("error:"+ result);
                    }
                },
                error: function (e) {
                    //alert("error:"+e);
                }
            })
            var obj = $("#countdown");
            showCountdownTime(obj);
        }
    }
}
/**
 * 求购
 */
function submitToBuyForm() {
    if(!checkTradeTime()) {
        return;
    }
    $('#wantToBuyForm').data('bootstrapValidator').validate();
    if(!$('#wantToBuyForm').data('bootstrapValidator').isValid() ){
        return ;
    }
    $.ajax({
        url:"wantToBuy",
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
                showErrorAlert("求购失败!" );
            }
        },
        error : function(e) {
            console.log(e);
            $('failureAlert').show();
        }
    });
}