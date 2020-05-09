// 开启严格模式
'use strict';
app.controller('UserPageCtrl', [ '$scope', '$http', '$state', '$sce','$cookieStore','myCommonService',
		function($scope, $http, $state, $sce,$cookieStore,myCommonService) {

			$scope.itemNames = [ {name : "用户ID"}, {name : "用户名(显示名)"}, {name : "用户组"}, {
				name : "区县"}, {name : "学校"}, {name : "操作"} ];

			$scope.userList = [];

			$scope.repeatDone = function() {
				$('[data-toggle="tooltip"]').tooltip();
			};

			$scope.queryObj = {
				name : null
			};

			// 角色管理
			$scope.queryUsers = function(pageable) {
				var formData = {
					page : pageable,
					searchName : $scope.queryObj.name
				};
				var cookieObj={"page":pageable,"keyword":$scope.queryObj.name};//用于放到cookie的查询条件对象
				var requestUrl = "/api/usermanager/user_list";
				// 提交请求到服务器
				$http({
					method : 'POST',
					url : requestUrl,
					data : $.param(formData), // pass in data as strings
					headers : {
						'Content-Type' : 'application/x-www-form-urlencoded'
					}
				}).success(function(data, status, headers, config) {
					myCommonService.setMyCookie($cookieStore,"userlist",cookieObj);//把页面查询条件对象放到cookie
					$scope.page=data;
					$scope.userList=data.content;
				}).error(function(data, status, headers, config) {
					// $scope.errorMsg = '服务器异常!';
				});
			};
			// 点击创建角色
			$scope.goAddUser = function() {
				$state.go("app.useradd");
			};

			/*
			 * 编辑用户
			 */
			$scope.editUser = function(userId) {
				$state.go("app.useredit",{"userId":userId});
			};

			/*
			 * 删除用户
			 */
			$scope.deleteUser = function(userId,userName) {
				swal({
					title : "您是否确认要删除该用户？",
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
							userId:userId,
							userName:userName
			    	};
					var requestUrl="/api/usermanager/user_del";
					$http({
						method  : 'POST',
						url     : requestUrl,
						data    : $.param(formData),  // pass in data as strings
						headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
					}).success(function(data, status, headers, config) {
						if(data.result=="ok"){
							swal("删除成功!", "您选择的用户已经删除.", "success");
							initPage();
						}else{
							swal("删除失败!", "", "error");
						}
					}).error(function(data, status, headers, config) {
					});	
				});
			};

			function initPage(){
				var cookieParam=myCommonService.getMyCookie($cookieStore,"userlist");//从cookie里获取当前页面查询条件对象
				if(cookieParam!=null && cookieParam.keyword !=null){//cookie中有值就赋给查询对象
					$scope.queryObj.name = cookieParam.keyword;
				}else{
					$scope.queryObj.name = null;
				}
				if(cookieParam!=null && cookieParam.page !=null){//用cookie中的页码去查询
					$scope.queryUsers(cookieParam.page);
				}else{
					$scope.queryUsers();
				}
			}
			
			initPage();
			
			$scope.clearCookie=function(){//清除搜索条件
				myCommonService.clearMyCookie($cookieStore,"userlist");
				initPage();
			};

		} ]);