const app = angular.module("bannoithatonline", []);
const hostFindCustomer = "http://localhost/rest/findCustomer/1";
app.controller("CustomerController", function ($scope, $http, $window){
    $http.get(hostFindCustomer).then(resp => {
        console.log(resp.data);
    })
});