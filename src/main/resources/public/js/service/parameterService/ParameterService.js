'use strict';//参数公共Service
angular.module('app.parameterService',[])
	.factory('parameterService', function($http){
		return {
			getParameterByCode: function(paramCode,getBack){//获取参数值的方法（参数code,回调函数）
				var requestUrl = "/api/parameters/get_param_by_code";
				var formData={"paramCode":paramCode};
				$http({
					method  : 'POST',
					url     : requestUrl,
					data    : $.param(formData),
					headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }
				}).success(function(data, status, headers, config) {
					getBack(data.result);//获取到的数据放到回调函数
				}).error(function(data, status, headers, config) {
				});
			}
		};
		
	});