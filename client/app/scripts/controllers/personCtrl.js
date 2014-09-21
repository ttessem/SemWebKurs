(function(app){
    'use strict';

    app.controller('PersonCtrl', ['$scope', '$location', '$http',  
        'HentPerson', 'Person', 'Kjenner', 'Config', '$resource',   
        function ($scope, $location, $http, HentPerson, Person, Kjenner,
            Config, $resource) {
            
            $scope.method = 'POST';
            $scope.url = Config.apiBaseUrlPerson;
            $scope.venner = Person.get();
            $scope.selected = {};
            $scope.graph = {};
            $scope.wholeGraph = {};

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
                console.log(inputString);

                $http({method: 'PUT',
                        url: $scope.url + "/1/kjenner/2"
                        // headers: {'Access-Control-Allow-Methods': 'PUT, OPTIONS',
                        // 'Access-Control-Allow-Headers': 'Content-Type'}
                    })
                .success(function(){
                    console.log("Jipppiiiii");
                }).error(function(err){
                    console.log("æssshhh");
                }); 
                // Kjenner.addKjenner({
                //     id: 1,
                //     kjenner_id: getId(inputString["@id"])
                // }, function () {
                // $scope.feedbackTitle = "Your message has been edited";
                //     HentPerson.hentPerson({
                //         id: $scope.currentPerson["@id"]
                //     }, function(person){
                //         $scope.currentPerson = person;
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
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }).success(function(response){
                    console.log("Suksess");
                    console.log(response);
                    $scope.currentPerson = response["@graph"][0];
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