var app = angular.module("bannoithatonline", [])

const host = "/rest/product";
const hostCustomerId = "/rest/customer";
const hostListCart = "/rest/showCart";
const hostUpQuantityProduct = "/rest/cart/up";
const hostDownQuantityProduct = "/rest/cart/down";
const hostDeleteProduct = "/rest/removeFromCart";
const hostProductImage = "/rest/products";
const hostDeleteAllProductInCart = "/rest/removeAllCarts";
const hostProductSale = "/rest/product/sale";
const hostDiscount = "/rest/discounttop4";
const hostProductByRoom = "/rest/product/category/room";
const hostFlashSaleHourStart = "/rest/flashsaledelay/start";
const hostFlashSaleHourEnd = "/rest/flashsaledelay/end";
const hostFlashSale = "/rest/flashsale";
const hostFlashSaleUpdate = "/rest/flashsale/update"
const hostProductFlashsale = "/rest/product/flashsale";
const hostProductBestseller = "/rest/product/bestseller";
const hostInventory = "/rest/product/inventory";

app.controller("IndexController", function ($scope, $http, $window, $timeout, $interval) {
  $scope.listCart = [];
  $scope.productsbestsellers = [];
  $scope.productSale = [];
  $scope.discounts = [];
  $scope.productFlashSale = [];
  $scope.inventory = [];
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
      .get(hostProductBestseller)
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

    $http.get(hostProductFlashsale)
      .then(function (response) {

        $scope.productFlashSale = response.data
        localStorage.setItem('productFlashSale', JSON.stringify($scope.productFlashSale));
        console.log($scope.productFlashSale);

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


    $http.get(hostInventory)
      .then(function (response) {

        $scope.inventory = response.data
        localStorage.setItem('productInventory', JSON.stringify($scope.inventory));
        console.log($scope.inventory);

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
  //Kiểm tra sản phẩm Flashsale để thay đổi giá
  $scope.isProductInFlashSale = function (productId) {
    return $scope.productFlashSale.some(item => item.product.productid === productId);
  };

  $scope.getPercentFlashSaleForProduct = function (productId) {
    var foundItem = $scope.productFlashSale.find(item => item.product.productid === productId);
    return foundItem ? foundItem.percent : null;
  };
  //

  //Kiểm tra tồn kho
  $scope.checkInventory = function (productId) {
    const product = $scope.inventory.find(item => item.product.productid === productId);
    return product && product.quantityonhand > 0;
  }
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
    if (id == 1) title = "Phòng khách";
    if (id == 2) title = "Phòng Ngủ";
    if (id == 3) title = "Phòng Ăn";
    if (id == 4) title = "Phòng làm việc";

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


  //FLASH SALE
  $scope.FlashSaleHours = [];
  $scope.FlashSaleProducts = [];
  $scope.itemsPerPage = 15;
  $scope.currentPage = 1;
  $scope.totalItems = 0;
  $scope.totalPages = 0;

  $scope.timetodelay = 0;

  // Hàm lập lịch task cho flash sale
  $scope.scheduleFlashSaleTask = function () {
    angular.forEach($scope.FlashSaleHours, function (FlashSaleHour) {
      var delay = FlashSaleHour.delay;
      console.log(delay);
      if (delay >= 0) {
        $timeout(function () {
          // Thực hiện công việc khi bắt đầu flash sale
          console.log("Flash sale is starting now at ");
          $scope.getFlashSale();

          // Lập lịch cho công việc khi kết thúc flash sale
          $scope.scheduleSaleOffTask();
        }, delay);
      }


    });

  };

  // Hàm lập lịch task cho việc kết thúc flash sale
  $scope.flashsaleoff = null;
  $scope.scheduleSaleOffTask = function () {

    $http.get(hostFlashSaleHourEnd)
      .then(resp => {
        var delay = resp.data.delay
        $scope.timetodelay = resp.data.delay;
        $scope.flashsaleoff = resp.data.flashsalehour
        $scope.startCountdown();
        $timeout(function () {
          console.log("Sale off at " + new Date());
          $scope.FlashSaleProducts = [];
          $scope.UpdateFlashSale($scope.flashsaleoff);
          // Thực hiện công việc khi kết thúc flash sale

        }, delay);
      })
      .catch(error => {
        console.log("Error", error);
      });

  };

  $scope.getFlashSaleDelayStart = function () {
    $http.get(hostFlashSaleHourStart)
      .then(resp => {
        $scope.FlashSaleHours = resp.data;
        console.log($scope.FlashSaleHours);


        $scope.scheduleFlashSaleTask();

      })
      .catch(error => {
        console.log("Error", error);
      });
  }

  $scope.getFlashSaleDelayEnd = function () {
    $http.get(hostFlashSaleHourEnd)
      .then(resp => {
        console.log(resp.data);
        return resp.data;
      })
      .catch(error => {
        console.log("Error", error);
      });
  }

  $scope.getFlashSale = function () {
    var api = hostFlashSale + "?page=" + ($scope.currentPage - 1) + "&size" + $scope.itemsPerPage;
    $http.get(api)
      .then(resp => {
        console.log(resp.data);
        $scope.FlashSaleProducts = resp.data.content;

        $scope.totalItems = resp.data.totalElements;
        console.log($scope.totalItems);

        $scope.totalPages = parseInt(resp.data.totalPages, 10);
        console.log($scope.totalPages);
      })
      .catch(error => {
        console.log("Error", error);
      });
  }

  $scope.UpdateFlashSale = function (dataflashsale) {
    $http.put(hostFlashSaleUpdate, dataflashsale)
      .then(function (response) {
        // Xử lý kết quả thành công
        console.log('Dữ liệu đã được gửi thành công:', response.data);
      })
      .catch(function (error) {
        // Xử lý lỗi
        console.error('Đã xảy ra lỗi:', error);
      });
  }


  //

  $scope.numberArray = function () {
    var result = [];
    for (var i = 1; i <= $scope.totalPages; i++) {
      result.push(i);
    }
    return result;
  };


  var timer; // Biến lưu trữ interval
  // var timetoend = $scope.timetodelay;
  $scope.startCountdown = function () {
    // Dừng interval trước khi bắt đầu một lần mới
    if (angular.isDefined(timer)) {
      $interval.cancel(timer);
    }
    // Bắt đầu đếm ngược
    timer = $interval($scope.countdown, 1000);
  }

  $scope.countdown = function () {
    // Kiểm tra xem thời gian còn lại có hợp lệ không

    if ($scope.timetodelay <= 0) {
      console.log("Thời gian đã kết thúc.");
      return;
    }



    // Chuyển đổi thời gian còn lại thành giờ, phút, giây
    $scope.days = Math.floor($scope.timetodelay / (1000 * 60 * 60 * 24));
    $scope.hours = Math.floor($scope.timetodelay / (1000 * 60 * 60));
    $scope.minutes = Math.floor(($scope.timetodelay % (1000 * 60 * 60)) / (1000 * 60));
    $scope.seconds = Math.floor(($scope.timetodelay % (1000 * 60)) / 1000);

    if ($scope.timetodelay <= 1000) {
      console.log("đã dừng");
      $interval.cancel(timer);
      $scope.days = $scope.hours = $scope.minutes = $scope.seconds = 0;
    } else {
      $scope.timetodelay -= 1000;
      $scope.startCountdown();
    }



  }





  $scope.getFlashSaleDelayStart();

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
      "/rest/cart/" +
      $scope.customer.customerId +
      "/" +
      product.productid;
    $http
      .get(urlCheckCart)
      .then(function (response) {
        if (response.data) {

          var url =
            "/rest/cart/up/" +
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
        var url = "/rest/addToCart";
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
    $http.post('/createThread', {})
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

    $http.post('/sendMessageApi', jsonBody)
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
    $http.post('/runThread').then(function (response) {
      console.log(response);
      $scope.runThreadId = response.data.runThreadId;
      $scope.checkRunThreadId();
    });
  };

  $scope.checkRunThreadId = function () {
    $http.get('/checkRunThreadId').then(function (response) {
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
      $http.get('/showFeedback').then(function (response) {
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