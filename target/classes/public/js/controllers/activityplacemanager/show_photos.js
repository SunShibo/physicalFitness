// 开启严格模式
'use strict';
app.controller('ShowPhotosModalPageCtrl', ['$scope', '$http', '$state','$uibModalInstance',//这里引入学期Service可以调用他的方法
                                      function($scope, $http, $state,$uibModalInstance) {

	 
		$scope.close = function(){
			$uibModalInstance.close();
		};
  }]);