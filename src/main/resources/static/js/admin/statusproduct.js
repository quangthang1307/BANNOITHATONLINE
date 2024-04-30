var app = angular.module('bannoithatonline', []);

app.controller('StatusProduct', function ($scope, $http, $window,$timeout) {
    function updateUpdateButtonStatus() {
        document.querySelectorAll('select[name^="orderstatusname"]').forEach(function(selectElement) {
            // var selectedValue = selectElement.value;
            // var updateButton = selectElement.closest('form').querySelector('button[type="submit"]');
            // if (selectedValue === 'Giao hàng thành công') {
            //     updateButton.style.display = 'none';
            //     selectElement.disabled = true;
            //     selectElement.style.border = '2px solid green';
            // } else if(selectedValue === 'Hủy đơn hàng'){
            //     updateButton.style.display = 'none';
            //     selectElement.disabled = true;
            //     selectElement.style.border = '2px solid red';
            // }else if(selectedValue === 'Giao hàng thanh toán') {
            //     selectElement.options[1].disabled  = true;
            //     selectElement.options[0].disabled  = true;
            // }else if(selectedValue === 'Đang giao hàng') {
            //     selectElement.options[0].disabled  = true;
            // }else{
            //     // Bỏ vô hiệu hóa tất cả các phần tử option
            //      for (var i = 0; i < selectElement.options.length; i++) {
            //     selectElement.options[i].disabled = false;
            // }
            // }
            

            var selectedOptionIndex = selectElement.selectedIndex;
            var updateButton = selectElement.closest('form').querySelector('button[type="submit"]');
        
        if (selectedOptionIndex === 5) { // Hủy đơn hàng
            updateButton.style.display = 'none';
            selectElement.disabled = true;
            selectElement.style.border = '2px solid red';
        } else if(selectedOptionIndex === 4) { // Giao hàng thành công
            updateButton.style.display = 'none';
            selectElement.disabled = true;
            selectElement.style.border = '2px solid green';
        } else if(selectedOptionIndex === 3) { // Thanh toán thành công
            selectElement.options[2].disabled = true;
            selectElement.options[1].disabled = true;
            selectElement.options[0].disabled = true;
        } else if(selectedOptionIndex === 2) { // Đang giao hàng
            selectElement.options[1].disabled = true;
            selectElement.options[0].disabled = true;
        } else if(selectedOptionIndex === 1) { // Đã xác nhận
            selectElement.options[0].disabled = true;
        } else { // Trường hợp khác // Đã xác nhận
            // Bỏ vô hiệu hóa tất cả các phần tử option
            for (var i = 0; i < selectElement.options.length; i++) {
                selectElement.options[i].disabled = false;
            }
        }
        });
    }
    
    $scope.deleteOrder = function(event) {
        var orderId = event.target.previousElementSibling.textContent;
        console.log(orderId);
        Swal.fire({
            title: "Bạn có chắc chắn muốn xóa đơn hàng " + orderId + " này không?",
            text: "Hành động này sẽ không thể hoàn tác!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Có, tôi đồng ý!"
        }).then((result) => {
            if (result.isConfirmed) {
                $http.delete("/admin/deleteoder/" + orderId)
                    .then(function(response) {
                        Swal.fire({
                            icon: "success",
                            title: "Xóa thành công",
                            text: "Đơn hàng " + orderId + " đã được xóa thành công!",
                        }).then(() => {
                            // Reload lại trang sau khi xác nhận xóa thành công
                            setTimeout(function() {
                                window.location.reload();
                            }, 1000);
                        });
                    })
                    .catch(function(error) {
                        // Xảy ra lỗi khi xóa đơn hàng, hiển thị thông báo lỗi
                        Swal.fire({
                            icon: "error",
                            title: "Lỗi",
                            text: "Đã xảy ra lỗi khi xóa đơn hàng " + orderId + " . Vui lòng thử lại sau.",
                        });
                    });
            }
        });
    }
    window.onload = function() {
        updateUpdateButtonStatus();
    };
});
