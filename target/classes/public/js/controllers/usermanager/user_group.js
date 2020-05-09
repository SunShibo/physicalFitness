//开启严格模式
'use strict';
app.controller('UserGroupPageCtrl', ['$scope', '$http', '$state', '$sce', '$cookieStore','myCommonService',
                                      function($scope, $http, $state, $sce,$cookieStore,myCommonService) {
	 
	 $scope.itemNames = [{name: "用户组ID"},{name: "用户组名称"},{name: "操作"}];//列表表头
	 
	 $scope.userList = [];//列表数据
	 
	 $scope.repeatDone = function(){//列表数据加载完成后加载操作栏
	 		$('[data-toggle="tooltip"]').tooltip();
	 	};
	 
	 		
		//角色管理
	 $scope.queryRolePage = function(pageable){//请求列表数据方法
//	    	var formData = {//参数
//	    			page:pageable
//	    	};
//	    	var cookieObj={"page":pageable};//用于放到cookie的查询条件对象
		var requestUrl = "/api/rolemanager/role_list";
		  // 提交请求到服务器
			$http({
				method  : 'POST',
			   	url     : requestUrl,
//			   	data    : $.param(formData),  // pass in data as strings
			   	headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
//				myCommonService.setMyCookie($cookieStore,"usergroup",cookieObj);//把页面查询条件对象放到cookie
				$scope.userList = data;//列表数据绑定
			}).error(function(data, status, headers, config) {
				// $scope.errorMsg = '服务器异常!';
				$scope.operaResult = data;
			});
		};
		
	
	/*
	 * 跳转到添加用户组
	 * */	
	$scope.addGroup = function(){
		 $state.go("app.usergroupadd");
	 };	
		
	
	/*
	 * 编辑用户组
	 * */
	$scope.editGroup =function(groupId){
		
		 $state.go("app.usergroupedit",{"groupId":groupId});

	};
	
	/*
	 * 删除用户组
	 * groupId 用户组的id
	 * index 该数组的下标
	 * */
	$scope.deleteGroup = function(groupId){
		swal({
			title : "您是否确认要删除该用户组？",
			text : "删除后将无法恢复!",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : false,
			customClass : ""
		}, function() {
			$(".confirm").attr("disabled",true);
			var formData = {
					groupId:groupId
	    	};
			var requestUrl="/api/rolemanager/user_group_del";
			$http({
				method  : 'POST',
				url     : requestUrl,
				data    : $.param(formData),  // pass in data as strings
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				if(data.result=="ok"){
					swal("删除成功!", "您选择的用户组已经删除.", "success");
					initPage();
				}else{
					swal("删除失败!", "", "error");
				}
			}).error(function(data, status, headers, config) {
			});	
		});
	};
	
	function initPage(){
//		var cookieParam=myCommonService.getMyCookie($cookieStore,"usergroup");//从cookie里获取当前页面查询条件对象
//		if(cookieParam!=null && cookieParam.page !=null){//用cookie中的页码去查询
			$scope.queryRolePage();
//		}else{
//			$scope.queryRolePage();
//		}
	}
	initPage();

  }]);