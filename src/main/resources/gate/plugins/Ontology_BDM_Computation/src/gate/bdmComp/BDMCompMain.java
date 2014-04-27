/**
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Yaoyong Li 15/03/2009
 *
 *  $Id: IaaMain.java, v 1.0 2009-03-15 12:58:16 +0000 yaoyong $
 */

package gate.bdmComp;

import gate.Factory;
import gate.ProcessingResource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.ontology.OClass;
import gate.creole.ontology.Ontology;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO: the output file is always growing for each run of the PR
 * it's like if the local variables are not reset for each run so it
 * reuses the previous results plus the new
 */
public class BDMCompMain extends AbstractLanguageAnalyser implements
ProcessingResource {
  /** The ontology used. */
  Ontology ontology = null;
  /** The file storing the BDM score. */
  URL outputBDMFile = null;
  /** store the BDM information for each pair of concepts. */
  Set<BDMOne>bdmScores = null;
  
	/** Initialise this resource, and return it. */
	public gate.Resource init() throws ResourceInstantiationException {
    bdmScores = new HashSet<BDMOne>();
		return this;
	} // init()
	
	HashMap<OClass,Integer> concept2id= new HashMap<OClass,Integer>();
	/**
	 * Run the resource.
	 * 
	 * @throws ExecutionException
	 */
	public void execute() throws ExecutionException {
	  //open the result file
	  if(corpus != null) {
	    if(corpus.size() != 0)
	      if(corpus.indexOf(document)>0)
	        return;
	  }
	  BufferedWriter bdmResultsWriter = null;
	  boolean isExistingResultFile = false;
	  try {
	    if(outputBDMFile != null && !outputBDMFile.toString().equals("")) {
	      bdmResultsWriter = new BufferedWriter(new OutputStreamWriter(
	        new FileOutputStream(new File(outputBDMFile.toURI())), "UTF-8"));
	      isExistingResultFile = true;
	    }
	    else {
	      System.out.println("There is no file specified for storing the BDM scores!");
	    }
    
	  /** load the ontology. */ 
	  //if(ontologyUsed == null || ontologyUsed.toString().trim() == "") {
	    if(ontology == null) {
        throw new ExecutionException("No ontology: neither using a loaded ontology nor giving the ontology URL!");
	    }
	    /*else { 
	      // step 3: set the parameters 
	      
	    }*/
	  //}
	    //write the header of the bdm score file
	    if(isExistingResultFile) {
	      bdmResultsWriter.append("##The following are the BDM scores for ");
	      bdmResultsWriter.append("each pair of concepts in the ontology named "+ontology.getName()+".\n");
	    }
	    // retrieving a list of top classes 
	    Set<OClass> topClasses = ontology.getOClasses(true);
	    if(topClasses.size()>1) {
	      System.out.println("The ontology has "+topClasses.size() +" top classes!!");
	    }
	    // retrieving a list of all classes 
      Set<OClass> allConcepts = ontology.getOClasses(false);
      //assign a number id to each class
      
      HashMap<Integer,OClass> id2concept= new HashMap<Integer,OClass>();
      int num=1;
      for(OClass ob:allConcepts) {
        concept2id.put(ob, new Integer(num));
        id2concept.put(new Integer(num), ob);
        //System.out.println(num+", *"+ob.getName()+"*"+", id="+concept2id.get(ob).intValue()+"*");
        ++num;
      }
      
      System.out.println("ontology "+ontology.getName()+", allConcepts="+allConcepts.size());
      
      //for each concept, get the chain from it to the top class
      HashMap<OClass,String> concept2chain = new HashMap<OClass,String>();
      //obtainChains(concept2id, concept2chain);
      num=1;
      for(OClass curCon:allConcepts) {
        String chainSofar = "";
        int numS = curCon.getSuperClasses(OClass.DIRECT_CLOSURE).size();
        //if(numS>1)
          //System.out.println("****** curCon="+curCon.getName()+"*"+", num="+numS+"*");
        String chains = obtainAChain(curCon, chainSofar);
        concept2chain.put(curCon, chains);
        //chainId.append(concept2id.get(curCon).toString());
        /*String [] idsC = chains.split(ConstantParameters.separater2);
        for(int i=0; i<idsC.length; ++i) {
          String [] oneC = idsC[i].split(ConstantParameters.separater1);
          String conChains="";
          for(int j=0; j<oneC.length; ++j)
            conChains += " "+ id2concept.get(new Integer(oneC[j])).getName();
          System.out.println("num="+num+", concept:"+curCon.getName()+", chain="+conChains);
        }*/
        ++num;
      }
      //get the leaf nodes, and the chain length for each leafy node
      HashMap<OClass,String> leafyCon2Chain = new HashMap<OClass,String>();
      num = 1;
      for(OClass curCon:allConcepts) {
        if(curCon.getSubClasses(OClass.DIRECT_CLOSURE).size()==0) {
          //System.out.println(num+", leafy node="+curCon.getName()+"*");
          leafyCon2Chain.put(curCon, concept2chain.get(curCon));
          ++num;
        }
      }
      //compute the chain length coming through one node
      HashMap<OClass, Float> con2ChainLen = new HashMap<OClass, Float>();
      HashMap<OClass, Integer> con2ChainNum = new HashMap<OClass, Integer>();
      float n0BDM=0.0f;
      num = 0;
      for(OClass curCon:leafyCon2Chain.keySet()) {
        String [] idsC = leafyCon2Chain.get(curCon).split(ConstantParameters.separater2);
        String lenS = "";
        for(int i=0; i<idsC.length; ++i) {
          String [] oneC = idsC[i].split(ConstantParameters.separater1);
          int len = oneC.length-1;
          n0BDM += len;
          lenS += len + " ";
          //System.out.println(num+", con="+curCon.getName()+", len="+len+", lenS="+lenS+"*");
          ++num;
          //get each concept from the chain
          for(int j=0; j<oneC.length; ++j) {
            OClass con = id2concept.get(new Integer(oneC[j]));
            if(con2ChainLen.containsKey(con)) {
              int len00= con2ChainLen.get(con).intValue()+len;
              con2ChainLen.put(con, new Float(len00));
            } else {
              con2ChainLen.put(con, new Float(len));
            }
            if(con2ChainNum.containsKey(con)) {
              int len00= con2ChainNum.get(con).intValue()+1;
              con2ChainNum.put(con, new Integer(len00));
            } else {
              con2ChainNum.put(con, new Integer(1));
            }
          }
        }
        lenS = lenS.trim();
        //leafyCon2ChainLen.put(curCon, lenS);
      }
      if(num>1) n0BDM /= num;
      //compute the average chain length for each concept
      num=1;
      for(OClass curCon:con2ChainLen.keySet()) {
        float len = con2ChainLen.get(curCon).floatValue();
        len /= con2ChainNum.get(curCon).intValue();
        con2ChainLen.put(curCon, new Float(len));
        //curCon = (OClass) ontologyUsed.getOResourceByName(curCon.getName());
        //System.out.println(num+", con="+curCon.getName()+", averlen="+len+"*"+", num="+con2ChainNum.get(curCon).intValue());
        ++num;
      }
      //compute the number of branches for each concept 
      HashMap<OClass,Integer>concept2branch = new HashMap<OClass,Integer>();
      float averBran = 0.0f;
      num = 0;
      for(OClass curCon:allConcepts) {
        int len = curCon.getSubClasses(OClass.DIRECT_CLOSURE).size();
        if(len>0) {
          concept2branch.put(curCon, new Integer(len));
          averBran += len;
          ++num;
        }
      }
      if(num>1) averBran /= num;
      //Now compute the BDM for each pair of concept
      
      for(OClass curCon11:allConcepts) {
        String [] chainS11 = concept2chain.get(curCon11).split(ConstantParameters.separater2);
        for(OClass curCon22:allConcepts) {
          int id11 = concept2id.get(curCon11).intValue();
          int id22 = concept2id.get(curCon22).intValue();
          if(id11<id22) continue;
          BDMOne bdmS = new BDMOne(curCon11, curCon22);
          if(id11==id22) {
            //get the shortest chain
            int len=Integer.MAX_VALUE;
            for(int i=0; i<chainS11.length; ++i) {
              String [] items = chainS11[i].split(ConstantParameters.separater1);
              if(len>items.length) len = items.length;
            }
            len -=1;
            bdmS.setValues(1.0f, len, 0, 0, n0BDM, 1, 1, 1);
            bdmS.setMsca(curCon11);
            bdmScores.add(bdmS);
            
            continue;
          }
          String [] chainS22 = concept2chain.get(curCon22).split(ConstantParameters.separater2);
          int lenS11 = chainS11.length;
          int lenS22 = chainS22.length;
          for(int iS11=0; iS11<lenS11; ++iS11) {
            for(int iS22=0; iS22<lenS22; ++iS22) {
              //determine the common part of the two chains
              String [] chain11 = chainS11[iS11].split(ConstantParameters.separater1);
              String [] chain22 = chainS22[iS22].split(ConstantParameters.separater1);
              int len11 = chain11.length;
              int len22 = chain22.length;
              int len00=len11;
              if(len00>len22) len00 = len22;
              int cp =0;
              for(int ii=0; ii<len00; ++ii) {
                if(chain11[len11-1-ii].equals(chain22[len22-1-ii])) {
                  ++cp;
                }
                else break;
              }
              //System.out.println("cp="+cp+", ("+curCon11.getName()+","+curCon22.getName()+
                //"), ch1="+chainS11[iS11]+",ch2="+chainS22[iS22]);
              float m1, m2;
              m1 = con2ChainLen.get(curCon11).floatValue();
              m2 = con2ChainLen.get(curCon22).floatValue();
              if(cp==0) { //the two concepts are not in the same connect part of ontology
                bdmS.setValues(0, -1, len11-1, len22-1, n0BDM, m1, m2, 1.0f);
              } else {
                Integer commonConId;
                commonConId=new Integer(chain11[len11-cp]); //get the common concept
                OClass commonCon = id2concept.get(commonConId);
                
                //System.out.println("comId="+commonConId.intValue()+", con="+commonCon.getName());
                
                cp -= 1; //count the edges, not the nodes
                int dpk, dpr;
                dpk = len11-1-cp;
                dpr = len22-1-cp;
                //compute the averaged branch in the chain from key to response
                int num11=0;
                float bran = 0.0f;
                if(concept2branch.containsKey(commonCon))
                  bran += concept2branch.get(commonCon).intValue();
                ++num11;
                
                for(int i=1; i<len11-cp-1; ++i) {
                  OClass con = id2concept.get(new Integer(chain11[i]));
                  if(concept2branch.containsKey(con))
                    bran += concept2branch.get(con).intValue();
                  ++num11;
                }
                for(int i=1; i<len22-cp-1; ++i) {
                  OClass con = id2concept.get(new Integer(chain22[i]));
                  if(concept2branch.containsKey(con))
                    bran += concept2branch.get(con).intValue();
                  ++num11;
                }
                if(num11>1) bran /= num11;
                bran /= averBran;
                //compute the bdm score for the two chains
                float bdm = bran*cp/n0BDM;
                bdm = bdm/(bdm+dpk/m1 + dpr/m2);
                if(bdm>bdmS.bdmScore) {
                  bdmS.setValues(bdm, cp, dpk, dpr, n0BDM, m1, m2, bran);
                }
                bdmS.setMsca(commonCon);
              }
            }
          } //end of the loop for the chains of the two concepts
          bdmScores.add(bdmS);
          
        }//end of the loop for the second concept
      }//end of the loop for the first concept
      //write the results into a file or console
      for(BDMOne oneb:bdmScores) {
        String text = oneb.printResult();
        if(isExistingResultFile) {
          bdmResultsWriter.append(text+"\n");
        } else {
          System.out.println(text);
        }
      }
      if(isExistingResultFile) {
        bdmResultsWriter.flush();
      }

	  }
    catch(UnsupportedEncodingException e1) {
      e1.printStackTrace();
    }
    catch(FileNotFoundException e1) {
      e1.printStackTrace();
    }
    catch(URISyntaxException e1) {
      e1.printStackTrace();
    }
    catch(IOException e) {
      e.printStackTrace();
    }
    finally { // in any case
      if(isExistingResultFile) {
        try {
          // close the output file
          bdmResultsWriter.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      // unload the ontology
      if (ontology != null) Factory.deleteResource(ontology);
    }
      
	  
	}
	/** recursive function to get the chain */
	String obtainAChain(OClass curCon, String chainSofar) {
	  String chainNow = "";
	  //String chainSofar = "";
	  //System.out.println("conCur=*"+curCon.getName()+"*"+", chainsofar=*"+chainSofar+"*");
	  //System.out.println("conId="+concept2id.get(curCon)+"*");
	  chainSofar += concept2id.get(curCon).toString();
	  if(curCon.isTopClass()) {
	    chainNow = chainSofar + ConstantParameters.separater2;
	    return chainNow;
	  } else {
	    Set<OClass> superCons = 
        curCon.getSuperClasses(OClass.DIRECT_CLOSURE);
	    chainSofar += ConstantParameters.separater1;
	    //if(superCons.size()>1) {
	     // System.out.println("****** curCon="+curCon.getName()+"*"+", num="+superCons.size());
	    //}
	    for(OClass oneCon:superCons) {
	      oneCon = (OClass) ontology.getOResourceByName(oneCon.getName());
	      chainNow +=  obtainAChain(oneCon, chainSofar);
	    }
	  }
	 return chainNow;
	}
	
	public void setOntology(Ontology ontology) {
    this.ontology = ontology;
  }

  public Ontology getOntology() {
    return this.ontology;
  }
  public void setOutputBDMFile(URL ontoU) {
    this.outputBDMFile = ontoU;
  }

  public URL getOutputBDMFile() {
    return this.outputBDMFile;
  }
  
  //public void setOntology(Ontology onto) {
    //this.ontologyUsed = onto;
  //}

  //public Ontology getOntology() {
   // return this.ontologyUsed;
  //}
}
