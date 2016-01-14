/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.core.net;

import java.io.IOException;


public interface NetServer {

	public void start(NetHandler netHandler, String name, int port)
			throws IOException;

	public void close() throws IOException;

}
