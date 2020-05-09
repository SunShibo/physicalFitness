// 开启严格模式
'use strict';
app.controller('ClearRedisPageCtrl', ['$scope', '$http', '$state', 
                                      function($scope, $http, $state) {

	$scope.queryObj={
			key:""
	};
	
	$scope.clearRedis=function(){
		var formData = {//参数
				key:$scope.queryObj.key
	    };
		var requestUrl="/api/parameters/clear_redis";
		$http({
			method  : 'POST',
			url     : requestUrl,
			data    : $.param(formData),  // pass in data as strings
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			if(data.result=="ok"){
				swal("清除成功!", "您指定的缓存已经清除.", "success");
			}else{
				swal("清除失败!", "", "error");
			}
		}).error(function(data, status, headers, config) {
		});	
	};
	
  }]);