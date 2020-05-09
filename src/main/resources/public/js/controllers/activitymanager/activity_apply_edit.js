//开启严格模式
'use strict';

app.controller('ActivityApplyEditPageCtrl', ['$scope', '$http', '$state', '$sce','$window','$stateParams',
                                      function($scope, $http, $state, $sce,$window,$stateParams) {
		
	$scope.obj={name:'ckl',area:null,activity:null};
	
	$scope.isShow = {value:false};
	
	$scope.saveObj = {
			subject_id:null,//学科
			area:null,//领域
			institution:null,//机构id
			studentNum:null,//每次接待人数
			post1:null,//附件二封面模板
			xzdw:null,//协作单位 
			field:null, //所属领域（对应Field的id）
			grade_id:null, //年级ID
			activity:null,//活动形式
			name:null,//项目名称
			number:null,//项目编号（唯一）
			hdmb:null,//活动目的
			hdnr:null,//活动内容
			zd:null,//重点
			nd:null,//难点
			xqd:null,//兴趣点
			online_at:null,//活动地点,
			sxtj:null,//条件与设施
			cl:null,//材料
			laoshi:null,//指导人员情况
			qt :null,//阶段 
			qtname :null//阶段内容 
	};
	
/*	
	
	 * 阶段
	 * 
	$scope.steps =[{name:'方法'}];
	
	
	 * 添加步骤
	 * 
	$scope.addStep = function(){
		
		$scope.steps.push({name:'heheh'});
	};
	
	
	 * 删除步骤
	 * 
	
	$scope.removeStep= function(index){
		
		$scope.steps.splice(index,1);
	};*/
	
	$scope.goBack = function(){//跳转回参数列表页
		$state.go("app.activityapplylist");
	};
	
	
	$scope.areas = [{name:'数据信息',id:1},{name:'自然与环境',id:2},{name:'电子与控制',id:3},{name:'其他',id:0}];
	$scope.grades = [{name:'初一',id:"21"},{name:'初二',id:'22'}];
	
	//涉及学科  物理化学生物
	$scope.subjects = [];
	//活动形式
	$scope.activities = [{name:'实地考察',id:'实地考察'},{name:'实验探究',id:'实验探究'},{name:'动手制作',id:'动手制作'},{name:'实践体验',id:'实践体验'}];
	 		
	 //$scope.selected = $scope.oneObj.selectEles[1];
	 $scope.initMultiSelect = function(){
		 $(".select2").select2();
	 };
	 $scope.saveObj.area={};    
    $scope.multipleArea = {};
    $scope.multipleArea.colors = ['Blue','Red'];
    
    $scope.multipleActivity = {};
    $scope.multipleSubject = {};
    $scope.multipleActivity.colors = ['Blue','Red'];
    
  //获取所有的所属领域
	$scope.init = function() {
		var requestUrl = "/api/coursemanager/query_all_field";
		var requestUrl2 = "/api/coursemanager/query_all_subject";
		
		$http({
			method  : 'POST',
			url     : requestUrl,
		/*	data    : $.param(formData),  // pass in data as strings
*/			headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			//id,realname,username,password,gender,birthday,company,post,title,occupation,uuid,telphone,instid,idcardphoto,is_del
			$scope.areas=data;
		}).error(function(data, status, headers, config) {
		});
		$http({
			method  : 'POST',
			url     : requestUrl2,
			/*	data    : $.param(formData),  // pass in data as strings
			 */			headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			//id,realname,username,password,gender,birthday,company,post,title,occupation,uuid,telphone,instid,idcardphoto,is_del
			$scope.subjects=data;
			$scope.edit();
		}).error(function(data, status, headers, config) {
		});
		
	};
	//初始化
	$scope.init();
	
	
    $scope.edit = function() {
		 var requestUrl = "/api/coursemanager/query_by_id";
		 var formData = {
					id :$stateParams.id
			};
	   	  $http({
	               method  : 'POST',
	               url     : requestUrl,
	               data    : $.param(formData),  // pass in data as strings
	               headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
	           }).success(function(data, status, headers, config) {
	        	   //subject id处理
	        	  //存放需要显示的元素数组
	        	  var subjectArrV=[];
	        	  var subjectIds=data.subject_id;

	        	  if(subjectIds.length>0){
	        		  var subjectArr=subjectIds.split(',');
	        		
	        		  $.each(subjectArr, function(index1,val1) {
	        			  $.each($scope.subjects, function(index2,val2) {
	      	          		if(val1==val2.id){
	      	          		subjectArrV.push($scope.subjects[index2]);
	      	          		}
	      	          		
	      	          	});
	  	          		
	  	          	});
	        	  }
	        	  $scope.multipleSubject.selectedSubjectWithGroupBy=subjectArrV;
	        	  //存放需要显示的元素数组
	        	  var activityArrV=[];
	        	  var activityIds=data.activity;
	        	  if(activityIds.length>0){
	        		  var activityArr=activityIds.split(',');
	        		  $.each(activityArr, function(index1,val1) {
	        			  $.each($scope.activities, function(index2,val2) {
	        				  if(val1==val2.name){
	        					  activityArrV.push($scope.activities[index2]);
	        				  }
	        				  
	        			  });
	        			  
	        		  });
	        	  }
	        	  $scope.multipleActivity.selectedActivityWithGroupBy=activityArrV;
	        	   $scope.saveObj.id=data.id;
	        	   $scope.saveObj.name=data.name;
	        	   $scope.saveObj.courses_cover=data.courses_cover;
	        	   $scope.saveObj.attachmenta=data.attachmenta;
	        	   $scope.saveObj.attachmentb=data.attachmentb;
	        	   $scope.saveObj.vido=data.vido;
	        	   
	        	   $scope.saveObj.publicity_pic_one=data.publicity_pic_one;
	        	   $scope.saveObj.publicity_pic_two=data.publicity_pic_two;
	        	   $scope.saveObj.intro=data.intro;
	        	   //领域的处理
	        	   $.each($scope.areas, function(index,val) {
	        			 if(data.field==val.id){
	        				 $scope.saveObj.area=$scope.areas[index];
	        			 }else if(data.field>7&&val.name=='其他'){
	        				 $scope.saveObj.area=$scope.areas[index];
	        			 }
	        			  
	        		  });
	        	   //处理接待对象saveObj.grade
	        	   $.each($scope.grades, function(index,val) {
	        			 if(data.grade_id==val.id){
	        				 $scope.saveObj.grade=$scope.grades[index];
	        			 }
	        			  
	        		  });
	        	   $scope.saveObj.studentNum=data.studentNum;
	        	   $scope.saveObj.post1=data.post1;
	        	   $scope.saveObj.xzdw=data.xzdw;//协作单位
	        	   $scope.saveObj.hdmb=data.hdmb;//活动目标
	        	   $scope.saveObj.hdnr=data.hdnr;// 活动内容
	        	   $scope.saveObj.zd=data.zd; //重点
	        	   $scope.saveObj.nd=data.nd;// 难点
	        	   $scope.saveObj.xqd=data.xqd;//兴趣点
	        	   $scope.saveObj.online_at=data.online_at;//活动地点
	        	   $scope.saveObj.sxtj=data.sxtj;//条件与设施
	        	   $scope.saveObj.cl=data.cl;//材料
	        	   $scope.saveObj.laoshi=data.laoshi;//指导人员情况
	        	   $scope.saveObj.qt=data.qt;//阶段
	        	   $scope.saveObj.qtname=data.qtname;//阶段备注
	           	
	           }).error(function(data, status, headers, config) {
	        	  // swal("提交成功!", "您添加的地址已经提交.", "success");
	           });
		
	};
	
    $scope.save = function() {

    	var requestUrl = "/api/coursemanager/course_edit_by_id";
    	var vSubjectData=$scope.multipleSubject.selectedSubjectWithGroupBy;
    	var vActivityData=$scope.multipleActivity.selectedActivityWithGroupBy;
    	
    	var subject_id="";
    	var subject_name="";
    	var activity_name="";
    	$.each(vSubjectData, function(index,val) {
    		subject_id+=val.id+",";
    		subject_name+=val.name+",";
    		
    	});
    	$.each(vActivityData, function(index,val) {
    		activity_name+=val.name+",";
    		
    	});
    	
    	if(subject_id.length>0){
    		subject_id=subject_id.substring(0, subject_id.length-1);
    		subject_name=subject_name.substring(0, subject_name.length-1);
    	}
    	if(activity_name.length>0){
    		activity_name=activity_name.substring(0, activity_name.length-1);
    	}
    	
    	
    	var grade_id="";
    	var area_id="";
    	if($scope.saveObj.grade!=null){
    		grade_id=$scope.saveObj.grade.id;
    	}
    	if($scope.saveObj.area!=null){
    		area_id=$scope.saveObj.area.id;
    	}
    	 var formData = {
    			 id :$stateParams.id,
					subject_id:subject_id,//学科
					field:area_id,// 所属领域
					grade_id:grade_id,		//年级ID
					activity:activity_name,
					 name:$scope.saveObj.name,		  //项目名称
	    		/*	  institution		//机构id
	    			  state		   //状态（0未审核，1审核通过，2审核未通过）
*/	    			  studentNum:$scope.saveObj.studentNum,		//每次接待人数
	    			  post1:$scope.photoPath,		 // 附件二封面模板
	    			  xzdw:$scope.saveObj.xzdw,		  //协作单位
	    			  hdmb:$scope.saveObj.hdmb,		// 活动目标
	    			  hdnr:$scope.saveObj.hdnr,	// 活动内容
	    			  zd:$scope.saveObj.zd,		// 重点
	    			  nd:$scope.saveObj.nd,		 // 难点
	    			  xqd:$scope.saveObj.xqd,		 // 兴趣点
	    			  online_at:$scope.saveObj.online_at,		//活动地点
	    			  sxtj:$scope.saveObj.sxtj,		// 条件与设施
	    			  cl:$scope.saveObj.cl,		 //材料
	    			  laoshi:$scope.saveObj.laoshi,		//指导人员情况
	    			  qt:$scope.saveObj.qt,	// 阶段
	    			  qtname:$scope.saveObj.qtname,		// 阶段备注
	    			  subjectName:subject_name//学科名称
			};
    	$http({
			method  : 'POST',
			url     : requestUrl,
			data    : $.param(formData),  // pass in data as strings
			headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
		}).success(function(data, status, headers, config) {
			//
			 swal("修改成功!", "您修改的记录已经提交成功.", "success");
			 $state.go('app.activityapplylist');
		}).error(function(data, status, headers, config) {
		});
		
    
	};
	
	
	$scope.setFileUploadResult = function(fileUri, uploadBackup){
		 
		  $scope.photoPath = fileUri;
		  	  		 
		 };
	
		//校验
		$scope.validForm=function(){
			$('#defaultForm').bootstrapValidator({
				message: 'This value is not valid',
				feedbackIcons: {
					valid: 'glyphicon glyphicon-ok',
					invalid: 'glyphicon',//glyphicon glyphicon-remove验证错误时输入框内需要显示红叉就加上
					validating: 'glyphicon glyphicon-refresh'
				},
				fields: {
					name: {
						/*group: '.col-lg-4',*/
						validators: {notEmpty: {message: '活动项目名称必填'}}
					},
					studentNum: {
						validators: {
							notEmpty: {message: '每次接待学生人数不能为空'},
							/*stringLength: {min: 1,max: 255,message: '参数名称必须大于1个字小于255个字'}, */
							regexp: {
	                            regexp: /^[0-9]+$/,
	                            message: '每次接待学生人数只能是数字'
	                        }
							
						}
					},
					xzdw: {
						validators: {
							notEmpty: {message: '协作单位不能为空'},
							stringLength: {max: 200,message: '协作单位不得超过200个字'}
						}
					},
					hdmb: {
						validators: {
							notEmpty: {message: '活动目标不能为空'}
						}
					},
					hdnr: {
						validators: {
							notEmpty: {message: '活动内容不能为空'}
						}
					},
					zd: {
						validators: {
							notEmpty: {message: '重点不能为空'}
						}
					},
					nd: {
						validators: {
							notEmpty: {message: '难点不能为空'}
						}
					},
					xqd: {
						validators: {
							notEmpty: {message: '兴趣点不能为空'}
						}
					},
					online_at: {
						validators: {
							notEmpty: {message: '地点不能为空'}
						}
					},
					sxtj: {
						validators: {
							notEmpty: {message: '条件设施不能为空'}
						}
					},
					cl: {
						validators: {
							notEmpty: {message: '材料不能为空'}
						}
					},
					laoshi: {
						validators: {
							notEmpty: {message: '指导人员情况不能为空'}
						}
					},
					qt: {
						validators: {
							notEmpty: {message: '实施阶段不能为空'}
						}
					}
					,
					qtname: {
						validators: {
							notEmpty: {message: '阶段备注不能为空'}
						}
					}
					
				}
			});
			$('#validateBtn').click(function() {//保存按钮的点击事件
				$('#defaultForm').bootstrapValidator('validate');//验证表单
				var flag = $("#defaultForm").data('bootstrapValidator').isValid();//获取整个表单项的验证结果
				if(flag){//验证通过
					$scope.save();
				}
			});
		  };
 
  }]);

