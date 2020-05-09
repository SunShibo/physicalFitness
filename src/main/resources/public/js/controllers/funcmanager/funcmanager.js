// 开启严格模式
'use strict';
app.controller('FuncManagerPageCtrl', ['$scope', '$http', '$state', '$sce', '$cookieStore','myCommonService',
                                      function($scope, $http, $state, $sce,$cookieStore,myCommonService) {
	
	 $scope.itemNames = [{name: "权限ID"},{name: "权限名称"},{name: "权限级别"},{name: "操作"}];//列表表头
	 
	 $scope.repeatDone = function(){//列表加载完成后加载操作栏
	 	$('[data-toggle="tooltip"]').tooltip();
	 };
	 
	 $scope.editFunc = function(funcId){//跳转编辑页面 带参数
		 $state.go('app.toeditfunc',{"funcId":funcId});
	};
		
	$scope.queryObj={searchName:null};
		//角色管理
	 $scope.queryFuncPage=function(pageable){//列表数据查询
	    	var formData = {//参数
	    			page:pageable,
	    			searchName:$scope.queryObj.searchName
	    	};
	    	var cookieObj={"page":pageable,"keyword":$scope.queryObj.searchName};//用于放到cookie的查询条件对象
	    	var requestUrl = "api/funcmanager/query_func_page";
			$http({
				method  : 'POST',
				url     : requestUrl,
				data    : $.param(formData),  // pass in data as strings
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				myCommonService.setMyCookie($cookieStore,"funcmanager",cookieObj);//把页面查询条件对象放到cookie
				$scope.results = data.content;//数据绑定到列表循环项上
				$scope.page = data;//绑定分页对象
			}).error(function(data, status, headers, config) {
			});
		};
		
		//点击创建权限
		$scope.goCreateFunc = function(){//跳转新增页面
			$state.go("app.createfunc");
		};
		
		function initPage(){
			var cookieParam=myCommonService.getMyCookie($cookieStore,"funcmanager");//从cookie里获取当前页面查询条件对象
			if(cookieParam!=null && cookieParam.keyword !=null){//用cookie中的页码去查询
				$scope.queryObj.searchName = cookieParam.keyword;
			}else{
				$scope.queryObj.searchName =null;
			}
			if(cookieParam!=null && cookieParam.page !=null){//用cookie中的页码去查询
				$scope.queryFuncPage(cookieParam.page);
			}else{
				$scope.queryFuncPage();
			}
		}
		initPage();
		
		$scope.clearCookie=function(){//清除搜索条件
			myCommonService.clearMyCookie($cookieStore,"funcmanager");
			initPage();
		};

  }]);