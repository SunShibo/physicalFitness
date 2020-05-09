// 开启严格模式
'use strict';
app.controller('SchoolStatisticsRollTotalListPageCtrl', ['$scope', '$http', '$state','$stateParams',
                                      function($scope, $http, $state,$stateParams) {
	
	$scope.itemNames = [{name: "年级"},{name: "班级"},{name: "姓名"},{name:"教育ID"}];//列表页的表头
	 $scope.repeatDone = function(){
	 		$('[data-toggle="tooltip"]').tooltip();
	 	};
	 	
	$scope.queryObj = {
			keysword:null
	};
	 	
 	/*
	 * 返回
	 * */	
	$scope.goBack = function(){
		$state.go('app.schoolstatisticsdatalist');
	};
	 	
	 $scope.queryResult = {
				content : [ {
					studentname : '小丽',
					grade_name : '一年级',
					classname : '一班',
					education_id : '234445'
				} ]
			};
	 
	$scope.init = function(/*pageable*/){//获取列表数据的方法
	    	var formData = {//声明用于传到后台的参数
	    			gradeId:$stateParams.gradeId,
	    			classname:$stateParams.classname,
					term:$stateParams.term_id
	    	};
	    	var requestUrl = "/api/schoolstatisticsmanager/student_lists";
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