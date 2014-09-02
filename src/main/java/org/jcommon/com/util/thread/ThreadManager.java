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
package org.jcommon.com.util.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import org.jcommon.com.util.system.SystemConfig;
import org.jcommon.com.util.system.SystemListener;
/**
 * 
 * @author LeoLee<leo.li@protel.com.hk>
 *
 */
public class ThreadManager implements SystemListener {
	//private static int threads = SystemConfig.instance().getInit_threads();
	private static final ThreadManager instance = new ThreadManager();
	
	public ThreadManager(){
		exec = Executors.newCachedThreadPool();
	}
	
	public static ThreadManager instance(){
		return instance;
	}
	
	private static ExecutorService exec; 
	
	public void execute(Runnable thread){
		try{
			if(exec!=null)
				exec.execute(thread);
		}catch(Exception e){	
		}
		
	}
	
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		if(exec!=null)
			exec.shutdown();
		exec = null;
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		if(exec==null)
			exec = Executors.newCachedThreadPool();
	}

	@Override
	public boolean isSynchronized() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
