const app = angular.module("bannoithatonline", ["ui.bootstrap"]);
const hostListDiscount = "http://localhost:8080/rest/discount";
const hostSubmitOder = "http://localhost:8080/rest/vnpay";
const host = "http://localhost:8080";
app.controller("checkoutController", [
  "$scope",
  "$http",
  "$uibModal",
  function ($scope, $http, $uibModal) {
    var showListProduct = JSON.parse(localStorage.getItem("listPayment"));
    console.log(showListProduct);
    $scope.products = showListProduct;
    $scope.customer = {};

    $scope.discountCode = "";
    $scope.valueDiscountCode = 0;
    $scope.paymentOptions = "";
    $scope.address = [];
    $scope.addressPayment = {};

    $scope.loadInforCustomer = function () {
      var getCustomer = localStorage.getItem("customerId");
      var customer = JSON.parse(getCustomer);
      $scope.customer = customer;
      var urlAddress = `${host}/rest/profile/customers/${customer.customerId}/addresses`;
      $http.get(urlAddress).then((response) => {
        $scope.address = response.data;
        console.log($scope.address);
      });
    };

    $scope.loadInforCustomer();

    $scope.onAddressSelected = function (addr) {
      $scope.addressPayment = addr;
      console.log("Địa chỉ được chọn: ", $scope.addressPayment);

      if (
        $scope.addressPayment &&
        Object.keys($scope.addressPayment).length > 0
      ) {
        console.log("$scope.addressPayment có thuộc tính.");
      } else {
        console.log(
          "$scope.addressPayment không có thuộc tính hoặc là một đối tượng rỗng."
        );
      }
    };

    $scope.clearAddressPayment = function () {
      $scope.showFill = 1;
      $scope.onAddressSelected(null);
      var getCustomer = localStorage.getItem("customerId");
      var customer = JSON.parse(getCustomer);
      var urlAddress = `${host}/rest/profile/customers/${customer.customerId}/addresses`;
      $http.get(urlAddress).then((response) => {
        response.data.forEach((address) => {
          var radioButton = document.getElementById(address.addressID);
          radioButton.checked = false;
        });
      });
    };

    console.log(showListProduct);
    console.log($scope.customer);

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
    $scope.listOptionPayments();

    $scope.onPaymentMethodChange = function () {
      console.log("Người dùng đã chọn: " + $scope.selectedPaymentMethod);
      // var url = `${host}//rest/checkOptionPayment/${$scope.selectedPaymentMethod}`;
    };

    $scope.continuePayment = function () {
      // Lấy phương thức thanh toán được chọn từ $scope
      var optionPayment = $scope.selectedPaymentMethod;

      // Lấy thông tin khách hàng từ localStorage
      var getCustomer = localStorage.getItem("customerId");
      var customer = JSON.parse(getCustomer);
      var urlCreateOrder = `${host}/rest/createOrder`;
      var discount = null;

      // Kiểm tra xem đã chọn phương thức thanh toán chưa
      if (!($scope.selectedPaymentMethod != null)) {
        Swal.fire({
          title: "Hãy chọn phương thức thanh toán!",
          icon: "warning",
          timer: 900,
        });
        return;
      }

      // Hiển thị hộp thoại xác nhận đặt hàng
      Swal.fire({
        title: "Xác nhận đặt hàng",
        text: "Bạn có chắc chắn muốn đặt hàng?",
        icon: "question",
        showCancelButton: true,
        confirmButtonText: "Đồng ý",
        cancelButtonText: "Hủy bỏ",
      }).then((result) => {
        if (result.isConfirmed) {
          // Kiểm tra xem có mã giảm giá không
          console.log($scope.discountCode);
          console.log($scope.products);

          // Xóa sản phẩm thanh toán khỏi giỏ hàng
          var urlDelete =
            "http://localhost:8080/rest/deleteProductInCartByCustomerId";
          for (var i = 0; i < $scope.products.length; i++) {
            var productGroup = $scope.products[i].Product;

            for (var j = 0; j < productGroup.length; j++) {
              var product = productGroup[j].product;
              $http.delete(urlDelete, {
                params: {
                  customerId: customer.customerId,
                  productId: product.productid,
                },
              });
              console.log(product.productid);
            }
          }

          // Kiểm tra địa chỉ nhận hàng
          if (
            !(
              $scope.addressPayment &&
              Object.keys($scope.addressPayment).length > 0
            )
          ) {
            Swal.fire({
              title: "Địa chỉ!",
              text: "Hãy chọn địa chỉ nhận hàng!",
              icon: "warning",
              timer: 1500,
              showConfirmButton: true,
            });
            return;
          }

          // Nếu có mã giảm giá
          if ($scope.valueDiscountCode > 0) {
            var url = `${host}/rest/discount/${$scope.discountCode}`;
            $http.get(url).then((response) => {
              var dataPost = {
                sumpayment:
                  $scope.products[0].TotalPayment - $scope.valueDiscountCode,
                discount: response.data,
                customer: customer,
                payment: {
                  paymentid: $scope.selectedPaymentMethod,
                },
                orderstatus: {
                  orderstatusID: 1,
                },
                address: $scope.addressPayment,
              };
              console.log(dataPost);
              $http.post(urlCreateOrder, dataPost).then((response) => {
                var urlPost = `${host}/rest/createOrderDetail`;
                $scope.products[0].Product.forEach((element) => {
                  window.localStorage.setItem("orderId", response.data.orderID);
                  dataPost = {
                    productquantity: element.quantity,
                    totalpayment: element.quantity * element.product.pricexuat,
                    price: element.product.pricexuat,
                    order: {
                      orderID: response.data.orderID,
                    },
                    product: {
                      productid: element.product.productid,
                      // Thêm các trường khác của Product nếu cần
                    },
                  };
                  console.log(dataPost);
                  $http.post(urlPost, dataPost);
                });
              });
            });
          } else {
            // Nếu không có mã giảm giá
            var dataPost = {
              sumpayment:
                $scope.products[0].TotalPayment - $scope.valueDiscountCode,
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
              address: $scope.addressPayment,
            };
            console.log(dataPost);
            $http.post(urlCreateOrder, dataPost).then((response) => {
              var urlPost = `${host}/rest/createOrderDetail`;
              $scope.products[0].Product.forEach((element) => {
                window.localStorage.setItem("orderId", response.data.orderID);
                dataPost = {
                  productquantity: element.quantity,
                  totalpayment: element.quantity * element.product.pricexuat,
                  price: element.product.pricexuat,
                  order: {
                    orderID: response.data.orderID,
                  },
                  product: {
                    productid: element.product.productid,
                    // Thêm các trường khác của Product nếu cần
                  },
                };
                console.log(dataPost);
                $http.post(urlPost, dataPost);
              });
            });
          }

          // Xử lý phương thức thanh toán
          if (optionPayment == "1") {
            $scope.submitOrderVNPay();
          } else {
            // Hiển thị thông báo đặt hàng thành công
            Swal.fire({
              title: "Đặt hàng",
              text: "Bạn đã đặt hàng thành công!",
              icon: "success",
              timer: 850,
              showConfirmButton: false, // Ẩn nút Xác nhận
            });

            // Thiết lập một setTimeout để tự động chuyển hướng khi timer kết thúc
            setTimeout(() => {
              window.location.href = "/order";
            }, 850);
          }
        } else {
          // Xử lý khi người dùng hủy bỏ
          // console.log(optionPayment);
          // console.log("Hủy bỏ");
        }
      });
    };

    $http.get("/rest/profile/customer").then(function (response) {
      $scope.customer = response.data;
      // $scope.customerId = $scope.customer.customerId;
      // console.log($scope.customerId);
    });

    // $http.get("/rest/customers/" + $scope.customerId + "/addresses")
    // .then(function(response) {
    //     $scope.addresses = response.data;
    // });

    function getCustomerId() {
      return $http.get("/rest/profile/customer").then(function (response) {
        return response.data.customerId;
      });
    }

    getCustomerId().then(function (customerId) {
      $scope.customerId = customerId;

      fetchAddresses(customerId);
      console.log(customerId);
    });

    function fetchAddresses(customerId) {
      $http
        .get("/rest/profile/customers/" + customerId + "/addresses")
        .then(function (response) {
          $scope.addresses = response.data;
        });
    }

    $scope.newAddress = {
      sonha: "",
      duong: "",
    };

    $scope.addAddress = function () {
      if (
        $scope.selectedProvince === null ||
        $scope.selectedDistrict === null ||
        $scope.selectedWard === null ||
        $scope.newAddress.duong === "" ||
        $scope.newAddress.duong === null
      ) {
        console.log($scope.newAddress.duong);
        Swal.fire({
          title: "Thông tin không hợp lệ",
          text: "Vui lòng chọn đày đủ thông tin !",
          icon: "error",
          timer: 1000,
        });
        return;
      }
      var newAddress = {
        tinhthanhpho: $scope.selectedProvince.Name,
        quanhuyen: $scope.selectedDistrict.Name,
        phuongxa: $scope.selectedWard.Name,
        sonha: $scope.newAddressc,
        duong: $scope.newAddress.duong,
      };
      $http
        .post(
          "/rest/profile/customers/" + $scope.customerId + "/addresses",
          newAddress
        )
        .then(function (response) {
          $scope.newAddress = {
            sonha: "",
            duong: "",
          };
          fetchAddresses($scope.customerId);

          Swal.fire({
            title: "Thành công",
            text: "Thêm địa chỉ mới thành công !",
            icon: "success",
            timer: 1000,
          });

          setTimeout(() => {
            window.location.reload();
          }, 1000);
        });
    };

    $http.get("/js/data.json").then(function (response) {
      $scope.provinces = response.data;
    });

    $scope.provinces = [];
    $scope.selectedProvince = null;
    $scope.selectedDistrict = null;
    $scope.selectedWard = null;

    $scope.updateDistricts = function () {
      $scope.selectedDistrict = null;
      $scope.selectedWard = null;
    };

    $scope.updateWards = function () {
      $scope.selectedWard = null;
    };

    $scope.editAddress = function (address) {
      $scope.editingAddress = angular.copy(address);
      document.getElementById("editTinhthanhpho").value = address.tinhthanhpho;
      document.getElementById("editQuanhuyen").value = address.quanhuyen;
      document.getElementById("editPhuongxa").value = address.phuongxa;
      document.getElementById("editSonha").value = address.sonha;
      document.getElementById("editduong").value = address.duong;
      document.getElementById("updateAddressForm").style.display = "block";
    };

    $scope.updateAddress = function () {
      var newAddress = {
        tinhthanhpho: $scope.selectedProvince.Name,
        quanhuyen: $scope.selectedDistrict.Name,
        phuongxa: $scope.selectedWard.Name,
        sonha: document.getElementById("updateSonha").value,
        duong: document.getElementById("updateduong").value,
      };
      $http
        .put(
          "/rest/profile/customers/" +
            $scope.customerId +
            "/addresses/" +
            $scope.editingAddress.addressID,
          newAddress
        )
        .then(function (response) {
          $scope.editingAddress = null;
          document.getElementById("updateAddressForm").style.display = "none";
          fetchAddresses($scope.customerId);
        });
    };

    $scope.deleteAddress = function (addressId) {
      $http
        .delete(
          "/rest/profile/customers/" +
            $scope.customerId +
            "/addresses/" +
            addressId
        )
        .then(function (response) {
          fetchAddresses($scope.customerId);
        });
    };

    $scope.showSelectedInfoModal = function () {
      $scope.clearAddressPayment();
      var modalInstance = $uibModal.open({
        templateUrl: "myModalContent.html", // Create a template for your modal content
        controller: "checkoutController", // Create a separate controller for the modal
        resolve: {
          selectedInfo: function () {
            return $scope.provinces;
          },
        },
      });

      modalInstance.result.then(
        function () {
          // Handle modal closed (if needed)
        },
        function () {
          // Handle modal dismissed (if needed)
        }
      );
    };

    function getParameterByName(name, url) {
      if (!url) url = window.location.href;
      name = name.replace(/[\[\]]/g, "\\$&");
      var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
      if (!results) return null;
      if (!results[2]) return "";
      return decodeURIComponent(results[2].replace(/\+/g, " "));
    }

    // Check if the 'checkoutagain' parameter is set to true
    var checkoutAgainParam = getParameterByName("again");
    if (checkoutAgainParam === "true") {
      // If true, hide the checkout button
      document.getElementById("checkoutButtonF").style.display = "none";
      document.getElementById("checkoutButtonL").style.display = "inline-block";
    } else {
      document.getElementById("checkoutButtonF").style.display = "inline-block";
      document.getElementById("checkoutButtonL").style.display = "none";
    }

    $scope.isSelectedAddress = function (addr) {
      if (window.localStorage.getItem("orderId")) {
        var orderId = JSON.parse(window.localStorage.getItem("orderId"));
        $http
          .get("/rest/findOrder", { params: { orderId: orderId } })
          .then((response) => {
            var radioButton = document.getElementById(
              response.data.address.addressID
            );
            radioButton.checked = true;

            var discount = document.getElementById("discountInput");
            if (response.data.discount !== null) {
              discount.value = response.data.discount.code;
            }
            $scope.selectedPaymentMethod = response.data.payment.paymentid;
            console.log(response.data);
          });
      }
    };
    $scope.isSelectedAddress();

    // Existing code...

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
  },
]);

app.controller("ModalInstanceCtrl", [
  "$scope",
  "$uibModalInstance",
  "selectedInfo",
  "$http",
  function ($scope, $uibModalInstance, selectedInfo, $http) {
    $scope.provinces = selectedInfo;
  },
]);
