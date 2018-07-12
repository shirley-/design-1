var $userTable = $('#user_table').bootstrapTable({
    method: 'post',
    url: 'memberlist/',
    contentType: "application/x-www-form-urlencoded",
    singleSelect: true,//单选
    //server分页
    pageNumber: 1, //初始化加载第一页，默认第一页
    pagination:true,//是否分页
    paginationLoop: false,
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
    pageSize:5,//单页记录数
    pageList:[5],//分页步进值
    paginationPreText: '<',//指定分页条中上一页按钮的图标或文字,这里是<
    paginationNextText: '>',//指定分页条中下一页按钮的图标或文字,这里是>
});

var $eCoinLogTable = $('#eCoin_log_table').bootstrapTable({
    method: 'post',
    url: 'ecoinLogList/',
    contentType: "application/x-www-form-urlencoded",
    singleSelect: true,//单选
    //server分页
    pageNumber: 1, //初始化加载第一页，默认第一页
    pagination:true,//是否分页
    paginationLoop: false,
    cache: false,
    queryParams:function (params){  //请求服务器时所传的参数
        return {
            pageIndex : params.offset/params.limit+1, //当前页面,默认是上面设置的1(pageNumber)
            pageSize : params.limit, //每一页的数据行数，默认是上面设置的10(pageSize)
            sort: params.sort,      //排序列名
            sortOrder: params.order, //排序命令（desc，asc）
            searchKey: params.search,
            searchUserId: $('#searchUserId').val()
            // param : "Your Param" //这里是其他的参数，根据自己的需求定义，可以是多个
        }
    },
    sidePagination:'server',//指定服务器端分页
    pageSize:5,//单页记录数
    pageList:[5],//分页步进值
    paginationPreText: '<',//指定分页条中上一页按钮的图标或文字,这里是<
    paginationNextText: '>',//指定分页条中下一页按钮的图标或文字,这里是>
});
$userTable.on('check.bs.table', function (e, row,ele) {
    $('#searchUserId').val(row.id);
    $eCoinLogTable.bootstrapTable('refresh');
});
$userTable.on('uncheck.bs.table', function (e, row,ele) {
    $('#searchUserId').val(null);
    $eCoinLogTable.bootstrapTable('refresh');
});

var $modal = $('#eCoinModal').modal({show: false});

$(function () {
    // create member event
    $('.ecoinAdd').click(function () {
        var selectRow = $userTable.bootstrapTable('getSelections');
        if(selectRow.length != 1) {
            alert("请在会员列表中选择一条会员记录!");
            return;
        }
        showModal($(this).text(), selectRow[0]);
    });
    $modal.find('.submit').click(function () {
        //开启验证
        $('#ecoinAddForm').data('bootstrapValidator').validate();
        if(!$('#ecoinAddForm').data('bootstrapValidator').isValid()){
            return ;
        }
        var row = {};//post data
        //input
        $modal.find('input[name]').each(function () {
            row[$(this).attr('name')] = $(this).val();
        });
        //select
        // $modal.find('select[name]').each(function() {
        //     row[$(this).attr('name')] = $(this).val();
        // });
        //row:  {uid: "xx03", name: "xx03", role: "1"}
        $.ajax({
            url: "ecoinLogAdd",
            type: 'post',
            contentType: 'application/json',
            dataType: "json",
            data: JSON.stringify(row),
            success: function (data) {
                console.log(data);
                if(data.code == 0) {
                    $modal.modal('hide');
                    $eCoinLogTable.bootstrapTable('refresh');
                    $userTable.bootstrapTable('refresh');
                    showAlert('新增积分充值信息成功', 'success');
                }else {
                    $modal.modal('hide');
                    showAlert('新增积分充值信息失败!'+e.responseJSON.msg, 'danger');
                }
            },
            error: function (e) {
                console.log(e.responseJSON.msg);
                $modal.modal('hide');
                showAlert('新增积分充值信息失败!'+e.responseJSON.msg, 'danger');
            }
        });
    });
});


function showModal(title, selectRow) {
    var row={};
    row.userId = selectRow.id;
    row.userUid = selectRow.uid;

    // $modal.data('id', row.id);
    $modal.find('.modal-title').text(title);
    for (var name in row) {
        $modal.find('input[name="' + name + '"]').val(row[name]);
    }
    $modal.modal('show');
}

$(document).ready(function() {
    initAddValidator();
    //search placeHolder
    var div = $('div.user_table div.search');
    var searchInput = div.children();
    searchInput.attr('placeHolder', '搜索用户ID');
    var div2 = $('div.eCoin_log_table div.search');
    var searchInput2 = div2.children();
    searchInput2.attr('placeHolder', '搜索用户ID');
});


function initAddValidator() {
    $('#ecoinAddForm').bootstrapValidator({
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
                        message: '请输入积分金额'
                    },
                    regexp: {
                        regexp:/(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/,
                        message: '请输入正确金额'
                    }
                }
            },
        }
    })
    $('#ecoinAddForm').bootstrapValidator().on('success.form.bv', function(e) {
        // 阻止默认事件提交
        e.preventDefault();
    });
}

//Modal验证销毁重构
$('#eCoinModal').on('hidden.bs.modal', function() {
    // validator destroy
    $("#ecoinAddForm").data('bootstrapValidator').destroy();
    $('#ecoinAddForm').data('bootstrapValidator', null);
    //clear input field
    $(this).removeData("bs.modal");
    //clear select field
    $(':input', $('#ecoinAddForm')).each(function () {
        var type = this.type;
        var tag = this.tagName.toLowerCase(); // normalize case
        if (type == 'text' || type == 'password' || tag == 'textarea') {
            this.value = "";
        }
    });
    //重置validator
    initAddValidator();
});




