'use strict';// 开启严格模式
app.controller('InstInfoEduExpCtrl', ['$scope', '$http', '$state', function ($scope, $http, $state) {

	$scope.inst = {
		edu_exp: ""
	};

	$scope.init = function () {

		var requestUrl = "/api/instmanager/query_inst_edu_exp";
		var formData = {};
		$http({
			method: 'POST',
			url: requestUrl,
			data: $.param(formData),
			headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		}).success(function (data) {
			$scope.inst.edu_exp = data['edu_exp'];
		}).error(function () {
		});
	};

	$scope.updateExp = function () {
		if($scope.inst.edu_exp.length == 0 || $scope.inst.edu_exp.trim().length == 0 || $scope.inst.edu_exp.length >150){
			swal("请输入正确的机构简介（长度150字以内）");
			return;
		}
		var requestUrl = "/api/instmanager/update_inst_edu_exp";
		var formData = {edu_exp:$scope.inst.edu_exp};
		$http({
			method: 'POST',
			url: requestUrl,
			data: $.param(formData),
			headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		}).success(function (data) {
			if(data>0){
				swal("修改成功！","","success");
			}else{
				swal("修改失败！","","error");
			}
		}).error(function () {
		});
	};

	$scope.init();
}]);