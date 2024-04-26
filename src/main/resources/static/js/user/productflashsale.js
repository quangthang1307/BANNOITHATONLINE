var app = angular.module("bannoithatonline", []);
const host = "/rest/product";
const hostCategory = "/rest/category";
const hostProductByCategory = "/rest/flashsale/category";
const hostProductByCategoryDESC = "/rest/product/category/desc";
const hostProductByCategoryASC = "/rest/product/category/asc";
const hostProductByCategoryAZ = "/rest/product/category/az";
const hostProductByCategoryZA = "/rest/product/category/za";

const hostTop5ProductByCategory = "/rest/product/relate";
const hostCustomerId = "/rest/customer";
const hostListCart = "/rest/showCart";
const hostUpQuantityProduct = "/rest/cart/up";
const hostDownQuantityProduct = "/rest/cart/down";
const hostDeleteProduct = "/rest/removeFromCart";
const hostProductImage = "/rest/products";
const hostDeleteAllProductInCart = "/rest/removeAllCarts";
const hostProductSale = "/rest/product/sale";
const hostProductSales = "/rest/product/sales";
const hostProductSaleDESC = "/rest/product/sales/desc";
const hostProductSaleASC = "/rest/product/sales/asc";
const hostProductSaleAZ = "/rest/product/sales/az";
const hostProductSaleZA = "/rest/product/sales/za";
const hostProductSaleByCategory = "/rest/product/sales/category";
const hostProductSaleByCategoryAndDESC = "/rest/product/sales/category/desc";
const hostProductSaleByCategoryAndASC = "/rest/product/sales/category/asc";
const hostProductSaleByCategoryAndAZ = "/rest/product/sales/category/az";
const hostProductSaleByCategoryAndZA = "/rest/product/sales/category/za";
const hostProductDESC = "/rest/product/desc";
const hostProductASC = "/rest/product/asc";
const hostProductAZ = "/rest/product/az";
const hostProductZA = "/rest/product/za";

const hostProductByRoom = "/rest/product/category/room";
const hostProductByRoomDESC = "/rest/product/category/room/desc";
const hostProductByRoomASC = "/rest/product/category/room/asc";
const hostProductByRoomAZ = "/rest/product/category/room/az";
const hostProductByRoomZA = "/rest/product/category/room/za";
const hostProductByRoomAndCategory = "/rest/category/room";
const hostFlashSaleHourStart = "/rest/flashsaledelay/start";
const hostFlashSaleHourEnd = "/rest/flashsaledelay/end";
const hostFlashSale = "/rest/flashsale";
const hostFlashSaleUpdate = "/rest/flashsale/update"

const hostInventory = "/rest/product/inventory";





