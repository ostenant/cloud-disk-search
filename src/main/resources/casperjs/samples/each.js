/*eslint strict:0*/
/*global CasperError, console, phantom, require*/

var casper = require("casper").create();

var links = [
    "http://www.baidu.com/",
    "http://www.jd.com/",
    "http://www.taobao.com/"
];

casper.start();

casper.each(links, function(self, link) {
    this.thenOpen(link, function() {
        this.echo(this.getTitle() + " - " + link);
    });
});

casper.run();
