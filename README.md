
A repo for collecting files related to mapping of CMDI to CIDOC-PE in the context of the project Parthenos.

It contains separate folders for individual mappings
with sample input and output files as well as the x3ml files containing the defined mappings.

Separate folder "ontologies" collects a snaphost of the underlying ontologies.

# Mappings in X3ml 

## PE1.8 and 1st release of the generator policy 

300 - E-Service - IULA UPF OAI Archive: Services for NLP (Deprecated)

303 - Dataset - modifications from George (Deprecated)

305 - Example of generator's output (our generator)(Deprecated)

307 - copy of 303 with updates from matteo (Deprecated)

313 - Dataset - output of the generator (Deprecated)

315 - Dataset/Multiresource - a mapping for a CMDI record describing multiple Resources (Deprecated)

334 - Deutsches Textarchiv - NERLiX VRE handmade mapping from Matteo and Davor

## PE1.11 and 2nd release of the generator policy

548 - Dataset - Official Mapping

554 - Service - Official Mapping

403 -NERLiX - Stable Mapping (Unpublished)

404 -NERLiX - Testing Mapping (Unpublished)

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

Java 9:

	java -jar --add-modules java.xml.bind  x3ml-gen.jar -profile <arg> -mappingXml <arg> -conditions <arg> <arg> ...


### Parameters

1. profile    - url of the profiles xsd files
2. mappingXml - xml file that contains mappings. See CMDI2CIDOC.xml
3. conditions - array of strings to filter conditional enitities. If parameter specified only entitities with condition that matches one of the passed values will be kept

### Example	

#### Generate x3ml mapping file for DATASET.

sudo java -jar  x3ml-gen.jar -profile https://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/1.x/profiles/clarin.eu:cr1:p_1271859438164/xsd -mappingXml path/to/CMDI2CIDOC.xml -conditions creator-software dataset > map.x3m   
	

#### Generate x3ml mapping file for SERVICE.

java -jar x3ml-gen.jar -profile https://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/1.x/profiles/clarin.eu:cr1:p_1288172614026/xsd -mappingXml path/to/CMDI2CIDOC.xml -conditions service > map.x3m

### Running from Batch folder

1. run.sh script - will process all the profiles specified in the cmdi.json file and all the .xml mapping files from mapping folder. All the mapping files created by the script will be stored in x3ml_mappings folder automatically created running the script.

2. x3ml-gen.jar file - executable file creted compiling the source code (copied from target folder)

3. .xml mapping files in mappings folder - mapping files containing mapping rules from CMDI to CIDOC-PE model.


	
	
