/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.im.core.client;

public interface IMClientMessage {

	public void from(String who, String msg);

	public void initLogin(boolean status, String[] users, String msg);

	public void join(String who);

	public void leave(String who);

}
