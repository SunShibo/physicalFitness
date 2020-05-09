// 开启严格模式
'use strict';
app.controller('UploadModalPageCtrl', ['$scope', 'FileUploader','$http', '$state','$uibModalInstance','$uibModal',//这里引入学期Service可以调用他的方法
                                      function($scope,FileUploader, $http, $state,$uibModalInstance,$uibModal) {
	
	
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
   
   $scope.showPhoto = function(url){
		$scope.showUrls = [{path:url}];
		var modalInstance = $uibModal.open({
			templateUrl: "/cityshowphotos",//后台url
   		controller: 'CityShowPhotosModalPageCtrl',//controllerjs
   		scope: $scope,//父页面$scope对象
   		keyboard: false
	    });
		
		modalInstance.result.then(function(result){
			
		});
	};
   
	var uploader = $scope.uploader = new FileUploader({
        url: '/api/common/upload_file',
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
        			alert("允许上传的文件类型是："+uploadFileTypeCheckResult);
        			return false;
        		}
        	}
        	catch(e)
        	{
        		//没有定义控制文件类型的方法就会出异常，不用处理
        		//alert(e);
        	}
            return true;
        }
    });

    // CALLBACKS

    uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
        console.info('onWhenAddingFileFailed', item, filter, options);
    };
    uploader.onAfterAddingFile = function(fileItem) {
        console.info('onAfterAddingFile', fileItem);
    };
    uploader.onAfterAddingAll = function(addedFileItems) {
        console.info('onAfterAddingAll', addedFileItems);
        
//        //选择完文件后验证文件类型和大小
//        $scope.checkFileTypeAndSize(addedFileItems);
        
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
//        $scope.setFileUploadResult(response.fileUri, response.uploadFeedback);
    };
    uploader.onCompleteAll = function() {
        console.info('onCompleteAll');
    };
    //关闭按钮
    $scope.close = function(){
    	$uibModalInstance.close();
    };
    //提交按钮
    $scope.handle = function(){
    	if($scope.oneObj.index=="a"){
//    		alert($scope.fileUploadResult.fileUri);
    		$scope.oneObj.photoA=$scope.fileUploadResult.fileUri;
    		$scope.write();
    	}
    	if($scope.oneObj.index=="b"){
    		$scope.oneObj.photoB=$scope.fileUploadResult.fileUri;
    		$scope.write();
    	}
    	$scope.updateList();
    	$scope.close();
    };
		
	
    $scope.loadUpload=function(){
    	$('.dropify').dropify();
    	// Translated
    	$('.dropify-fr').dropify({
    		messages: {
    			'default': 'Glissez-déposez un fichier ici ou cliquez',
    			replace: 'Glissez-déposez un fichier ou cliquez pour remplacer',
    			remove:  'Supprimer',
    			error:   'Désolé, le fichier trop volumineux'
    		}
    	});
    	// Used events
    	var drEvent = $('#input-file-events').dropify();

	        drEvent.on('dropify.beforeClear', function(event, element){
	            return confirm("Do you really want to delete \"" + element.file.name + "\" ?");
	        });

	        drEvent.on('dropify.afterClear', function(event, element){
	            alert('File deleted');
	        });

	        drEvent.on('dropify.errors', function(event, element){
	            swal('Has Errors');
	        });

	        var drDestroy = $('#input-file-to-destroy').dropify();
	        drDestroy = drDestroy.data('dropify');
	        $('#toggleDropify').on('click', function(e){
	            e.preventDefault();
	            if (drDestroy.isDropified()) {
	                drDestroy.destroy();
	            } else {
	                drDestroy.init();
	            }
	        });
		};
		
  }]);