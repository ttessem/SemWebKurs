(function(app){
    'use strict';

    app.controller('PersonCtrl', ['$scope', '$location', '$http', '$cookies',
        'HentPerson', 'Person', 'Kjenner', 'Config', '$resource', 'Search', 'HarSett',
        'SuggestMovie',
        function ($scope, $location, $http, $cookies, HentPerson, Person, Kjenner,
                  Config, $resource, Search, HarSett, SuggestMovie) {

            $scope.method = 'POST';
            $scope.url = Config.apiBaseUrlPerson;
            $scope.selected = {};
            $scope.graph = {};
            $scope.wholeGraph = {};
            $scope.currentPerson = $scope.getPerson($cookies.userId);
            $scope.currentPersonId = null;
            $scope.currentPersonMovies = [];
            $scope.currentPersonFriends = [];
            $scope.currentUserId = $cookies.userId || null;

            $scope.getSearch = function(movie) {
                console.log('Movie: ' + movie);
                if(movie.length >= 3) {
                    var res = Search.get({title: movie});
                    console.log(res);
                    $scope.movieResults = res;
                }
            };
            $scope.localMovieSearch = function(movieTitle) {
                if(movieTitle.length >= 3){
                var movies = SuggestMovie.get({title: movieTitle});
                    $scope.movieResults = movies;
                }
            };
            $scope.likesMovie = function(movieId) {
                console.log('movie id: ' + movieId);

                $http.put($scope.url + '/'+ getId($scope.currentPerson['@id']) + '/harSett',
                    movieId,
                    {headers: {'Access-Control-Allow-Methods': 'PUT, OPTIONS',
                        'cx_auth': $cookies.cx_secret,
                        'Content-Type': 'text/plain'}}
                ).success(function() {
                        $http.get($scope.url + '/'+ getId($scope.currentPerson['@id']))
                            .success(function(response) {
                                console.log('yeay:::: ' + response);
                                console.log(response);
                                $scope.currentPerson = response["@graph"][0];

                            }).error(function(err) {
                                console.log('æsj!!!');
                            });
                        $http.get($scope.url + '/'+ getId($scope.currentPerson['@id']) + '/harSett')
                            .success(function(response){
                               $scope.currentPersonMovies = response;
                            });

                        // var person = HentPerson.get({id: $scope.currentPerson['@id']});
                    }).error(function() {
                        console.log('æsj!!!');
                    });
                // HarSett.harSett({id: getId($scope.currentPerson["@id"])}, movieId);
            };

            $http({method: 'GET', url: $scope.url}).success(function(response){
                console.log(response['@graph']);
                console.log(response);
                $scope.wholeGraph = response['@graph'];
            });
            var persons = Person.getAllPersons({}, function() {
                console.log(persons);
            });

            $scope.addKjenner = function (inputString) {
                console.log('addKjenner');
                console.log($cookies.cx_secret);

                $http.put($scope.url + '/'+ getId($scope.currentPerson['@id']) + '/kjenner/' + getId(inputString['@id']),
                    null,
                    {headers: {'Access-Control-Allow-Methods': 'PUT, OPTIONS',
                        'cx_auth': $cookies.cx_secret,
                        'Access-Control-Allow-Headers': 'Content-Type'}
                    })
                    .success(function(){
                        var person = HentPerson.get({id: getId($scope.currentPerson["@id"])});
                        console.log("Jipppiiiii");
                        console.log(person);
                        $http.get($scope.url + '/'+ getId($scope.currentPerson['@id']) + '/kjenner')
                            .success(function(response){
                                $scope.currentPersonFriends = response;
                            });

                    }).error(function(err){
                        console.log("æssshhh");
                    });
                // Kjenner.addKjenner({
                //     id: getId($scope.currentPerson["@id"]),
                //     kjenner_id: getId(inputString["@id"])
                // }, function (response) {
                //     console.log("Svar fra addKjenner");
                //     console.log(response);

                //     HentPerson({
                //         id: $scope.currentPerson["@id"]
                //     }, function(person, headers){
                //         $cookies.cx_secret = headers("cx_secret");
                //         $scope.currentPerson = person["@graoh"][0];
                //     }, function(err) {
                //         console.log("Noe galt med hent person etter lagt til kjenner");
                //     });
                // }, function (err) {
                //     $scope.error = "Something went wrong ;(";
                //     console.log("Noe galt med legge til kjenner");
                // });

            };

            $scope.selectedInputFormatter = function () {
                return "";
            };

            $scope.getPerson = function(id) {
                console.log("henter****person");

                $http.get($scope.url + '/'+ getId(id))
                    .success(function(response) {
                        console.log('yeay:::: ' + response);
                        console.log(response);
                        return response;
                    }).error(function(err) {
                        console.log('æsj!!!');
                        return null;
                    });

                // var person = new HentPerson.get({id: getId(id)}, function(response) {
                //     console.log("hent person");
                //     console.log(response);
                //     $scope.listePerson.kontakter  = response;
                // }, function(err, headers) {
                //    console.log(err);
                //     console.log(headers);
                // });
                // console.log(person);
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
                    console.log(headers('cx_secret'));
                    $cookies.cx_secret = headers("cx_secret");
                    $scope.currentPerson = response["@graph"][0];
                    $scope.currentPersonId = $scope.currentPerson["@id"];
                    $cookies.userId = getId($scope.currentPerson['@id']);
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
