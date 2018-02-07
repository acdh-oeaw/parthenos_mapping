#!/bin/bash

#jq library is required

function verbose {
	echo $1 
}

OUT_DIR=x3ml-mappings
MAPPING_XML=mappings/CMDI2CIDOC.xml

hash jq 2>/dev/null || { echo >&2 "jq library is required but it's not installed.  Aborting."; exit 1; }
hash wget 2>/dev/null || { echo >&2 "wget library is required but it's not installed.  Aborting."; exit 1; }

JSON_FILE=cmdi.json

rm -R $OUT_DIR
mkdir $OUT_DIR

#CIDOC class to x3ml-gen condition map
# declare -A types_to_conditions_map=(["crmpe:PE24_Volatile_Dataset"]="dataset" ["crmpe:PE8_E-Service"]="service")

declare counter=0

while IFS= read -r xsd &&
      IFS= read -r type; do
	  
	  #extract profile ID from url
	  profileId=$(grep -o 'p_[0-9]\{13\}' <<< $xsd)
	  
	  
	  mkdir $OUT_DIR/$profileId
	  ACTOR_X3ML=$OUT_DIR/$profileId/$profileId.actor.x3ml
	  SOFTWARE_X3ML=$OUT_DIR/$profileId/$profileId.software.x3ml
	  
	  #generate x3ml files
	  verbose "generating x3ml mappings for $xsd"
	  
	 if [ $type = "crmpe:PE8_E-Service" ]; then
	  	condition= "service"
          else if [ $type = "crmpe:PE24_Volatile_Dataset" ]; then
                condition = "dataset"
	  fi
	  
	  java -jar x3ml-gen.jar -mappingXml $MAPPING_XML -profile $xsd -conditions creator-actor $condition > $ACTOR_X3ML
	  java -jar x3ml-gen.jar -mappingXml $MAPPING_XML -profile $xsd -conditions creator-software $condition > $SOFTWARE_X3ML
	  
	  cmdRecords=$(jq -c -r .cmdi2pe[$counter].cmdi_records[] $JSON_FILE)
	  let counter=counter+1
	  
	  if [ -n "$cmdRecords" ]
	  then
		for cmdiUrl in $(echo $cmdRecords | sed "s/\s/ /g")
		do
			cmdi=$(basename "$cmdiUrl")
			wget $cmdiUrl > /dev/null 2>&1
			verbose "transforming $cmdi into $OUT_DIR/$profileId/$cmdi.rdf"
			#	format text/turtle
			java -jar x3ml.jar -format text/turtle -xml $cmdi -x3ml $ACTOR_X3ML -policy policy.xml -rdf $OUT_DIR/$profileId/$cmdi.rdf > /dev/null 2>&1
			rm $cmdi
		done
	fi
	  
done < <(jq -r '.cmdi2pe[] | .xsd, .pe_type' < $JSON_FILE)
