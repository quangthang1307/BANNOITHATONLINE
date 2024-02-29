var app = angular.module("bannoithatonline", [])
const host = "http://localhost:8080/rest/product";
const hostCustomerId = "http://localhost:8080/rest/customer";
const hostListCart = "http://localhost:8080/rest/showCart";
const hostUpQuantityProduct = "http://localhost:8080/rest/cart/up";
const hostDownQuantityProduct = "http://localhost:8080/rest/cart/down";
const hostDeleteProduct = "http://localhost:8080/rest/removeFromCart";
const hostProductImage = "http://localhost:8080/rest/products";
const hostDeleteAllProductInCart = "http://localhost:8080/rest/removeAllCarts";
const hostProductSale = "http://localhost:8080/rest/product/sale";
const hostDiscount = "http://localhost:8080/rest/discounttop4";
const hostProductByRoom = "http://localhost:8080/rest/product/category/room";

app.controller("IndexController", function ($scope, $http, $window) {
  $scope.listCart = [];
  $scope.productsbestsellers = [];
  $scope.productSale = [];
  $scope.discounts = [];
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
  $scope.getDataProductBestSeller = function () {
    $http
      .get("/rest/product/bestseller")
      .then(function (response) {
        $scope.productsbestsellers = response.data;
        console.log(response.data);
      })
      .catch(function (error) {
        console.error("Error fetching products:", error);
      });

    $http.get(hostProductSale)
      .then(function (response) {

        $scope.productSale = response.data
        console.log($scope.productSale);

      })
      .catch(function (error) {
        console.error('Error fetching productSale:', error);
      }
      );

    $http.get(hostDiscount)
      .then(function (response) {

        $scope.discounts = response.data
        console.log($scope.discounts);

      })
      .catch(function (error) {
        console.error('Error fetching productSale:', error);
      }
      );
  }

  //Kiểm tra sản phẩm sale để thay đổi giá
  $scope.isProductInSale = function (productId) {
    return $scope.productSale.some(item => item.productID === productId);
  };

  $scope.getPercentSaleForProduct = function (productId) {
    var foundItem = $scope.productSale.find(item => item.productID === productId);
    return foundItem ? foundItem.percent : null;
  };
  //

  // Hàm xử lý khi nhấp vào tên sản phẩm
  $scope.clickById = function (productId) {
    var url = `${host}/${productId}`;

    $http.get(url)
      .then(resp => {
        $scope.productbyid = resp.data;
        // Trong controller hiện tại, lưu dữ liệu dạng Json và localStorage
        if ($scope.isProductInSale(resp.data.productid)) {
          var percent = $scope.getPercentSaleForProduct(resp.data.productid);
          resp.data.percent = percent;
        } else {
          resp.data.percent = 0;
        }
        localStorage.setItem('productById', JSON.stringify(resp.data));

        // Chuyển hướng đến trang chi tiết sản phẩm
        window.location.href = `/productdetail/${productId}`;
        //  console.log("Success", resp);
      })
      .catch(error => {
        console.log("Error", error);
      });
  }
  //

  $scope.clickByRoom = function (id) {
    var title = "";
    if(id == 1) title = "Phòng khách";
    if(id == 2) title = "Phòng Ngủ";
    if(id == 3) title = "Phòng Ăn";
    if(id == 4) title = "Phòng làm việc";
   
    localStorage.setItem('idproductByroom', JSON.stringify(id));
    localStorage.setItem('titleCategory', JSON.stringify(title));
    window.location.href = "/product/room";
  }


  $scope.copyContent = function (elementId) {
    // Lấy nội dung cần sao chép bằng id được truyền vào
    var content = document.getElementById(elementId).innerText;

    // Tạo một thẻ textarea tạm thời để sao chép nội dung vào clipboard
    var tempTextArea = document.createElement("textarea");
    tempTextArea.value = content;
    document.body.appendChild(tempTextArea);

    // Chọn toàn bộ nội dung trong textarea và sao chép vào clipboard
    tempTextArea.select();
    document.execCommand("copy");

    // Xóa thẻ textarea tạm thời
    document.body.removeChild(tempTextArea);

    // Thay đổi nội dung của nút thành "Copied!"
    document.getElementById(elementId + "_button").innerText = "Đã sao chép";

  };


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

          $http.put(url, null, { params: { quantity: response.data.quantity + 1 } });
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

app.controller("openAiCtrl", function ($scope, $http) {

  $scope.responseStatus = '';

  $scope.callAPI = function () {
    $http.post('http://localhost:8080/createThread', {})
      .then(function (response) {
        console.log(response);
        $scope.threadId = response.data;
      })
      .catch(function (error) {
        console.log("Lỗi khi gọi API: ", error);
      });
  };

  $scope.callAPI();

  $scope.sendMessage = function () {
    if (!$scope.threadId) {
      alert("Chưa có Thread ID");
      return;
    }

    var jsonBody = {
      role: 'user',
      content: $scope.userMessage,
      file_ids: "file-PQeqheySy2be7PYRIuVVgQNQ"
    };

    $http.post('http://localhost:8080/sendMessageApi', jsonBody)
      .then(function (response) {
        console.log(response);
        $scope.responseMessage = "Server Response: " + response.data;
        $scope.runThread();
      })
      .catch(function (error) {
        console.log("Lỗi khi gọi API: ", error);
      });
  };

  $scope.runThread = function () {
    $http.post('http://localhost:8080/runThread').then(function (response) {
      console.log(response);
      $scope.runThreadId = response.data.runThreadId;
      $scope.checkRunThreadId();
    });
  };

  $scope.checkRunThreadId = function () {
    $http.get('http://localhost:8080/checkRunThreadId').then(function (response) {
      $scope.runThreadStatus = response.data;

      if (response.data && response.data.status === "in_progress") {
        $scope.responseStatus = "in_progress";
      } if (response.data && response.data.status === "completed") {
        $scope.responseStatus = "completed";
        $scope.showFeedback();
      } else {
        setTimeout(function () {
          $scope.checkRunThreadId();
        }, 2500);
      }
    });
  };

  $(function () {
    var INDEX = 0;

    function generate_message(msg, type) {
      INDEX++;
      var str = "";
      str += "<div id='cm-msg-" + INDEX + "' class=\"chat-msg " + type + "\">";
      str += "          <span class=\"msg-avatar\">";
      str += "          <\/span>";
      str += "          <div class=\"cm-msg-text\">";
      str += msg;
      str += "          <\/div>";
      str += "        <\/div>";
      $(".chat-logs").append(str);
      $("#cm-msg-" + INDEX).hide().fadeIn(300);
      if (type == 'self') {
        $("#chat-input").val('');
      }
      $(".chat-logs").stop().animate({ scrollTop: $(".chat-logs")[0].scrollHeight }, 1000);
    }

    $("#chat-submit").click(function (e) {
      e.preventDefault();
      var msg = $("#chat-input").val();
      if (msg.trim() == '') {
        return false;
      }

      generate_message(msg, 'self');
    });

    $scope.showFeedback = function () {
      $http.get('http://localhost:8080/showFeedback').then(function (response) {
        $scope.feedbackData = response.data;


        var messages = $scope.feedbackData.data;

        var assistantMessage = messages.find(function (message) {
          return message.role === "assistant";
        });

        if (assistantMessage) {
          var content = assistantMessage.content[0].text.value;
          generate_message(content, 'user');
        }
      });
    };

    $(document).delegate(".chat-btn", "click", function () {
      var value = $(this).attr("chat-value");
      var name = $(this).html();
      $("#chat-input").attr("disabled", false);
      generate_message(name, 'self');
    })

    $("#chat-circle").click(function () {
      $("#chat-circle").toggle('scale');
      $(".chat-box").toggle('scale');
    })

    $(".chat-box-toggle").click(function () {
      $("#chat-circle").toggle('scale');
      $(".chat-box").toggle('scale');
    })

    $scope.showFeedback(msg);
  })
});