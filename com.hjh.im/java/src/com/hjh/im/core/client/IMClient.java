/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.im.core.client;

import java.io.IOException;

public interface IMClient {

	String getId();

	void join(String ip, int port, IMClientHandler listener) throws IOException;

	String login(String loginId);
	
	boolean isLogin();

	void say(String to, String msg) throws IOException;

	void leave() throws IOException;

}
