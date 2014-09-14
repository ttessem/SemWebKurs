'use strict';

describe('controllers', function(){
	beforeEach(module('myApp.controllers'));

	it('should...', inject(function($controller) {
		var myCtrl = $controller('MainCtrl', { $scope {}});
		expect(myCtrl).toBeDefined();
	}));
	it('should load and compile right template', function() {
		element(by.linkText('Person')).click();
		var content = element(by.css('[ng-view]')).getText();
		expect(content).toMatch(/Person/);

		element.(by.partialLinkText('Mov')).click();
		content = element(by.css('[ng-view]')).getText();
		expect(content).toMatch(/Movie/);
	});
})