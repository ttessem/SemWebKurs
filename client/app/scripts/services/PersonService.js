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
	return $resource(Config.apiBaseUrlPerson+ '/:id');
}]);


app.factory('Kjenner', ['$resource', '$cookies', '$http', function($resource, $cookies, $http) {
    console.log("cx_auth");
    console.log($cookies.cx_secret);
    var secret = $cookies.cx_secret;
	return $resource(CONFIG.API_BASE_URL + 'uib/person/:id/kjenner/:kjenner_id', {}, {
		addKjenner: {
			method: 'PUT',
			params: {id : "@id", kjenner_id : "@kjenner_id"},
			headers: {'cx_auth' : secret}
		}
	});
}]);

app.factory('HarSett', ['$resource', '$cookies', function($resource, $cookies) {
	var secret = $cookies.cx_secret;
	return $resource(CONFIG.API_BASE_URL + 'uib/person/:id/harSett', {}, {
		harSett: {
			method: 'PUT',
            params : {id: "@id"},
            headers: {'Access-Control-Allow-Methods': 'GET, POST, PUT, OPTIONS', 'cx_auth' : secret, "Content-Type": "text/plain"}
		}
	});
}]);


})(angular.module('app'));