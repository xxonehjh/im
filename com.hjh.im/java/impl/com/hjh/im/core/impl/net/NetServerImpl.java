/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.core.impl.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hjh.core.util.CommonUtil;
import com.hjh.core.util.LogHelper;
import com.hjh.core.util.SocketUtil;
import com.hjh.core.util.message.proxy.Message;
import com.hjh.im.core.IMConfig;
import com.hjh.im.core.net.NetHandler;
import com.hjh.im.core.net.NetServer;
import com.hjh.im.core.net.NetUser;

public class NetServerImpl implements NetServer {

	private ExecutorService executorService;
	private NetHandler netHandler;
	private Selector selector;
	private ServerSocketChannel serverSocket;
	private String logName;
	private String name;
	private int port;
	private boolean exit;

	private Map<SocketChannel, NetUser> users;

	@Override
	public synchronized void start(NetHandler netHandler, String name, int port)
			throws IOException {

		if (serverSocket != null) {
			throw new IOException("服务器已经启动");
		}

		users = new HashMap<SocketChannel, NetUser>();

		this.netHandler = netHandler;
		this.name = name;
		this.port = port;
		this.logName = String.format("服务线程[%s:%d]:", name, port);

		regPort();
		new ServerThread().start();
	}

	@Override
	public synchronized void close() throws IOException {
		if (null == serverSocket) {
			return;
		}

		exit = true;

		CommonUtil.threadSleep(500);

		executorService.shutdown();
		executorService = null;

		selector.close();
		selector = null;

		CommonUtil.threadSleep(500);

		serverSocket.close();
		serverSocket = null;
	}

	private void regPort() throws IOException {
		if (null == selector) {
			selector = Selector.open();
		}

		if (serverSocket != null) {
			throw new IOException(logName + "端口已经注册过!");
		}

		exit = false;
		executorService = Executors.newFixedThreadPool(5);
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		ServerSocket ss = ssc.socket();
		InetSocketAddress address = new InetSocketAddress(port);
		ss.bind(address);
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		LogHelper.info(logName + "端口注册完毕!");

		serverSocket = ssc;
	}

	public NetUser getUser(SocketChannel sc, boolean create) {
		NetUser user = users.get(sc);
		if (null == user && create) {
			user = new NetUserImpl(sc);
			users.put(sc, user);
		}
		return user;
	}

	public void delUser(SocketChannel sc) {
		users.remove(sc);
	}

	private class ServerThread extends Thread {

		public ServerThread() {
			this.setName("Net Server:" + name);
		}

		public void doRun() throws IOException {

			while (true) {

				if (exit) {
					break;
				}

				if (selector.select() > 0 && !exit) {
					Set<SelectionKey> selectionKeys = selector.selectedKeys();
					Iterator<SelectionKey> iter = selectionKeys.iterator();

					while (iter.hasNext()) {
						if (exit) {
							break;
						}
						SelectionKey key = iter.next();
						if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
							ServerSocketChannel subssc = (ServerSocketChannel) key
									.channel();

							SocketChannel sc = subssc.accept();
							sc.configureBlocking(false);
							sc.register(selector, SelectionKey.OP_READ);

							iter.remove();

							getUser(sc, true);

							LogHelper.debug(logName
									+ "有新连接:"
									+ sc.socket().getInetAddress()
											.getHostAddress() + ":"
									+ sc.socket().getPort());

						} else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {

							SocketChannel sc = (SocketChannel) key.channel();
							if (sc.isOpen()) {
								executorService
										.submit(new SocketChannelReadHandler(sc));
							}
							iter.remove();
						}
					}

					CommonUtil.threadSleep(100);
				}
			}
		}

		public void run() {
			try {
				doRun();
			} catch (ClosedSelectorException ex) {
				LogHelper.info(logName + ":关闭ClosedSelectorException");
			} catch (ClosedChannelException ex) {
				LogHelper.info(logName + ":关闭ClosedChannelException");
			} catch (Throwable e) {
				LogHelper.error(e);
			} finally {
				netHandler.handle(null,
						Message.newInstance(IMConfig.MESSAGE_SHUTDOWN));
			}

		}

		class SocketChannelReadHandler implements Runnable {

			private SocketChannel sc;

			public SocketChannelReadHandler(SocketChannel sc) {
				this.sc = sc;
			}

			@Override
			public void run() {
				if (exit || !sc.isOpen()) {
					return;
				}
				ByteBuffer echoBuffer = ByteBuffer.allocate(1024);
				Message data = null;
				try {
					data = SocketUtil.read(sc, echoBuffer);
					if (null != data) {
						boolean exit = Message.MESSAGE_EXIT.equals(data
								.getCmd());
						NetUser user = getUser(sc, exit ? false : true);
						if (null != user) {
							netHandler.handle(user, data);
						}
						if (exit) {
							sc.close();
						}
					}
				} catch (IOException e) {
					LogHelper.error(e.toString());
					try {
						NetUser user = getUser(sc, false);
						try {
							if (null != user) {
								Message msg = Message
										.newInstance(
												Message.MESSAGE_EXIT,
												IMConfig.MESSAGE_CLIENT_EXIT_PARAM_EXCEPTION,
												"true");
								netHandler.handle(user, msg);
							}
						} finally {
							delUser(sc);
							sc.close();
						}
					} catch (IOException ex) {
						LogHelper.error(ex.toString());
					}
				} catch (Throwable e) {
					LogHelper.error(e);
				}

			}
		}

	}

}
