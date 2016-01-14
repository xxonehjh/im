/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.core.util;

public class CommonUtil {

	public static void threadSleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			LogHelper.error(e);
		}
	}
}
