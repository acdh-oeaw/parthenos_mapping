#!/bin/bash
mvn clean compile assembly:single
chmod -R 777 target/
java -jar  target/x3ml-gen.jar -profile https://catalog.clarin.eu/ds/ComponentRegistry/rest/registry/1.x/profiles/clarin.eu:cr1:p_1380106710826/xsd -mappingXml /Users/matteo/GitHub/parthenos_mapping/src/main/resources/mapping/CMDI2CIDOC.xml -conditions creator-software dataset > test.x3ml
