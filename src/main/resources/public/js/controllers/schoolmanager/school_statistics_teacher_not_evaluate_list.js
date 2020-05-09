// 开启严格模式
'use strict';
app.controller('SchoolStatisticsTeacherNotEvalauteListPageCtrl', ['$scope', '$http', '$state', 'schoolYearService','$stateParams',//这里引入学期Service可以调用他的方法
                                      function($scope, $http, $state,schoolYearService,$stateParams) {

	
	$scope.itemNames = [{name: "教育ID"},{name: "姓名"},{name: "预约教师教育ID"},{name: "预约教师姓名"},{name: "课程名称"},{name: "上课时间"}];//列表页的表头
	
	 $scope.repeatDone = function(){
	 		$('[data-toggle="tooltip"]').tooltip();
	 	};
	 	
	 $scope.queryResult = {
				content : [ {
					eduid : '小丽',
					studentname : 234,
					coursename : '化学入门'
				} ]
			};


	$scope.goOrdered = function(obj) {
		
		$state.go('app.schoolstatisticsorderedtimes');
	};
	
	/*
	 * 已预约人数详情——完成次数
	 * */
	$scope.goCompletedOrder = function(obj) {
		
		$state.go('app.schoolstatisticsorderedcompletetimes');
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
	    	var requestUrl = "/api/schoolstatisticsmanager/school_statistics_teacher_not_evaluate_list";
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