import sys
import os
import pathlib
from lxml import etree

for path, subdirs, files in os.walk(sys.argv[1]):
    creators = set()
    for cmdi_record in files:
        tree = etree.parse(path + '/' + cmdi_record)
        creator = tree.xpath('/cmd:CMD/cmd:Header/cmd:MdCreator',
        namespaces={'cmd' : 'http://www.clarin.eu/cmd/1'})
        if len(creator) == 1 and creator[0].text != None:
            creators.add(creator[0].text)

    for creator in creators:
        print(creator)