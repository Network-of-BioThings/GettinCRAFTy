/*
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Valentin Tablan 03/04/2003
 *
 *  $Id: StringToNominalFilter.java 7504 2006-07-05 13:10:35 +0000 (Wed, 05 Jul 2006) julien_nioche $
 *
 */

package gate.creole.ml.weka;

import java.io.Serializable;
import java.util.*;

import weka.core.*;
import weka.filters.Filter;

/**
 * This filter converts one or more string attributes from the input dataset
 * into nominal attributes.
 */
public class StringToNominalFilter extends Filter implements OptionHandler{
  /**
   * Anonymous constructor.
   */
  public StringToNominalFilter() {
  }

  /**
   * Sets the format of the input instances.
   *
   * @param instanceInfo an Instances object containing the input
   * instance structure (any instances contained in the object are
   * ignored - only the structure is required).
   * @return <tt>false</tt> as this filter needs to see all the instances
   * before being able to convert the input.
   * @exception UnsupportedAttributeTypeException if the selected attribute
   * is not a string attribute.
   */
  public boolean setInputFormat(Instances instanceInfo)
       throws Exception {
    super.setInputFormat(instanceInfo);
    Iterator attIter = attributesData.iterator();
    while(attIter.hasNext()){
      AttributeData aData = (AttributeData)attIter.next();
      if (!instanceInfo.attribute(aData.index).isString()) {
        throw new UnsupportedAttributeTypeException(
          "Attribute at selcted index " + aData.index +
          " is not of type string!");
      }
    }
    return false;
  }

  /**
   * Input an instance for filtering. The instance is processed
   * and made available for output immediately.
   *
   * @param instance the input instance.
   * @return true if the filtered instance may now be
   * collected with output().
   * @exception IllegalStateException if no input structure has been defined.
   */
  public boolean input(Instance instance) {
    if (getInputFormat() == null) {
      throw new IllegalStateException("No input instance format defined");
    }

    if (m_NewBatch) {
      resetQueue();
      m_NewBatch = false;
    }

    bufferInput(instance);
    return false;
  }

  /**
   * Signifies that this batch of input to the filter is finished. If the
   * filter requires all instances prior to filtering, output() may now
   * be called to retrieve the filtered instances.
   *
   * @return true if there are instances pending output.
   * @exception IllegalStateException if no input structure has been defined.
   */
  public boolean batchFinished() {
    if (getInputFormat() == null) {
      throw new IllegalStateException("No input instance format defined");
    }
    //do the maths
    buildOutputFormat();

    // Convert pending input instances
    for(int i = 0; i < getInputFormat().numInstances(); i++) {
      push(processInstance(getInputFormat().instance(i)));
    }

    flushInput();
    m_NewBatch = true;
    return (numPendingOutput() != 0);
  }


