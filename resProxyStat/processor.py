import json
import time
from pprint import pprint

with open('records.json', encoding="utf8") as data_file: 
    cmdi_records = json.load(data_file)

    print("Stats from ", time.strftime("%Y-%m-%d %H:%M:%S"))
    print("Total number of records: ", len(cmdi_records['response']['docs']))

    cnt_resource = 0
    cnt_metadata = 0
    cnt_landing_page = 0
    cnt_search_service = 0
    cnt_search_page = 0

    
    #Different access points for the 'same' resource (raw 'Resource', vs. 'Landing Page' vs. Search Service)
    case1 = 0
    
    #Record represents a collection, all ResourceProxies point to other metadata records (Metadata)
    case2 = 0
    
    #Record represents/described a number of distinct resources ('Resource')
    case3 = 0
    
    for cmdi in cmdi_records['response']['docs']: 
        #counts in individual record
        resource = len(cmdi.get('_resourceRef')) if cmdi.get('_resourceRef') else 0
        landing_page = len(cmdi.get('_landingPageRef')) if cmdi.get('_landingPageRef') else 0
        search_page = len(cmdi.get('_searchPageRef')) if cmdi.get('_searchPageRef') else 0
        search_service = len(cmdi.get('_contentSearchRef')) if cmdi.get('_contentSearchRef') else 0
        metadata = len(cmdi.get('_hasPart')) if cmdi.get('_hasPart') else 0

        # Total counts
        cnt_resource += resource
        cnt_metadata += metadata
        cnt_landing_page += landing_page
        cnt_search_service += search_service
        cnt_search_page += search_page

        # Case1
        if resource <= 1 and search_service <= 1 and landing_page <= 1 and search_page <= 1:
            case1 += 1

        # Case2
        if metadata > 0 and (resource + search_service + landing_page + search_page == 0):
            case2 += 1

        # Case3
        if resource > 1:
            case3 += 1

        #pprint(cmdi)

    print("Total number of Resource resProxies: ", cnt_resource)
    print("Total number of Metadata resProxies: ", cnt_metadata)
    print("Total number of LandingPage resProxies: ", cnt_landing_page)
    print("Total number of SearchService resProxies: ", cnt_search_service)
    print("Total number of SearchPage resProxies: ", cnt_search_page)
    
    print("Different access points for the 'same' resource (raw 'Resource', vs. 'Landing Page', vs. Search Service): ", case1)
    print("Record represents a collection, all ResourceProxies point to other metadata records (Metadata): ", case2)
    print("Record represents/described a number of distinct resources ('Resource'): ", case3)