// 开启严格模式
'use strict';
app.controller('SysdefaultPageCtrl', ['$scope', '$http', '$state', '$sce', 'userService','$location', '$window', '$timeout',
                                      function($scope, $http, $state, $sce, userService,$location,$window,$timeout) {
	
	$scope.checkUserType =function(loginUser){
		if(loginUser !=null && loginUser.groupId != 2 && loginUser.groupId != 3 &&
				 loginUser.groupId != 7 && loginUser.groupId != 8 && loginUser.groupId != 9 && loginUser.groupId != 10 && loginUser.groupId != 13){
			return 0;
		}else{
			if(loginUser == null){
				$window.location.href = $window.location.href.substring(0, $window.location.href.indexOf('index_page'))+"index_page";
				$window.location.reload();
			}else{
				return loginUser.groupId;
			}
		}
	};


	
  }]);