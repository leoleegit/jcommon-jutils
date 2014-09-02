package org.jcommon.com.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.jcommon.com.util.system.SystemListener;
import org.jcommon.com.util.system.SystemManager;

public class Command implements SystemListener {
    private String help;
    
    public  Command(String help){
    	this.help = help;
    }
	
	@Override
	public void shutdown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startup() {
		// TODO Auto-generated method stub
		LineReader lineReader = null;
		try {
			lineReader = new LineReader();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        boolean running = true;
        System.out.println(help);
        while (running) {
            try {
                final String command = lineReader.readLine();
                handleCommand(command);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
	}

	public void handleCommand(String command) {
		// TODO Auto-generated method stub
	
		if("q".equals(command)){
			SystemManager.instance().contextDestroyed(null);
			System.exit(0);
		}
	}

	@Override
	public boolean isSynchronized() {
		// TODO Auto-generated method stub
		return false;
	}

}

class LineReader {
	private BufferedReader in;

    public LineReader() throws UnsupportedEncodingException {
        in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
    }


    public String readLine() throws Exception {
        return in.readLine();
    }
}
