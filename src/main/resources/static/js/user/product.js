var app = angular.module('bannoithatonline', ['ui.bootstrap']);
const host = "http://localhost:8080/rest/product";

app.controller('productController', function ($scope, $http) {

    // $http.get('/rest/product')
    // .then(function (response) {
    //     $scope.products = response.data;
    //     console.log(response.data);
    // })
    // .catch(function (error) {
    //     console.error('Error fetching products:', error);
    // });

    $scope.itemsPerPage = 9;
    $scope.currentPage = 1;
    $scope.totalItems = 0;
    $scope.totalPages = 0;
    $scope.products = [];

    $scope.getData = function () {

        var apiUrl = host + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage;

        $http.get(apiUrl)
            .then(function (response) {

                $scope.products = response.data.content;
                console.log($scope.products);

                $scope.totalItems = response.data.totalElements;
                console.log($scope.totalItems);

                $scope.totalPages = parseInt(response.data.totalPages, 10);
                console.log($scope.totalPages);

            })
            .catch(function (error) {
                console.error('Error fetching products:', error);
            });
    };


    $scope.goToPage = function (page) {
        $scope.currentPage = page;
        $scope.getData();
    };


    $scope.numberArray = function () {
        var result = [];
        for (var i = 1; i <= $scope.totalPages; i++) {
            result.push(i);
        }
        return result;
    }

    $scope.clickById = function (productId) {
        var url = host + '/' + productId;
        console.log(url);
        $http.get(url)
            .then(resp => {

                console.log("Success", resp.data);
                // Trong controller hiện tại, lưu dữ liệu dạng Json và localStorage
                localStorage.setItem('productById', JSON.stringify(resp.data));

                // Chuyển hướng đến trang chi tiết sản phẩm
                window.location.href = `/productdetail/${productId}`;
                //  console.log("Success", resp);
            })
            .catch(error => {
                console.log("Error", error);
            });
    }

    // Load data for the first time
    $scope.getData();
});

app.controller('productDetailController', function ($scope, $http) {

    $scope.prodcuctdetail = JSON.parse(localStorage.getItem('productById'));

    console.log("ProductDetail", $scope.prodcuctdetail);
});