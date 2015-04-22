package org.jcommon.com.util.system;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jcommon.com.util.http.HttpListener;
import org.jcommon.com.util.http.HttpRequest;
import org.jcommon.com.util.thread.ThreadManager;

public class CommandMonitor extends Thread
{
    private Process _process;
    private final int _port;
    private final String _key;

    ServerSocket _socket;
    private Logger logger = Logger.getLogger(getClass());
    private Set<String> group_server = new HashSet<String>(); 
    
    public CommandMonitor(int port,String key)
    {
        try
        {
            if(port<0)
                return;
            setDaemon(true);
	        setName("StopMonitor");
            _socket=new ServerSocket(port,1,InetAddress.getByName("127.0.0.1"));
            if (port==0)
            {
                port=_socket.getLocalPort();
                System.out.println(port);
            }
            
            if (key==null)
            {
                key=Long.toString((long)(Long.MAX_VALUE*Math.random()+this.hashCode()+System.currentTimeMillis()),36);
                System.out.println("STOP.KEY="+key);
            }
        }
        catch(Exception e)
        {
            System.err.println(e.toString());
        }
        finally
        {
            _port=port;
            _key=key;
        }
        
        if (_socket!=null)
            this.start();
        else
            System.err.println("WARN: Not listening on monitor port: "+_port);
    }
    
    public Process getProcess()
    {
        return _process;
    }
    
    public void setProcess(Process process)
    {
        _process = process;
    }
    
    @Override
    public void run()
    {
        while (true)
        {
            Socket socket=null;
            try{
                socket=_socket.accept();
                
                LineNumberReader lin=
                    new LineNumberReader(new InputStreamReader(socket.getInputStream()));
                String key=lin.readLine();
                if (!_key.equals(key))
                    continue;
                
                String cmd=lin.readLine();
                logger.info("command=" + cmd);
                if ("stop".equals(cmd))
                {
                    try {socket.close();}catch(Exception e){e.printStackTrace();}
                    try {_socket.close();}catch(Exception e){e.printStackTrace();}
                    if (_process!=null)
                        _process.destroy();
                    System.exit(0);
                }
                else if ("status".equals(cmd))
                {
                    socket.getOutputStream().write("OK\r\n".getBytes());
                    socket.getOutputStream().flush();
                }else if ("active".equalsIgnoreCase(cmd))
                {
                	if(SystemConfig.instance().isStandby()){
	                	SystemConfig.instance().setStandby(false);
	                	SystemManager.instance().start();
                	}
                }else if ("standby".equalsIgnoreCase(cmd))
                {
                	if(!SystemConfig.instance().isStandby()){
	                	SystemManager.instance().stop();
	                	SystemConfig.instance().setStandby(true);
	                	SystemManager.instance().start();
                	}  	
                }else if (cmd!=null && cmd.startsWith("SystemProperty"))//SystemProperty:key=value
                {
                	String pro = cmd.substring(15);
                	if(pro.indexOf(":")!=-1){
                		String key_ = pro.split(":")[0];
                		String value_ = pro.split(":")[1];
                		SystemManager.instance().addProperties(key_, value_);
                	}
                }
                for(String server : group_server){
                	String host = server.split(":")[0];
                	int port    = Integer.valueOf(server.split(":")[1]);
                	copyCommand(host,port,key,cmd);
                }
                
                if(SystemConfig.instance().getSystem_group()!=null){
    				Map<String,String > group_server = SystemConfig.instance().getSystem_group();
    				for(String key_ : group_server.keySet()){
    					if(key_.equals(SystemConfig.instance().getGroup_id())){
    						String url = group_server.get(key_) + "?cmd="+cmd;
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
            }
            catch(Exception e)
            {
                System.err.println(e.toString());
            }
            finally
            {
                if (socket!=null)
                {
                    try{socket.close();}catch(Exception e){}
                }
                socket=null;
            }
        }
    }
    
    public void copyCommand(String host, int port, String key, String command)
    {
        int _port = port;
        String _key = key;
        
        try
        {
            if (_port <= 0)
            {
                System.err.println("STOP.PORT system property must be specified");
            }
            if (_key == null)
            {
                _key = "";
                System.err.println("STOP.KEY system property must be specified");
                System.err.println("Using empty key");
            }

            Socket s = new Socket(InetAddress.getByName(host),_port);
            
            try
            {
                OutputStream out = s.getOutputStream();
                out.write((_key + "\r\n"+command+"\r\n").getBytes());
                out.flush();
            }
            finally
            {
                s.close();
            }
        }
        catch (ConnectException e)
        {
            logger.error(host+":"+port, e);
        }
        catch (Exception e)
        {
        	logger.error(host+":"+port, e);
        }
    }
    
    public void addGroupServer(String host, int port){
    	if(host!=null && port!=0){
    		group_server.add(host+":"+port);
    	}
    }
    
    public void removeGroupServer(String host, int port){
    	if(host!=null && port!=0){
    		group_server.remove(host+":"+port);
    	}
    }
}
