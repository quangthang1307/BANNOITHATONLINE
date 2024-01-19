const app = angular.module("bannoithatonline", []);

const hostListCart = "http://localhost:8080/rest/showCart";
const hostUpQuantityProduct = "http://localhost:8080/rest/cart/up";
const hostDownQuantityProduct = "http://localhost:8080/rest/cart/down";
const hostDeleteProduct = "http://localhost:8080/rest/removeFromCart";
const hostProductImage = "http://localhost:8080/rest/products";
const hostCustomerId = "http://localhost:8080/rest/customer";
const hostDeleteAllProductInCart = "http://localhost:8080/rest/removeAllCarts";

app.controller("CartController", function ($scope, $http, $window) {
  $scope.listCart = [];
  $scope.listCartId = [];

  //Gọi customerId khi login thành công
  // const hostCustomerId = "http://localhost:8080/rest/customer";

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
        var urlProduct = `${hostProductImage}/${cart.product.productid}`;
        $http.get(urlProduct).then((respProduct) => {
          // Gán URL hình ảnh từ kết quả API vào mục tương ứng trong $scope.listCart
          cart.imageUrl = respProduct.data[0].image;
        });
      });

      //Tổng tiền toàn bộ giỏ hàng
      $scope.calculateTotalAmount();
      $scope.calculateSelectedTotalAmount();
    });
  };

  $scope.calculateTotalAmount = function () {
    $scope.totalAmount = 0;

    $scope.listCart.forEach(function (cartItem) {
      var productPrice = cartItem.product.pricexuat;
      var quantity = cartItem.quantity;
      $scope.totalAmount += productPrice * quantity;
    });
  };

  $scope.calculateSelectedTotalAmount = function () {
    $scope.selectedTotalAmount = 0;

    $scope.listCartId.forEach(function (cartItem) {
      var productPrice = cartItem.product.pricexuat;
      var quantity = cartItem.quantity;
      $scope.selectedTotalAmount += productPrice * quantity;
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

    $scope.calculateSelectedTotalAmount();
    console.log($scope.selectedTotalAmount);
  };

  $scope.upQuantityProduct = function (cart) {
    var urlUpQuantityProduct = `${hostUpQuantityProduct}/${customer.customerId}/${cart.product.productid}`;
    $http.put(urlUpQuantityProduct).then((resp) => {
      // $scope.loadCart();
      cart.quantity++;
      $scope.calculateTotalAmount();
      $scope.calculateSelectedTotalAmount();
    });
  };

  $scope.downQuantityProduct = function (cart) {
    var urlUpQuantityProduct = `${hostDownQuantityProduct}/${customer.customerId}/${cart.product.productid}`;
    $http
      .put(urlUpQuantityProduct)
      .then((resp) => {
        cart.quantity--;
        $scope.calculateTotalAmount();
        $scope.calculateSelectedTotalAmount();
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
          $scope.calculateTotalAmount();
          $scope.calculateSelectedTotalAmount();
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
    for (var j = 0; j < $scope.listCart.length; j++) {
      $scope.listCart[j].isSelected = !allSelected;
      if ($scope.listCart[j].isSelected) {
        if (
          !$scope.listCartId.some(
            (item) => item.cartId === $scope.listCart[j].cartId
          )
        ) {
          $scope.listCartId.push($scope.listCart[j]);
          $scope.calculateSelectedTotalAmount();
        }
      } else {
        $scope.listCartId = $scope.listCartId.filter(
          (item) => item.cartId !== $scope.listCart[j].cartId
        );
        $scope.calculateSelectedTotalAmount();
      }
    }
  };

  $scope.deleteAll = function () {
    var urlDeleteAllProduct = `${hostDeleteAllProductInCart}/${customer.customerId}`;

    if ($scope.listCart.length > 0) {
      Swal.fire({
        title: "Bạn có chắc muốn xóa không?",
        text: "Hành động này sẽ xóa vĩnh viễn dữ liệu!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Xóa",
        cancelButtonText: "Hủy bỏ",
      }).then((result) => {
        if (result.isConfirmed) {
          $http.delete(urlDeleteAllProduct).then((response) => {
            $scope.loadCart();
            $scope.calculateTotalAmount();
            $scope.calculateSelectedTotalAmount();
            Swal.fire(
              "Đã xóa!",
              "Tất cả sản phẩm đã được xóa khỏi giỏ hàng.",
              "Thành công"
            );
          });
        }
      });
    } else {
      Swal.fire({
        title:
          "Bạn chưa có sản phẩm trong giỏ hàng, tiến hành mua thêm sản phẩm nhé ?",
        text: "",
        icon: "question",
        showCancelButton: true,
        confirmButtonText: "Thêm sản phẩm",
        cancelButtonText: "Hủy bỏ",
      }).then((result) => {
        if (result.isConfirmed) {
          $window.location.href = "/index";
        }
      });
    }
  };

  $scope.PaymentAction = function () {
    var listPayMent = $scope.listCartId;
    if (listPayMent.length > 0) {
      $scope.pay = [];

      $scope.pay.push({
        Product: $scope.listCartId,
        TotalPayment: $scope.selectedTotalAmount,
      });

      console.log($scope.pay);      
      $window.localStorage.setItem("listPayment",JSON.stringify($scope.pay));
      window.location.href = '/checkout';
    }else{
      Swal.fire({
        title: "Vui lòng chọn sản phẩm để thanh toán!",
        icon: "warning",
        showConfirmButton: false,
        timer: 3000,
        customClass: {
          popup: 'custom-popup-class',
          title: 'custom-title-class',
        },
      });
      
      
    }
    // var show = JSON.parse(localStorage.getItem("listPayment"));
  };

  $scope.loadCart();
});
