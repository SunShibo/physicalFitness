//开启严格模式
'use strict';
app.controller('ActivityPlaceRecordListPageCtrl', ['$scope', '$http', '$state', '$cookieStore','myCommonService','parameterService',
                                      function($scope, $http, $state,$cookieStore,myCommonService,parameterService) {

	$scope.obj= {name:'ckl'};
	
	$scope.dateOptions = {
	        formatYear: 'yy',
	        startingDay: 1,
	        class: 'datepicker'
	      };
	 
	 $scope.itemNames = [{name:"序号"},{name: "授课地点(具体地址)"},{name:"所在区县"},{name:'审核状态'},{name:'操作'}];
//	 $scope.itemNames = [{name:"序号"},{name: "地址类别"},{name: "授课地点(具体地址)"},{name:"所在区县"},{name:'审核状态'},{name:'操作'}];
	 
	 $scope.queryReocordResult = {recordsList:[]};
	 
	 $scope.places = [{id:null,address_send:'请选择地点'}];
	 
	 $scope.checkStatus = [{dictId:null,dictName:'请选择审核状态'},{dictId:0,dictName:'待审核'},{dictId:1,dictName:'审核通过'},{dictId:2,dictName:'未通过'}];
	 
	 $scope.queryObj={
			 selectPlace:null,
			 selectCheckStatus:null
	 };
	
	 $scope.repeatDone = function(){
		 $('[data-toggle="tooltip"]').tooltip();
	 };
	
	$scope.paramObj = {
		courseAddressNum:null
	};
	
	function setValue(value){
		$scope.paramObj.courseAddressNum = value;
	}
	parameterService.getParameterByCode("course_address_num",setValue);
	 	/*
	 	 * 编辑参数
	 	 **/
	 	
	 	$scope.addRecord = function(){
//	 		if($scope.queryReocordResult.recordsList.length >= $scope.paramObj.courseAddressNum){
//	 			swal("备案地址最多允许"+$scope.paramObj.courseAddressNum+"个");
//	 		}else{
	 			$state.go('app.activityplacerecordadd');
//	 		}
	 	};
	 	
	 	
	 	$scope.canAdd=function(){
	 		if($scope.queryReocordResult.recordsList.length >= $scope.paramObj.courseAddressNum){
	 			return false;
	 		}else{
	 			return true;
	 		}
	 	};
	 	/*
	 	 * 编辑参数
	 	 **/
	 	
	 	$scope.editRecord = function(obj){
	 		
//	 		if(obj.checkStatus !='审核通过'){
	 			
		 		$state.go('app.activityplacerecordedit',{"placeId":obj});
//	 		}
	 	};

		  $scope.positionRecord = function(obj){
			  $state.go('app.activityplaceedit',{"placeId":obj});
		  };
	 	
	 	$scope.lookRecord = function(paramterId){
	 		
	 		$state.go('app.activityplacerecorddetail',{"placeId":paramterId});
	 	};
	 	 
	 	
	 	/*
		 * 删除参数
		 * */
		$scope.deleteRecord = function(obj){
				swal({
					title : "您是否确认要删除该地址？",
					text : "删除后将无法恢复!",
					type : "warning",
					showCancelButton : true,
					confirmButtonColor : "#DD6B55",
					confirmButtonText : "确认",
					cancelButtonText : "取消",
					closeOnConfirm : false,
					customClass : ""
				}, function() {
					$(".confirm").attr("disabled",true);
					var formData = {//参数
							addressId:obj
			    	};
					var requestUrl="/api/activityplacemanager/address_del";
					$http({
						method  : 'POST',
						url     : requestUrl,
						data    : $.param(formData),  // pass in data as strings
						headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
					}).success(function(data, status, headers, config) {
						if(data.result=="ok"){
							swal("删除成功!", "您选择的地址已经删除.", "success");
							initPage();
						}else{
							swal("删除失败!", "", "error");
						}
					}).error(function(data, status, headers, config) {
					});	
				});
		};
		
		function querySelectAddress(){
			var requestUrl = "/api/activityplacemanager/query_select_address";
			$http({
				method  : 'POST',
				url     : requestUrl,
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				angular.forEach(data, function(addressObj, key) {
					$scope.places.push(addressObj);
				});
				if($scope.queryObj.selectPlace ==null){
					$scope.queryObj.selectPlace = $scope.places[0];
				}else{
					angular.forEach($scope.places, function(addressObj, key) {
						if(addressObj.id == $scope.queryObj.selectPlace.id){
							$scope.queryObj.selectPlace = addressObj;
						}
					});
				}
			}).error(function(data, status, headers, config) {
			});	
		}
		
		 $scope.queryAddressPage = function(pageable){
		    	var formData = {
		    			addressId : $scope.queryObj.selectPlace.id,
		    			searchStatus : $scope.queryObj.selectCheckStatus.dictId,
		    			page:pageable
		    	};
		    	var cookieObj={"page":pageable,"address":$scope.queryObj.selectPlace,"searchStatus":$scope.queryObj.selectCheckStatus};//用于放到cookie的查询条件对象
		    	var requestUrl = "/api/activityplacemanager/query_address_page";
				$http({
					method  : 'POST',
					url     : requestUrl,
					data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
					headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
				}).success(function(data, status, headers, config) {
					myCommonService.setMyCookie($cookieStore,"activityplacerecordlist",cookieObj);//把页面查询条件对象放到cookie
					$scope.queryReocordResult.recordsList= data.content;//列表数据赋值
					$scope.page = data;//分页对象赋值
				}).error(function(data, status, headers, config) {
				});				 
			};
			
			function initPage(){
				var cookieParam=myCommonService.getMyCookie($cookieStore,"activityplacerecordlist");//从cookie里获取当前页面查询条件对象
				if(cookieParam!=null && cookieParam.address !=null){
					$scope.queryObj.selectPlace = cookieParam.address;
				}else{
					$scope.queryObj.selectPlace =  $scope.places[0];
				}
				if(cookieParam!=null && cookieParam.searchStatus !=null){
					angular.forEach($scope.checkStatus, function(checkStatu, key) {
						if(cookieParam.searchStatus.dictId == checkStatu.dictId){
							$scope.queryObj.selectCheckStatus = cookieParam.searchStatus;
						}
					});
				}else{
					$scope.queryObj.selectCheckStatus = $scope.checkStatus[0];
				}
				querySelectAddress();
				if(cookieParam!=null && cookieParam.page !=null){//用cookie中的页码去查询
					$scope.queryAddressPage(cookieParam.page);
				}else{
					$scope.queryAddressPage();
				}
			}
			initPage();
			
			$scope.clearCookie=function(){//清除搜索条件
				myCommonService.clearMyCookie($cookieStore,"activityplacerecordlist");
				initPage();
			};
		
  }]);