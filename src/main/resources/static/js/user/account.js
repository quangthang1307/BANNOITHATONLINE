// Viết bằng js thuần
// var postRegister = 'http://localhost:8080/user/sigup'

// function start() {
//     handleCreateForm();
// }

// start();

// function createCoure(data, callblack) {
//     var options = {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify(data)
//     }
//     fetch(postRegister, options)
//         .then(function (respone) {
//             respone.json();
//         })
//         .then(callblack);
// }

// function handleCreateForm() {
//     var createBtn = document.querySelector('#create');

//     createBtn.onclick = function () {
//         var username = document.querySelector('input[name="username"]').value;
//         var email = document.querySelector('input[name="email"]').value;
//         var password = document.querySelector('input[name="password"]').value;

//         var formData = {
//             username: username,
//             email: email,
//             password: password
//         }
//         createCoure(formData)
//     }
// }

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
                //Đăng ký thành công
                Swal.fire(
                    'Thành công!',
                    response.data.message,
                    'success'
                ).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href='http://localhost:8080/login'
                    }
                });
            })
            .catch(function (error) {
                document.getElementById('notification').innerHTML = error.data.message1;
            });

    };
});