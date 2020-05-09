// 开启严格模式
'use strict';
app.controller('UserEditPageCtrl', ['$scope', '$http', '$state', '$sce','$stateParams',
                                      function($scope, $http, $state, $sce,$stateParams) {

	
	$scope.sexes  =  [{name:'男',value:1},{name:'女',value:2}];
	$scope.groups = [{id:null,name:'请选择用户组'}];//用户组
	$scope.towns=[{id:null,name:"请选择区县"}];//所有区县
	$scope.schools=[{id:null,name:"请选择学校"}];//选择的区县下的学校
	$scope.grades=[{id:null,name:"请选择年级"}];//年级
	$scope.userObj={
			userName:null,
			sex:$scope.sexes[0],
			showName:null,
			firstPassword:null,
			secondPassword:null,
			groupObj:$scope.groups[0],
			townObj:$scope.towns[0],
			schoolObj:$scope.schools[0],
			gradeObj:$scope.grades[0],
			idcardNo:null
		};
	 
	 function getEditObj(){
		 var requestUrl = "/api/usermanager/get_edit_user";
		 var formData = {
			userId :$stateParams.userId
		 };
		 $http({
			 method	: 'POST',
			 url		: requestUrl,
			 data	: $.param(formData),
			 headers : { 'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
		 }).success(function(data, status, headers, config){
			 $scope.userObj.userId=data.id;
			 if(data.sex=="女" ){
				 $scope.userObj.sex =$scope.sexes[1]; 
			 }else{
				 $scope.userObj.sex =$scope.sexes[0]; 
			 }
			 $scope.userObj.userName=data.username;
			 $scope.userObj.showName=data.showname;
			 $scope.userObj.idcardNo=data.cardno;
			 queryAllGroup(data.group_id);
			 queryAllTown(data.town_id);
			 queryAllGrade(data.grade_id);
			 if(data.town_id !=null){
				 $scope.changeTown(data.town_id,data.school_id);
			 }
		 }).error(function(data, status, headers, config){
		 }); 
	 }
	 
	 $scope.save=function(){
		 var requestUrl = "/api/usermanager/user_update";
		 var parameters = {
				semester :vm.selectTerm.id,
				parameterCode : vm.parameterCode,
				parameterName : vm.parametersNameValue,
				parameters : vm.parameters_value
		 };
		 var formData = {
				parameters : angular.toJson(parameters),//处理为JSON串后后台用String接收
		 };
		 $http({
			 method	: 'POST',
			 url		: requestUrl,
			 data	: $.param(formData),
			 headers : { 'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
		 }).success(function(data, status, headers, config){
			 swal("保存成功!", "您编辑的用户已经修改成功", "success");
			 $scope.goback();
		 }).error(function(data, status, headers, config){
			 swal("添加失败!", "", "error");
		 });
	 };
	 
		 
		$scope.changeTown=function(townId,userSchoolId){
			var requestUrl = "/api/usermanager/get_school";
			var formData={
				townId:townId
			};
			$http({
				method  : 'POST',
				url     : requestUrl,
				data	: $.param(formData),
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				angular.forEach(data, function(groupObj, key) {//循环结果
					$scope.schools.push(groupObj);// 放到学校list
					if(userSchoolId == groupObj.id){
						$scope.userObj.schoolObj=groupObj;
					}
				});
			}).error(function(data, status, headers, config) {
				$scope.operaResult = data;
			});
		};
		 
		function queryAllGroup(userGroupId){//获取所有用户组
			var requestUrl = "/api/usermanager/get_all_group";
			$http({
				method  : 'POST',
				url     : requestUrl,
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				angular.forEach(data, function(groupObj, key) {//循环结果
					$scope.groups.push(groupObj);// 放到用户组
					if(userGroupId == groupObj.id){
						$scope.userObj.groupObj = groupObj;
					}
				});
			}).error(function(data, status, headers, config) {
				$scope.operaResult = data;
			});
		}
		function queryAllTown(userTownId){
			var requestUrl = "/api/town/get_all_town_for_user";
			$http({
				method  : 'POST',
				url     : requestUrl,
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				angular.forEach(data, function(groupObj, key) {//循环结果
				 	$scope.towns.push(groupObj);// 放到区县list
				 	if(userTownId == groupObj.id){
				 		$scope.userObj.townObj=groupObj;
				 	}
				});
			}).error(function(data, status, headers, config) {
				$scope.operaResult = data;
			});
		}
		function queryAllGrade(userGradeId){
			var requestUrl = "/api/usermanager/get_all_grade";
			$http({
				method  : 'POST',
				url     : requestUrl,
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				angular.forEach(data, function(groupObj, key) {//循环结果
				 	$scope.grades.push(groupObj);// 放到年级list
				 	if(userGradeId == groupObj.id){
				 		$scope.userObj.gradeObj=groupObj;
				 	}
				});
			}).error(function(data, status, headers, config) {
				$scope.operaResult = data;
			});
		}
		
		
		$scope.validForm=function(){
			$('#defaultForm').bootstrapValidator({
				message: 'This value is not valid',
				feedbackIcons: {
					valid: 'glyphicon glyphicon-ok',
					invalid: 'glyphicon',//glyphicon glyphicon-remove验证错误时输入框内需要显示红叉就加上
					validating: 'glyphicon glyphicon-refresh'
				},
				fields: {
					showName: {
						group: '.col-lg-4',
						validators: {
							notEmpty: {message: '姓名必填'},
							stringLength: {min: 1,max: 255,message: '姓名必须大于1个字小于255个字'}
						}
					},
					firstPassword: {
						group: '.col-lg-4',
						validators: {
//							stringLength: {min: 6,max: 18,message: '密码必须大于6个字小于18个字'},
		                    regexp: {
		                        regexp: /^(?![0-9]+$)(?![a-zA-Z]+$)(?!([a-z0-9_\W])+$)(?![0-9A-Za-z]+$)(\S){6,20}$/,
		                        message: '密码由6至20位字母、数字、特殊字符随机组成，其中必须有一个大写字母和一个特殊字符'
		                    }
						}
					},
					secondPassword: {
						group: '.col-lg-4',
						validators: {
							identical: {field: 'firstPassword',message: '两次密码不一致'}
						}
					},
					userGroup:{
						group: '.col-lg-4',
						validators: {
							notEmpty: {message: '用户组必选'},
						}
					},
					gender:{
						group: '.col-lg-4',
						validators: {
		                    notEmpty: {message: '性别必选'}
		                }
					}
				}
			});
			$('#validateBtn').click(function() {//保存按钮的点击事件
				$('#defaultForm').bootstrapValidator('validate');//验证表单
				if($scope.userObj.groupObj.id == null){//验证uiselect
					$("#groupDiv small").css("display","block");//错误时改变样式并显示
					$("#groupDiv").addClass("has-error");
					$("#groupDiv").addClass("has-feedback");
				}else{
					$("#groupDiv small").css("display","none");//正确时隐藏样式
					$("#groupDiv").removeClass("has-error");
					$("#groupDiv").addClass("has-success");
					var flag = $("#defaultForm").data('bootstrapValidator').isValid();//获取整个表单项的验证结果
					if(flag){//验证通过
						var requestUrl = "/api/usermanager/user_update";
						var formData = {
								userId:$scope.userObj.userId,
								userName:$scope.userObj.userName,
								password:$scope.userObj.firstPassword,
								groupId:$scope.userObj.groupObj.id,
								showName:$scope.userObj.showName,
								sex:$scope.userObj.sex.value,
								townId:$scope.userObj.townObj.id,
								schoolId:$scope.userObj.schoolObj.id,
								gradeId:$scope.userObj.gradeObj.id,
								idcardNo:$scope.userObj.idcardNo,
							};
						$http({
							method	: 'POST',
							url		: requestUrl,
							data	: $.param(formData),
							headers : { 'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
						}).success(function(data, status, headers, config){
							if(data.result == "ok"){
								swal("修改成功!", "您编辑的用户已经修改成功", "success");
								$scope.goBack();
							}else{
								swal("修改失败!", "", "error");
							}
						}).error(function(data, status, headers, config){
							swal("修改失败!", "", "error");
						});
					}
				}
			});
		  };
		 
		 /*
		  * 返回用户页面
		  * */
		 $scope.goBack = function(){
			 $state.go("app.user");
		 };
		 
		getEditObj();
		 
	  }]);