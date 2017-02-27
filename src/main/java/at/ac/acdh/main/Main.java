package at.ac.acdh.main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import at.ac.acdh.transformer.ProfileTransformer;
import at.ac.acdh.transformer.utils.CMDICreatorType;
import at.ac.acdh.transformer.utils.XMLIOService;
import gr.forth.x3ml.X3ML;


public class Main {
	
	static String HELP_TEXT_HEADER = "(-profile url_of_xsd) (-actor OR -software) (-resourceType namespace-quilified-CIDOC-CRM-class)\n"
			+ "For the resource type please use one of following:"
			+ "\n\tcrmpe:PE8_E-Service"
			+ "\n\tcrmpe:PE24_Volatile_Dataset"
			+ "\n\tcrmdig:D14_Software";
	static String HELP_TEXT_FOOTER = "\nExample of parameters: -profile \"https://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/1.x/profiles/clarin.eu:cr1:p_1288172614026/xsd\" "
			+ "-software -resourceType crmpe:PE8_E-Service\n";

	public static void main(String[] args) throws Exception {
		
		CommandLineParser parser = new PosixParser();

		Options helpOptions = createHelpOption();
		Options options = createOptions();

		CommandLine cli = null;
		HelpFormatter formatter = new HelpFormatter();		
		try {
			cli = parser.parse(helpOptions, args);
			if (cli.hasOption("help")) {
				formatter.printHelp(HELP_TEXT_HEADER, "description of parameters", options, HELP_TEXT_FOOTER);
				return;
			}
		} catch (org.apache.commons.cli.ParseException e) {
			// do nothing
		}

		try {
			cli = parser.parse(options, args);
		} catch (org.apache.commons.cli.ParseException e) {
			formatter.printHelp(HELP_TEXT_HEADER, "description of parameters", options, HELP_TEXT_FOOTER);
			return;
		}
		CMDICreatorType creatorType = cli.hasOption("actor")? CMDICreatorType.ACTOR : CMDICreatorType.SOFTWARE;	
		
		X3ML x3ml;
		if(cli.hasOption("resourceType")){
			x3ml = new ProfileTransformer().transform(cli.getOptionValue("profile"), creatorType, cli.getOptionValue("resourceType"));
		}else{
			x3ml = new ProfileTransformer().transform(cli.getOptionValue("profile"), creatorType);
		}
		
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
		
		OptionGroup creatorType = new OptionGroup();
		creatorType.addOption(OptionBuilder.withDescription("Use this when CMDI is created by an software").create("software"));
		creatorType.addOption(OptionBuilder.withDescription("Use this when CMDI is created by an person or institution").create("actor"));
		creatorType.setRequired(true);
		
		Option resourceType = OptionBuilder.hasArg().isRequired(false).withDescription("Related CIDOC-CRM class for the type of the resource").create("resourceType");

		Options options = new Options();
		options.addOption(profile);
		options.addOptionGroup(creatorType);
		options.addOption(resourceType);

		return options;
	}

}
