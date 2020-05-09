// 开启严格模式
'use strict';
app.controller('SchoolStatisticsUnfinishedDetailPageCtrl', ['$scope', '$http', '$state', 'schoolYearService','$stateParams',//这里引入学期Service可以调用他的方法
                                      function($scope, $http, $state,schoolYearService,$stateParams) {
	
	$scope.itemNames = [{name: "年级"},{name: "班级"},{name: "姓名"},{name: "性别"},{name: "教育id"},{name: "课程名称"},{name: "机构名称"},{name: "上课时间"},{name: "上课地点"},{name: "授课老师"},{name:"机构确认状态"},{name:"是否出勤"},{name:"任务单"},{name:"学生评价"},{name:"老师评价"},{name:"完成状态"},{name:"成绩"}];//列表页的表头
	
	 $scope.repeatDone = function(){
	 		$('[data-toggle="tooltip"]').tooltip();
	 	};
	 	
	 	
	 	
	 $scope.queryResult = {
				content : []
			};
	 
	$scope.queryRecordPage = function(pageable){//获取列表数据的方法
        var formData = {//声明用于传到后台的参数
            edu_id:$stateParams.eduId,
			term:$stateParams.term
        };
        var requestUrl = "/api/schoolstatisticsmanager/school_statistics_uncompleted_detail";
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
		
	/*
	 * 返回
	 * */	
	$scope.goBack = function(){
		if($stateParams.backType=='1'){
			$state.go('app.townstatisticsuncompletedlist',{
				schoolId:$stateParams.schoolId,
				schoolYear:$stateParams.semester,
				field:$stateParams.field,
				type:$stateParams.type,
				keyword:$stateParams.keyword});
		}else{

			$state.go('app.schoolstatisticsuncompletedlist',{"gradeId":$stateParams.gradeId,"classname":$stateParams.classname,"term":$stateParams.term});
			// $state.go('app.schoolstatisticsuncompletedlist',{
			// 	gradeId:$stateParams.gradeId,
			// 	classname:$stateParams.classname,
			// 	semester:$stateParams.semester,
			// 	field:$stateParams.field,
			// 	type:$stateParams.type,
			// 	keyword:$stateParams.keyword});
		}
		
	};		
	

  }]);