var API_URL = 'members/';
var API_URL1 = 'memberlist/';
var $table = $('#table').bootstrapTable({
    url: API_URL1,
    method: 'post',
    contentType: "application/x-www-form-urlencoded",
//     data_local: "zh-US",//表格汉化
    singleSelect: true,
    searchTimeOut: 2000,
    //server分页
    pageNumber: 1, //初始化加载第一页，默认第一页
    pagination:true,//是否分页
    paginationLoop: false,
    // showPaginationSwitch: true,//True to show the pagination switch button.
    cache: false,
    queryParams:function (params){  //请求服务器时所传的参数
        return {
            pageIndex : params.offset/params.limit+1, //当前页面,默认是上面设置的1(pageNumber)
            pageSize : params.limit, //每一页的数据行数，默认是上面设置的10(pageSize)
            sort: params.sort,      //排序列名
            sortOrder: params.order, //排序命令（desc，asc）
            searchKey: params.search
            // param : "Your Param" //这里是其他的参数，根据自己的需求定义，可以是多个
        }
    },
    sidePagination:'server',//指定服务器端分页
    pageSize:10,//单页记录数
    pageList:[10],//分页步进值
    paginationPreText: '<',//指定分页条中上一页按钮的图标或文字,这里是<
    paginationNextText: '>',//指定分页条中下一页按钮的图标或文字,这里是>
});


var $modal = $('#modal').modal({show: false});


//modal增
$(function () {
    // create member event
    $('.create').click(function () {
        showModal($(this).text());
    });
    $modal.find('.submit').click(function () {
        //开启验证
        $('#add_form').data('bootstrapValidator').validate();
        if(!$('#add_form').data('bootstrapValidator').isValid()){
            return ;
        }
        var row = {};//post data
        //input
        $modal.find('input[name]').each(function () {
            row[$(this).attr('name')] = $(this).val();
        });
        //select
        $modal.find('select[name]').each(function() {
            row[$(this).attr('name')] = $(this).val();
        });
        //row:  {uid: "xx03", name: "xx03", role: "1"}
        $.ajax({
            url: API_URL + ($modal.data('id') || ''),
            type: $modal.data('id') ? 'put' : 'post',
            contentType: 'application/json',
            dataType: "json",
            data: JSON.stringify(row),
            success: function (data) {
                if(data.code == 0) {
                    $modal.modal('hide');
                    $table.bootstrapTable('refresh');
                    showAlert(($modal.data('id') ? '修改' : '新增') + '会员成功!', 'success');
                }else {
                    console.log(data);
                    $modal.modal('hide');
                    showAlert(($modal.data('id') ? '修改' : '新增') + '会员失败!', 'danger');
                }
            },
            error: function (e) {
                // console.log(e.responseJSON.msg);
                $modal.modal('hide');
                showAlert(($modal.data('id') ? '修改' : '新增') + '会员失败!', 'danger');
            }
        });
    });
});

function actionFormatter(value) {
    return [
        '<a class="update" href="javascript:" title="修改" style="margin:0 10px 0 0"><i class="glyphicon glyphicon-edit"></i></a>',
        '<a class="remove" href="javascript:" title="删除"><i class="glyphicon glyphicon-remove-circle"></i></a>',
    ].join('');
}
// update and delete events
window.actionEvents = {
    'click .update': function (e, value, row) {
        showModal($(this).attr('title'), row);
        /*if(row.ruleId) {
            $('select[name=ruleId]').attr('readonly', true);
            $('input[name=ecoin]').attr('readonly', true);
        }*/
        // disableEditAndValidate(['uid', 'ruleId', 'introducerUid', 'ecoin' ]);
        disableEditAndValidate(['uid',  'introducerUid']);
    },
    'click .remove': function (e, value, row) {
        if (confirm('确定删除么?')) {
            $.ajax({
                url: API_URL + row.id,
                type: 'delete',
                dataType: "json",
                success: function (data) {
                    if(data.code == 0) {
                        $table.bootstrapTable('refresh');
                        showAlert('删除成功!', 'success');
                    }else {
                        showAlert('删除失败!', 'danger');
                    }
                },
                error: function (e) {
                    // console.log(e.responseJSON.msg);
                    showAlert('删除失败!', 'danger');
                }
            })
        }
    }
};
function showModal(title, row) {
    row = row || {
            id: '',
            uid:'',
            name: '',
            // role: '1',
        }; // default row value
    $modal.data('id', row.id);
    $modal.find('.modal-title').text(title);
    for (var name in row) {
        $modal.find('input[name="' + name + '"]').val(row[name]);
    }
    for (var name in row) {
        $modal.find('select[name="' + name + '"]').val(row[name]);
    }
    $modal.modal('show');
}


