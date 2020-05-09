// 开启严格模式
'use strict';
app.controller('InstteacherChangePasswordCtrl', ['$scope', '$http', '$state','schoolYearService',
                                      function($scope, $http, $state ,schoolYearService) {

	$scope.saveObj = {oldPassword:null};
	
	$scope.vm = {
			selectTerm:{
				
			},
			terms: [],
			userList:[]
	};
	
	function setSchoolYear(schoolYearListParam){
		$scope.vm.terms=schoolYearListParam;
		$scope.vm.selectTerm=$scope.vm.terms[0];
	}
	schoolYearService.getAllSchoolYear(setSchoolYear);

	

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
		                    different: {
		                        field: 'oldPassword',
		                        message: '新密码不能和原密码相同'
		                    }
		                }
		            },
		            confirmPassword: {
		                validators: {
		                    notEmpty: {
		                        message: '确认密码不能为空'
		                    },
		                    identical: {
		                        field: 'password',
		                        message: '新密吗和确认密码不一致'
		                    }
		                }
		            },		           
		            captcha: {
		                validators: {
		                    callback: {
		                        message: 'Wrong answer',
		                        callback: function(value, validator) {
		                            var items = $('#captchaOperation').html().split(' '), sum = parseInt(items[0]) + parseInt(items[2]);
		                            return value == sum;
		                        }
		                    }
		                }
		            }
		        }
		    });

			// Validate the form manually
			$('#validateBtn').click(
					function() {
						$('#defaultForm').bootstrapValidator('validate');//
						if($scope.vm.selectTerm.id == null){
							$("#termDiv small").css("display","block");
							$("#termDiv").addClass("has-error");
							$("#termDiv").addClass("has-feedback");
						}else{
							$("#termDiv small").css("display","none");
							$("#termDiv").removeClass("has-error");
							$("#termDiv").addClass("has-success");
							if($("#defaultForm").data('bootstrapValidator').isValid()){
							}
						}
					});
			
	  };

  }]);