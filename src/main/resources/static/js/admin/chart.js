function updateChart(selectedYear) {
    $.ajax({
        type: "GET",
        url: "/admin/rest/statics/data",
        data: { selectedYear: selectedYear },
        dataType: "json",
        success: function (data) {
            chart.series[0].setData(data);
            chart.setTitle({ text: 'Bảng thống kê doanh thu của năm ' + selectedYear });
        },
        error: function (error) {
            console.error("Error fetching data:", error);
        }
    });

    // Cập nhật biểu đồ thứ hai
    $.ajax({
        type: "GET",
        url: "/admin/rest/statics/categoryData",
        data: { selectedYear: selectedYear },
        dataType: "json",
        success: function (data) {
            chart2.series[0].setData(data);
            chart2.setTitle({ text: 'Phân loại danh mục đã bán của năm ' + selectedYear });
        },
        error: function (error) {
            console.error("Error fetching category data:", error);
        }
    });
}

var currentYear = new Date().getFullYear();

var chart = Highcharts.chart('container', {
    chart: {
        type: 'column'
    },
    title: {
        text: 'Bảng thống kê doanh thu của năm ' + currentYear,
        align: 'left'
    },
    xAxis: {
        categories: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'],
        crosshair: true
    },
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
    ]
});

var chart2 = Highcharts.chart('container1', {
    chart: {
        type: 'pie'
    },
    title: {
        text: 'Phân loại danh mục đã bán'
    },
    tooltip: {
        valueSuffix: ''
    },
    subtitle: {
        text:
            'Biểu đồ:<a href="https://www.mdpi.com/2072-6643/11/3/684/htm" target="_default"></a>'
    },
    plotOptions: {
        series: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: [{
                enabled: true,
                distance: 20
            }, {
                enabled: true,
                distance: -40,
                format: '{point.percentage:.1f}%',
                style: {
                    fontSize: '1.2em',
                    textOutline: 'none',
                    opacity: 0.7
                },
                filter: {
                    operator: '>',
                    property: 'percentage',
                    value: 10
                }
            }]
        }
    },
    // series: [
    //     {
    //         name: 'Percentage',
    //         colorByPoint: true,
    //         data: [
    //             {
    //                 name: 'Water',
    //                 y: 55.02
    //             },
    //             {
    //                 name: 'Fat',
    //                 sliced: true,
    //                 selected: true,
    //                 y: 26.71
    //             },
    //             {
    //                 name: 'Carbohydrates',
    //                 y: 1.09
    //             },
    //             {
    //                 name: 'Protein',
    //                 y: 15.5
    //             },
    //             {
    //                 name: 'Ash',
    //                 y: 1.68
    //             }
    //         ]
    //     }
    // ],
    series: [{
        name: 'Số lượng',
        colorByPoint: true,
        data: chartData
    }]
});

$('#yearForm').submit(function (event) {
    event.preventDefault();
    var selectedYear = $('#year').val();
    updateChart(selectedYear);
});


