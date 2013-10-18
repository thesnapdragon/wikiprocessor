#!/usr/bin/env node

var port = parseInt(process.argv[2]);
if (isNaN(port)) {
    console.log('ERROR! First parameter must be integer!');
    process.exit(-1);
}

var http = require('http'),
    ParserEnv = require('../lib/mediawiki.parser.environment.js').MWParserEnvironment,
    ParsoidConfig = require('../lib/mediawiki.ParsoidConfig.js').ParsoidConfig,
    Util = require('../lib/mediawiki.Util.js').Util;

var prefix = 'en';
var parsoidConfig = new ParsoidConfig(null, {defaultWiki: prefix});

console.log('Starting parsoid...');

http.createServer(function (req, res) {
    
    var body = "";
    
    res.writeHead(200, {"Content-Type": "text/html"});

    req.on('data', function (chunk) {
        body += chunk;
    });

    req.on('end', function () {
        ParserEnv.getParserEnv(parsoidConfig, null, prefix, null, function (err, env) {
            // Init parsers, serializers, etc.
            var parserPipeline = Util.getParserPipeline(env, 'text/x-mediawiki/full');

            // process input
            var processInput = function() {
                parserPipeline.on('document', function(document) {
                    var doc = Util.serializeNode(document.body);
                    doc += "\n";
                    res.end(doc);
                });
                env.setPageSrcInfo(body);
                parserPipeline.processToplevelDoc(env.page.src);
            };

            processInput();
        });
    });
}).listen(port);
