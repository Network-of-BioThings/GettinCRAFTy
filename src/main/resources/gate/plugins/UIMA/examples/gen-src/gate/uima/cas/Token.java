

/* First created by JCasGen Thu Nov 29 20:45:26 GMT 2012 */
package gate.uima.cas;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** A token in text
 * Updated by JCasGen Thu Nov 29 20:45:26 GMT 2012
 * XML source: /Users/ian/svn-release/gate/plugins/UIMA/examples/conf/uima_descriptors/AllTokenRelatedAnnotators.xml
 * @generated */
public class Token extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(Token.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected Token() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public Token(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public Token(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public Token(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: String

  /** getter for String - gets The string represented by the token
   * @generated */
  public String getString() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_String == null)
      jcasType.jcas.throwFeatMissing("String", "gate.uima.cas.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_String);}
    
  /** setter for String - sets The string represented by the token 
   * @generated */
  public void setString(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_String == null)
      jcasType.jcas.throwFeatMissing("String", "gate.uima.cas.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_String, v);}    
   
    
  //*--------------*
  //* Feature: LowerCaseLetters

  /** getter for LowerCaseLetters - gets The number of lower case letters in the token
   * @generated */
  public int getLowerCaseLetters() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_LowerCaseLetters == null)
      jcasType.jcas.throwFeatMissing("LowerCaseLetters", "gate.uima.cas.Token");
    return jcasType.ll_cas.ll_getIntValue(addr, ((Token_Type)jcasType).casFeatCode_LowerCaseLetters);}
    
  /** setter for LowerCaseLetters - sets The number of lower case letters in the token 
   * @generated */
  public void setLowerCaseLetters(int v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_LowerCaseLetters == null)
      jcasType.jcas.throwFeatMissing("LowerCaseLetters", "gate.uima.cas.Token");
    jcasType.ll_cas.ll_setIntValue(addr, ((Token_Type)jcasType).casFeatCode_LowerCaseLetters, v);}    
   
    
  //*--------------*
  //* Feature: Kind

  /** getter for Kind - gets The kind of Token - word, symbol, etc.
   * @generated */
  public String getKind() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_Kind == null)
      jcasType.jcas.throwFeatMissing("Kind", "gate.uima.cas.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_Kind);}
    
  /** setter for Kind - sets The kind of Token - word, symbol, etc. 
   * @generated */
  public void setKind(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_Kind == null)
      jcasType.jcas.throwFeatMissing("Kind", "gate.uima.cas.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_Kind, v);}    
   
    
  //*--------------*
  //* Feature: Orth

  /** getter for Orth - gets The orthography of the Token
   * @generated */
  public String getOrth() {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_Orth == null)
      jcasType.jcas.throwFeatMissing("Orth", "gate.uima.cas.Token");
    return jcasType.ll_cas.ll_getStringValue(addr, ((Token_Type)jcasType).casFeatCode_Orth);}
    
  /** setter for Orth - sets The orthography of the Token 
   * @generated */
  public void setOrth(String v) {
    if (Token_Type.featOkTst && ((Token_Type)jcasType).casFeat_Orth == null)
      jcasType.jcas.throwFeatMissing("Orth", "gate.uima.cas.Token");
    jcasType.ll_cas.ll_setStringValue(addr, ((Token_Type)jcasType).casFeatCode_Orth, v);}    
  }

    