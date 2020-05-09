'use strict';//学期公共Service
angular.module('app.schoolYearService',[])
	.factory('schoolYearService', function($http){
//		var allResult;
		return {
			getAllSchoolYear: function(getBack){//声明获取所有学期方法（参数为回调函数）
				var requestUrl = "/api/schoolyear/all_school_year";
				$http({
					method  : 'POST',
					url     : requestUrl,
					headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
				}).success(function(data, status, headers, config) {
					getBack(data);//获取到的数据放到回调函数
				}).error(function(data, status, headers, config) {
				});
			}
		};
		
	});