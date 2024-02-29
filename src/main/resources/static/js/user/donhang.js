const app = angular.module("bannoithatonline", []);


app.controller("chiTietDonHangController", function ($scope, $http){
    if(window.localStorage.getItem('orderId') != null){
        var orderID = JSON.parse(window.localStorage.getItem('orderId'));
        if(vnp_TransactionStatus == '00'){            
            $http.post('/rest/createTransaction', null, {params : { 
                orderID : orderID,
                Amount: vnp_Amount/100,
                Status : 0,
                Message : 'Thanh toán thành công',
                bank : vnp_BankCode                
            }} ).then((resp) => { console.log(resp);})
        } else {
            var orderID = JSON.parse(window.localStorage.getItem('orderId'));           
            $http.post('/rest/createTransaction', null, {params : { 
                orderID : orderID,
                Amount: vnp_Amount/100,
                Status : 1,
                Message : 'Chưa thanh toán',
                bank : vnp_BankCode
                
            }} ).then((resp) => { console.log(resp);})
        }
    }



    

    
});
