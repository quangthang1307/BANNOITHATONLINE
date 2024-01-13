var app = angular.module("bannoithatonline", []);
const hostListCart = "http://localhost:8080/rest/showCart";
const hostUpQuantityProduct = "http://localhost:8080/rest/cart/up";
const hostDownQuantityProduct = "http://localhost:8080/rest/cart/down";
const hostDeleteProduct = "http://localhost:8080/rest/removeFromCart";

app.controller("CartController", function ($scope, $http) {
  $scope.listCart = [];
  $scope.listCartId = [];

  var urlListCart = `${hostListCart}/1`;
  $http.get(urlListCart).then((resp) => {
    $scope.listCart = resp.data;
    console.log($scope.listCart);

    $scope.checkboxClicked = function (cart) {
        var index = $scope.listCartId.findIndex(item => item.cartId === cart.cartId);
      if (index === -1) {
        $scope.listCartId.push(cart);
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
        var urlUpQuantityProduct = `${hostUpQuantityProduct}/1/${cart.product.productid}`;
        $http.put(urlUpQuantityProduct).then((resp) => {
          $http.get(urlListCart).then((resp) => {
            $scope.listCart = resp.data;
            console.log($scope.listCart);
          });
        })
      }
    });
  };

  $scope.downQuantityProduct = function (cart) {
    var urlDeleteProduct = `${hostDeleteProduct}/${cart.cartId}`;
    $scope.listCart.forEach((element) => {
        console.log(cart.cartId);
      if (element.cartId == cart.cartId) {
        var urlUpQuantityProduct = `${hostDownQuantityProduct}/1/${cart.product.productid}` ;
        $http.put(urlUpQuantityProduct).then((resp) => {
          $http.get(urlListCart).then((resp) => {
            $scope.listCart = resp.data;
            console.log($scope.listCart);
          });
        }).catch((err) => {
            Swal.fire({
                title: 'Bạn có chắc muốn xóa không?',
                text: 'Hành động này sẽ xóa vĩnh viễn dữ liệu!',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Xóa',
                cancelButtonText: 'Hủy bỏ'
            }).then((result) => {
                if (result.isConfirmed) {
                    $http.delete(urlDeleteProduct).then(resp => {
                        $http.get(urlListCart).then((resp) => {
                            $scope.listCart = resp.data;});
                    });
                    Swal.fire('Đã xóa!', 'Sản phẩm đã được xóa khỏi giỏ hàng.', 'Thành công');
                } 
            });
        });
      }
    });
  };

  $scope.deleteProduct = function(cart){
    var urlDeleteProduct = `${hostDeleteProduct}/${cart.cartId}`;

    Swal.fire({
        title: 'Bạn có chắc muốn xóa không?',
        text: 'Hành động này sẽ xóa vĩnh viễn dữ liệu!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xóa',
        cancelButtonText: 'Hủy bỏ'
    }).then((result) => {
        if (result.isConfirmed) {
            $http.delete(urlDeleteProduct).then(resp => {
                $http.get(urlListCart).then((resp) => {
                    $scope.listCart = resp.data;});
            });
            Swal.fire('Đã xóa!', 'Sản phẩm đã được xóa khỏi giỏ hàng.', 'Thành công');
        } 
    });
  }









});
