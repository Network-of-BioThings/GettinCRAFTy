package com.digitalpebble.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/** 
 * Copy the data from an inputStream into an output stream 
 **/
public class StreamWriter implements Runnable {
  private static final int SIZE = 1024;

  private InputStream is;
  private OutputStream os;
  
  public StreamWriter(InputStream input, OutputStream output) {
    is = input;
    os=output;
  }

  public void run() {
    byte[] buf = new byte[SIZE];
    int len;
    try {
      while((len = is.read(buf)) > 0) {
        os.write(buf, 0, len);
      }
    } catch(IOException e) {
      e.printStackTrace();
    }
    finally {
      try {
        os.close();
        is.close();
      } catch(IOException e) {}

    }
  }

}
