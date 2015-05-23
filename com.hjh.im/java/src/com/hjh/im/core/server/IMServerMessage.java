/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.im.core.server;


public interface IMServerMessage {

	void join(String name);

	void say(String to, String msg);
	
}
