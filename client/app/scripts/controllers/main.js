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
                console.log("submitting person");
                var person = retrievePerson();
                $http({
                    method: 'POST',
                    url: $scope.url,
                    data: $.param({fornavn: person.fornavn,
                        etternavn: person.etternavn,
                        alder: person.alder,
                        studieretning: person.studieretning}),
                    headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}
                }).success(function(response, statusResponse, headers){
                    console.log("Suksess");
                    console.log(response);
                    console.log(headers('cx_secret'))
                    $cookies.cx_secret = headers("cx_secret");
                    $scope.currentPerson = response["@graph"][0];
                    $scope.currentPersonId = $scope.currentPerson["@id"];

                });

            };
	

            function retrievePerson() {
                var person = {};
                person.fornavn = $scope.person.fornavn;
                person.etternavn = $scope.person.etternavn;
                person.alder = $scope.person.alder;
                person.studieretning = $scope.person.studieretning;
                return person;
            }
		

	}]);
})(angular.module('app'));