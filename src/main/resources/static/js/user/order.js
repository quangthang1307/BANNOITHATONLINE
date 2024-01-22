const app = angular.module("bannoithatonline", []);
const host = "http://localhost:8080";

app.controller("OrderController", function ($scope, $http) {
  $scope.customer = {};
  $scope.orders = [];

  $scope.loadCustomer = function () {
    if (window.localStorage.getItem("customerId")) {
      var getCustomer = localStorage.getItem("customerId");
      var customer = JSON.parse(getCustomer);
      $scope.customer = customer;
    }
  };
  $scope.loadCustomer();

  // Fill ListOrder theo CustomerID
  $scope.fillOrder = function () {
    var urlFillOrder = `${host}/rest/orderByCustomer`;
    $http
      .get(urlFillOrder, { params: { customerId: $scope.customer.customerId } })
      .then((response) => {
        // Iterate through each order
        response.data.forEach((order) => {
          order.formattedTime = formatOrderTime(order.time);
          order.products = []; // Create an array to store product details for this order

          // Fetch order details for the current order
          var urlBtnXemChiTiet = `${host}/rest/orderDetailsByOrderID`;
          $http
            .get(urlBtnXemChiTiet, {
              params: { orderId: order.orderID },
            })
            .then((orderDetailsResponse) => {
              console.log(orderDetailsResponse);
              // Iterate through each product in order details
              orderDetailsResponse.data.forEach((orderDetail) => {
                var urlProduct = `${host}/rest/products/${orderDetail.product.productid}`;
                $http.get(urlProduct).then((productResponse) => {
                  order.products.push({
                    name: orderDetail.product.productname,
                    imageUrl: productResponse.data[0].image,
                  });
                });
              });
            });
        });

        // Sort the orders based on time
        $scope.orders = response.data.sort(
          (a, b) => new Date(b.time) - new Date(a.time)
        );

        console.log($scope.orders);
      })
      .catch((error) => {
        console.log("Chưa đăng nhập");
      });
  };
  $scope.fillOrder();

  function formatOrderTime(time) {
    const date = new Date(time);
    const formattedTime = `${date.getHours()}:${date.getMinutes()} - ${date.getDate()}-${
      date.getMonth() + 1
    }-${date.getFullYear()}`;
    return formattedTime;
  }

  // Button Xem chi tiết
  $scope.btnXemChiTiet = function (order) {
    window.localStorage.setItem("orderDetails", JSON.stringify(order));
    window.location.href = "/orderdetail";
  };
});
