// Converts a selenium test case from Selenese commands to CSV files in the format defined by MIT
// Borrowed most of the code from: http://wiki.openqa.org/display/SIDE/Adding+Custom+Format

var SEPARATORS = {
  comma: ",",
  tab: "\t"
};

function formatCommands(name, commands) {
  var sep = SEPARATORS[options['separator']];
  var result = name + sep + sep + "\n";
  for (var i = 0; i < commands.length; i++) {
    var command = commands[i];
    if (command.type == 'command') {
      result += sep + command.command + sep + command.target + sep + command.value + "\n";
    }
  }
  return result;
}

function parse(testCase, source) {
  var doc = source;
  var commands = [];
  var sep = SEPARATORS[options['separator']];
  while (doc.length > 0) {
    var line = /(.*)(\r\n|[\r\n])?/.exec(doc);
    var array = line[1].split(sep);
    if (array.length >= 3) {
      var command = new Command();
      command.command = array[0];
      command.target = array[1];
      command.value = array[2];
      commands.push(command);
    }
    doc = doc.substr(line[0].length);
  }
  testCase.setCommands(commands);
}

function format(testCase, name) {
  return formatCommands(name, testCase.commands);
}

options = {separator: 'comma'};

configForm =
    '<description>Separator</description>' +
	'<menulist id="options_separator">' +
	'<menupopup>' +
	'<menuitem label="Comma" value="comma"/>' +
	'<menuitem label="Tab" value="tab"/>' +
	'</menupopup>' +
	'</menulist>';