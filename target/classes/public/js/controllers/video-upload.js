'use strict';
app.controller('VideoUploadCtrl',  ['$scope', 'FileUploader',   function($scope, FileUploader) {
    
   $scope.videoUploadResult = {
		   fileUri : "",
		   uploadFeedback : ""
   };
   
	var videoUploader = $scope.videoUploader = new FileUploader({
        url: '/api/common/service_upload_video_file',
        autoUpload : true
    });
    // FILTERS

	videoUploader.filters.push({
        name: 'customFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            return this.queue.length < 1;
        }
    });
	videoUploader.filters.push({
        name: 'fileTypeFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
//        	//获得上传文件的扩展名
        	var position = item.name.lastIndexOf(".");
        	var extName = item.name.substr(position + 1);
        	try
        	{
        		//调用父控制器的上传文件类型控制方法
        		//返回对象：如果返回空，则表示允许上传，否则返回允许上传的文件类型范围
        		var uploadFileTypeCheckResult = $scope.checkUploadFileType(extName);
        		if (uploadFileTypeCheckResult != ""){
        			swal("允许上传的文件类型是："+uploadFileTypeCheckResult);
        			return false;
        		}
        	}
        	catch(e)
        	{
        		//没有定义控制文件类型的方法就会出异常，不用处理
        	}
            return true;
        }
    });

    // CALLBACKS

	videoUploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
        console.info('onWhenAddingFileFailed', item, filter, options);
    };
    
    videoUploader.onAfterAddingFile = function(fileItem) {
        console.info('onAfterAddingFile', fileItem);
        
        if(fileItem.file.type.indexOf('video') != -1 ){//如果是上传视频
        	
        	if(fileItem.file.size > 50*1024*1024){
        		swal('操作失败','上传的视频超过50M','warning');
                fileItem.remove();
        	}    		
        }else
        {
        	swal('操作失败','请上传mp4格式的视频','warning');
            fileItem.remove();
        }
    };
    
    videoUploader.onAfterAddingAll = function(addedFileItems) {
        console.info('onAfterAddingAll', addedFileItems);
    };
    videoUploader.onBeforeUploadItem = function(item) {
        console.info('onBeforeUploadItem', item);
    };
    videoUploader.onProgressItem = function(fileItem, progress) {
        console.info('onProgressItem', fileItem, progress);
    };
    videoUploader.onProgressAll = function(progress) {
        console.info('onProgressAll', progress);
    };
    videoUploader.onSuccessItem = function(fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        $scope.videoUploadResult = fileItem; 
    };
    videoUploader.onErrorItem = function(fileItem, response, status, headers) {
        console.info('onErrorItem', fileItem, response, status, headers);
    };
    videoUploader.onCancelItem = function(fileItem, response, status, headers) {
        console.info('onCancelItem', fileItem, response, status, headers);
    };
    
    videoUploader.onCompleteItem = function(fileItem, response, status, headers) {
        console.info('onCompleteItem', fileItem, response, status, headers);
        $scope.videoUploadResult.fileUri = response.fileUri; 
        $scope.videoUploadResult.uploadFeedback = response.uploadFeedback;
        $scope.setVideoUploadResult(response.fileUri, response.uploadFeedback);
        
        fileItem.remove();
    };
    
    videoUploader.onCompleteAll = function() {
        console.info('onCompleteAll');
    };

    
}]);