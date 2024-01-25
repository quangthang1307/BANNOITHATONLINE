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

$('#yearForm').submit(function (event) {
    event.preventDefault();
    var selectedYear = $('#year').val();
    updateChart(selectedYear);
});


