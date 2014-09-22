(function(app){

'use strict';
app.factory('Config', [function(){
	return {
		apiBaseUrl: "http://semweb.computas.com/service/",
		apiBaseUrlPerson: "http://semweb.computas.com/service/uib/person",
		resourcePersonBaseUrl: "http://semweb.computas.com/uib/person/"
	}
}]);

app.factory('Person',['$resource', 'Config', function($resource, Config){
	return $resource(Config.apiBaseUrlPerson, {}, {
		getAllPersons: {
			method: 'GET'
			
		},
		createPerson: {
			method: 'POST'
		}
	});
}]);

app.factory('HentPerson', ['$resource', 'Config', function($resource, Config) {
	return $resource(Config.apiBaseUrlPerson+ ':id');
}]);


app.factory('Kjenner', ['$resource', '$cookies', function($resource, $cookies) {
    console.log("cx_auth");
    console.log($cookies.cx_secret);
	return $resource(CONFIG.API_BASE_URL + 'uib/person/:id/kjenner/:kjenner_id', {}, {
		addKjenner: {
			method: 'PUT',
			params: {id : "@id", kjenner_id : "@kjenner_id"},
			headers: {'Access-Control-Allow-Methods': 'GET, POST, PUT, OPTIONS', 'cx_auth' : $cookies.cx_secret}
		}
	});
}]);

app.factory('HarSett', ['$resource', '$cookies', function($resource, $cookies) {

	return $resource(CONFIG.API_BASE_URL + 'uib/person/:id/harSett', {}, {
		harSett: {
			method: 'PUT',
            params : {id: "@id"},
            headers: {'Access-Control-Allow-Methods': 'GET, POST, PUT, OPTIONS', 'cx_auth' : $cookies.cx_secret}
		}
	});
}]);


})(angular.module('app'));