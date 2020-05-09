// 开启严格模式
'use strict';
app.controller('ParamterListPageCtrl', ['$scope', '$http', '$state', '$cookieStore','schoolYearService','myCommonService',//这里引入学期Service可以调用他的方法
                                      function($scope, $http, $state,$cookieStore,schoolYearService,myCommonService) {

	$scope.dateOptions = {
	        formatYear: 'yy',
	        startingDay: 1,
	        class: 'datepicker'
	      };
	 
	$scope.itemNames = [{name: "所属学期"},{name: "参数简称"},{name: "参数名称"},{name:"参数"},{name:'操作'}];//列表页的表头
	 
	$scope.paramterQueryResult = {//列表数据
		paramtersList:[]
	};
	
	$scope.repeatDone = function(){//列表操作栏按钮
	 	$('[data-toggle="tooltip"]').tooltip();
	};
	 	
	$scope.queryObj = {//声明查询对象
		term:{},
	 	searchName:null
	};
	
	/*
	 * 跳转添加
	**/
	$scope.addParamter = function(){
	 	$state.go('app.paramteradd');
	};
	/*
	 * 编辑带参数
	**/
	$scope.editParamter = function(paramterId){
	 	$state.go('app.paramteredit',{"paramterId":paramterId});//带参数跳转
	};
	/*
	 * 删除参数
	 * */
	$scope.deleteParamter = function(paramterId){
		swal({
			title : "您是否确认要删除该参数？",
			text : "删除后将无法恢复!",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : false,
			customClass : ""
		}, function() {
			$(".confirm").attr("disabled",true);
			var formData = {//参数
					parameterId:paramterId
		    };
			var requestUrl="/api/parameters/parameters_del";
			$http({
				method  : 'POST',
				url     : requestUrl,
				data    : $.param(formData),  // pass in data as strings
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				if(data.result=="ok"){
					swal("删除成功!", "您选择的参数已经删除.", "success");
					schoolYearService.getAllSchoolYear(setSchoolYear);
				}else{
					swal("删除失败!", "", "error");
				}
			}).error(function(data, status, headers, config) {
			});	
		});
	};
	$scope.queryParamterPage = function(pageable){//获取列表数据的方法
		var formData = {//声明用于传到后台的参数
				page:pageable,
		    	schoolYearId:$scope.queryObj.term==null?null:$scope.queryObj.term.id,
		    	searchName:$scope.queryObj.searchName
		};
		var cookieObj={"page":pageable,"term":$scope.queryObj.term,"keyword":$scope.queryObj.searchName};//用于放到cookie的查询条件对象
		var requestUrl = "/api/parameters/parameters_list";
		$http({
			method  : 'POST',
			url     : requestUrl,
			data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			myCommonService.setMyCookie($cookieStore,"parameterlist",cookieObj);//把页面查询条件对象放到cookie
			$scope.paramterQueryResult.paramtersList= data.content;//列表数据赋值
			$scope.page = data;//分页对象赋值
		}).error(function(data, status, headers, config) {
		});			 
	};
	
	schoolYearService.getAllSchoolYear(setSchoolYear);//进页面默认调取schoolYearService的方法获取学期list
	function setSchoolYear(schoolYearListParam){//获取学期数据的回调方法
		$scope.schoolYearList=[{"id":null,"name":"请选择学期"}];
		var cookieParam=myCommonService.getMyCookie($cookieStore,"parameterlist");//从cookie里获取当前页面查询条件对象
		$scope.queryObj.term=$scope.schoolYearList[0];//默认选中第一个学期
		angular.forEach(schoolYearListParam, function(schoolYear, key) {//循环获取到的学期放到学期list里
			$scope.schoolYearList.push(schoolYear);
		});
		if(cookieParam!=null && cookieParam.term !=null){//cookie中有学期选择值就选中该值
			angular.forEach($scope.schoolYearList, function(schoolYear, key) {
				if(cookieParam.term.id == schoolYear.id){
					$scope.queryObj.term = schoolYear;
				}
			});
		}
		if(cookieParam!=null && cookieParam.keyword !=null){//cookie中有值就赋给查询对象
			$scope.queryObj.searchName = cookieParam.keyword;
		}else{
			$scope.queryObj.searchName = null;
		}
		if(cookieParam!=null && cookieParam.page !=null){//用cookie中的页码去查询
			$scope.queryParamterPage(cookieParam.page);
		}else{
			$scope.queryParamterPage();
		}
	}
	
	$scope.clearCookie=function(){//清除搜索条件
		myCommonService.clearMyCookie($cookieStore,"parameterlist");
		schoolYearService.getAllSchoolYear(setSchoolYear);
	};
  }]);