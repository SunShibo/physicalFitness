/**
 * 
 */
// 开启严格模式
'use strict';
app.controller('UserGroupAddPageCtrl', ['$scope', '$http', '$state', '$sce',
                                      function($scope, $http, $state, $sce) {
	
	/*
	 * 二级权限树形
	 * */
	$scope.countryChanged = function(country) {//选择一级时自动处理二级
		    // 自动选中所有下级
		    angular.forEach(country.level2List, function(province,key) {
		      province.checked = country.checked;
		    });
		  };
	  $scope.provinceChanged = function(province, country) {//选择二级时自动处理一级
	    // 如果有任何一个子节点被选中，则让上级节点也选中
	    // 如果所有子节点不选中，则上级也不选中
		var flag=false;
	    angular.forEach(country.level2List,function(province1,key){
	    	if(province1.checked){
	    		flag=true;
	    		country.checked = true;
	    	}
	    });
	    if(!flag){
	    	country.checked = false;
	    }
	  };

	 $scope.allFuncList=[];//所有权限list
	 $scope.groupName="";
	 
	 $scope.validForm=function(){
		 $('#defaultForm').bootstrapValidator({
				message: 'This value is not valid',
				feedbackIcons: {
					valid: 'glyphicon glyphicon-ok',
					invalid: 'glyphicon',//glyphicon glyphicon-remove验证错误时输入框内需要显示红叉就加上
					validating: 'glyphicon glyphicon-refresh'
				},
				fields: {
					groupName: {
						group: '.col-lg-4',
						validators: {
							notEmpty: {message: '群组名称不能为空'},
							stringLength: {min: 1,max: 255,message: '参数名称必须大于1个字小于255个字'}
						}
					}
				}
			});
			$('#validateBtn').click(function() {//保存按钮的点击事件
				$('#defaultForm').bootstrapValidator('validate');//验证表单
				var flag = $("#defaultForm").data('bootstrapValidator').isValid();//获取整个表单项的验证结果
				if(flag){//验证通过
					var allIds="";
					if($scope.allFuncList.length>0){
						angular.forEach($scope.allFuncList, function(level1Func, key) {
							if(level1Func.checked){
								allIds+=level1Func.id+",";
							}
							if(level1Func.level2List.length>0){
								angular.forEach(level1Func.level2List, function(level2Func, key) {
									if(level2Func.checked){
										allIds+=level2Func.id+",";
									}
								});
							}
						});
					}
					var requestUrl="/api/rolemanager/add_user_group";
					var formData={//提交的数据
						roleIds : allIds,
						roleName : $scope.groupName
					};
					$http({
						method  : 'POST',
						url     : requestUrl,
						data	: $.param(formData),
						headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
					}).success(function(data, status, headers, config) {
						if(data.result == "ok"){
							swal("添加成功!", "您新增的用户组已经添加成功", "success");
							$scope.goBack();
						}else{
							swal("添加失败!", "", "error");
						}
					}).error(function(data, status, headers, config) {
						swal("添加失败!", "", "error");
						$scope.operaResult = data;
					});
				}else{
					var invalidArr= $("#defaultForm").data('bootstrapValidator').getInvalidFields();
					invalidArr.focus();
				}
			});
		  };
	 
	 function querySysFuncByLevel(){//获取所有权限的方法
		 var requestUrl="/api/rolemanager/query_all_sysfunc";
		 $http({
			 method  : 'POST',
			 url     : requestUrl,
			 headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		 }).success(function(data, status, headers, config) {
			 $scope.allFuncList = data;//所有权限赋值
		 }).error(function(data, status, headers, config) {
			 $scope.operaResult = data;
		 });
	 }
	 
	 querySysFuncByLevel();//进页面默认查询所有权限
	 $scope.goBack=function(){
		 $state.go("app.usergroup");
	 };
	 
  }]);