/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.im.core.server;

import java.io.IOException;

public interface IMServer {

	void start(int port,IMServerHandler handler) throws IOException;
	
	void close() throws IOException;

}
