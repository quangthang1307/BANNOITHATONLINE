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
            
            var selectedOptionValue = selectElement.value;
            var updateButton = selectElement.closest('form').querySelector('button[type="submit"]');
        
        if (selectedOptionValue === "Đã hủy") { // Hủy đơn hàng
            updateButton.style.display = 'none';
            selectElement.disabled = true;
            selectElement.style.border = '2px solid red';
        } else if(selectedOptionValue === "Giao hàng thành công") { // Giao hàng thành công
            updateButton.style.display = 'none';
            selectElement.disabled = true;
            selectElement.style.border = '2px solid green';
        } else if(selectedOptionValue === "Thanh toán thành công") { // Thanh toán thành công
            // selectElement.options[2].style.display = 'none';
            // selectElement.options[1].style.display = 'none';
            // selectElement.options[0].style.display = 'none';
            for (var i = 0; i < selectElement.options.length; i++) {
                if (selectElement.options[i].value === "Chờ xác nhận" || selectElement.options[i].value === "Đã xác nhận" || selectElement.options[i].value === "Đang giao hàng") {
                    selectElement.options[i].style.display = 'none';
                }
            }
        } else if(selectedOptionValue === "Đang giao hàng") { // Đang giao hàng
            // selectElement.options[1].style.display = 'none';
            // selectElement.options[0].style.display = 'none';
            for (var i = 0; i < selectElement.options.length; i++) {
                if (selectElement.options[i].value === "Chờ xác nhận" || selectElement.options[i].value === "Đã xác nhận") {
                    selectElement.options[i].style.display = 'none';
                }
            }
        } else if(selectedOptionValue === "Đã xác nhận") { // Đã xác nhận
            for (var i = 0; i < selectElement.options.length; i++) {
                if (selectElement.options[i].value === "Chờ xác nhận") {
                    selectElement.options[i].style.display = 'none';
                    break; // Khi đã tìm được và ẩn phần tử "Chờ xác nhận", ta thoát khỏi vòng lặp
                }
            }
            // selectElement.options[0].style.display = 'none';
        } else { // Trường hợp khác // Đã xác nhận
            // Bỏ vô hiệu hóa tất cả các phần tử option
            for (var i = 0; i < selectElement.options.length; i++) {
                selectElement.options[i].style.display = 'block';
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
