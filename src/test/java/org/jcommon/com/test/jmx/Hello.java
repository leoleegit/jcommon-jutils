package org.jcommon.com.test.jmx;

public class Hello implements HelloMBean {

	private String name1;   
    public String getName1() {   
        return name1;   
    }   
    public void setName1(String name) {   
        this.name1 = name;   
    }  

    public void printHello() {   
        System.out.println("Hello World, " + name1);   
    }   
    
    public void printHello(String whoName) {   
        System.out.println("Hello , " + whoName);   
    }   

    public void HelloYour(){
    	  System.out.println("Hello HelloYour, " + name1); 
    }
}
