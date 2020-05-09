// 开启严格模式
'use strict';
app.controller('SchoolStudentOrderRecordListPageCtrl', ['$scope', '$http', '$state','$cookieStore','myCommonService',//这里引入学期Service可以调用他的方法
                                      function($scope, $http, $state,$cookieStore,myCommonService) {
	
	
	$scope.itemNames = [{name: "课程名称"},{name: "课程类别"},{name: "上课时间"},{name:"上课状态"},{name:"考勤状态"},{name:"任务单"},{name:"任务单上传者"}];//列表页的表头
		
	$scope.repeatDone = function(){
	 		$('[data-toggle="tooltip"]').tooltip();
	 	};
	
	$scope.grades = [{grade:'一年级',classes:[{classname:'一班'},{classname:'二班'}]},{grade:'二年级',classes:[{classname:'1班'},{classname:'2班'}]}];
	
	$scope.selectGrade = $scope.grades[0];
	
	$scope.selectClass = $scope.selectGrade.classes[0];
	
	$scope.dispanSelected = function(dict){
    	
    	if (dict == undefined){
    		return;
    	}else{
    		
    		$scope.selectGrade = dict;
    		$scope.selectClass = $scope.selectGrade.classes[0];

    	}
    	
    	
    };
	 	
	$scope.queryResult= {
			  "content": [],
			            "describe": "string,描述",
			            "code": "integer,200"
			          };

	
	 	
	$scope.queryObj = {//声明查询对象
			keyword:null
	};
	
	/*
	 * 查询约课退课记录
	 * */
	$scope.queryRecord = function(pageable){//获取列表数据的方法
		    //没有输入教育ID 不查数据
			if($scope.queryObj.keyword==null){
				return false;
			}
	    	var formData = {//声明用于传到后台的参数
	    			page:pageable,
	    			keyword:$scope.queryObj.keyword
	    				
	    	};
	    	var cookieObj={"keyword":$scope.queryObj.keyword};//用于放到cookie的查询条件对象
	    	var requestUrl = "/api/schoolmanager/school_studentorder_record_list";
	    	
			$http({
				method  : 'POST',
				url     : requestUrl,
				data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				myCommonService.setMyCookie($cookieStore,"school_studentorder_record_list",cookieObj);//把页面查询条件对象放到cookie
				$scope.queryResult.content = data.content;//列表数据赋值
				$scope.page = data;//分页对象赋值
			}).error(function(data, status, headers, config) {
//					$scope.operaResult = data;
			});			 
		};
		$scope.init=function(){
			var cookieParam=myCommonService.getMyCookie($cookieStore,"school_studentorder_record_list");//从cookie里获取当前页面查询条件对象
			if(cookieParam!=null && cookieParam.keyword !=null){//cookie中有值就赋给查询对象
				$scope.queryObj.keyword = cookieParam.keyword;
			}
			$scope.queryRecord(0);	
		};
		$scope.init();
		$scope.clearCookie=function(){//清除搜索条件
			myCommonService.clearMyCookie($cookieStore,"school_studentorder_record_list");
			$scope.queryObj.keyword=null;
			$scope.queryRecord();	
		};	
	

  }]);