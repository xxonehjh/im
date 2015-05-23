/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.core.util.message.proxy;

import java.util.Arrays;

import com.hjh.core.util.LogHelper;

public class Tester {

	public interface TesterHandler {
		public void hello(@Param("to") String to, float age, String[] arr);
	}

	public static void main(String argv[]) {

		final MessageManage messageManage = new MessageManage();

		messageManage.reg(new TesterHandler() {

			@Override
			public void hello(String to, float age, String[] arr) {
				System.out.println("收到:" + to + ":age:" + age + ":arr:"
						+ Arrays.toString(arr));
			}

		});

		TesterHandler caller = MessageManage.createMessageCaller(
				new MessageSender() {

					@Override
					public void send(Message msg) {
						LogHelper.debug("发送:" + msg.toString());
						try {
							messageManage.handle(msg);
						} catch (MessageHandlerException e) {
							e.printStackTrace();
						}
					}

				}, TesterHandler.class);

		caller.hello("我是测试", 15, new String[] { "123", "1234" });
	}

}
