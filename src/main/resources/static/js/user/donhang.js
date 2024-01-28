const app = angular.module("bannoithatonline", []);
const host = "http://localhost:8080";

app.controller("chiTietDonHangController", function ($scope, $http){

    if(window.localStorage.getItem('orderId') != null){
        var orderID = JSON.parse(window.localStorage.getItem('orderId'));
        if(vnp_TransactionStatus == '00'){
            var urlCreateTransaction = `${host}/rest/createTransaction`;
            $http.post(urlCreateTransaction, null, {params : { 
                orderID : orderID,
                Amount: vnp_Amount/100,
                Status : 0,
                Message : 'Thanh toán thành công',
                bank : vnp_BankCode
                
            }} ).then((resp) => { console.log(resp);})
        } else {
            var urlCreateTransaction = `${host}/rest/createTransaction`;
            $http.post(urlCreateTransaction, null, {params : { 
                orderID : orderID,
                Amount: vnp_Amount/100,
                Status : 1,
                Message : 'Chưa thanh toán',
                bank : vnp_BankCode
                
            }} ).then((resp) => { console.log(resp);})
        }
    }



    

    
});
