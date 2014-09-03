'use strict';
describe('whishList', function() {
	browser.get('index.html');
	it('should automatically redirect to /about when ', function() {
		expect(browser.getLocationAbsUrl()).toMatch("/view");
	})
})