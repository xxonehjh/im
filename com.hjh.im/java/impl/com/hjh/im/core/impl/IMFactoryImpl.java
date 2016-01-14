/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.core.impl;

import com.hjh.im.core.IMFactory;
import com.hjh.im.core.client.IMClient;
import com.hjh.im.core.impl.client.IMClentImpl;
import com.hjh.im.core.impl.net.NetClientImpl;
import com.hjh.im.core.impl.net.NetServerImpl;
import com.hjh.im.core.impl.server.IMServerImpl;
import com.hjh.im.core.net.NetClient;
import com.hjh.im.core.net.NetServer;
import com.hjh.im.core.server.IMServer;

public class IMFactoryImpl extends IMFactory {

	public IMServer createServer() {
		return new IMServerImpl();
	}

	public IMClient createClient() {
		return new IMClentImpl();
	}

	public NetServer createNetServer() {
		return new NetServerImpl();
	}

	public NetClient createNetClient() {
		return new NetClientImpl();
	}

}
