// 开启严格模式
'use strict';
app.controller('ShowWordCtrl', ['$scope', '$http', '$state','$cookieStore', 'schoolYearService','myCommonService','$stateParams','$uibModalInstance',//这里引入学期Service可以调用他的方法
                                      function($scope, $http, $state, $cookieStore,schoolYearService, myCommonService,$stateParams,$uibModalInstance) {
	
//	$scope.queryObj = {
//	};
	
	$scope.close = function(){
		$uibModalInstance.close();
	};
	
	/*
	 * 列表
	 * */
	$scope.queryRecord = function(){//获取列表数据的方法
    	var formData = {//声明用于传到后台的参数
    			keywords:$scope.queryObj.keyword,
    			gradeId:$scope.queryObj.gradeId,
    			classname:$scope.queryObj.classname,
    			starttime:$scope.queryObj.starttime,
    			endtime:$scope.queryObj.endtime
    	};
    	var requestUrl = "/api/userhour/showword";
    	
    	$http({
			method  : 'POST',
			url     : requestUrl,
			data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			debugger;
			//myCommonService.setMyCookie($cookieStore,"coursebytownlist",cookieObj);//把页面查询条件对象放到cookie
			$scope.result= data;//列表数据赋值
			$("#showWord").html(data['result']);
		}).error(function(data, status, headers, config) {
		});		 
	};
	
	$scope.queryRecord();
	
	
  }]);