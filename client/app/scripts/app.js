(function (angular) {
    'use strict';

    var app = angular.module('app',
        ['ngRoute', 'ngResource', 'ui.bootstrap', 'ngCookies']);

    app.config(['$routeProvider', function ($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'views/person.html',
                controller: 'PersonCtrl'
            })
//            .when('/person', {
//                templateUrl: '',
//                controller: ''
//            })
//            .when('/movie', {
//                templateUrl: 'views/movie.html',
//                controller: 'MovieCtrl'
//            })
            .otherwise({
                redirectTo: '/'
            });
    }]);

    app.config(['$httpProvider', 
        function ($httpProvider) {

            // Enable interceptors
            // $httpProvider.interceptors.push('httpInterceptor');

            // Enable cross domain calls
            $httpProvider.defaults.useXDomain = true;
            delete $httpProvider.defaults.headers.common['X-Requested-With'];

            // Configure angular-local-storage to use sessionStorage and a proper prefix
            // localStorageServiceProvider.prefix = 'app';
            // localStorageServiceProvider.storageType = 'sessionStorage';
        }
    ]);
}(angular));



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
