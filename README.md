
A repo for collecting files related to mapping of CMDI to CIDOC-PE in the context of the project Parthenos.

It contains separate folders for individual mappings
with sample input and output files as well as the x3ml files containing the defined mappings.

Separate folder "ontologies" collects a snaphost of the underlying ontologies.

Mappings in X3ml (old version: generator policy without URI definition and PE 1.8):

300 - E-Service - IULA UPF OAI Archive: Services for NLP

303 - Dataset - modifications from George

305 - Example of generator's output (our generator)

307 - copy of 303 with updates from matteo

313 - Dataset - output of the generator

315 - Dataset/Multiresource - a mapping for a CMDI record describing multiple Resources

334 - Deutsches Textarchiv - NERLiX VRE handmade mapping from Matteo and Davor

Mappings in X3ml (new generator policy and PE1.11)

365 - Dataset - modifications from George and Matteo

# Generator
This application parses CLARIN profiles from a given URL and generates x3ml (xml) mapping files

## How to run:

### Requirements:
	java8
	maven

### Installation
#### Get source code from github and compile it with command:

	mvn clean compile assembly:single
	
It creates x3ml-gen.jar in target folder
	

### Running

#### Run the programm with following command:

	java -jar x3ml-gen.jar -profile <arg> -mappingXml <arg> -conditions <arg> <arg> ...
	
To print help:

	java -jar  x3ml-gen.jar -help
	
### Parameters

1. profile    - url of the profiles xsd files
2. mappingXml - xml file that contains mappings. See CMDI2CIDOC.xml
3. conditions - array of strings to filter conditional enitities. If parameter specified only entitities with condition that matches one of the passed values will be kept

### Example	

#### generate x3ml mapping file for OLAC-DcmiTerms (describes datasets) profile for case when creator is a software (D14_Software).


	java -jar x3ml-gen.jar -profile https://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/1.x/profiles/clarin.eu:cr1:p_1288172614026/xsd -mappingXml path/to/CMDI2CIDOC.xml -conditions creator-software dataset
	
	
