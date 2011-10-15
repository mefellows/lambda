package au.com.onegeek.lambda.tests

class LanguageList {
	def list = ['Java', 'Groovy', 'Scala']

	// Set metaClass property to ExpandoMetaClass instance, so we
	// can add dynamic methods.
	LanguageList() {
		def mc = new ExpandoMetaClass(LanguageList, false, true)
		mc.initialize()
		this.metaClass = mc
	}

	def methodMissing(String name, args) {
		// Intercept method that starts with find.
		if (name.startsWith("find")) {
			def result = list.find { it == name[4..-1] }
			// Add new method to class with metaClass.
			this.metaClass."$name" = {-> result + "[cache]" }
			result
		} else {
			throw new MissingMethodException(name, this.class, args)
		}
	}
}
