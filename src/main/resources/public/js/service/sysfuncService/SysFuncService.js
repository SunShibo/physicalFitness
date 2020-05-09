'use strict';
angular.module('app.sysFuncService',[])//serviceJs 需要在app.js加载之前加载
	.factory('sysFuncService', function($http){
		var allResults;
		return {
			getSysFuncPage: function(formData){
				var requestUrl = "ajax/funcmanager/query_func_page";
				$http({
					method  : 'POST',
					url     : requestUrl,
					data    : $.param(formData),  // pass in data as strings
					headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
				}).success(function(data, status, headers, config) {
					allResults = data;
				}).error(function(data, status, headers, config) {
				});
				return allResults;
			},
			getSysFuncLevel1: function(){
				var requestUrl="ajax/funcmanager/query_func_level1";
				$http({
					method  : 'POST',
					url     : requestUrl,
					headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
				}).success(function(data, status, headers, config) {
					allResults = data;//把值绑定给一级菜单列表
				}).error(function(data, status, headers, config) {
				});
				return allResults;
			},
			addSysFunc:function(func){
				var requestUrl = "ajax/funcmanager/add_func";
				var formData = {
						func : angular.toJson(func),
				};
				$http({
					method	: 'POST',
					url		: requestUrl,
					data	: $.param(formData),
					headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
				}).success(function(data, status, headers, config){
					allResults=true;
				}).error(function(data, status, headers, config){
					allResults=false;
				});
				return allResults;
			}
		};
		
	});