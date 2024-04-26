var app = angular.module("bannoithatonline", []);
const host = "/rest/product";
const hostCategory = "/rest/category";
const hostProductByCategoryAndPrice = "/rest/product/categoryprice";
const hostProductByCategory = "/rest/product/category";
const hostProductByCategoryDESC = "/rest/product/category/desc";
const hostProductByCategoryASC = "/rest/product/category/asc";
const hostProductByCategoryAZ = "/rest/product/category/az";
const hostProductByCategoryZA = "/rest/product/category/za";

const hostGetEvaluateByProductID = "/rest/evaluate/";
const hostTop5ProductByCategory = "/rest/product/relate";
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

const hostProductAZ = "/rest/product/az";
const hostProductZA = "/rest/product/za";

const hostProductFlashsale = "/rest/product/flashsale";

const hostInventory = "/rest/product/inventory";
const hostPrice = "/rest/product/priceon";
const hostSaleCategoryPrice = "/rest/productsale/categoryprice";
const hostSalePrice = "/rest/productsale/priceon";


app.controller("productController", function ($scope, $http, $window) {
    $scope.titlePage = "Tất cả sản phẩm";
    $scope.filterByCategory = false;
    $scope.filterBySale = false;
    $scope.filterByPrice = false;
    $scope.locationInProductSale = 0;
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
    $scope.productFlashSale = [];
    $scope.inventory = [];

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
                localStorage.setItem('productSale', JSON.stringify($scope.productSale));
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
                if ($scope.isProductInSale(resp.data.productid) && !$scope.isProductInFlashSale(resp.data.productid)) {
                    var percent = $scope.getPercentSaleForProduct(resp.data.productid);
                    resp.data.percent = percent;
                } else if (!$scope.isProductInSale(resp.data.productid) && $scope.isProductInFlashSale(resp.data.productid)) {
                    var percent = $scope.getPercentFlashSaleForProduct(resp.data.productid);
                    resp.data.percent = percent;
                } else if ($scope.isProductInSale(resp.data.productid) && $scope.isProductInFlashSale(resp.data.productid)) {
                    var percent = $scope.getPercentFlashSaleForProduct(resp.data.productid);
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
        if ($scope.filterByCategory === true && $scope.filterByCategoryDESC == false && $scope.filterByCategoryASC == false && $scope.filterByCategoryAZ == false && $scope.filterByCategoryZA == false) return $scope.getDataByCategory();

        if ($scope.filterByCategory === true && $scope.filterByCategoryDESC == true) return $scope.getDataByCategoryDESC();

        if ($scope.filterByCategory === true && $scope.filterByCategoryASC == true) return $scope.getDataByCategoryASC();

        if ($scope.filterByCategory === true && $scope.filterByCategoryAZ == true) return $scope.getDataByCategoryAZ();

        if ($scope.filterByCategory === true && $scope.filterByCategoryZA == true) return $scope.getDataByCategoryZA();

        if ($scope.filterBySale === true && $scope.filterBySaleDESC === false && $scope.filterBySaleASC === false && $scope.filterBySaleAZ === false && $scope.filterBySaleZA === false) return $scope.getDataProductSale();

        if ($scope.filterBySale === true && $scope.filterBySaleDESC === true) return $scope.filterProductSaleDESC();

        if ($scope.filterBySale === true && $scope.filterBySaleASC === true) return $scope.filterProductSaleASC();

        if ($scope.filterBySale === true && $scope.filterBySaleAZ === true) return $scope.filterProductSaleAZ();

        if ($scope.filterBySale === true && $scope.filterBySaleZA === true) return $scope.filterProductSaleZA();

        if ($scope.filterActiveDESC === true) return $scope.filterProductDESC();

        if ($scope.filterActiveASC === true) return $scope.filterProductASC();

        if ($scope.filterActiveAZ === true) return $scope.filterProductAZ();

        if ($scope.filterActiveZA === true) return $scope.filterProductZA();

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
    $scope.CheckboxPrice =
        [
            {
                label: 'Dưới 1.000.000₫',
                minPrice: 0,
                maxPrice: 1000000,
                isChecked: false
            },
            {
                label: '1.000.000₫ - 2.000.000₫',
                minPrice: 1000000,
                maxPrice: 2000000,
                isChecked: false
            },
            {
                label: '2.000.000₫ - 3.000.000₫',
                minPrice: 2000000,
                maxPrice: 3000000,
                isChecked: false
            },
            {
                label: '3.000.000₫ - 4.000.000₫',
                minPrice: 3000000,
                maxPrice: 4000000,
                isChecked: false
            },
            {
                label: 'Trên 4.000.000₫',
                minPrice: 4000000,
                maxPrice: 1000000000,
                isChecked: false
            },
            {
                label: 'macdinh',
                minPrice: 0,
                maxPrice: 1000000000,
                isChecked: false
            }
        ]
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
            $scope.filterBySale = false;
            $scope.filterActiveASC = false;
            $scope.filterActiveDESC = false;
            $scope.filterActiveAZ = false;
            $scope.filterActiveZA = false;
            $scope.filterByCategory = false;
            $scope.CheckboxCategory.forEach(function (item) {
                if (item.isChecked) {
                    item.isChecked = false;
                }
            });
            $scope.filterByPrice = false;
            $scope.CheckboxPrice.forEach(function (item) {
                if (item.isChecked) {
                    item.isChecked = false;
                }
            });
            $scope.titlePage = "Tất cả sản phẩm";
            return $scope.getData();
        }

        if (id === 0 && index === 1) {
            console.log($scope.productSale);
            $scope.filterBySale = true;
            $scope.filterByCategory = false;
            $scope.CheckboxCategory.forEach(function (item) {
                if (item.isChecked) {
                    item.isChecked = false;
                }
            });
            $scope.filterByPrice = false;
            $scope.CheckboxPrice.forEach(function (item) {
                if (item.isChecked) {
                    item.isChecked = false;
                }
            });
            $scope.getDataProductSale();
            $scope.titlePage = "Sản phẩm khuyến mãi";

            return;
        }

        $scope.filterByCategory = true;


        IntegerArray.push(id);
        console.log(IntegerArray.length);

        if (!$scope.CheckboxCategory[index].isChecked) {
            IntegerArray = IntegerArray.filter(number => number !== id);
            if ($scope.CheckboxCategory.every(category => category.isChecked === false)) {
                if ($scope.filterBySale === true) {
                    return $scope.getDataProductSale();
                }
                if ($scope.CheckboxPrice.every(category => category.isChecked === false)) {
                    $scope.filterByCategory = false;
                    return $scope.getData();
                }

                return getDataByCategory();

            }
        }

        $scope.currentPage = 1;
        if ($scope.filterBySale === true) return $scope.getDataProductSaleByCategory();
        $scope.getDataByCategory();

    }

    var PriceArray = [];
    $scope.clickfillprice = function (index) {
        if (!$scope.CheckboxPrice[index].isChecked) {
            PriceArray = PriceArray.filter(item => item.name !== $scope.CheckboxPrice[index].label);
            if ($scope.CheckboxPrice.every(category => category.isChecked === false)) {
                if ($scope.CheckboxCategory.every(category => category.isChecked === false)) {
                    $scope.filterByPrice = false;
                    if ($scope.filterBySale === true) return $scope.getDataProductSale();
                    return $scope.getData();
                }
                $scope.filterByPrice = false;
                if ($scope.filterBySale === true) return $scope.getDataProductSaleByCategory();
                return $scope.getDataByCategory();
            }
            console.table(PriceArray);
            $scope.productssum = [];
            if ($scope.filterBySale === true) return $scope.getDataSaleByCategoryAndPrice();
            $scope.getDataByCategory();
            return
        }
        var price = {
            name: $scope.CheckboxPrice[index].label,
            minPrice: $scope.CheckboxPrice[index].minPrice,
            maxPrice: $scope.CheckboxPrice[index].maxPrice
        }
        $scope.filterByPrice = true;
        PriceArray.push(price);
        console.table(PriceArray);
        if ($scope.filterBySale === true) return $scope.getDataSaleByCategoryAndPrice();
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

    $scope.productssum = [];
    $scope.getDataByCategory = function () {
        if (PriceArray.length <= 0) {
            var apiUrl = hostProductByCategory + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + '&categoryId=' + IntegerArray;
            $scope.getProductByCategory(apiUrl)
            return
        }

        $scope.productssum = [];
        PriceArray.forEach(function (element) {
            console.log(element.minPrice); // In ra từng phần tử của mảng
            if (IntegerArray.length <= 0) {
                var apiUrl = hostPrice + '?minprice=' + element.minPrice + '&maxprice=' + element.maxPrice;
                console.log(apiUrl);
                $scope.getProductByCPriceon(apiUrl)
            } else {
                var apiUrl = hostProductByCategoryAndPrice + '?categoryId=' + IntegerArray + '&minprice=' + element.minPrice + '&maxprice=' + element.maxPrice;
                console.log(apiUrl);
                $scope.getProductByCPriceon(apiUrl)
            }

        });

    }
    $scope.getDataSaleByCategoryAndPrice = function () {
        // if (PriceArray.length <= 0) {
        //     var apiUrl = hostProductByCategory + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + '&categoryId=' + IntegerArray;
        //     $scope.getProductByCategory(apiUrl)
        //     return
        // }

        $scope.productssum = [];
        PriceArray.forEach(function (element) {
            console.log(element.minPrice); // In ra từng phần tử của mảng
            if (IntegerArray.length <= 0) {
                var apiUrl = hostSalePrice + '?minprice=' + element.minPrice + '&maxprice=' + element.maxPrice;
                console.log(apiUrl);
                $scope.getProductByCPriceon(apiUrl)
            } else {
                var apiUrl = hostSaleCategoryPrice + '?categoryId=' + IntegerArray + '&minprice=' + element.minPrice + '&maxprice=' + element.maxPrice;
                console.log(apiUrl);
                $scope.getProductByCPriceon(apiUrl)
            }

        });

    }
    $scope.getProductByCategory = function (apiUrl) {
        $http.get(apiUrl)
            .then(function (response) {

                $scope.products = response.data.content;
                console.table($scope.products);

                $scope.totalItems = response.data.totalElements;
                console.log($scope.totalItems);

                $scope.totalPages = parseInt(response.data.totalPages, 10);
                console.log($scope.totalPages);


            })
            .catch(function (error) {
                console.error('Error fetching products:', error);
            });
    }

    $scope.getProductByCPriceon = function (apiUrl) {
        $http.get(apiUrl)
            .then(function (response) {

                $scope.productssum = $scope.productssum.concat(response.data);
                $scope.products = $scope.productssum;
                console.table($scope.productssum);
                $scope.totalItems = $scope.products.length
                console.log($scope.totalItems);

                $scope.totalPages = 0
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

    $scope.getDataProductSale = function () {
        var apiProductASC = hostProductSales + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage;
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

    $scope.filterProductSaleDESC = function () {
        var apiProductASC = hostProductSaleDESC + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage;
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

    $scope.filterProductSaleASC = function () {
        var apiProductASC = hostProductSaleASC + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage;
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

    $scope.filterProductSaleAZ = function () {
        var apiProductASC = hostProductSaleAZ + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage;
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

    $scope.filterProductSaleZA = function () {
        var apiProductASC = hostProductSaleZA + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage;
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

    $scope.getDataProductSaleByCategory = function () {
        var apiProductASC = hostProductSaleByCategory + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + '&categoryId=' + IntegerArray;
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

    $scope.getDataProductSaleByCategoryAndDESC = function () {
        var apiProductASC = hostProductSaleByCategoryAndDESC + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + '&categoryId=' + IntegerArray;
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

    $scope.getDataProductSaleByCategoryAndASC = function () {
        var apiProductASC = hostProductSaleByCategoryAndASC + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + '&categoryId=' + IntegerArray;
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

    $scope.getDataProductSaleByCategoryAndAZ = function () {
        var apiProductASC = hostProductSaleByCategoryAndAZ + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + '&categoryId=' + IntegerArray;
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

    $scope.getDataProductSaleByCategoryAndZA = function () {
        var apiProductASC = hostProductSaleByCategoryAndZA + '?page=' + ($scope.currentPage - 1) + '&size=' + $scope.itemsPerPage + '&categoryId=' + IntegerArray;
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
    $scope.filterActive = false;
    $scope.filterActiveDESC = false;
    $scope.filterActiveASC = false;
    $scope.filterActiveAZ = false;
    $scope.filterActiveZA = false;

    $scope.filterBySaleDESC = false;
    $scope.filterBySaleASC = false;
    $scope.filterBySaleAZ = false;
    $scope.filterBySaleZA = false;

    $scope.filterSaleByCategoryDESC = false;
    $scope.filterSaleByCategoryASC = false;
    $scope.filterSaleByCategoryAZ = false;
    $scope.filterSaleByCategoryZA = false;

    $scope.filterByCategoryDESC = false;
    $scope.filterByCategoryASC = false;
    $scope.filterByCategoryAZ = false;
    $scope.filterByCategoryZA = false;
    $scope.clickFilter = function (key) {
        switch (key) {
            case 1:
                if ($scope.filterBySale && !$scope.filterByCategory) {

                    $scope.filterBySaleASC = true;

                    $scope.filterBySaleDESC = false;
                    $scope.filterBySaleAZ = false;
                    $scope.filterBySaleZA = false;
                    $scope.filterProductSaleASC();
                } else if ($scope.filterBySale && $scope.filterByCategory) {

                    $scope.filterSaleByCategoryASC = true;

                    $scope.filterBySaleASC = false;
                    $scope.filterBySaleDESC = false;
                    $scope.filterBySaleAZ = false;
                    $scope.filterBySaleZA = false;
                    $scope.filterSaleByCategoryDESC = false;
                    $scope.filterSaleByCategoryAZ = false;
                    $scope.filterSaleByCategoryZA = false;
                    $scope.getDataProductSaleByCategoryAndASC();
                } else if (!$scope.filterBySale && $scope.filterByCategory) {
                    $scope.filterByCategoryDESC = false;
                    $scope.filterByCategoryASC = true;
                    $scope.filterByCategoryAZ = false;
                    $scope.filterByCategoryZA = false;

                    $scope.getDataByCategoryASC();
                }else if ($scope.filterByPrice) {
                    $scope.products.sort(function(a, b) {                     
                        return a.pricexuat - b.pricexuat;
                    });
                   
                }else {
                    $scope.filterActiveASC = true;
                    $scope.filterActiveDESC = false;
                    $scope.filterActiveAZ = false;
                    $scope.filterActiveZA = false;
                    $scope.filterProductASC();
                }

                break;
            case 2:
                if ($scope.filterBySale && !$scope.filterByCategory) {
                    $scope.filterBySaleASC = false;
                    $scope.filterBySaleDESC = true;
                    $scope.filterBySaleAZ = false;
                    $scope.filterBySaleZA = false;
                    $scope.filterProductSaleDESC();
                } else if ($scope.filterBySale && $scope.filterByCategory) {

                    $scope.filterSaleByCategoryDESC = true;

                    $scope.filterBySaleASC = false;
                    $scope.filterBySaleDESC = false;
                    $scope.filterBySaleAZ = false;
                    $scope.filterBySaleZA = false;
                    $scope.filterSaleByCategoryASC = false;
                    $scope.filterSaleByCategoryAZ = false;
                    $scope.filterSaleByCategoryZA = false;
                    $scope.getDataProductSaleByCategoryAndDESC();
                }
                else if (!$scope.filterBySale && $scope.filterByCategory) {
                    $scope.filterByCategoryDESC = true;
                    $scope.filterByCategoryASC = false;
                    $scope.filterByCategoryAZ = false;
                    $scope.filterByCategoryZA = false;

                    $scope.getDataByCategoryDESC();
                }else if ($scope.filterByPrice) {
                    $scope.products.sort(function(a, b) {                     
                        return b.pricexuat - a.pricexuat;
                    });
                   
                }
                else {
                    $scope.filterActiveDESC = true;
                    $scope.filterActiveASC = false;
                    $scope.filterActiveAZ = false;
                    $scope.filterActiveZA = false;
                    $scope.filterProductDESC();
                }

                break;
            case 3:
                // Sắp xếp mảng theo trường "productname" từ a đến z
                if ($scope.filterBySale && !$scope.filterByCategory) {
                    $scope.filterBySaleASC = false;
                    $scope.filterBySaleDESC = false;
                    $scope.filterBySaleAZ = true;
                    $scope.filterBySaleZA = false;
                    $scope.filterProductSaleAZ();
                } else if ($scope.filterBySale && $scope.filterByCategory) {

                    $scope.filterSaleByCategoryAZ = true;

                    $scope.filterBySaleASC = false;
                    $scope.filterBySaleDESC = false;
                    $scope.filterBySaleAZ = false;
                    $scope.filterBySaleZA = false;
                    $scope.filterSaleByCategoryASC = false;
                    $scope.filterSaleByCategoryDESC = false;
                    $scope.filterSaleByCategoryZA = false;
                    $scope.getDataProductSaleByCategoryAndAZ();
                } else if (!$scope.filterBySale && $scope.filterByCategory) {
                    $scope.filterByCategoryDESC = false;
                    $scope.filterByCategoryASC = false;
                    $scope.filterByCategoryAZ = true;
                    $scope.filterByCategoryZA = false;

                    $scope.getDataByCategoryAZ();
                }else if ($scope.filterByPrice) {
                    $scope.products.sort(function(a, b) {
                        var nameA = a.productname.toUpperCase(); // Chuyển tên thành chữ hoa để so sánh không phân biệt chữ hoa - chữ thường
                        var nameB = b.productname.toUpperCase(); // Chuyển tên thành chữ hoa để so sánh không phân biệt chữ hoa - chữ thường
                        
                        if (nameA < nameB) {
                            return -1; // Trả về số nhỏ hơn để sắp xếp tên tăng dần
                        } else if (nameA > nameB) {
                            return 1; // Trả về số lớn hơn để sắp xếp tên tăng dần
                        } else {
                            return 0; // Trả về 0 nếu tên bằng nhau
                        }
                    });     
                }
                else {
                    $scope.filterActiveDESC = false;
                    $scope.filterActiveASC = false;
                    $scope.filterActiveAZ = true;
                    $scope.filterActiveZA = false;
                    $scope.filterProductAZ();
                }

                break;
            case 4:
                // Sắp xếp mảng theo trường "productname" từ z đến a
                if ($scope.filterBySale && !$scope.filterByCategory) {
                    $scope.filterBySaleASC = false;
                    $scope.filterBySaleDESC = false;
                    $scope.filterBySaleAZ = false;
                    $scope.filterBySaleZA = true;
                    $scope.filterProductSaleZA();
                } else if ($scope.filterBySale && $scope.filterByCategory) {

                    $scope.filterSaleByCategoryZA = true;

                    $scope.filterBySaleASC = false;
                    $scope.filterBySaleDESC = false;
                    $scope.filterBySaleAZ = false;
                    $scope.filterBySaleZA = false;
                    $scope.filterSaleByCategoryASC = false;
                    $scope.filterSaleByCategoryAZ = false;
                    $scope.filterSaleByCategoryDESC = false;
                    $scope.getDataProductSaleByCategoryAndZA();
                } else if (!$scope.filterBySale && $scope.filterByCategory) {
                    $scope.filterByCategoryDESC = false;
                    $scope.filterByCategoryASC = false;
                    $scope.filterByCategoryAZ = false;
                    $scope.filterByCategoryZA = true;

                    $scope.getDataByCategoryZA();
                }else if ($scope.filterByPrice) {
                    $scope.products.sort(function(a, b) {
                        var nameA = a.productname.toUpperCase(); // Chuyển tên thành chữ hoa để so sánh không phân biệt chữ hoa - chữ thường
                        var nameB = b.productname.toUpperCase(); // Chuyển tên thành chữ hoa để so sánh không phân biệt chữ hoa - chữ thường
                        
                        if (nameA < nameB) {
                            return 1; // Trả về số lớn hơn để sắp xếp tên giảm dần
                        } else if (nameA > nameB) {
                            return -1; // Trả về số nhỏ hơn để sắp xếp tên giảm dần
                        } else {
                            return 0; // Trả về 0 nếu tên bằng nhau
                        }
                    }); 
                   
                }
                else {
                    $scope.filterActiveDESC = false;
                    $scope.filterActiveASC = false;
                    $scope.filterActiveAZ = false;
                    $scope.filterActiveZA = true;
                    $scope.filterProductZA();
                }

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
    $scope.productRelates = null;
    $scope.productSale = null;
    $scope.productFlashSale = null;
    $scope.inventory = [];
    $scope.quantity = 1;
    $scope.getProductDetail = function () {
        $scope.productdetail = JSON.parse(localStorage.getItem('productById'));
        $scope.productSale = JSON.parse(localStorage.getItem('productSale'));
        $scope.productFlashSale = JSON.parse(localStorage.getItem('productFlashSale'));
        $scope.inventory = JSON.parse(localStorage.getItem('productInventory'));
        $scope.getTop5ProductRelate($scope.productdetail.category.id);
        console.log("ProductDetail", $scope.productdetail);
        console.log("productSale", $scope.productSale);
    };
    //get đánh giá
    $scope.getEvaluate = function (productdetail) {
        console.log("productdetail", productdetail);
        $http.get(hostGetEvaluateByProductID + productdetail.productid)
            .then(function (response) {

                $scope.productEvaluate = response.data;
                console.log($scope.productEvaluate);

            })
            .catch(function (error) {
                console.error('Error fetching products:', error);
            }
            );
    }

    $scope.getTop5ProductRelate = function (categoryid) {
        var api = hostTop5ProductByCategory + '?categoryId=' + categoryid;
        $http.get(api)
            .then(function (response) {

                $scope.productRelates = response.data;
                console.log($scope.productRelates);

            })
            .catch(function (error) {
                console.error('Error fetching products:', error);
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

    $scope.addToCart = function (product) {
        // var product = $scope.productdetail
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
    $scope.getEvaluate($scope.productdetail);

});
