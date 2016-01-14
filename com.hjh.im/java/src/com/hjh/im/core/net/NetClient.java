/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.core.net;

import java.io.IOException;


public interface NetClient {

	public void start(NetHandler netHandler, String ip, int port)
			throws IOException;

	public void close() throws IOException;

	public NetUser getUser();

}
