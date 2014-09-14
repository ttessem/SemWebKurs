(function(app){
	'use strict';

  app.controller('PersonCtrl', ['$scope', '$location', '$http', 
  	function ($scope, $location, $http) {
  	$scope.submitperson = function() {
  		alert("Something submitted");
  	}
  	$scope.method = 'POST';
  	$scope.url = "http://localhost/person";

  	$scope.submitperson = function() {
		console.log("submit person");
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
  }]); 
})(angular.module('app'));