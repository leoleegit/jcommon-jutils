package org.jcommon.com.util.health;

import org.jcommon.com.util.DateUtils;
import org.jcommon.com.util.JsonObject;
import org.jcommon.com.util.system.SystemConfig;

public class Status extends JsonObject {
	private String name;
	private String status = StandbyMode;
	private String update_time;
	
	public final static String ActiveMode="ACTIVE";
	public final static String StandbyMode="STANDBY";
	
	public Status(String name){
		this.setName(name);
	}
	
	public Status(String name, String status){
		this.setName(name);
		this.setStatus(status);
	}
	
	public String toJson(){
		if(SystemConfig.instance().isStandby())
			return "\'" + name +"\':\'"+(StandbyMode)+"\'";
		else
			return "\'" + name +"\':\'"+(!"OK".equals(status)?"DOWN":"OK")+"\'";
	}
	
	public boolean isOk(){
		return "OK".equals(status);
	}
	
	public String toString(){
		return super.toJson();
	}

	public void setStatus(String status) {
		this.status = status;
		setUpdate_time(DateUtils.getNowSinceYear());
	}

	public String getStatus() {
		return status;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getUpdate_time() {
		return update_time;
	}
	
	public  boolean isEncode() {
		return false;
    }
}
