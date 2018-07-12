$(document).ready(function() {
    $('#pwdForm').bootstrapValidator({
        message: '输入错误',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            originPwd: {
                validators: {
                    notEmpty: {
                        message: '请输入原交易密码'
                    },
                    remote: {
                        url: '/isTradePwdCorrect',
                        delay: 2000,
                        type: 'POST',
                        message: '密码错误',
                        data: {
                            id: function () {
                                return $('input[name=id]').val();
                            },
                            originPwd: function () {
                                return $('input[name=originPwd]').val();
                            }
                        }
                    }
                }
            },
            newPwd: {
                validators: {
                    notEmpty: {
                        message: '请输入新交易密码'
                    },
                    callback: {
                        message: '不能与用户名或原交易密码相同',
                        callback:  function(value, validator) {
                            if(value == $('input[name=uid]').val() || value == $('input[name=originPwd]').val() ) {
                                return {
                                    valid: false,
                                    message: '不能与用户名或原交易密码相同'
                                };
                            }else {
                                return true;
                            }
                        }
                    },
                    regexp:{
                        regexp:/^(?=.*[a-zA-Z])(?=.*\d)[^]{8,16}$/,
                        message:'请检查密码强度，选择8-16位复杂密码'
                    }
                }
            },
            newPwd2: {
                validators: {
                    notEmpty: {
                        message: '请输入确认密码'
                    },
                    callback: {
                        message: '两次密码不一致',
                        callback:  function(value, validator) {
                            if(value == $('input[name=newPwd]').val() ) {
                                return true;
                            }else {
                                return {
                                    valid: false,
                                    message: '两次密码不一致'
                                };
                            }
                        }
                    }
                }
            },
        }
    });
    $('#pwdForm').bootstrapValidator().on('success.form.bv', function(e) {
        // 阻止默认事件提交
        e.preventDefault();
    });
});

function submitForm() {
    $('#pwdForm').data('bootstrapValidator').validate();
    if(!$('#pwdForm').data('bootstrapValidator').isValid()){
        return ;
    }
    $.ajax({
        url:"changeTradePwd",
        type: "post",
        dataType: "json",//预期服务器返回的数据类型
        data: $('#pwdForm').serialize(),
        success: function (result) {
            console.log(result);
            if (result.code == 0) {
                showAlert('修改交易密码成功!', 'success');
            }else {
                showAlert('修改交易密码失败!', 'danger');
            }
            $('#pwdForm')[0].reset();
        },
        error : function(e) {
            console.log(e);
            showAlert('修改交易密码失败!', 'danger');
        }
    });
}