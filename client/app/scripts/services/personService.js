(function(app){
'use strict';
	app.factory('PersonService', ['$http', function($http){
	var baseUrl = "uib/person/";
    var PersonService = {};
    PersonService.getAllPersons = function(){
        return $http.get(baseUrl);
    };
    PersonService.getPerson = function(id) {
        return $http.get(baseUrl + id);
    };
    PersonService.createPerson = function() {
        return $http.post();
    }
    PersonService.putKjenner = function(id, kjennerId) {
        return $http.put(baseUrl + id +"/kjenner/"+ kjennerId);
    }

    app.factory('Movie', ['$resource', function($resource){
        return $resource(CONFIG.API_BASE_URL + "movies", {}, {
           getMovies: {
               method: 'GET'
           },
            getMovie: {
                method: 'GET'
            },
            addMovie: {
                method: 'POST'
            }
        });
    }]);

        
    return PersonService;
	}]);
})(angular.module('app'));