// 开启严格模式
'use strict';
app.controller('ParamterEditPageCtrl', ['$scope', '$http', '$state','$stateParams',
                                      function($scope, $http, $state,$stateParams) {
	
	$scope.paraObj = {//声明参数对象
		pId:null,
		paraName:null,
		pCode:null,
		pValue:null,
		term:null
	};

	function getEditObj(){//根据传过来的id获取参数对象值
		var requestUrl="/api/parameters/get_edit_parameter";
		var formData = {
				parametersId :$stateParams.paramterId
		};
		$http({
			method	: 'POST',
			url		: requestUrl,
			data	: $.param(formData),
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config){
			$scope.paraObj.pId = data.parameter_id;
			$scope.paraObj.paraName = data.parameter_name;
			$scope.paraObj.pCode = data.parameter_code;
			$scope.paraObj.pValue = data.parameters;
			$scope.paraObj.term = data.term;
		}).error(function(data, status, headers, config){
		});
	}
	
	$scope.validForm=function(){
		$('#defaultForm').bootstrapValidator({
			message: 'This value is not valid',
			feedbackIcons: {
				valid: 'glyphicon glyphicon-ok',
				invalid: 'glyphicon',//glyphicon glyphicon-remove验证错误时输入框内需要显示红叉就加上
				validating: 'glyphicon glyphicon-refresh'
			},
			fields: {
				paramName: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '参数名称不能为空'},
						stringLength: {min: 1,max: 255,message: '参数名称必须大于1个字小于255个字'}
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
			$('#defaultForm').bootstrapValidator('validate');
			var flag = $("#defaultForm").data('bootstrapValidator').isValid();//获取整个表单项的验证结果
			if(flag){//验证通过
				var requestUrl="/api/parameters/parameters_update";
				var formData = {
						parametersId :$scope.paraObj.pId,
						parametersName : $scope.paraObj.paraName,
						parameters:$scope.paraObj.pValue
				};
				$http({
					method	: 'POST',
					url		: requestUrl,
					data	: $.param(formData),
					headers : { 'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
				}).success(function(data, status, headers, config){
					swal("编辑成功!", "", "success");
					goback();
				}).error(function(data, status, headers, config){
					swal("编辑失败!", "", "error");
				});
			}
		});
	  };
	
	function goback(){
		$state.go("app.paramterlist");
	}
	
	getEditObj();
  }]);