  /**
   * Called after a batch of input has finished. Will perform all the necessary
   * calculations to define the output format and build the data needed in order
   * to convert the input instances into output.
   */
  protected void buildOutputFormat(){
    //build the new instances value
    //collect the frequency data for all the words into a map
    //(String)word -> (Integer)Attribute Index -> (Double)Class value ->
    //->(Integer)count
    Map wordData = new HashMap();
    //for all input instances
    for(int i = 0; i < getInputFormat().numInstances(); i++) {
      Instance instance = getInputFormat().instance(i);
      //for all attributes that need processing
      Iterator attIter = attributesData.iterator();
      while(attIter.hasNext()){
        AttributeData aData = (AttributeData)attIter.next();
        String word = instance.stringValue(aData.index);
        //get the map for this word
        Map wMap = (Map)wordData.get(word);
        if(wMap == null){
          wMap = new HashMap();
          wordData.put(word, wMap);
        }
        //get the map for word->attribute
        Integer attIndex = new Integer(aData.index);
        Map w_aMap = (Map)wMap.get(attIndex);
        if(w_aMap == null){
          w_aMap = new HashMap();
          wMap.put(attIndex, w_aMap);
        }
        //get the count for word->attribute->class
        Double classValue = new Double(instance.classValue());
        WordData w_a_cCount = (WordData)w_aMap.get(classValue);
        //increment the count
        if(w_a_cCount == null){
          w_a_cCount = new WordData(word, aData.index, classValue, 1);
          w_aMap.put(classValue, w_a_cCount);
        }else{
          w_a_cCount.inc();
        }
      }
    }
    // Compute new attributes
    Instances newData;
    FastVector newAtts, newVals;
    //start with a copy of the initial dataset header
    newAtts = new FastVector(getInputFormat().numAttributes());
    for (int i = 0; i < getInputFormat().numAttributes(); i++) {
      Attribute att = getInputFormat().attribute(i);
      newAtts.addElement(att.copy());
    }

    //replace the filtered attributes
    Iterator attIter = attributesData.iterator();
    while(attIter.hasNext()){
      AttributeData aData = (AttributeData)attIter.next();
      FastVector values = new FastVector(aData.maxCount);
      if(aData.method.equalsIgnoreCase(FREQUENCY)){
        List wordFreqs = new ArrayList(wordData.size());
        Iterator entryIter = wordData.entrySet().iterator();
        while(entryIter.hasNext()){
          Map.Entry entry = (Map.Entry)entryIter.next();
          String word = (String)entry.getKey();
          Map w_aMap = (Map)((Map)entry.getValue()).get(new Integer(aData.index));
          int count = addLeaves(w_aMap);
          wordFreqs.add(new WordCount(word, count));
        }
        int start = 0;
        if(wordFreqs.size() > aData.maxCount){
          Collections.sort(wordFreqs);
          start = wordFreqs.size() - aData.maxCount;
        }
        for(int i = wordFreqs.size() -1; i >= start; i--){
          values.addElement(((WordCount)wordFreqs.get(i)).word);
        }
System.out.println("Values count" + values.size());
      }else if(aData.method.equalsIgnoreCase(TFIDF)){
        int classCount = getInputFormat().classAttribute().numValues();
        List wordTFIDFValues = new ArrayList(wordData.size());
        Iterator entryIter = wordData.entrySet().iterator();
        while(entryIter.hasNext()){
          Map.Entry entry = (Map.Entry)entryIter.next();
          String word = (String)entry.getKey();
          Map w_aMap = (Map)((Map)entry.getValue()).get(new Integer(aData.index));
          if(w_aMap == null || w_aMap.isEmpty()) continue;
          int count = addLeaves(w_aMap);
          int classFreq = w_aMap.size();
          double tfidf = count * Math.log(classCount/classFreq);
          wordTFIDFValues.add(new WordCount(word, count, tfidf));
        }
        int start = 0;
        if(wordTFIDFValues.size() > aData.maxCount){
          Collections.sort(wordTFIDFValues, new Comparator(){
            public int compare(Object o1, Object o2){
              double value = ((WordCount)o1).tfidf - ((WordCount)o2).tfidf;
              if(value > Utils.SMALL) return 1;
              else if(value < -Utils.SMALL) return -1;
              else return 0;
            }
          });
          start = wordTFIDFValues.size() - aData.maxCount;
        }
        for(int i = wordTFIDFValues.size() -1; i >= start; i--){
          values.addElement(((WordCount)wordTFIDFValues.get(i)).word);
        }

      }
      Attribute oldAttr = (Attribute)newAtts.elementAt(aData.index);
      Attribute newAttribute = new Attribute(oldAttr.name(), values);
System.out.println("Atribute \"" + newAttribute.name() + "\":" + values.size());
      newAtts.setElementAt(newAttribute, aData.index);
    }

    // Construct new header
    newData = new Instances(getInputFormat().relationName(), newAtts, 0);
    newData.setClassIndex(getInputFormat().classIndex());
    setOutputFormat(newData);
  }

