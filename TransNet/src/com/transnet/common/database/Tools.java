package com.transnet.common.database;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tools {
	private Tools() {
	}

	public static String getCheckSum(String username, String password) {
		String input = username + password;
		MessageDigest digest = null;
		byte[] digestBytes = null;
		try {
			digest = MessageDigest.getInstance("MD5");
			digest.update(input.getBytes("UTF-8"));
			digestBytes = digest.digest();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (digestBytes != null) {
			StringBuffer hexString = new StringBuffer();
			for (byte digestByte : digestBytes) {
				hexString.append(Integer.toHexString(0xFF & digestByte));
			}
			return hexString.toString();
		}

		return null;
	}
	/*
	 * Escape the special character
	 */
	public static String Escape (String src)
	 {
	  int i;
	  char j;
	  StringBuffer tmp = new StringBuffer();
	  tmp.ensureCapacity(src.length()*6);

	  for (i=0;i<src.length() ;i++ )
	  {

	   j = src.charAt(i);

	   if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
	    tmp.append(j);
	   else
	    if (j<256)
	    {
	    tmp.append( "%" );
	    if (j<16)
	     tmp.append( "0" );
	    tmp.append( Integer.toString(j,16) );
	    }
	    else
	    {
	    tmp.append( "%u" );
	    tmp.append( Integer.toString(j,16) );
	    }
	  }
	  return tmp.toString();
	 }
	
	/*
	 * Unescape the special character
	 */
	 public static String Unescape (String src)
	 {
	  StringBuffer tmp = new StringBuffer();
	  tmp.ensureCapacity(src.length());
	  int  lastPos=0,pos=0;
	  char ch;
	  while (lastPos<src.length())
	  {
	   pos = src.indexOf("%",lastPos);
	   if (pos == lastPos)
	    {
	    if (src.charAt(pos+1)=='u')
	     {
	     ch = (char)Integer.parseInt(src.substring(pos+2,pos+6),16);
	     tmp.append(ch);
	     lastPos = pos+6;
	     }
	    else
	     {
	     ch = (char)Integer.parseInt(src.substring(pos+1,pos+3),16);
	     tmp.append(ch);
	     lastPos = pos+3;
	     }
	    }
	   else
	    {
	    if (pos == -1)
	     {
	     tmp.append(src.substring(lastPos));
	     lastPos=src.length();
	     }
	    else
	     {
	     tmp.append(src.substring(lastPos,pos));
	     lastPos=pos;
	     }
	    }
	  }
	  return tmp.toString();
	 }
	/*
	public static void main(String[] args){
		String u = "username";
		String p = "password";
		System.out.println(getCheckSum(u,p));
	}*/
}
