// 开启严格模式
'use strict';
app.controller('CreateFuncCtrl', ['$scope', '$http', '$state', '$sce',
                                      function($scope, $http, $state, $sce) {
	
	$scope.levelSelect=[{"id":1,"name":"一级菜单"},{"id":2,"name":"二级菜单"}];//菜单级别选项
	
	$scope.funcObj ={//定义大对象包含各种小对象-避免一些意想不到的错误 -.-
		levelSelectItem:$scope.levelSelect[0],//菜单级别选择值-默认一级菜单
		level1FuncList:[],//一级菜单list
		level1FuncListItem:null,//一级菜单选择值
		funcId:null,//权限id
		funcName:null,//权限名称
		funcShortNam:null,//权限简称
		level1Show:false//一级菜单选择项是否显示 菜单级别选择二级菜单时 将这个值改为true 显示一级菜单选择项
	};
	
	$scope.changeLevel=function(itemId){//切换菜单级别的方法
		if(itemId == 2){//选择二级菜单时 显示一级菜单选择项 
			$scope.funcObj.level1Show=true;
			queryLevel1Func(0);//获取一级菜单的方法 传0默认选中第一项
		}else{
			$scope.funcObj.level1Show=false;//其他情况不显示一级菜单选择项
			$scope.funcObj.level1FuncListItem=null;
		}
	};
	
	function queryLevel1Func(funcParentId){//获取一级菜单方法
		var requestUrl="/api/funcmanager/query_func_level1";
		$http({
			method  : 'POST',
			url     : requestUrl,
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			$scope.funcObj.level1FuncList = data;//把值绑定给一级菜单列表
			if(funcParentId == 0 ){
				if($scope.funcObj.level1FuncListItem != null ){
					angular.forEach($scope.funcObj.level1FuncList, function(level1Func, key) {
						if(level1Func.id == $scope.funcObj.level1FuncListItem.id){
							$scope.funcObj.level1FuncListItem=level1Func;
						}
					});
				}else{
					$scope.funcObj.level1FuncListItem=$scope.funcObj.level1FuncList[0];
				}
			}else{
				angular.forEach(data, function(level1Func,index,array){//循环一级菜单结果集 
					if(funcParentId == level1Func.id){
						$scope.funcObj.level1FuncListItem=level1Func;//选中与参数相等的一级菜单
					}
				});
			}
		}).error(function(data, status, headers, config) {
			
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
				funcId: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '菜单ID不能为空'},
						stringLength: {min: 1,max: 11,message: '参数名称必须大于1个字小于11个字'},
						remote:{
							type: 'POST',url: '/api/funcmanager/check_func_id',delay :  1000,//延迟2秒的值写到公共服务里
	                        data:{"funcOldId":null,"funcId":$scope.funcObj.funcId},message: '菜单ID已经存在'
	                     }
					}
				},
				funcName: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '菜单名称不能为空'},
						stringLength: {min: 1,max: 255,message: '参数名称必须大于1个字小于255个字'}
					}
				},
				funcShortName: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '菜单简称不能为空'},
						stringLength: {min: 1,max: 255,message: '参数名称必须大于1个字小于255个字'}
					}
				},
				funcLevel: {
					group: '.col-lg-4',
					validators: {notEmpty: {message: '菜单级别必选'}}
				},
				parentFunc: {
					group: '.col-lg-4',
					validators: {notEmpty: {message: '父级菜单必选'}}
				}
			}
		});
		$('#validateBtn').click(function() {//保存按钮的点击事件
			$('#defaultForm').bootstrapValidator('validate');//验证表单
			var flag = $("#defaultForm").data('bootstrapValidator').isValid();//获取整个表单项的验证结果
			if(flag){//验证通过
				var requestUrl="/api/funcmanager/add_func";
				var dmRole = {//用于提交的对象
						id :$scope.funcObj.funcId,
						name : $scope.funcObj.funcName,
						roletypeId : $scope.funcObj.level1FuncListItem==null?null:$scope.funcObj.level1FuncListItem.id,
						role : $scope.funcObj.funcShortNam
				};
				var formData = {
						dmRole : angular.toJson(dmRole),//处理为JSON串后后台用String接收
				};
				$http({
					method	: 'POST',
					url		: requestUrl,
					data	: $.param(formData),
					headers : { 'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
				}).success(function(data, status, headers, config){
					swal("保存成功!", "您新建的权限已经添加成功", "success");
					$scope.goback();//跳转到列表
				}).error(function(data, status, headers, config){
					swal("添加失败!", "", "error");
				});
			}
		});
	  };
	  
	
	$scope.goback=function(){
		$state.go("app.funcmanager");
	};
	 
	
  }]);