(function(app){
	'use strict';

  app.controller('PersonCtrl', ['$scope', '$location', '$http', 'PersonService',
  	function ($scope, $location, $http, PersonService) {
  	$scope.submitperson = function() {
  		alert("Something submitted");
  	}
  	$scope.method = 'POST';
  	$scope.url = "http://localhost/person";
  	$scope.venner = PersonService.getAllPersons();
  	$scope.selected;
//  	Person.get({}, function())

  	$scope.submitperson = function() {
		console.log("submit person");
		PersonService.createPerson($scope.selected);
	  	$http({method: $scope.method, url: $scope.url})
	  	.success(function(data, status){
	  		$scope.data = data;
	  		$scope.status = status;
	  	}).
	  	error(function(data, status){
	  		$scope.data = data || "Request failed";
	  		$scope.status = status;
	  	});
	  };
	$scope.lagreKjenner = function(selected) {
		console.log("venn: " + selected);
		$scope.venner.push(selected);
		$scope.selected;
	};
  }]); 
})(angular.module('app'));