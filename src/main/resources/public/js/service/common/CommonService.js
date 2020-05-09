'use strict';//存取删cookie
angular.module('app.myCommonService',[])
	.factory('myCommonService', function($http){
		var courseCache=null;
		return {
			setMyCookie: function(cookie,key,value){
				cookie.put(key,value);
			},
			getMyCookie: function(cookie,key){
				return cookie.get(key);
			},
			clearMyCookie: function(cookie,key){
				return cookie.put(key,null);
			},
			setCourseCache: function(value){
				courseCache=value;
			},
			getCourseCache: function(){
				return courseCache;
			},
			clearCourseCache: function(){
				return courseCache=null;
			}
		};
	});