'use strict';

describe('controllers', function(){
	beforeEach(module('myApp.controllers'));

	it('should...', inject(function($controller) {
		var myCtrl = $controller('MainCtrl', { $scope {}});
		expect(myCtrl).toBeDefined();
	}));
	
})