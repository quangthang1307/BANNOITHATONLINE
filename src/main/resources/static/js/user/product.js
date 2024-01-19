var app = angular.module("bannoithatonline", []);
const host = "http://localhost:8080/rest/product";
const hostCustomerId = "http://localhost:8080/rest/customer";

app.controller("productController", function ($scope, $http, $window) {
  // Gán CustomerId người dùng
  $http.get(hostCustomerId).then(function (response) {
    $scope.customer = response.data;
    $window.localStorage.setItem("customerId", JSON.stringify($scope.customer));
  });

  $http
    .get("/rest/product")
    .then(function (response) {
      $scope.products = response.data;
      console.log(response.data);
    })
    .catch(function (error) {
      console.error("Error fetching products:", error);
    });

  $scope.addToCart = function (product) {
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

            $http.put(url);
        } else {
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
        }
      })
      .catch(function (error) {
        // Log any errors to the console
        console.error("Error checking cart:", error);
      });
  };
});
