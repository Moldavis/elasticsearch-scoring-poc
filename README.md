# PoC project to test an elasticsearch scoring bug

This project helps to illustrate a scoring issue with identical elasticsearch
documents. For more information check out the [stackoverflow issue](https://stackoverflow.com/questions/72769815/different-scores-for-identical-documents-after-upgrading-from-spring-data-elasti)


## Setup

This project requires a locally running elasticsearch cluster reachable under port 9200. If you want to change this, check out EmbeddedElasticsearchConfig.java.


## Issue

The DocumentScoringTest indexes 2 identical documents and fetches them with a matching query. Afterwards it selects the highest scoring documents. We would expect 2 documents with identical scores to be found.

In spring boot 2.5 this works flawlessly and 2 documents are always returned.

In spring boot 2.6 this fails most of the time with only 1 document being returned. The problem is most likely that the documents get fetched from different shards and the individual shards scoring them differently.
