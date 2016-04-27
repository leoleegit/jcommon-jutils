package org.jcommon.com.util.media;

import java.io.File;

import org.jcommon.com.util.JsonObject;

public class Media extends JsonObject{
	private String media_id;
	private File media;
	private String url;
	private String name;
	private String content_type;
	private String media_name;
	private String update_time;
	  
	public Media(String json, boolean decode) {
		super(json,decode);
		// TODO Auto-generated constructor stub
	}
	
	public Media(String json) {
		super(json,true);
		// TODO Auto-generated constructor stub
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public File getMedia() {
		return media;
	}

	public void setMedia(File media) {
		this.media = media;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent_type() {
		return content_type;
	}

	public void setContent_type(String content_type) {
		this.content_type = content_type;
	}

	public String getMedia_name() {
		return media_name;
	}

	public void setMedia_name(String media_name) {
		this.media_name = media_name;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	@Override
	public void setListObject(Object args, Object args2) {
		// TODO Auto-generated method stub
		
	}
}

