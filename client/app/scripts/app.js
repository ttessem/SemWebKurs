  'use strict';

  var app = angular.module('app', 
    ['ngRoute']);

  app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/', {
         templateUrl: 'views/main.html',
         controller: 'MainCtrl'
       })
      .when('/person', {
        templateUrl: 'views/person.html',
        controller: 'PersonCtrl'
      })
      .when('/movie', {
        templateUrl: 'views/movie.html',
        controller: 'MovieCtrl'
      })
      .otherwise({
        redirectTo: '/person'
      });
  }]);




// config(function($routeProvider, $locationProvider) {
//   $routeProvider
//    .when('/Book/:bookId', {
//     templateUrl: 'book.html',
//     controller: 'BookController',
//     resolve: {
//       // I will cause a 1 second delay
//       delay: function($q, $timeout) {
//         var delay = $q.defer();
//         $timeout(delay.resolve, 1000);
//         return delay.promise;
//       }
//     }
//   })
//   .when('/Book/:bookId/ch/:chapterId', {
//     templateUrl: 'chapter.html',
//     controller: 'ChapterController'
//   });