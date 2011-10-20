var SEPARATORS = {
  comma: ",",
  tab: "\t"
};

function formatCommands(commands) {
  var result = '';
  var sep = SEPARATORS[options['separator']];
  for (var i = 0; i < commands.length; i++) {
    var command = commands[i];
    if (command.type == 'command') {
      result += command.command + sep + command.target + sep + command.value + "\n";
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
  return formatCommands(testCase.commands);
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
