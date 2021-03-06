package org.jcommon.com.util.health;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jcommon.com.util.jmx.Monitor;
import org.jcommon.com.util.system.SystemConfig;

public class StatusManager extends Monitor{
	private Logger logger = Logger.getLogger(this.getClass());
	
	private List<Status> status = new ArrayList<Status>();
	private static StatusManager instance;
	
	public StatusManager() {
		super("Status Manager");
		// TODO Auto-generated constructor stub
		StatusManager.instance = this;
	}

	public static StatusManager instance(){
		return instance;
	}
	
	public void addStatus(Status status){
		if(status==null || status.getName()==null)
			return;
		Status s = getStatusByName(status!=null?status.getName():null);
		if(s!=null)
			removeStatus(s);
		this.status.add(status);
		super.addProperties(status.getName(), status.toString());
	}
	
	public boolean removeStatus(Status status){
		if(status==null || status.getName()==null)
			return false;
		if(this.status.contains(status)){
			super.removeProperties(status.getName());
			return this.status.remove(status);
		}
		return false;
	}
	
	public Status getStatusByName(String name){
		for(Status status : this.status){
			if(name!=null && name.equals(status.getName()))
				return status;
		}
		return null;
	}
	
	public String getStatus(){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for(Status s : status){
			sb.append(s.toString()).append(",");
		}
		if (sb.lastIndexOf(",") == sb.length() - 1)
		      sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		logger.info(sb);
		return sb.toString();
	}
	
	public String getStatus2Html(){
		StringBuilder sb = new StringBuilder();
		sb.append("{</br>");
		sb.append(getModeStauts().toString()).append(",</br>");
		for(Status s : status){
			sb.append(s.toString()).append(",</br>");
		}
		if (sb.lastIndexOf(",</br>") == sb.length() - 6)
		      sb.delete(sb.length() - 6, sb.length());
		sb.append("</br>}");
		logger.info(sb);
		return sb.toString();
	}
	
	public static boolean isOk(List<Status> status){
		if(status!=null){
			for(Status s : status){
				if(!s.isOk())
					return false;
			}
		}
		return true;
	}
	
	public Status getModeStauts(){
		Status status = new Status("MODE"){
			public String toJson(){
				return "\'" + this.getName() +"\':\'"+(this.getStatus())+"\'";
			}
		};
		status.setStatus(SystemConfig.instance().isStandby()?Status.StandbyMode:Status.ActiveMode);
		return status;	
	}
	
	public List<Status> getStauts(){
		return status;	
	}
}
