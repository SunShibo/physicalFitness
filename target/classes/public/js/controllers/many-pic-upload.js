'use strict';
app.controller('ManyPicUploadCtrl',  ['$scope', 'FileUploader','$stateParams',   function($scope, FileUploader,$stateParams) {
    
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
   //第一张图片
	var uploader1 = $scope.uploader1 = new FileUploader({
        url: '/api/common/upload_inst_file?courseId='+$stateParams.param+"&isType=1",
        autoUpload : true
    });

    // FILTERS

    uploader1.filters.push({
        name: 'customFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            return this.queue.length < 1;
        }
    });
    uploader1.filters.push({
        name: 'fileTypeFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
        	$scope.flags.flag1=1;
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
   /*第二张图片上传 start*/
    var uploader2 = $scope.uploader2 = new FileUploader({
    	url: '/api/common/upload_inst_file?courseId='+$stateParams.param+"&isType=1",
    	autoUpload : true
    });
    
    // FILTERS
    
    uploader2.filters.push({
    	name: 'customFilter',
    	fn: function(item /*{File|FileLikeObject}*/, options) {
    		return this.queue.length < 1;
    	}
    });
    uploader2.filters.push({
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
    uploader2.onCompleteItem = function(fileItem, response, status, headers) {
       /* console.info('onCompleteItem', fileItem, response, status, headers);
        $scope.fileUploadResult.fileUri = response.fileUri; 
        $scope.fileUploadResult.uploadFeedback = response.uploadFeedback;*/
    	if(response.fileUri == ""){
    		swal("图片格式有误,请重新上传");
    	}else{
    		$scope.setFileUploadResult2(response.fileUri, response.uploadFeedback);
    	}
        
    };
    /*第二张图片上传 end*/
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*第三张图片上传 start*/
    var uploader3 = $scope.uploader3 = new FileUploader({
    	url: '/api/common/upload_inst_file?courseId='+$stateParams.param+"&isType=1",
    	autoUpload : true
    });
    
    // FILTERS
    
    uploader3.filters.push({
    	name: 'customFilter',
    	fn: function(item /*{File|FileLikeObject}*/, options) {
    		return this.queue.length < 1;
    	}
    });
    uploader3.filters.push({
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
    uploader3.onCompleteItem = function(fileItem, response, status, headers) {
    	/* console.info('onCompleteItem', fileItem, response, status, headers);
        $scope.fileUploadResult.fileUri = response.fileUri; 
        $scope.fileUploadResult.uploadFeedback = response.uploadFeedback;*/
    	if(response.fileUri == ""){
    		swal("图片格式有误,请重新上传");
    	}else{
    		$scope.setFileUploadResult3(response.fileUri, response.uploadFeedback);
    	}
    	
    };
    /*第三张图片上传 end*/
    
    
    
    
    
    
    
    
    
    
    /*第四张图片上传 start*/
    var uploader4 = $scope.uploader4 = new FileUploader({
    	url: '/api/common/upload_inst_file?courseId='+$stateParams.param+"&isType=1",
    	autoUpload : true
    });
    
    // FILTERS
    
    uploader4.filters.push({
    	name: 'customFilter',
    	fn: function(item /*{File|FileLikeObject}*/, options) {
    		return this.queue.length < 1;
    	}
    });
    uploader4.filters.push({
    	name: 'fileTypeFilter',
    	fn: function(item /*{File|FileLikeObject}*/, options) {
    		//获得上传文件的扩展名
    		$scope.flags.flag2=1;
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
    uploader4.onCompleteItem = function(fileItem, response, status, headers) {
    	/* console.info('onCompleteItem', fileItem, response, status, headers);
        $scope.fileUploadResult.fileUri = response.fileUri; 
        $scope.fileUploadResult.uploadFeedback = response.uploadFeedback;*/
    	if(response.fileUri == ""){
    		swal("图片格式有误,请重新上传");
    	}else{
    		$scope.setFileUploadResult4(response.fileUri, response.uploadFeedback);
    	}
    	
    };
    /*第四张图片上传 end*/
    
    
    
    
    
    
    
    
    
    
    
    
    /*第五张图片上传 start*/
    var uploader5 = $scope.uploader5 = new FileUploader({
    	url: '/api/common/upload_inst_file?courseId='+$stateParams.param+"&isType=1",
    	autoUpload : true
    });
    
    // FILTERS
    
    uploader5.filters.push({
    	name: 'customFilter',
    	fn: function(item /*{File|FileLikeObject}*/, options) {
    		return this.queue.length < 1;
    	}
    });
    uploader5.filters.push({
    	name: 'fileTypeFilter',
    	fn: function(item /*{File|FileLikeObject}*/, options) {
    		//获得上传文件的扩展名
    		$scope.flags.flag3=1;
    		var position = item.name.lastIndexOf(".");
    		var extName = item.name.substr(position + 1);
    		try
    		{
    			//调用父控制器的上传文件类型控制方法
    			//返回对象：如果返回空，则表示允许上传，否则返回允许上传的文件类型范围
    			var uploadFileTypeCheckResult = $scope.checkUploadFileType(extName);
    			if (uploadFileTypeCheckResult != ""){
    				alert("允许上传的文件类型是："+uploadFileTypeCheckResult);
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
    uploader5.onCompleteItem = function(fileItem, response, status, headers) {
    	/* console.info('onCompleteItem', fileItem, response, status, headers);
        $scope.fileUploadResult.fileUri = response.fileUri; 
        $scope.fileUploadResult.uploadFeedback = response.uploadFeedback;*/
    	if(response.fileUri == ""){
    		swal("图片格式有误,请重新上传");
    	}else{
    		$scope.setFileUploadResult5(response.fileUri, response.uploadFeedback);
    	}
    	
    };
    /*第五张图片上传 end*/
    
    
    // CALLBACKS

    uploader1.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
        console.info('onWhenAddingFileFailed', item, filter, options);
    };
    uploader1.onAfterAddingFile = function(fileItem) {
        console.info('onAfterAddingFile', fileItem);
    };
    uploader1.onAfterAddingAll = function(addedFileItems) {
        console.info('onAfterAddingAll', addedFileItems);
        $scope.checkFileTypeAndSize(addedFileItems);
    };
    uploader2.onAfterAddingAll = function(addedFileItems) {
        console.info('onAfterAddingAll', addedFileItems);
        $scope.checkFileTypeAndSize2(addedFileItems);
    };
    uploader1.onBeforeUploadItem = function(item) {
        console.info('onBeforeUploadItem', item);
    };
    uploader1.onProgressItem = function(fileItem, progress) {
        console.info('onProgressItem', fileItem, progress);
    };
    uploader1.onProgressAll = function(progress) {
        console.info('onProgressAll', progress);
    };
    uploader1.onSuccessItem = function(fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
        $scope.fileUploadResult1 = fileItem; 
    };
    uploader1.onErrorItem = function(fileItem, response, status, headers) {
        console.info('onErrorItem', fileItem, response, status, headers);
    };
    uploader1.onCancelItem = function(fileItem, response, status, headers) {
        console.info('onCancelItem', fileItem, response, status, headers);
    };
    
    uploader1.onCompleteItem = function(fileItem, response, status, headers) {
    	
        console.info('onCompleteItem', fileItem, response, status, headers);
        $scope.fileUploadResult1.fileUri = response.fileUri; 
        $scope.fileUploadResult1.uploadFeedback = response.uploadFeedback;
        if(response.fileUri == ""){
    		swal("图片格式有误,请重新上传");
    	}else{
    		$scope.setFileUploadResult1(response.fileUri, response.uploadFeedback);
    	}
        
    };
    uploader1.onCompleteAll = function() {
        console.info('onCompleteAll');
    };

    console.info('uploader', uploader1);
}]);