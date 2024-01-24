const app = angular.module("bannoithatonline", []);
const host = "http://localhost:8080";

app.controller("OrderController", function ($scope, $http) {
  $scope.customer = {};
  $scope.orders = [];

  var order =  $scope.orders;

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
          order.products = []; 
          order.statusPayment = {};

          if(order.payment.paymentname === 'Thanh toán VNPay'){
            $http.get(`${host}/rest/findTransaction`, { params: { orderID: order.orderID }}).then((response) => {
              console.log(response);
              // order.statusPayment.push({status:response.data.status});
              order.statusPayment = response.data;
            })
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
                  order.products.push({
                    quantity: orderDetail.productquantity,
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

  $scope.huyDon = function (order) {
    Swal.fire({
      title: "Hủy đặt đơn hàng ?",
      text: "Bạn có chắc chắn muốn hủy đơn hàng ?",
      icon: "question",
      showCancelButton: true,
      confirmButtonText: "Hủy đặt hàng",
      cancelButtonText: "Đóng",
    }).then((result) => {
      if (result.isConfirmed) {
        var urlDelete = `${host}/rest/deleteOrder`;

        $http.delete(urlDelete, {
          params: {
            orderId: order.orderID,
          },
        });
        const index = $scope.orders.findIndex(
          (o) => o.orderID === order.orderID
        );
        if (index !== -1) {
          $scope.orders.splice(index, 1);
        }
      }
    });
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

  
  
  
});
