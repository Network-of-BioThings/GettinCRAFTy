/* Copyright (c) 2006-2009, Marian Olteanu <marian_DOT_olteanu_AT_gmail_DOT_com>
 All rights reserved.

 Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the following conditions are met:
 - Redistributions of source code must retain the above copyright notice, this list
 of conditions and the following disclaimer.
 - Redistributions in binary form must reproduce the above copyright notice, this
 list of conditions and the following disclaimer in the documentation and/or
 other materials provided with the distribution.
 - Neither the name of the University of Texas at Dallas nor the names of its
 contributors may be used to endorse or promote products derived from this
 software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package info.olteanu.utils;
import info.olteanu.interfaces.*;
import java.lang.reflect.*;

// version independent abstraction
// it was tested with both Java 5 and Java 6
// no recompilation needed
public class TextNormalizer
{
	public static StringFilter getNormalizationStringFilter() throws ClassNotFoundException
	{
		try
		{
			return new Java6Normalizer();
		}
		catch (Exception e)
		{}
		try
		{
			return new Java5Normalizer();
		}
		catch (Exception e)
		{}
		throw new ClassNotFoundException("Cannot instantiate a normalizer");
	}

	public static class Java6Normalizer implements StringFilter
	{
		private final Method normalizer;
		private final Object nfd;
		public Java6Normalizer() throws IllegalAccessException, ClassNotFoundException
		{
			normalizer = java6GetMethodNormalizer();
			nfd = java6GetNFD();
		}
		public String filter(String text)
		{
			try
			{
				return java6Invoke(text , normalizer , nfd);
			}
			catch (IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
			catch (InvocationTargetException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	private static String java6Invoke(String text , Method normalizer , Object nfd) throws IllegalAccessException, InvocationTargetException
	{
		//return java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD);// JDK 1.6 method
		return (String)normalizer.invoke(null , new Object[]{text , nfd});
	}

	private static Method java6GetMethodNormalizer() throws ClassNotFoundException
	{
		Class c = Class.forName("java.text.Normalizer");
		Method[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++)
			if (methods[i].getName().equals("normalize"))
				return methods[i];
		return null;
	}

	private static Object java6GetNFD() throws ClassNotFoundException, IllegalAccessException
	{
		Class x = Class.forName("java.text.Normalizer$Form");
		Object nfd = null;
		for (Field f : x.getDeclaredFields())
			if (f.getName().equals("NFD"))
				nfd = f.get(null);
		return nfd;
	}




	public static class Java5Normalizer implements StringFilter
	{
		private final Method normalizer;
		public Java5Normalizer() throws ClassNotFoundException
		{
			normalizer = java5GetMethodNormalizer();
		}
		public String filter(String text)
		{
			try
			{
				return java5Invoke(text , normalizer);
			}
			catch (IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
			catch (InvocationTargetException e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	private static final Integer ZERO = 0;
	private static String java5Invoke(String text, Method normalizer) throws InvocationTargetException, IllegalAccessException
	{
		//return sun.text.Normalizer.decompose(text, false, 0);// Pre-JDK 1.6 method
		return (String)normalizer.invoke(null , new Object[]{text , Boolean.FALSE , ZERO});
	}

	private static Method java5GetMethodNormalizer() throws ClassNotFoundException
	{
		Class c = Class.forName("sun.text.Normalizer");
		Method[] methods = c.getMethods();
		for (int i = 0; i < methods.length; i++)
			if (methods[i].getName().equals("decompose")
				&& methods[i].getGenericParameterTypes().length == 3)
				return methods[i];
		return null;
	}

	// method that is not portable
	// but it is faster
	// java 1.5.0_12: 2659ms vs 4096ms for 10 million calls
	// java 1.6.0_02: 3094ms vs 4264ms for 10 million calls
//	public static String normalize(String text)
//	{
//		return sun.text.Normalizer.decompose(text, false, 0);// Pre-JDK 1.6 method
//		return java.text.Normalizer.normalize(text, java.text.Normalizer.Form.NFD);// JDK 1.6 method
//	}

}