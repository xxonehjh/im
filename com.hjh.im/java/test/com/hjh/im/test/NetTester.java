/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.test;

import java.io.IOException;

import com.hjh.im.core.IMConfig;
import com.hjh.im.core.IMFactory;
import com.hjh.im.core.client.IMClient;
import com.hjh.im.core.client.IMClientHandlerAdapter;
import com.hjh.im.core.server.IMServer;
import com.hjh.im.core.server.SimpleIMServerHandler;

public class NetTester {

	public static void main(String argv[]) throws InterruptedException,
			IOException {

		IMServer server = IMFactory.getInstance().createServer();
		server.start(IMConfig.PORT, new SimpleIMServerHandler());

		IMClient client_xiaoming = IMFactory.getInstance().createClient();
		client_xiaoming.join(IMConfig.IP, IMConfig.PORT,
				new IMClientHandlerAdapter() {

					@Override
					public void from(String who, String msg) {
						System.out.println("小明听到[" + who + "]说:" + msg);
					}

				});

		client_xiaoming.login("小明");

		IMClient client_xiaoli = IMFactory.getInstance().createClient();
		client_xiaoli.join(IMConfig.IP, IMConfig.PORT,
				new IMClientHandlerAdapter() {

					@Override
					public void from(String who, String msg) {
						System.out.println("小李听到[" + who + "]说:" + msg);
					}

				});

		client_xiaoli.login("小李");

		Thread.sleep(1000);

		client_xiaoming.say("小李", "你好，我叫小明");
		client_xiaoli.say("小明", "你好，我叫小李");

		Thread.sleep(1000);

		client_xiaoli.leave();
		client_xiaoming.leave();

		Thread.sleep(1000);

		server.close();
	}

}
