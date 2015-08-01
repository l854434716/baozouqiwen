package com.ultrapower.baozouqiwen.util;

import java.io.UnsupportedEncodingException;


public class StringUtil {
	
	public  static  String  deCode(String soureString ,String soureCode, String newCode){
		
		try {
			return new String(soureString.getBytes(soureCode),newCode);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return soureString;
		}
	}
	
	
	public static String  changeDataFormat(String rq){
		StringBuilder sb= new StringBuilder();
		
		sb.append(rq.substring(0, 4));
		sb.append('-');
		sb.append(rq.substring(4, 6));
		sb.append('-');
		sb.append(rq.substring(6));
		
		return sb.toString();
	}
	
  
	

}
