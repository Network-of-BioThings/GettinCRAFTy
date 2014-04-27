/**
   This file contains an example use of the Minipar API. The program
   reads a sentence from the standard input one line at a time (upto
   1024 characters), treating each line as a sentence. The outputs of
   the program are the parse trees of the input sentences. Using the
   command line options one output the parse trees in constituency or
   dependency format and decide which features to include in the
   output.

   The printing routines in this file should provide some idea how to
   access the information contained in a ParseTree object.  

   @modified by: Mr. Niraj Aswani (GATE Team)
   NOTE:  This version of the code is based on the example code
   available at http://www.cs.ualberta.ca/~lindek/minipar.htm
   Instead of taking one line from command prompt, this version of code
   accepts the file name provided with -file tag.  It interprets the file
   and reads one line at a time from the file and generates the parse tree.
   This process repeats until it reaches to the end of the file.
*/

#include <cstring>
#include <cstdlib>
#include <iostream>
#include <fstream>
using namespace std;

//#include "quotewhite.h"
#define quote_white(x) x
#include "ptree.h"


char *empty_cat_str = "()";

const int MAX_LINE_LEN=1024;

int PRINT_ROOT = 0;
int PRINT_RELATION = 0;
int PRINT_FEATURE = 0;
int PRINT_TRIPLES = 0;
int INTERACTIVE = 1;
int HAS_COMMANDS = 0;

void print_triples(const ParseNode* node)
{
  ParseNode* parent = ((ParseNode*) node)->parent();
  if (node->relation() && parent && parent->category()) {
    if (parent->root() && parent->category() && node->category() && node->root() && node->relation())
      cout << parent->root() << "\t"
	   << parent->category() << ':' << node->relation()
	   << ':' << node->category() << "\t"
	   << node->root() << endl;
  }
  forall (ParseNode*, sub, TRSTree, node) {
    print_triples(sub);
  }
}

void print_parse_tree(const ParseNode* n, int level)
{
  ParseNode* node = dynamic_cast(ParseNode*, n);
  if (node==0)
    return;
  cout << endl;
  // print two spaces for each level of indentation
  if (node->label())
    cout << node->label() << '\t';
  else     cout << node->high() << '\t';
  for (int i = 0; i<level; i++)
    cout << "  ";

  cout << "(";
  if (node->category())
    cout << node->category();

  if (node->word()) {
    if (strpbrk(node->word(), " \t\n()"))
      cout << " \"" << node->word() << "\"";
    else if (strcmp(node->word(), "\"")==0)
      cout << " \"\\\"\"";
    else
      cout << " " << node->word();
  }
  if (PRINT_ROOT || PRINT_RELATION || PRINT_FEATURE) {
    if (PRINT_ROOT && node->root())
      cout << " (root " << node->root() << ")";
    if (PRINT_RELATION && node->relation())
      cout << " (relation " << node->relation() << ")";
    if (PRINT_FEATURE) {
      cout << " (atts";
      // print the set of features
      forall (FeatureValue, fv, TArray, node->features()) {
	cout << " (" << fv._feature << ' ' << fv._value << ")";
      }
      cout << ")";
    }
  }

  // print the subtrees
  forall (ParseNode*, sub, TRSTree, node) {
    print_parse_tree(sub, level+1);
  }
  cout << ")";
}

void print_dependency_node(const ParseNode* n)
{
  ParseNode* node = dynamic_cast(ParseNode*, n);
  if (node==node->tree()->root())
    return;
  cout << node->label() << "\t";
  if (node->word() && *node->word()) 
    cout << quote_white(node->word());
  else
    cout << empty_cat_str;  /* was << "()" */
  cout << '\t';
  if (node->root()) {
    if (node->word() && strcasecmp(node->root(), node->word())==0)
      cout << '~';
    else
      cout << quote_white(node->root());
  }
  cout << "\t";
  if (node->category())
    cout << node->category();
  else
    cout << "U";
  cout << '\t';
  if (node->parent() && node->parent()!=node->tree()->root())
    cout << node->parent()->label();
  else cout << "*";
  cout << '\t';
  if (node->relation())
    cout << node->relation();
  if (node->parent() && node->parent()->root())
    cout << "\t(gov " 
	 << quote_white(node->parent()->root()) << ")";
  if (node->antecedent())
    cout << "\t(antecedent " << node->antecedent()->label() << ")";
  if (!node->features().empty()) {
    cout << "\t(atts";
    // print the set of attributes
    forall (FeatureValue, fv, TArray, node->features()) {
      cout << " (" << fv._feature << ' ' << fv._value << ")";
    }
    cout << ")";
  }
  cout << endl;
}

void print_dependency_tree(const ParseNode* n)
{
  int print_self = 0;
  if (n==0)
    return;
  ParseNode* node = dynamic_cast(ParseNode*, n);
  if (node->parent()==0)
    cout << "(" << endl;
  int pos = node->head_pos();
  forall (ParseNode*, sub, TRSTree, node) {
    int sub_pos = sub->head_pos();
    if (print_self==0 && sub_pos>=pos) {
      print_dependency_node(node);
      print_self = 1;
    }
    print_dependency_tree(sub);
  }
  if (!print_self)
    print_dependency_node(node);    
  if (node->parent()==0)
    cout << ")";
}

int main(int argc, char* argv[])
{
  char line[MAX_LINE_LEN+2];
  char* features=0;
  const char* paths = getenv("MINIPATH");
  const char* fileName = "temp.txt";
  //  ParseType type = DEPENDENCY;
  ifstream in;

  for (int i = 1; i<argc; i++) {
    if (strcmp(argv[i], "-i")==0)
      INTERACTIVE = 0;
    else if (strcmp(argv[i], "-r")==0)
      PRINT_ROOT = 1;
    else if (strcmp(argv[i], "-d")==0) {
      HAS_COMMANDS = 1;
    }
    else if (strcmp(argv[i], "-t")==0) {
      PRINT_TRIPLES = 1;
      //      type = DEPENDENCY;
    }
    else if (strcmp(argv[i], "-f")==0 && i<argc) {
      PRINT_FEATURE = 1;
      features = argv[++i];
    }
    else if (strcmp(argv[i], "-l")==0)
      PRINT_RELATION = 1;
    else if (strcmp(argv[i], "-p")==0 && i<argc)
      paths = argv[++i];
    else if (strcmp(argv[i], "-e")==0 && i<argc)
      empty_cat_str = argv[++i];
    else if(strcmp(argv[i], "-file")==0 && i<argc)
      fileName = argv[++i];
  }

  if (paths==0) {
    cerr << "The search path for data files are not defined.\n ";
    cerr << "You must use '-p PATHS' option or define the environment\n"
	 << "variable MINIPATH" << endl;
    exit(1);
  }


  initialize_minipar(paths);
  if (features)
    extract_features(features);

  ParseTree parsetree;

  in.open(fileName);
  if(in.fail()) return -1;

  while (!in.eof()) {

    in.getline(line, MAX_LINE_LEN);
    cout << HAS_COMMANDS;

    if (HAS_COMMANDS && line[0]=='~') {
      interpret_command_line(line+1);
    }
    else {
      parsetree.reset();
      parse(line, parsetree);
      if (PRINT_TRIPLES) 
	print_triples(parsetree.root());
      else {
	print_dependency_tree(parsetree.root());
     }
      cout << endl;
    }
  }
  in.close();
}
