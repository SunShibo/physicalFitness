'use strict';
app.controller('UploadsingleFileCtrl', ['$scope', '$http', '$state',
                                      function($scope, $http, $state) {
	 $scope.oneObj = {
		  aboutme: '我是上传文件demo页面！'
	 };
	 
	 $scope.setFileUploadResult = function(fileUri, uploadBackup){
		 alert(fileUri);
	 };
	 
	 // 文件上传插件初始化
	 $(document).ready(function(){
         // Basic
         $('.dropify').dropify();

         // Translated
         $('.dropify-fr').dropify({
             messages: {
                 default: 'Glissez-déposez un fichier ici ou cliquez',
                 replace: 'Glissez-déposez un fichier ou cliquez pour remplacer',
                 remove:  'Supprimer',
                 error:   'Désolé, le fichier trop volumineux'
             }
         });
         
         // Used events
         var drEvent = $('#input-file-events').dropify();

         drEvent.on('dropify.beforeClear', function(event, element){
             return confirm("Do you really want to delete \"" + element.file.name + "\" ?");
         });

         drEvent.on('dropify.afterClear', function(event, element){
             alert('File deleted');
         });

         drEvent.on('dropify.errors', function(event, element){
             console.log('Has Errors');
         });

         var drDestroy = $('#input-file-to-destroy').dropify();
         drDestroy = drDestroy.data('dropify')
         $('#toggleDropify').on('click', function(e){
             e.preventDefault();
             if (drDestroy.isDropified()) {
                 drDestroy.destroy();
             } else {
                 drDestroy.init();
             }
         })
     });
 }]);