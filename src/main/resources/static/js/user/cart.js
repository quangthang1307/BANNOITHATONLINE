const app = angular.module("bannoithatonline", []);

const hostListCart = "http://localhost:8080/rest/showCart";
const hostUpQuantityProduct = "http://localhost:8080/rest/cart/up";
const hostDownQuantityProduct = "http://localhost:8080/rest/cart/down";
const hostDeleteProduct = "http://localhost:8080/rest/removeFromCart";
const hostProductImage = "http://localhost:8080/rest/products";
const hostCustomerId = "http://localhost:8080/rest/customer";

app.controller("CartController", function ($scope, $http, $window) {
  $scope.listCart = [];
  $scope.listCartId = [];

  //Gọi customerId khi login thành công
  // const hostCustomerId = "http://localhost:8080/rest/customer";
  $http.get(hostCustomerId).then(function (response) {
    $scope.customer = response.data;
    $window.localStorage.setItem("customerId", JSON.stringify($scope.customer));
  });

  //Lấy thông tin khách hàng
  var getCustomer = localStorage.getItem("customerId");
  var customer = JSON.parse(getCustomer);
  console.log(customer);

  //Load giỏ hàng
  var urlListCart = `${hostListCart}/${customer.customerId}`;
  $scope.loadCart = function () {
    $http.get(urlListCart).then((resp) => {
      $scope.listCart = resp.data;

      // Lặp qua mỗi mục trong $scope.listCart để thêm URL hình ảnh
      $scope.listCart.forEach((cart) => {
        // Gọi API để lấy thông tin sản phẩm (bao gồm URL hình ảnh)
        cart.isSelected = false;
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
    var index = $scope.listCartId.findIndex(
      (item) => item.cartId === cart.cartId
    );
    if (index === -1) {
      $scope.listCartId.push(cart);
    } else {
      $scope.listCartId.splice(index, 1);
    }
    console.log("Checkbox clicked for cart with ID:", cart.cartId);
    console.log($scope.listCartId);
  };

  $scope.upQuantityProduct = function (cart) {
    var urlUpQuantityProduct = `${hostUpQuantityProduct}/${customer.customerId}/${cart.product.productid}`;
    $http.put(urlUpQuantityProduct).then((resp) => {
      // $scope.loadCart();
      cart.quantity++;
    });
  };

  $scope.downQuantityProduct = function (cart) {
    var urlUpQuantityProduct = `${hostDownQuantityProduct}/${customer.customerId}/${cart.product.productid}`;
    $http
      .put(urlUpQuantityProduct)
      .then((resp) => {
        cart.quantity--;
      })
      .catch((err) => {
        Swal.fire({
          title: "Bạn có chắc muốn xóa không?",
          text: "Hành động này sẽ xóa vĩnh viễn dữ liệu!",
          icon: "warning",
          showCancelButton: true,
          confirmButtonText: "Xóa",
          cancelButtonText: "Hủy bỏ",
        }).then((result) => {
          var urlDeleteProduct = `${hostDeleteProduct}/${cart.cartId}`;
          if (result.isConfirmed) {
            $http.delete(urlDeleteProduct).then((resp) => {
              $scope.loadCart();
            });
            Swal.fire(
              "Đã xóa!",
              "Sản phẩm đã được xóa khỏi giỏ hàng.",
              "Thành công"
            );
          }
        });
      });
  };

  $scope.deleteProduct = function (cart) {
    var urlDeleteProduct = `${hostDeleteProduct}/${cart.cartId}`;

    Swal.fire({
      title: "Bạn có chắc muốn xóa không?",
      text: "Hành động này sẽ xóa vĩnh viễn dữ liệu!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Xóa",
      cancelButtonText: "Hủy bỏ",
    }).then((result) => {
      if (result.isConfirmed) {
        $http.delete(urlDeleteProduct).then(() => {
          $scope.loadCart();
        });
        Swal.fire(
          "Đã xóa!",
          "Sản phẩm đã được xóa khỏi giỏ hàng.",
          "Thành công"
        );
      }
    });
  };

  $scope.selectAll = function () {
    var allSelected = true;
    // Xác định xem tất cả các checkbox có được chọn không
    for (var i = 0; i < $scope.listCart.length; i++) {
      if (!$scope.listCart[i].isSelected) {
        allSelected = false;
        break;
      }
    }
    // Nếu tất cả đã được chọn, hủy chọn tất cả; ngược lại, chọn tất cả
    for (var j = 0; j < $scope.listCart.length; j++) {
      $scope.listCart[j].isSelected = !allSelected;
    }
  };

  $scope.loadCart();
});
