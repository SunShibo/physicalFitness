// 开启严格模式
'use strict';
app.controller('WhitePageCtrl', ['$scope', '$http', '$state',  '$window', '$timeout',
                                      function($scope, $http, $state, $window,$timeout) {
	
	$timeout(init_indexpage, 1000);
	function init_indexpage(){
		if($scope.loginUser != null ){
			//这里用来编写根据不同的用户显示不同的默认界面
//			$window.location.href = $window.location.href.substring(0, $window.location.href.indexOf('index_page'))+"index_page";
//			$window.location.reload();
		}
	}
  }]);