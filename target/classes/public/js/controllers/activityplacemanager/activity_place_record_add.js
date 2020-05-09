//开启严格模式
'use strict';

app.controller('ActivityPlaceRecordAddPageCtrl', ['$scope', '$http', '$state', '$sce','$window',
                                      function($scope, $http, $state, $sce,$window) {
		
	$scope.dropZoneOptions = {//上传配置
		maxFilesize:2,  //上传文件大小最大值 MB
		maxFiles:50,//最大上传文件个数
		addRemoveLinks: true,//删除链接
		dictFileTooBig: '正在上传的文件大小为：{{filesize}}MB，允许上传的文件最大不能超过：2MB。'
	 };
    
//	 var successUploadFiles = [];//成功上传的文件
	 var successUploadCQFiles=[];//产权图片list
	 var successUploadMJFiles=[];//面积图片list
	 var successUploadSBFiles=[];//设备图片list
	 
	 var successUploadFile = {//上传文件对象
			'file_timeStamp' : '',        //上传文件的时间戳, 如果时间戳相同则认为是同一个文件
			'file_uri' : ''     //文件上传后返回的相对地址，根据该地址可以获取到该文件
	 };

	$scope.callbacks ={//产权图片上传回调
		onFileUpload:function( file) {
			if (file.status == 'error'){//上传文件大小或类型不满足
				return;
			}
			var formData = new FormData();
			formData.append('file', file);
			formData.append('courseId', "address");
			$http({
				method:'POST',
				url:"/api/common/upload_inst_file",
				data: formData,
				headers: {'Content-Type':undefined},
				transformRequest: angular.identity
			}).success(function(data, status, headers, config) {
				successUploadFile.file_timeStamp = file.timeStamp;
				successUploadFile.file_uri = data.fileUri;
				successUploadCQFiles.push(successUploadFile);
				initSuccessUploadFile();
				//下发文件上传成功的事件
				$scope.$emit('dropZone:completed',file);
			}).error(function(data, status, headers, config) {
				
			});
		},
		removedFile:function(file){
			var currentSuccessUploadFile = ifSameFile(file.timeStamp,0);
			if (currentSuccessUploadFile != null){
				var fileUri = currentSuccessUploadFile.file_uri;
				//todo: 根据文件的相对地址删除文件
			}
			//删除成功后
			var index = successUploadFiles.indexOf(currentSuccessUploadFile);
		    if (index !== -1) {
		    	successUploadCQFiles.splice(index, 1);
		    }
		}
	};
	$scope.callbacks1 ={//面积图片上传回调
		onFileUpload:function( file) {
			if (file.status == 'error'){//上传文件大小或类型不满足
				return;
			}
			var formData = new FormData();
			formData.append('file', file);
			formData.append('courseId', "address");
			$http({
				method:'POST',
				url:"/api/common/upload_inst_file",
				data: formData,
				headers: {'Content-Type':undefined},
				transformRequest: angular.identity
			}).success(function(data, status, headers, config) {
				successUploadFile.file_timeStamp = file.timeStamp;
				successUploadFile.file_uri = data.fileUri;
				successUploadMJFiles.push(successUploadFile);
				initSuccessUploadFile();
				//下发文件上传成功的事件
				$scope.$emit('dropZone:completed',file);
			}).error(function(data, status, headers, config) {
					
			});
		},
		removedFile:function(file){
			var currentSuccessUploadFile = ifSameFile(file.timeStamp,1);
			if (currentSuccessUploadFile != null){
				var fileUri = currentSuccessUploadFile.file_uri;
				//todo: 根据文件的相对地址删除文件
			}
			//删除成功后
			var index = successUploadFiles.indexOf(currentSuccessUploadFile);
			if (index !== -1) {
				successUploadMJFiles.splice(index, 1);
			}
		}
	};
	$scope.callbacks2 ={//设备图片上传回调
		onFileUpload:function( file) {
			if (file.status == 'error'){//上传文件大小或类型不满足
				return;
			}
			var formData = new FormData();
			formData.append('file', file);
			formData.append('courseId', "address");
			$http({
				method:'POST',
				url:"/api/common/upload_inst_file",
				data: formData,
				headers: {'Content-Type':undefined},
				transformRequest: angular.identity
			}).success(function(data, status, headers, config) {
				successUploadFile.file_timeStamp = file.timeStamp;
				successUploadFile.file_uri = data.fileUri;
				successUploadSBFiles.push(successUploadFile);
				initSuccessUploadFile();
				//下发文件上传成功的事件
				$scope.$emit('dropZone:completed',file);
			}).error(function(data, status, headers, config) {
						
			});
		},
		removedFile:function(file){
			var currentSuccessUploadFile = ifSameFile(file.timeStamp,2);
			if (currentSuccessUploadFile != null){
				var fileUri = currentSuccessUploadFile.file_uri;
				//todo: 根据文件的相对地址删除文件
			}
			//删除成功后
			var index = successUploadFiles.indexOf(currentSuccessUploadFile);
			if (index !== -1) {
				successUploadSBFiles.splice(index, 1);
			}
		}
	};
	function initSuccessUploadFile(){
		successUploadFile = {
				'file_timeStamp' : '',        //上传文件的时间戳, 如果时间戳相同则认为是同一个文件
				'file_uri' : ''     //文件上传后返回的相对地址，根据该地址可以获取到该文件
		};
	}
	function ifSameFile(fileTimeStamp,index){
		var successUploadFiles=null;
		if(index == 0){
			successUploadFiles=successUploadCQFiles;
		}else if(index == 1){
			successUploadFiles=successUploadMJFiles;
		}else if(index == 2){
			successUploadFiles=successUploadSBFiles;
		}else{
			return null;
		}
		try{
			for (var i = 0; i < successUploadFiles.length; i++){
				var currentSuccessUploadFile = successUploadFiles[i];
				if (currentSuccessUploadFile.file_timeStamp == fileTimeStamp){
					return currentSuccessUploadFile;
				}
			}
		}catch(e){
			//不处理
		}
		return null;
	}
	//初始化
	function initSuccessUploadFiles(){
		successUploadCQFiles = [];
		successUploadMJFiles = [];
		successUploadSBFiles = [];
	}
	initSuccessUploadFiles();
	
	$scope.addressTypes=[{"id":null,"name":"请选择地址类型"}];//地址类型list
	$scope.towns=[{"id":null,"name":"请选择区县"}];//区县list
	$scope.propertyType=[{"id":1,"value":1,"name":"租赁"},{"id":2,"value":2,"name":"自有"}];//场地类型
	$scope.courseList=[];
	
	$scope.activityAddressObj = {
			addressTypeObj:$scope.addressTypes[0],//场地类型
			townObj:$scope.towns[0],//区县
			propertyTypeObj:$scope.propertyType[0],//产权类型
			detailSummary:null,//活动场地描述
			address:null,//关键地址
			site:null,//详细地址
//			siteType:null,//活动场地产权类型
			placeArea:null,//场地面积
			classroomArea:null,//具体活动教室(实验室)面积，对应以前字段classroom_ha
			maxNum:null,//最大接待人数
			contactName:null,//联系人
			contactPhone:null,//联系方式
			equipment:null,//通风设备
			lng:null,
			lat:null
	};
	
	$scope.validForm=function(){
		loadMap();
		$('#defaultForm').bootstrapValidator({
			message: 'This value is not valid',
			feedbackIcons: {
				valid: 'glyphicon glyphicon-ok',
				invalid: 'glyphicon',//glyphicon glyphicon-remove验证错误时输入框内需要显示红叉就加上
				validating: 'glyphicon glyphicon-refresh'
			},
			fields: {
				addressType: {
					group: '.col-lg-4',
					validators: {notEmpty: {message: '地址类型必选'}}
				},
				townId: {
					group: '.col-lg-4',
					validators: {notEmpty: {message: '地址所在区县必选'}}
				},
				addressKey: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '关键地址不能为空'},
						stringLength: {min: 1,max: 100,message: '关键地址必须大于1个字小于100个字'}
					}
				},
				addressDetailed: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '详细地址不能为空'},
						stringLength: {min: 1,max: 150,message: '详细地址必须大于1个字小于150个字'}
					}
				},
				detailSummary: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '场地描述不能为空'}
					}
				},
				propertyType: {
					group: '.col-lg-4',
					validators: {notEmpty: {message: '产权情况必选'}}
				},
				siteArea: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '场地总面积不能为空'},
						regexp: {regexp: /^\d+(\.\d{1,2})?$/,message: '只能输入数字(精确到小数点两位)'}
					}
				},
				classroomArea: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '实际教室面积不能为空'},
						regexp: {regexp: /^\d+(\.\d{1,2})?$/,message: '只能输入数字(精确到小数点两位)'}
					}
				},
				maxNum: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '接待最大人数不能为空'},
						regexp: {regexp: /^[0-9]*$/,message: '只能输入数字'}
					}
				},
				equipment: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '通风设备不能为空'}
					}
				},
				contactName: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '联系人不能为空'},
						stringLength: {min: 1,max: 10,message: '联系人必须大于1个字小于10个字'}
					}
				},
				contactPhone: {
					group: '.col-lg-4',
					validators: {
						notEmpty: {message: '联系电话不能为空'},
						regexp: {regexp: /(\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/,message: '请输入正确的联系电话(手机/固话)'}
					}
				},
				courses: {
					group: '.col-lg-4',
					validators: {
						choice: {
	                        min: 1,
	                        max: 20,
	                        message: '至少选择一项活动'
	                    }
					}
				}
			}
		});
		$('.validateBtn').click(function() {//保存按钮的点击事件
			$('#defaultForm').bootstrapValidator('validate');//验证表单
			var flag1=false;//地址类型
			var flag2=false;//区县id
			var flag3=false;//详细地址
			var flag4=false;//活动场地
			var flag6=false;//活动项目
			var flag5=false;//设备
			if($scope.activityAddressObj.addressTypeObj.id == null){
				$("#addressTypeDiv small").css("display","block");//错误时改变样式并显示
				$("#addressTypeDiv").addClass("has-error");
				$("#addressTypeDiv").addClass("has-feedback");
				flag1=false;
			}else{
				flag1=true;
				$("#addressTypeDiv small").css("display","none");//正确时隐藏样式
				$("#addressTypeDiv").removeClass("has-error");
				$("#addressTypeDiv").addClass("has-success");
			}
			if($scope.activityAddressObj.townObj.id == null){
				$("#townDiv small").css("display","block");//错误时改变样式并显示
				$("#townDiv").addClass("has-error");
				$("#townDiv").addClass("has-feedback");
				flag2=false;
			}else{
				flag2=true;
				$("#townDiv small").css("display","none");//正确时隐藏样式
				$("#townDiv").removeClass("has-error");
				$("#townDiv").addClass("has-success");
			}
			if($scope.activityAddressObj.site == null){
				$("#addressDetailedDiv small").css("display","block");//错误时改变样式并显示
				$("#addressDetailedDiv").addClass("has-error");
				$("#addressDetailedDiv").addClass("has-feedback");
				flag3=false;
			}else{
				flag3=true;
				$("#addressDetailedDiv small").css("display","none");//正确时隐藏样式
				$("#addressDetailedDiv").removeClass("has-error");
				$("#addressDetailedDiv").addClass("has-success");
			}
			if($scope.activityAddressObj.detailSummary == null){
				$("#detailSummaryDiv small").css("display","block");//错误时改变样式并显示
				$("#detailSummaryDiv").addClass("has-error");
				$("#detailSummaryDiv").addClass("has-feedback");
				flag4=false;
			}else{
				flag4=true;
				$("#detailSummaryDiv small").css("display","none");//正确时隐藏样式
				$("#detailSummaryDiv").removeClass("has-error");
				$("#detailSummaryDiv").addClass("has-success");
			}
			if($scope.activityAddressObj.equipment == null){
				$("#equipmentDiv small").css("display","block");//错误时改变样式并显示
				$("#equipmentDiv").addClass("has-error");
				$("#equipmentDiv").addClass("has-feedback");
				flag5=false;
			}else{
				flag5=true;
				$("#equipmentDiv small").css("display","none");//正确时隐藏样式
				$("#equipmentDiv").removeClass("has-error");
				$("#equipmentDiv").addClass("has-success");
			}
			var courseIds="";//用来存放选中的课程id
			angular.forEach($scope.courseList, function(couse, key) {
				if(couse.checked){//循环所有课程 将选中的放到课程id数组中
					courseIds+=couse.id+",";
				}
			});
			if(courseIds.length <=0){
				$("#courseIdDiv small").css("display","block");//错误时改变样式并显示
				$("#courseIdDiv").addClass("has-error");
				$("#courseIdDiv").addClass("has-feedback");
				swal("请选择在该地址上课的课程（如没有可选，请先申报新项目）");
				flag6=false;
			}else{
				flag6=true;
				$("#courseIdDiv small").css("display","none");//正确时隐藏样式
				$("#courseIdDiv").removeClass("has-error");
				$("#courseIdDiv").addClass("has-success");
			}
			
			var flag=$("#defaultForm").data('bootstrapValidator').isValid();//获取整个表单项的验证结果
			if(flag && flag1 && flag2 && flag3 && flag4 && flag5 && flag6){
				if(successUploadCQFiles.length <= 0){
					swal("请至少上传一张产权图片！");
					return;
				}
				if(successUploadMJFiles.length <= 0){
					swal("请至少上传一张面积图片！");
					return;
				}
				if(successUploadSBFiles.length <= 0){
					swal("请至少上传一张设备图片！");
					return;
				}
				var cqUrls="";
				angular.forEach(successUploadCQFiles, function(myfile, key) {
					cqUrls+=myfile.file_uri+"&%*";
				});
				var mjUrls="";
				angular.forEach(successUploadMJFiles, function(myfile, key) {
					mjUrls+=myfile.file_uri+"&%*";
				});
				var sbUrls="";
				angular.forEach(successUploadSBFiles, function(myfile, key) {
					sbUrls+=myfile.file_uri+"&%*";
				});
				var requestUrl = "/api/activityplacemanager/add_course_address";
				var formData = {
						lng:$scope.activityAddressObj.lng,
						lat:$scope.activityAddressObj.lat,
						cqUrls:cqUrls,
						mjUrls:mjUrls,
						sbUrls:sbUrls,
						courseIds : courseIds,
						addressTypeId : $scope.activityAddressObj.addressTypeObj.id,
						townId:$scope.activityAddressObj.townObj.id,
						addressKey:$scope.activityAddressObj.address,
						addressDetailed:$scope.activityAddressObj.detailSummary,
						site:$scope.activityAddressObj.site,
						siteType:$scope.activityAddressObj.propertyTypeObj.id,
						siteArea:$scope.activityAddressObj.placeArea,
						classroomArea:$scope.activityAddressObj.classroomArea,
						maxNum:$scope.activityAddressObj.maxNum,
						classroomRemark:$scope.activityAddressObj.equipment,
						contacts:$scope.activityAddressObj.contactName,
						contactsTel:$scope.activityAddressObj.contactPhone
					};
				$http({
					method	: 'POST',
					url		: requestUrl,
					data	: $.param(formData),
					headers : { 'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
				}).success(function(data, status, headers, config){
					if(data.result=="ok"){
						swal("保存成功!", "您新增的场地已经申请成功", "success");
						$scope.goback();
					}else{
						swal("添加失败!", "", "error");
					}
				}).error(function(data, status, headers, config){
					swal("添加失败!", "", "error");
				});
			}
		});
	  };
	
	function queryAllCourse(){
		var requestUrl = "/api/activityplacemanager/query_all_course";
		$http({
			method  : 'POST',
			url     : requestUrl,
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			$scope.courseList=data;
		}).error(function(data, status, headers, config) {
		});
	}
	
	function queryAllTown(){
		var requestUrl = "/api/activityplacemanager/query_add_address_town";
		$http({
			method  : 'POST',
			url     : requestUrl,
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			angular.forEach(data, function(groupObj, key) {//循环结果
			 	$scope.towns.push(groupObj);// 放到区县list
			});
		}).error(function(data, status, headers, config) {
		});
	}
	function queryAllAddressType(){
		var requestUrl = "/api/addressType/get_all_address_type";
		$http({
			method  : 'POST',
			url     : requestUrl,
			headers : { 'Content-Type': 'application/x-www-form-urlencoded'	 }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			angular.forEach(data, function(groupObj, key) {//循环结果
			 	$scope.addressTypes.push(groupObj);// 放到地址类型list
			});
		}).error(function(data, status, headers, config) {
		});
	}
	
	 
	$scope.goback=function(){
		$state.go("app.activityplacerecordlist");
	};
	
	queryAllCourse();
	queryAllTown();
	queryAllAddressType();
	
    function G(id) {
		return document.getElementById(id);
	}
    
    function loadMap(){
    	var map = new BMap.Map("allmap");
    	map.centerAndZoom("北京",12); 	// 初始化地图,设置城市和地图级别。
    	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
    	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
    	var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL});
    	map.addControl(top_left_control);
    	map.addControl(top_left_navigation);
    	map.addControl(top_right_navigation); 
    	var menu = new BMap.ContextMenu();
    	
    	var dizhi_lng=null;
    	var dizhi_lat=null;
    	var dizhi =null;
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
                        map.clearOverlays();
                        var new_point = new BMap.Point(dizhi_lng, dizhi_lat);
                        var marker = new BMap.Marker(new_point);  // 创建标注
                        map.addOverlay(marker);              // 将标注添加到地图中
                        map.panTo(new_point);
                        $('#address').val(dizhi);
                        $('#jzad').val(dizhi);
                        $scope.activityAddressObj.address=dizhi;
                        $scope.activityAddressObj.lng=dizhi_lng;
                        $scope.activityAddressObj.lat=dizhi_lat;
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
    		{"input" : "address"
    		,"location" : map
    	});

    	ac.addEventListener("onhighlight", function(e) {  //鼠标放在下拉列表上的事件
    		var str = "";
    		var _value = e.fromitem.value;
    		var value = "";
    		if (e.fromitem.index > -1) {
    			value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
    		}    
    		str = "FromItem<br />index = " + e.fromitem.index + "<br />value = " + value;
    		
    		value = "";
    		if (e.toitem.index > -1) {
    			_value = e.toitem.value;
    			value = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
    		}    
    		str += "<br />ToItem<br />index = " + e.toitem.index + "<br />value = " + value;
    		G("searchResultPanel").innerHTML = str;
    	});
    	var myValue;
    	ac.addEventListener("onconfirm", function(e) {    //鼠标点击下拉列表后的事件
    		var _value = e.item.value;
    		myValue = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
    		G("searchResultPanel").innerHTML ="onconfirm<br />index = " + e.item.index + "<br />myValue = " + myValue;
    		
    		setPlace(map,myValue);
    	});

    	
    }
	
    function setPlace(map,myValue){
    	
		map.clearOverlays();    //清除地图上所有覆盖物
		function myFun(){
			var pp = local.getResults().getPoi(0).point;    //获取第一个智能搜索的结果
			map.centerAndZoom(pp, 18);
			map.addOverlay(new BMap.Marker(pp));    //添加标注
			$scope.activityAddressObj.address = myValue;
			$scope.activityAddressObj.lng=pp.lng;
            $scope.activityAddressObj.lat=pp.lat;
		}
		var local = new BMap.LocalSearch(map, { //智能搜索
		  onSearchComplete: myFun
		});
		local.search(myValue);
	}
	
	 
  }]);
