var $table = $('#table').bootstrapTable({
    url: 'noticeList',
    method:'POST',
    dataType:'json',
    contentType: "application/x-www-form-urlencoded",
    singleSelect: true,
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
            // sort: params.sort,      //排序列名
            // sortOrder: params.order, //排序命令（desc，asc）
            // searchKey: params.search
            // param : "Your Param" //这里是其他的参数，根据自己的需求定义，可以是多个
        }
    },
    sidePagination:'server',//指定服务器端分页
    pageSize:10,//单页记录数
    pageList:[10],//分页步进值
    paginationPreText: '<',//指定分页条中上一页按钮的图标或文字,这里是<
    paginationNextText: '>',//指定分页条中下一页按钮的图标或文字,这里是>
});


function actionFormatter(value) {
    return [
        '<a class="look" href="javascript:" title="查看" style="margin:0 10px 0 0">查看</a>',
        '<a class="update" href="javascript:" title="编辑" style="margin:0 10px 0 10px">编辑</a>',
    ].join('');
}
function actionFormatter0(value) {
    return [
        '<a class="look" href="javascript:" title="查看" style="margin:0 10px 0 0">查看</a>',
    ].join('');
}

window.actionEvents = {
    'click .look': function (e, value, row) {
        window.location.href="readNotice?id=" + row.id;
    },
    'click .update': function (e, value, row) {
        window.location.href="editNotice?id=" + row.id;
    },
};
$(function () {
    // create notice event
    $('.create').click(function () {
        window.location.href="editNotice";
    });
});