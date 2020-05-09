'use strict';
app.controller('TeacherFileUploadCtrl', ['$scope', 'FileUploader', function ($scope, FileUploader) {

	$scope.selectPic = false;
	//选择图片
	$scope.to_select_pic = function () {
		$scope.selectPic = true;
	};
	//取消选择图片
	$scope.cancel_select_pic = function () {
		$scope.selectPic = false;
	};
	$scope.fileUploadResult = {
		fileUri: "",
		uploadFeedback: ""
	};

	var uploader = $scope.uploader = new FileUploader({
		url: '/api/inst/teacher_batch_upload_file',
		autoUpload: true
	});
	/*$scope.checkUploadFileType=function(extName){
		//XLSX
	}*/
	// FILTERS

	uploader.filters.push({
		name: 'customFilter',
		fn: function (item /*{File|FileLikeObject}*/, options) {
			return this.queue.length < 1;
		}
	});
	uploader.filters.push({
		name: 'fileTypeFilter',
		fn: function (item /*{File|FileLikeObject}*/, options) {
			//获得上传文件的扩展名
			var position = item.name.lastIndexOf(".");
			var extName = item.name.substr(position + 1);
			try {
				//调用父控制器的上传文件类型控制方法
				//返回对象：如果返回空，则表示允许上传，否则返回允许上传的文件类型范围
				var uploadFileTypeCheckResult = $scope.checkUploadFileType(extName);
				if (uploadFileTypeCheckResult != "") {
					return false;
				}
			}
			catch (e) {
				//没有定义控制文件类型的方法就会出异常，不用处理
			}
			return true;
		}
	});

	// CALLBACKS

	uploader.onWhenAddingFileFailed = function (item /*{File|FileLikeObject}*/, filter, options) {
		console.info('onWhenAddingFileFailed', item, filter, options);
	};
	uploader.onAfterAddingFile = function (fileItem) {
		console.info('onAfterAddingFile', fileItem);
	};
	uploader.onAfterAddingAll = function (addedFileItems) {
		console.info('onAfterAddingAll', addedFileItems);
	};
	uploader.onBeforeUploadItem = function (item) {
		console.info('onBeforeUploadItem', item);
	};
	uploader.onProgressItem = function (fileItem, progress) {
		console.info('onProgressItem', fileItem, progress);
	};
	uploader.onProgressAll = function (progress) {
		console.info('onProgressAll', progress);
	};
	uploader.onSuccessItem = function (fileItem, response, status, headers) {
		console.info('onSuccessItem', fileItem, response, status, headers);
		$scope.fileUploadResult = fileItem;
	};
	uploader.onErrorItem = function (fileItem, response, status, headers) {
		console.info('onErrorItem', fileItem, response, status, headers);
	};
	uploader.onCancelItem = function (fileItem, response, status, headers) {
		console.info('onCancelItem', fileItem, response, status, headers);
	};

	uploader.onCompleteItem = function (fileItem, response, status, headers) {
		console.info('onCompleteItem', fileItem, response, status, headers);
		$scope.fileUploadResult.fileUri = response.fileUri;
		$scope.fileUploadResult.uploadFeedback = response.uploadFeedback;
		$scope.fileUploadResult.resultData = response.resultData;
		$scope.setFileUploadResult(response.fileUri, response.uploadFeedback, response.resultData);

	};
	uploader.onCompleteAll = function () {
		console.info('onCompleteAll');
	};

	console.info('uploader', uploader);
}]);