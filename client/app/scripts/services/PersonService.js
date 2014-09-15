(function(app){

'use strict';

app.factory('PersonService', ['$resource', function($resource) {
	var PersonService = {
		getAllPersons: function() {
			return [
				"Lisa", "Lene", "Max", "Mari"
				//{firstName: "Lisa", lastName: "Halvorsen"},
				//{firstName: "Max", lastName: "Muller"}
				];
		},
		getPerson: function(id) {
			return {firstName: "Lisa", lastName: "Halvorsen"}
		},
		createPerson: function(person) {
			console.log("Person: " + person);
			return $resource(CONFIG.API_BASE_URL + "uib/person", {},
				{ create: {
					method: 'POST'
				}})
		}	
	};

	return PersonService;		

	}]);
app.factory('Person',['$resource', function($resource){
	return $resource(CONFIG.API_BASE_URL + "uib/person", {}, {
		getPersons: {
			method: 'GET'
			isArray: true
		}
		createPerson: {
			method: 'POST'
		}
	})
}]);
app.factory('HentPerson', ['$resource', function($resource) {
	return $resource(CONFIG.API_BASE_URL + ":id", {}, {
		hentPerson: {
			method: 'GET'
		}
	})
}]);


app.factory('Kjenner', ['$resource', function($resource) {
	return $resource(CONFIG.API_BASE_URL + ":id/kjenner/:kjenner_id", {}, {
		addKjenner: {
			method: 'PUT'
		}
	})
}]);
app.factory('HarSett', ['$resource', function($resource) {
	return $resource(CONFIG.API_BASE_URL + ":id/harSett", {}, {
		harSett: {
			method: 'PUT'
		}
	})
}]);

})(angular.CONFIG.API_BASE_URL + "uib/person"module('app'));