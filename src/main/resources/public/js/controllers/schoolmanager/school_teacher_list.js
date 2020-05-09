// 开启严格模式
'use strict';
app.controller('SchoolTeacherListPageCtrl', ['$scope', '$http', '$state',//这里引入学期Service可以调用他的方法
    function ($scope, $http, $state) {


        $scope.itemNames = [{name: "老师名称"}, {name: "工号"}, {name: "出生日期"}, {name: "状态"}, {name: '操作'}];//列表页的表头

        $scope.repeatDone = function () {
            $('[data-toggle="tooltip"]').tooltip();
        };


        $scope.terms = [{name: '请选择学期', termId: 0}, {name: '2016-2017年第一学期', termId: 1}, {
            name: '2016-2017年第二学期',
            termId: 2
        }];
        $scope.courses = [{name: '请选择课程名称', courseId: 0}, {name: '数学入门', courseId: 1}, {name: '语文入门', courseId: 2}];

        $scope.times = [{name: '请选择课程时间', timeId: 0}, {name: 1485493200000, timeId: 1}, {
            name: 1485493200000,
            timeId: 2
        }];


        $scope.queryResult = {
            "content": [],
            "describe": "string,描述",
            "code": "integer,200"
        };


        $scope.queryObj = {//声明查询对象
            teacherName: null
        };

        $scope.cancelRole = function (obj) {

            swal({
                title: "您是否确认要取消对该老师的授权吗？",
                text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                closeOnConfirm: false,
                customClass: ""
            }, function () {
				$(".confirm").attr("disabled",true);
                var formData = {//声明用于传到后台的参数
                    teacherId: obj.edu_id
                };
                var requestUrl = "/api/schoolmanager/deleteassignmentteacher";

                $http({
                    method: 'POST',
                    url: requestUrl,
                    data: $.param(formData),//将参数转换为param1=&param2=&param3=的形式
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
                }).success(function (data, status, headers, config) {
                    if (data.success == '1') {
                        swal("取消成功!", "您选择的老师授权已经取消成功!", "success");
                        $scope.queryRecordPage();
                    } else {
                        swal("取消失败!", "取消失败，请重新取消分配老师!", "error");
                    }
                }).error(function (data, status, headers, config) {
//					$scope.operaResult = data;
                });


            });
        };

        $scope.assignTeacher = function (obj) {

            $state.go('app.schoolassignteacher', {'teacherId': obj.edu_id});

        };


        /*
         * 查询教师列表
         * */
        $scope.queryRecordPage = function (pageable) {//获取列表数据的方法
            var formData = {//声明用于传到后台的参数
                page: pageable,
                teacherName: $scope.queryObj.teacherName
            };
            var requestUrl = "/api/schoolmanager/teacher_list";

            $http({
                method: 'POST',
                url: requestUrl,
                data: $.param(formData),//将参数转换为param1=&param2=&param3=的形式
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
            }).success(function (data, status, headers, config) {
                $scope.queryResult.content = data.content;//列表数据赋值
                $scope.page = data;//分页对象赋值
            }).error(function (data, status, headers, config) {
//					$scope.operaResult = data;
            });
        };
        //初始化查询
        $scope.queryRecordPage();
        $scope.reQueryRecordPage = function () {
            $scope.queryObj.teacherName = "";
            $scope.queryRecordPage();
        };

    }]);