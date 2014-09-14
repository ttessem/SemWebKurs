'use strict';
describe('SemWebKursKlient', function() {
	browser.get('index.html');
	it('should automatically redirect to /about when ', function() {
		expect(browser.getLocationAbsUrl()).toMatch("/view");
	})
})