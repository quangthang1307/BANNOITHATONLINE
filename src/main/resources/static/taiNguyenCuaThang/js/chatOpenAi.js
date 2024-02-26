var app = angular.module('bannoithatonline', []);
app.controller("openAiCtrl", function ($scope, $http) {

    $scope.callAPI = function () {
        $http.post('http://localhost:8080/createThread', {})
            .then(function (response) {
                console.log(response);
                $scope.threadId = response.data;
            })
            .catch(function (error) {
                console.log("Lỗi khi gọi API: ", error);
            });
    };

    $scope.callAPI();

    $(function () {
        var INDEX = 0;

        $("#chat-submit").click(function (e) {
            e.preventDefault();
            var msg = $("#chat-input").val();
            if (msg.trim() == '') {
                return false;
            }

            generate_message(msg, 'self');
            $scope.showFeedback(msg);
        });

        function generate_message(msg, type) {
            INDEX++;
            var str = "";
            str += "<div id='cm-msg-" + INDEX + "' class=\"chat-msg " + type + "\">";
            str += "          <span class=\"msg-avatar\">";
            // str += "            <img src=\"https:\/\/image.crisp.im\/avatar\/operator\/196af8cc-f6ad-4ef7-afd1-c45d5231387c\/240\/?1483361727745\">";
            str += "          <\/span>";
            str += "          <div class=\"cm-msg-text\">";
            str += msg;
            str += "          <\/div>";
            str += "        <\/div>";
            $(".chat-logs").append(str);
            $("#cm-msg-" + INDEX).hide().fadeIn(300);
            if (type == 'self') {
                $("#chat-input").val('');
            }
            $(".chat-logs").stop().animate({ scrollTop: $(".chat-logs")[0].scrollHeight }, 1000);
        }

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
                    $scope.runThread();
                })
                .catch(function (error) {
                    console.log("Lỗi khi gọi API: ", error);
                });
        };

        $scope.runThread = function () {
            $http.post('http://localhost:8080/runThread').then(function (response) {
                console.log(response);
                $scope.runThreadId = response.data.runThreadId;
                $scope.checkRunThreadId();
            });
        };

        $scope.checkRunThreadId = function () {
            $http.get('http://localhost:8080/checkRunThreadId').then(function (response) {
                $scope.runThreadStatus = response.data;

                if (response.data && response.data.status === "completed") {
                    $scope.showFeedback();
                } else {

                    setTimeout(function () {
                        $scope.checkRunThreadId();
                    }, 2500);
                }
            });
        };

        $scope.showFeedback = function () {
            $http.get('http://localhost:8080/showFeedback').then(function (response) {
                $scope.feedbackData = response.data;


                var messages = $scope.feedbackData.data;

                var assistantMessage = messages.find(function (message) {
                    return message.role === "assistant";
                });

                if (assistantMessage) {
                    var content = assistantMessage.content[0].text.value;
                    generate_message(content, 'user');
                }
            });
        };
        function generate_button_message(msg, buttons) {
            /* Buttons should be object array 
              [
                {
                  name: 'Existing User',
                  value: 'existing'
                },
                {
                  name: 'New User',
                  value: 'new'
                }
              ]
            */
            INDEX++;
            var btn_obj = buttons.map(function (button) {
                return "              <li class=\"button\"><a href=\"javascript:;\" class=\"btn btn-primary chat-btn\" chat-value=\"" + button.value + "\">" + button.name + "<\/a><\/li>";
            }).join('');
            var str = "";
            str += "<div id='cm-msg-" + INDEX + "' class=\"chat-msg user\">";
            str += "          <span class=\"msg-avatar\">";
            str += "            <img src=\"https:\/\/image.crisp.im\/avatar\/operator\/196af8cc-f6ad-4ef7-afd1-c45d5231387c\/240\/?1483361727745\">";
            str += "          <\/span>";
            str += "          <div class=\"cm-msg-text\">";
            str += msg;
            str += "          <\/div>";
            str += "          <div class=\"cm-msg-button\">";
            str += "            <ul>";
            str += btn_obj;
            str += "            <\/ul>";
            str += "          <\/div>";
            str += "        <\/div>";
            $(".chat-logs").append(str);
            $("#cm-msg-" + INDEX).hide().fadeIn(300);
            $(".chat-logs").stop().animate({ scrollTop: $(".chat-logs")[0].scrollHeight }, 1000);
            $("#chat-input").attr("disabled", true);
        }

        $(document).delegate(".chat-btn", "click", function () {
            var value = $(this).attr("chat-value");
            var name = $(this).html();
            $("#chat-input").attr("disabled", false);
            generate_message(name, 'self');
        })

        $("#chat-circle").click(function () {
            $("#chat-circle").toggle('scale');
            $(".chat-box").toggle('scale');
        })

        $(".chat-box-toggle").click(function () {
            $("#chat-circle").toggle('scale');
            $(".chat-box").toggle('scale');
        })

    })
});