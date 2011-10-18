package au.com.onegeek.lambda.splash;

import java.io.OutputStream;
import java.io.PrintStream;

import cli.clamshell.api.Configurator;
import cli.clamshell.api.Context;
import cli.clamshell.api.SplashScreen;

public class LambdaCliSplashScreen implements SplashScreen {
	    private static StringBuilder screen;
	    public void render(Context ctx) {
	        PrintStream out = new PrintStream ((OutputStream)ctx.getValue(Context.KEY_OUTPUT_STREAM));
	        out.println(screen);
	    }

	    public void plug(Context plug) {
	        screen  = new StringBuilder();
	        
	        // Uses Colossal. Doom, larry3d, hollywood also good from http://www.network-science.de/ascii/
	        // TODO: on certain significant dates, put something crazy in there (April fools, Xmas.. etc.)
	        if ((Math.random() * 10) > 1) {
		        screen
		        .append(Configurator.VALUE_LINE_SEP)    
		        .append(Configurator.VALUE_LINE_SEP)    
		        .append(Configurator.VALUE_LINE_SEP)    
		        .append(Configurator.VALUE_LINE_SEP)    
				.append(" .d8888b.  888    .d                       888           888           .d8888b.  888               888 888").append(Configurator.VALUE_LINE_SEP)
				.append("d88P  Y88b 888     d88                     888           888          d88P  Y88b 888               888 888").append(Configurator.VALUE_LINE_SEP)
				.append("888    888 888      888                    888           888          Y88b.      888               888 888").append(Configurator.VALUE_LINE_SEP)
				.append("888        888       888     88888b.d88b.  88888b.   .d88888  8888b.   \"Y888b.   88888b.   .d88b.  888 888").append(Configurator.VALUE_LINE_SEP)
				.append("888        888     888888    888 \"888 \"88b 888 \"88b d88\" 888     \"88b     \"Y88b. 888 \"88b d8P  Y8b 888 888").append(Configurator.VALUE_LINE_SEP)
				.append("888    888 888    888  888   888  888  888 888  888 888  888 .d888888       \"888 888  888 88888888 888 888").append(Configurator.VALUE_LINE_SEP)
				.append("Y88b  d88P 888  888     888  888  888  888 888 d88P Y88b 888 888  888 Y88b  d88P 888  888 Y8b.     888 888 ").append(Configurator.VALUE_LINE_SEP)
				.append(" Y8888P\"   888 888       888 888  888  888 88888P\"   \"Y88888 \"Y888888  \"Y8888P\"  888  888  \"Y8888  888 888 ").append(Configurator.VALUE_LINE_SEP)
		        .append(Configurator.VALUE_LINE_SEP)
		        .append("                                                                           Lambda Command-Line Interpreter").append(Configurator.VALUE_LINE_SEP)
		        .append(Configurator.VALUE_LINE_SEP)
		        .append("Java version: ").append(System.getProperty("java.version")).append(Configurator.VALUE_LINE_SEP)
		        .append("Java Home: ").append(System.getProperty("java.home")).append(Configurator.VALUE_LINE_SEP)
		        .append("OS: ").append(System.getProperty("os.name")).append(", Version: ").append(System.getProperty("os.version"))
		        .append(Configurator.VALUE_LINE_SEP)
		        .append(Configurator.VALUE_LINE_SEP);
	        } else {
	        	screen
	        	.append(Configurator.VALUE_LINE_SEP)    
	        	.append(Configurator.VALUE_LINE_SEP)    
	        	.append(Configurator.VALUE_LINE_SEP)    
	        	.append(Configurator.VALUE_LINE_SEP)               
				.append("        CCCCCCCCCCCCClllllll                                          b::::::b                        d::::::d                     SSSSSSSSSSSSSSS hhhhhhh                                lllllll lllllll").append(Configurator.VALUE_LINE_SEP)
				.append("     CCC::::::::::::Cl:::::l                                          b::::::b                        d::::::d                   SS:::::::::::::::Sh:::::h                                l:::::l l:::::l").append(Configurator.VALUE_LINE_SEP)
				.append("   CC:::::::::::::::Cl:::::l                                          b::::::b                        d::::::d                  S:::::SSSSSS::::::Sh:::::h                                l:::::l l:::::l").append(Configurator.VALUE_LINE_SEP)
				.append("  C:::::CCCCCCCC::::Cl:::::l                                           b:::::b                        d:::::d                   S:::::S     SSSSSSSh:::::h                                l:::::l l:::::l").append(Configurator.VALUE_LINE_SEP)
				.append(" C:::::C       CCCCCC l::::l   aaaaaaaaaaaaa      mmmmmmm    mmmmmmm   b:::::bbbbbbbbb        ddddddddd:::::d   aaaaaaaaaaaaa   S:::::S             h::::h hhhhh           eeeeeeeeeeee    l::::l  l::::l").append(Configurator.VALUE_LINE_SEP)
				.append("C:::::C               l::::l   a::::::::::::a   mm:::::::m  m:::::::mm b::::::::::::::bb    dd::::::::::::::d   a::::::::::::a  S:::::S             h::::hh:::::hhh      ee::::::::::::ee  l::::l  l::::l").append(Configurator.VALUE_LINE_SEP)
				.append("C:::::C               l::::l   aaaaaaaaa:::::a m::::::::::mm::::::::::mb::::::::::::::::b  d::::::::::::::::d   aaaaaaaaa:::::a  S::::SSSS          h::::::::::::::hh   e::::::eeeee:::::eel::::l  l::::l").append(Configurator.VALUE_LINE_SEP)
				.append("C:::::C               l::::l            a::::a m::::::::::::::::::::::mb:::::bbbbb:::::::bd:::::::ddddd:::::d            a::::a   SS::::::SSSSS     h:::::::hhh::::::h e::::::e     e:::::el::::l  l::::l").append(Configurator.VALUE_LINE_SEP)
				.append("C:::::C               l::::l     aaaaaaa:::::a m:::::mmm::::::mmm:::::mb:::::b    b::::::bd::::::d    d:::::d     aaaaaaa:::::a     SSS::::::::SS   h::::::h   h::::::he:::::::eeeee::::::el::::l  l::::l").append(Configurator.VALUE_LINE_SEP)
				.append("C:::::C               l::::l   aa::::::::::::a m::::m   m::::m   m::::mb:::::b     b:::::bd:::::d     d:::::d   aa::::::::::::a        SSSSSS::::S  h:::::h     h:::::he:::::::::::::::::e l::::l  l::::l").append(Configurator.VALUE_LINE_SEP)
				.append("C:::::C               l::::l  a::::aaaa::::::a m::::m   m::::m   m::::mb:::::b     b:::::bd:::::d     d:::::d  a::::aaaa::::::a             S:::::S h:::::h     h:::::he::::::eeeeeeeeeee  l::::l  l::::l").append(Configurator.VALUE_LINE_SEP)
				.append(" C:::::C       CCCCCC l::::l a::::a    a:::::a m::::m   m::::m   m::::mb:::::b     b:::::bd:::::d     d:::::d a::::a    a:::::a             S:::::S h:::::h     h:::::he:::::::e           l::::l  l::::l").append(Configurator.VALUE_LINE_SEP)
				.append("  C:::::CCCCCCCC::::Cl::::::la::::a    a:::::a m::::m   m::::m   m::::mb:::::bbbbbb::::::bd::::::ddddd::::::dda::::a    a:::::a SSSSSSS     S:::::S h:::::h     h:::::he::::::::e         l::::::ll::::::l").append(Configurator.VALUE_LINE_SEP)
				.append("   CC:::::::::::::::Cl::::::la:::::aaaa::::::a m::::m   m::::m   m::::mb::::::::::::::::b  d:::::::::::::::::da:::::aaaa::::::a S::::::SSSSSS:::::S h:::::h     h:::::h e::::::::eeeeeeee l::::::ll::::::l").append(Configurator.VALUE_LINE_SEP)
				.append("     CCC::::::::::::Cl::::::l a::::::::::aa:::am::::m   m::::m   m::::mb:::::::::::::::b    d:::::::::ddd::::d a::::::::::aa:::aS:::::::::::::::SS  h:::::h     h:::::h  ee:::::::::::::e l::::::ll::::::l").append(Configurator.VALUE_LINE_SEP)
				.append("        CCCCCCCCCCCCCllllllll  aaaaaaaaaa  aaaammmmmm   mmmmmm   mmmmmmbbbbbbbbbbbbbbbb      ddddddddd   ddddd  aaaaaaaaaa  aaaa SSSSSSSSSSSSSSS    hhhhhhh     hhhhhhh    eeeeeeeeeeeeee llllllllllllllll").append(Configurator.VALUE_LINE_SEP)
	        	.append(Configurator.VALUE_LINE_SEP)
	        	.append("                                                                                                                                                                           Lambda Command-Line Interpreter").append(Configurator.VALUE_LINE_SEP)
	        	.append(Configurator.VALUE_LINE_SEP)
	        	.append("Java version: ").append(System.getProperty("java.version")).append(Configurator.VALUE_LINE_SEP)
	        	.append("Java Home: ").append(System.getProperty("java.home")).append(Configurator.VALUE_LINE_SEP)
	        	.append("OS: ").append(System.getProperty("os.name")).append(", Version: ").append(System.getProperty("os.version"))
	        	.append(Configurator.VALUE_LINE_SEP)
	        	.append(Configurator.VALUE_LINE_SEP);        	
	        }
	    }
	}