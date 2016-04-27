package org.jcommon.com.util.media;

import java.io.File;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;
import org.jcommon.com.util.MD5;
import org.jcommon.com.util.http.ContentType;

public class DefaultMediaFactory extends MediaFactory{
	private Logger logger = Logger.getLogger(getClass());
	private static final String MEDIA_STORE="media.store";
	private static final String MEDIA_URL  ="media.url";
	private static final String DEFAULT_STORE = System.getProperty("user.dir")+File.separator+"media";
	
	@Override
	public File createEmptyFile(Media media) {
		// TODO Auto-generated method stub
		String store = System.getProperty(MEDIA_STORE, DEFAULT_STORE);
		checkDir(store);
	    String id    = media.getMedia_id();
	    File file    = new File(store,id);
		return file;
	}
	
	private void checkDir(String dir){
		File file = new File(dir);
		if(!file.exists())
			file.mkdirs();
	}

	/**
	 * return url : MEDIA_URL + / + MD5.getMD5(content_type.getBytes())+ / + filename
	 */
	@Override
	public Media createUrl(Media media) {
		// TODO Auto-generated method stub
		String url = System.getProperty(MEDIA_URL, "/");
		File file  = media.getMedia();
		String content_type = media.getContent_type();
		if(file!=null && file.exists()){
			String file_name = media.getMedia_name()==null?file.getName():media.getMedia_name();
			try {
				if(content_type==null){
					content_type = ContentType.html.type;
					logger.info("content_type is null, will use default content_type:"+content_type);
				}else{
					logger.info(content_type);
				}
				content_type     = MD5.getMD5(content_type.getBytes());
				File type_dir    = new File(file.getParent(),content_type);
				if(!type_dir.exists())
					type_dir.mkdirs();
				type_dir    = new File(type_dir,media.getMedia_id());
				if(!type_dir.exists())
					type_dir.mkdirs();
				
				File newfile     = new File(type_dir.getAbsolutePath(),file_name);
				boolean rename   = file.renameTo(newfile);
				if(rename)
					logger.info("File Renamed");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				logger.error("", e);
			}
			url = url + content_type + "/" + media.getMedia_id() + "/" +file_name;
			media.setUrl(url);
		}
		
		return media;
	}

	@Override
	public Media getMediaFromUrl(String url) {
		// TODO Auto-generated method stub
		Media media = new Media(null,true);
		if(url!=null){
			String store = System.getProperty(MEDIA_STORE, DEFAULT_STORE);
			String url_start =  "/";
			url              = url.substring(url.indexOf(url_start)+url_start.length());
			String[] urls    = url.split("/");
			
			String content_type = urls[0];
			String media_id     = urls[1];
			String file_name    = urls[2];
			
			File type_dir    = new File(store,content_type);
			ContentType type = ContentType.getContentByType(content_type, true);
			logger.info(String.format("store:%s;file_name:%s;media_id:%s;content_type:%s", store,file_name,media_id,type.type));
			
			if(!type_dir.exists())
				return media;
			
			type_dir    = new File(type_dir,media_id);
			if(!type_dir.exists())
				return media;
			
			File file        = new File(type_dir.getAbsolutePath(),file_name);
			media.setMedia(file);
			media.setMedia_name(file_name.indexOf(".")!=-1?file_name:file_name+type.name);
			media.setContent_type(type.type);
		}
		
		return media;
	}
}

