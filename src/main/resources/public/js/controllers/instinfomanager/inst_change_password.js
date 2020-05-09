'use strict';// 开启严格模式
app.controller('InstInfoChangePasswordCtrl', ['$scope', '$http', '$state',
                                      function($scope, $http, $state ) {

	$scope.passwordObj = {
		oldPassword:null,
		password:null,
		confirmPassword:null
	};
	
	$scope.validForm=function(){		 
		 $('#defaultForm').bootstrapValidator({
		        message: 'This value is not valid',
		        feedbackIcons: {
		            valid: 'glyphicon glyphicon-ok',
		            invalid: 'glyphicon',//glyphicon glyphicon-remove验证错误时需要显示红叉就加上
		            validating: 'glyphicon glyphicon-refresh'
		        },
		        fields: {
		            oldPassword: {
		                validators: {
		                    notEmpty: {
		                        message: '密码不能为空'
		                    },
// 		                    regexp: {
// //		                        regexp: /^(?![0-9]+$)(?![a-zA-Z]+$)(?!([a-z0-9_\W])+$)(?![0-9A-Za-z]+$)(\S){6,20}$/,
// //		                        message: '密码由6至20位字母、数字、特殊字符随机组成，其中必须有一个大写字母和一个特殊字符'
// 		                        regexp: /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$/,
// 		                        message: '密码由6至20位字母、数字随机组成，其中必须有一个字母和一个数字'
// 		                    },
		                    different: {
		                        field: 'password', message: '新密码不能和原密码相同'
		                    }
		                }
		            },
		            password: {
		                validators: {
		                    notEmpty: {
		                        message: '新密码不能为空'
		                    },
		                    stringLength: {
		                        min: 6,
		                        max: 20,
		                        message: '新密码不能少于6位,不能大于20位'
		                    },
		                    regexp: {
//		                        regexp: /^(?![0-9]+$)(?![a-zA-Z]+$)(?!([a-z0-9_\W])+$)(?![0-9A-Za-z]+$)(\S){6,20}$/,
//		                        message: '密码由6至20位字母、数字、特殊字符随机组成，其中必须有一个大写字母和一个特殊字符'
		                        regexp: /^(?=.*[0-9])(?=.*[a-zA-Z])[0-9A-Za-z~!@#$%^&*]{6,20}$/,
		                        message: '密码由6至20位字母、数字随机组成，其中必须有一个字母和一个数字'
		                    },
		                    different: {
		                        field: 'oldPassword', message: '新密码不能和原密码相同'
		                    },
		                	identical: {field: 'confirmPassword',message: '两次密码不一致'}
		                }
		            },
		            confirmPassword: {
		                validators: {
		                	identical: {field: 'password',message: '两次密码不一致'}
		                }
		            }
		        }
		    });

			// 提交按钮点击事件
			$('#validateBtn').click(function() {
				$('#defaultForm').bootstrapValidator('validate');//验证表单
				var flag = $("#defaultForm").data('bootstrapValidator').isValid();//获取整个表单项的验证结果
				if(flag){//验证通过
					var requestUrl = "/api/usermanager/user_password";
					var formData = {
							oldPassword:$scope.passwordObj.oldPassword,
							password:$scope.passwordObj.password,
							confirmPassword:$scope.passwordObj.confirmPassword
						};
					$http({
						method	: 'POST',
						url		: requestUrl,
						data	: $.param(formData),
						headers : { 'Content-Type': 'application/x-www-form-urlencoded'}  // 设置HTTP的类型和规则
					}).success(function(data, status, headers, config){
						if(data.result == "ok"){
							swal("修改成功！", "您修改密码成功！", "success");
						}else if(data.result == "passwordfail"){
							swal("修改失败！", "旧密码错误！", "error");
						}else{
							swal("修改失败！", "您修改密码失败！", "error");
						}
					}).error(function(data, status, headers, config){
						swal("修改密码失败！", "", "error");
					});
				}
			});
	  };
  }]);