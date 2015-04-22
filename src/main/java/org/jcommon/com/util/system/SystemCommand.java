package org.jcommon.com.util.system;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.*;
import org.jcommon.com.util.thread.ThreadManager;


public class SystemCommand  extends HttpServlet {

	private static Logger logger = Logger.getLogger(SystemCommand.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(SystemConfig.instance().isAuth()){
			if(!authUser(request,response)){
				return;
			}
		}
		
		String cmd_ = request.getParameter("cmd");
		responseMessage(response,"handling,"+cmd_);
		if(cmd_!=null){
			if ("active".equalsIgnoreCase(cmd_))
            {
            	if(SystemConfig.instance().isStandby()){
                	SystemConfig.instance().setStandby(false);
                	SystemManager.instance().start();
            	}
            }else if ("standby".equalsIgnoreCase(cmd_))
            {
            	if(!SystemConfig.instance().isStandby()){
                	SystemManager.instance().stop();
                	SystemConfig.instance().setStandby(true);
                	SystemManager.instance().start();
            	}  	
            }else{
				responseMessage(response,"false,parameter exception");
				return;
			}
			if(SystemConfig.instance().getSystem_group()!=null){
				Map<String,String > group_server = SystemConfig.instance().getSystem_group();
				for(String key : group_server.keySet()){
					if(key.equals(SystemConfig.instance().getGroup_id())){
						String url = group_server.get(key) + "?cmd="+cmd_;
						HttpRequest notifyRequest = new HttpRequest(url,new HttpListener(){

							@Override
							public void onSuccessful(HttpRequest reqeust,
									StringBuilder sResult) {
								// TODO Auto-generated method stub
								logger.info(sResult);
							}

							@Override
							public void onFailure(HttpRequest reqeust,
									StringBuilder sResult) {
								// TODO Auto-generated method stub
								logger.error(sResult);
							}

							@Override
							public void onTimeout(HttpRequest reqeust) {
								// TODO Auto-generated method stub
								logger.error("timeout");
							}

							@Override
							public void onException(HttpRequest reqeust,
									Exception e) {
								// TODO Auto-generated method stub
								logger.error("", e);
							}
							
						});
						Map<String,String> system_user = SystemConfig.instance().getSystem_user();
						if(system_user!=null && 
								system_user.size()!=0 &&
								SystemConfig.instance().isAuth()){
							String user = (String) system_user.keySet().toArray()[0];
							String pass = system_user.get(user);
							logger.info(String.format("user:%s;pass:%s", user,pass));
							notifyRequest.setAuthorization(user, pass);
						}
						ThreadManager.instance().execute(notifyRequest);
					}
				}
			}
			responseMessage(response,"true,"+cmd_);
		}else{
			responseMessage(response,"false,parameter exception");
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}
	
	private boolean authUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String authorization=request.getHeader("authorization");
		if(authorization==null||authorization.equals("")){
            response.setStatus(401);
            response.setHeader("WWW-authenticate","Basic realm=\"please input your username and password\"");
            responseMessage(response,"false,403 Forbidden");
            return false;
        }
		String user_pass= new String(new sun.misc.BASE64Decoder().decodeBuffer(authorization.split(" ")[1]));
        if(user_pass.split(":").length<2){
            response.setStatus(401);
            response.setHeader("WWW-authenticate","Basic realm=\"please input your username and password\"");
            responseMessage(response,"false,403 Forbidden");
            return false;
        }
        String user=user_pass.split(":")[0];
        String pass=user_pass.split(":")[1];
        Map<String, String> users = SystemConfig.instance().getSystem_user();
        if(users!=null && pass.equals(users.get(user))){
            return true;
        }else{
            response.setStatus(401);
            response.setHeader("WWW-authenticate","Basic realm=\"Username and password do not match.\"");
            responseMessage(response,"false,Username and password do not match.");
            return false;

        }
	}
	
	private void responseMessage(HttpServletResponse response, String msg){
		try {
			response.getWriter().println(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("", e);
		}
	}
}
