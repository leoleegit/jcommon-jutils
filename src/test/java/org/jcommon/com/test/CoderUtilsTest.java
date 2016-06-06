package org.jcommon.com.test;

import java.io.UnsupportedEncodingException;
import java.security.KeyStore;

import org.jcommon.com.util.CoderUtils;

public class CoderUtilsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "Hi%20Mic%E3%80%82%E6%98%8E%E7%99%BD%E6%82%A8%E6%83%B3%E6%9F%A5%E8%A9%A2%E7%B6%B2%E4%B8%8A%E8%A1%8C%E5%8F%8Anow%20TV%E6%9C%8D%E5%8B%99%E3%80%82%E7%82%BA%E9%80%B2%E4%B8%80%E6%AD%A5%E5%8D%94%E5%8A%A9%E6%82%A8%EF%BC%8C%E8%AB%8B%E6%82%A8%E9%80%8F%E9%81%8E%E6%88%91%E5%93%8B%20facebook%20%E5%B0%88%E9%A0%81%20https%3A%2F%2Fwww.facebook.com%2Fpccwcs%20%E5%8F%B3%E4%B8%8A%E6%96%B9%E6%8C%89%20%27%20%E8%A8%8A%E6%81%AF%27%20%E5%B0%87%E6%9C%89%E9%97%9C%E5%B8%B3%E6%88%B6%E8%B3%87%E6%96%99%20%28%E5%A6%82%E7%99%BB%E5%85%A5%E5%90%8D%E7%A8%B1%E3%80%81%E8%B3%AC%E6%88%B6%E8%99%9F%E7%A2%BC%E6%88%96%E7%9B%B8%E9%97%9C%E9%9B%BB%E8%A9%B1%E8%99%9F%E7%A2%BC%29%E5%90%8C%E7%99%BB%E8%A8%98%E4%BA%BA%E5%85%A8%E5%90%8Dsend%E7%95%80%E6%88%91%E5%93%8B%EF%BC%8C%E7%95%B6%E6%94%B6%E5%88%B0%E8%B3%87%E6%96%99%E5%BE%8C%E6%88%91%E5%93%8B%E6%9C%83%E7%AB%8B%E5%8D%B3%E4%BD%9C%E5%87%BA%E8%B7%9F%E9%80%B2%E3%80%82Thx.%0A%0AGrace";
		if(str!=null)
			try {
				str = java.net.URLDecoder.decode(str, "utf-8");
				Boolean.parseBoolean(null);
				System.out.println(str);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
	}

}
