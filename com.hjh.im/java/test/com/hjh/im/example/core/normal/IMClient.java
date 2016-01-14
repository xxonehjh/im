/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.example.core.normal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class IMClient {

	public static void main(String argv[]) throws UnknownHostException,
			IOException {
		int PORT = 8888; // 侦听端口
		// 建立连接
		Socket socket = new Socket("127.0.0.1", PORT);

		// 输入数据的读取
		BufferedReader netIn = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		// 写入数据
		PrintWriter netOut = new PrintWriter(socket.getOutputStream());

		netOut.println("Hello:我是IM");

		netOut.close();
		netIn.close();

		socket.close();
	}

}
