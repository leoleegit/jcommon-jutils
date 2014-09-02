package org.jcommon.com.util;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

public class CoderUtils {
	private static Logger logger = Logger.getLogger(CoderUtils.class);
	
	public static String encodeToFlex(String url){
		if(url!=null)
			url        = url.replaceAll("\\+", "%20");
		return url;
	}
	
	public static String decode(String str){
		if(str!=null)
			try {
				str = java.net.URLDecoder.decode(str, "utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
		return str;
	}
	
	public static String encode(String str){
		if(str!=null)
			try {
				str = java.net.URLEncoder.encode(str, "utf-8");
				str = encodeToFlex(str);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
		return str;
	}
}
