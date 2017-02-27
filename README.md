
A repo for collecting files related to mapping of CMDI to CIDOC-PE in the context of the project Parthenos.

It contains separate folders for individual mappings
with sample input and output files as well as the x3ml files containing the defined mappings.

Separate folder "ontologies" collects a snaphost of the underlying ontologies.

Mappings in X3ml:

300 - E-Service - IULA UPF OAI Archive: Services for NLP

303 - Dataset - modifications from George

305 - Example of generator's output (our generator)

307 - copy of 303 with updates from matteo

313 - Dataset - output of the generator

315 - Dataset/Multiresource - a mapping for a CMDI record describing multiple Resources


#Generator
This application parses CLARIN profiles from a given URL and generates x3ml (xml) mapping files

##How to run:

###Requirements:
	java8
	maven

###Installation
####Get source code from github and compile it with command:
	mvn clean compile assembly:single
	

###Running
####Run the programm with following command:
	java -jar jar_name -profile profileURL creatorType -resourceType namespace-quilified-CIDOC-CRM-class 
	
###Parameters
1. profileURL   - url of the profiles xsd files
2. creatorType  - type of parthenos entity that created CMDI record (cmd:CMD/cmd:Header/cmd:MdCreator field). Use -actor or -software
3. resourceType - type of corresponding parthenos entity for the resource. Use crmpe:PE8_E-Service, crmpe:PE24_Volatile_Dataset or crmdig:D14_Software

###Example
####print help
	java -jar parthenos_mapping.jar -help

####generate x3ml mapping file for OLAC-DcmiTerms profile for case when creator is a software (D14_Software) and type of the resource a service (PE8_E-Service)
	java -jar parthenos_mapping.jar -profile https://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/1.x/profiles/clarin.eu:cr1:p_1288172614026/xsd -software -resourceType crmpe:PE8_E-Service
	
