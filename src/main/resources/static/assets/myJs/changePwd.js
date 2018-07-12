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
                        message: '请输入原密码'
                    },
                    remote: {
                        url: '/isPwdCorrect',
                        delay: 2000,
                        type: 'POST',
                        message: '密码错误',
                        data: {
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
                        message: '请输入新密码'
                    },
                    /*different: {//不能和用户名相同
                        field: 'name',//需要进行比较的input name值
                        message: '不能和用户名相同'
                    },*/
                    callback: {
                        message: '不能与用户名或原密码相同',
                        callback:  function(value, validator) {
                            if(value == $('input[name=uid]').val() || value == $('input[name=originPwd]').val() ) {
                                return {
                                    valid: false,
                                    message: '不能与用户名或原密码相同'
                                };
                            }else {
                                return true;
                            }
                        }
                    }
                }
            },
            newPwd2: {
                validators: {
                    notEmpty: {
                        message: '请输入确认密码'
                    },
                    /*identity: {
                        field: 'newPwd', //需要进行比较的input name值
                        message: '两次密码不一致'
                    },*/
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
        url:"changePwd",
        type: "post",
        dataType: "json",//预期服务器返回的数据类型
        data: $('#pwdForm').serialize(),
        success: function (result) {
            console.log(result);
            if (result.code == 0) {
                showAlert('修改密码成功!', 'success');
                setTimeout("location.href='/logout'",2000);
            }else {
                showAlert('修改密码失败!', 'danger');
            }
            $('#pwdForm')[0].reset();
        },
        error : function(e) {
            console.log(e);
            showAlert('修改密码失败!', 'danger');
        }
    });
}