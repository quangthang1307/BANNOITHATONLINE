const app = angular.module("bannoithatonline", []);

const hostListCart = "http://localhost:8080/rest/showCart";
const hostUpQuantityProduct = "http://localhost:8080/rest/cart/up";
const hostDownQuantityProduct = "http://localhost:8080/rest/cart/down";
const hostDeleteProduct = "http://localhost:8080/rest/removeFromCart";
const hostProductImage = "http://localhost:8080/rest/products";

app.controller("CartController", function ($scope, $http) {
  $scope.listCart = [];
  $scope.listCartId = [];

  var urlListCart = `${hostListCart}/1`;
  $scope.loadCart = function () {
    $http.get(urlListCart).then((resp) => {
      $scope.listCart = resp.data;
  
      // Lặp qua mỗi mục trong $scope.listCart để thêm URL hình ảnh
      $scope.listCart.forEach((cart) => {
        // Gọi API để lấy thông tin sản phẩm (bao gồm URL hình ảnh)
        var urlProduct = `${hostProductImage}/${cart.cartId}`;
        $http.get(urlProduct).then((respProduct) => {
          // Gán URL hình ảnh từ kết quả API vào mục tương ứng trong $scope.listCart
          cart.imageUrl = respProduct.data[0].image;
        });
      });
  
      console.log($scope.listCart);
    });
  };

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

  $scope.upQuantityProduct = function (cart) {
    var urlUpQuantityProduct = `${hostUpQuantityProduct}/1/${cart.product.productid}`;
    $http.put(urlUpQuantityProduct).then((resp) => {
      // $scope.loadCart();
      cart.quantity++;
    });
  };

  $scope.downQuantityProduct = function (cart) {
    var urlUpQuantityProduct = `${hostDownQuantityProduct}/1/${cart.product.productid}`;
    $http.put(urlUpQuantityProduct).then((resp) => {
      cart.quantity--;
    }).catch((err) => {
      Swal.fire({
        title: 'Bạn có chắc muốn xóa không?',
        text: 'Hành động này sẽ xóa vĩnh viễn dữ liệu!',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Xóa',
        cancelButtonText: 'Hủy bỏ'
    }).then((result) => {
      var urlDeleteProduct = `${hostDeleteProduct}/${cart.cartId}`;
        if (result.isConfirmed) {
            $http.delete(urlDeleteProduct).then(resp => {
              $scope.loadCart();
            });
            Swal.fire('Đã xóa!', 'Sản phẩm đã được xóa khỏi giỏ hàng.', 'Thành công');
        } });
    });
  };

  $scope.deleteProduct = function (cart) {
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
        $http.delete(urlDeleteProduct).then(() => {
          $scope.loadCart();
        });
        Swal.fire('Đã xóa!', 'Sản phẩm đã được xóa khỏi giỏ hàng.', 'Thành công');
      }
    });
  };




  $scope.loadCart();
});
