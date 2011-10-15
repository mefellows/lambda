package au.com.onegeek.lambda.tests

def languages = new LanguageList()
assert 'Groovy' == languages.findGroovy()
assert 'Scala' == languages.findScala()
assert 'Java' == languages.findJava()
assert !languages.findRuby()
 
assert 'Groovy[cache]' == languages.findGroovy()
assert 'Scala[cache]' == languages.findScala()
assert 'Java[cache]' == languages.findJava()