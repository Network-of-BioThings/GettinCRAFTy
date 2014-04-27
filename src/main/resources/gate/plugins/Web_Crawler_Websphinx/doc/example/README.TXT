example for running google pr and crawler in a pipeline


1. load google+crawl
2. select the google+crawl pipeline
3. set the following parameters for the crawler:
	1. dfs true or false
	2. select the corpus as google
      3. select max = 10
4. run the pipeline
At this stage the urls found in the google corpus are used to start the crawl and the crawl corpus contains all the documents


5. load annie with default settings
6. run annie over the crawl/google corpus
7. load the probability application (prob.gapp)
8. for prob pipeline select the same corpus as selected in step 6
   also select the desired entity to be used
9. run the prob pipeline

the selected entities are shown in the message tab