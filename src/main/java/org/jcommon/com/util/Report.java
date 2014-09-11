package org.jcommon.com.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

public class Report {
	private Logger logger = Logger.getLogger(this.getClass());
	
	private String parent;
	private String child;
	
	private File file;
	
	public Report(String parent, String child){
		this.parent = parent;
		this.child  = child;
		
		file = new File(this.parent, this.child);
		if(!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("",e);
			}
		logger.info(String.format("parent:%s\nchild:%s", parent,child));
	}
	
	public PrintWriter getPrintWriter() throws IOException{
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
		return pw;
	}
	
	public void printLine(String str) throws NullPointerException, IOException{
		if(str!=null && file==null){
			throw new NullPointerException("str or outputstream is null");
		}
		PrintWriter pw =  getPrintWriter();
		pw.println(DateUtils.getNowSinceYear() + "\t"+ str);
		pw.close();
	}
}
