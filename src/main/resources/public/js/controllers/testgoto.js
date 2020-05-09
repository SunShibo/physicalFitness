'use strict';
app.controller('TestGotoPageCtrl', ['$scope', '$http', '$state',
                                      function($scope, $http, $state) {
	 $scope.oneObj = {
		  test12: '我是测试跳转到的页面！'
	 };
  }]);