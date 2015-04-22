package org.jcommon.com.test;

import java.util.ArrayList;
import java.util.List;

import org.jcommon.com.util.health.Status;
import org.jcommon.com.util.health.StatusManager;

public class StatusManagerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new StatusManager().getStatus2Html());
		

		
	    List<Status> status = new ArrayList<Status>();
		status.add(new Status("LCAP"));
		status.add(new Status("LCWS"));
		status.add(new Status("AAWS"));
		status.add(new Status("CSMG"));
		status.add(new Status("SMMG"));
			
		StringBuilder sb = new StringBuilder(org.jcommon.com.util.JsonObject.list2Json(status));
			 
			System.out.println(sb);
		    if (sb.charAt(0) == '[')
		        sb.deleteCharAt(0);
		    sb.insert(0, '{');
		    if (sb.lastIndexOf("]") == sb.length() - 1)
		        sb.deleteCharAt(sb.length() - 1);
			sb.append("}");
			System.out.println(sb);
	}

}
