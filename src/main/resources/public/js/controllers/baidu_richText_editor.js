// 开启严格模式
'use strict';
app.controller('BaiduRichTextEditorCtrl', ['$scope', '$http', '$state', '$sce', 'userService','$location', '$window', '$timeout','$stateParams',
                                      function($scope, $http, $state, $sce, userService,$location,$window,$timeout,$stateParams) {
	
		$scope.pageObj = {
				text : '我是百度富文本编辑框实例页面',
				editorContent: ''
		};
		
		$scope.getEditorContent = function(){
			$scope.pageObj.editorContent = $scope.editor.getContent();
		}
		$scope.setEditorContent = function(){
			$scope.editor.setContent("我是为富文本编辑框初始化的内容。");
		}
		
		$scope.initBaiduEditor = function(){
			//添加编辑器
			$scope.editor = new UE.ui.Editor({initialFrameHeight:400,initialFrameWidth:800});
			$scope.editor.render("addInformContent");
			
			/**
    		function getContent() {
    	        var arr = [];
    	        arr.push("使用editor.getContent()方法可以获得编辑器的内容");
    	        arr.push("内容为：");
    	        arr.push(UE.getEditor('addInformContent').getContent());
    	        $("[href$='.zip']").each(
    	                function(){
    	                	alert($(this).val());
    	                }   
    	            );
    	    }
    	    */
		}
  }]);