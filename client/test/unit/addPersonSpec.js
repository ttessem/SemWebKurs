describe('Adding person', function() {
	it('Should load the correct template', function(){
		element(by.linkText('Person')).click();
		var content = element(by.css('[ng-view]')).getText();
		expect(content).toMatch(/Person/);
		expect(content).toMatch(/Name/);
		expect(content).toMatch(/Gender/);
		expect(content).toMatch(/Age/);
		expect(content).toMatch(/Study program/);
	});	
});

// it('should load and compile correct template', function() {
//   element(by.linkText('Moby: Ch1')).click();
//   var content = element(by.css('[ng-view]')).getText();
//   expect(content).toMatch(/controller\: ChapterController/);
//   expect(content).toMatch(/Book Id\: Moby/);
//   expect(content).toMatch(/Chapter Id\: 1/);

//   element(by.partialLinkText('Scarlet')).click();

//   content = element(by.css('[ng-view]')).getText();
//   expect(content).toMatch(/controller\: BookController/);
//   expect(content).toMatch(/Book Id\: Scarlet/);
// });