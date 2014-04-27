package GettingCRAFTy.Examples;

import gate.Gate;

public class Example {

    // method main(): ALWAYS the APPLICATION entry point
    public static void main (String[] args) throws Exception{
	System.out.println ("Hello World!");

	gate.Gate.init();

	// load ANNIE as an application from a gapp file 
	// SerialAnalyserController controller = (SerialAnalyserController) 
	//     PersistenceManager.loadObjectFromFile(new File(new File( 
	// 							    Gate.getPluginsHome(), ANNIEConstants.PLUGIN_DIR), 
	// 						   ANNIEConstants.DEFAULT_FILE));

    }
}
