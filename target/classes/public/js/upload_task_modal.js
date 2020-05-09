// 开启严格模式
'use strict';
app.controller('UploadTaskModalPageCtrl', ['$scope', 'FileUploader','$http', '$state','$uibModalInstance','$uibModal',//这里引入学期Service可以调用他的方法
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
   
	var uploader = $scope.uploader = new FileUploader({
        url: '/api/common/upload_task_list?edu_id='+$scope.photoObj.edu_id,
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
            extName=extName.toUpperCase();
			if(extName!='PNG' && extName!='JPG'){
                swal("错误", "上传文件类型错误!", "error");
	    		return false;
			}
            var size=item.size;
			// alert(size);
			if(size>1024*1000*2){
				swal("错误", "上传文件大小超过2M!", "error");
				return false;
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
//    //提交按钮
//    $scope.handle = function(){
//    	if($scope.oneObj.index=="a"){
//    		alert($scope.fileUploadResult.fileUri);
//    		$scope.oneObj.photoA=$scope.fileUploadResult.fileUri;
//    		$scope.write();
//    	}
//    	if($scope.oneObj.index=="b"){
//    		$scope.oneObj.photoB=$scope.fileUploadResult.fileUri;
//    		$scope.write();
//    	}
//    	$scope.close();
//    };
    
    $scope.showPhoto = function(){
		$scope.showUrls = [{path:$scope.photoObj.photourl}];
		var modalInstance = $uibModal.open({
			templateUrl: "/cityshowphotos",//后台url
    		controller: 'CityShowPhotosModalPageCtrl',//controllerjs
    		scope: $scope,//父页面$scope对象
    		keyboard: false
	    });
		
		modalInstance.result.then(function(result){
			
		});
	};


    $scope.submitSave=function(){
    	if($scope.fileUploadResult.fileUri==null||$scope.fileUploadResult.fileUri==""){
    		swal("提交失败!", "请选择要上传的图片。", "error");
    	}else{
    		if($scope.photoObj.type=="A"){
        		$scope.photoObj.contenta=$scope.fileUploadResult.fileUri;
        	}else{
        		$scope.photoObj.contentb=$scope.fileUploadResult.fileUri;
        	}
    		  var formData = {//声明用于传到后台的参数
    	    			courseId:$scope.photoObj.course_id,
    	    			hourId:$scope.photoObj.hour_id,
    	    			eduId:$scope.photoObj.edu_id,
    	    			contenta:$scope.photoObj.contenta,
    	    			contentb:$scope.photoObj.contentb
    	    			
    	    	};
    	    	var requestUrl = "/api/instteachercoursemanager/instteacher_add_taskphotos";
    	    	
    			$http({
    				method  : 'POST',
    				url     : requestUrl,
    				data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
    				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
    			}).success(function(data, status, headers, config) {
    				if(data.success=='1'){
    					 swal("提交成功!", "上传任务单成功.", "success");
    					 $scope.queryRecordPage();
    					 $scope.close();					 
    				}else{
                        swal("提交失败!", "上传任务单失败.", "error");
                        $scope.close();
					}
    			}).error(function(data, status, headers, config) {
    			});	
    	}
    			 
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