$(document).ready(function() {
    initAddValidator();
    //search input
    var div = $('div.search');
    var searchInput = div.children();
    searchInput.attr('placeHolder', '搜索用户ID');
});

/**
 * add form 前台验证
 */
function initAddValidator() {
    // $('#add_form').on('init.field.bv', function(e, data) {
    //     var $icon      = data.element.data('bv.icon'),
    //         options    = data.bv.getOptions(),                      // Entire options
    //         validators = data.bv.getOptions(data.field).validators; // The field validators
    //     if (validators.notEmpty && options.feedbackIcons && options.feedbackIcons.required) {
    //         $icon.addClass(options.feedbackIcons.required).show();
    //     }
    // }).bootstrapValidator({
    $('#add_form').bootstrapValidator({
        message: '输入错误',
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            uid: {
                validators: {
                    notEmpty: {
                        message: '请输入用户ID'
                    },
                    remote: {
                        url: '/isUidNotExist',
                        delay: 1500,
                        type: 'POST',
                        message: '该用户ID已存在',
                        data: {
                            inputUid: function() {
                                return $('input[name=uid]').val();
                            }
                        }
                    }
                }
            },
            introducerUid: {
                validators: {
                    /*notEmpty: {
                        message: '请输入用户ID'
                    },*/
                    remote: {
                        url: '/isUidExist',
                        delay: 2000,
                        type: 'POST',
                        message: '该用户ID不存在',
                        data: {
                            inputUid: function() {
                                return $('input[name=introducerUid]').val();
                            }
                        }
                    },
                    callback: {
                        message: '推荐人不符合规定',
                        callback:  function(value, validator) {
                            if(value == $('input[name=uid]').val() ) {
                                return {
                                    valid: false,
                                    message: '推荐人不符合规定'
                                };
                            }else {
                                return true;
                            }
                        }
                    }
                }
            },
            name: {
                validators: {
                    notEmpty: {
                        message: '请输入姓名'
                    }
                }
            },
            phone: {
                validators: {
                    notEmpty: {
                        message: '请输入电话'
                    },
                    stringlength:{
                        min:11,
                        max:11,
                        message:'请输入11位手机号码'
                    },
                    regexp:{
                        regexp:/^1[0-9]{10}$/,
                        message:'请输入正确的手机号码'
                    }
                }
            },
            /*email: {
                validators: {
                    /!*notEmpty: {
                        message: '请输入邮箱'
                    },*!/
                    emailAddress:{
                        message:'请输入正确的邮箱地址'
                    }
                }
            },*/
            /*rule: {
                validators: {
                    callback: {
                        message: '请选择用户星级',
                        callback:  function(value, validator) {
                            if(value == '' ) {
                                return {
                                    valid: false,
                                    message: '请选择用户星级'
                                };
                            }else {
                                return true;
                            }
                        }
                    }
                }
            },*/
            /*ecoin: {
                validators: {
                    // notEmpty: {
                    //     message: '请输入积分数量'
                    // },
                    regexp: {
                        regexp: /^[0-9]*$/,
                        message: '请输入正确数量'
                    },
                }
            },*/
           /* idNumber: {
                validators: {
                    /!*notEmpty: {
                        message: '请输入身份证号'
                    },*!/
                    regexp: {
                        message: '清输入正确的身份证号',
                        regexp: /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
                    }
                }
            },*/
           /* bankAccount: {
                validators: {
                    notEmpty: {
                        message: '请输入银行卡号'
                    }
                }
            },
            depositBank: {
                validators: {
                    notEmpty: {
                        message: '请输入开户银行'
                    }
                }
            },
            address: {
                validators: {
                    notEmpty: {
                        message: '请输入地址'
                    }
                }
            },*/
            /*accountHolder: {
                validators: {
                    notEmpty: {
                        message: '请输入开户人姓名'
                    }
                }
            }*/
        }
    });
    $('#add_form').bootstrapValidator().on('success.form.bv', function(e) {
        // 阻止默认事件提交
        e.preventDefault();
    });
}
//Modal验证销毁重构
$('#modal').on('hidden.bs.modal', function() {
    // validator destroy
    $("#add_form").data('bootstrapValidator').destroy();
    $('#add_form').data('bootstrapValidator', null);
    //clear input field
    $(this).removeData("bs.modal");
    //clear select field
    $(':input', $('#add_form')).each(function () {
        var type = this.type;
        var tag = this.tagName.toLowerCase(); // normalize case
        if (type == 'text' || type == 'password' || tag == 'textarea' ||type=='tel') {
            this.value = "";
        }
        // 多选checkboxes清空..
        // select 下拉框清空
        else if (tag == 'select'){
            this.selectedIndex = 0;
        }
        this.readOnly = false;
    });
    //重置validator
    initAddValidator();
});

