// 开启严格模式
'use strict';
app.controller('ParamterAddPageCtrl', ['$scope', '$http', '$state','$sce','schoolYearService','myCommonService',
                                      function($scope, $http, $state ,$sce ,schoolYearService,myCommonService) {

	$scope.obj = {name:'ckl'};
	
	$scope.terms=[];//学期list
	
	$scope.termObj={
		selectTerm:{}
	};//选择的学期
	
	$scope.myParamObj={//参数对象
		paramCode:null,//参数code
		paramName:null,//参数名称
		paramValue:null,//参数值
		paramCodeHas:false//参数是否存在
	};
	
	function setSchoolYear(schoolYearListParam){
		$scope.terms=schoolYearListParam;
		$scope.termObj.selectTerm=$scope.terms[0];
	}
	schoolYearService.getAllSchoolYear(setSchoolYear);
	
	$scope.validForm=function(){
		$('#defaultForm').bootstrapValidator({
			message: 'This value is not valid',
			feedbackIcons: {
				valid: 'glyphicon glyphicon-ok',
				invalid: 'glyphicon',//glyphicon glyphicon-remove验证错误时输入框内需要显示红叉就加上
				validating: 'glyphicon glyphicon-refresh'
			},
			fields: {
				termId: {
					group: '.col-lg-4',
					validators: {notEmpty: {message: '学期必选'}}
				},
				paramName: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '参数名称不能为空'},
						stringLength: {min: 1,max: 255,message: '参数名称必须大于1个字小于255个字'}
					}
				},
				paramCode: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '简称不能为空'},
//						regexp: {regexp: /^(?!^[L][0-9]{1,}$)(?!^\d+$)(^[A-Za-z0-9]+$)$/,message: '简称只能包含数字,字母'},
						stringLength: {min: 1,max: 50,message: '简称必须大于1个字小于50个字'},
						remote: {
								type: 'POST',url: '/api/parameters/check_parameter_code',delay :  1000,//延迟2秒的值写到公共服务里
		                        data:{"schoolYearId":function(){return $scope.termObj.selectTerm.id; }},message: '参数简称已经存在'
		                }
					}
				},
				paramValue: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '参数不能为空'},
						stringLength: {min: 1,max: 100,message: '参数必须大于1个字小于100个字'}
					}
				}
			}
		});
		$('#validateBtn').click(function() {//保存按钮的点击事件
			$('#defaultForm').bootstrapValidator('validate');//验证表单
			if($scope.termObj.selectTerm.id == null){//验证uiselect
				$("#termDiv small").css("display","block");//错误时改变样式并显示
				$("#termDiv").addClass("has-error");
				$("#termDiv").addClass("has-feedback");
			}else{
				$("#termDiv small").css("display","none");//正确时隐藏样式
				$("#termDiv").removeClass("has-error");
				$("#termDiv").addClass("has-success");
				var flag = $("#defaultForm").data('bootstrapValidator').isValid();//获取整个表单项的验证结果
				if(flag){//验证通过
					var requestUrl = "/api/parameters/parameters_add";
					var parameters = {
							semester :$scope.termObj.selectTerm.id,
							parameterCode : $scope.myParamObj.paramCode,
							parameterName : $scope.myParamObj.paramName,
							parameters : $scope.myParamObj.paramValue
						};
					var formData = {
							parameters : angular.toJson(parameters),//处理为JSON串后后台用String接收
						};
					$http({
						method	: 'POST',
						url		: requestUrl,
						data	: $.param(formData),
						headers : { 'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
					}).success(function(data, status, headers, config){
						swal("保存成功!", "您新建的参数已经添加成功", "success");
						$scope.goBack();
					}).error(function(data, status, headers, config){
						swal("添加失败!", "", "error");
					});
				}
			}
		});
	  };
	  
	$scope.checkMyForm = function(){//此方法用于uiselect点击事件时验证结果并改变样式
		if($scope.termObj.selectTerm.id == null){
			$("#termDiv small").css("display","block");
			$("#termDiv").addClass("has-error");
			$("#termDiv").addClass("has-feedback");
		}else{
			$("#termDiv small").css("display","none");
			$("#termDiv").removeClass("has-error");
			$("#termDiv").addClass("has-success");
		}
	};
	
	$scope.goBack = function(){//跳转回参数列表页
		$state.go("app.paramterlist");
	};

  }]);