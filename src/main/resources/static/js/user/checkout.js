const app = angular.module("bannoithatonline", []);
const hostListDiscount = "http://localhost:8080/rest/discount";
const hostSubmitOder = "http://localhost:8080/rest/vnpay";
app.controller(
  "checkoutController",
  function ($scope, $http, $window, $timeout, $location) {
    var showListProduct = JSON.parse(localStorage.getItem("listPayment"));
    $scope.products = showListProduct;

    $scope.discountCode = "";
    $scope.valueDiscountCode = 0;

    $scope.discount = function () {
      if ($scope.discountCode.length > 0) {
        $http.get(hostListDiscount).then((resp) => {
          var trust = resp.data.find((element) => {
            const currentDate = new Date();
            const startDate = new Date(element.startDate);
            const endDate = new Date(element.endDate);
            if ($scope.discountCode === element.code) {
              if (!(currentDate >= startDate && currentDate <= endDate)) {
                $scope.discountCode = "";
                Swal.fire({
                  title: "Mã giảm giá không hợp lệ !",
                  icon: "error",
                  showConfirmButton: false,
                  timer: 3000,
                  customClass: {
                    popup: "custom-popup-class",
                    title: "custom-title-class",
                  },
                });
                return false;
              } else if (element.quantityUsed >= element.quantity) {
                $scope.discountCode = "";
                Swal.fire({
                  title: "Mã giảm giá đã hết lượt sử dụng !",
                  icon: "error",
                  showConfirmButton: false,
                  timer: 3000,
                  customClass: {
                    popup: "custom-popup-class",
                    title: "custom-title-class",
                  },
                });
                return false;
              } else if (
                currentDate >= startDate &&
                currentDate <= endDate &&
                element.quantityUsed < element.quantity
              ) {
                $scope.valueDiscountCode =
                  ($scope.products[0].TotalPayment * element.percent) / 100;
                return true;
              }
            }
            return false;
          });

          if (!trust) {
            console.log($scope.valueDiscountCode);
            $scope.valueDiscountCode = 0;
            $scope.discountCode = "";
            Swal.fire({
              title: "Mã giảm giá không hợp lệ !",
              icon: "error",
              showConfirmButton: false,
              timer: 3000,
              customClass: {
                popup: "custom-popup-class",
                title: "custom-title-class",
              },
            });
          }
        });
      }
      // Dời kiểm tra xuống đây để đảm bảo kiểm tra sau khi vòng lặp kết thúc
    };

    $scope.checkClear = function () {
      // Kiểm tra xem input có trống hay không
      document
        .getElementById("discountInput")
        .addEventListener("input", function () {
          var discountCode = document.getElementById("discountInput").value;
          if (!discountCode) {
            $scope.valueDiscountCode = 0;
          }
        });
    };

    $scope.submitOrder = function () {
      var data = {
        amount: $scope.products[0].TotalPayment - $scope.valueDiscountCode, // Thay thế giá trị này bằng giá trị thực từ form hoặc logic khác
        orderInfo: "Mô tả đơn hàng", // Thay thế giá trị này bằng giá trị thực từ form hoặc logic khác
      };

      // Gửi POST request đến endpoint
      var urlVNPay = `${hostSubmitOder}`;
      $http.post(urlVNPay, data).then((resp) => {
        window.location.href = resp.data.vnpayUrl;
      });
    };


    // Lấy URL hiện tại
// var currentUrl = window.location.href;
// var checkoutIndex = currentUrl.indexOf("checkout?");
// if (checkoutIndex !== -1) {
//     var queryParams = currentUrl.substring(checkoutIndex + "checkout?".length);
//     var decodedParams = decodeURIComponent(queryParams);
//     console.log(decodedParams);
// } else {
//     console.log("Không tìm thấy chuỗi 'checkout?' trong URL");
// }

$scope.onPaymentMethodChange = function () {
  console.log('Người dùng đã chọn: ' + $scope.selectedPaymentMethod);
};

$scope.continuePayment = function () {
  var optionPayment = $scope.selectedPaymentMethod;
  console.log(optionPayment);
  if(optionPayment == 'vnpay'){
    $scope.submitOrder();
  }


};  



    console.log(showListProduct);
  }
);
