/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.core.net;

import com.hjh.core.util.message.proxy.Message;

public interface NetHandler {

	public void handle(NetUser sc, Message data);

}
