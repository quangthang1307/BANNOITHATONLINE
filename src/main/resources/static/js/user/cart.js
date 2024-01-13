var app = angular.module("bannoithatonline", []);
const hostListCart = "http://localhost:8080/rest/showCart";
const hostUpQuantityProduct = "http://localhost:8080/rest/cart/up";
const hostDownQuantityProduct = "http://localhost:8080/rest/cart/down";

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

  $scope.upQuantityProduct = function (cart) {
    $scope.listCart.forEach((element) => {
        console.log(cart.cartId);
      if (element.cartId == cart.cartId) {
        var urlUpQuantityProduct = `${hostUpQuantityProduct}/1/1`;
        $http.put(urlUpQuantityProduct).then((resp) => {
          $http.get(urlListCart).then((resp) => {
            $scope.listCart = resp.data;
            console.log($scope.listCart);
          });
        });
      }
    });
  };

  $scope.downQuantityProduct = function (cart) {
    $scope.listCart.forEach((element) => {
        console.log(cart.cartId);
      if (element.cartId == cart.cartId) {
        var urlUpQuantityProduct = `${hostDownQuantityProduct}/1/1`;
        $http.put(urlUpQuantityProduct).then((resp) => {
          $http.get(urlListCart).then((resp) => {
            $scope.listCart = resp.data;
            console.log($scope.listCart);
          });
        });
      }
    });
  };
});
