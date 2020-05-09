// 开启严格模式
'use strict';
app.controller('IpAddressCtrl', ['$scope', '$http', '$state',
                                      function($scope, $http, $state) {
	$scope.queryResult={
		ip:null	
	};
	
	function getIp(){
//		alert("getIp");
		var requestUrl = "/api/common/ip_address";
    	$http({
			method  : 'POST',
			url     : requestUrl,
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			$scope.queryResult.ip=data.ip;
		}).error(function(data, status, headers, config) {
		});
	}
	
	getIp();
	
  }]);