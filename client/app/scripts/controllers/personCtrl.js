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
            $scope.getPerson = function(id) {
                if(id) {
                    console.log("get pserson id: " + id)
                $http.get($scope.url + '/'+ id)
                    .success(function(response) {
                        console.log('yeay:::: ' + response);

                        $scope.currentPerson = response['@graph'][0];
                        return response['@graph'][0];
                    }).error(function(err) {

                        return null;
                    });
                    //henter filmer
                    $http.get($scope.url + '/'+ id + '/harSett')
                        .success(function(response){
                            console.log(response);
                            $scope.currentPersonMovies = response['@graph'];
                        });
                    //henter venner
                    $http.get($scope.url + '/'+ id + '/kjenner')
                        .success(function(response){
                            console.log(response);
                            $scope.currentPersonFriends = response['@graph'];
                        });
                }
            };
            $scope.hideForm = ($cookies.cx_secret && $cookies.userId); // userId eller null
            $scope.secret = $cookies.cx_secret || null;            
            $scope.currentPersomId = $cookies.userId || null;
            $scope.currentPerson = $scope.getPerson($cookies.userId);
            $scope.currentPersonMovies = [];
            $scope.currentPersonFriends = [];
            $scope.movieUrls =[];

            $scope.getSearch = function(movie) {
                if(movie.length >= 3) {
                    Search.get({title: movie}, function(res){
                        var movies = res["@graph"];
                        console.log(res);
                        $scope.movieResults = res;

                    });

                }
            };

            $scope.localMovieSearch = function(movieTitle) {
                if(movieTitle.length >= 3){
                    SuggestMovie.get({title: movieTitle}, function(res){
                        console.log(res["@graph"]);
                        $scope.movieResults = res;
                    });
                }
            };

            $scope.likesMovie = function(movie, context) {
                var movieId = movie['@id'];
                var prefix = movie['@id'].replace(/(.*):.*/, "\$1");
                var ns = context[prefix];
                if(ns) {
                    movieId = movieId.replace(/.*:(.*)/, ns+"\$1");

                }
                $http.put($scope.url + '/'+ getId($scope.currentPerson['@id']) + '/harSett',
                    movieId,
                    {headers: {'Access-Control-Allow-Methods': 'PUT, OPTIONS',
                        'cx_auth': $cookies.cx_secret,
                        'Content-Type': 'text/plain'}}
                ).success(function() {
                        $http.get($scope.url + '/'+ getId($scope.currentPerson['@id']) + '/harSett')
                            .success(function(response){
                                console.log(response);
                               $scope.currentPersonMovies = response['@graph'];
                            });
                    }).error(function() {
                        console.log('æsj!!!');
                    });
            };

            //henter alle i grafen
            $http({method: 'GET', url: $scope.url}).success(function(response){
                console.log(response);
                $scope.wholeGraph = response['@graph'];
            });


            $scope.addKjenner = function (inputString) {
                $http.put($scope.url + '/'+ getId($scope.currentPerson['@id']) + '/kjenner/' + getId(inputString['@id']),
                    null,
                    {headers: {'Access-Control-Allow-Methods': 'PUT, OPTIONS',
                        'cx_auth': $cookies.cx_secret,
                        'Access-Control-Allow-Headers': 'Content-Type'}
                    })
                    .success(function(){
                        $http.get($scope.url + '/'+ getId($scope.currentPerson['@id']) + '/kjenner')
                            .success(function(response){
                                console.log(response);
                                $scope.currentPersonFriends = response['@graph'];
                            });

                    }).error(function(err){
                        $scope.addKjennerError = "Kunne ikke legge til " + inputString['firstName'] + " som din venn";
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
                    console.log(headers('cx_secret'));
                    $cookies.cx_secret = headers("cx_secret");
                    $scope.currentPerson = response["@graph"][0];
                    $scope.currentPersonId = getId($scope.currentPerson["@id"]);
                    $cookies.userId = getId($scope.currentPerson['@id']);
                    $scope.hideForm = ($cookies.userId && $cookies.cx_secret);
                });

            };
            function getId(url){
                if(url) {
                    var id = url.substring(Config.resourcePersonBaseUrl.length);
                    console.log(url + " id: " +id);
                    return id;
                } return null;
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
