$(document).ready(function() {
    $('#defaultForm')
        .bootstrapValidator({
            message: '输入错误',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                name: {
                    // message: '请输入用户名',
                    validators: {
                        notEmpty: {
                            message: '请输入用户名'
                        },
                        /*stringLength: {
                         min: 6,
                         max: 30,
                         message: 'The username must be more than 6 and less than 30 characters long'
                         },
                         /!*remote: {
                         url: 'remote.php',
                         message: 'The username is not available'
                         },*!/
                         regexp: {
                         regexp: /^[a-zA-Z0-9_\.]+$/,
                         message: 'The username can only consist of alphabetical, number, dot and underscore'
                         }*/
                    }
                },
                password: {
                    validators: {
                        notEmpty: {
                            message: '请输入密码'
                        }
                    }
                },
                validateCode: {
                    threshold: 4,
                    validators: {
                        notEmpty: {
                            message: '请输入验证码'
                        },stringlength: {
                            min:4,
                            max:4,
                            message: '验证码错误'
                        },
                        /*remote: {
                            message: '验证码错误',
                            url: '/isValidateCodeCorrect',
                            delay: 1000,
                            type: 'POST',
                            data: {inputVCode:
                                function() {
                                    return  $('#validateCode').val()
                                }
                            }
                        }*/
                    }
                }
            }
        })

});

function refreshVcode() {
    $("#validateCodeImg").attr("src","/validateCode?number="+Math.random() );
}