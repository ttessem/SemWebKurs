(function(app){

'use strict';

/**
 * @ngdoc function
 * @name whishlistApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the whishlistApp
 */
app.controller('MainCtrl', ['$scope', function($scope) {
		$scope.app = {name: "SeWebKrusKlient"}
		$scope.welcomeText = "Velkomemen til bedriftspresentasjon med Computas";
		$scope.submitperson = function() {
			$scope.name
			$scope.gender
			$scope.age
			$scope.study_program	
		}
		

	}]);
})(angular.module('app'));