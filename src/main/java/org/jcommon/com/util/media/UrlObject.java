package org.jcommon.com.util.media;

import org.jcommon.com.util.JsonObject;


public class UrlObject extends JsonObject{
	
	private String url;
	public UrlObject(String url, boolean decode){
		super(url,decode);
		if(this.url==null)
			this.url = url;
	}
	
	public UrlObject(String url){
		super(url,true);
		if(this.url==null)
			this.url = url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}

	@Override
	public void setListObject(Object args, Object args2) {
		// TODO Auto-generated method stub
		
	}
} 

