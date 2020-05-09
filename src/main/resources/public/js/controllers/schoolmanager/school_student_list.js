// 开启严格模式
'use strict';
app.controller('SchoolStudentListPageCtrl', ['$scope', '$http', '$state','$location','$cookieStore','myCommonService',//这里引入学期Service可以调用他的方法
                                      function($scope, $http, $state,$location,$cookieStore,myCommonService) {
	
	var vm = $scope.vm  ={};
	
	$scope.itemNames = [{name: "年级"},{name: "班级"},{name: "姓名"},{name:"教育ID"},{name:'更新时间'}];//列表页的表头
		
	$scope.repeatDone = function(){
	 		$('[data-toggle="tooltip"]').tooltip();
	 	};
	 	
	 	
	 	
	vm.grades =[{grade:'请选择年级',id:null},{grade:'初一',id:21},{grade:'初二',id:22}];
	vm.classes =[{classname:'请选择班级',id:null}];
	//$scope.grades = [{grade:'一年级',classes:[{classname:'一班'},{classname:'二班'}]},{grade:'二年级',classes:[{classname:'1班'},{classname:'2班'}]}];
	
	vm.selectGrade = vm.grades[0];
	vm.selectClass = vm.classes[0];
	//检测值
	 $scope.$watch('vm.selectGrade',  function(newValue, oldValue) {
		 if(newValue!=null){
			 if(newValue.id==null){
				 vm.classes =[{classname:'请选择班级',id:null}];
				 vm.selectClass =vm.classes[0];
				 return;
			 }
			 //根据学年id，查询当前机构活动
			 var formData = {//声明用于传到后台的参数
		    			/*semester:$scope.queryObj.term.id,*/
					 gradeId:newValue.id
		    	};
		    	var requestUrl = "/api//selectedmanager/classname_by_gradeid";
				$http({
					method  : 'POST',
					url     : requestUrl,
					data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
					headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
				}).success(function(data, status, headers, config) {
                    vm.classes =[{classname:'请选择班级',id:null}];
                    vm.selectClass =vm.classes[0];
                        angular.forEach(data, function(classes, key) {
                            $scope.vm.classes.push(classes);
                        });
				}).error(function(data, status, headers, config) {
					
				});		
		 }
   });
	$scope.dispanSelected = function(dict){
    	
    	if (dict == undefined){
    		return;
    	}else{
    		
    		vm.selectGrade = dict;
    		vm.selectClass = vm.selectGrade.classes[0];

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
	
	$scope.cancelRole = function(obj) {
		
		swal({
			title : "您是否确认要取消对该老师的授权吗？",
			text : "",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : false,
			customClass : ""
		}, function() {
			
			swal("取消成功!", "您选择的老师授权已经取消成功!","success");
			
		});
	};
	
	$scope.assignTeacher = function(obj) {
		
		$state.go('app.schoolassignteacher',{'teacherId':obj.edu_id});
	};
	
	/*
	 * 查询学生列表
	 * */
	$scope.queryRecordPage = function(/*pageable*/){//获取列表数据的方法
		var reg = /^[\u4e00-\u9fa5a-zA-Z0-9]+$/;
		if($scope.queryObj.keyword!=null && $scope.queryObj.keyword.length>0 && !reg.test($scope.queryObj.keyword)){
			swal("只支持中文英文数字搜索！");
			return;
		}
			var classname="";
			if(vm.selectClass.classname!='请选择班级'){
				classname=vm.selectClass.classname;
			}
	    	var formData = {//声明用于传到后台的参数
//	    			page:pageable,
	    			keywords:$scope.queryObj.keyword,
	    			gradeId:vm.selectGrade.id,
	    			classname:classname
	    				
	    	};
	    	var cookieObj={"keywords":$scope.queryObj.keyword,"gradeId":vm.selectGrade,"classname":classname};//用于放到cookie的查询条件对象
	    	var requestUrl = "/api/usermanager/school_student_list";
	    	
			$http({
				method  : 'POST',
				url     : requestUrl,
				data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				myCommonService.setMyCookie($cookieStore,"school_student_list",cookieObj);//把页面查询条件对象放到cookie
				$scope.queryResult.content = data;//列表数据赋值
				$scope.page = data;//分页对象赋值
			}).error(function(data, status, headers, config) {
//					$scope.operaResult = data;
			});			 
		};
		//按条件导出数据
		$scope.exportData=function(){
			if($scope.vm.selectGrade==null){
				return false;
			}
			if($scope.vm.selectGrade.id==-1){
				return false;
			}
			var classname="";
			if(vm.selectClass.classname!='请选择班级'){
				classname=vm.selectClass.classname;
			}
	    	var formData = {//声明用于传到后台的参数
//	    			page:pageable,
	    			keywords:$scope.queryObj.keyword,
	    			gradeId:vm.selectGrade.id,
	    			classname:classname
	    	};
	    	var requestUrl = "/api/usermanager/export_school_student_list";

			$http({
				method  : 'POST',
				url     : requestUrl,
				responseType: 'arraybuffer',
				data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				saveAs(new Blob([data], { type: 'application/vnd.ms-excel' }), decodeURI(headers()["filename"]));
			
			}).error(function(data, status, headers, config) {
//					$scope.operaResult = data;
			});			 
		};
		$scope.init=function(){
			var cookieParam=myCommonService.getMyCookie($cookieStore,"school_student_list");//从cookie里获取当前页面查询条件对象
			if(cookieParam!=null && cookieParam.keywords !=null){//cookie中有值就赋给查询对象
				$scope.queryObj.keyword = cookieParam.keywords;
			}else{
				$scope.queryObj.keyword = null;
			}
			if(cookieParam!=null && cookieParam.classname !=null){//cookie中有值就赋给查询对象
				vm.selectClass.classname = cookieParam.classname;
			}else{
				vm.classes =[{classname:'请选择班级'}];
				vm.selectClass = vm.classes[0];
			}
			if(cookieParam!=null && cookieParam.gradeId !=null){//cookie中有值就赋给查询对象
				vm.selectGrade = cookieParam.gradeId;
			}else{
				vm.selectGrade = vm.grades[0];
			}
			
		};
		$scope.init();
		$scope.clearCookie=function(){//清除搜索条件
			myCommonService.clearMyCookie($cookieStore,"school_student_list");
			$scope.init();
		};

  }]);