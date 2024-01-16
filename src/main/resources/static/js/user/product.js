var app = angular.module('bannoithatonline', []);
const host = "http://localhost:8080/rest/product";
const hostCustomerId = "http://localhost:8080/rest/customer";

app.controller('productController', function ($scope, $http, $window) {

    // Gán CustomerId người dùng
    $http.get(hostCustomerId).then(function (response) {
        $scope.customer = response.data;
        $window.localStorage.setItem("customerId", JSON.stringify($scope.customer));
      });

    $http.get('/rest/product')
    .then(function (response) {
        $scope.products = response.data;
        console.log(response.data);
    })
    .catch(function (error) {
        console.error('Error fetching products:', error);
    });
    
});