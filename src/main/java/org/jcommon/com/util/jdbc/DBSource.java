// ========================================================================
// Copyright 2012 leolee<workspaceleo@gmail.com>
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//     http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ========================================================================
package org.jcommon.com.util.jdbc;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;


import snaq.db.ConnectionPoolManager;

public class DBSource {
    private static Logger logger = Logger.getLogger(DBSource.class);   
	
    private static ConnectionPoolManager poolManager;	
	private static Connection connection = null;
	
    private static String CONFIG_FILENAME = "/";
    private static String CONFIG_DIRECTORY = System.getProperty("user.dir") + File.separator + "conf";
    
    public static void initConnectionPoolManager(){
    	if(null == poolManager)
			try {
				File url = new File(CONFIG_DIRECTORY, CONFIG_FILENAME);
				poolManager = ConnectionPoolManager.getInstance(url);
				logger.info("init poolManager end...\n"+url.getPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("getConnectionPoolManager:"+e);
			}
	}
    
    private static ConnectionPoolManager getConnectionPoolManager(){
		if(null == poolManager)
			initConnectionPoolManager();
		return poolManager;
	}
	
	public static Connection getConnection(String poolName) throws SQLException{
		if(null==getConnectionPoolManager())
			return null;
		connection = getConnectionPoolManager().getConnection(poolName, 5000);
		return connection;
	}
	
	public static void release(String poolName){
		if(null == getConnectionPoolManager()){
			return ;
		}
		if(getConnectionPoolManager().getPool(poolName)!=null)
			getConnectionPoolManager().getPool(poolName).release();
	};    
	
	public static void init(String CONFIG_FILENAME, String CONFIG_DIRECTORY){
		DBSource.CONFIG_FILENAME  = CONFIG_FILENAME;
		DBSource.CONFIG_DIRECTORY = CONFIG_DIRECTORY;
	}
}
