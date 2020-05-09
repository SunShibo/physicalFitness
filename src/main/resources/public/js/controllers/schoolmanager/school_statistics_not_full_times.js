// 开启严格模式
'use strict';
app.controller('SchoolStatisticsNotFullTimesPageCtrl', ['$scope', '$http', '$state', '$stateParams',//这里引入学期Service可以调用他的方法
                                      function($scope, $http, $state ,$stateParams) {
	
	$scope.itemNames = [{name:"姓名"},{name: "活动名称"},{name: "活动类别"},{name: "开课时间"},{name: "预约时间"}];//列表页的表头
	
	 $scope.repeatDone = function(){
	 		$('[data-toggle="tooltip"]').tooltip();
	 	};
	 	
	 	 $scope.queryResult = {
					content : []
				};
	 
	   $scope.queryRecordPage = function(){//获取列表数据的方法
		   var formData = {//声明用于传到后台的参数
	    			eduId:$stateParams.eduId,
			   		term:$stateParams.term
	    	};
	    	var requestUrl = "/api/schoolstatisticsmanager/school_statistics_ordered_times";
	    	$http({
				method  : 'POST',
				url     : requestUrl,
				data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				$scope.queryResult.content=data;
			}).error(function(data, status, headers, config) {
			});
		};
		$scope.queryRecordPage();
		
		$scope.goBack=function(){
			$state.go('app.schoolstatisticsnotfulllist',{"gradeId":$stateParams.gradeId,"classname":$stateParams.classname,"term":$stateParams.term});
		};
			
	

  }]);