package org.jcommon.com.test;

import org.jcommon.com.util.CoderUtils;

public class CoderUtilsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "PCCW%20Customer%20Service%20%28official%29";
		System.out.println(CoderUtils.decode(str));
	}

}
