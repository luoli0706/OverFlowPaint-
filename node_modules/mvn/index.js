"use strict";
var exec = require('child_process').exec;
var target = require('./target');
const path = require('path');

target.init();

var argv = process.argv.splice(2).join(' ');

module.exports = function(name) {
  exec(path.join(target.path, name + ` ${argv}`), function(err, out) {
    console.log(out.toString());
  })
};
