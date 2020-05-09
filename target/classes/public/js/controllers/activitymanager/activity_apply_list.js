//开启严格模式
'use strict';
app.controller('ActivityApplyListPageCtrl', ['$scope', '$http', '$state',
                                      function($scope, $http, $state) {

	$scope.obj= {name:'ckl'};
		
	 
	 $scope.itemNames = [{name:"活动项目编号"},{name: "活动项目名称"},{name: "所属年级"},{name:"所属领域"},{name:'涉及学科'},{name:'操作'}];
	 $scope.queryReocordResult = {courseList:[{number:'1-1-1',name:'测试',grade:'初一',activity :'数据与信息',subject:'物理',state:'0'},{number:'1-1-1',name:'测试',grade:'初一',activity :'数据与信息',subject:'物理',state:'1'}]};
	 
	 $scope.checkStatus = [{dictId:0,dictName:'请选择审核状态'},{dictId:1,dictName:'审核通过'},{dictId:2,dictName:'未通过'}];
	 $scope.selectCheckStatus = $scope.checkStatus[0];
	 
	
	 $scope.repeatDone = function(){
	 		$('[data-toggle="tooltip"]').tooltip();
	 	};
	
	 	
	 	/*
	 	 * 查询对象
	 	 * */
	 	$scope.queryObj = {
	 			term:null,
	 			shortName:null,
	 			name:null,
	 			paramter:null
	 			};
	 	
	 	/*
	 	 * 编辑参数
	 	 **/
	 	
	 	$scope.addRecord = function(obj){
	 		
	 		$state.go('app.instteacheradd');
	 	
	 	};
	 	
	 	
	 	/*
	 	 * 编辑参数
	 	 **/
	 	
	 	$scope.editRecord = function(id,state){
	 		if(state!=1){
	 			$state.go('app.activityapplyedit',{"id":id});
	 		}else{
	 			
	 		}	
	 		
	 		
	 		
	 		

	 	};
	 	
	 	$scope.lookRecord = function(paramterId){
	 		
	 		$state.go('app.instteacherdetail');
	 	};
	 		 	
	 	/*
		 * 禁用
		 * */
		$scope.lockRecord = function(obj){
			
			swal({
				title : "您是否确认禁用该用户吗？",
				text : "",
				type : "warning",
				showCancelButton : true,
				confirmButtonColor : "#DD6B55",
				confirmButtonText : "确认",
				cancelButtonText : "取消",
				closeOnConfirm : false,
				customClass : ""
			}, function() {
		            swal("禁用成功!", "您选择的用户已禁用.", "success");
			});
			
		};
		
		$scope.activities = [{name:'实地考察',id:'实地考察'},{name:'实验探究',id:'实验探究'},{name:'动手制作',id:'动手制作'},{name:'实践体验',id:'实践体验'}];
		/*
		 * 查询方法
		 * */
		 $scope.queryRecordPage = function(pageable){
		    	///coursemanager/course_info_list
			 var formData = {
		    			page:pageable,
		    			state:$scope.selectCheckStatus
		    	};
		    	var requestUrl = "/api/coursemanager/course_info_list_by_instid";
			    	  $http({
			                method  : 'POST',
			                url     : requestUrl,
			                data    : $.param(formData),  // pass in data as strings
			                headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
			            }).success(function(data, status, headers, config) {
			            	$scope.queryReocordResult.courseList = data.content;
							$scope.page = data;
			            	
			            }).error(function(data, status, headers, config) {
			            	// $scope.errorMsg = '服务器异常!';
			            });
			
		    				 
			};
		 //默认初始化查询	
		$scope.queryRecordPage();

  }]);