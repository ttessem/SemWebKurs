(function (angular) {
  'use strict';
  angular.module('appServices', []);
  angular.module('appControllers', []);
  angular.module('appDirectives', []);

  var app = angular.module('app', 
    ['ngRoute','appServices', 'appControllers', 'appDirectives']);

  app.config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('#/person', {
        templateUrl: 'views/person.html',
        controller: 'PersonCtrl'
      })
      .when('#/movie', {
        templateUrl: 'views/movie.html',
        controller: 'MovieCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
}(angular));