/*
 *  MetaMapPR.java
 *
 *
 * Copyright (c) 2010,2011 The University of Sheffield.
 *
 * This file is part of GATE (see http://gate.ac.uk/), and is free
 * software, licenced under the GNU Library General Public License,
 * Version 2, June1991.
 *
 * A copy of this licence is included in the distribution in the file
 * licence.html, and is also available at http://gate.ac.uk/gate/licence.html.
 *
 *  philipgooch, 30/01/2011
 */
package gate.metamap;

import gov.nih.nlm.nls.metamap.*;

import gate.*;
import gate.creole.*;
import gate.creole.metadata.*;
import gate.util.*;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;

import java.util.*;
import java.io.*;

// TextNormalizer code from phramer.org
// Allows compilation under both Java 5 and Java 6
import info.olteanu.utils.*;
import info.olteanu.interfaces.StringFilter;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/** 
 * This class is the implementation of the resource METAMAP.
 */
@CreoleResource(name = "MetaMap Annotator",
helpURL = "http://gate.ac.uk/userguide/sec:misc-creole:metamap",
comment = "This plugin uses the MetaMap Java API to send GATE document content to MetaMap skrmedpostctl server and PrologBeans mmserver instances running on the given machine/port")
public class MetaMapPR extends AbstractLanguageAnalyser
        implements ProcessingResource, Serializable {

    private String inputASName;     // name of input annotation set in which inputASTypes occur
    private ArrayList<String> inputASTypes; // list of input annotations from which string content will be submitted
    private String inputASTypeFeature;      // name of feature within inputASTypes from which string content will be submitted
    private String outputASName;    // output annotation set name for MetaMap annotations
    private String outputASType;    // output annotation name within outputASName for MetaMap annotations
    private OutputMode outputMode;  // output HighestMappingOnly, AllMappings, AllCandidates, or AllCandidatesAndMappings 
    private Boolean annotatePhrases; // annotate MetaMap phrase chunks with outputASType
    private String metaMapOptions;   // MetaMap command line string
    private Boolean annotateNegEx;  // output NegEx results as features to outputASType
    private TaggerMode taggerMode;  // term tagger behaviour: all instances, first only, or first+coreferences
    private AnnotNormalizeMode annotNormalize;   // remove determiners from annotation content?
    private ArrayList<String> excludeIfContains;    // don't process if inputASType contains one of these annots
    private ArrayList<String> excludeIfWithin;      // don't process if inputASType occurs inside one of these annots
    // Exit gracefully if exception caught on init()
    private boolean gracefulExit;

    @Override
    public Resource init() throws ResourceInstantiationException {
        gracefulExit = false;

        // check required parameters are set
        if (outputMode == null) {
            //throw new ResourceInstantiationException("outputMode parameter must be set");
            gate.util.Err.println("outputMode parameter must be set");
            gracefulExit = true;
        }

        // check required parameters are set
        if (taggerMode == null) {
            //throw new ResourceInstantiationException("taggerMode parameter must be set");
            gate.util.Err.println("taggerMode parameter must be set");
            gracefulExit = true;
        }

        return this;
    }

    /* Set gracefulExit flag and clean up */
    private void gracefulExit(String msg) {
        gate.util.Err.println(msg);
        cleanup();
        fireProcessFinished();
    }

    /**
     *
     * @param inputAS           some input Annotation Set
     * @param inputAnnExpr      some Annotation or Annotation.feature or Annotation.feature == value expression
     * @return  inputAS filtered according to inputAnnExpr
     */
    private AnnotationSet getFilteredAS(AnnotationSet inputAS, String inputAnnExpr) {
        // We allow inputAnnExpr of the form
        // Annotation.feature == value or just Annotation.feature
        String annFeature;
        String annFeatureValue;
        String[] inputAnnArr = inputAnnExpr.split("(\\.)|(\\s*==\\s*)");
        
        // Assume a simple ann name unless we have a feature and feature value present
        String annName = inputAnnArr[0];
        AnnotationSet filteredAS = inputAS.get(annName);
        
        if (inputAnnArr.length == 3 || inputAnnArr.length == 2) {
            annFeature = inputAnnArr[1];
            if (inputAnnArr.length == 2) {
                Set<String> feats = new HashSet<String>();
                feats.add(annFeature);
                filteredAS = inputAS.get(annName, feats);
            } else {
                FeatureMap annFeats = Factory.newFeatureMap();
                annFeatureValue = inputAnnArr[2];
                annFeats.put(annFeature, annFeatureValue);
                filteredAS = inputAS.get(annName, annFeats);
            }
        }
        return filteredAS;
    }


    /**
     *
     * @param expression    an expression of the form Annotation.feature == value
     * @return              string containing the annotation name from the input expression
     */
    private String getAnnNameFromExpression(String expression) {
        String[] inputAnnArr = expression.split("(\\.)|(\\s*==\\s*)");
        return inputAnnArr[0];
    }

    
    @Override
    public void execute() throws ExecutionException {
        // quit if setup failed
        if (gracefulExit) {
            gracefulExit("Plugin was not initialised correctly. Exiting ... ");
            return;
        }

        MetaMapApi mmInst;

        // If no document provided log the error and exit gracefully
        if (document == null) {
            gracefulExit("No document to process!");
            return;
        }

        if (outputASType == null || outputASType.trim().length() == 0) {
            gracefulExit("outputASType parameter must be set!");
            return;
        }


        // set up MetaMap API instance
        try {
            mmInst = setUpApi();
        } catch (Exception e) {
            gracefulExit("Unable to create MetaMap session instance");
            return;
        }

        Long lngInitialOffset = Long.valueOf(0);
        Long lngEndOffset = null;

        if (inputASTypes != null && !(inputASTypes.isEmpty())) {
            AnnotationSet inputAS = (inputASName == null || inputASName.trim().length() == 0) ? document.getAnnotations() : document.getAnnotations(inputASName);
            AnnotationSet outputAS = (outputASName == null || outputASName.trim().length() == 0) ? document.getAnnotations() : document.getAnnotations(outputASName);

            // process the content of each annot or expression in inputASTypes
            for (String inputAnnExpr : inputASTypes) {
                // Iterator<Annotation> itr = inputAS.get(inputAnnExpr).iterator();
                AnnotationSet inputAnnSet = getFilteredAS(inputAS, inputAnnExpr);

                // get annots in document order, so we can just want to process the first instance of each
                Iterator<Annotation> itr = gate.Utils.inDocumentOrder(inputAnnSet).listIterator();

                // Need to create a map of annotations whose string content appears
                // elsewhere in the document, as we only want to process each term
                // once through MetaMap
                HashMap<Integer, ArrayList<Integer>> termMapById = new HashMap<Integer, ArrayList<Integer>>();        // map of annots with content duplicated by other annots, indexed by annot id
                HashMap<String, Integer> termMapByString = new HashMap<String, Integer>();    // as above, but indexed by string content

                // iterate over each annot of type inputAnnExpr
                while (itr.hasNext()) {

                    Annotation ann = itr.next();
                    boolean skip = false;
                    Long annStart = ann.getStartNode().getOffset();
                    Long annEnd = ann.getEndNode().getOffset();
                    String annContent = "";

                    // Don't process this term if it occurs within or wraps any of these annots
                    if (excludeIfWithin != null && !(excludeIfWithin.isEmpty())) {
                        for (String excludeAnnExpr : excludeIfWithin) {
                            String excludeAnnName = getAnnNameFromExpression(excludeAnnExpr);
                            AnnotationSet tempAS = inputAS.getCovering(excludeAnnName, annStart, annEnd);
                            AnnotationSet excludeAS = getFilteredAS(tempAS, excludeAnnExpr);
                            if (!excludeAS.isEmpty()) {
                                skip = true;
                                break;
                            }
                        }
                    }
                    if (excludeIfContains != null && !(excludeIfContains.isEmpty())) {
                        for (String excludeAnnExpr : excludeIfContains) {
                            AnnotationSet tempAS = inputAS.getContained(annStart, annEnd);
                            AnnotationSet excludeAS = getFilteredAS(tempAS, excludeAnnExpr);
                            if (!excludeAS.isEmpty()) {
                                skip = true;
                                break;
                            }
                        }
                    }
                    if (skip) {
                        continue;
                    }

                    try {
                        lngEndOffset = null;
                        lngInitialOffset = annStart;
                        // grab the string content of the annotation, or the
                        // value of the feature inputASTypeFeature if specified
                        annContent = document.getContent().getContent(lngInitialOffset,
                                annEnd).toString().toLowerCase();
                        if (inputASTypeFeature == null || inputASTypeFeature.trim().length() == 0) {
                            // if inputASTypeFeature not specified, continue to pass the string content of the ann
                        } else {
                            Object o = ann.getFeatures().get(inputASTypeFeature);
                            String annFeatureContent = (o == null) ? null : o.toString().toLowerCase();
                            if (annFeatureContent == null || annFeatureContent.trim().length() == 0) {
                                // if feature has no value, continue to pass the string content of the ann
                            } else {
                                // otherwise, pass the string content of the feature
                                // and keep track of the ann's end point
                                // as we will wrap new annots around our existing ann
                                annContent = annFeatureContent;
                                lngEndOffset = annEnd;
                            }
                        }
                        // strip leading or all determiners. We'll need to adjust the output annotation end offset
                        // if any changes were made to the content string of the input annotation
                        if (annotNormalize != AnnotNormalizeMode.None) {
                            String tmpContent = stripDeterminers(inputAS, ann, annContent, annotNormalize);
                            if (!tmpContent.equalsIgnoreCase(annContent)) {
                                annContent = tmpContent;
                                lngEndOffset = annEnd;
                            }
                        }

                    } catch (InvalidOffsetException ioe) {
                        // this should never happen
                        gate.util.Err.println(ioe.getMessage());
                    } // end try

                    Integer annId = ann.getId();
                    // Only process ann / ann feature string values once if this option has been set
                    if (taggerMode == TaggerMode.FirstOccurrenceOnly || taggerMode == TaggerMode.CoReference) {
                        if (termMapByString.get(annContent) == null) {
                            // if string annContent not already processed ...
                            termMapById.put(annId, new ArrayList<Integer>());
                            termMapByString.put(annContent, annId);
                            try {
                                this.processWithMetaMap(mmInst, annContent, lngInitialOffset, lngEndOffset);
                            } catch (Exception e) {
                                gate.util.Err.println(e.getMessage());
                            }
                        } else {
                            // string annContent already processed, so keep a note
                            // of the annotation id - we'll copy over the MetaMap annotations later
                            Integer uniqueId = termMapByString.get(annContent);
                            ArrayList<Integer> dupAnnIds = termMapById.get(uniqueId);
                            dupAnnIds.add(annId);
                        }
                    } else {
                        // process every annot
                        try {
                            this.processWithMetaMap(mmInst, annContent, lngInitialOffset, lngEndOffset);
                        } catch (Exception e) {
                            gate.util.Err.println(e.getMessage());
                        }
                    }

                } // end while

                // process coreferences, if this option has been set
                if (taggerMode == TaggerMode.CoReference) {
                    doCoreferenceAnnots(termMapById, inputAS, outputAS);
                }

            } // end for

        } else {
            // Just process the entire document
            String docText = document.getContent().toString();
            try {
                this.processWithMetaMap(mmInst, docText, lngInitialOffset, lngEndOffset);
            } catch (Exception e) {
                gracefulExit(e.getMessage());
            }
        } // end if

        // options are sticky between session connects, so need to reset them
        mmInst.resetOptions();

        fireProcessFinished();

    }

    /**
     *
     * @param ann           Annotation to be classified by MetaMap
     * @param annContent    Annotation content or user-defined feature value
     * @param mode          Mode to determine whether to strip just leading determiner or all determiners in the content
     * @return              Annotation content with leading or all determiners stripped.
     *                      E.g. his hypertension -> hypertension, disease of the nervous system -> disease of nervous system
     */
    public String stripDeterminers(AnnotationSet inputAS, Annotation ann, String annContent, AnnotNormalizeMode mode) {


        Long annStart = ann.getStartNode().getOffset();
        Long annEnd = ann.getEndNode().getOffset();

        // These will return an empty set if no Tokens/SpaceTokens so don't need to test for null
        AnnotationSet innerTokAS = inputAS.getContained(annStart, annEnd).get("Token");
        AnnotationSet innerSpaceTokAS = inputAS.getContained(annStart, annEnd).get("SpaceToken");

        List<Annotation> tokenAS = new ArrayList<Annotation>(innerTokAS);
        tokenAS.addAll(innerSpaceTokAS);

        Collections.sort(tokenAS, new OffsetComparator());
        StringBuilder strbuf = new StringBuilder("");
        String strWord = "";
        String strPOS = "";
        boolean skipDeterminer = true;

        for (Annotation tok : tokenAS) {
            FeatureMap tokFeats = tok.getFeatures();
            strWord = (String) tokFeats.get("string");
            strPOS = (String) tokFeats.get("category");
            if (skipDeterminer && strPOS != null && (strPOS.equals("DT") || strPOS.equals("PRP") || strPOS.equals("PRP$"))) {
                if (mode == AnnotNormalizeMode.LeadingDeterminer) {
                    skipDeterminer = false;
                }
                continue;
            } else {
                strbuf.append(strWord);
            }
        }

        // Normalize white space
        String stripped = strbuf.toString().replaceAll("[\\s\\xA0]+", " ").trim();
        if (stripped.isEmpty()) {   // if all we had was determiners, or there were no Tokens to process, return the original content
            return annContent;
        } else {
            return stripped;
        }
    }

    /**
     * Requires Tokenizer and POS tagger to have been run on text
     * @param termMapById   HashMap containing first occurrence of each of term
     * @param inputAS   input AnnotationSet
     * @param outputAS  output AnnotationSet
     * @throws ExecutionException
     */
    public void doCoreferenceAnnots(HashMap<Integer, ArrayList<Integer>> termMapById, AnnotationSet inputAS, AnnotationSet outputAS) throws ExecutionException {
        // Iterate over the annotations in termMapById and
        // copy over the MetaMap annotations contained to the
        // other annotations with the same string content

        if (termMapById == null) {
            return;
        }
        Set keys = termMapById.keySet();

        Iterator keyIter = keys.iterator();

        while (keyIter.hasNext()) {
            Integer key = (Integer) keyIter.next();  // Get the id of the processed annot

            ArrayList<Integer> dupAnnIds = termMapById.get(key);  // Get list containing annots with duplicate content.

            Annotation pAnn = inputAS.get(key);
            FeatureMap fm = pAnn.getFeatures();
            fm.put("coreferences", dupAnnIds);
            AnnotationSet mmAnns = outputAS.getContained(pAnn.getStartNode().getOffset(), pAnn.getEndNode().getOffset()).get(outputASType);

            // Copy all the MetaMap annots within mmAnns to the
            // unprocessed annotations that have the same string value
            // as the annots we processed through MetaMap
            for (Integer dupAnnId : dupAnnIds) {
                Annotation uAnn = inputAS.get(dupAnnId);
                Long startOffset = uAnn.getStartNode().getOffset();
                Long endOffset = uAnn.getEndNode().getOffset();
                for (Annotation mmAnn : mmAnns) {
                    FeatureMap mmFm = mmAnn.getFeatures();
                    try {
                        outputAS.add(startOffset, endOffset, outputASType, mmFm);
                    } catch (InvalidOffsetException ie) {
                        gracefulExit(ie.getMessage());
                    }
                } // end for

            } // end for

        } // end while
    }

    /**
     * 
     * @return MetaMapApi instance
     * @throws Exception
     */
    public MetaMapApi setUpApi() throws Exception {
        List<String> mmOptions = new ArrayList<String>();

        // mmserver10 now supports parameters with arguments
        String[] args = getMetaMapOptions().split("\\s+");

        String serverhost = MetaMapApi.DEFAULT_SERVER_HOST;
        int serverport = MetaMapApi.DEFAULT_SERVER_PORT; 	// default port
        int timeout = -1; // use default timeout

        int i = 0;

        while (i < args.length) {
            if (args[i] == null || args[i].length() == 0) {
                args[i] = " ";
            }
            if (args[i].charAt(0) == '-') {
                if (args[i].equals("-%") || args[i].equals("--XML")) {
                    mmOptions.add(args[i]);
                    i++;
                    mmOptions.add(args[i]);
                } else if (args[i].equals("-@") || args[i].equals("--WSD")) {
                    mmOptions.add(args[i]);
                    i++;
                    mmOptions.add(args[i]);
                } else if (args[i].equals("-J") || args[i].equals("--restrict_to_sts")) {
                    mmOptions.add(args[i]);
                    i++;
                    mmOptions.add(args[i]);
                } else if (args[i].equals("-Q") || args[i].equals("--composite_phrases")) {
                    mmOptions.add(args[i]);
                    // if we are using metamap11, this option takes a mandatory integer
                    // so let's check if we have this option
                    int q = i;
                    boolean isInt = true;
                    try {
                        String arg = args[++q];
                        q = Integer.parseInt(arg);
                    } catch (NumberFormatException ne) {
                        isInt = false;
                    } catch (ArrayIndexOutOfBoundsException ae) {
                        isInt = false;
                    }
                    if (isInt) {
                        i++;
                        mmOptions.add(args[i]);
                    }
                } else if (args[i].equals("-R") || args[i].equals("--restrict_to_sources")) {
                    mmOptions.add(args[i]);
                    i++;
                    mmOptions.add(args[i]);
                } else if (args[i].equals("-S") || args[i].equals("--tagger")) {
                    mmOptions.add(args[i]);
                    i++;
                    mmOptions.add(args[i]);
                } else if (args[i].equals("-V") || args[i].equals("--mm_data_version")) {
                    mmOptions.add(args[i]);
                    i++;
                    mmOptions.add(args[i]);
                } else if (args[i].equals("-Z") || args[i].equals("--mm_data_year")) {
                    mmOptions.add(args[i]);
                    i++;
                    mmOptions.add(args[i]);
                } else if (args[i].equals("-e") || args[i].equals("--exclude_sources")) {
                    mmOptions.add(args[i]);
                    i++;
                    mmOptions.add(args[i]);
                } else if (args[i].equals("-k") || args[i].equals("--exclude_sts")) {
                    mmOptions.add(args[i]);
                    i++;
                    mmOptions.add(args[i]);
                } else if (args[i].equals("-r") || args[i].equals("--threshold")) {
                    mmOptions.add(args[i]);
                    i++;
                    mmOptions.add(args[i]);
                } else if (args[i].equals("--metamap_server_host")) {
                    i++;
                    serverhost = args[i];
                } else if (args[i].equals("--metamap_server_port")) {
                    i++;
                    serverport = Integer.parseInt(args[i]);
                } else if (args[i].equals("--metamap_server_timeout")) {
                    i++;
                    timeout = Integer.parseInt(args[i]);
                } else if (args[i].equals("--output")) {
                    i++;
                    // ignore - not relevant within GATE
                } else {
                    mmOptions.add(args[i]);
                }
            }
            i++;
        } // end while

        MetaMapApi api = new MetaMapApiImpl(serverhost, serverport, timeout);

        if (mmOptions.size() > 0) {
            api.setOptions(mmOptions);
        }

        return api;
    }

    /**
     * 
     * @param pcm - phrase chunk returned by MetaMap
     * @param lngInitialOffset - offset start of annotation relative to prior text
     * @throws Exception
     */
    public void processPhrase(PCM pcm, Long lngInitialOffset) throws Exception {
        AnnotationSet outputAs = (outputASName == null || outputASName.trim().length() == 0) ? document.getAnnotations() : document.getAnnotations(outputASName);

        Phrase phrase = pcm.getPhrase();
        Position pos = phrase.getPosition();

        int intStartPos = pos.getX();
        int intEndPos = pos.getY() + intStartPos;

        FeatureMap fm = Factory.newFeatureMap();
        fm.put("Type", "Phrase");

        Long lngStartPos = new Long((long) intStartPos + lngInitialOffset.longValue());
        Long lngEndPos = new Long((long) intEndPos + lngInitialOffset.longValue());
        try {
            outputAs.add(lngStartPos, lngEndPos, outputASType, fm);
        } catch (InvalidOffsetException ie) {
            gate.util.Err.println(ie.getMessage());
        }
    }

    /**
     *
     * @param eVList - list of events returned by MetaMap
     * @param negList - list of negated terms returned by MetaMap
     * @param type - Candidate or Mapping
     * @param lngInitialOffset - offset start of annotation relative to prior text
     * @param lngEndOffset - use existing annotation end if annotating feature inside annotation
     * @throws Exception
     */
    public void processEvents(List<Ev> eVList, List<Negation> negList, String type, Long lngInitialOffset, Long lngEndOffset) throws Exception {
        AnnotationSet outputAs = (outputASName == null || outputASName.trim().length() == 0) ? document.getAnnotations() : document.getAnnotations(outputASName);


        for (Ev mapEv : eVList) {
            FeatureMap fm = Factory.newFeatureMap();

            List<Position> lstSpans = mapEv.getPositionalInfo();

            int numSpans = lstSpans.size();

            int intStartPos = lstSpans.get(0).getX();
            int intEndPos = lstSpans.get(numSpans - 1).getX() + lstSpans.get(numSpans - 1).getY();


            if (negList != null && negList.size() > 0) {
                // see if there is a NegEx match at the same position as Event match
                // if so, add an annotation feature for the NexEx type and trigger
                for (Negation ne : negList) {
                    List<Position> p = ne.getConceptPositionList();

                    int intNegStartPos = p.get(0).getX();
                    int negSpans = p.size();

                    int intNegEndPos = p.get(negSpans - 1).getX() + p.get(negSpans - 1).getY();

                    if (intNegStartPos == intStartPos && intNegEndPos == intEndPos) {
                        fm.put("NegExType", ne.getType());
                        fm.put("NegExTrigger", ne.getTrigger());
                        break;
                    }
                }
            } // end if

            fm.put("Type", type);
            fm.put("Score", mapEv.getScore());
            fm.put("ConceptId", mapEv.getConceptId());
            fm.put("ConceptName", mapEv.getConceptName());
            fm.put("PreferredName", mapEv.getPreferredName());

            // Output semantic types and sources both as raw strings and as Lists
            // so that they can be queried both via JAPE LHS matches and Java RHS
            fm.put("SemanticTypes", mapEv.getSemanticTypes());
            fm.put("SemanticTypesString", mapEv.getSemanticTypes().toString());
            fm.put("Sources", mapEv.getSources().toString());
            fm.put("SourcesString", mapEv.getSources().toString());

            // Features missing from previous versions of plugin
            fm.put("IsHead", mapEv.isHead());
            fm.put("IsOvermatch", mapEv.isOvermatch());

            // lngEndOffset will be null if we are processing the whole document
            // or the string content of individual annotations.
            // Otherwise, we are processing the feature content, in which case
            // we will simply wrap our MetaMap annotation around our existing annotations
            Long lngStartPos = (lngEndOffset == null) ? new Long((long) intStartPos + lngInitialOffset.longValue()) : lngInitialOffset;
            Long lngEndPos = (lngEndOffset == null) ? new Long((long) intEndPos + lngInitialOffset.longValue()) : lngEndOffset;
            try {
                outputAs.add(lngStartPos, lngEndPos, outputASType, fm);
            } catch (InvalidOffsetException ie) {
                gate.util.Err.println(ie.getMessage());
            }

        } // end for
    }

    /**
     *
     * @param pcm - phrase chunk returned by MetaMap
     * @param negList - list of negated terms returned by MetaMap
     * @param lngInitialOffset - offset start of annotation relative to prior text
     * @param lngEndOffset - use existing annotation end if annotating feature inside annotation
     * @throws Exception
     */
    public void processMappings(PCM pcm, List<Negation> negList, Long lngInitialOffset, Long lngEndOffset) throws Exception {

        List<Mapping> mappings = pcm.getMappingList();

        // Sort according to properties of the first map event in each mapping
        Collections.sort(mappings, new Comparator<Mapping>() {

            public int compare(Mapping m1, Mapping m2) {
                int result = 0;
                try {
                    List<Ev> m1Evs = m1.getEvList();
                    List<Ev> m2Evs = m2.getEvList();
                    Ev m1HeadEvent = m1Evs.get(0);
                    Ev m2HeadEvent = m2Evs.get(0);
                    // Iterate over both event lists to get and compare the head term
                    for (Ev ev : m1Evs) {
                        if (ev.isHead()) {
                            m1HeadEvent = ev;
                            break;
                        }
                    }
                    for (Ev ev : m2Evs) {
                        if (ev.isHead()) {
                            m2HeadEvent = ev;
                            break;
                        }
                    }
                    if (outputMode.equals(OutputMode.HighestMappingMostSources)) {
                        // return highest number of sources
                        result = m2HeadEvent.getSources().size() - m1HeadEvent.getSources().size();
                    } else if (outputMode.equals(OutputMode.HighestMappingLowestCUI)) {
                        // return lowest CUI
                        result = m1HeadEvent.getConceptId().compareTo(m2HeadEvent.getConceptId());
                    }
                } catch (Exception e) {
                    // we'll ignore this and return the default (i.e. sorted by score)
                } finally {
                    return result;
                }
            }
        });



        // By default, mappings are ordered by score, so outputting the first item will give the mapping
        // with the highest score, if requested. Otherwise, the above sorting will be applied
        if ((outputMode.equals(OutputMode.HighestMappingOnly)
                || outputMode.equals(OutputMode.HighestMappingMostSources)
                || outputMode.equals(OutputMode.HighestMappingLowestCUI)) && !(mappings.isEmpty())) {
            processEvents(mappings.get(0).getEvList(), negList, "Mapping", lngInitialOffset, lngEndOffset);
        } else {
            for (Mapping map : mappings) {
                processEvents(map.getEvList(), negList, "Mapping", lngInitialOffset, lngEndOffset);
            }
        }
    }

    /**
     *
     * @param pcm - phrase chunk returned by MetaMap
     * @param negList - list of negated terms returned by MetaMap
     * @param lngInitialOffset - offset start of annotation relative to prior text
     * @param lngEndOffset - use existing annotation end if annotating feature inside annotation
     * @throws Exception
     */
    public void processCandidates(PCM pcm, List<Negation> negList, Long lngInitialOffset, Long lngEndOffset) throws Exception {
        processEvents(pcm.getCandidates(), negList, "Candidate", lngInitialOffset, lngEndOffset);
    }

    /**
     *
     * @param result - result returned by MetaMap
     * @param lngInitialOffset - offset start of annotation relative to prior text
     * @param lngEndOffset - use existing annotation end if annotating feature inside annotation
     * @throws Exception
     */
    public void processUtterances(Result result, Long lngInitialOffset, Long lngEndOffset) throws Exception {

        List<Negation> negList = null;

        if (annotateNegEx) {
            negList = result.getNegations();
        }

        for (Utterance utterance : result.getUtteranceList()) { // each utterance (sentence ?)

            for (PCM pcm : utterance.getPCMList()) { // each phrase in the utterance
                int numPCMMappings = pcm.getMappings().size();

                if (outputMode.equals(OutputMode.AllCandidatesAndMappings) || outputMode.equals(OutputMode.AllCandidates)) {
                    this.processCandidates(pcm, negList, lngInitialOffset, lngEndOffset);
                }
                if (outputMode.equals(OutputMode.AllCandidatesAndMappings)
                        || outputMode.equals(OutputMode.AllMappings)
                        || outputMode.equals(OutputMode.HighestMappingOnly)
                        || outputMode.equals(OutputMode.HighestMappingMostSources)
                        || outputMode.equals(OutputMode.HighestMappingLowestCUI)) {
                    this.processMappings(pcm, negList, lngInitialOffset, lngEndOffset);
                }


                // only annotate phrases if they contain MetaMap mappings and we're not annotating the feature
                if (annotatePhrases && numPCMMappings > 0 && lngEndOffset == null) {
                    this.processPhrase(pcm, lngInitialOffset);
                }
            } // end for
        } // end for
    }

    /**
     * @param api - MetaMapApi instance
     * @param text - text to be annotated
     * @param lngInitialOffset - offset start of annotation relative to prior text
     * @param lngEndOffset - use existing annotation end if annotating feature inside annotation
     * @throws Exception
     */
    public void processWithMetaMap(MetaMapApi api, String text, Long lngInitialOffset, Long lngEndOffset) throws Exception {
        /* metamap API 2010 now has processCitationsFromString as opposed to
         * processString, and it returns List<Result> rather than Result
         */
        List<Result> resultList = null;

        String asciiText = filterNonAscii(normalizeString(text)) + "\n\n";

        // Create a pattern that strips leading whitespace and
        // and chunks the text delimited by 2 or more blank lines
        Pattern pattern = Pattern.compile("(?s)((\\p{Space}|\\p{Cntrl})*)(.+?)\n[\\s]*\n");

        Matcher m = pattern.matcher(asciiText);
        int iStart = 0;
        String chunk = "";
        int chunkLength = 0;

        while (m.find(iStart)) {
            lngInitialOffset += m.group(1).length();
            chunk = m.group(3);
            chunkLength = chunk.length();

            if (!chunk.trim().isEmpty()) {
                resultList = api.processCitationsFromString(chunk);

                int resultLength = 0;

                if (resultList != null) {
                    for (Result result : resultList) {
                        if (result != null) {
                            this.processUtterances(result, lngInitialOffset + resultLength, (lngEndOffset == null) ? null : lngEndOffset + resultLength);
                            // We've pre-chunked the text now so this loop should only iterate once
                            // but, if for some reason it iterates more often, we need to keep track
                            // of the length of the previous result to update the start offset
                            resultLength = resultLength + result.getInputText().length();
                        } else {
                            // throw new Exception("NULL result instance! ");
                            gracefulExit("NULL MetaMap result instance! ");
                        }
                    }
                }
            }

            lngInitialOffset += chunkLength;
            iStart = m.end(3);
        }
    }

    /**
     *
     * @param str
     * @return Normalized version of str with accented characters replaced by unaccented version and
     * with diacritics removed. E.g. Ö -> O
     */
    public String normalizeString(String str) throws ClassNotFoundException {
        // TextNormalizer code from phramer.org
        // Allows compilation under both Java 5 and Java 6
        StringFilter stringFilter = TextNormalizer.getNormalizationStringFilter();
        String nfdNormalizedString = stringFilter.filter(str);

        // Normalizer is Java 6 only
        // String nfdNormalizedString = java.text.Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    /**
     *
     * @param text
     * @return ASCII encoding of text with non ASCII characters replaced by ?
     * @throws UnsupportedEncodingException
     */
    public String filterNonAscii(String text) throws UnsupportedEncodingException {
        String aText;
        byte[] b = text.getBytes("US-ASCII");
        aText = new String(b, "US-ASCII");
        return aText;
    }

    // PR parameters
    @Optional
    @RunTime
    @CreoleParameter(comment = "Input Annotation Set Name")
    public void setInputASName(String inputASName) {
        this.inputASName = inputASName;
    }

    public String getInputASName() {
        return inputASName;
    }

    @Optional
    @RunTime
    @CreoleParameter(comment = "Output Annotation Set Name")
    public void setOutputASName(String outputASName) {
        this.outputASName = outputASName;
    }

    public String getOutputASName() {
        return outputASName;
    }

    @RunTime
    @CreoleParameter(defaultValue = "MetaMap",
    comment = "Name for the MetaMap Annotation types")
    public void setOutputASType(String outputASType) {
        this.outputASType = outputASType;
    }

    public String getOutputASType() {
        return outputASType;
    }

    @Optional
    @RunTime
    @CreoleParameter(comment = "If set, only send the content of the given Annotations in the input Annotation Set to MetaMap")
    public void setInputASTypes(ArrayList<String> inputASTypes) {
        this.inputASTypes = inputASTypes;
    }

    public ArrayList<String> getInputASTypes() {
        return inputASTypes;
    }

    @Optional
    @RunTime
    @CreoleParameter(comment = "If set, only send the content of the given feature from annotations within inputASTypes to MetaMap")
    public void setInputASTypeFeature(String inputASTypeFeature) {
        this.inputASTypeFeature = inputASTypeFeature;
    }

    public String getInputASTypeFeature() {
        return inputASTypeFeature;
    }

    @RunTime
    @CreoleParameter(defaultValue = "HighestMappingOnly",
    comment = "Output highest scoring final mapping, all mappings, all candidate terms, or all candidates and mappings")
    public void setOutputMode(OutputMode outputMode) {
        this.outputMode = outputMode;
    }

    public OutputMode getOutputMode() {
        return outputMode;
    }

    @RunTime
    @CreoleParameter(defaultValue = "false",
    comment = "Output MetaMap phrase-level annotations?")
    public void setAnnotatePhrases(Boolean annotatePhrases) {
        this.annotatePhrases = annotatePhrases;
    }

    public Boolean getAnnotatePhrases() {
        return annotatePhrases;
    }

    @Optional
    @RunTime
    @CreoleParameter(defaultValue = "-Xy",
    comment = "MetaMap runtime options")
    public void setMetaMapOptions(String metaMapOptions) {
        this.metaMapOptions = metaMapOptions;
    }

    public String getMetaMapOptions() {
        return metaMapOptions;
    }

    @RunTime
    @CreoleParameter(defaultValue = "false",
    comment = "Output NegEx negation annotations and features?")
    public void setAnnotateNegEx(Boolean annotateNegEx) {
        this.annotateNegEx = annotateNegEx;
    }

    public Boolean getAnnotateNegEx() {
        return annotateNegEx;
    }

    @RunTime
    @CreoleParameter(defaultValue = "CoReference",
    comment = "Map first instance of a term only, first instance plus coreferences, or all instances independently")
    public void setTaggerMode(TaggerMode taggerMode) {
        this.taggerMode = taggerMode;
    }

    public TaggerMode getTaggerMode() {
        return taggerMode;
    }

    @RunTime
    @CreoleParameter(defaultValue = "None",
    comment = "Strip determiners from annotation content prior to MetaMap submission?")
    public void setAnnotNormalize(AnnotNormalizeMode annotNormalize) {
        this.annotNormalize = annotNormalize;
    }

    public AnnotNormalizeMode getAnnotNormalize() {
        return annotNormalize;
    }

    @Optional
    @RunTime
    @CreoleParameter(comment = "Don't process terms that contain these annotations")
    public void setExcludeIfContains(ArrayList<String> excludeIfContains) {
        this.excludeIfContains = excludeIfContains;
    }

    public ArrayList<String> getExcludeIfContains() {
        return excludeIfContains;
    }

    @Optional
    @RunTime
    @CreoleParameter(comment = "Don't process terms that are within these annotations")
    public void setExcludeIfWithin(ArrayList<String> excludeIfWithin) {
        this.excludeIfWithin = excludeIfWithin;
    }

    public ArrayList<String> getExcludeIfWithin() {
        return excludeIfWithin;
    }
} // class MetaMapPR

