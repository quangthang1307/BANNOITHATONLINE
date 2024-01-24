Highcharts.chart('container', {
    chart: {
        type: 'column'
    },
    title: {
        text: 'Bảng thống kê doanh thu của năm',
        align: 'left'
    },
    // subtitle: {
    //     text:
    //         'Source: <a target="_blank" ' +
    //         'href="https://www.indexmundi.com/agriculture/?commodity=corn">indexmundi</a>',
    //     align: 'left'
    // },
    xAxis: {
        categories: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
        crosshair: true
    },
    // yAxis: {
    //     min: 0,
    //     title: {
    //         text: '1000 metric tons (MT)'
    //     }
    // },
    // tooltip: {
    //     valueSuffix: ' (1000 MT)'
    // },
    plotOptions: {
        column: {
            pointPadding: 0.2,
            borderWidth: 0
        }
    },
    series: [
        {
            name: 'Doanh thu',
            data: sumpayment
        },

        {
            name: 'Doanh thu',
            data: sumpaymentWithVNPay
        },
        // {
        //     name: 'Wheat',
        //     data: [51086, 136000, 5500, 141000, 107180, 77000]
        // }
    ]
});
