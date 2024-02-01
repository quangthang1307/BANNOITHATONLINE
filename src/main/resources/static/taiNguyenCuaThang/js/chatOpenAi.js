var app = angular.module('bannoithatonline', []);
app.controller('openAiCtrl', function($scope, $http){

    $scope.callAPI = function(){
        $http.post('http://localhost:8080/createThread', {})
        .then(function(response){
            console.log(response);
            $scope.threadId = response.data;
        })
        .catch(function(error){
            console.log("Lỗi khi gọi API: ", error);
        });
    };

    $scope.sendMessage = function () {
        if (!$scope.threadId) {
            alert("Chưa có Thread ID");
            return;
        }

        var jsonBody = {
            role: 'user',
            content: $scope.userMessage,
            file_ids: "file-PQeqheySy2be7PYRIuVVgQNQ"
        };

        $http.post('http://localhost:8080/sendMessageApi', jsonBody)
            .then(function (response) {
                console.log(response);
                $scope.responseMessage = "Server Response: " + response.data;
            });
    };

    $scope.runThread = function() {
        $http.post('http://localhost:8080/runThread').then(function(response) {
            console.log(response);
            $scope.runThreadId = response.data.runThreadId;
        });
    };

    $scope.checkRunThreadId = function() {
        $http.get('http://localhost:8080/checkRunThreadId').then(function(response) {
            $scope.runThreadStatus = response.data;
        });
    };

    $scope.showFeedback = function() {
        $http.get('http://localhost:8080/showFeedback').then(function(response) {
            $scope.feedbackData = response.data;
        });
    };
});