  public static void main(String[] args){
    try{
      StringToNominalFilter filter = new StringToNominalFilter();
      filter.setOptions(new String[]{"-A", "10,200,TFIDF",
                                     "-A", "11,200,TFIDF",
                                     "-A", "12,200,TFIDF"});
      Instances input = new Instances(
        new java.io.FileReader("D:\\tmp\\ML-Weka\\Strings\\MUC7dataset.arff"));
      input.setClassIndex(18);
      filter.setInputFormat(input);
      for(int i = 0; i < input.numInstances(); i++){
        filter.input(input.instance(i));
      }
      filter.batchFinished();
      Instances output = filter.getOutputFormat();
      Instance instance = filter.output();
      while(instance != null){
        output.add(instance);
        instance = filter.output();
      }
      java.io.FileWriter fw = new java.io.FileWriter("D:\\tmp\\ML-Weka\\Strings\\MUC7dataset.filtered.arff");
      fw.write(output.toString());
      fw.flush();
      fw.close();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  protected int addLeaves(Map map){
    int res = 0;
    Iterator valuesIter = map.values().iterator();
    while(valuesIter.hasNext()){
      Object value = valuesIter.next();
      if(value instanceof WordData) res += ((WordData)value).count;
      else if(value instanceof Map) res += addLeaves((Map)value);
    }
    return res;
  }

  /**
   * Once the output format is defined this method can be used to covert
   * input instances into output instances.
   * @param inputInstance
   * @return the coverted output instance.
   */
  protected Instance processInstance(Instance inputInstance){
    Instance  newInstance = new Instance(getOutputFormat().numAttributes());
    newInstance.setDataset(getOutputFormat());
    for(int i = 0; i < getOutputFormat().numAttributes(); i++){
      if(inputInstance.isMissing(i)) newInstance.setMissing(i);
      else{
        if(isString(i)){
          String value = inputInstance.stringValue(i);
          if(getOutputFormat().attribute(i).indexOfValue(value) == -1){
            newInstance.setMissing(i);
          }else{
            newInstance.setValue(i, value);
          }
        }else{
          newInstance.setValue(i, inputInstance.value(i));
        }
      }
    }
    return newInstance;
  }

  /**
   * Checks whether the aqttribute at a particular index in the input dataset
   * is string.
   * @param index
   * @return a <tt>boolean</tt> value.
   */
  protected boolean isString(int index){
    
    int[] stringIndices = getInputStringIndex();
    for(int i = 0; i < stringIndices.length; i++)
      if(stringIndices[i] == index) return true;
    return false;
  }

  public Enumeration listOptions() {
    return optionsDesc.elements();
  }

  public void setOptions(String[] options) throws java.lang.Exception {
    this.options = options;
    parseOptions();
Iterator itr = attributesData.iterator();
while(itr.hasNext()){
  AttributeData aData = (AttributeData)itr.next();
  System.out.println("Attribute " + aData.index + " " + aData.maxCount + " " + aData.method);
}
  }

  public String[] getOptions() {
    return options;
  }

  /**
   * Parses the set of options supplied to this filter
   */
  protected void parseOptions() throws Exception{
    attributesData = new ArrayList();
    String option = Utils.getOption('A', options);
System.out.print("Option " + option);
    while(option != null && option.length() > 0){
      StringTokenizer strTok = new StringTokenizer(option, ",", false);
      int index = Integer.parseInt(strTok.nextToken());
System.out.print(": " + index);
      int maxCnt = Integer.parseInt(strTok.nextToken());
System.out.print(": " + maxCnt);
      //check if we got a method
      String method = null;
      if(strTok.hasMoreTokens()){
        method = strTok.nextToken();
        if(!method.equalsIgnoreCase(FREQUENCY) &&
           !method.equalsIgnoreCase(TFIDF)){
          throw new Exception("Unknown filtering method: " + method);
        }
      }
      attributesData.add(new AttributeData(index, maxCnt, method));
      //get the next "-A" option
      option = Utils.getOption('A', options);
    }
  }

  /**
   * Stores data about one attribute to be converted.
   */
  protected static class AttributeData implements Serializable{
    public AttributeData(int index, int count, String method){
      this.index = index;
      this.maxCount = count;
      this.method = method;
    }

    int index;
    int maxCount;
    String method;
  }

  protected static class WordData{
    public WordData(String word, int attrIndex, Double classValue, int count){
      this.word = word;
      this.attributeIndex = attrIndex;
      this.classValue = classValue;
      this.count = count;
    }

    public void inc(){
      count ++;
    }
    String word;
    int attributeIndex;
    Double classValue;
    int count;
  }

  protected static class WordCount implements Comparable{
    public WordCount(String word, int count){
      this.word = word;
      this.count = count;
      tfidf = -1;
    }

    public WordCount(String word, int count, double tfidf){
      this.word = word;
      this.count = count;
      this.tfidf = tfidf;
    }

    public int compareTo(Object other){
      return count - ((WordCount)other).count;
    }

    String word;
    int count;
    double tfidf;
  }

  /**
   * The options set on this filter.
   */
  private String[] options;

  protected List attributesData;
  /**
   * The description for the options accepted by this filter
   */
  protected static Vector optionsDesc;

  /**
   * Constant for conversion method.
   */
  public static final String FREQUENCY = "FREQ";

  /**
   * Constant for conversion method.
   */
  public static final String TFIDF = "TFIDF";
  /**
   * Static initialiser: creates the description for this filter's options.
   */
  static{
    optionsDesc = new Vector(1);
    Option option = new Option(
    "Selects one attribute for conversion. " +
    "The optional <method> argument can be one of FREQ or TFIDF " +
    "(the default is FREQ). " +
    "This option can be repeated for as many attributes as necessary.",
    "A", 1, "-A <index>,<max count>[,<method>] ...");
    optionsDesc.add(option);
  }
  
  
}