const app = angular.module("bannoithatonline", []);
const hostListDiscount = "http://localhost:8080/rest/discount"
app.controller("checkoutController", function ($scope,$http,$window, $timeout){
    var showListProduct = JSON.parse(localStorage.getItem("listPayment"));
    $scope.products = showListProduct;

    $scope.discountCode = '';
    $scope.valueDiscountCode = 0;

    $scope.discount = function () {
        let foundDiscount = false;
        if ($scope.discountCode.length > 0) {

            $http.get(hostListDiscount).then((resp) => {
                resp.data.forEach(element => {
                    const currentDate = new Date();
                    const startDate = new Date(element.startDate);
                    const endDate = new Date(element.endDate);
                    if ($scope.discountCode === element.code) {
                        if (!(currentDate >= startDate && currentDate <= endDate)) {
                            foundDiscount = false;
                            $scope.discountCode = '';
                            Swal.fire({
                                title: "Mã giảm giá không hợp lệ !",
                                icon: "error",
                                showConfirmButton: false,
                                timer: 3000,
                                customClass: {
                                    popup: 'custom-popup-class',
                                    title: 'custom-title-class',
                                },
                            });
                        } else if (element.quantityUsed >= element.quantity) {
                            foundDiscount = false;
                            $scope.discountCode = ''
                            Swal.fire({
                                title: "Mã giảm giá đã hết lượt sử dụng !",
                                icon: "error",
                                showConfirmButton: false,
                                timer: 3000,
                                customClass: {
                                    popup: 'custom-popup-class',
                                    title: 'custom-title-class',
                                },
                            });
                        } else if ((currentDate >= startDate && currentDate <= endDate) && (element.quantityUsed < element.quantity)) {
                            $scope.valueDiscountCode = $scope.products[0].TotalPayment * element.percent / 100;
                            foundDiscount = true;
                            console.log('tren', foundDiscount);
                        }
                    }
                });
            })
        }
        // Dời kiểm tra xuống đây để đảm bảo kiểm tra sau khi vòng lặp kết thúc
        if (!foundDiscount) {
            console.log('duoi', foundDiscount);
            console.log($scope.valueDiscountCode);
            $scope.valueDiscountCode = 0;
        }

    }

    $scope.checkClear = function() {
        // Kiểm tra xem input có trống hay không
        document.getElementById("discountInput").addEventListener("input", function() {
            var discountCode = document.getElementById("discountInput").value;
            if(!discountCode){
                $scope.valueDiscountCode = 0;
            }
        });
    };





    
    console.log(showListProduct);
});