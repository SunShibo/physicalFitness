'use strict';
app.controller('PhotoUploadCtrl',  ['$scope', 'FileUploader',   function($scope, FileUploader) {
    
   $scope.selectPic = false;   
	//选择图片
   $scope.to_select_pic = function(){
   		$scope.selectPic = true;
   };
   //取消选择图片
   $scope.cancel_select_pic = function(){
	   $scope.selectPic = false;
   };
   $scope.fileUploadResult = {
		   fileUri : "",
		   uploadFeedback : ""
   };
   
	var uploader = $scope.uploader = new FileUploader({
        url: 'api/common/upload_file',
        autoUpload : true
    });

    // FILTERS

    uploader.filters.push({
        name: 'customFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            return this.queue.length < 1;
        }
    });
    uploader.filters.push({
        name: 'fileTypeFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
        	//获得上传文件的扩展名
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

    uploader.onAfterAddingFile = function(fileItem) {
        console.info('onAfterAddingFile', fileItem);
        
        if(fileItem.file.type.indexOf('video') != -1 ){//如果是上传视频
        	
        	if(fileItem.file.size > 200*1024*1024){
        		
        		swal('操作失败','上传的视频超过200M','warning');
                fileItem.remove();
        	}    		
        }else
        {
        	
        	if(fileItem.file.size > 5*1024*1024){
        		swal('操作失败','上传的图片超过5M','warning');
                fileItem.remove();
        	}
        }
    };
    
    uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
        console.info('onWhenAddingFileFailed', item, filter, options);
    };
   
    uploader.onAfterAddingAll = function(addedFileItems) {
        console.info('onAfterAddingAll', addedFileItems);
    };
    uploader.onBeforeUploadItem = function(item) {
        console.info('onBeforeUploadItem', item);
    };
    uploader.onProgressItem = function(fileItem, progress) {
        console.info('onProgressItem', fileItem, progress);
    };
    uploader.onProgressAll = function(progress) {
        console.info('onProgressAll', progress);
    };
    uploader.onSuccessItem = function(fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        $scope.fileUploadResult = fileItem; 
    };
    uploader.onErrorItem = function(fileItem, response, status, headers) {
        console.info('onErrorItem', fileItem, response, status, headers);
    };
    uploader.onCancelItem = function(fileItem, response, status, headers) {
        console.info('onCancelItem', fileItem, response, status, headers);
    };
    
    uploader.onCompleteItem = function(fileItem, response, status, headers) {
        console.info('onCompleteItem', fileItem, response, status, headers);
        $scope.fileUploadResult.fileUri = response.fileUri; 
        $scope.fileUploadResult.uploadFeedback = response.uploadFeedback;
        $scope.setFileUploadResult(response.fileUri, response.uploadFeedback);
        
    };
    uploader.onCompleteAll = function() {
        console.info('onCompleteAll');
    };

    console.info('uploader', uploader);
}]);