// 开启严格模式
'use strict';
app.controller('OutdefaultPageCtrl', ['$scope', '$http', '$state', '$sce', 'userService','$location', '$window', '$timeout',
                                      function($scope, $http, $state, $sce, userService,$location,$window,$timeout) {
	
//	$timeout(init_indexpage, 1000);
//	function init_indexpage(){
//		if ($scope.loginUser.groupId == 4){
//			$window.location.href = $window.location.href.substring(0, $window.location.href.indexOf('index_page'))+"index_page#/app/stuhomeindex";
//			$window.location.reload();
//		}else{
//			$state.go('app.sysdefaultpage'); 
//		}
//	}
	
	
	//	 
//	 $scope.initFirstMenuBgColor=function(menuName1){
//		 var aElementes = $('#side-menu').find('a');
//	       	for (var i = 0; i < aElementes.length; i++){
//	       		if ($.trim($(aElementes[i]).text()) == menuName1){
//	       			$(aElementes[i]).addClass("active");
//	       		}else{
//	       			$(aElementes[i]).removeClass("active");
//	       		}
//	       	}
//	 };
//	 $scope.initFirstMenuBgColor('');
  }]);