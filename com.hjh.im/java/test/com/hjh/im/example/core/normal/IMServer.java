/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.example.core.normal;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class IMServer {

	public static void main(String argv[]) throws IOException {
		int PORT = 8888; // 侦听端口
		// 创建ServerSocket
		ServerSocket serverSocket = new ServerSocket(PORT);

		try {
			// 开始循环
			while (true) {
				// 等待连接
				Socket socket = serverSocket.accept();
				// 处理链接的线程类
				ServerThread st = new ServerThread(socket);
				// 启动线程处理
				new Thread(st).start();
			}
		} finally {
			serverSocket.close();
		}
	}

	public static class ServerThread implements Runnable {

		private Socket socket;

		public ServerThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				InputStream in = socket.getInputStream();
				byte[] cache = new byte[1024];
				while (true) {
					int len = in.read(cache);
					if (len > 0) {
						System.out.println(new String(cache, 0, len));
					} else {
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
