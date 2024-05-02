const app = angular.module("bannoithatonline", ["ui.bootstrap"]);
const host = "";


app.controller("OrderController", [
  "$scope",
  "$http",
  "$uibModal",
  "$timeout",
  function ($scope, $http, $uibModal, $timeout) {
    $scope.customer = {};
    $scope.orders = [];

    var order = $scope.orders;

    $scope.loadCustomer = function () {
      if (window.localStorage.getItem("customerId")) {
        var getCustomer = localStorage.getItem("customerId");
        var customer = JSON.parse(getCustomer);
        $scope.customer = customer;
        console.log($scope.customer);
      }
    };
    $scope.loadCustomer();

    // Fill ListOrder theo CustomerID
    $scope.fillOrder = function () {
      var urlFillOrder = `${host}/rest/orderByCustomer`;
      $http
        .get(urlFillOrder, {
          params: { customerId: $scope.customer.customerId },
        })
        .then((response) => {
          // Iterate through each order
          response.data.forEach((order) => {
            order.formattedTime = formatOrderTime(order.time);
            order.products = [];
            order.statusPayment = {};

            if (order.payment.paymentname === "Thanh toán VNPay") {
              $http
                .get(`${host}/rest/findTransaction`, {
                  params: { orderID: order.orderID },
                })
                .then((response) => {
                  console.log(response);
                  // order.statusPayment.push({status:response.data.status});
                  order.statusPayment = response.data;
                });
            }

            // Fetch order details for the current order
            var urlBtnXemChiTiet = `${host}/rest/orderDetailsByOrderID`;
            $http
              .get(urlBtnXemChiTiet, {
                params: { orderId: order.orderID },
              })
              .then((orderDetailsResponse) => {
                // console.log(orderDetailsResponse);
                // Iterate through each product in order details
                orderDetailsResponse.data.forEach((orderDetail) => {
                  console.log(orderDetail);
                  var urlProduct = `${host}/rest/products/${orderDetail.product.productid}`;
                  $http.get(urlProduct).then((productResponse) => {
                    console.log(productResponse);
                    order.products.push({
                      customerid: $scope.customer.customerId,
                      productId: orderDetail.product.productid,
                      evaluateId: orderDetail.evaluate,
                      quantity: orderDetail.productquantity,
                      name: orderDetail.product.productname,
                      imageUrl: productResponse.data[0].image,
                      price: orderDetail.price,
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
    // $scope.btnXemChiTiet = function (order) {
    //   window.localStorage.setItem("orderDetails", JSON.stringify(order));
    //   window.location.href = "/orderdetail";
    // };

    $scope.thanhToan = function (order) {
      $http
        .get("/rest/payment/again?orderId=" + order.orderID)
        .then((response) => {
          window.localStorage.setItem(
            "listPayment",
            JSON.stringify(response.data)
          );
          window.localStorage.setItem("orderId", JSON.stringify(order.orderID));
          window.location.href = "/checkout?again=true";
        });
    };

    $scope.huyDon = function (order) {
      console.log(order);
      var product = [];

      order.products.forEach((resp) => {
        product.push(resp.name + " - " + resp.price + " VND");
        console.log(product);
      });
      var productString = product.join(", ");

      if (order.statusPayment.status == "0") {
        Swal.fire({
          title: "Hủy đặt đơn hàng ?",
          text: "Bạn đã thanh toán đơn hàng, nếu như hủy đơn hàng bạn chỉ nhận lại được 80% giá trị tổng tiền !",
          icon: "question",
          showCancelButton: true,
          confirmButtonText: "Hủy đặt hàng",
          cancelButtonText: "Đóng",
        }).then((result) => {
          if (result.isConfirmed) {
            $http.get("/rest/sendEmailHuyDon", {
              params: {
                to: $scope.customer.account.email,
                subject: "Hủy đơn đặt hàng",
                content:
                  "<html>" +
                  "<head>" +
                  "<style>" +
                  "body { font-family: Arial, sans-serif;  }" +
                  ".container { max-width: 1200px; margin: 0 auto; padding: 20px; border: 1px solid #ccc; border-radius: 5px; }" +
                  "h2 { text-align: center; }" +
                  "</style>" +
                  "</head>" +
                  "<body>" +
                  "<div class='container'>" +
                  "<h2>Thông Báo Hủy Đơn Hàng</h2>" +
                  "<p>" +
                  "Bạn vừa hủy đơn đặt hàng có mã đơn hàng: <strong>" +
                  order.orderID +
                  "</strong><br>" +
                  "Gồm các sản phẩm: " +
                  productString +
                  "<br>" +
                  "Tổng giá trị đơn hàng là: <strong>" +
                  order.sumpayment +
                  " VND</strong>" +
                  "<br><strong>Bạn chỉ nhận lại được 80% tổng tiền của đơn hàng, hãy liên hệ với cửa hàng để được hỗ trợ</strong><br>" +
                  "Chúng tôi sẽ liên hệ cho bạn trong thời gian sớm nhất, nếu bạn có mắc hoặc chưa được hỗ trợ, vui lòng liên hệ: xxx" +
                  "</p>" +
                  "</div>" +
                  "</body>" +
                  "</html>",
              },
            });

            $http.put("/rest/order/huydon", null, {
              params: {
                orderId: order.orderID,
              },
            });
            const index = $scope.orders.findIndex(
              (o) => o.orderID === order.orderID
            );
            if (index !== -1) {
              $scope.orders[index].orderstatus.orderstatusname = "Đã hủy";
            }
            Swal.fire({
              title: "Hủy thành công !",
              text: "Bạn đã hủy thành công đơn đặt hàng !",
              icon: "success",
              timer: 850,
            });
          }
        });
      } else {
        Swal.fire({
          title: "Hủy đặt đơn hàng ?",
          text: "Bạn có chắc chắn muốn hủy đơn hàng ?",
          icon: "question",
          showCancelButton: true,
          confirmButtonText: "Hủy đặt hàng",
          cancelButtonText: "Đóng",
        }).then((result) => {
          if (result.isConfirmed) {
            $http.get("/rest/sendEmailHuyDon", {
              params: {
                to: $scope.customer.account.email,
                subject: "Hủy đơn đặt hàng",
                content:
                  "<html>" +
                  "<head>" +
                  "<style>" +
                  "body { font-family: Arial, sans-serif; }" +
                  ".container { max-width: 1200px; margin: 0 auto; padding: 20px; border: 1px solid #ccc; border-radius: 5px; }" +
                  "h2 { text-align: center; }" +
                  "</style>" +
                  "</head>" +
                  "<body>" +
                  "<div class='container'>" +
                  "<h2>Thông Báo Hủy Đơn Hàng</h2>" +
                  "<p>" +
                  "Bạn vừa hủy đơn đặt hàng có mã đơn hàng: <strong>" +
                  order.orderID +
                  "</strong><br>" +
                  "Gồm các sản phẩm: " +
                  productString +
                  "<br>" +
                  "Tổng giá trị đơn hàng là: <strong>" +
                  order.sumpayment +
                  " VND</strong>" +
                  "</p>" +
                  "</div>" +
                  "</body>" +
                  "</html>",
              },
            });

            $http.put("/rest/order/huydon", null, {
              params: {
                orderId: order.orderID,
              },
            });
            const index = $scope.orders.findIndex(
              (o) => o.orderID === order.orderID
            );
            if (index !== -1) {
              $scope.orders[index].orderstatus.orderstatusname = "Đã hủy";
            }
            Swal.fire({
              title: "Hủy thành công !",
              text: "Bạn đã hủy thành công đơn đặt hàng !",
              icon: "success",
              timer: 850,
            });
          }
        });
      }
    };

    $scope.searchProducts = function () {
      var inputValue = $scope.searchProduct;

      $scope.filteredOrders = [];
      $scope.listFind = [];
      var addedOrderIDs = [];

      $scope.orders.forEach(function (order) {
        var orderID = order.orderID;

        // Kiểm tra xem orderID đã được thêm vào kết quả chưa
        if (addedOrderIDs.indexOf(orderID) === -1) {
          addedOrderIDs.push(orderID);
          $scope.filteredOrders.push(order);
        }

        // Kiểm tra điều kiện tìm kiếm cho từng sản phẩm trong order
        order.products.forEach((product) => {
          // console.log(product.name);
          // console.log($scope.searchProduct);
          if (product.name.toLowerCase().includes(inputValue.toLowerCase())) {
            // Kiểm tra xem order đã được thêm vào danh sách tìm kiếm chưa
            if ($scope.listFind.indexOf(order) === -1) {
              $scope.listFind.push(order);
            }
            return;
          }
        });
      });

      console.log(addedOrderIDs);
      console.log($scope.filteredOrders);
      console.log($scope.listFind);
    };

    function filterOrder() {
      $http
        .get("/rest/options", {
          params: {
            statusName: $scope.selectStatus,
            customerId: $scope.customer.customerId,
          },
        })
        .then((response) => {
          console.log(response.data);
          $scope.orders = response.data;
          $scope.orders.sort((a, b) => b.orderID - a.orderID);
        });
    }


    $scope.xacNhan = function(order) {
    Swal.fire({
        title: "Xác nhận !",
        text: "Bạn có chắc chắn đã nhận được đơn hàng ?",
        icon: "question",
        showCancelButton: true,
        confirmButtonText: "Tôi chắc chắn",
        cancelButtonText: "Đóng",
    }).then((result) => {
        if (result.isConfirmed) {
            $http.put('/rest/order', null, { params: { orderId: order.orderID } })
                .then(function(response) {
                  // Sử dụng $timeout để gọi hàm cập nhật dữ liệu sau một khoảng thời gian ngắn
                  $timeout(function() {
                    const index = $scope.orders.findIndex(o => o.orderID === order.orderID);
                    if (index !== -1) {
                        // Thực hiện cập nhật các thuộc tính của đơn hàng ở đây
                         $scope.orders[index].orderstatus.orderstatusname = 'Giao hàng thành công';
                        $scope.orders[index].confirmed = true; // Ví dụ
                    }
                });
                
                // Log thông báo xác nhận
                console.log("Đã xác nhận đơn hàng");
                })
                .catch(function(error) {
                    // Xử lý lỗi (nếu có)
                    console.error("Lỗi khi xác nhận đơn hàng:", error);
                });
        }
    });
}


    // Chức năng sắp xếp và cập nhật giao diện
    $scope.optionStatusView = function () {
      switch ($scope.selectStatus) {
        case "Chờ xác nhận":
          filterOrder();
          break;

        // Thêm các trường hợp khác tùy thuộc vào nhu cầu
        case "Đang giao hàng":
          filterOrder();
          break;

        case "Giao hàng thành công":
          filterOrder();
          break;

        case "Đã xác nhận":
          filterOrder();
          break;

        case "Đã hủy":
          filterOrder();
          break;

          case "Thanh toán thành công":
          filterOrder();
          break;

        case "Tất cả":
          $scope.loadCustomer();
          $scope.fillOrder();

          break;
      }
    };

    $scope.showSelectedInfoModal = function () {
      var modalInstance = $uibModal.open({
        templateUrl: "myModalEvaluate.html", // Create a template for your modal content
        controller: "OrderController", // Create a separate controller for the modal
        resolve: {
          selectedInfo: function () {
            return $scope.ratings;
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
  },
]);
