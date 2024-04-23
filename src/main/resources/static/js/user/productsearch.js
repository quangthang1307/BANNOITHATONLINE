var app = angular.module("bannoithatonline", []);

app.controller('productSearchController', function ($scope, $http) {
    $scope.dataProductSearch = [];
    $scope.valueSearch = "";
    $scope.lengthProduct = 0;
    $scope.productSale = null;
    $scope.productFlashSale = null;
    $scope.inventory = [];

    $scope.getDataProductSearch = function () {
        $scope.dataProductSearch = JSON.parse(localStorage.getItem('productBySearch'));
        $scope.valueSearch = JSON.parse(localStorage.getItem('valueSearch'));
        $scope.productSale = JSON.parse(localStorage.getItem('productSale'));
        $scope.productFlashSale = JSON.parse(localStorage.getItem('productFlashSale'));
        $scope.inventory = JSON.parse(localStorage.getItem('productInventory'));
        $scope.lengthProduct = $scope.dataProductSearch.length;
    }

        //Kiểm tra sản phẩm sale để thay đổi giá
        $scope.isProductInSale = function (productId) {
            return $scope.productSale.some(item => item.productID === productId);
        };
    
        $scope.getPercentSaleForProduct = function (productId) {
            var foundItem = $scope.productSale.find(item => item.productID === productId);
            return foundItem ? foundItem.percent : null;
        };
        //
        //Kiểm tra sản phẩm Flashsale để thay đổi giá
        $scope.isProductInFlashSale = function (productId) {
            return $scope.productFlashSale.some(item => item.product.productid === productId);
        };
    
        $scope.getPercentFlashSaleForProduct = function (productId) {
            var foundItem = $scope.productFlashSale.find(item => item.product.productid === productId);
            return foundItem ? foundItem.percent : null;
        };
        //
        //Kiểm tra tồn kho
        $scope.checkInventory = function (productId) {
            const product = $scope.inventory.find(item => item.product.productid === productId);
            return product && product.quantityonhand > 0;
        }
        //

    $scope.getDataProductSearch();
});