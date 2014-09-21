(function(app){
'use strict';
    app.controller('MovieCtrl', ['$scope', 'Movie', 'Search', function($scope, Movie, Search){
      $scope.page = { 'title': "Add movie"}; 
      $scope.movies = [];
      $scope.searchResult = [];
      
      var movies = Movie.get(function(){
        $scope.movies = movies;
      });


      var searchResult = Search.get({title: $scope.searchInput}, function() {
        $scope.searchResult = searchResult;
      });

    }]);
	// app.controller('MovieCtrl', ['$scope', 'Post', function($scope, Post){
	// $scope.page = { 'title': "Add movie"};
 //    $scope.post = new Post();
 //    $scope.posts = Post.query();
 //    $scope.save = function() {
 //       $scope.post.$save();
 //       $scope.posts.push($scope.post);
 //       $scope.post = new Post();
 //    }
 //    $scope.delete = function() {
 //        Post.delete(post);
 //        _.remove($scope.posts, post);
 //    }
	// }]);
 //    app.provider('Post', function(){
 //       this.$get = ['$resource', function($resource){
 //           var Post = $resource(CONFIG.API_BASE_URL, {}, {
 //               update: {
 //                   method: 'PUT'
 //               }
 //           })
 //           return Post;
 //       }];
 //    });
})(angular.module('app'));