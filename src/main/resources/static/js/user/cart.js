var app = angular.module("bannoithatonline", []);
const hostListCart = "http://localhost:8080/rest/showCart";

app.controller("CartController", function ($scope, $http) {
  $scope.listCart = [];
  $scope.listCartId = [];



  var urlListCart = `${hostListCart}/1`;
  $http.get(urlListCart).then((resp) => {
    $scope.listCart = resp.data;
    console.log($scope.listCart);



    $scope.checkboxClicked = function (cart) {
        var index = $scope.listCartId.indexOf(cart.cartId);
        if (index === -1) {
            $scope.listCartId.push(cart.cartId);
        } else {
            $scope.listCartId.splice(index, 1);
        }    
        console.log("Checkbox clicked for cart with ID:", cart.cartId);
        console.log($scope.listCartId);
    };
    
  });





});
