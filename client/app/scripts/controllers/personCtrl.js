(function(appControllers){
	'use strict';

  appControllers.controller('PersonCtrl', ['$scope', '$location', 
  	function ($scope, $location) {
  	$scope.submitperson = function() {
  		alert("Something submitted");
  	}
  }]); 
})(angular.module('appControllers'));