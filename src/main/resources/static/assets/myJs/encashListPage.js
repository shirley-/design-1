var $table;
$(function () {
    //Date picker
    var today = new Date();
    $('#datepicker').val(today.format('yyyy-MM-dd'));
    $table = $('#table').bootstrapTable({
        url: 'encashList',
        method:'POST',
        dataType:'json',
        contentType: "application/x-www-form-urlencoded",
        // singleSelect: true,
        //server分页
        pageNumber: 1, //初始化加载第一页，默认第一页
        pagination:true,//是否分页
        paginationLoop: false,
        // showPaginationSwitch: true,//True to show the pagination switch button.
        cache: true,
        queryParams:function (params){  //请求服务器时所传的参数
            return {
                pageIndex : params.offset/params.limit+1, //当前页面,默认是上面设置的1(pageNumber)
                pageSize : params.limit, //每一页的数据行数，默认是上面设置的10(pageSize)
                state: $('#state').val(),
                searchDate: $('#datepicker').val(),      //查询日期
                // sort: $('#sort').val(),
                // sortOrder: $('#sortOrder').val(),
            }
        },
        sidePagination:'server',//指定服务器端分页
        // pageSize:10,//单页记录数
        //可供选择的每页的行数（*）
        pageList: [10, 25, 50, 100],
        paginationPreText: '<',//指定分页条中上一页按钮的图标或文字,这里是<
        paginationNextText: '>',//指定分页条中下一页按钮的图标或文字,这里是>
    });



});

function searchTable() {
    $table.bootstrapTable('refresh');
}

function actionFormatter(value) {
    return [
        '<a class="update" href="javascript:" title="审批" style="margin:0 10px 0 0"><i class="glyphicon glyphicon-edit"></i>审批</a>',
        // '<a class="remove" href="javascript:" title="拒绝"><i class="glyphicon glyphicon-remove-circle"></i>拒绝</a>',
    ].join('');
}
var $modal = $('#modal').modal({show: false});
$(function () {
    $modal.find('.submit').click(function () {
        var row = {};//post data
        //input
        $modal.find('input[name]').each(function () {
            row[$(this).attr('name')] = $(this).val();
        });
        //row:  {uid: "xx03", name: "xx03", role: "1"}
        $.ajax({
            url: 'approveEncash/' + ($modal.data('id') || ''),
            type: 'post',
            // contentType: 'application/json',
            dataType: "json",
            data: $('#form').serialize(),
            success: function (data) {
                if(data.code==0) {
                    $modal.modal('hide');
                    $table.bootstrapTable('refresh');
                    showAlert('同意成功', 'success');
                }else{
                    $modal.modal('hide');
                    showAlert('同意失败! ' + data.msg, 'danger');
                }

            },
            error: function (e) {
                // console.log(e.responseJSON.msg);
                $modal.modal('hide');
                showAlert('同意失败! ' , 'danger');
            }
        });
    });
    $modal.find('.submit2').click(function () {
        var row = {};//post data
        //input
        $modal.find('input[name]').each(function () {
            row[$(this).attr('name')] = $(this).val();
        });
        //row:  {uid: "xx03", name: "xx03", role: "1"}
        $.ajax({
            url: 'rejectEncash/' + ($modal.data('id') || ''),
            type:  'post',
            // contentType: 'application/json',
            dataType: "json",
            data: $('#form').serialize(),
            success: function (data) {
                if(data.code==0) {
                    $modal.modal('hide');
                    $table.bootstrapTable('refresh');
                    showAlert('拒绝成功!', 'success');
                }else {
                    $modal.modal('hide');
                    showAlert('拒绝失败! ' + data.msg, 'danger');
                }

            },
            error: function (e) {
                // console.log(e.responseJSON.msg);
                $modal.modal('hide');
                showAlert('拒绝失败', 'danger');
            }
        });
    });
});
// update and delete events
window.actionEvents = {
    'click .update': function (e, value, row) {
        showModal($(this).attr('title'), row);
    },
};
function showModal(title, row) {
    /*row = row || {
            id: ''
        }; // default row value*/
    $modal.data('id', row.id);
    $modal.find('.modal-title').text(title+'提现请求');
    for (var name in row) {
        $modal.find('input[name="' + name + '"]').val(row[name]);
    }
    $modal.modal('show');
}

function displayState(value,row,index) {
    if(value) {
        if(value == '1') {
            return "申请中";
        }
        if(value == '3') {
            return "同意";
        }
        if(value == '4') {
            return "拒绝";
        }
        return value;
    }else {
        return value;
    }
}

