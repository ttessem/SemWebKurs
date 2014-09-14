(function(app){
'use strict';
	app.factory('httpService', ['$scope', '$http', function($scope, $http){
	
	$scope.person_base_url = "url/person"
	$scope.person  = { fornavn : "", etternavn : "", alder: "", studieretning: ""};
	


	}]);
})(angular.module('app'));