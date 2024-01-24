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

    $scope.getCustomer = function () {
        // Gán CustomerId người dùng
        $http.get(hostCustomerId).then(function (response) {
            $scope.customer = response.data;
            $window.localStorage.setItem("customerId", JSON.stringify($scope.customer));
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
                if ($scope.isProductInSale(resp.data.productid)){
                   var percent = $scope.getPercentSaleForProduct(resp.data.productid);
                   resp.data.percent = percent;
                }else{
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



    $scope.addToCart = function (product) {
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

                    $http.put(url);
                    Swal.fire({
                        title: "Thêm sản phẩm thành công !",
                        text: "",
                        icon: "success",
                        confirmButtonText: "OK",
                        timer: 850,
                    });
                } else {
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
                console.error("Error checking cart:", error);
            });
    };




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





    $scope.loadCart = function () {
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
    $scope.getCustomer();
    $scope.loadCart();
    // Load data for the first time
    $scope.getData();
    $scope.getDataCategory();




});

//Product Details Controller
app.controller('productDetailController', function ($scope, $http) {
    $scope.productdetail = [];
    console.log("ProductDetailController");
    $scope.productdetail = JSON.parse(localStorage.getItem('productById'));

    console.log("ProductDetail", $scope.productdetail); 
});
