//开启严格模式
'use strict';
app.controller('ActivityPlaceRecordDetailPageCtrl', ['$scope', '$http', '$state','$stateParams','$uibModal',
                                      function($scope, $http, $state ,$stateParams,$uibModal) {

	$scope.obj= {name:'ckl'};
	
	$scope.propertyType=[{"id":1,"value":1,"name":"租赁"},{"id":2,"value":2,"name":"自有"}];//场地类型
	
	$scope.courseList=[];
	
	$scope.addressObj={
		
	};
	
	function queryAddressCourse(){
		var requestUrl="/api/activityplacemanager/get_address_course";
		var formData = {
				addressId :$stateParams.placeId
		};
		$http({
			method	: 'POST',
			url		: requestUrl,
			data	: $.param(formData),
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config){
			$scope.courseList=data;
		}).error(function(data, status, headers, config){
		});
	}
	
	function getEditObj(){
		var requestUrl="/api/activityplacemanager/get_edit_address";
		var formData = {
				addressId :$stateParams.placeId
		};
		$http({
			method	: 'POST',
			url		: requestUrl,
			data	: $.param(formData),
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config){
			$scope.addressObj=data;
			dealUrls();
			loadMap();
		}).error(function(data, status, headers, config){
		});
	}

	$scope.cqUrls=[];//产权图片urls
	$scope.mjUrls=[];
	$scope.sbUrls=[];
	function dealUrls(){
		var cqUrls=$scope.addressObj.site_photo.split("&%*");
		angular.forEach(cqUrls, function(obj, key) {
			if(obj.length>0){
				$scope.cqUrls.push(obj);
			}
		});
		var mjUrls=$scope.addressObj.class_room.split("&%*");
		angular.forEach(mjUrls, function(obj1, key) {
			if(obj1.length>0){
				$scope.mjUrls.push(obj1);
			}
		});
		var sbUrls=$scope.addressObj.remark.split("&%*");
		angular.forEach(sbUrls, function(obj2, key) {
			if(obj2.length>0){
				$scope.sbUrls.push(obj2);
			}
		});
	}
	
	$scope.showUrls=[];
	$scope.showPhotos=function(index){
		if(index == 1){
			$scope.showUrls=$scope.cqUrls;
		}else if(index == 2 ){
			$scope.showUrls=$scope.mjUrls;
		}else if(index == 3 ){
			$scope.showUrls=$scope.sbUrls;
		}else{
			$scope.showUrls=null;
		}
		var modalInstance = $uibModal.open({
				templateUrl: "/showphotos",//后台url
	    		controller: 'ShowPhotosModalPageCtrl',//controllerjs
	    		scope: $scope,//父页面$scope对象
	    		keyboard: false
		    });
		modalInstance.result.then(function(result){
    		
			
    	});
	};
	
	$scope.goBack= function(){
		$state.go('app.activityplacerecordlist');
	};

	
	getEditObj();
	queryAddressCourse();
	
	function loadMap(){
    	var map = new BMap.Map("allmap");
    	var new_point = new BMap.Point($scope.addressObj.longitude, $scope.addressObj.latitude);
    	map.centerAndZoom(new_point,12); 	// 初始化地图,设置城市和地图级别。
    	var marker = new BMap.Marker(new_point);  // 创建标注
    	map.addOverlay(marker);              // 将标注添加到地图中
    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    	var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL});
    	map.addControl(top_left_control);
    	map.addControl(top_left_navigation);
    	map.addControl(top_right_navigation); 
    }
	
    function setPlace(map,myValue){
		map.clearOverlays();    //清除地图上所有覆盖物
		function myFun(){
			var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
			map.centerAndZoom(pp, 18);
			map.addOverlay(new BMap.Marker(pp));    //添加标注
		}
		var local = new BMap.LocalSearch(map, { //智能搜索
		  onSearchComplete: myFun
		});
		local.search(myValue);
	}

  }]);