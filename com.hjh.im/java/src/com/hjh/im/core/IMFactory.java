/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.core;

import com.hjh.im.core.client.IMClient;
import com.hjh.im.core.impl.IMFactoryImpl;
import com.hjh.im.core.net.NetClient;
import com.hjh.im.core.net.NetServer;
import com.hjh.im.core.server.IMServer;

public abstract class IMFactory {

	private static IMFactory instance = new IMFactoryImpl();

	public static IMFactory getInstance() {
		return instance;
	}

	public abstract IMServer createServer();

	public abstract IMClient createClient();

	public abstract NetClient createNetClient();

	public abstract NetServer createNetServer();

}
