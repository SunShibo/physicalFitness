// 开启严格模式
'use strict';
app.controller('SchoolStatisticsNotEvaluateListPageCtrl', ['$scope', '$http', '$state', '$stateParams',//这里引入学期Service可以调用他的方法
                                      function($scope, $http, $state,$stateParams) {
	
	$scope.itemNames = [{name: "年级"},{name: "班级"},{name: "姓名"},{name: "性别"},{name: "教育ID"},{name: "课程名称"},{name: "上课时间"},{name: "授课老师"}];//列表页的表头
	
	
	 $scope.repeatDone = function(){
	 		$('[data-toggle="tooltip"]').tooltip();
	 	};
	 $scope.queryResult = {
				content : []
			};
	 
		
	/*
	 * 返回
	 * */	
	$scope.goBack = function(){
		$state.go('app.schoolstatisticsdatalist');
	};		
	
	 $scope.init = function(/*pageable*/){//获取列表数据的方法
		 
	    	var formData = {//声明用于传到后台的参数
	    			gradeId:$stateParams.gradeId,
	    			classname:$stateParams.classname,
	    			term:$stateParams.term
	    	};
	    	var requestUrl = "/api/schoolstatisticsmanager/school_statistics_not_evaluate_list";
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
		//初始化
	$scope.init();	

  }]);