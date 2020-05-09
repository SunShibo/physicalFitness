// 开启严格模式
'use strict';
//应用部署在非root目录下时，访问地址的前缀。"tpl" 根据实际情况修改，该值来自于运维
var urlPrefix="tpl";
var app =  
	
angular.module('app',['ui.router','ngCookies','oc.lazyLoad','app.userService','app.parameterService','app.schoolYearService','app.myCommonService','ui.bootstrap.modal'])
	.directive('repeatDone', function() {
	    return {
	        link: function(scope, element, attrs) {
	            if (scope.$last) {                   // 这个判断意味着最后一个 OK
	                scope.$eval(attrs.repeatDone);    // 执行绑定的表达式
	            }
	        }
	    };
	})
	.directive('rendAfter', ['$timeout', function($timeout) { //该指令用来处理元素上的指令执行完后，再执行指定的自定义方法（一般是用jquery操作dom）
	    return {
	        link: function(scope, element, attrs) {
	        	$timeout(function(){  
	        		scope.$eval(attrs.rendAfter);
	            },0);
	        }
	    };
	}])

	
	.filter('propsFilter', function() {
    return function(items, props) {
        var out = [];
        if (angular.isArray(items)) {
          items.forEach(function(item) {
            var itemMatches = false;

            var keys = Object.keys(props);
            for (var i = 0; i < keys.length; i++) {
              var prop = keys[i];
              var text = props[prop].toLowerCase();
              if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                itemMatches = true;
                break;
              }
            }

            if (itemMatches) {
              out.push(item);
            }
          });
        } else {
          // Let the output be the input untouched
          out = items;
        }

        return out;
    };
})

	.filter('highlight', ['$sce',function($sce) {
		  return function (text, search, caseSensitive) {
		    if (text && (search || angular.isNumber(search))) {
		      text = text.toString();
		      search = search.toString();
		      if (caseSensitive) {
		        return $sce.trustAsHtml(text.split(search).join('<span class="ui-match">' + search + '</span>'));
		      } else {
		        return $sce.trustAsHtml(text.replace(new RegExp(search, 'gi'), '<span class="ui-match">$&</span>'));
		      }
		    } else {
		      return $sce.trustAsHtml(text);
		    }
		  };
	}])
	
	.run(
	    [          '$rootScope', '$state', '$stateParams',
	      function ($rootScope,   $state, $stateParams) {
	          $rootScope.$state = $state;
	          $rootScope.$stateParams = $stateParams;        
	      }
	    ]
	 )
	 .config(
	    [        '$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
	    function ($controllerProvider,   $compileProvider,   $filterProvider,   $provide) {
	        
	        // lazy controller, directive and service
	        app.controller = $controllerProvider.register;
	        app.directive  = $compileProvider.directive;
	        app.filter     = $filterProvider.register;
	        app.factory    = $provide.factory;
	        app.service    = $provide.service;
	        app.constant   = $provide.constant;
	        app.value      = $provide.value;
	    }
	  ])
	  .factory('loginTimeoutInterceptor', ['$q','$rootScope',function($q,$rootScope) {
		  var ifLogout = false;
		  
	  	  var interceptor = {
	  			  'request' : function(config){
	  				ifLogout = false;
	  				  //成功的请求方法
	  				  return config; //或者$q.when(config); 
	  			  },
	  			  'response' : function(response){
//	  				alert("res=="+JSON.stringify(response));
	  				$(response.data).each(function(index){
	  					if ($(this).attr('content') == 'login_page'){
	  						if (!ifLogout){
//	  							alert("登录超时！");
		  						$rootScope.$emit("userIntercepted","sessionOut",response);
		  						ifLogout = true;
	  						}
	  						return false;
	  					}
	  				});
	  	            //响应成功
		  		    return response; //或者$q.when(config);
	  			  },
	  			  'requestError' : function(rejection){
//	  				  alert("reqError=="+JSON.stringify(rejection));
	  				  //请求发生了错误，如果能从错误中恢复，可以返回一个新的请求或promise
	  				  return rejection;  
	  				  //或者通过返回一个rejection来阻止下一步
	  				  //return $q.reject(rejection);
	  			  },
	  			  'responseError' : function(response){
//	  				  	alert(JSON.stringify(response));
	  					if(response.status == '403' && response.statusText == "Forbidden" ){
	  						swal("您当前登录的角色没有此权限");
	  					}
//	  				alert("repError=="+JSON.stringify(response));
//	  				 var data = response['data'];
//		  				// 判断错误码，如果是未登录
//		  	            if(data["errorCode"] == "500999"){
//		  					// 全局事件，方便其他view获取该事件，并给以相应的提示或处理
//		  	                $rootScope.$emit("userIntercepted","notLogin",response);
//		  	            }
		  				// 如果是登录超时
		  				//if(data["errorCode"] == "500998"){
		  	                //$rootScope.$emit("userIntercepted","sessionOut",response);
		  	            //}
	  				  //请求发生了错误，如果能从错误中恢复，可以返回一个新的请求或promise
	  				  //return response;  
	  				  //或者通过返回一个rejection来阻止下一步
//	  				  return $q.reject(response);
	  			  }
	  			  
	  	  };
	  	  return interceptor;
	  }])
	  .config(function($httpProvider) {
	  	   $httpProvider.interceptors.push('loginTimeoutInterceptor');
	  	   $httpProvider.defaults.headers.common['Cache-Control'] = 'no-cache';
	  })
