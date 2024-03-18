const app = angular.module("bannoithatonline", []);
const host = "";


app.controller("OrderDetailController",function($scope, $http){
    $scope.customer = {};
    $scope.orderDetail = {};
  
    $scope.loadCustomer = function () {
      if (window.localStorage.getItem("customerId")) {
        var getCustomer = localStorage.getItem("customerId");
        var customer = JSON.parse(getCustomer);
        $scope.customer = customer;
      }
    };
    $scope.loadCustomer();


    $scope.loadOrderDetail = function () {
        if (window.localStorage.getItem("orderDetails")) {
          var getorderDetails = localStorage.getItem("orderDetails");
          var orderDetail = JSON.parse(getorderDetails);
          $scope.orderDetail = orderDetail;
          console.log(orderDetail);
        }
      };
      $scope.loadOrderDetail();



      


});