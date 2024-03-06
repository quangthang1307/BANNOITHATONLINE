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

        fetchProfiles(customerId);
        console.log(customerId);
    });

    function fetchProfiles(customerId) {
        $http.get("/rest/customers/" + customerId)
            .then(function (response) {
                $scope.profiles = response.data;
            });
    }

    $scope.updateProfile = function() {
        var newAddress = {
            name: document.getElementById('updatename').value,
            phone:document.getElementById('updatephone').value
        };
        $http.put("/rest/customers/" + $scope.customerId,newAddress)
        .then(function(response) {
            document.getElementById('updateProfileForm').style.display = 'none';
            window.location.reload();
            fetchProfiles($scope.customerId);
        });
    }
    $scope.editProfile = function() {
            document.getElementById('updateProfileForm').style.display = 'inline';
    }
    

      
});
