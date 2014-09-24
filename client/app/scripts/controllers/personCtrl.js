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
                console.log("henter****person");
                if(id) {
                    console.log("get pserson id: " + id)
                $http.get($scope.url + '/'+ id)
                    .success(function(response) {
                        console.log('yeay:::: ' + response);
                        console.log(response);
                        $scope.currentPerson = response['@graph'][0];
                        return response['@graph'][0];
                    }).error(function(err) {
                        console.log('æsj!!!');
                        return null;
                    });
                }
            };
            $scope.hideForm = ($cookies.cx_secret && $cookies.userId); // userId eller null
            console.log("hideForm " + $scope.hideForm);
            $scope.secret = $cookies.cx_secret || null;            
            $scope.currentPersomId = $cookies.userId || null;
            $scope.currentPerson = $scope.getPerson($cookies.userId);
            console.log("CurrentPerson " + $scope.currentPerson);
            $scope.currentPersonMovies = [];
            $scope.currentPersonFriends = [];
            $scope.movieUrls =[];

            $scope.getSearch = function(movie) {
                if(movie.length >= 3) {
                    Search.get({title: movie}, function(res){
                        var movies = res["@graph"];
                        console.log(res);
                        $scope.movieResults = res;
//                        movies.forEach(function(m){
//                            console.log(m['@id']);
//                            var movieId = m['@id'];
//                            var dbpediaUrl = res["@context"].dbpedia;
//                            var movieUrl = movieId.replace('dbpedia:', dbpediaUrl);
//                            console.log(movieUrl);
//                            $scope.movieUrls.push(movieUrl);
//
//                        });

                    });

                }
            };
            $scope.localMovieSearch = function(movieTitle) {
                if(movieTitle.length >= 3){
                    SuggestMovie.get({title: movieTitle}, function(res){
                        console.log(res["@graph"]);
                        $scope.movieResults = res['@graph'];
                    });
                }
            };
            $scope.likesMovie = function(movie, context) {
                console.log('movie id: ' + movie);
//                var dbpediaUrl = res["@context"].dbpedia;
                var movieId = movie['@id'];
                var prefix = movie['@id'].replace(/(.*):.*/, "\$1");
                var ns = context[prefix];
                if(ns) {
                    movieId = movieId.replace(/.*:(.*)/, ns+"\$1");

                }
                console.log(movieId);
                $http.put($scope.url + '/'+ getId($scope.currentPerson['@id']) + '/harSett',
                    movieId,
                    {headers: {'Access-Control-Allow-Methods': 'PUT, OPTIONS',
                        'cx_auth': $cookies.cx_secret,
                        'Content-Type': 'text/plain'}}
                ).success(function() {
                        // $http.get($scope.url + '/'+ getId($scope.currentPerson['@id']))
                        //     .success(function(response) {
                        //         console.log('yeay:::: ' + response);
                        //         console.log(response);
                        //         $scope.currentPerson = response["@graph"][0];

                        //     }).error(function(err) {
                        //         console.log('æsj!!!');
                        //     });
                        $http.get($scope.url + '/'+ getId($scope.currentPerson['@id']) + '/harSett')
                            .success(function(response){
                                console.log(response);
                               $scope.currentPersonMovies = response['@graph'];
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
                        // var person = HentPerson.get({id: getId($scope.currentPerson["@id"])});
                        // console.log("Jipppiiiii");
                        // console.log(person);
                        $http.get($scope.url + '/'+ getId($scope.currentPerson['@id']) + '/kjenner')
                            .success(function(response){
                                console.log(response);
                                $scope.currentPersonFriends = response['@graph'];
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