//	  .config(['$translateProvider', function($translateProvider){
//	    // Register a loader for the static files
//	    // So, the module will search missing translation tables under the specified urls.
//	    // Those urls are [prefix][langKey][suffix].
//	    $translateProvider.useStaticFilesLoader({
//	      prefix: 'l10n/',
//	      suffix: '.js'
//	    });
//	    // Tell the module what language to use by default
//	    $translateProvider.preferredLanguage('en');
//	    // Tell the module to store the language in the local storage
//	    $translateProvider.useLocalStorage();
//	  }])
	  .constant('MODULE_CONFIG', [
			{
				name:'angularFileUpload',
				files: [
					'js/libs/angular-file-upload/angular-file-upload.js',
					'js/libs/dropify/js/dropify.min.js',
					'js/libs/dropify/css/dropify.min.css'
				]
			},
		      {
		          name: 'ui.select',
		          files: [
		        	  'js/libs/angular-ui-select/dist/select.min.js',
		        	  'js/libs/angular-ui-select/dist/select.min.css',
		        	  'js/libs/custom-select/custom-select.css',
		        	  'js/libs/bootstrap_select/bootstrap-select.min.css'
		          ]
		      }
	    ]
	  )
	  // oclazyload config
	  .config(['$ocLazyLoadProvider', 'MODULE_CONFIG', function($ocLazyLoadProvider, MODULE_CONFIG) {
	      // We configure ocLazyLoad to use the lib script.js as the async loader
	      $ocLazyLoadProvider.config({
	          debug:  false,
	          events: true,
	          modules: MODULE_CONFIG
	      });
	  }])	 
	 .config( // 定义路由
			 ['$stateProvider', '$urlRouterProvider', 'MODULE_CONFIG', 
			            function ($stateProvider, $urlRouterProvider, MODULE_CONFIG) {
								 
				 // 访问默认界面
				 var layout = "app_new.html";
				 var index = window.location.href.lastIndexOf("app/");
//				 var startWith = index == -1 ?"":window.location.href.substring(index+4, index+7);
//				 if(startWith == "stu"){
//					 layout = "/student_new.html";
//					 $urlRouterProvider.otherwise('/app/stuhomeindex');
//				 }else if(startWith == "adm"){
//					 layout = "/app_new.html";
//					 $urlRouterProvider.otherwise('/app/sysdefaultpage');
//				 }else if(startWith == "out"){
//					 layout = "/out_new.html";
//					 $urlRouterProvider.otherwise('/app/outdefaultpage');
//				 }else if(startWith == "visitor"){
//					 layout = "/visitor_new.html";
//					 $urlRouterProvider.otherwise('/app/outdefaultpage');
//				 }else{
//					 $urlRouterProvider.otherwise('/app/sysdefault');            
//				 }
				 $urlRouterProvider.otherwise('/app/sysdefault');        		       
		             
					$stateProvider
			              .state('app', {
			                  abstract: true,
			                  url: '/app',
//			                  templateUrl: window.location.href.substring(index+4, index+7) == "stu"?
//			                		  "/student_new.html":window.location.href.substring(index+4, index+7) == "adm"?
//			                		  "/app_new.html":window.location.href.substring(index+4, index+7) == "out"?
//			                		  "/out_new.html":"/index_page.html",
			                  templateUrl:layout
			              })
			              //默认进入的页面
			              .state('app.sysdefault', { // 供菜单栏a标签ui-sref属性使用
			                  url: '/sysdefault', // 地址栏路由路径--跟state.go和后台url没有关系只是在地址栏显示
			                  templateUrl: 'sysdefault', // 指定后端Controller地址
			                  resolve: load(['js/controllers/white_default.js']) // 加载模块控制器
			              })
			              .state('app.sysdefaultpage', { // 供菜单栏a标签ui-sref属性使用
			                  url: '/admsysdefaultpage', // 地址栏路由路径
			                  templateUrl: 'sysdefaultpage', // 指定后端Controller地址
			                  resolve: load(['js/controllers/default.js']) // 加载模块控制器
			              })			              
			              
			              .state('app.uploadpic', { // 供菜单栏a标签ui-sref属性使用
			                  url: '/uploadpicture', // 地址栏路由路径
			                  templateUrl: 'uploadpicpage', // 指定后端Controller地址
			                  resolve: load(['angularFileUpload','js/controllers/upload_pic.js','js/controllers/file-upload.js']) // 加载模块控制器
			              })
						 .state('app.specialuploadpic', { // 供菜单栏a标签ui-sref属性使用
			                  url: '/specialuploadpic', // 地址栏路由路径
			                  templateUrl: 'specialuploadpic', // 指定后端Controller地址
			                  resolve: load(['angularFileUpload','ui.select','js/controllers/special_upload_pic.js','js/controllers/file-upload.js']) // 加载模块控制器
			              })
			              //百度富文本编辑框使用样例
			               .state('app.baiduRichTextEditor', { // 供菜单栏a标签ui-sref属性使用
			                  url: '/baiduRichTextEditor', // 地址栏路由路径
			                  templateUrl: 'baiduRichTextEditor', // 指定后端Controller地址
			                  resolve: load(['js/controllers/baidu_richText_editor.js']) // 加载模块控制器
			              })
			              //单文件上传样例
			              .state('app.singlefileupload', { // 供菜单栏a标签ui-sref属性使用
			                  url: '/singlefileupload', // 地址栏路由路径
			                  templateUrl: 'singlefileupload', // 指定后端Controller地址
			                  resolve: load(['angularFileUpload','js/controllers/single_file_upload.js','js/controllers/file-upload.js']) // 加载模块控制器
			              })
			               //多文件上传样例
			              .state('app.multifileupload', { // 供菜单栏a标签ui-sref属性使用
			                  url: '/multifileupload', // 地址栏路由路径
			                  templateUrl: 'multifileupload', // 指定后端Controller地址
			                  resolve: load(['angularFileUpload','js/directives/drop-zone.js','js/controllers/multiple_file_upload.js','js/controllers/file-upload.js']) // 加载模块控制器
			              })			              
			              //模态窗口多文件上传样例
			              .state('app.multifileupload4modal', { // 供菜单栏a标签ui-sref属性使用
			                  url: '/multifileupload4modal', // 地址栏路由路径
			                  templateUrl: 'multifileupload4modal', // 指定后端Controller地址
			                  resolve: load(['angularFileUpload','js/directives/drop-zone.js','js/controllers/multiple_file_upload4modal.js','js/controllers/multiple_file_upload_window.js','js/controllers/file-upload.js']) // 加载模块控制器
			              })
			             
			              //权限管理
			              .state('app.funcmanager', { // 供菜单栏a标签ui-sref属性使用
			                  url: '/admfuncmanager', // 地址栏路由路径
			                  templateUrl: 'sysfunclist', // 指定后端Controller地址
			                  resolve: load(['js/controllers/funcmanager/funcmanager.js']) // 加载模块控制器
			              })
			              //添加权限
			              .state('app.createfunc', { //供菜单栏a标签ui-sref属性使用
			                  url: '/admcreatefunc', // 地址栏路由路径
			                  templateUrl: 'sysfuncadd', // 指定后端Controller地址
			                  resolve: load(['ui.select','js/libs/bootstrapValidator/css/bootstrapValidator.css','js/libs/bootstrapValidator/js/bootstrapValidator.js','js/controllers/funcmanager/createfunc.js']) // 加载模块控制器
			              })
			              /*
			               *学科列表页面
			               * */
			              .state('app.subjectlist', {
			                  url: '/admsubjectlist', 
			                  templateUrl: 'subjectlist',
			                  resolve: load(['js/controllers/subjectmanager/subject_list.js']) // 加载模块控制器
			              })
			              
			               /*
			               *学科添加页面
			               * */
			              .state('app.subjectadd', {
			                  url: '/admsubjectadd', 
			                  templateUrl: 'subjectadd',
			                  resolve: load(['angularFileUpload','js/controllers/subjectmanager/subject_add.js','js/controllers/file-upload.js','js/libs/bootstrapValidator/css/bootstrapValidator.css','js/libs/bootstrapValidator/js/bootstrapValidator.js']) // 加载模块控制器
			              })
			              
			            /*
			          	 * 单个参数传递示例
			          	 * */
			              .state('app.visitorcoursedetail', {
			            	  url: '/visitorcoursedetail/{courseId}',
			            	  templateUrl: 'visitorcoursedetail', 
			            	  resolve: load(['ui.select','js/controllers/studentmanager/visitor_course_detail.js','css/visitor_main.css','css/zoom.css','css/bootstrap.min.css','js/jwplayer.js'])
			              })
			            
			             /*
			          	 * 多个参数传递示例
			          	 * */
			          	
			              .state('app.stucoursedetail', { 
			            	  url: '/stucoursedetail/{userhourId}/{if_start}',
			            	  templateUrl: 'stucoursedetail', 
			            	  resolve: load(['angularFileUpload','js/controllers/studentmanager/stu_course_detail.js','js/controllers/file-upload.js','js/libs/angular-dropzone/dropzone.css','js/directives/drop-zone.js','js/libs/owl.carousel/owl.carousel.min.css','js/libs/owl.carousel/owl.theme.default.css','css/main.css','js/libs/owl.carousel/owl.carousel.min.js','js/libs/starscore/startScore.js','js/controllers/studentmanager/stu_practice_upload.js'])
			              })
			              // html转pdf
						  .state('app.html_to_pdf', {
						  	  url: '/html_to_pdf',
							  templateUrl: 'html_to_pdf',
							  resolve: load([
								'js/controllers/html_to_pdf.js'
							  ])
						  })
			             
			            
			              ;
									
					
					// 加载远程资源文件
			          function load(srcs, callback) {
			            return {
			                deps: ['$ocLazyLoad', '$q',
			                  function( $ocLazyLoad, $q ){
			                    var deferred = $q.defer();
			                    var promise  = false;
			                    srcs = angular.isArray(srcs) ? srcs : srcs.split(/\s+/);
			                    if(!promise){
			                      promise = deferred.promise;
			                    }
			                    angular.forEach(srcs, function(src) {
			                      promise = promise.then( function(){
//			                        if(JQ_CONFIG[src]){
//			                          return $ocLazyLoad.load(JQ_CONFIG[src]);
//			                        }
									var name="";
			                        angular.forEach(MODULE_CONFIG, function(module) {
			                          if( module.name == src){
			                            name = module.name;
			                          }else{
			                            name = src;
			                          }
			                        });
			                        return $ocLazyLoad.load(name);
			                      } );
			                    });
			                    deferred.resolve();
			                    return callback ? promise.then(function(){ return callback(); }) : promise;
			                }]
			            }
			          }
						 
}]);
app.controller('AppCtrl', ['$scope', '$http', '$state', '$rootScope','$window','$location', '$anchorScroll','$interval',
                                  function($scope, $http, $state, $rootScope,$window,$location,$anchorScroll,$interval) {
	
	$scope.test = 'Hello world!';
	 //生成导航数据
   $scope.breadcrumbObj = {
   		menuName1 : '',
   		menuUrl1 : '',
   		menuName2 : '',
   		menuUrl2 : '',
   		title : ''
   };
   //点击菜单的事件
   $scope.createBreadcrumb = function(menu1, menu1_url, menu2, menu2_url, event, title, func_id){
   	$scope.breadcrumbObj.menuName1 = menu1;
   	$scope.breadcrumbObj.menuUrl1 = menu1_url;
   	$scope.breadcrumbObj.menuName2 = menu2;
   	$scope.breadcrumbObj.menuUrl2 = menu2_url;
   	$scope.breadcrumbObj.title = title;
   	initMenuColor();
   	var aElementes = $('#side-menu').find('a');
      	for (var i = 0; i < aElementes.length; i++){
      		if ($.trim($(aElementes[i]).text()) == $scope.breadcrumbObj.menuName1){
      			$(aElementes[i]).addClass("active");
      		}else if ($.trim($(aElementes[i]).text()) == $scope.breadcrumbObj.menuName2){
      			$(aElementes[i]).addClass("active");
      		}else{
      			$(aElementes[i]).removeClass("active");
      		}
      	}
//   	if(menu2 == ''){
//   		$(event['target']).addClass("active");
//   	}else{
//   		$(event['target']).parent().parent().parent().find("a").addClass("active");
//       	$(event['target']).parent().parent().find("a").removeClass("active");
//       	$(event['target']).addClass("active");
//   	}
   	setBreadcrumbToSession();
   };
   $scope.isShowBottom=function(list){//判断是否为空
   	if(list!=null && list !=undefined){
   		if(list.length == 0){
   			return true;
   		}else{
   			return false;
   		}
   	}else{
   		return false;
   	}
	};
   function addCreateBreadcrumb(menu2,menu2Url,funcId){
   	var formData = {
   		funcName : menu2,
	  		menuUrl : menu2Url,
	  		funcId : funcId
	        };
       $http({
           method  : 'POST',
           url     : 'ajax/home/add_create_bread_crumb',
           data    : $.param(formData),  // pass in data as strings
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
       }).success(function(data, status, headers, config) {
       }).error(function(data, status, headers, config) {
       });
   }
   
   //将所有点击过的菜单的底色值为原始状态
   function initMenuColor(){
   	var aElementes = $('#side-menu').find('a');
      	for (var i = 0; i < aElementes.length; i++){
      		$(aElementes[i]).removeClass("active");
      	}
   }
   //当前登录用户
   $scope.loginUser = null;
   function getLoginUser(){
   	$http({
           method  : 'GET',
           url     :'ajax/user/query_logined_user',
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
       }).success(function(data, status, headers, config) {
          $scope.loginUser = data;
       }).error(function(data, status, headers, config) {
       });
   }
   getLoginUser();

   //点击首页链接
   $scope.initNavInfo = function(){
   	initMenuColor();
   	$scope.breadcrumbObj.menuName1 = '';
   	$scope.breadcrumbObj.menuUrl1 = '';
   	$scope.breadcrumbObj.menuName2 = '';
   	$scope.breadcrumbObj.menuUrl2 = '';
   	$scope.breadcrumbObj.title = '';
   	setBreadcrumbToSession();
   };
   function setBreadcrumbToSession() { //设置当前导航信息到session中
	  	var formData = {
	  		breadcrumb : JSON.stringify($scope.breadcrumbObj)
	    };
       $http({
           method  : 'POST',
           url     : 'ajax/set_breadcrumb',
           data    : $.param(formData),  // pass in data as strings
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
       }).success(function(data, status, headers, config) {
       }).error(function(data, status, headers, config) {
       });
   };
   function getBreadcrumbFromSession(){//从服务器获取菜单信息
       $http({
           method  : 'GET',
           url     : 'ajax/get_breadcrumb',
           headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
       }).success(function(data, status, headers, config) {
          if (data != ''){
       	  	$scope.breadcrumbObj = data;	
          }
       }).error(function(data, status, headers, config) {
       });
   }
   //刷新页面时，处理第一级菜单的底色
   $scope.initFirstMenuBgColor = function(){
   	getBreadcrumbFromSession();
   	var aElementes = $('#side-menu').find('a');
      	for (var i = 0; i < aElementes.length; i++){
      		if ($.trim($(aElementes[i]).text()) == $scope.breadcrumbObj.menuName1){
      			$(aElementes[i]).addClass("active");
      		}else if ($.trim($(aElementes[i]).text()) == $scope.breadcrumbObj.menuName2){
      			$(aElementes[i]).addClass("active");
      		}else{
      			$(aElementes[i]).removeClass("active");
      		}
      	}
   };
   //判断通过funcIds传入的功能号是否存在一个在当前登录用户所拥有的角色对应的功能号中
   $scope.ifAnyFuncIn = function(funcIds){//funcIds  格式为  funcId1,functId2,funcId3
   	var arrFuncIds = funcIds.split(",");
   	if (arrFuncIds.length > 0){
   		for (var i = 0; i < arrFuncIds.length; i++){
   			if ($scope.loginUser.roleIds.indexOf(arrFuncIds[i]) >= 0){
   				return true;
   			}
   		}
   	}
   };
   
   //判断学生端页面导航栏选中那个
   $scope.stuNavValue=function(){
	   if(window.location.href.indexOf("stuhomeindex") > 0){
		   return 1;
	   }else if(window.location.href.indexOf("stuoptionalcourselist") > 0){
		   return 2;
	   }else if(window.location.href.indexOf("stulooksummarylist") > 0){
		   return 4;
	   }else{
		   return 0;
	   }
   };
   
   //判断用户组属于哪个类型(1.机构老师、机构管理员2.学校老师、学校班主任、学校管理员)
   $scope.checkGroup = function(groupId){
	   if(groupId==9||groupId==10){
		   return 1;
	   }else if(groupId==2||groupId==3||groupId==6||groupId==13){
		   return 2;
	   }else{
		   return 3;
	   }
   };
   
   /**
    * 判断当前是否在学生首页
    */
   $scope.checkUrl=function(){
	   return window.location.href.indexOf("stuhomeindex") >0 ?1:2;
   };
   /**
    * 学生页面根据参数处理跳转到选课记录列表
    */
   $scope.stuGoBottom = function(jump) {
	   if(jump == 1){
		   $location.hash('courselist');// 将location.hash的值设置为你想要滚动到的元素的id
		   $anchorScroll();// 调用 $anchorScroll
	   }else{
		   var old = window.location.href.substring(0, window.location.href.indexOf("#")+1);
		   $window.location.href = old+"/app/stuhomeindex#courselist";
	   }
   };
   
   /**
    * 学生页面信息错误提示块是否显示
    */
   $scope.stuErrorObj={
		   errorMsg:"",
		   errorShow:false
   };
   /**
    * 学生页面错误提示设置显示
    */
   $scope.setStuErrorShow = function(msg) {
	   $scope.stuErrorObj.errorShow=true;
   };
   
   $scope.stuGo=function(){
	   // 将location.hash的值设置为
		// 你想要滚动到的元素的id
	   $location.hash('courselist');
	   // 调用 $anchorScroll()
	   $anchorScroll();
   };
   
   /**
    * 以下为了实现页面不允许浏览器的返回
    */
   history.pushState(null, null, document.URL);
   window.addEventListener('popstate', function () {
           history.pushState(null, null, document.URL);
   });
	
	$rootScope.$on('userIntercepted',function(errorType){
		// 跳转到登录界面，这里我记录了一个from，这样可以在登录后自动跳转到未登录之前的那个界面
		//$state.go("app.login",{from:$state.current.name,w:errorType});
//		$state.go("app.relogin",{from:null});
		$state.go("app.relogin");
	});
	getBreadcrumbFromSession();
	
	
}]);