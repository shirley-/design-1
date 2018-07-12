var options = {
    currentPage: $('#pageIndex').val(), //设置当前页，默认起始页为第一页
    totalPages: $('#orderListTotal').val()%10==0?$('#orderListTotal').val()/10:parseInt($('#orderListTotal').val()/10) +1 , //总页数,后台已设置每页10条记录
    numberOfPages: 5 , //设置控件显示的页码数,跟后台计算出来的总页数没多大关系
    bootstrapMajorVersion:3,//如果是bootstrap3版本需要加此标识，并且设置包含分页内容的DOM元素为UL,如果是bootstrap2版本，则DOM包含元素是DIV
    useBootstrapTooltip: false,//是否显示tip提示框
    pageUrl:function(type, page, current){
        //为每个页码设置url访问请求链接，page为页码数
        var url = '/member/trade?s=orderListPage&pageIndex='+page;
        if($('#sort').val() && $('#sortOrder').val()) {
            url = url + '&sort=' + $('#sort').val() + '&sortOrder=' +$('#sortOrder').val();
        }
        //为每个页码设置url访问请求链接，page为页码数
        return url;
    },
    tooltipTitles: function (type, page, current) {
        return "";
    },
    shouldShowPage: function (type, page, current) {
        var result = true;
        switch (type) {
            case "first":
                result = true;
                // result = (current !== 1);
                break;
            case "prev":
                result = true;
                // result = (current !== 1);
                break;
            case "next":
                result = true;
                // result = (current !== this.totalPages);
                break;
            case "last":
                result = true;
                // result = (current !== this.totalPages);
                break;
            case "page":
                result = true;
                break;
        }
        return result;
    }
}

$('.pagination').bootstrapPaginator(options);



function cancelSellTry(id) {

    if(checkTradeTime()) {
        var data={"id": id, "action": "cancelSellTry", "title":"撤销挂卖"};
        showTradeModal("撤销挂卖", data);
    }
    /*$.ajax({
        url: "cancelSellTry",
        type: "post",
        dataType: "json",
        data: {'id' : id},
        success: function(result) {
            if(result.code == 0) {
                // var line = result.msg;
                showSuccessAlert("撤销挂卖成功!");
            }else {
                showErrorAlert("撤销挂卖失败! " + result.msg);
            }
        },
        error: function(e) {
            showErrorAlert("撤销挂卖失败! " +e);
        }
    });*/
}

function buyVc(id, ecoin, userEcoin) {
    if(userEcoin<ecoin) {//用户持有ecoin不足以买这笔
        showErrorAlert('余额不足，购买失败!');
        return;
    }

    if(checkTradeTime()) {
        var data={"id": id, "action": "buyVc", "title":"购买KTH", "choose": 1};
        showTradeModal("购买KTH", data);
    }

   /* $.ajax({
        url: "buyVc",
        type: "post",
        dataType: "json",
        data: {'id' : id},
        success: function(result) {
            if(result.code == 0) {
                var line = result.msg;
                showSuccessAlert('购买成功!');
            }else {
                showErrorAlert('购买失败!');

            }
        },
        error: function(e) {
            showErrorAlert('购买失败!');
        }
    });*/
}

