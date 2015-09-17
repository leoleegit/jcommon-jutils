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

import org.jcommon.com.util.BufferUtils;

public class FileRequest extends HttpRequest{
  String crlf = "\r\n";
  String twoHyphens = "--";
  String boundary = "*****";
  private File file;
  private String filename;
  private String content_type;

  public FileRequest(String url, File file, HttpListener listener) {
	  super(url,listener);
	  this.file = file;
  }
  
  public FileRequest(String url, File file, String method, HttpListener listener) {
	  super(url,null,method,listener);
	  this.file = file;
	  if(file!=null){
		  this.filename = file.getName();
		  this.content_type = ContentType.getContentType(file).type;
	  }
  }
  
  public FileRequest(String url, File file, String filename, String content_type, HttpListener listener) {
	  super(url,null,HttpRequest.POST,listener);
	  this.file         = file;
	  this.filename     = filename;
	  this.content_type = content_type;
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
      URL url = new URL(this.url_);
      httpUrlConnection = (HttpURLConnection)url.openConnection();
      httpUrlConnection.setUseCaches(false);
      httpUrlConnection.setDoOutput(true);

      httpUrlConnection.setRequestMethod("POST");
      httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
      httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
      httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary);

      DataOutputStream out = new DataOutputStream(httpUrlConnection.getOutputStream());

      out.writeBytes(this.twoHyphens + this.boundary + this.crlf);
      //out.writeBytes("Content-Type: " + this.content_type + this.crlf);
      out.writeBytes("Content-Disposition: form-data; name=\"" + this.filename + "\";filename=\"" + this.filename + "\"" + this.crlf);
      out.writeBytes(this.crlf);

      FileInputStream fis = new FileInputStream(this.file);
      byte[] inOutb = new byte[fis.available()];
      fis.read(inOutb);
      out.write(inOutb);

      out.writeBytes(this.crlf);
      out.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens + this.crlf);
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
      else
      {
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
}