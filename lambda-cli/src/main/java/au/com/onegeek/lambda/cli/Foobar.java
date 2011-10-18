package au.com.onegeek.lambda.cli;

public class Foobar {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String formatter = "%n\t%1$-20s %2$s %3$s";
		
		if ((Math.random() * 10) > 5) {
			System.out.println("greater than 5");
		} else {
			System.out.println("less than or equal to 5");
			
		}
		
		StringBuffer foo = new StringBuffer("");
		foo
		.append(" .d8888b.  888    .d                       888           888           .d8888b.  888               888 888").append("\n")
		.append("d88P  Y88b 888     d88                     888           888          d88P  Y88b 888               888 888").append("\n")
		.append("888    888 888      888                    888           888          Y88b.      888               888 888").append("\n")
		.append("888        888       888     88888b.d88b.  88888b.   .d88888  8888b.   \"Y888b.   88888b.   .d88b.  888 888").append("\n")
		.append("888        888     888888    888 \"888 \"88b 888 \"88b d88\" 888     \"88b     \"Y88b. 888 \"88b d8P  Y8b 888 888").append("\n")
		.append("888    888 888    888  888   888  888  888 888  888 888  888 .d888888       \"888 888  888 88888888 888 888").append("\n")
		.append("Y88b  d88P 888  888     888  888  888  888 888 d88P Y88b 888 888  888 Y88b  d88P 888  888 Y8b.     888 888 ").append("\n")
		.append(" Y8888P\"   888 888       888 888  888  888 88888P\"   \"Y88888 \"Y888888  \"Y8888P\"  888  888  \"Y8888  888 888 ").append("\n");
		
		System.out.println(foo);
		System.out.println();
		
		System.out.print(String.format(formatter,"-b, -browser", "", "Specifies the browsers for which to run the tests against. This parameter can be used multiple times. e.g. -b firefox -b ie7 -b chrome. Defaults to 'firefox'"));
		System.out.print(String.format(formatter,"-f, -filename", "", "Specifies the location of input file and dataset if -dataset not set."));
		System.out.print(String.format(formatter,"-d, -dataset", "", "Specifies the location of dataset."));
		System.out.print(String.format(formatter,"-e, -environment", "", "Specifies the hostname for which to run the tests against."));
		
		System.out.print(String.format("%n%n\tClambda Shell (surface tension) supports the @ syntax to import the commandline args." +
										"\nExample: 'mfellows > lambda @/Users/mfellows/Desktop/retail.txt' " +
										"\nIf you're really lazy, create a ~/.config file and put your defaults there so you can do this: 'mfellows > lambda @retail.txt"));
		
		
//		   -b, -browser    Specifies the browsers for which to run the tests against. This parameter can be used multiple times. e.g. -b firefox -b ie7 -b chrome. Defaults to 'firefox'
//		   -f, -filename    Specifies the location of input file and dataset if -dataset not set.
//		    -d, -dataset    Specifies the location of dataset.
//		 -e, -environment    Specifies the hostname for which to run the tests against.
	}

}
