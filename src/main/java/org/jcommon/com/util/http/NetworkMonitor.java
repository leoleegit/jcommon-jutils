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
package org.jcommon.com.util.http;

import java.util.Timer;
import java.util.TimerTask;

import org.jcommon.com.util.system.SystemListener;

public class NetworkMonitor implements HttpListener, SystemListener{
	private NetworkListener listener;
	private Timer timer;
	private Task  task;
	private long  period;
	private String url;
	
	public NetworkMonitor(NetworkListener listener, 
			long  period, 
			String url){
		this.listener = listener;
		this.period   = period;
		this.url      = url;
	}
	
	class Task extends TimerTask{
	
		public void run() {
			// TODO Auto-generated method stub
			org.jcommon.com.util.thread.ThreadManager.instance().execute(new HttpRequest(url,NetworkMonitor.this));
		}	
	}
	
	public String getUrl(){
		return url;
	}

	@Override
	public void onSuccessful(HttpRequest reqeust, StringBuilder sResult) {
		// TODO Auto-generated method stub
		listener.onConnect(this);
	}

	@Override
	public void onFailure(HttpRequest reqeust, StringBuilder sResult) {
		// TODO Auto-generated method stub
		listener.onError(this);
	}

	@Override
	public void onTimeout(HttpRequest reqeust) {
		// TODO Auto-generated method stub
		listener.onError(this);
	}

	@Override
	public void onException(HttpRequest reqeust, Exception e) {
		// TODO Auto-generated method stub
		listener.onError(this);
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		try{if(null!=timer){
			timer.cancel();
			timer = null;
			task  = null;
		}
		}catch(Exception e){}
	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		this.task   = new Task();
		timer = new Timer(this.getClass().getName());
		timer.schedule(task, 60, period);
	}

	@Override
	public boolean isSynchronized() {
		// TODO Auto-generated method stub
		return false;
	}
}
