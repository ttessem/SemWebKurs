(function(app){
	'use strict';

  app.controller('PersonCtrl', ['$scope', '$location', '$http', 'PersonService', 'HentPerson', 'Person',
  	function ($scope, $location, $http, PersonService, HentPerson, Person) {
  	$scope.submitperson = function() {
  		alert("Something submitted");
  	}
  	$scope.method = 'POST';
  	$scope.url = CONFIG.API_BASE_URL + "service/uib/person";
  	$scope.venner = PersonService.getAllPersons();
  	$scope.selected;
  	$scope.graph = {};
  	$scope.wholeGraph = {};
  	
  	$http({method: 'GET', url: $scope.url}).success(function(response){
  		console.log(response);
  		$scope.wholeGraph = response;
  	});
	var persons = Person.getAllPersons({}, function() {
		console.log(persons)	
	}); 
	$scope.test = {"@c": "lalal"} 	
//  	Person.get({}, function())
	// Person.query().$promis.then(function (persons) { //getAllPersons({}, function(person){
	// 	console.log(persons);
	// 	//person.$promis.then(function(persons) {
	// 	//	console.log(persons);
	// 	})


	// }
  //, function(err) {

	//});
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
    		headers: {'Content-Type': 'application/x-www-form-urlencoded'}
		}).success(function(response){
			console.log("Suksess");
			console.log(response);
			$scope.graph = response;
			//console.log(response.@graph);
			//console.log(response.@context);
		});
  		
  		// var newPerson = new Person(person);
  		// console.log("newPerson");
  		// console.log(newPerson);
  		// var anNewThinf = newPerson.$save();
  		// console.log("aNewThing");
  		// console.log(anNewThinf);

  		// new Person(person).$createPerson({

  		// }, function(person) {
  		// 	//hva som skjer når funksjon returnerer
  		// 	console.log("Suksess");
  		// 	console.log(person);
  		// 	clearPersonInput();
  		// }, function(err) {
  		// 	//error
  		// 	$scope.error = "Noe gikk galt :(";
  		// });
  	};
  	function retrievePerson() {
  		var person = {};
  		person.fornavn = $scope.person.fornavn;
  		person.etternavn = $scope.person.etternavn;
  		person.alder = $scope.person.alder;
  		person.studieretning = $scope.person.studieretning;
  		return person;
  	}
  	function clearPersonInput() {
  		$scope.person = {};
  	}
  }]); 
})(angular.module('app'));