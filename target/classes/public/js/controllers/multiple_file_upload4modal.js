'use strict';
app.controller('MultiFileUpload4modalCtrl', ['$scope', '$http', '$state', '$uibModal',
                                      function($scope, $http, $state, $uibModal) {
	 $scope.oneObj = {
			 aboutme: '点击我，以模态窗口形式上传多个文件！'
	 };
	 
	 $scope.openMultiFileUploadWindow = function(){
		 
		 var modalInstance = $uibModal.open({
				templateUrl: "multifileuploadwindow",
				controller: 'UploadMultiPicWindowCtrl',
				scope: $scope,
				size:"md",
				keyboard: false
			});
			modalInstance.result.then(function (result) {
				//一般情况下不作处理
			});
	 }
  }]);