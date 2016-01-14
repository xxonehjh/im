/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.core.impl.server;

import java.io.IOException;

import com.hjh.core.util.LogHelper;
import com.hjh.core.util.message.proxy.Message;
import com.hjh.core.util.message.proxy.MessageHandlerException;
import com.hjh.core.util.message.proxy.MessageManageEx;
import com.hjh.im.core.IMConfig;
import com.hjh.im.core.IMFactory;
import com.hjh.im.core.net.NetHandler;
import com.hjh.im.core.net.NetServer;
import com.hjh.im.core.net.NetUser;
import com.hjh.im.core.server.IMServer;
import com.hjh.im.core.server.IMServerHandler;
import com.hjh.im.core.server.IMServerMessage;

public class IMServerImpl implements IMServer {

	private NetServer server;

	@Override
	public synchronized void start(int port, final IMServerHandler handler)
			throws IOException {

		if (null != server) {
			return;
		}

		final MessageManageEx<NetUser> messageManageEx = new MessageManageEx<NetUser>();

		messageManageEx.reg(new IMServerMessage() {

			@Override
			public void join(String name) {
				handler.join(messageManageEx.get(), name);
			}

			@Override
			public void say(String to, String msg) {
				if (messageManageEx.get().isLogin()) {
					handler.say(messageManageEx.get(), to, msg);
				} else {
					LogHelper.info("未登陆 :" + messageManageEx.get());
				}

			}

		});

		server = IMFactory.getInstance().createNetServer();
		server.start(new NetHandler() {
			@Override
			public void handle(final NetUser sc, Message data) {
				if (Message.MESSAGE_EXIT.equals(data.getCmd())) {
					if (sc.isLogin()) {
						handler.exit(
								sc,
								null != data
										.get(IMConfig.MESSAGE_CLIENT_EXIT_PARAM_EXCEPTION));
					}
				} else if (IMConfig.MESSAGE_SHUTDOWN.equals(data.getCmd())) {
					handler.shutdown();
				} else {
					messageManageEx.set(sc);
					try {
						messageManageEx.handle(data);
					} catch (MessageHandlerException e) {
						LogHelper.error(e);
					} finally {
						messageManageEx.set(null);
					}
				}
			}

		}, "服务器", port);

	}

	@Override
	public synchronized void close() throws IOException {
		if (null != server) {
			server.close();
			server = null;
		}
	}

}
