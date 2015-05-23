/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.core.util;

public class LogHelper {

	public static void error(Throwable e) {
		e.printStackTrace();
	}

	public static void debug(String msg) {
		System.out.println(msg);
	}
	
	public static void info(String msg) {
		System.out.println(msg);
	}

	public static boolean isDebug() {
		return true;
	}

	public static void error(String msg) {
		System.out.println(msg);
	}
	
	

}
