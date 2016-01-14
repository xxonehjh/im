/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.test;

import java.io.IOException;

import com.hjh.im.core.IMConfig;
import com.hjh.im.core.IMFactory;
import com.hjh.im.core.server.IMServer;
import com.hjh.im.core.server.SimpleIMServerHandler;

public class SimpleServer {

	public static void main(String argv[]) throws InterruptedException,
			IOException {

		IMServer server = IMFactory.getInstance().createServer();
		server.start(IMConfig.PORT, new SimpleIMServerHandler());

		System.out.println("print \"exit\" to leave!");

		byte[] cache = new byte[4];
		int len = 0;
		while (true) {
			Thread.sleep(1000);
			len = System.in.read(cache);
			if (len > 0) {
				if ("exit".equals(new String(cache, 0, len))) {
					break;
				}
			}
		}

		server.close();
	}

}
