/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.core.util.message.proxy;

public class MessageHandlerException extends Exception {

	private static final long serialVersionUID = 2866411005736493798L;

	public MessageHandlerException(String msg) {
		super(msg);
	}

	public MessageHandlerException(Exception superEx) {
		super(superEx);
	}
}
