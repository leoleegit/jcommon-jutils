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
package org.jcommon.com.util.system;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;


public class SystemClassLoader extends URLClassLoader{
	 private Logger logger = Logger.getLogger(this.getClass());
	 public SystemClassLoader() {
	        super(new URL[] {}, findParentClassLoader());
	 }
	 
	 @SuppressWarnings("deprecation")
	public void addDirectory(File directory) throws MalformedURLException{
		 File[] jars = null;
		 if(directory.isDirectory()){
			 // Add lib directory to classpath.
	         File libDir = new File(directory, "lib");
	         jars = libDir.listFiles(new FilenameFilter() {
	             public boolean accept(File dir, String name) {
	                 return name.endsWith(".jar") || name.endsWith(".zip");
	             }
	         });
		 }else if(directory.getName().endsWith(".jar")){
			 jars = new File[]{directory};
		 }
         
         if (jars != null) {
             for (int i = 0; i < jars.length; i++) {
                 if (jars[i] != null && jars[i].isFile()) {
                     addURL(jars[i].toURL());  
                     logger.info("path:"+jars[i].toURL());
                 }
             }
         }
	 }
	 
	 public void addURLFile(URL file) {
	        addURL(file);
	 }
    /**
     * Locates the best parent class loader based on context.
     *
     * @return the best parent classloader to use.
     */
    private static ClassLoader findParentClassLoader() {
        ClassLoader parent = SystemClassLoader.class.getClassLoader();
        return parent;
    }
}
