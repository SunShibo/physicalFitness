// 开启严格模式
'use strict';
app.controller('RunTimeTaskPageCtrl', ['$scope', '$http', '$state', 
                                      function($scope, $http, $state) {

	$scope.repeatDone = function(){//列表操作栏按钮
	 	$('[data-toggle="tooltip"]').tooltip();
	};
	 	
	$scope.allUrl = [
	                 {"url":"/api/timetask/sms_student_notice"}
	                 ,{"url":"/api/timetask/miss_min_del_notice"}
	                 ,{"url":"/api/timetask/miss_min_del_school_notice"}
	                 ,{"url":"/api/timetask/no_sign_inst_notice"}
	                 ,{"url":"/api/timetask/no_sign_student_notice"}
	                 ,{"url":"/api/timetask/daily_statistics"}
	                 ,{"url":"/api/timetask/auto_set_retro_active"}
	                 ,{"url":"/api/timetask/miss_task_set_retro_active"}
	                 ,{"url":"/api/timetask/aotu_receive_update_apply"}
	                 ,{"url":"/api/timetask/aotu_receive_exit_apply"}
	                 ,{"url":"/api/timetask/aotu_reset_self_course"}
	                 ,{"url":"/api/timetask/aotu_reset_stu_hour"}
	                 ,{"url":"/api/timetask/aotu_ins_txl"}
	                 ,{"url":"/api/timetask/export_week_excel"}
	                 // ,{"url":"/api/timetask/auto_set_retro_active_absence"}
	                 ,{"url":"/api/timetask/sms_teacher_notice"}
					 ,{"url":"/api/timetask/sms_student_cancel_notice"}
					 ,{"url":"/api/timetask/sms_absence_teacher_notice"}
					 ,{"url":"/api/timetask/sms_evaluate_student_notice"}
		             ,{"url":"/api/timetask/auto_update_data_to_oracle"}
		             ,{"url":"/api/timetask/auto_update_course_info"}
		             ,{"url":"/api/timetask/auto_update_ajax_data"}
		             ,{"url":"/api/timetask/auto_update_classhour_video"}
		             ,{"url":"/api/timetask/auto_reset_redis_default"}
		             ,{"url":"/api/timetask/auto_reset_redis_default?type=0"}
		             ,{"url":"/api/timetask/auto_reset_redis_default?type=1"}
		             ,{"url":"/api/timetask/auto_reset_redis_default?type=2"}
		             ,{"url":"/api/timetask/auto_reset_redis_default?type=3"}
		             ,{"url":"/api/timetask/auto_reset_redis_default?type=4"}
		             ,{"url":"/api/timetask/auto_add_special_stu"}
		             ,{"url":"/api/timetask/auto_reset_city_date"}
		             ,{"url":"/api/timetask/auto_sync_super_visor"}
		             ,{"url":"/api/timetask/auto_supervise_classhour"}
		             ,{"url":"/api/timetask/course_average"}
		             ,{"url":"/api/timetask/auto_sync_browse"}
		             ];

	$scope.run=function(index){
		swal({
			title : "您是否确认要执行该任务？",
			text : "执行后将无法取消!",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : false,
			customClass : ""
		}, function() {
			$(".confirm").attr("disabled",true);
			$http({
				method  : 'POST',
				url     : $scope.allUrl[index].url,
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				if(data.result=="ok"){
					swal("执行成功!", "", "success");
				}else{
					swal("执行失败!", "", "error");
				}
			}).error(function(data, status, headers, config) {
			});	
		});
	};
	
	$scope.synchronousPhpData=function(){
		swal({
			title : "请确认是否开始执行同步？",
			text : "学期表(school_year)数据是否正常;各个学期分表是否已正确创建;确认无误后方可开始同步",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : false,
			customClass : ""
		}, function() {
			$(".confirm").attr("disabled",true);
			$http({
				method  : 'POST',
				url     : "/api/common/synchronous_php_data",
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				if(data.result=="ok"){
					swal("同步成功!", "", "success");
				}else{
					swal("同步失败!", "", "error");
				}
			}).error(function(data, status, headers, config) {
			});	
		});
	};
	
	$scope.refreshCache=function(){
		swal({
			title : "请确认是否刷新缓存？",
//			text : "学期表(school_year)数据是否正常;各个学期分表是否已正确创建;确认无误后方可开始同步",
			type : "warning",
			showCancelButton : true,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "确认",
			cancelButtonText : "取消",
			closeOnConfirm : false,
			customClass : ""
		}, function() {
			$(".confirm").attr("disabled",true);
			$http({
				method  : 'POST',
				url     : "/api/common/refresh_cache",
				headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
			}).success(function(data, status, headers, config) {
				if(data.result=="ok"){
					swal("刷新成功!", "", "success");
				}else{
					swal("刷新失败!", "", "error");
				}
			}).error(function(data, status, headers, config) {
			});	
		});
	};
	
  }]);