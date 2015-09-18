package org.jcommon.com.util.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jcommon.com.util.BufferUtils;

public class FileRequest extends HttpRequest{
  private final static String crlf = "\r\n";
  private final static String twoHyphens = "--";
  private final static String boundary = "----------" + System.currentTimeMillis();
  private File file;
  private String name;
  private String filename;
  private String content_type;
  private Map<String,String> headers = new HashMap<String,String>();

  public FileRequest(String url, File file, HttpListener listener) {
	  super(url,listener);
	  this.file = file;
  }
  
  public FileRequest(String url, File file, String filename, String name, String content_type, HttpListener listener) {
	  super(url,null,HttpRequest.POST,listener);
	  this.file         = file;
	  this.name         = name;
	  this.filename     = filename;
	  this.content_type = content_type;
	  if(file!=null && filename==null){
		  this.filename = file.getName();
		  this.content_type = ContentType.getContentType(file).type;
	  }
	  if(name == null)
		  this.name = filename;
  }
  
  public FileRequest(String url, File file, String name, HttpListener listener) {
	  this(url,file,null,name,null,listener);
  }

  public void addHeader(String name, String value){
	  headers.put(name, value);
  }
  
  public void delHeader(String name){
	  headers.remove(name);
  }
  
  public void run(){
    if (HttpRequest.POST == this.method_)
      upload();
    else
      download();
  }

