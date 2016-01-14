/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.core.impl.net;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import com.hjh.core.util.LogHelper;
import com.hjh.core.util.SocketUtil;
import com.hjh.core.util.message.proxy.Message;
import com.hjh.core.util.message.proxy.MessageManage;
import com.hjh.core.util.message.proxy.MessageSender;
import com.hjh.im.core.client.IMClientMessage;
import com.hjh.im.core.net.NetUser;
import com.hjh.im.core.server.IMServerMessage;

public class NetUserImpl implements NetUser, MessageSender {

	private String address;

	private SocketChannel channel;
	private String name;
	private long loginTime;

	public NetUserImpl(SocketChannel channel) {
		this.channel = channel;
		this.loginTime = System.currentTimeMillis();

		address = channel.socket().getInetAddress().getHostAddress() + ":"
				+ channel.socket().getPort();
	}

	public String toString() {
		return address + "[" + (name == null ? "" : name) + "]";
	}

	@Override
	public boolean isLogin() {
		return null != name;
	}

	@Override
	public String getId() {
		return name;
	}

	@Override
	public void init(String name) {
		this.name = name;
	}

	@Override
	public long getLoginTime() {
		return loginTime;
	}

	@Override
	public void send(Message msg) {
		try {
			SocketUtil.send(channel, msg);
		} catch (IOException e) {
			LogHelper.error(e);
		}
	}

	@Override
	public IMServerMessage getServerMessage() {
		return MessageManage.createMessageCaller(this, IMServerMessage.class);
	}

	@Override
	public IMClientMessage getClientMessage() {
		return MessageManage.createMessageCaller(this, IMClientMessage.class);
	}

}
