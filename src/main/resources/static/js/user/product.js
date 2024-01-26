var app = angular.module("bannoithatonline", []);
const host = "http://localhost:8080/rest/product";
const hostCategory = "http://localhost:8080/rest/category";
const hostProductByCategory = "http://localhost:8080/rest/product/category";
const hostCustomerId = "http://localhost:8080/rest/customer";
const hostListCart = "http://localhost:8080/rest/showCart";
const hostUpQuantityProduct = "http://localhost:8080/rest/cart/up";
const hostDownQuantityProduct = "http://localhost:8080/rest/cart/down";
const hostDeleteProduct = "http://localhost:8080/rest/removeFromCart";
const hostProductImage = "http://localhost:8080/rest/products";
const hostDeleteAllProductInCart = "http://localhost:8080/rest/removeAllCarts";
const hostProductSale = "http://localhost:8080/rest/product/sale";


app.controller("productController", function ($scope, $http, $window) {

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

    //Gọi API lấy sản phẩm
    $scope.itemsPerPage = 9;
    $scope.currentPage = 1;
    $scope.totalItems = 0;
    $scope.totalPages = 0;
    $scope.products = [];
    $scope.listCart = [];
    $scope.productsale = [];

    $scope.getData = function () {

        var apiUrl = host + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage;

        $http.get(apiUrl)
            .then(function (response) {

                $scope.products = response.data.content;
                console.log($scope.products);

                $scope.totalItems = response.data.totalElements;
                console.log($scope.totalItems);

                $scope.totalPages = parseInt(response.data.totalPages, 10);
                console.log($scope.totalPages);

            })
            .catch(function (error) {
                console.error('Error fetching products:', error);
            }
            );

        $http.get(hostProductSale)
            .then(function (response) {

                $scope.productsale = response.data
                console.log($scope.productsale);


            })
            .catch(function (error) {
                console.error('Error fetching productsale:', error);
            }
            );
    };
    //


    //Kiểm tra sản phẩm sale để thay đổi giá
    $scope.isProductInSale = function (productId) {
        return $scope.productsale.some(item => item.productID === productId);
    };

    $scope.getPercentSaleForProduct = function (productId) {
        var foundItem = $scope.productsale.find(item => item.productID === productId);
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

    //Điều hướng phân trang
    $scope.next = function () {
        if ($scope.totalPages == $scope.currentPage) return;
        console.log('Next');
        $scope.currentPage++;
        $scope.getData();
    };

    $scope.back = function () {
        if ($scope.currentPage == 1) return;
        console.log('Back');
        $scope.currentPage--;
        $scope.getData();
    };

    $scope.goToPage = function (page) {
        $scope.currentPage = page;
        $scope.getData();
    };

    $scope.numberArray = function () {
        var result = [];
        for (var i = 1; i <= $scope.totalPages; i++) {
            result.push(i);
        }
        return result;
    };
    //








    //Loc Sản Phẩm
    $scope.getDataCategory = function () {

        $scope.categorys = [];

        $http.get(hostCategory)
            .then(function (response) {
                $scope.categorys = response.data;
                console.log("Success", $scope.categorys);

            })
            .catch(function (error) {
                console.error('Error fetching category:', error);
            });
    };


    $scope.clickCategory = function (id) {
        console.log("OK" + id)
        if (id === 0) return $scope.getData();
        var apiUrl = hostProductByCategory + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + '&categoryId=' + id;
        console.log(apiUrl);

        $http.get(apiUrl)
            .then(function (response) {

                console.log($scope.products);
                console.log(response.data);


                $scope.products = response.data.content;
                console.log($scope.products);

                $scope.totalItems = response.data.totalElements;
                console.log($scope.totalItems);

                $scope.totalPages = parseInt(response.data.totalPages, 10);
                console.log($scope.totalPages);

            })
            .catch(function (error) {
                console.error('Error fetching products:', error);
            });


    }
    //


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

            var percent = 0;
            console.log("san pham khong sale:" + $scope.isProductInSale(cartItem.product.productid));
            console.log(cartItem.product.productid);
            if ($scope.isProductInSale(cartItem.product.productid)) {
                percent = $scope.getPercentSaleForProduct(cartItem.product.productid);
            }
            console.log(percent);
            var productPrice = cartItem.product.pricexuat - (cartItem.product.pricexuat * percent / 100);
            var quantity = cartItem.quantity;
            $scope.totalAmount += productPrice * quantity;
            cartItem.product.pricexuat = productPrice;
        });

    };

    // Load data for the first time
    $scope.fetchCustomer();
    $scope.getData();
    $scope.getDataCategory();

    // Call loadCart when the customer is loaded
    $scope.loadCart();



});

//Product Details Controller
app.controller('productDetailController', function ($scope, $http) {
    $scope.productdetail = null;
    $scope.quantity = 1;
    $scope.getProductDetail = function () {
        $scope.productdetail = JSON.parse(localStorage.getItem('productById'));
        console.log("ProductDetail", $scope.productdetail);
    };

    // Hàm tăng số lượng
    $scope.increase = function () {
        $scope.quantity++;
    };

    // Hàm giảm số lượng
    $scope.decrease = function () {
        if ($scope.quantity > 1) {
            $scope.quantity--;
        }
    }

        // Load data for the first time
        $scope.getProductDetail();
    });
