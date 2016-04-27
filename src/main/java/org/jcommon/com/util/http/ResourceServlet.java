package org.jcommon.com.util.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ResourceServlet extends HttpServlet {
	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String path = getAcessSource(request);
		if(path!=null){
			logger.info("path:"+path);
	  		InputStream is = getInputStream(path);
	  		if(is==null){
	  			response.getWriter().println("404");
	  			return;
	  		}
	        try {
	        	String file_type  = path.indexOf(".")!=-1?path.substring(path.lastIndexOf(".")):"";
	        	response.setContentType(ContentType.getContentType(file_type).type);
	  			OutputStream os = response.getOutputStream();
	  			byte[] buffer = new byte[65536];
	  	        int len;
	  	        while ((len = is.read(buffer)) >= 0)
	  	           os.write(buffer, 0, len);
	  		} catch (IOException e) {
	  			// TODO Auto-generated catch block
	  			logger.error("", e);
	  		}
		}else{
			response.getWriter().println("404");
		}
	}
	
	public InputStream getInputStream(String path){
		return getClass().getClassLoader().getResourceAsStream(path);
	}
	
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
	
	public String getAcessSource(HttpServletRequest request){
		try{
			String path = request.getPathInfo();
			logger.info("path:"+path);
	        if ((path != null) && (path.length() != 0)) { 
		        if (path.startsWith("/")) path = path.substring(1);
		        return path;
		    }
		}catch(Exception e){
			logger.error("", e);
		}	
		return null;
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
