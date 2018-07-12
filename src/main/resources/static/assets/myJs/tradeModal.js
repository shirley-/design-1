/*function modalSendSms() {
    if(checkTradeTime()) {
        if ($('#modalTradePwdForm').data("bootstrapValidator").validateField('tradePassword')) {
            var vs = $('.help-block[data-bv-for=tradePassword]');
            var flag = 0;
            $.each(vs, function (i, item) {
                if (item.dataset.bvResult == 'VALID') {
                    flag++;
                }
            })
            //flag
            if (flag == vs.length) {
                $.ajax({
                    url: "sendSms",
                    type: "post",
                    dataType: "json",
                    data: $('#modalTradePwdForm').serialize(),
                    success: function (result) {
                        if (result.code == 0) {
                            alert("短信验证码已发送到手机");
                        } else {
                            //alert("error:"+ result);
                        }
                    },
                    error: function (e) {
                        //alert("error:"+e);
                    }

                })
                var obj = $("#modalCountdown");
                showCountdownTime(obj);
            }
        }
    }
}*/

function modalSendSms() {
    if(checkTradeTime()) {
        if($('input[name=action]').val().indexOf("cancelOrder")>-1) {//撤销，需显示tradePassword2
            if(!$('input[name=tradePassword2]').val()) {
                alert("请输入交易密码!")
                return ;
            }
            if ($('#modalTradePwdForm').data("bootstrapValidator").validateField('tradePassword2')) {
                var vs = $('.help-block[data-bv-for=tradePassword2]');
                var flag = 0;
                $.each(vs, function (i, item) {
                    if (item.dataset.bvResult == 'VALID') {
                        flag++;
                    }
                })
                //flag
                if (flag == vs.length) {
                    //防止重复提交

                    $('input[name=tradePassword]').val($('input[name=tradePassword2]').val());
                    $.ajax({
                        url: "sendSms",
                        type: "post",
                        dataType: "json",
                        data: $('#modalTradePwdForm').serialize(),
                        success: function (result) {
                            if (result.code == 0) {
                                alert("短信验证码已发送到手机");
                            } else {
                                alert("error:" );
                            }
                        },
                        error: function (e) {
                            alert("error:");
                        }
                    });
                    var obj = $("#modalCountdown");
                    showCountdownTime(obj);
                }
            }

        }else {
            $.ajax({
                url: "sendSms",
                type: "post",
                dataType: "json",
                data: $('#modalTradePwdForm').serialize(),
                success: function (result) {
                    if (result.code == 0) {
                        alert("短信验证码已发送到手机");
                    } else {
                        alert("error:" );
                    }
                },
                error: function (e) {
                    alert("error:" );
                }
            });
            var obj = $("#modalCountdown");
            showCountdownTime(obj);
        }
    }
}

var $tradeModal = $('#modalTradePwd').modal({show: false});
$(function () {
    // initValidator();
    $tradeModal.find('.submitModal').click(function () {
        //开启验证
        $('#modalTradePwdForm').data('bootstrapValidator').validate();
        if(!$('#modalTradePwdForm').data('bootstrapValidator').isValid()){
            return ;
        }
        //防止重复提交
        $(this).attr("disabled","disabled"); //设置变灰按钮
        $tradeModal.modal('hide');
        showLoadingModal();

        /*if($('input[name=action]').val().indexOf("cancelOrder")>-1) {//撤销，需显示tradePassword2
            $('input[name=tradePassword]').val($('input[name=tradePassword2]').val());
        }*/
        $.ajax({
            url: $('input[name=action]').val(),
            type: 'post',
            // contentType: 'application/json',
            dataType: "json",
            data: $('#modalTradePwdForm').serialize(),
            success: function (data) {
                hideLoadingModal();
                console.log(data);
                if(data.code == 0) {
                    $tradeModal.modal('hide');
                    showSuccessAlert($('input[name=title]').val() + '成功!');
                }else {
                    $tradeModal.modal('hide');
                    showErrorAlert($('input[name=title]').val() + '失败!' );
                }
            },
            error: function (e) {
                hideLoadingModal();
                // console.log(e.responseJSON.msg);
                $tradeModal.modal('hide');
                showErrorAlert($('input[name=title]').val() + '失败!' );
            }
        });
    });
});


function showTradeModal(title, selectRow) {
    $tradeModal.find('.modal-title').text(title);
    for (var name in selectRow) {
        $tradeModal.find('input[name="' + name + '"]').val(selectRow[name]);
    }
    $tradeModal.modal('show');

    /*if(selectRow.action == "sellVc") {//出售不选择天数，购买必须选择
        disableValidate(["option"]);
    }*/
    /*if(selectRow.action.indexOf("cancelOrder")>-1) {//撤销，需显示tradePassword
        $('#tradePwdGroup').show();
    }else {
        disableValidate(["tradePassword"]);
    }*/
    initValidator();
}

function initValidator() {
    $('#modalTradePwdForm').bootstrapValidator({
        message: '输入错误',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            tradePassword: {
                validators: {
                    notEmpty: {
                        message: '请输入交易密码'
                    },
                },
            }
            /*tradePassword2: {
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
                                return $('input[name=tradePassword2]').val();
                            }
                        }
                    },
                }
            },*/
           /* validateCode:{
                validators: {
                    notEmpty: {
                        message: '请输入验证码'
                    },
                }
            },*/
            /*option: {
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
            }*/
        },
    });
    $('#modalTradePwdForm').bootstrapValidator().on('success.form.bv', function(e) {
        // 阻止默认事件提交
        e.preventDefault();
    });
}

//Modal验证销毁重构
$('#modalTradePwd').on('hidden.bs.modal', function() {
    // validator destroy
    $("#modalTradePwdForm").data('bootstrapValidator').destroy();
    $('#modalTradePwdForm').data('bootstrapValidator', null);
    //clear input field
    $(this).removeData("bs.modal");

    $(this).removeAttr("disabled"); //变灰按钮,恢复
    //clear select field
    $(':input', $('#modalTradePwdForm')).each(function () {
        var type = this.type;
        var tag = this.tagName.toLowerCase(); // normalize case
        if (type == 'text' || type == 'password' || tag == 'textarea') {
            this.value = "";
        }
    });
    //重置validator
    initValidator();
    // resetCountdown();
});


function disableValidate(fieldList) {
    for(var index in fieldList) {
        $('#modalTradePwdForm').data('bootstrapValidator').enableFieldValidators(fieldList[index], false);
    }
}