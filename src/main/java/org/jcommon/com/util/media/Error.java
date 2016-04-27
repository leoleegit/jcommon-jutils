package org.jcommon.com.util.media;

import org.jcommon.com.util.JsonObject;


public class Error extends JsonObject {
	private String message;
	private String type;
	private int code;
	private int error_subcode;
	private String error_user_title;
	private String error_user_msg;
	private String fbtrace_id;
	
	public Error(String json, boolean decode){
		super(json,decode);
	}
	
	public Error(String json){
		super(json,true);
	}
	
	public Error(int code, String message){
		super(null,true);
		this.code = code;
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getError_subcode() {
		return error_subcode;
	}
	public void setError_subcode(int error_subcode) {
		this.error_subcode = error_subcode;
	}
	public String getError_user_title() {
		return error_user_title;
	}
	public void setError_user_title(String error_user_title) {
		this.error_user_title = error_user_title;
	}
	public String getError_user_msg() {
		return error_user_msg;
	}
	public void setError_user_msg(String error_user_msg) {
		this.error_user_msg = error_user_msg;
	}
	public String getFbtrace_id() {
		return fbtrace_id;
	}
	public void setFbtrace_id(String fbtrace_id) {
		this.fbtrace_id = fbtrace_id;
	}

	@Override
	public void setListObject(Object args, Object args2) {
		// TODO Auto-generated method stub
		
	}
}

