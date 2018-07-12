$(function () {
    var myChart = echarts.init(document.getElementById('main'));
    /*myChart.showLoading({
     text: '正在努力的读取数据中...',
     });*/
    /*用来配置参数*/
    /*var option = {
        title: {
            text: '动态数据 + 时间坐标轴'
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                params = params[0];
                var date = new Date(params.name);
                return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear() + ' : ' + params.value[1];
            },
            axisPointer: {
                animation: false
            }
        },
        xAxis: {
            type: 'time',
            splitLine: {
                show: false
            }
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: false
            }
        },
        series: [
            {
                name: '模拟数据',
                type: 'line',
                showSymbol: false,
                hoverAnimation: false,
                data: data
            },
            {
                name: '.anchor',
                type: 'line',
                showSymbol: false,
                data: anchor,
                itemStyle: {normal: {opacity: 0}},
                lineStyle: {normal: {opacity: 0}}
            }
        ]
    };*/
    var data = [
        {name:'2016/12/17 6:38:08', value:['2016/12/17 6:38:08', 80]},
        {name:'2016/12/18 16:18:18', value:['2016/12/18 16:18:18', 60]},
        {name:'2016/12/18 19:18:18', value:['2016/12/18 19:18:18', 90]}
    ];
    var anchor = [
        {name:'2016/12/16 00:00:00', value:['2016/12/16 00:00:00', 0]},
        {name:'2016/12/25 00:00:00', value:['2016/12/25 00:00:00', 0]}
    ];

    option = {
        title: {
            text: '动态数据 + 时间坐标轴'
        },
        tooltip: {
            trigger: 'axis',
            formatter: function (params) {
                params = params[0];
                var date = new Date(params.name);
                return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear() + ' : ' + params.value[1];
            },
            axisPointer: {
                animation: false
            }
        },
        xAxis: {
            type: 'time',
            splitLine: {
                show: false
            }
        },
        yAxis: {
            type: 'value',
            boundaryGap: [0, '100%'],
            splitLine: {
                show: false
            }
        },
        series: [{
            name: '模拟数据',
            type: 'line',
            showSymbol: false,
            hoverAnimation: false,
            data: data
        },
            {
                name:'.anchor',
                type:'line',
                showSymbol:false,
                data:anchor,
                itemStyle:{normal:{opacity:0}},
                lineStyle:{normal:{opacity:0}}
            }]
    };
    /*调用option生成图表*/
    myChart.setOption(option);


    // ajax getting data...............
    /*$.ajax({
        url: '/getData',
        type: 'post',
        dataType: 'json',
        success: function (e) {
            var p_y = [];
            $.each(e, function (i, result) {
                var t = new Date(Date.parse(result.time.replace(/-/g, "/")))
                var p = result.value;
                p_y.push([t, p]);
            });
            // ajax callback
            myChart.hideLoading();
            var option = {
                title: {
                    text: 'KMC趋势图',
                },
                tooltip: {
                    trigger: 'item',
                    formatter: function (params) {
                        var date = new Date(params.value[0]);
                        data = date.getFullYear() + '-'
                            + (date.getMonth() + 1) + '-'
                            + date.getDate() + ' '
                            + date.getHours() + ':'
                            + date.getMinutes() + ':'
                            + date.getSeconds();
                        return data + '<br/>'
                            + params.value[1] + 'kW';
                    }
                },
                toolbox: {
                    show: true,
                    feature: {
                        dataView: {show: true, readOnly: false},
                        magicType: {show: true, type: ['line', 'bar']},
                        restore: {show: true},
                        saveAsImage: {show: true}
                    }
                },
                dataZoom: {
                    show: true,
                    start: 0,
                    end: 100
                },
                grid: {
                    x: 50,
                    x2: 20
                },
                xAxis: [
                    {
                        type: 'time',
                        splitNumber: 10
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: [
                    {
                        name: 'test',
                        type: 'line',
                        showAllSymbol: true,
                        symbolSize: 2,
                        data: p_y
                    }
                ]
            };
            myChart.setOption(option);*/
});