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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class ConnectionManager extends DBSource { 
	 private Logger logger = Logger.getLogger(this.getClass());   
	 
	 private String dbname = null;
	 
	 public ConnectionManager(String dbname){
		 this.dbname = dbname;
	 }
	 
	 public ConnectionManager(String CONFIG_FILENAME, String CONFIG_DIRECTORY, String dbname){
		 this.dbname = dbname;
		 init(CONFIG_FILENAME,CONFIG_DIRECTORY);
	 }
	 
	 public  Connection getConnection() throws Exception{
		 return getConnection(dbname);
	 }
	
	 public void closeConnection(Connection connection) throws Exception{
		 if(connection!=null)
			 connection.close();
	 }
	 
	 public ResultSet getResultSet(String sql, Connection connection){
		 PreparedStatement ps;
		 try {
			ps = connection.prepareStatement(sql);
			return ps.executeQuery();
		 } catch (SQLException e) {
			// TODO Auto-generated catch block
			 logger.error("", e);
		 }
		 return null;
	 }
	 
	 //fire the event when application is going down 
	 public void release(){
		 release(dbname);
	 }
}