function disableEditAndValidate(fieldList) {
    for(var index in fieldList) {
        $('input[name='  + fieldList[index]+ ']').attr('readonly', true);
        try{
            $('#add_form').data('bootstrapValidator').enableFieldValidators(fieldList[index], false);
        }catch (e) {

        }

    }
}

/*function displayUserLevel(value,row,index) {
    if(value) {
        return value + "星级";
    }else {
        return value;
    }
}*/



/**
 * echart 会员推荐结构
 */
var myChart = echarts.init(document.getElementById('main'));
// 指定图表的配置项和数据
function bindTreeChartData(jsonData) {
    console.log(jsonData);
    var option = {
        title : {
            text: '树图',
            // subtext: '虚构数据'
        },
        toolbox: {
            show : false,
        },
        tooltip: {
            show: true,
            position: 'right',
            formatter: '{c}',
        },
        series:
        // jsonData
        [
            {
                name: '树图',
                type: 'tree',
                orient: 'vertical',  // vertical horizontal
                rootLocation: {x: 'center', y: 50}, // 根节点位置  {x: 100, y: 'center'}
                nodePadding: 20,
                symbol: 'circle',
                symbolSize: [110,70],
                initialTreeDepth: -1,
                itemStyle: {
                    normal: {
                        color: '#F2E2BA',
                        borderColor: '#ebdbb4',
                    },
                    emphasis: {
                        color:'#DCCEAA',
                        borderColor: '#d0c29f',
                    }
                },
                label: {
                    normal: {
                        show: true,
                        position: 'inside',
                        color: '#557ef7',
                        // fontWeight: 'bold'
                    }
                },
                lineStyle: {
                    normal: {
                        color: '#F2BAC9',
                        width: 3
                    }
                },
                data: [
                    {
                        name: jsonData.name,
                        value: jsonData.value,
                        children: jsonData.children
                    }
                ]
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.hideLoading();
    myChart.clear();
    myChart.setOption(option);
    // setTimeout(() => { echatsInstance.setOption([option]) }, 500)
}




/**
 * treeview
 */
$(function () {

    // show member organization event
    $('.memberOrg').click(function () {
        var selections = $table.bootstrapTable('getSelections');
        console.log(selections);
        if (selections.length != 1) {
            alert("请勾选一条信息进行查看");
            return;
        }
        myChart.showLoading({
            text: '正在努力的读取数据中...',
        });
        //else
        var id = selections[0].id;
        $.ajax({
            url: 'memberOrgTreeview?id='+id,
            type: 'get',
            contentType: 'application/json',
            dataType: "json",
            success: function (data) {
                if(data.code == 0) {
                    $('#tree').treeview({data: data.msg});
                }
            },
            error: function (e) {
                // console.log(e.responseJSON.msg);
            }
        });
        $.ajax({
            url: 'memberOrgTreeChart?id='+id,
            type: 'get',
            contentType: 'application/json',
            dataType: "json",
            success: function (data) {
                if(data.code == 0) {
                    bindTreeChartData(JSON.parse(data.msg));
                }
            },
            error: function (e) {
                // console.log(e.responseJSON.msg);
            }
        });
    });

    //重置密码
    $('.resetLoginPwd').click(function () {
        var selections = $table.bootstrapTable('getSelections');
        console.log(selections);
        if (selections.length != 1) {
            alert("请勾选一条用户");
            return;
        }
        var id = selections[0].id;
        if (confirm('确定重置该用户的登录密码?')) {
            $.ajax({
                url: 'resetLoginPwd?id='+id,
                type: 'post',
                data: {'id': id },
                // contentType: 'application/json',
                dataType: "json",
                success: function (data) {
                    if(data.code == 0) {
                        showSuccessAlert("重置登录密码成功",2);
                    }
                },
                error: function (e) {
                    // console.log(e.responseJSON.msg);
                    showErrorAlert("重置登录密码失败",2);
                }
            });
        }

    });

    $('.resetTradePwd').click(function () {
        var selections = $table.bootstrapTable('getSelections');
        console.log(selections);
        if (selections.length != 1) {
            alert("请勾选一条用户");
            return;
        }
        var id = selections[0].id;
        if (confirm('确定重置该用户的交易密码?')) {
            $.ajax({
                url: 'resetTradePwd?id='+id,
                type: 'post',
                data: {'id': id },
                // contentType: 'application/json',
                dataType: "json",
                success: function (data) {
                    if(data.code == 0) {
                        showSuccessAlert("重置交易密码成功",2);
                    }
                },
                error: function (e) {
                    // console.log(e.responseJSON.msg);
                    showErrorAlert("重置交易密码失败",2);
                }
            });
        }
    });


});




