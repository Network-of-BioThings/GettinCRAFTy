MetaMap 
=================================

MetaMap, from the National Library of Medicine (NLM), maps biomedical text to the UMLS Metathesaurus and allows Metathesaurus concepts to be discovered in a text corpus.

The MetaMap plugin for GATE wraps the MetaMap Java API client to allow GATE to communicate with a remote (or local) MetaMap PrologBeans mmserver10 and MetaMap distribution. This allows the content of specified annotations (or the entire document content) to be processed by MetaMap and the results converted to GATE annotations and features.

To use this plugin, you will need access to a remote MetaMap server, or install one locally by downloading and installing the complete distribution:

http://metamap.nlm.nih.gov/

and Java PrologBeans mmserver

http://metamap.nlm.nih.gov/README_javaapi.html

The default mmserver10 location and port locations are localhost and 8066. To use a different server location and/or port, see the above API documentation and specify the --metamap_server_host and --metamap_server_port options within the metaMapOptions run-time parameter.


Parameters
==========

- Run-time
----------------
annotateNegEx: set this to true to add NegEx features to annotations (NegExType and NegExTrigger). See http://www.dbmi.pitt.edu/chapman/NegEx.html for more on NegEx

annotatePhrases: set to true to output MetaMap phrase-level annotations (generally noun-phrase chunks). Only phrases containing a MetaMap mapping will be annotated. Can be useful for later post-coordination of phrase-level terms that do not exist in a pre-coordinated form in UMLS.

annotNormalize: can be set to 'None', 'LeadingDeterminer', or 'AllDeterminers' to control whether and how determiners within the content of each of inputASTypes are handled. This is most useful for CoReference mode, so that 'his hypertension' and 'the hypertension' are both processed by MetaMap as simply 'hypertension' and treated as equivalent strings for coreference purposes.

excludeIfContains: If an entry within inputASTypes contains any of the annotations or annotation expressions (of the form Annotation.feature == value) in this list, then do not process this instance.

excludeIfWithin: If an entry within inputASTypes occurs within any of the annotations or annotation expressions (of the form Annotation.feature == value) in this list, then do not process that instance.

inputASName: input Annotation Set name. Use in conjunction with inputASTypes (see below). Unless specified, the entire document content will be sent to MetaMap. 

inputASTypes: only send the content of these annotations within inputASName to MetaMap and add new MetaMap annotations inside each. Unless specified, the entire document content will be sent to MetaMap. 

This parameter also accepts entries in the form Annotation.feature == value so that you can filter your input annotations
according to feature value (although regexes for value are not currently allowed).

inputASTypeFeature: send the content of this feature within inputASTypes to MetaMap and wrap a new MetaMap annotation around each annotation in inputASTypes. If the feature is empty or does not exist, then the annotation content is sent instead.

metaMapOptions: set MetaMap options here. Default is -Xdt (truncate Candidates mappings, disallow derivational variants and do not use full text parsing). See http://metamap.nlm.nih.gov/README_javaapi.html for more details of accepted arguments. NB: only set the -y parameter (word-sense disambiguation) if wsdserverctl is running. 

outputASName: output Annotation Set name.

outputASType: output annotation name to be used for all MetaMap annotations

outputMode: determines which MetaMap mappings are output as annotations in the GATE document, for each phrase:
- AllCandidatesAndMappings: annotate all Candidate and final Mappings. This will usually result in multiple, overlapping annotations for each term/phrase
- AllMappings: annotate all the final Mappings for each phrase. This will result in fewer annotations with higher precision (e.g. for 'lung cancer' only the complete phrase will be annotated as Neoplastic Process [neop])
- HighestMappingOnly: annotate only the highest scoring MetaMap Mapping for each phrase. If two Mappings have the same score, the first returned by MetaMap is output.
- HighestMappingLowestCUI: Where there is more than one highest-scoring mapping, returns the mapping where the head word map event has the lowest CUI.
- HighestMappingMostSources: Where there is more than one highest-scoring mapping, returns the mapping where the head word map event has the highest number of source vocabulary occurrences. 
- AllCandidates: annotate all Candidate mappings and not the final Mappings. This will result in more annotations with less precision (e.g. for 'lung cancer' both 'lung' (bpoc) and 'lung cancer' (neop) will be annotated).

taggerMode: determines whether all term instances are processed by MetaMap, the first instance only, or the first instance with coreference annotations added. Only used if the inputASTypes parameter has been set.
- FirstOccurrenceOnly: only process and annotate the first instance of each term in the document
- CoReference: process and annotate the first instance and coreference following instances of the same term.
- AllOccurrences: process and annotate all term instances independently
