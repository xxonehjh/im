/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.im.core.server;

import com.hjh.im.core.net.NetUser;

public interface IMServerHandler {

	boolean join(NetUser user, String name);

	void say(NetUser user, String to, String msg);

	void exit(NetUser user, boolean exception);

	void shutdown();

}
