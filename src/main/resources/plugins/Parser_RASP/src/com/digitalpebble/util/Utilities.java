package com.digitalpebble.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utilities {

	public static String getArch() {
		StringBuffer arch = new StringBuffer();
		String osarch = System.getProperty("os.arch");
		if (osarch.matches("i.86"))
			osarch = "ix86";
		else if(osarch.matches("amd64"))
			osarch = "x86_64";
		arch.append(osarch).append("_");
		String osName = System.getProperty("os.name").toLowerCase();
		arch.append(osName);
		return arch.toString();
	}

	public static void copyFile(File source, File target) throws IOException {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(source);
			out = new FileOutputStream(target);
			while (true) {
				int data = in.read();
				if (data == -1) {
					break;
				}
				out.write(data);
			}
			in.close();
			out.close();
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	
	// find all the caracters that can't be mapped to ISO 1
	// and normalise some of the punctuations. This is done in order
	// to get the right POS tags with RASP as it does not support UTF8
	public static String detectNonISO1Characters(String token) {
		char[] dup = token.toCharArray();
		boolean hasChanged = false;
		for (int c=0;c< token.length();c++) {
			char car = token.charAt(c);
			int value = (int) car;
			// ‑ ‒ – — ―
			if (value>=8208 && value<=8213){
				dup[c]='-';
				hasChanged=true;
			}
			else if (value>=8216 && value<=8219){
				dup[c]='\'';
				hasChanged=true;
			}
			// ' " etc...
			else if (value>=8220 && value<=8223){
				dup[c]='"';
				hasChanged=true;
			}
			else if (value>=8226 && value<=8231){
				dup[c]='.';
				hasChanged=true;
			}
			else if (value >= 256) {
				dup[c]='x';
				hasChanged=true;
			}
		}
		if (hasChanged==false)return token;
		return new String(dup);
	}
	
}
