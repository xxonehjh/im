/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.core.impl.client;

import java.io.IOException;

import com.hjh.core.util.CommonUtil;
import com.hjh.core.util.LogHelper;
import com.hjh.core.util.message.proxy.Message;
import com.hjh.core.util.message.proxy.MessageHandlerException;
import com.hjh.core.util.message.proxy.MessageManage;
import com.hjh.im.core.IMConfig;
import com.hjh.im.core.IMFactory;
import com.hjh.im.core.client.IMClient;
import com.hjh.im.core.client.IMClientHandler;
import com.hjh.im.core.client.IMClientMessage;
import com.hjh.im.core.net.NetClient;
import com.hjh.im.core.net.NetHandler;
import com.hjh.im.core.net.NetUser;

public class IMClentImpl implements IMClient {

	interface LoginCallback {
		public void call(boolean success, String msg);

		public boolean isTimeOut();
	}

	private String name;
	private NetClient client;
	private LoginCallback loginCallback;

	@Override
	public synchronized void join(String ip, int port,
			final IMClientHandler handler) throws IOException {
		if (null != client) {
			return;
		}
		final MessageManage messageManage = new MessageManage();
		messageManage.reg(new IMClientMessage() {

			@Override
			public void from(String who, String msg) {
				handler.from(who, msg);
			}

			@Override
			public void join(String who) {
				handler.join(who);
			}

			@Override
			public void leave(String who) {
				handler.leave(who);
			}

			@Override
			public void initLogin(boolean status, String[] users, String msg) {
				if (null != loginCallback) {
					if (status) {
						loginCallback.call(true, null);
						handler.initUsers(users);
					} else {
						loginCallback.call(false, msg);
					}

					loginCallback = null;
				}
			}

		});

		client = IMFactory.getInstance().createNetClient();
		client.start(new NetHandler() {

			@Override
			public void handle(NetUser sc, Message data) {
				if (IMConfig.MESSAGE_SHUTDOWN.equals(data.getCmd())) {
					LogHelper.info(name + ":离开");
					loginCallback = null;
					handler.shutdown();
				} else {
					try {
						messageManage.handle(data);
					} catch (MessageHandlerException e) {
						LogHelper.error(e);
					}
				}
			}

		}, ip, port);

	}

	@Override
	public synchronized void say(String to, String msg) throws IOException {
		client.getUser().getServerMessage().say(to, msg);
	}

	@Override
	public synchronized void leave() throws IOException {
		if (null != client) {
			client.close();
			client = null;
		}
	}

	@Override
	public String getId() {
		return name;
	}

	@Override
	public String login(final String loginId) {

		client.getUser().getServerMessage().join(loginId);

		client.getUser().init(loginId);

		final String[] resp = new String[1];

		loginCallback = new LoginCallback() {

			private long start = System.currentTimeMillis();

			@Override
			public void call(boolean success, String msg) {
				if (success) {
					name = loginId;
				} else {
					resp[0] = msg;
				}
			}

			@Override
			public boolean isTimeOut() {
				return System.currentTimeMillis() - start > 1000 * 60; // 1分钟
			}

		};

		while (null != loginCallback) {
			if (null != loginCallback && loginCallback.isTimeOut()) {
				loginCallback = null;
				throw new RuntimeException("登陆超时");
			}
			CommonUtil.threadSleep(100);
		}

		return resp[0];
	}

	@Override
	public boolean isLogin() {
		return null != name;
	}

}
