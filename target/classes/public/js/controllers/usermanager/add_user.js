// 开启严格模式
'use strict';
app.controller('UserAddPageCtrl', ['$scope', '$http', '$state', '$sce',
                                      function($scope, $http, $state, $sce ) {
	
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
	
	$scope.changeTown=function(townId){
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
			$scope.schools=[{id:null,name:"请选择学校"}];
			angular.forEach(data, function(groupObj, key) {//循环结果
				$scope.schools.push(groupObj);// 放到学校list
			});
		}).error(function(data, status, headers, config) {
		});
	};
	 
	function queryAllGroup(){//获取所有用户组
		var requestUrl = "/api/usermanager/get_all_group";
		$http({
			method  : 'POST',
			url     : requestUrl,
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			angular.forEach(data, function(groupObj, key) {//循环结果
				$scope.groups.push(groupObj);// 放到用户组
			});
		}).error(function(data, status, headers, config) {
			$scope.operaResult = data;
		});
	}
	function queryAllTown(){
		var requestUrl = "/api/town/get_all_town_for_user";
		$http({
			method  : 'POST',
			url     : requestUrl,
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			angular.forEach(data, function(groupObj, key) {//循环结果
			 	$scope.towns.push(groupObj);// 放到区县list
			});
		}).error(function(data, status, headers, config) {
			$scope.operaResult = data;
		});
	}
	function queryAllGrade(){
		var requestUrl = "/api/usermanager/get_all_grade";
		$http({
			method  : 'POST',
			url     : requestUrl,
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			angular.forEach(data, function(groupObj, key) {//循环结果
			 	$scope.grades.push(groupObj);// 放到年级list
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
				userName: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '用户名必填'},
						stringLength: {min: 6,max: 18,message: '用户名必须大于6个字小于18个字'},
						regexp: {regexp: /^[A-Za-z0-9]{6,18}$/,message: '用户名只能包含数字,字母'},
						remote: {
							type: 'POST',url: '/api/usermanager/check_user_name',delay :  1000,//延迟2秒的值写到公共服务里
							message: '用户名已经存在'
						}
					}
				},
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
						notEmpty: {message: '密码不能为空'},
						regexp: {
	                        regexp: /^(?![0-9]+$)(?![a-zA-Z]+$)(?!([a-z0-9_\W])+$)(?![0-9A-Za-z]+$)(\S){6,20}$/,
	                        message: '密码由6至20位字母、数字、特殊字符随机组成，其中必须有一个大写字母和一个特殊字符'
	                    }
					}
				},
				secondPassword: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '确认密码不能为空'},
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
					var requestUrl = "/api/usermanager/user_add";
					var formData = {
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
							swal("保存成功!", "您新建的用户已经添加成功", "success");
							$scope.goBack();
						}else{
							swal("添加失败!", "", "error");
						}
					}).error(function(data, status, headers, config){
						swal("添加失败!", "", "error");
					});
				}else{
					var invalidArr = $("#defaultForm").data('bootstrapValidator').getInvalidFields();
					invalidArr.focus();
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
	 
	 queryAllGrade();
	 queryAllTown();
	 queryAllGroup();
	 
  }]);