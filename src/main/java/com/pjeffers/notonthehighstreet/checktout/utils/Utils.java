package com.pjeffers.notonthehighstreet.checktout.utils;

public class Utils {
	
	public static boolean isNullEmptyOrWhiteSpace(String value){
		boolean result=false;
		if((value==null)||(value.trim().equals(""))) {
			result=true;
		}
	
		return result;
	}
}
