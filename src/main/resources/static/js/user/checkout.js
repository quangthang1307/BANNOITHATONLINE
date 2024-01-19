const app = angular.module("bannoithatonline", []);
const hostListDiscount = "http://localhost:8080/rest/discount";
const hostSubmitOder = "http://localhost:8080/rest/vnpay";
const host = "http://localhost:8080";
app.controller(
  "checkoutController",
  function ($scope, $http, $window, $timeout, $location) {
    var showListProduct = JSON.parse(localStorage.getItem("listPayment"));
    $scope.products = showListProduct;

    $scope.discountCode = "";
    $scope.valueDiscountCode = 0;
    $scope.paymentOptions = "";

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

    $scope.submitOrderVNPay = function () {
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

    $scope.listOptionPayments = function () {
      var urlListOptionPayments = `${host}/rest/checkAllOptionPayment`;
      $http.get(urlListOptionPayments).then((response) => {
        $scope.paymentOptions = response.data;
        console.log(response.data);
      });
    };

    $scope.onPaymentMethodChange = function () {
      console.log("Người dùng đã chọn: " + $scope.selectedPaymentMethod);
      // var url = `${host}//rest/checkOptionPayment/${$scope.selectedPaymentMethod}`;
    };

    $scope.continuePayment = function () {
      var optionPayment = $scope.selectedPaymentMethod;

      if (!($scope.selectedPaymentMethod != null)) {
        return;
      }

      console.log(optionPayment);
      var getCustomer = localStorage.getItem("customerId");
      var customer = JSON.parse(getCustomer);
      var urlCreateOrder = `${host}/rest/createOrder`;
      var discount = null;

      if (optionPayment == "1") {
        if ($scope.valueDiscountCode > 0) {
          var url = `${host}/rest/discount/${$scope.discountCode}`;
          $http.get(url).then((response) => {
            var dataPost = {
              sumpayment: $scope.products[0].TotalPayment - $scope.valueDiscountCode,
              discount: response.data,
              customer: {
                customerId: customer.customerId,
              },
              payment: {
                paymentid: $scope.selectedPaymentMethod,
              },
              orderstatus: {
                orderstatusID: 1,
              },
              address: {
                addressID: 1,
              },
            };
  
            console.log(dataPost);
          $http.post(urlCreateOrder, dataPost).then((response) => {});
          });
  
  
        } else {
          var dataPost = {
            sumpayment: $scope.products[0].TotalPayment - $scope.valueDiscountCode,
            discount: null,
            customer: {
              customerId: customer.customerId,
            },
            payment: {
              paymentid: $scope.selectedPaymentMethod,
            },
            orderstatus: {
              orderstatusID: 1,
            },
            address: {
              addressID: 1,
            },
          };
  
          console.log(dataPost);
          $http.post(urlCreateOrder, dataPost).then((response) => {});
        }


        $scope.submitOrderVNPay();
      }

    };

    // $http.get('/rest/test', { params: { customerid: customerId } })
    //         .then(function(response) {
    //             // Kiểm tra nếu response.data là một chuỗi
    //             if (typeof response.data === 'string') {
    //                 console.log(response.data);
    //             } else {
    //                 console.error('Invalid data format:', response.data);
    //             }
    //         })
    //         .catch(function(error) {
    //             console.error('Error:', error);
    //         });

    // Make sure to inject $http service in your controller or service

    // $http.get('/vnpay-payment1', {
    //   params: {
    //       vnp_Amount: 250000000,
    //       vnp_BankCode: 'VNPAY',
    //       vnp_CardType: 'QRCODE',
    //       vnp_OrderInfo: 'M? t? ?n h?ng',
    //       vnp_PayDate: 20240118233915,
    //       vnp_ResponseCode: 24,
    //       vnp_TmnCode: 'NQOS23IZ',
    //       vnp_TransactionNo: 0,
    //       vnp_TransactionStatus: '00',
    //       vnp_TxnRef: '01018729',
    //       vnp_SecureHash: '64739e502affa5bc4cd838d47919a144c3757505a9304cb360763a28072c8664f373adc087aa94a635d6aecedca757de6581bd3f35b3457ba2ada63d7ac8bf4d'
    //   }
    // })
    //   .then(function (response) {
    //       // Check if response.data is a string
    //       if (typeof response.data === 'string') {
    //           console.log(response.data);
    //       } else {
    //           console.log('Invalid data format:', response.data);
    //       }
    //   })
    //   .catch(function (error) {
    //       console.error('Error:', error);
    //   });

    $scope.listOptionPayments();

    console.log(showListProduct);
  }
);
