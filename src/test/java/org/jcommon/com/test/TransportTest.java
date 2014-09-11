package org.jcommon.com.test;

import java.io.IOException;

import org.jcommon.com.util.Report;

public class TransportTest{

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//System.out.println(TransportType.FLASH_SOCKET.toString());
		Report report = new Report(System.getProperty("user.dir"),"report.tmp");
		report.printLine("111");
		report.printLine("222");
		System.in.read();
	}

}
