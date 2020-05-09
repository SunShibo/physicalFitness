// 开启严格模式
'use strict';
app.controller('SchoolStatisticsNotFullListPageCtrl', ['$scope', '$http', '$state', 'schoolYearService','$stateParams',//这里引入学期Service可以调用他的方法
                                      function($scope, $http, $state,schoolYearService,$stateParams) {

	
	$scope.itemNames = [{name:"年级"},{name: "班级"},{name: "姓名"},{name: "教育ID"},{name: "应上课次数"},{name: "预约次数"},{name: "完成次数"}];//列表页的表头
	
	 $scope.repeatDone = function(){
	 		$('[data-toggle="tooltip"]').tooltip();
	 	};
	 	
	 $scope.queryObj = {
			 keysword:null
	 };
	 	
	 $scope.queryResult = {
				content : []
			};
	 
	 $scope.init = function(){//获取列表数据的方法
	    	var formData = {//声明用于传到后台的参数
	    			gradeId:$stateParams.gradeId,
	    			classname:$stateParams.classname,
					term:$stateParams.term
	    	};
	    	var requestUrl = "/api/schoolstatisticsmanager/school_statistics_not_full_list";
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
	 * 返回
	 * */	
	$scope.goBack = function(){
		$state.go('app.schoolstatisticsdatalist');
	};	
	
	/**
	 * 未约满人数详情
	 */
	$scope.toNotFullDetail = function(eduId){
		$state.go('app.schoolstatisticsnotfulltimes',{"gradeId":$stateParams.gradeId,"classname":$stateParams.classname,"eduId":eduId,"term":$stateParams.term});
	};	
			
	


  }]);