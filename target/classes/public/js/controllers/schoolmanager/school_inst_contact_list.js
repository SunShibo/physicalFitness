// 开启严格模式
'use strict';
app.controller('SchoolInstContactListPageCtrl', ['$scope', '$http', '$state','$cookieStore','myCommonService',//这里引入学期Service可以调用他的方法
                                      function($scope, $http, $state,$cookieStore,myCommonService) {
	
	
	$scope.itemNames = [{name: "机构名称"},{name: "项目负责人"},{name:"项目负责人电话"},{name: "管理员电话"}];//列表页的表头
		
	$scope.repeatDone = function(){
	 		$('[data-toggle="tooltip"]').tooltip();
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
	 * 查询教师列表
	 * */
	$scope.queryRecordPage = function(pageable){//获取列表数据的方法
	    	var formData = {//声明用于传到后台的参数
	    			page:pageable,
	    			keywords:$scope.queryObj.keyword	    			
	    	};
	    	var requestUrl = "/api/instmanager/inst_contacts_list";
	    	var cookieObj={"keyword":$scope.queryObj.keyword};//用于放到cookie的查询条件对象
			$http({
				method  : 'POST',
				url     : requestUrl,
				data    : $.param(formData),//将参数转换为param1=&param2=&param3=的形式
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				myCommonService.setMyCookie($cookieStore,"school_inst_contact_list",cookieObj);//把页面查询条件对象放到cookie
				$scope.queryResult.content = data.content;//列表数据赋值
				$scope.page = data;//分页对象赋值
			}).error(function(data, status, headers, config) {
			});			 
		};
		
		
		$scope.init=function(){
			var cookieParam=myCommonService.getMyCookie($cookieStore,"school_inst_contact_list");//从cookie里获取当前页面查询条件对象
			if(cookieParam!=null && cookieParam.keyword !=null){//cookie中有值就赋给查询对象
				$scope.queryObj.keyword = cookieParam.keyword;
			}
			$scope.queryRecordPage();	
		};
		$scope.init();
		$scope.clearCookie=function(){//清除搜索条件
			myCommonService.clearMyCookie($cookieStore,"school_inst_contact_list");
			$scope.queryObj.keyword=null;
			$scope.init();
		};
	

  }]);