  private void download(){
    HttpURLConnection httpUrlConnection = null;
    try {
    	 logger.info(getUrl());
         URL url = new URL(this.url_);
         httpUrlConnection = (HttpURLConnection)url.openConnection();
         httpUrlConnection.setRequestMethod("GET");
         httpUrlConnection.setRequestProperty("content-type", "binary/data");

         httpUrlConnection.setConnectTimeout(15000);
         httpUrlConnection.setReadTimeout(15000);

         int responseCode = httpUrlConnection.getResponseCode();

         if (responseCode == 200) {
    	    String raw = httpUrlConnection.getHeaderField("Content-Disposition");
            String content_type = httpUrlConnection.getHeaderField("Content-Type");
            setContent_type(content_type);
            if(file==null || file.isDirectory()){
        	  if(file!=null && !file.exists()){
        		  file.mkdirs();
        	  }else{
        		  file = new File(System.getProperty("java.io.tmpdir"));
        	  }
        	  String fileName = null;

              if ((raw != null) && (raw.indexOf("=") != -1)) {
                  fileName = raw.split("=")[1];
                  if (fileName.startsWith("\""))
                	  fileName = fileName.substring(1);
                  if (fileName.endsWith("\""))
                	  fileName = fileName.substring(0, fileName.length() - 1);
              } else {
                  fileName = BufferUtils.generateRandom(20);
              }
              file = new File(file.getAbsolutePath(),fileName);
            }
//          if ("text/plain".equalsIgnoreCase(content_type)) {
//              InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());
//
//              BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
//              String line = "";
//              while ((line = responseStreamReader.readLine()) != null)
//              {
//                this.sResult.append(line).append("\n");
//              }
//              responseStreamReader.close();
//              logger.info("[URL][response][failure]" + this.sResult + "\n" + this.url_);
//              if (this.listener_ != null) this.listener_.onFailure(this, this.sResult);
//              return;
//          }
           if (this.file.exists()) {
               this.file.mkdir();
           }
           InputStream in = httpUrlConnection.getInputStream();
           FileOutputStream out = new FileOutputStream(this.file);

           byte[] b = new byte[1024];
           int count;
           while ((count = in.read(b)) > 0) {
               out.write(b, 0, count);
           }
           out.flush();
           out.close();
           in.close();
           this.sResult.append("download file done:" + this.file.getAbsolutePath());
           if (this.listener_ != null) this.listener_.onSuccessful(this, this.sResult); 
      }
      else if (responseCode >= 400) {
        InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());

        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
        String line = "";
        while ((line = responseStreamReader.readLine()) != null){
          this.sResult.append(line).append("\n");
        }
        responseStreamReader.close();
        logger.info("[URL][response][failure]" + this.sResult + "\n" + this.url_);
        if (this.listener_ != null) this.listener_.onFailure(this, this.sResult); 
      }
      else{
        this.sResult.append("[URL][response][failure][code : " + responseCode + " ]");
        if (this.listener_ != null) this.listener_.onFailure(this, this.sResult);
        logger.info("[URL][response][failure][code : " + responseCode + " ]" + "\n" + this.url_);
      }
    } catch (SocketTimeoutException e) {
      if (this.listener_ != null) this.listener_.onTimeout(this);
      logger.warn("[HttpReqeust]TimeOut:" + this.url_);
    } catch (Exception e) {
      if (this.listener_ != null) this.listener_.onException(this, e);
      logger.warn("[HttpReqeust] error:" + this.url_ + "\n" + e);
    } finally {
      if (httpUrlConnection != null)
        httpUrlConnection.disconnect();
    }
  }

  private void upload() {
      HttpURLConnection httpUrlConnection = null;
      try {
    	  logger.info(getUrl());
    	  logger.info(String.format("filename:%s;content_type:%s", filename,content_type));
	      URL url = new URL(this.url_);
	      httpUrlConnection = (HttpURLConnection)url.openConnection();
	      httpUrlConnection.setUseCaches(false);
	      httpUrlConnection.setDoOutput(true);
	
	      httpUrlConnection.setRequestMethod("POST");
	      httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
	      httpUrlConnection.setRequestProperty("Charset", getCharset());
	      httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
	      httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	      DataOutputStream out = new DataOutputStream(httpUrlConnection.getOutputStream());
	
	      StringBuilder head = new StringBuilder();
	      head
	      .append(twoHyphens).append(boundary).append(crlf)
	      .append("Content-Type:").append(content_type).append(crlf)
	      .append("Content-Disposition:")
	      	  .append("form-data;")
	      	  .append("name=\"" + this.name + "\";")
	      	  .append("filename=\"" + this.filename + "\"").append(crlf);
	      for(String name : headers.keySet()){
	    	  head.append(name).append("=\"").append(headers.get(name)).append("\"").append(crlf);
	      }
	      head.append(crlf);
	      out.write(head.toString().getBytes(getCharset()));
	     
	      FileInputStream fis = new FileInputStream(this.file);
	      byte[] intOutb = new byte[1024];
	      while(fis.read(intOutb)>0){
	    	  out.write(intOutb);
	      }
	      fis.close();
	
	      StringBuilder foot = new StringBuilder();
	      foot
	      .append(crlf)
	      .append(twoHyphens).append(boundary).append(twoHyphens).append(crlf);
	      out.write(foot.toString().getBytes(getCharset()));
	      
	      out.flush();
	      out.close();
	     
	      int responseCode = httpUrlConnection.getResponseCode();
	      if (responseCode == 200) {
	        InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());
	
	        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
	        String line = "";
	        while ((line = responseStreamReader.readLine()) != null)
	        {
	          this.sResult.append(line).append("\n");
	        }
	        responseStreamReader.close();
	        if (this.listener_ != null) this.listener_.onSuccessful(this, this.sResult);
	      }
	      else if (responseCode >= 400) {
	        InputStream responseStream = new BufferedInputStream(httpUrlConnection.getInputStream());
	
	        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
	        String line = "";
	        while ((line = responseStreamReader.readLine()) != null)
	        {
	          this.sResult.append(line).append("\n");
	        }
	        responseStreamReader.close();
	        logger.info("[URL][response][failure]" + this.sResult + "\n" + this.url_);
	        if (this.listener_ != null) this.listener_.onFailure(this, this.sResult); 
	      }
	      else{
	        this.sResult.append("[URL][response][failure][code : " + responseCode + " ]");
	        if (this.listener_ != null) this.listener_.onFailure(this, this.sResult);
	        logger.info("[URL][response][failure][code : " + responseCode + " ]" + "\n" + this.url_);
	      }
      } catch (SocketTimeoutException e) {
	      if (this.listener_ != null) this.listener_.onTimeout(this);
	      logger.warn("[HttpReqeust]TimeOut:" + this.url_);
      } catch (Exception e) {
	      if (this.listener_ != null) this.listener_.onException(this, e);
	      logger.warn("[HttpReqeust] error:" + this.url_ + "\n" + e);
      } finally {
	      if (httpUrlConnection != null)
	          httpUrlConnection.disconnect();
      }
  }

  public File getFile() {
    return this.file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public String getContent_type() {
	return content_type;
  }

  public void setContent_type(String content_type) {
	this.content_type = content_type;
  }

  public void setName(String name) {
	  this.name = name;
  }
	
  public String getName() {
	  return name;
  }
}