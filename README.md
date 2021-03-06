
A repo for collecting files related to mapping of CMDI to CIDOC-PE in the context of the project Parthenos.

It contains separate folders for individual mappings
with sample input and output files as well as the x3ml files containing the defined mappings.

Separate folder "ontologies" collects a snaphost of the underlying ontologies.

# Mappings in X3ml 

## PE1.8 and 1st release of the generator policy 

300 - E-Service - IULA UPF OAI Archive: Services for NLP

303 - Dataset - modifications from George

305 - Example of generator's output (our generator)

307 - copy of 303 with updates from matteo

313 - Dataset - output of the generator

315 - Dataset/Multiresource - a mapping for a CMDI record describing multiple Resources

334 - Deutsches Textarchiv - NERLiX VRE handmade mapping from Matteo and Davor

## PE1.11 and 2nd release of the generator policy

365 - Dataset - Official Mapping

373 - Service - Official Mapping

403 -NERLiX - Stable Mapping

404 -NERLiX - Testing MApping

# Generator
This application parses CLARIN profiles from a given URL and generates x3ml (xml) mapping files

## How to run:

### Requirements:
	java8
	maven
	jq library (for run.sh file in batch folder)

### Installation
#### Get source code from github and compile it with command:

	mvn clean compile assembly:single
	
It creates x3ml-gen.jar in target folder
	

### Running

#### Run the programm with following command:

	java -jar x3ml-gen.jar -profile <arg> -mappingXml <arg> -conditions <arg> <arg> ...
	
To print help:

	java -jar  x3ml-gen.jar -help

Java 9

	To make the JAXB APIs available at runtime, specify the following command-line option:
        --add-modules java.xml.bind ie:
			sudo java -jar  --add-modules java.xml.bind  x3ml-gen.jar -profile https://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/1.x/profiles/clarin.eu:cr1:p_1271859438164/xsd -mappingXml /Users/matteo/GitHub/parthenos_mapping/src/main/resources/mapping/CMDI2CIDOC.xml -conditions creator-software dataset > map.x3ml


### Parameters

1. profile    - url of the profiles xsd files
2. mappingXml - xml file that contains mappings. See CMDI2CIDOC.xml
3. conditions - array of strings to filter conditional enitities. If parameter specified only entitities with condition that matches one of the passed values will be kept

### Example	

#### generate x3ml mapping file for OLAC-DcmiTerms (describes datasets) profile for case when creator is a software (D14_Software).


	java -jar x3ml-gen.jar -profile https://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/1.x/profiles/clarin.eu:cr1:p_1288172614026/xsd -mappingXml path/to/CMDI2CIDOC.xml -conditions creator-software dataset


### batch folder

1. run.sh script - will process all the profiles specified in the cmdi.json file and all the .xml mapping files from mapping folder. All the mapping files created by the script will be stored in x3ml_mappings folder automatically created running the script.

2. x3ml-gen.jar file - executable file creted compiling the source code (copied from target folder)

3. .xml mapping files in mappings folder - mapping files containing mapping rules from CMDI to CIDOC-PE model.

### .xml mapping files

1. Mapping files are stored in batch/mappings folder and in src/main/resources/mapping folder. They are indipendent between each other and, in order to have a common mapping output, .xml files from src/main/resources/mapping should be copied in batch/mappings folder.

2. Mapping files from src/main/resources/mapping folder are processed via the executable file x3ml-gen.jar file according to the syntax i.e java -jar x3ml-gen.jar -profile https://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/1.x/profiles/clarin.eu:cr1:p_1288172614026/xsd -mappingXml path/to/CMDI2CIDOC.xml -conditions creator-software dataset
The mapping process will be runned just on the profile specified in the parameters i.e clarin.eu:cr1:p_1288172614026

3. Mapping files from batch/mappings folder are processed by default by the run.sh script. run.sh script provides to a "global" mapping for all the profiles specified in the cmdi.json file.
	
	
