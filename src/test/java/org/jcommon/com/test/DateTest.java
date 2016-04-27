package org.jcommon.com.test;

import java.util.Date;

import org.jcommon.com.util.DateUtils;

public class DateTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//info create time:1437571982000;now:1437571377374
		 //long expire = Long.valueOf(expires_in);
		 long limit  = 5183999*1000l;
		 long create = 1437571982000l;
		 long now    = new Date().getTime();
		 System.out.println(DateUtils.getNowSinceYear(new Date(create)));
		 System.out.println(DateUtils.getNowSinceYear(new Date(now)));
		 System.out.println(DateUtils.getNowSinceYear(new Date(now+limit)));
	}

}
