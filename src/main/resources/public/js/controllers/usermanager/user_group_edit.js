/**
 * 
 */
// 开启严格模式
'use strict';
app.controller('UserGroupEditPageCtrl', ['$scope', '$http', '$state', '$stateParams',
                                      function($scope, $http, $state, $stateParams) {

	 $scope.allFuncList=[];//所有权限
	 
	 $scope.groupId=null;//用户组id
	 $scope.groupName="";//用户组名称
	 
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
		 $scope.userFuncs="";
		 
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
						var requestUrl="/api/rolemanager/update_user_group";
						var formData={//提交的数据
								 roleIds : allIds,
								 groupId : $scope.groupId,
								 roleName : $scope.groupName
						 };
							$http({
								method  : 'POST',
							   	url     : requestUrl,
							   	data	: $.param(formData),
							   	headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
							}).success(function(data, status, headers, config) {
								if(data.result == "ok"){
									swal("修改成功!", "您修改的用户组信息成功", "success");
									$scope.goBack();
								}else{
									swal("修改失败!", "", "error");
								}
							}).error(function(data, status, headers, config) {
								swal("修改失败!", "", "error");
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
				 $scope.allFuncListReady=true;
			 }).error(function(data, status, headers, config) {
				 $scope.operaResult = data;
			 });
		 }
		 
		 function getEditObj(){//获取待编辑用户组
			 var requestUrl="/api/rolemanager/get_edit_group";
				var formData = {
						groupId :$stateParams.groupId//列表页传过来的用户组id
				};
				$http({
					method	: 'POST',
					url		: requestUrl,
					data	: $.param(formData),
					headers : { 'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
				}).success(function(data, status, headers, config){
					$scope.groupName=data.name;//用户组名称赋值
					$scope.groupId = data.id;//用户组id赋值
					$scope.userFuncs=JSON.stringify(data.funcs);
					var requestUrl="/api/rolemanager/query_all_sysfunc";
					 $http({
						 method  : 'POST',
						 url     : requestUrl,
						 headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
					 }).success(function(data, status, headers, config) {
						 $scope.allFuncList = data;//所有权限赋值
						 checkedUserFunc();
					 }).error(function(data, status, headers, config) {
						 $scope.operaResult = data;
					 });
				}).error(function(data, status, headers, config){
				});
		 }
		 
		 function checkedUserFunc(){
			 angular.forEach($scope.allFuncList, function(level1Func, key) {
					if($scope.userFuncs.indexOf(level1Func.id)>0){
						level1Func.checked=true;
					}
					angular.forEach(level1Func.level2List, function(level2Func, key) {
						if($scope.userFuncs.indexOf(level2Func.id)>0){
							level2Func.checked=true;
						}
					});
				});
		 }
		 
//		 querySysFuncByLevel();//进页面默认查询所有权限
		 getEditObj();//进页面默认获取编辑对象
		 $scope.goBack=function(){
			 $state.go("app.usergroup");
		 };
	 
  }]);