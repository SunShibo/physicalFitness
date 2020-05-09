// 开启严格模式
'use strict';
app.controller('SchoolAssignTeacherPageCtrl', ['$scope', '$http', '$state','schoolYearService','$stateParams',
                                      function($scope, $http, $state ,schoolYearService,$stateParams) {

	
	$scope.obj = {name:'ckl'};
	
	$scope.dataRole = [];
	
	
	$scope.countryChanged = function(grade) {
		angular.forEach(grade.classList, function(classname,key) {// 点击一级自动选中所有下级
			classname.checked = grade.checked;
		});
	};
	
	$scope.provinceChanged = function(classname, grade) {//选择二级时自动处理一级
		var flag=false;
		angular.forEach(grade.classList,function(classname1,key){// 如果有任何一个子节点被选中，则让上级节点也选中
	    	if(classname1.checked){
	    		flag=true;
	    		grade.checked = true;
	    	}
	    });
	    if(!flag){// 如果所有子节点不选中，则上级也不选中
	    	grade.checked = false;
	    }
	};
	
	$scope.gradeList = [];
	
	function initPage(){
		var requestUrl="/api/schoolmanager/assign_teacher_grade_list";
		var formData={//提交的数据
			teacherId : $stateParams.teacherId
		};
		$http({
			method	: 'POST',
			url		: requestUrl,
			data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config){
			$scope.gradeList = data;
			angular.forEach($scope.gradeList, function(grade, key) {
				var count = 0;
				angular.forEach(grade.classList, function(classname, key) {
					if(classname.teacherHas == true){
						count ++;
						classname.checked = classname.teacherHas;
					}
				});
				grade.checked = count>0?true:false;
			});
		}).error(function(data, status, headers, config){
		});
	}
	initPage();
	
	$scope.teacherClassList =[];
	$scope.submit=function(){
		angular.forEach($scope.gradeList, function(grade, key) {
			if(grade.checked == true){
				angular.forEach(grade.classList, function(classname, key) {
					if(classname.checked == true){
						$scope.teacherClassObj = { grade:null, className:null };
						$scope.teacherClassObj.grade = grade.id;
						$scope.teacherClassObj.className = classname.classname;
						$scope.teacherClassList.push($scope.teacherClassObj);
					}
				});
			}
		});
		if($scope.teacherClassList.length <= 0){
			swal("请至少选择一个班级！");
			return;
		}
		var jsonStr = angular.toJson($scope.teacherClassList);
		var formData = {//声明用于传到后台的参数
			allIds : jsonStr,
			teacherId : $stateParams.teacherId    			
	    };
	    var requestUrl = "/api/schoolmanager/assignmentteacher_new";
		$http({
			method  : 'POST',
			url     : requestUrl,
			data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			if(data.result == "ok"){
				swal("分配成功!", "", "success");
				$scope.goBack();
			}else{
				swal("确认失败!", "", "error");
			}
		}).error(function(data, status, headers, config) {
		});	
	};
	
	  
	  //点击返回按钮
	  $scope.goBack = function() {
		$state.go('app.schoolteacherlist');
	};
	

  }]);