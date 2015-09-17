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
		String str = "https%3A%2F%2Flivechat4.pccw.com%2Fchat%2Findex.jsp%3Fc_t%3DNTK%26c_id%3DGuest%26m_n%3DLGIF_PA_LGIF%26l%3Dzh%26n_n%3DGuest%26c_s%3DCONS%26e_s%3Dwww.hkt.com";
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
