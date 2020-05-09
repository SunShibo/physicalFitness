'use strict';
app.controller('MutilSelectWithSearchPageCtrl', ['$scope', '$http', '$state',
                                      function($scope, $http, $state) {
	 $scope.oneObj = {
		  aboutme: '我是带搜索功能的多选下拉框demo页面！',
		  selectValue: [{value:'DE', text:'Delaware'}],
		  selectEles: [{value:'CT', text:'Connecticut'},{value:'DE', text:'Delaware'},{value:'FL', text:'Florida'}],
	      person: {}
	 };
	
	 //$scope.selected = $scope.oneObj.selectEles[1];
	 $scope.initMultiSelect = function(){
		 $(".select2").select2();
	 };
	 
	 $scope.person = {};
     $scope.people = [
     { name: 'Adam',      email: 'adam@email.com',      age: 12, country: 'United States' },
     { name: 'Amalie',    email: 'amalie@email.com',    age: 12, country: 'Argentina' },
     { name: 'Estefanía', email: 'estefania@email.com', age: 21, country: 'Argentina' },
     { name: 'Adrian',    email: 'adrian@email.com',    age: 21, country: 'Ecuador' },
     { name: 'Wladimir',  email: 'wladimir@email.com',  age: 30, country: 'Ecuador' },
     { name: 'Samantha',  email: 'samantha@email.com',  age: 30, country: 'United States' },
     { name: 'Nicole',    email: 'nicole@email.com',    age: 43, country: 'Colombia' },
     { name: 'Natasha',   email: 'natasha@email.com',   age: 54, country: 'Ecuador' },
     { name: 'Michael',   email: 'michael@email.com',   age: 15, country: 'Colombia' },
     { name: 'Nicolás',   email: 'nicolas@email.com',    age: 43, country: 'Colombia' }
     ];

     $scope.availableColors = ['Red','Green','Blue','Yellow','Magenta','Maroon','Umbra','Turquoise'];

     $scope.multipleDemo = {};
     $scope.multipleDemo.colors = ['Blue','Red'];
     $scope.multipleDemo.selectedPeople = [$scope.people[5], $scope.people[4]];
     $scope.multipleDemo.selectedPeopleWithGroupBy = [$scope.people[8], $scope.people[6]];
     $scope.multipleDemo.selectedPeopleSimple = ['samantha@email.com','wladimir@email.com'];
 }]);