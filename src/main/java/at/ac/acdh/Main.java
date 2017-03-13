package at.ac.acdh;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import at.ac.acdh.transformer.ProfileTransformer;
import at.ac.acdh.transformer.utils.XMLIOService;
import gr.forth.x3ml.X3ML;


public class Main {
	
	static String HELP_TEXT_HEADER = "-profile url_of_xsd  -conditions cond1 cond2 ...\n";

	public static void main(String[] args) throws Exception {
		
		CommandLineParser parser = new PosixParser();

		Options helpOptions = createHelpOption();
		Options options = createOptions();

		CommandLine cli = null;
		HelpFormatter formatter = new HelpFormatter();
		try {
			cli = parser.parse(helpOptions, args);
			if (cli.hasOption("help")) {
				formatter.printHelp(HELP_TEXT_HEADER, "description of parameters", options, "");
				return;
			}
		} catch (org.apache.commons.cli.ParseException e) {
			// do nothing
		}

		try {
			cli = parser.parse(options, args);
		} catch (org.apache.commons.cli.ParseException e) {
			formatter.printHelp(HELP_TEXT_HEADER, "description of parameters", options, "");
			return;
		}
		
		String profileUrl = cli.getOptionValue("profile");
		List<String> conditions = Arrays.asList(cli.getOptionValues("conditions")); 
		X3ML x3ml = new ProfileTransformer().transform(profileUrl, conditions);
		
		new XMLIOService().marshal(x3ml, System.out);		
	}
	

	
	private static Options createHelpOption() {
		Option help = new Option("help", "print this message");
		Options options = new Options();
		options.addOption(help);
		return options;
	}

	private static Options createOptions() {
		
		Option profile = OptionBuilder.hasArg().isRequired(true).withDescription("URL of CLARIN profile XSD").create("profile");
		
		Option conditions = OptionBuilder.hasArgs().isRequired(true).withDescription("Conditions to filter entities from xml").create("conditions");		
		
		//Option resourceType = OptionBuilder.hasArg().isRequired(false).withDescription("Related CIDOC-CRM class for the type of the resource").create("resourceType");

		Options options = new Options();
		options.addOption(profile).addOption(conditions);
		//options.addOption(resourceType);

		return options;
	}

}
