// 开启严格模式
'use strict';
app.controller('SchoolStatisticsDataListPageCtrl', ['$scope', '$http', '$state','$cookieStore','myCommonService','schoolYearService',//这里引入学期Service可以调用他的方法
                                      function($scope, $http, $state,$cookieStore ,myCommonService,schoolYearService) {
	
	$scope.itemNames = [{name:"年级"},{name: "班级"},{name: "学籍总人数"},{name:"已预约人数"},{name:"未预约人数"},{name:"已约满人数"},{name:"未约满人数"},{name:"未签到人数"},{name:"旷课人数"},{name:"请假人数"},{name:"未评价人数"},{name:"预约总数"},{name:"已完成人次"},{name:"已完成人数"},{name:"未完成人数"},{name:"老师未评价课程数"},{name:'操作'}];//列表页的表头
		
	$scope.repeatDone = function(){
	 		$('[data-toggle="tooltip"]').tooltip();
	 	};
 	$scope.queryObj = {//声明查询对象
			term:null,
			field:null,
			type:null,
			keyword:null
	};
	$scope.schoolYearList = [];
	 	
	$scope.areas = [{id:null,name:"请选择所属领域"}];
	
	$scope.types = [{type:'请选择课程类别',typeId:null},{type:'自主选课',typeId:1},{type:'送课到校',typeId:2}];
	
	
	$scope.showLabels = [ {
		name : '学籍总人数',
		value : '以北京市中小学学籍管理系统为依据的在籍学生总数。'
	}, {
		name : '已预约人数',
		value : '至少完成一次预约（含集体课或自选课）的人数。'
	}, {
		name : '未预约人数',
		value : '一次都未预约（含集体课或自选课）的人数。'
	}, {
		name : '已约满人数',
		value : '完成本学期5次预约的人数。'
	}, {
		name : '未约满人数',
		value : '未完成本学期5次预约的人数。'
	}, {
		name : '未签到人数',
		value : '机构还没有给学生签到的人数。'
	}, {
		name : ' 旷课人数',
		value : '已预约课程且在上课时间旷课的学生人数。'
	}, {
		name : '请假人数',
		value : '已预约课程且在上课时间请假的学生人数。'
	}, {
		name : '未评价人数',
		value : '机构完成考勤并上付任务单，学生未对课程进行评价的人数。'
	}, {
		name : '预约总数',
		value : '预约的总人次数（含集体课或自选课的）。'
	}, {
		name : '已完成人次',
		value : '机构已完成考勤、上传任务单、学生、老师已完成评价的人次数。'
	}, {
		name : '已完成人数',
		value : '全部完成本学期5次课程的学生人数。'
	}, {
		name : '未完成人数',
		value : '未完成本学期5次课程的学生人数。'
	}, {
		name : '老师未评价课程数',
		value : '老师未评价的课程（送课到校）个数'
	} ];
			
	
	$scope.dispanSelected = function(dict){
    	
    	if (dict == undefined){
    		return;
    	}else{
    		
    		$scope.selectGrade = dict;
    		$scope.selectClass = $scope.selectGrade.classes[0];

    	}    	
    };
	 	
	$scope.queryResult = 
	{
	  "content": [],
	  "describe": "string,描述",
	  "code": "integer,200"
	};
	
	
	/*
	 * 汇总对象
	 * */
	$scope.summarizeResult = {
			"zrsAll": 0,
	    	"yyrsAll": 0,
	    	"wyyrsAll": 0,
	    	"yymrsAll": 0,
	    	"wyymrsAll":0,
	    	"wqdrsAll": 0,
	    	"kkrsAll": 0,
	    	"qjrsAll": 0,
	    	"wpjrsAll": 0,
	    	"yyzsAll": 0,
	    	"wcrcAll": 0,
	    	"wcrsAll": 0,
	    	"wwcrsAll": 0,
	    	"twpjrsAll":0
		    };
	 	
	
	
		
	$scope.exportData = function(obj){
		

	};
	
	
	/*
	 *跳转到学籍总数列表 
	 * */
	$scope.goRollTotal = function(obj){
		$state.go('app.schoolstatisticsrolltotallist',{"gradeId":obj.grade_id,"classname":obj.classname,"term_id":$scope.queryObj.term.id});
	};
	
	/*
	 *跳转到 已预约人数
	 * */
	$scope.goOrderedDetail = function(obj){
		
		$state.go('app.schoolstatisticsorderedlist',{"gradeId":obj.grade_id,"classname":obj.classname,"term":$scope.queryObj.term.id});
	};
	
	/*
	 *跳转到 未满预约人数
	 * */
	$scope.goNotFullDetail = function(obj){
		
		$state.go('app.schoolstatisticsnotfulllist',{"gradeId":obj.grade_id,"classname":obj.classname,"term":$scope.queryObj.term.id});
	};
	
	/*
	 *跳转到 未签到人数
	 * */
	$scope.goNoSignDetail = function(obj){
		
		$state.go('app.schoolstatisticsnotsignlist',{"gradeId":obj.grade_id,"classname":obj.classname,"term":$scope.queryObj.term.id});
	};
	
	/*
	 *跳转到 旷课人数
	 * */
	$scope.goSkipClassesDetail = function(obj){
		
		$state.go('app.schoolstatisticsskipclasseslist',{"gradeId":obj.grade_id,"classname":obj.classname,"term":$scope.queryObj.term.id});
	};
	
	/*
	 *跳转到 请假人数
	 * */
	$scope.goAskLeaveDetail = function(obj){
		
		$state.go('app.schoolstatisticsaskleavelist',{"gradeId":obj.grade_id,"classname":obj.classname,"term":$scope.queryObj.term.id});
	};
	
	/*
	 *跳转到 未评价人数
	 * */
	$scope.goNotEvalauteDetail = function(obj){
		$state.go('app.schoolstatisticsnotevaluatelist',{"gradeId":obj.grade_id,"classname":obj.classname,"term":$scope.queryObj.term.id});
	};
	/*
	 *跳转到 未完成人数
	 * */
	$scope.goUncompletedDetail = function(obj){
		$state.go('app.schoolstatisticsuncompletedlist',{"gradeId":obj.grade_id,"classname":obj.classname,"term":$scope.queryObj.term.id});
	};
	
	/*
	 *跳转到 老师未评价人数
	 * */
	$scope.goTeacherNotEvalauteDetail = function(obj){

		$state.go('app.schoolstatisticsteachernotevaluatelist',{"gradeId":obj.grade_id,"classname":obj.classname,"term":$scope.queryObj.term.id});
	};
	

	/*
	 *跳转到查看详情
	 * */
	$scope.lookDetail = function(obj){
		
		$state.go('app.schoolstatisticsstucoursedetail',{"gradeId":obj.grade_id,"classname":obj.classname,"term":$scope.queryObj.term.id});//带参数跳转
	};
	
	function setSchoolYear(schoolYearParam){
		$scope.schoolYearList=schoolYearParam;
		//$scope.queryObj.term = $scope.schoolYearList[1];
		if($scope.queryObj.term == null){
			angular.forEach($scope.schoolYearList, function(schoolYear, key) {//循环获取到的学期放到学期list里
				if(schoolYear.if_start == 1){
					$scope.queryObj.term = schoolYear;
				}
			});
		}else{
			angular.forEach($scope.schoolYearList, function(schoolYear, key) {//循环获取到的学期放到学期list里
				if($scope.queryObj.term.id == schoolYear.id){
					$scope.queryObj.term = schoolYear;
				}
			});
		}
	}
	/*
	 * 用户对象
	 * */
	$scope.user = {
			"groupId": 0
		    };
	$scope.getUserGroup=function(){
    	var requestUrl = "/api/usermanager/get_user";

		$http({
			method  : 'POST',
			url     : requestUrl,
			/*data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
*/			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			$scope.user.groupId = data.groupId;
		}).error(function(data, status, headers, config) {
//				$scope.operaResult = data;
		});			 
	};
	$scope.getUserGroup();
	$scope.exportEveryClassStudentData=function(){
		/*var formData = {//声明用于传到后台的参数
//    			page:pageable,
    			keywords:$scope.queryObj.keyword,
    			gradeId:vm.selectGrade.id,
    			classname:classname
    	};*/
    	var requestUrl = "/api/usermanager/export_school_statistics_stu_course_detail";

		$http({
			method  : 'POST',
			url     : requestUrl,
			responseType: 'arraybuffer',
			/*data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
*/			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			saveAs(new Blob([data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;' }), decodeURI(headers()["filename"]));
		
		}).error(function(data, status, headers, config) {
//				$scope.operaResult = data;
		});			 
	};
	
	/*
	 * 查询列表
	 * */
	$scope.queryRecordPage = function(/*pageable*/){//获取列表数据的方法
		$scope.queryResult.content = null;
	    	var formData = {//声明用于传到后台的参数
	    			semester:$scope.queryObj.term == null?null:$scope.queryObj.term.id,
	    			field:$scope.queryObj.area == null ?null:$scope.queryObj.area.id,
	    			courseType:$scope.queryObj.type == null ?null:$scope.queryObj.type.typeId,
	    			keyword:$scope.queryObj.keyword,
	    			classname:null
	    	};
	    	var cookieObj ={"term":$scope.queryObj.term,"field":$scope.queryObj.field,"type":$scope.queryObj.type
	    			,"keyword":$scope.queryObj.keyword};
	    	var requestUrl = "/api/usermanager/school_statistics_data_list";
			$http({
				method  : 'POST',
				url     : requestUrl,
				data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				myCommonService.setMyCookie($cookieStore,"school_statistics_data_list",cookieObj);//把页面查询条件对象放到cookie
				$scope.queryResult.content = data;//分页对象赋值
				var total=data.length;
				$scope.summarizeResult = {
						"zrsAll": 0,
				    	"yyrsAll": 0,
				    	"wyyrsAll": 0,
				    	"yymrsAll": 0,
				    	"wyymrsAll":0,
				    	"wqdrsAll": 0,
				    	"kkrsAll": 0,
				    	"qjrsAll": 0,
				    	"wpjrsAll": 0,
				    	"yyzsAll": 0,
				    	"wcrcAll": 0,
				    	"wcrsAll": 0,
				    	"wwcrsAll": 0,
				    	"twpjrsAll":0
					    };
				//计算总和
				for (var i = total - 1; i >= 0; i--) {
					$scope.summarizeResult.zrsAll += parseInt(data[i]['zrs']);
					$scope.summarizeResult.yyrsAll += parseInt(data[i]['yyrs']);
					$scope.summarizeResult.wyyrsAll += parseInt(data[i]['wyyrs']);
					$scope.summarizeResult.yymrsAll += parseInt(data[i]['yymrs']);
					$scope.summarizeResult.wyymrsAll += parseInt(data[i]['wyymrs']);
					$scope.summarizeResult.wqdrsAll += parseInt(data[i]['wqdrs']);
					$scope.summarizeResult.kkrsAll += parseInt(data[i]['kkrs']);
					$scope.summarizeResult.qjrsAll += parseInt(data[i]['qjrs']);
					$scope.summarizeResult.wpjrsAll += parseInt(data[i]['wpjrs']);
					$scope.summarizeResult.yyzsAll += parseInt(data[i]['yyzs']);
					$scope.summarizeResult.wcrcAll += parseInt(data[i]['wcrc']);
					$scope.summarizeResult.wcrsAll += parseInt(data[i]['wcrs']);
					$scope.summarizeResult.wwcrsAll += parseInt(data[i]['wwcrs']);
					$scope.summarizeResult.twpjrsAll += parseInt(data[i]['twpjrs']);
				  }
			}).error(function(data, status, headers, config) {
//					$scope.operaResult = data;
			});			 
		};
			
		
		$scope.init=function(){
			var cookieParam=myCommonService.getMyCookie($cookieStore,"school_statistics_data_list");//从cookie里获取当前页面查询条件对象
			if(cookieParam!=null && cookieParam.term !=null ){
				$scope.queryObj.term = cookieParam.term;
			}else{
				$scope.queryObj.term = null;
			}
			schoolYearService.getAllSchoolYear(setSchoolYear);
			if(cookieParam!=null && cookieParam.field !=null ){
				$scope.queryObj.area = cookieParam.field;
			}else{
				$scope.queryObj.area = null;
			}
			if(cookieParam!=null && cookieParam.type !=null ){
				$scope.queryObj.type = cookieParam.type;
			}else{
				$scope.queryObj.type = $scope.types[0];
			}
			if(cookieParam!=null && cookieParam.keyword !=null ){
				$scope.queryObj.keyword = cookieParam.keyword;
			}else{
				$scope.queryObj.keyword = null;
			}
			var requestUrl = "/api/coursemanager/query_all_field";
			$http({
				method  : 'POST',
				url     : requestUrl,
				headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				$scope.areas = [{id:null,name:"请选择所属领域"}];
				angular.forEach(data, function(field, key) {
					$scope.areas.push(field);
				});
				if($scope.queryObj.area !=null){
					angular.forEach($scope.areas, function(field, key) {
						if($scope.queryObj.area.id == field.id){
							$scope.queryObj.area = field;
						}
					});
				}else{
					$scope.queryObj.area =$scope.areas[0];
				}
			}).error(function(data, status, headers, config) {
			});
			$scope.queryRecordPage();
		};
	
		$scope.init();
		//执行默认查询
		$scope.clearCookie=function(){//清除搜索条件
			myCommonService.clearMyCookie($cookieStore,"school_statistics_data_list");
			$scope.init();
		};
  }]);