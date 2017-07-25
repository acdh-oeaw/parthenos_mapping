package at.ac.acdh;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.*;

import com.ximpleware.VTDException;

import at.ac.acdh.transformer.ProfileTransformer;
import at.ac.acdh.transformer.utils.XMLIOService;
import gr.forth.x3ml.X3ML;


public class Main {
	
	private static final String HELP_TEXT_HEADER = "<-mappingXml path/to/mapping/xml> -profile url_of_xsd  <-conditions cond1 cond2 ...>\n";

	public static void main(String[] args) throws Exception {
		new Main().process(args);
	}
		
		
	private void process(String[] args) throws VTDException, JAXBException{	
		
		CommandLineParser parser = new DefaultParser();

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
		
		String mappingXml = cli.getOptionValue("mappingXml"); 
		String profileUrl = cli.getOptionValue("profile");
		List<String> conditions = (cli.getOptionValues("conditions") != null?Arrays.asList(cli.getOptionValues("conditions")):null); 
		
		X3ML x3ml = mappingXml != null? 
			new ProfileTransformer().transform(mappingXml, profileUrl, conditions):
			new ProfileTransformer().transform(profileUrl, conditions);
		
			
		new XMLIOService<X3ML>().marshal(x3ml, System.out);		
	}
	
	private Options createHelpOption() {
		Option help = new Option("help", "print this message");
		Options options = new Options();
		options.addOption(help);
		return options;
	}

	private Options createOptions() {
		
		Option mappingFile = Option.builder("mappingXml").hasArg().required(false).desc("xml file with mappings").build();		
		Option profile = Option.builder("profile").hasArg().required(true).desc("URL of CLARIN profile XSD").build();		
		Option conditions = Option.builder("conditions").hasArgs().desc("Conditions to exclude entities from xml").build();
		
		Options options = new Options();
		return options
			.addOption(mappingFile)
			.addOption(profile)
			.addOption(conditions);
	}

}
