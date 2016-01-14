/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.core.server;

import java.io.IOException;

public interface IMServer {

	void start(int port,IMServerHandler handler) throws IOException;
	
	void close() throws IOException;

}
