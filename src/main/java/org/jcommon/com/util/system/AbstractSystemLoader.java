package org.jcommon.com.util.system;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public abstract class AbstractSystemLoader implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		SystemManager.instance().contextInitialized(sce);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		SystemManager.instance().contextDestroyed(sce);
	}

}
