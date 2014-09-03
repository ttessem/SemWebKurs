(function(appControllers){

'use strict';

/**
 * @ngdoc function
 * @name whishlistApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the whishlistApp
 */
appControllers.controller('MainCtrl', ['$scope', function($scope) {
		$scope.app = {name: "SeWebKrusKlient"}
		$scope.welcomeText = "Velkomemen til bedriftspresentasjon med Computas";
	}]);
})(angular.module('appControllers'));