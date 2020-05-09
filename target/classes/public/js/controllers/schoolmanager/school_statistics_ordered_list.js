// 开启严格模式
'use strict';
app.controller('SchoolStatisticsOrderedListPageCtrl', ['$scope', '$http', '$state', '$stateParams',//这里引入学期Service可以调用他的方法
                                      function($scope, $http, $state,$stateParams) {

	
	$scope.itemNames = [{name: "年级"},{name: "班级"},{name: "姓名"},{name: "教育ID"},{name: "预约次数"},{name: "完成次数"}];//列表页的表头
	
	
	 $scope.repeatDone = function(){
	 		$('[data-toggle="tooltip"]').tooltip();
	 	};
	 	
	 $scope.queryResult = {
				content : []
			};
	
	 $scope.init = function(/*pageable*/){//获取列表数据的方法
	    	var formData = {//声明用于传到后台的参数
	    			gradeId:$stateParams.gradeId,
	    			classname:$stateParams.classname,
					term:$stateParams.term
	    	};
	    	var requestUrl = "/api/schoolstatisticsmanager/school_statistics_ordered_list";
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

	/*
	 * 已预约次数
	 */
	$scope.goOrdered = function(eduId) {
		$state.go('app.schoolstatisticsorderedtimes',{"gradeId":$stateParams.gradeId,"classname":$stateParams.classname,"eduId":eduId,"term":$stateParams.term});
	};
	
	/*
	 * 已预约人数详情——完成次数
	 * */
	$scope.goCompletedOrder = function(eduId) {
		
		$state.go('app.schoolstatisticsorderedcompletetimes',{"gradeId":$stateParams.gradeId,"classname":$stateParams.classname,"eduId":eduId,"term":$stateParams.term});
	};
	
	
		
		
	/*
	 * 返回
	 * */	
	$scope.goBack = function(){
		$state.go('app.schoolstatisticsdatalist');
	};		
	
	

  }]);