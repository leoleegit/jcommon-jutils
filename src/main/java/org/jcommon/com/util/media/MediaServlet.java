package org.jcommon.com.util.media;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

public class MediaServlet extends HttpServlet {
	protected Logger logger = Logger.getLogger(this.getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MediaFactory mediaFactory;
	
	public MediaServlet(MediaFactory mediaFactory){
		this.mediaFactory = mediaFactory;
	}
	
	public MediaServlet(){
		this.mediaFactory = new DefaultMediaFactory();
	}
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
    	throws ServletException, IOException{
	    request.setCharacterEncoding("utf-8");
		
		String errormsg = null;
		List<FileItem> list   = new ArrayList<FileItem>();
		List<UrlObject> urls  = new ArrayList<UrlObject>();
		if(ServletFileUpload.isMultipartContent(request)){
			FileItem item = null;
			try{
				ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
				@SuppressWarnings("unchecked")
				List<FileItem>  items = upload.parseRequest(request);
				Iterator<FileItem> it = items.iterator();
				while (it.hasNext()){
					item = it.next();
					if (!item.isFormField()){
						list.add(item);
					}
				}
			}
			catch (Throwable e){
				logger.error("", e);
				errormsg = "transfer exception";
			}
		}else{
			errormsg = "not found multipart content";
		}
		if(errormsg!=null){
			error(response,errormsg);
			return;
		}
		for(FileItem item : list){
			try{
				String file_name     =  item.getName();
				String file_id       = org.jcommon.com.util.BufferUtils.generateRandom(20);
				String content_type  = item.getContentType();
				
				Media media = new Media(null);
				media.setContent_type(content_type);
				media.setMedia_id(file_id);
				media.setMedia_name(file_name);
				
				FileOutputStream out_file = null;
				java.io.File file  = mediaFactory.createEmptyFile(media);
				media.setMedia(file);
				out_file = new FileOutputStream(file);
				InputStream is = item.getInputStream();
				logger.info("uploading...........");
				byte[] b = new byte[1024];
				int nRead;
				while((nRead = is.read(b, 0, 1024))>0){
				   out_file.write(b, 0, nRead);
				}
				try {
					out_file.close();
					out_file.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					file.delete();
					throw e1;
				}
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					throw e;
				}
				String url = mediaFactory.createUrl(media).getUrl();
				logger.info(url);
				urls.add(new UrlObject(url));
				
			}catch (Throwable e){
				logger.error("", e);
				errormsg = "transfer exception";
			}
		}
		if(errormsg!=null){
			error(response,errormsg);
			return;
		}else{
			String msg = "";
			if(urls.size()>0){
				if(urls.size()==1)
					msg = urls.get(0).toJson();
				else
					msg = org.jcommon.com.util.JsonObject.list2Json(urls);
			}
			logger.info(msg);
			response.getWriter().println(msg);
		}	
  }

  protected void error(HttpServletResponse response, String msg) throws IOException {
	  Error error = new Error(null);
	  error.setType("Media");
	  error.setMessage(msg);
	  response.getWriter().println(error.toJson());
  }
  
  protected void doGet(HttpServletRequest request, HttpServletResponse response) 
  	throws ServletException, IOException {
	  String path = request.getPathInfo();
	  logger.info("path:"+path);
		
	  String errormsg = null;
	  if(path==null)
		  errormsg = "access path is null";
	  else{
		  Media media = mediaFactory.getMediaFromUrl(path);
		  String file_name    = media.getMedia_name();
		  String content_type = media.getContent_type();
		  java.io.File   file = media.getMedia();
		  if(file==null || !file.exists()){
			  logger.warn("file not found:"+media.getMedia_name());
			  errormsg = "file not found";
		  }else{
			  try{
				    String user_agent = request.getHeader("User-Agent");
				    if(user_agent.toLowerCase().indexOf("msie")!=-1){
						file_name = org.jcommon.com.util.CoderUtils.encode(file_name); 
					}
					else{
						file_name = file_name.replaceAll(" ", "");
						file_name = new String(file_name.getBytes("utf-8"),"iso-8859-1");
					}
		            logger.info(file_name);
					response.reset();  
					response.setContentType( content_type ); 
		            response.addHeader( "Content-Disposition" ,  "attachment;filename=\""   +   file_name+"\"");  
		            response.addHeader( "Content-Length" ,  ""   +  file.length());  
		            
					InputStream is = new FileInputStream(file);
					OutputStream out = response.getOutputStream();
					
					byte[] b = new byte[1024];
					int nRead;
					while((nRead = is.read(b, 0, 1024))>0){
					   out.write(b, 0, nRead);
					}
					try {
						is.close();
						out.close();
						out.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						throw e1;
					}
				}catch(IOException e){
					logger.error("", e);
					errormsg = e.getMessage();
				}
		    }	
		}
		if(errormsg!=null){
			logger.info(errormsg);
			response.getWriter().print(errormsg);
		}
	}

	public MediaFactory getMediaFactory() {
		return mediaFactory;
	}
	
	public void setMediaFactory(MediaFactory mediaFactory) {
		this.mediaFactory = mediaFactory;
	}
}

