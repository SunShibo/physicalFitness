//开启严格模式
'use strict';

app.controller('ActivityPlaceEditPageCtrl', ['$scope', '$http', '$state', '$stateParams',
	function ($scope, $http, $state, $stateParams) {

		$scope.addressObj = {};

		$scope.positionAddress = function () {
			if($scope.addressObj.longitude=='' || $scope.addressObj.longitude==null || $scope.addressObj.latitude=='' || $scope.addressObj.latitude==null){
				swal("定位失败!","地址定位失败，请重新输入关键地址定位","error");
				return;
			}
			var requestUrl = "/api/activityplacemanager/position_course_address";
			var formData = {
				courseAddressId: $scope.addressObj.id,
				lng: $scope.addressObj.longitude,
				lat: $scope.addressObj.latitude,
				townId: $scope.addressObj.town,
				addressKey: $scope.addressObj.address_key
			};
			$http({
				method: 'POST',
				url: requestUrl,
				data: $.param(formData),
				headers: {'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
			}).success(function (data, status, headers, config) {
				if (data.result == "success") {
					swal("定位成功!","","success");
					$state.go("app.activityplacerecordlist");
				} else {
					swal("定位失败!","","error");
				}
			}).error(function (data, status, headers, config) {
				swal("修改失败!", "", "error");
			});
		};

		function getEditObj() {
			var requestUrl = "/api/activityplacemanager/get_edit_address";
			var formData = {
				addressId: $stateParams.placeId
			};
			$http({
				method: 'POST',
				url: requestUrl,
				data: $.param(formData),
				headers: {'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
			}).success(function (data, status, headers, config) {
				$scope.addressObj = data;
				loadMap();
			}).error(function (data, status, headers, config) {
			});
		}

		getEditObj();

		// $scope.goback=function(){
		// 	$state.go("app.activityplacerecordlist");
		// };

		function G(id) {
			return document.getElementById(id);
		}

		function loadMap() {
			var map = new BMap.Map("allmap");
			var new_point = new BMap.Point($scope.addressObj.longitude, $scope.addressObj.latitude);
			map.centerAndZoom(new_point, 12); 	// 初始化地图,设置城市和地图级别。
			var marker = new BMap.Marker(new_point);  // 创建标注
			map.addOverlay(marker);              // 将标注添加到地图中
			var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
			var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
			var top_right_navigation = new BMap.NavigationControl({
				anchor: BMAP_ANCHOR_TOP_RIGHT,
				type: BMAP_NAVIGATION_CONTROL_SMALL
			});
			map.addControl(top_left_control);
			map.addControl(top_left_navigation);
			map.addControl(top_right_navigation);
			var menu = new BMap.ContextMenu();

			var dizhi_lng = $scope.addressObj.longitude;
			var dizhi_lat = $scope.addressObj.latitude;
			var dizhi = null;
			map.addEventListener("rightclick", function (e) {
				dizhi_lng = e.point.lng;
				dizhi_lat = e.point.lat;

				var pt = e.point;
				var geoc = new BMap.Geocoder();
				geoc.getLocation(pt, function (rs) {
					var addComp = rs.addressComponents;
					dizhi = addComp.street + addComp.streetNumber;
				});
			});
			var txtMenuItem = [
				{
					text: '定位到该处',
					callback: function () {
						$('#address').val(dizhi);
						map.clearOverlays();
						var new_point = new BMap.Point(dizhi_lng, dizhi_lat);
						var marker = new BMap.Marker(new_point);  // 创建标注
						map.addOverlay(marker);              // 将标注添加到地图中
						map.panTo(new_point);
						$('#jzad').val(dizhi);
						$scope.addressObj.address_key = dizhi==''?$scope.addressObj.address_key:dizhi;
						$scope.addressObj.longitude = dizhi_lng;
						$scope.addressObj.latitude = dizhi_lat;
						$('#lng').val(dizhi_lng);
						$('#lat').val(dizhi_lat);
					}
				},
				{
					text: '放大',
					callback: function () {
						map.zoomIn();
					}
				},
				{
					text: '缩小',
					callback: function () {
						map.zoomOut();
					}
				}
			];
			for (var i = 0; i < txtMenuItem.length; i++) {
				menu.addItem(new BMap.MenuItem(txtMenuItem[i].text, txtMenuItem[i].callback, 100));
			}
			map.addContextMenu(menu);

			var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
				{
					"input": "address"
					, "location": map
				});

			ac.addEventListener("onhighlight", function (e) {  //鼠标放在下拉列表上的事件
				var str;
				var _value = e.fromitem.value;
				var value = "";
				if (e.fromitem.index > -1) {
					value = _value.province + _value.city + _value.district + _value.street + _value.business;
				}
				str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;

				value = "";
				if (e.toitem.index > -1) {
					_value = e.toitem.value;
					value = _value.province + _value.city + _value.district + _value.street + _value.business;
				}
				str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
				G("searchResultPanel").innerHTML = str;
			});
			var myValue;
			ac.addEventListener("onconfirm", function (e) {    //鼠标点击下拉列表后的事件
				var _value = e.item.value;
				myValue = _value.province + _value.city + _value.district + _value.street + _value.business;
				G("searchResultPanel").innerHTML = "onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;

				setPlace(map, myValue);
			});


		}

		function setPlace(map, myValue) {
			map.clearOverlays();    //清除地图上所有覆盖物
			function myFun() {
				var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
				map.centerAndZoom(pp, 18);
				map.addOverlay(new BMap.Marker(pp));    //添加标注
				$scope.addressObj.address_key = myValue;
				$scope.addressObj.longitude = pp.lng;
				$scope.addressObj.latitude = pp.lat;
			}

			var local = new BMap.LocalSearch(map, { //智能搜索
				onSearchComplete: myFun
			});
			local.search(myValue);
		}

	}]);
