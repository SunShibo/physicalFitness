// 开启严格模式
'use strict';
app.controller('ReloginCtrl', ['$scope', '$http', '$state', '$stateParams', '$window', 'userService','$timeout',
                                      function($scope, $http, $state, $stateParams, $window, userService,$timeout) {
//	if($stateParams.from != ''){
//		$state.go($stateParams.from,{},{reload:true});
//	}
	
	swal("登录超时,请重新登录","即将跳转至登录页","warning");
	$timeout(function() {
//		$window.location.href = $window.location.href.substring(0, $window.location.href.indexOf('index_page'))+"index_page";
		$window.location.href = "http://kfsj.bjedu.cn/";
//		window.open(url);
		$window.location.open();
     }, 2000);
	
	     
  }]);