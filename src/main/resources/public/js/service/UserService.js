'use strict';
angular.module('app.userService',[])
	.factory('userService', function($http){
		var current_user;
		return {
			getCurrentUser: function(){
				if (current_user == undefined){
					// 请求地址
		        	var requestUrl = "/api/users";
		        	// 整理表单数据
//		        	var formData = {
//		        		beforeSomeoneDay : $scope.dayNum
//		            };
		        	// 重置错误信息
//		            $scope.errorMsg = null;
		            // 提交请求到服务器
		            $http({
		                method  : 'GET',
		                url     : requestUrl,
		                //data    : $.param(formData),  // pass in data as strings
		                headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
		            }).success(function(data, status, headers, config) {
		            	current_user = data;
		            }).error(function(data, status, headers, config) {
		            	// $scope.errorMsg = '服务器异常!';
		            });
				}    
				return current_user;
			},
			setCurrentUser: function(user){
				current_user = user;
			}
		};
		
	});