app.controller("productController", function ($scope, $http, $window, $timeout, $interval) {
    $scope.filterByCategory = false;
    $scope.filterBySale = false;
    $scope.listCart = [];

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


    $scope.FlashSaleHours = [];
    $scope.FlashSaleProducts = [];
    $scope.itemsPerPage = 15;
    $scope.currentPage = 1;
    $scope.totalItems = 0;
    $scope.totalPages = 0;
    $scope.timetodelay = 0;
    $scope.listCart = [];
    $scope.inventory = [];

    // Hàm lập lịch task cho flash sale
    $scope.scheduleFlashSaleTask = function () {
        angular.forEach($scope.FlashSaleHours, function (FlashSaleHour) {
            var delay = FlashSaleHour.delay;
            console.log(delay);
            $timeout(function () {
                console.log("Flash sale is starting now at ");
                $scope.getFlashSale();
                // Thực hiện công việc khi bắt đầu flash sale
                // $scope.getFlashSale();
                // Lập lịch cho công việc khi kết thúc flash sale
                $scope.scheduleSaleOffTask();
            }, delay);

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

    $scope.getInventory = function () {
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


    // Hàm xử lý khi nhấp vào tên sản phẩm
    $scope.clickById = function (productId) {
        var url = `${host}/${productId}`;
        const productflashsale = $scope.FlashSaleProducts.find(item => item.product.productid === productId);
        console.log(productflashsale.percent);

        $http.get(url)
            .then(resp => {
                $scope.productbyid = resp.data;
                // Trong controller hiện tại, lưu dữ liệu dạng Json và localStorage
                resp.data.percent = productflashsale.percent;

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

    //Kiểm tra tồn kho
    $scope.checkInventory = function (productId) {
        console.log($scope.inventory);
        const product = $scope.inventory.find(item => item.product.productid === productId);
        return product && product.quantityonhand > 0;
    }
    //

    //Điều hướng phân trang
    $scope.next = function () {
        if ($scope.totalPages == $scope.currentPage) return;
        console.log('Next');
        $scope.currentPage++;
        if ($scope.filterByCategory === true) return $scope.getDataByCategory();
        $scope.getFlashSale();
    };

    $scope.back = function () {
        if ($scope.currentPage == 1) return;
        console.log('Back');
        $scope.currentPage--;
        if ($scope.filterByCategory === true) return $scope.getDataByCategory();
        $scope.getFlashSale();
    };

    $scope.goToPage = function (page) {
        $scope.currentPage = page;
        console.log($scope.currentPage);

        $scope.getFlashSale();
    };

    $scope.numberArray = function () {
        var result = [];
        for (var i = 1; i <= $scope.totalPages; i++) {
            result.push(i);
        }
        return result;
    };
    //






    //Loc Sản Phẩm Theo Danh Mục
    $scope.categorys = [];
    $scope.CheckboxCategory = [];
    $scope.getDataCategory = function () {
        $http.get(hostCategory)
            .then(function (response) {
                $scope.categorys = response.data;
                console.log("Success", $scope.categorys[0].productname);
                console.log("Success", $scope.categorys.length);

                // Khởi tạo mảng chứa thông tin của các checkbox

                for (var i = 0; i < $scope.categorys.length; i++) {
                    console.log("hello");
                    var cboCategoryItem = {
                        label: 'CheckboxCategory' + i,
                        name: $scope.categorys[i].productname,
                        isChecked: false
                    };

                    console.log(cboCategoryItem);

                    // Thêm đối tượng vào mảng CheckboxCategory
                    $scope.CheckboxCategory.push(cboCategoryItem);
                }

            })
            .catch(function (error) {
                console.error('Error fetching category:', error);
            });
    };





    var IntegerArray = [];
    $scope.clickCategory = function (id, index) {
        console.log(id);
        if (id === 0 && index === 0) {
            window.location.href = `/product`;
        }

        if (id === 0 && index === 1) {
            window.location.href = `/product`;

            return;
        }

        $scope.filterByCategory = true;
        console.log("filterByCategory" + $scope.filterByCategory);

        IntegerArray.push(id);

        if (!$scope.CheckboxCategory[index].isChecked) {
            IntegerArray = IntegerArray.filter(number => number !== id);
            if ($scope.CheckboxCategory.every(category => category.isChecked === false)) {
                $scope.filterByCategory = false;
                return $scope.getFlashSale();
            }
        }

        $scope.currentPage = 1;
        $scope.getDataByCategory();

    }


    $scope.getDataByCategory = function () {

        var apiUrl = hostProductByCategory + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + '&categoryId=' + IntegerArray;
        $http.get(apiUrl)
            .then(function (response) {

                $scope.FlashSaleProducts = response.data.content;

                $scope.totalItems = response.data.totalElements;
                console.log($scope.totalItems);

                $scope.totalPages = parseInt(response.data.totalPages, 10);
                console.log($scope.totalPages);


            })
            .catch(function (error) {
                console.error('Error fetching products:', error);
            });
    }


    ////////////



    //Giỏ hàng

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

            // var percent = 0;
            // console.log("san pham khong sale:" + $scope.isProductInSale(cartItem.product.productid));
            // console.log(cartItem.product.productid);
            // if ($scope.isProductInSale(cartItem.product.productid)) {
            //     percent = $scope.getPercentSaleForProduct(cartItem.product.productid);
            // }
            // console.log(percent);
            var productPrice = cartItem.product.pricexuat;
            var quantity = cartItem.quantity;
            $scope.totalAmount += productPrice * quantity;
            cartItem.product.pricexuat = productPrice;
        });

    };

    ////////////

    // Load data for the first time
    $scope.getInventory();
    $scope.getDataCategory();

    // Call loadCart when the customer is loaded
    $scope.loadCart();



});
