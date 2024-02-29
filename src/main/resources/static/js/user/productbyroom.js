var app = angular.module("bannoithatonline", []);
const host = "/rest/product";
const hostCategory = "/rest/category";
const hostProductByCategory = "/rest/product/category";
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




app.controller("productController", function ($scope, $http, $window) {
    $scope.titlePage = "Tất cả sản phẩm";
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

    //Gọi API lấy sản phẩm
    $scope.itemsPerPage = 15;
    $scope.currentPage = 1;
    $scope.totalItems = 0;
    $scope.totalPages = 0;
    $scope.products = [];
    $scope.listCart = [];
    $scope.productSale = [];

    $scope.getData = function () {
        $scope.titlePage = JSON.parse(localStorage.getItem('titleCategory'))

        var apiProduct = hostProductByRoom + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + "&categoryId=" + JSON.parse(localStorage.getItem('idproductByroom'));;
        console.log(apiProduct);
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
                localStorage.setItem('productSale', JSON.stringify($scope.productSale));
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
        if ($scope.filterByCategory === true) return $scope.getDataByCategory();
        $scope.getData();
    };

    $scope.back = function () {
        if ($scope.currentPage == 1) return;
        console.log('Back');
        $scope.currentPage--;
        if ($scope.filterByCategory === true) return $scope.getDataByCategory();
        $scope.getData();
    };

    $scope.goToPage = function (page) {
        $scope.currentPage = page;
        console.log($scope.currentPage);
   
        if ($scope.filterByCategory === true && $scope.filterActiveDESC === false && $scope.filterActiveASC === false && $scope.filterActiveAZ === false && $scope.filterActiveZA === false) return $scope.getDataByCategory();
        
        if ($scope.filterActiveDESC === true) return $scope.getDataByCategoryDESC();
        
        if ($scope.filterByRoomDESC === true) return $scope.getDataByRoomDESC();

        if ($scope.filterActiveASC === true) return $scope.getDataByCategoryASC();
       
        if ($scope.filterByRoomASC === true) return $scope.getDataByRoomASC();

        if ($scope.filterActiveAZ === true) return $scope.getDataByCategoryAZ();
        
        if ($scope.filterByRoomAZ === true) return $scope.getDataByRoomAZ();

        if ($scope.filterActiveZA === true) return $scope.getDataByCategoryZA();
        
        if ($scope.filterByRoomZA === true) return $scope.getDataByRoomZA();

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
        var api = hostProductByRoomAndCategory + "?categoryId=" + JSON.parse(localStorage.getItem('idproductByroom'));
        $http.get(api)
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
                return $scope.getData();
            }
        }

        $scope.currentPage = 1;
        if ($scope.filterBySale === true) return $scope.getDataProductSaleByCategory();
        $scope.getDataByCategory();

    }

    // $scope.getFifteenProductSale = function () {
    //     // Tính toán vị trí bắt đầu và kết thúc của dãy phần tử cần lấy
    //     console.log("on sale" + $scope.itemsPerPage);
    //     let startIndex = ($scope.currentPage - 1) * $scope.itemsPerPage;
    //     let endIndex = startIndex + $scope.itemsPerPage;

    //     // Kiểm tra nếu vị trí kết thúc lớn hơn kích thước của mảng
    //     if (endIndex > $scope.products.length) {
    //         endIndex = $scope.products.length; // Đặt vị trí kết thúc là kích thước của mảng
    //     }
    //     console.log($scope.products);
    //     // Lấy các phần tử từ mảng sử dụng slice
    //     $scope.products = $scope.products.slice(startIndex, endIndex);
    //     console.log($scope.products);
    // }

    $scope.getDataByRoomDESC = function(){
        var apiProduct = hostProductByRoomDESC + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + "&categoryId=" + JSON.parse(localStorage.getItem('idproductByroom'));;
        console.log(apiProduct);
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
    }

    $scope.getDataByRoomASC = function(){
        var apiProduct = hostProductByRoomASC + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + "&categoryId=" + JSON.parse(localStorage.getItem('idproductByroom'));;
        console.log(apiProduct);
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
    }

    $scope.getDataByRoomAZ = function(){
        var apiProduct = hostProductByRoomAZ + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + "&categoryId=" + JSON.parse(localStorage.getItem('idproductByroom'));;
        console.log(apiProduct);
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
    }

    $scope.getDataByRoomZA = function(){
        var apiProduct = hostProductByRoomZA + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + "&categoryId=" + JSON.parse(localStorage.getItem('idproductByroom'));;
        console.log(apiProduct);
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
    }

    $scope.getDataByCategory = function () {

        var apiUrl = hostProductByCategory + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + '&categoryId=' + IntegerArray;
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

    $scope.getDataByCategoryDESC = function () {

        var apiUrl = hostProductByCategoryDESC + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + '&categoryId=' + IntegerArray;
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

    $scope.getDataByCategoryASC = function () {

        var apiUrl = hostProductByCategoryASC + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + '&categoryId=' + IntegerArray;
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

    $scope.getDataByCategoryAZ = function () {

        var apiUrl = hostProductByCategoryAZ + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + '&categoryId=' + IntegerArray;
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

    $scope.getDataByCategoryZA = function () {

        var apiUrl = hostProductByCategoryZA + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + '&categoryId=' + IntegerArray;
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
    $scope.filterProductDESC = function () {
        var apiProductASC = hostProductDESC + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage;
        console.log(apiProductASC);
        $http.get(apiProductASC)
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

    $scope.filterProductASC = function () {
        var apiProductASC = hostProductASC + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage;
        $http.get(apiProductASC)
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

    $scope.filterProductAZ = function () {
        var apiProductASC = hostProductAZ + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage;
        $http.get(apiProductASC)
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

    $scope.filterProductZA = function () {
        var apiProductASC = hostProductZA + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage;
        $http.get(apiProductASC)
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

    
    $scope.filterActiveDESC = false;
    $scope.filterActiveASC = false;
    $scope.filterActiveAZ = false;
    $scope.filterActiveZA = false;

    $scope.filterByRoomDESC = false;
    $scope.filterByRoomASC = false;
    $scope.filterByRoomAZ = false;
    $scope.filterByRoomZA = false;

    $scope.clickFilter = function (key) {
        switch (key) {
            case 1:
                if ($scope.filterByCategory) {
                    $scope.filterActiveDESC = false;
                    $scope.filterActiveASC = true;
                    $scope.filterActiveAZ = false;
                    $scope.filterActiveZA = false;
                    $scope.getDataByCategoryASC();
                }
                else {
                    $scope.filterByRoomDESC = false;
                    $scope.filterByRoomASC = true;
                    $scope.filterByRoomAZ = false;
                    $scope.filterByRoomZA = false;
                    $scope.getDataByRoomASC();
                }

                break;
            case 2:
                if ($scope.filterByCategory) {
                    $scope.filterActiveDESC = true;
                    $scope.filterActiveASC = false;
                    $scope.filterActiveAZ = false;
                    $scope.filterActiveZA = false;
                    $scope.getDataByCategoryDESC();
                }
                else {
                    $scope.filterByRoomDESC = true;
                    $scope.filterByRoomASC = false;
                    $scope.filterByRoomAZ = false;
                    $scope.filterByRoomZA = false;
                    $scope.getDataByRoomDESC();
                }
                break;
            case 3:
                // Sắp xếp mảng theo trường "productname" từ a đến z
                if ($scope.filterByCategory) {
                    $scope.filterActiveDESC = false;
                    $scope.filterActiveASC = false;
                    $scope.filterActiveAZ = true;
                    $scope.filterActiveZA = false;
                    $scope.getDataByCategoryAZ();
                }
                else {
                    $scope.filterByRoomDESC = false;
                    $scope.filterByRoomASC = false;
                    $scope.filterByRoomAZ = true;
                    $scope.filterByRoomZA = false;
                    $scope.getDataByRoomAZ();
                }
                

                break;
            case 4:
                // Sắp xếp mảng theo trường "productname" từ z đến a
                if ($scope.filterByCategory) {
                    $scope.filterActiveDESC = false;
                    $scope.filterActiveASC = false;
                    $scope.filterActiveAZ = false;
                    $scope.filterActiveZA = true;
                    $scope.getDataByCategoryZA();
                }
                else {
                    $scope.filterByRoomDESC = false;
                    $scope.filterByRoomASC = false;
                    $scope.filterByRoomAZ = false;
                    $scope.filterByRoomZA = true;
                    $scope.getDataByRoomZA();
                }

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
