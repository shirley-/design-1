/**
 * echart 会员推荐结构
 */
var myChart = echarts.init(document.getElementById('main'));
window.onresize = myChart.resize;
// 指定图表的配置项和数据
function bindTreeChartData(jsonData) {
    console.log(jsonData);
    var option = {
        title : {
            text: '',
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
                    rootLocation: {x: 'center', y: 5}, // 根节点位置  {x: 100, y: 'center'}
                    nodePadding: 20,
                    symbol: 'circle',
                    symbolSize: [80,50],
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
    myChart.clear();
    myChart.setOption(option);
}




/**
 * treeview
 */
$(function () {
    // show member organization event
    var id = $('#userId').val();
    $.ajax({
        url: '/member/memberOrgTreeview?id='+id,
        type: 'get',
        contentType: 'application/json',
        dataType:"json",
        success: function (data) {
            if(data.code == 0) {
                $('#childrenCount').text(JSON.parse(data.msg)[0].childrenCount);
                $('#tree').treeview({data: data.msg});
            }
        },
        error: function (e) {
            // console.log(e.responseJSON.msg);
        }
    });
    $.ajax({
        url: '/member/memberOrgTreeChart?id='+id,
        type: 'get',
        contentType: 'application/json',
        dataType: "json",
        success: function (data) {
            if(data.code == 0) {
                bindTreeChartData(JSON.parse(data.msg));
            }
        },
        error: function (e) {
        }
    });
});
