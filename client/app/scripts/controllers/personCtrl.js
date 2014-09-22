(function(app){
    'use strict';

    app.controller('PersonCtrl', ['$scope', '$location', '$http', '$cookies', 
        'HentPerson', 'Person', 'Kjenner', 'Config', '$resource', 'Search', 'HarSett', 
        function ($scope, $location, $http, $cookies, HentPerson, Person, Kjenner,
            Config, $resource, Search, HarSett) {
            
            $scope.method = 'POST';
            $scope.url = Config.apiBaseUrlPerson;
            $scope.selected = {};
            $scope.graph = {};
            $scope.wholeGraph = {};
            $scope.currentPersonId = null;

            $scope.getSearch = function(movie) {
                console.log("Movie: " + movie); 
                if(movie.length >= 3) {
                    var res = Search.get({title: movie});
                    console.log(res);
                    $scope.movieResults = res;
                }
            };
            $scope.likesMovie = function(movieId) {
                console.log("movie id: " + movieId);
                HarSett.harSett({id: getId($scope.currentPerson["@id"])}, movieId);
            };

            $http({method: 'GET', url: $scope.url}).success(function(response){
                console.log(response["@graph"]);
                console.log(response);
                $scope.wholeGraph = response["@graph"];
            });
            var persons = Person.getAllPersons({}, function() {
                console.log(persons);
            });

            $scope.addKjenner = function (inputString) {
                console.log("addKjenner");
                console.log($cookies.cx_secret);

                // $http({method: 'PUT',
                //         url: $scope.url + "/1/kjenner/2",
                //         data: '',
                //         headers: {'Access-Control-Allow-Methods': 'PUT, OPTIONS',
                //         'cx_auth': $cookies.cx_secret}
                //         // 'Access-Control-Allow-Headers': 'Content-Type'}
                //     })
                // .success(function(){
                //     console.log("Jipppiiiii");
                // }).error(function(err){
                //     console.log("æssshhh");
                // }); 
                Kjenner.addKjenner({
                    id: getId($scope.currentPerson["@id"]),
                    kjenner_id: getId(inputString["@id"])
                }, function (response) {
                    console.log("Svar fra addKjenner");
                    console.log(response);

                    HentPerson.hentPerson({
                        id: $scope.currentPerson["@id"]
                    }, function(person, headers){
                        $cookies.cx_secret = headers("cx_secret");
                        $scope.currentPerson = person["@graoh"][0];
                    }, function(err) {
                        console.log("Noe galt med hent person etter lagt til kjenner");
                    });
                }, function (err) {
                    $scope.error = "Something went wrong ;(";
                    console.log("Noe galt med legge til kjenner");
                });

            };
            $scope.selectedInputFormatter = function () {
                return "";
            };
            $scope.putKjenner = function(node) {
                //todo legge inn in kjenner inputbox
            };
            $scope.lagreKjenner = function(node){
                //$http({method: 'POST', url: $scope.url + :id/kjenner/:kjenner_id})

            };
            $scope.onMemberSelect = function (item) {
                $scope.addKjenner(item);
            };

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
            function getId(url){
                var id = url.substring(Config.resourcePersonBaseUrl.length);
                console.log(url + " id: " +id);
                return id;
            }
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
