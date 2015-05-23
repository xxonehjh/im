/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.im.core.impl.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

import com.hjh.core.util.CommonUtil;
import com.hjh.core.util.LogHelper;
import com.hjh.core.util.SocketUtil;
import com.hjh.core.util.message.proxy.Message;
import com.hjh.im.core.IMConfig;
import com.hjh.im.core.net.NetClient;
import com.hjh.im.core.net.NetHandler;
import com.hjh.im.core.net.NetUser;

public class NetClientImpl implements NetClient {

	private NetUser user;
	private SocketChannel client;
	private NetHandler netHandler;
	private boolean exit;

	@Override
	public void start(NetHandler netHandler, String ip, int port)
			throws IOException {
		this.netHandler = netHandler;
		SocketAddress address = new InetSocketAddress(ip, port);
		client = SocketChannel.open(address);
		client.configureBlocking(false);
		user = new NetUserImpl(client);
		new ClientThread().start();
	}

	@Override
	public void close() throws IOException {
		if (null != client) {
			exit = true;
			CommonUtil.threadSleep(500);
			client.close();
			client = null;
		}
	}

	private class ClientThread extends Thread {

		public void doRun() throws IOException {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			while (true) {
				if (exit) {
					break;
				}
				Message data = SocketUtil.read(client, buffer);
				if (null != data) {
					netHandler.handle(user, data);
				}
			}
		}

		public void run() {
			try {
				doRun();
			} catch (ClosedChannelException ex) {
				LogHelper.info(user + ":Socket关闭触发异常ClosedChannelException");
			} catch (Throwable e) {
				LogHelper.error(e);
			} finally {
				netHandler.handle(user,
						Message.newInstance(IMConfig.MESSAGE_SHUTDOWN));
			}
		}

	}

	@Override
	public NetUser getUser() {
		return user;
	}

}
