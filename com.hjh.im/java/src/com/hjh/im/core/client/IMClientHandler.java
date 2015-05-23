/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.im.core.client;

public interface IMClientHandler {

	public void from(String who, String msg);

	public void initUsers(String[] users);

	public void join(String who);

	public void leave(String who);

	public void shutdown();
}
