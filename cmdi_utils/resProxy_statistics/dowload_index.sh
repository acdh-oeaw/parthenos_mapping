#list of facets

#_contentSearchRef - SearchService
#_landingPageRef   - Landing page
#_searchPageRef    - SearchPage
#_resourceRef 	  - Resource
#_hasPart		  - Metadata

#collection
# _fileName

curl -GET "https://vlo.minerva.arz.oeaw.ac.at/vlo-solr/core0/select?q=*%3A*&fl=_fileName%2C+collection%2C+_resourceRef%2C+_landingPageRef%2C+_searchPageRef%2C+_contentSearchRef%2C+_hasPart&wt=json&indent=true&rows=1000000">records.json

python processor.py 'records.json'> stats.txt