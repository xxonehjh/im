/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.hjh.core.util.message.proxy.Message;

public class SocketUtil {

	private static boolean debug = false;

	public static void send(SocketChannel client, Message msg)
			throws IOException {
		if (debug && LogHelper.isDebug()) {
			LogHelper.debug("发送数据:" + msg.toString());
		}
		client.write(ByteBuffer.wrap(msg.toByteArray()));
	}

	public static Message read(SocketChannel sc, ByteBuffer buffer)
			throws IOException {
		ByteArrayOutputStream out = null;
		int readlen;
		while (true) {
			buffer.clear();
			readlen = sc.read(buffer);

			if (readlen > 0) {
				if (null == out) {
					out = new ByteArrayOutputStream();
				}
				out.write(buffer.array(), 0, readlen);
			} else {

				if (readlen < 0) {
					return Message.newInstance(Message.MESSAGE_EXIT);
				}

				break;
			}
		}

		if (null != out) {
			out.close();
			byte[] data = out.toByteArray();
			if (data.length > 0) {
				Message msg = Message.fromArray(data);
				if (debug && LogHelper.isDebug()) {
					LogHelper.debug("接收数据:"
							+ sc.socket().getInetAddress().getHostAddress()
							+ ":" + sc.socket().getPort() + ":"
							+ msg.toString());
				}
				return msg;
			}
		}

		return null;
	}

}
