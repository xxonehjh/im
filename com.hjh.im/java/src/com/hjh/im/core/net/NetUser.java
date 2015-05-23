/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.im.core.net;

import com.hjh.im.core.client.IMClientMessage;
import com.hjh.im.core.server.IMServerMessage;

public interface NetUser {

	public boolean isLogin();

	public String getId();

	public void init(String name);

	public long getLoginTime();

	public IMServerMessage getServerMessage();

	public IMClientMessage getClientMessage();

}
