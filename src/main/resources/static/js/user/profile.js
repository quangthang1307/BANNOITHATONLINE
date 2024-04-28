var app = angular.module('bannoithatonline', []);

app.controller('ProfileController', function ($scope, $http, $window) {

    $http.get("/rest/profile/customer")
        .then(function (response) {
            $scope.customer = response.data;
            // $scope.customerId = $scope.customer.customerId;
            // console.log($scope.customerId);
        });

    // $http.get("/rest/customers/" + $scope.customerId + "/addresses")
    // .then(function(response) {
    //     $scope.addresses = response.data;
    // });

    function getCustomerId() {
        return $http.get("/rest/profile/customer").then(function (response) {
            return response.data.customerId;
        });
    }

    getCustomerId().then(function (customerId) {
        $scope.customerId = customerId;

        fetchAddresses(customerId);
        console.log(customerId);
    });

    function fetchAddresses(customerId) {
        $http.get("/rest/profile/customers/" + customerId + "/addresses")
            .then(function (response) {
                $scope.addresses = response.data;
            });
    }

    $scope.newAddress = {
        sonha: '',
        duong: ''
    };

    $scope.addAddress = function() {

        if (!$scope.validateSonha()) {
            // Nếu dữ liệu đường không hợp lệ, không thêm mới địa chỉ
            return;
        }
        if (!$scope.validateDuong()) {
            // Nếu dữ liệu đường không hợp lệ, không thêm mới địa chỉ
            return;
        }
        var newAddress = {
            tinhthanhpho: $scope.selectedProvince.Name,
            quanhuyen: $scope.selectedDistrict.Name,
            phuongxa: $scope.selectedWard.Name,
            sonha: $scope.newAddress.sonha,
            duong: $scope.newAddress.duong,
            status: true
        };
        $http.post("/rest/profile/customers/" + $scope.customerId + "/addresses", newAddress)
        .then(function(response) {
            $scope.newAddress = {
                sonha: '',
                duong: ''
            };
            fetchAddresses($scope.customerId);
            Swal.fire({
                icon: "success",
                title: "Thêm mới thành công",
                text: "Thông tin địa chỉ đã được thêm mới !",
                showConfirmButton: true,
              });
        });
    }

    $http.get('/js/data.json')
        .then(function(response) {
            $scope.provinces = response.data;
        });

    $scope.provinces = [];
    $scope.selectedProvince = null;
    $scope.selectedDistrict = null;
    $scope.selectedWard = null;

    $scope.updateDistricts = function () {
        $scope.selectedDistrict = null;
        $scope.selectedWard = null;
    }

    $scope.updateWards = function () {
        $scope.selectedWard = null;
    }

    $scope.editAddress = function (address) {
        $scope.editingAddress = angular.copy(address);
        document.getElementById('editTinhthanhpho').value = address.tinhthanhpho;
        document.getElementById('editQuanhuyen').value = address.quanhuyen;
        document.getElementById('editPhuongxa').value = address.phuongxa;
        document.getElementById('editSonha').value = address.sonha;
        document.getElementById('editduong').value = address.duong;
        document.getElementById('updateAddressForm').style.display = 'block';
    }

    $scope.updateAddress = function() {
        var newAddress = {
            tinhthanhpho: $scope.selectedProvince.Name,
            quanhuyen: $scope.selectedDistrict.Name,
            phuongxa: $scope.selectedWard.Name,
            sonha: document.getElementById('updateSonha').value,
            duong: document.getElementById('updateduong').value
        };
        $http.put("/rest/profile/customers/" + $scope.customerId + "/addresses/" + $scope.editingAddress.addressID, newAddress)
        .then(function(response) {
            $scope.editingAddress = null;
            document.getElementById('updateAddressForm').style.display = 'none';
            fetchAddresses($scope.customerId);
            Swal.fire({
                icon: "success",
                title: "Cập nhật thành công",
                text: "Thông tin địa chỉ đã được cập nhật !",
                showConfirmButton: true,
              });
        });
    }
    
    $scope.deleteAddress = function(addressId) {
        Swal.fire({
            title: "Bạn có chắc chắn muốn xóa không?",
            text: "Địa chỉ đang chọn!",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Có, tôi đồng ý!"
          }).then((result) => {
            if (result.isConfirmed) {
                $http.delete("/rest/profile/customers/" + $scope.customerId + "/addresses/" + addressId)
        .then(function(response) {
            fetchAddresses($scope.customerId);
            
        });
              Swal.fire({
                icon: "success",
                title: "Xóa thành công",
                text: "Thông tin địa chỉ đã xóa!",
              });
            }
          });
        
    }

// Bắt lỗi 

    $scope.validateSonha = function() {
        // Sử dụng biểu thức chính quy để kiểm tra xem số nhà có chứa ký tự đặc biệt không
        var regex = /^[0-9]+$/;
        if (!regex.test($scope.newAddress.sonha)) {
            // Nếu không hợp lệ, hiển thị thông báo lỗi
            Swal.fire({
                icon: "error",
                title: "Lỗi",
                text: "Số nhà chỉ được chứa ký tự số!",
                showConfirmButton: true,
            });
            // Xóa nội dung nhập sai
            $scope.newAddress.sonha = '';
            return false;
        }
        return true;
    };

    $scope.validateDuong = function() {
        // Sử dụng biểu thức chính quy để kiểm tra xem đường có chứa ký tự đặc biệt không
        var regex = /^[a-zA-Z0-9\s]+$/;
        if (!regex.test($scope.newAddress.duong)) {
            // Nếu không hợp lệ, hiển thị thông báo lỗi
            Swal.fire({
                icon: "error",
                title: "Lỗi",
                text: "Đường không được chứa ký tự đặc biệt!",
                showConfirmButton: true,
            });
            // Xóa nội dung nhập sai
            $scope.newAddress.duong = '';
            return false;
        }
        return true;
    };


      
});
