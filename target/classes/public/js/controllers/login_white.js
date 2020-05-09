// 开启严格模式
'use strict';
app.controller('LoginWhiteCtrl', ['$scope', '$http', '$state', '$sce', 'userService','$location', '$window', '$timeout','$stateParams',
                                      function($scope, $http, $state, $sce, userService,$location,$window,$timeout,$stateParams) {
	$scope.username=$stateParams.username;
	$scope.password=$stateParams.password;
  }]);