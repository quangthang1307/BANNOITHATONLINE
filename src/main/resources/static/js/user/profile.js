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
        var newAddress = {
            tinhthanhpho: $scope.selectedProvince.Name,
            quanhuyen: $scope.selectedDistrict.Name,
            phuongxa: $scope.selectedWard.Name,
            sonha: $scope.newAddress.sonha,
            duong: $scope.newAddress.duong
        };
        $http.post("/rest/profile/customers/" + $scope.customerId + "/addresses", newAddress)
        .then(function(response) {
            $scope.newAddress = {
                sonha: '',
                duong: ''
            };
            fetchAddresses($scope.customerId);
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
        });
    }
    
    $scope.deleteAddress = function(addressId) {
        $http.delete("/rest/profile/customers/" + $scope.customerId + "/addresses/" + addressId)
        .then(function(response) {
            fetchAddresses($scope.customerId);
        });
    }

      
});
