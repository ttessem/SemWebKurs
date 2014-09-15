(function(app) {
	app.directive('autoComplete', ['$scope', '$timeout', function($scope, $timeout) {

		return function(scope, el, attr) {
			el.autoComplete({
				source: scope[attrs.uiItems],
                select: function() {
                    $timeout(function() {
                      el.trigger('input');
                    }, 0);		
			})
		}
	}])

})(angular.module('app'));