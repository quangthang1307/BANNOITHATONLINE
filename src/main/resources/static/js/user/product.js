var app = angular.module("bannoithatonline", []);
const host = "/rest/product";
const hostCategory = "/rest/category";
const hostProductByCategory = "/rest/product/category";
const hostCustomerId = "/rest/customer";
const hostListCart = "/rest/showCart";
const hostUpQuantityProduct = "/rest/cart/up";
const hostDownQuantityProduct = "/rest/cart/down";
const hostDeleteProduct = "/rest/removeFromCart";
const hostProductImage = "/rest/products";
const hostDeleteAllProductInCart = "/rest/removeAllCarts";
const hostProductSale = "/rest/product/sale";
const hostProductDESC = "/rest/product/desc";
const hostProductASC = "/rest/product/asc";


app.controller("productController", function ($scope, $http, $window) {
    $scope.titlePage = "Tất cả sản phẩm";

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
    $scope.itemsPerPage = 15;
    $scope.currentPage = 1;
    $scope.totalItems = 0;
    $scope.totalPages = 0;
    $scope.products = [];
    $scope.listCart = [];
    $scope.productSale = [];

    $scope.getData = function () {

        var apiProduct = host + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage;
        $http.get(apiProduct)
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

                $scope.productSale = response.data
                console.log($scope.productSale);

            })
            .catch(function (error) {
                console.error('Error fetching productSale:', error);
            }
            );

    };
    //


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
                        isChecked: false
                    };

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
            $scope.CheckboxCategory.forEach(function (item) {
                if (item.isChecked) {
                    item.isChecked = false;
                }
            });
            $scope.titlePage = "Tất cả sản phẩm";
            return $scope.getData();
        }

        if (id === 0 && index === 1) {
            console.log("OKKKKK");
            // Tạo một mảng chứa tất cả các productID từ mảng data
            const productIDs = $scope.productSale.map(item => item.productID);
            console.log(productIDs);
            // Lọc sản phẩm từ mảng dựa vào productID có trong mảng data
            $scope.products = $scope.products.filter(product => productIDs.includes(product.productid));
           
            $scope.titlePage = "Sản phẩm khuyến mãi";

            $scope.totalItems = $scope.productSale.length;

            $scope.totalPages = Math.ceil(  $scope.totalItems / 15);

            return;
        }


        IntegerArray.push(id);

        if (!$scope.CheckboxCategory[index].isChecked) {
            IntegerArray = IntegerArray.filter(number => number !== id);
            if ($scope.CheckboxCategory.every(category => category.isChecked === false)) return $scope.getData();
        }

        var apiUrl = hostProductByCategory + '?page=0' + '&size=' + $scope.itemsPerPage + '&categoryId=' + IntegerArray;
        $scope.currentPage = 1;
        console.log(apiUrl);
        $http.get(apiUrl)
            .then(function (response) {

                $scope.products = response.data.content;

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

    //Lọc Sản Phẩm
    $scope.filterActive = false;
    $scope.clickFilter = function (key) {
        switch (key) {
            case 1:
                $scope.products.sort((a, b) => a.pricexuat - b.pricexuat);
                break;
            case 2:
                $scope.products.sort((a, b) => b.pricexuat - a.pricexuat);
                break;
            case 3:
                // Sắp xếp mảng theo trường "productname" từ a đến z
                $scope.products.sort((a, b) => {
                    const nameA = a.productname.toLowerCase();
                    const nameB = b.productname.toLowerCase();
                    if (nameA < nameB) return -1;
                    if (nameA > nameB) return 1;
                    return 0;
                });
                break;
            case 4:
                // Sắp xếp mảng theo trường "productname" từ z đến a
                $scope.products.sort((a, b) => {
                    const nameA = a.productname.toLowerCase();
                    const nameB = b.productname.toLowerCase();
                    if (nameA > nameB) return -1;
                    if (nameA < nameB) return 1;
                    return 0;
                });
                break;
            case 5:

                break;
            case 6:

                break;
            case 7:

                break;
            case 8:

                break;

            default:
                break;
        }
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
    $scope.getData();
    $scope.getDataCategory();

    // Call loadCart when the customer is loaded
    $scope.loadCart();



});

//Product Details Controller
app.controller('productDetailController', function ($scope, $http) {

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

    $scope.addToCart = function () {
        var product = $scope.productdetail
        if ($scope.customer === null) {
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
            cartItem.product.pricexuat = productPrice;
        });

    };

    // Load data for the first time
    $scope.getProductDetail();
    $scope.loadCart();
});
