var app = angular.module('bannoithatonline', []);

app.controller('ProfileController', function ($scope, $http, $window) {

    $http.get("/rest/profile/customer")
        .then(function (response) {
            $scope.customer = response.data;
            // $scope.customerId = $scope.customer.customerId;
            // console.log($scope.customerId);
        });

    // $http.get("/rest/customers/" + $scope.customerId + "/addresses")
    // .then(function(response) {
    //     $scope.addresses = response.data;
    // });

    function getCustomerId() {
        return $http.get("/rest/profile/customer").then(function (response) {
            return response.data.customerId;
        });
    }

    getCustomerId().then(function (customerId) {
        $scope.customerId = customerId;

        fetchProfiles(customerId);
    });

    function fetchProfiles(customerId) {
        $http.get("/rest/customers/" + customerId)
            .then(function (response) {
                $scope.profiles = response.data;
            });
    }

    $scope.updateProfile = function() {
        var newAddress = {
            name: document.getElementById('updatename').value,
            phone:document.getElementById('updatephone').value
        };
        $http.put("/rest/customers/" + $scope.customerId,newAddress)
        .then(function(response) {
            document.getElementById('updateProfileForm').style.display = 'none';
            window.location.reload();
            fetchProfiles($scope.customerId);
        });
    }
    $scope.editProfile = function() {
            document.getElementById('updateProfileForm').style.display = 'inline';
    }
    
    
    
    $scope.updateProfile = function () {
        var name = $("#name").val();
        var email = $("#email").val();
        var phone = $("#phone").val();
        var avatarValue = $("#image").val();
        var isError = false;
    
        if (name == "" || name.length < 3) {
          $("#nameError").text("Vui lòng nhập họ tên và tên phải lớn hơn 3 kí tự");
          isError = true;
        } else if (
          !/^[a-zA-Z\sàáạãảăắằẳẵặâấầẩẫậèéẹẽẻêếềểễệđìíịĩỉòóọõỏôốồổỗộơớờởỡợùúụũủưứừửữựỳýỵỹỷÀÁẠÃẢĂẮẰẲẴẶÂẤẦẨẪẬÈÉẸẼẺÊẾỀỂỄỆĐÌÍỊĨỈÒÓỌÕỎÔỐỒỔỖỘƠỚỜỞỠỢÙÚỤŨỦƯỨỪỬỮỰỲÝỴỸỶ']*$/u.test(
            name
          )
        ) {
          $("#nameError").text(
            "Họ và tên không được chứa số hoặc ký tự đặc biệt"
          );
          isError = true;
        } else{
          $("#nameError").text("");
        }

        if (email == "") {
            $("#emailError").text("Vui lòng nhập Email");
            isError = true;
          } else if (
            !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
          ) {
            $("#emailError").text(
              "Email sai định dạng"
            );
            isError = true;
          } else{
            $("#emailError").text("");
          }
          if (phone == "" ) {
            $("#phoneError").text("Vui lòng nhập số điện thoại");
            isError = true;
          } else if (
            !/^0\d{9}$/.test(
                phone
            )
          ) {
            $("#phoneError").text(
              "Số điện thoại bắt buộc phải 10 chữ số và bắt đầu bằng số 0"
            );
            isError = true;
          } else {
            $("#phoneError").text("");
          }

        if (avatarValue != "") {
          console.log("đã chọn ảnh");
    
          const imageInput = document.getElementById("image");
    
          const file = imageInput.files[0];
          console.log(file);
          const formData = new FormData();
          formData.append("avatar", file);
          const request = $http({
            method: "PUT",
            url: "/rest/customer/update-avatar/" + $scope.customer.customerId,
            headers: {
              transformRequest: angular.identity,
              "Content-Type": undefined,
            },
            data: formData,
          });
          request
            .then((response) => {
              console.log(response.data);
              $scope.customer.image = response.data.avatarName;
            })
            .catch((error) => {
              console.log(error);
            });
        }
            $scope.customer.account.email = email;
            $scope.customer.name = name;
            $scope.customer.phone = phone;
        console.log(isError);
        if (!isError) {

            $http({
                method: 'PUT',
                url: '/rest/customer/' + $scope.customer.customerId,
                data: $scope.customer
            })
            .then(function (response) {
              Swal.fire({
                icon: "success",
                title: "Cập nhật thành công",
                text: "Thông tin cá nhân đã được cập nhật !",
                showConfirmButton: true,
            }).then((result) => {
                if (result.isConfirmed) {
                    setTimeout(function() {
                        location.reload(); // Load lại trang
                    }, 1000);
                }
            });
            })
            .catch(function (error) {
            Swal.fire({
                      icon: "warning",
                      title: "Cập nhật thất bại",
                      text: "Vui lòng thử lại !",
                      showConfirmButton: true,
                  });
            });
            
        }else{
          Swal.fire({
            icon: "warning",
            title: "Cập nhật thất bại",
            text: "Vui lòng thử lại !",
            showConfirmButton: true,
        });
        }
      };
    
//       // Bắt sự kiện khi người dùng chọn file ảnh
//       $("#image").change(function() {
//       // Lấy thông tin về file được chọn
//       var file = this.files[0];
      
//       // Kiểm tra nếu file tồn tại
//       if (file) {
//           // Lấy dung lượng của file (đơn vị bytes)
//           var fileSize = file.size;

//           // Chuyển đổi dung lượng từ bytes sang kilobytes
//           var fileSizeInKB = fileSize / 1024;

//           // Kiểm tra nếu dung lượng vượt quá 2MB
//           if (fileSizeInKB > 1024) {
//               // Hiển thị thông báo
//               Swal.fire({
//                 icon: "warning",
//                 title: "Dung lượng vượt quá 1MB",
//                 text: "Vui lòng thử lại !",
//                 showConfirmButton: true,
//               });
//           } else {
              
//           }
//       }
// });

});