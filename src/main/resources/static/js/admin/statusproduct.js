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
        
        if (selectedOptionIndex === 4) { // Hủy đơn hàng
            updateButton.style.display = 'none';
            selectElement.disabled = true;
            selectElement.style.border = '2px solid red';
        } else if(selectedOptionIndex === 3) { // Giao hàng thành công
            updateButton.style.display = 'none';
            selectElement.disabled = true;
            selectElement.style.border = '2px solid green';
        } else if(selectedOptionIndex === 2) { // Giao hàng thanh toán
            selectElement.options[1].disabled = true;
            selectElement.options[0].disabled = true;
        } else if(selectedOptionIndex === 1) { // Đang giao hàng
            selectElement.options[0].disabled = true;
        } else { // Trường hợp khác
            // Bỏ vô hiệu hóa tất cả các phần tử option
            for (var i = 0; i < selectElement.options.length; i++) {
                selectElement.options[i].disabled = false;
            }
        }
        });
    }
    
    window.onload = function() {
        updateUpdateButtonStatus();
    };
});
