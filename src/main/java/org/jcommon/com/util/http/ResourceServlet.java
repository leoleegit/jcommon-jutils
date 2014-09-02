package org.jcommon.com.util.http;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ResourceServlet extends HttpServlet {
	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public boolean isAcessSource(HttpServletRequest request, String url){
		try{
			String path = request.getPathInfo();
			logger.info("path:"+path);
	        if ((path != null) && (path.length() != 0)) { 
		        if (path.startsWith("/")) path = path.substring(1);
		        String[] parts = path.split("/");
		        String name    = parts[0];
		        if(name!=null && name.startsWith(url)){
		            return true;
		        }
		    }
		}catch(Exception e){
			logger.error("", e);
		}	
		return false;
	}

	public String getParameter(HttpServletRequest request, String name){
		String url = request.getPathInfo();
		if(url!=null && url.indexOf(name+"=")!=-1){
			int start = url.indexOf(name+"=")+name.length()+1;
			int end   = url.indexOf("&", start)!=-1?url.indexOf("&", start):url.length();
			String value = url.substring(start, end);
			return value;
		}
		return null;
	}
}
