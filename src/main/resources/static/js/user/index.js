var app = angular.module("bannoithatonline", []);
const host = "http://localhost:8080/rest/product";
const hostCustomerId = "http://localhost:8080/rest/customer";
const hostListCart = "http://localhost:8080/rest/showCart";
const hostUpQuantityProduct = "http://localhost:8080/rest/cart/up";
const hostDownQuantityProduct = "http://localhost:8080/rest/cart/down";
const hostDeleteProduct = "http://localhost:8080/rest/removeFromCart";
const hostProductImage = "http://localhost:8080/rest/products";
const hostDeleteAllProductInCart = "http://localhost:8080/rest/removeAllCarts";

app.controller("IndexController", function ($scope, $http, $window) {
  $scope.listCart = [];
  $scope.productsbestsellers = [];
  // Gán CustomerId người dùng
  function fetchCustomer() {
    return $http
      .get(hostCustomerId)
      .then(function (response) {
        // Kiểm tra xem request có thành công không
        if (response && response.data) {
          $scope.customer = response.data;
          $window.localStorage.setItem(
            "customerId",
            JSON.stringify($scope.customer)
          );
        } else {
          // Xử lý khi request không thành công hoặc không có dữ liệu 
        }
      })
      .catch(function (error) {        
        // console.error("Lỗi khi thực hiện request để lấy customerId:", error);       
      });
  }
$scope.getDataProductBestSeller = function() {
  $http
    .get("/rest/product/bestseller")
    .then(function (response) {
      $scope.productsbestsellers = response.data;
      console.log(response.data);
    })
    .catch(function (error) {
      console.error("Error fetching products:", error);
    });
}
  

  $scope.addToCart = function (product) {
    if ($scope.customer == null) {
      Swal.fire({
        title: "Bạn chưa có tài khoản",
        text: "Cần đăng nhập hoặc đăng ký",
        icon: "question",
        showCancelButton: true,
        confirmButtonText: "Đăng nhập",
        cancelButtonText: "Đăng ký",
        timer: 10000,
      }).then((result) => {
        if (result.isConfirmed) {
          window.location.href = "/login";
        } else if (result.dismiss === Swal.DismissReason.cancel) {
          // Người dùng chọn "Đăng ký"
          console.log("Đăng ký");
        }
      });

      return;
    }

    var urlCheckCart =
      "http://localhost:8080/rest/cart/" +
      $scope.customer.customerId +
      "/" +
      product.productid;
    $http
      .get(urlCheckCart)
      .then(function (response) {
        if (response.data) {
          
          var url =
            "http://localhost:8080/rest/cart/up/" +
            $scope.customer.customerId +
            "/" +
            product.productid;

          $http.put(url, null, {params : { quantity: response.data.quantity + 1}});
          $scope.loadCart();
          Swal.fire({
            title: "Thêm sản phẩm thành công !",
            text: "",
            icon: "success",
            confirmButtonText: "OK",
            timer: 850,
          });
        } 
      })
      .catch(function (error) {
        var url = "http://localhost:8080/rest/addToCart";
          var dataPost = {
            customer: {
              customerId: $scope.customer.customerId,
            },
            product: {
              productid: product.productid,
            },
            quantity: 1,
          };
          $http.post(url, dataPost);
          $scope.loadCart();
          Swal.fire({
            title: "Thêm sản phẩm thành công !",
            text: "",
            icon: "success",
            confirmButtonText: "OK",
            timer: 850,
          });
      });
  };

  $scope.loadCart = function () {
    // Use the fetchCustomer function to ensure it completes before moving to the next step
    fetchCustomer().then(function () {
      if (window.localStorage.getItem("customerId")) {
        var getCustomer = localStorage.getItem("customerId");
        var customer = JSON.parse(getCustomer);
        console.log(customer);
        var urlListCart = `${hostListCart}/${customer.customerId}`;

        $http.get(urlListCart).then((resp) => {
          $scope.listCart = resp.data;
          $scope.listCart.forEach((cart) => {
            cart.isSelected = false;
            var urlProduct = `${hostProductImage}/${cart.product.productid}`;
            $http.get(urlProduct).then((respProduct) => {
              cart.imageUrl = respProduct.data[0].image;
            });
          });

          //Tổng tiền toàn bộ giỏ hàng
          $scope.calculateTotalAmount();
          //   $scope.calculateSelectedTotalAmount();
        });
      }
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

  // Call loadCart when the customer is loaded
  $scope.getDataProductBestSeller();
  $scope.loadCart();
});
