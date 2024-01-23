Highcharts.chart('container', {
    chart: {
        type: 'column'
    },
    title: {
        text: 'Corn vs wheat estimated production for 2020',
        align: 'left'
    },
    subtitle: {
        text:
            'Source: <a target="_blank" ' +
            'href="https://www.indexmundi.com/agriculture/?commodity=corn">indexmundi</a>',
        align: 'left'
    },
    xAxis: {
        categories: ['Tháng 1'],
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
            name: 'Quần áo',
            data: [sumpayment]
        },
        // {
        //     name: 'Wheat',
        //     data: [51086, 136000, 5500, 141000, 107180, 77000]
        // }
    ]
});
