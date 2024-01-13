var app = angular.module('productApp', []);
const host = "http://localhost:8080/rest/product";

app.controller('productController', function ($scope, $http) {

    $http.get('/rest/product')
    .then(function (response) {
        $scope.products = response.data;
        console.log(response.data);
    })
    .catch(function (error) {
        console.error('Error fetching products:', error);
    });
    
});