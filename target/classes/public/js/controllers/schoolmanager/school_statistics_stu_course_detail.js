// 开启严格模式
'use strict';
app.controller('SchoolStatisticsStuCourseDetailPageCtrl', ['$scope', '$http', '$state', 'schoolYearService','$stateParams',//这里引入学期Service可以调用他的方法
                                      function($scope, $http, $state,schoolYearService,$stateParams) {

	
	$scope.itemNames = [{name: "年级"},{name: "班级"},{name: "姓名"},{name: "性别"},{name: "教育id"},{name: "课程名称"},{name: "机构名称"},{name: "上课时间"},{name: "上课地点"},{name: "授课老师"},{name:"机构确认状态"},{name:"是否出勤"},{name:"任务单"},{name:"学生评价"},{name:"老师评价"},{name:"完成状态"},{name:"成绩"}];//列表页的表头
	
	 $scope.repeatDone = function(){
	 		$('[data-toggle="tooltip"]').tooltip();
	 	};
	 	
	 	
	 	
	 $scope.queryResult = {
				content : [ {
					grade_name : '年级',
					classname : '一班',
					studentname : '小丽',
					sex:'男',
					education_id:234,
					coursename:'化学入门',
					instname:'后勤部',
					study_date:1483369446000,
					address:'城宝饭店8332',
					teachername:'李老师',
					teacher_state:"完成",
					absence_state:1,
					contenta:'/uploads/20173/1489054182083.png',
					contentb:'/uploads/20173/1489054182083.png',
					studentevaluation:'呵呵',
					teacherevaluation:'呵呵',
					finishedstate:1,
					score:100
				} ]
			};
	 
	$scope.init = function(){//获取列表数据的方法
		var formData = {//参数
				gradeId:$stateParams.gradeId,
				classname:$stateParams.classname,
				term:$stateParams.term
	    };
		var requestUrl="/api/usermanager/school_statistics_stu_course_detail";
		$http({
			method  : 'POST',
			url     : requestUrl,
			data    : $.param(formData),  // pass in data as strings
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			$scope.queryResult.content=data;
		}).error(function(data, status, headers, config) {
		});	
	};

		//初始化化获取数据
	$scope.init();
	/*
	 * 返回
	 * */	
	$scope.goBack = function(){
		$state.go('app.schoolstatisticsdatalist');
	};		
	
	/*function setSchoolYear(schoolYearListParam){//获取学期数据的回调方法
		$scope.schoolYearList=schoolYearListParam;//学期数据复制
		$scope.queryObj.term=$scope.schoolYearList[0];//默认选中第一个
	}
			
	schoolYearService.getAllSchoolYear(setSchoolYear);//进页面默认调取schoolYearService的方法获取学期list
*/
  }]);