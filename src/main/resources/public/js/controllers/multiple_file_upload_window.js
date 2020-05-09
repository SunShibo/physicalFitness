'use strict';
app.controller('UploadMultiPicWindowCtrl', ['$scope', '$http', '$state',
                                      function($scope, $http, $state) {
	 $scope.oneObj = {
		  aboutme: '我是多图片上传demo页面！'
	 };
	 
	 $scope.dropZoneOptions =
	 {
		maxFilesize:10,  //MB
		maxFiles:50,
		addRemoveLinks: true,
		dictFileTooBig: '正在上传的文件大小为：{{filesize}}MB，允许上传的文件最大不能超过：0.1MB。'
	 };
     //成功上传的文件
	 var successUploadFiles = [];
	 var successUploadFile = {
			'file_timeStamp' : '',        //上传文件的时间戳, 如果时间戳相同则认为是同一个文件
			'file_uri' : ''     //文件上传后返回的相对地址，根据该地址可以获取到该文件
	 };
		//var seconds = 3000;

		$scope.callbacks =
		{
			onFileUpload:function( file ) {
				//上传文件大小或类型不满足
				if (file.status == 'error'){
				  return;	
				}
			 var formData = new FormData();
			 formData.append('file', file); 
	         $http({
	              method:'POST',
	              url:"api/common/upload_file",
	              data: formData,
	              headers: {'Content-Type':undefined},
	              transformRequest: angular.identity 
	          }).success(function(data, status, headers, config) {
					
//					  alert(JSON.stringify(data.fileUri));
					  successUploadFile.file_timeStamp = file.timeStamp;
					  successUploadFile.file_uri = data.fileUri;
					  successUploadFiles.push(successUploadFile);
					  initSuccessUploadFile();
					
	       					
					  //下发文件上传成功的事件 
					  $scope.$emit('dropZone:completed',file);
	       				

				}).error(function(data, status, headers, config) {
					// $scope.errorMsg = '服务器异常!';
//					$scope.operaResult = data;
//					alert(status + '--------'+ headers+ '--------' + JSON.stringify(data)+ '---------' + config);
				});
				
			},
			removedFile:function(file){
				var currentSuccessUploadFile = ifSameFile(file.timeStamp);
				if (currentSuccessUploadFile != null){
					var fileUri = currentSuccessUploadFile.file_uri;
					//todo: 根据文件的相对地址删除文件
//					alert('fileUri='+fileUri);
				}
				//删除成功后
				var index = successUploadFiles.indexOf(currentSuccessUploadFile);
		        if (index !== -1) {
		        	successUploadFiles.splice(index, 1);
		        }
			}

		}
		function initSuccessUploadFile(){
			 successUploadFile = {
						'file_timeStamp' : '',        //上传文件的时间戳, 如果时间戳相同则认为是同一个文件
						'file_uri' : ''     //文件上传后返回的相对地址，根据该地址可以获取到该文件
			 };
		}
		function ifSameFile(fileTimeStamp){
			try{
				for (var i = 0; i < successUploadFiles.length; i++){
					var currentSuccessUploadFile = successUploadFiles[i];
					if (currentSuccessUploadFile.file_timeStamp == fileTimeStamp){
						return currentSuccessUploadFile;
					}
				}
			}
			catch(e){
				//不处理
			}
			return null;
		}
		//初始化
		function initSuccessUploadFiles(){
			successUploadFiles = [];
		}
		initSuccessUploadFiles();

 }]);