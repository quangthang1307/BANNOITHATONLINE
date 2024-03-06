//Viết bằng angular js
var app = angular.module('bannoithatonline', []);
app.controller('registerController', function ($scope, $http, $location) {

    $scope.registerUser = function () {
        var data = {
            username: $scope.username,
            email: $scope.email,
            password: $scope.password,
            name: $scope.name,
            phone: $scope.phone
        };

        $http.post('http://localhost:8080/rest/user/sigup', data)
        .then(function (response) {
            // Đăng ký thành công
            Swal.fire(
                'Thành công!',
                response.data.message,
                'success'
            ).then((result) => {
                if (result.isConfirmed) {
                    window.location.href = 'http://localhost:8080/login';
                }
            });
        })
        .catch(function (error) {
            // Đăng ký thất bại
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: error.data.message,
                confirmButtonText: 'OK'
            });
        });

    };
});
