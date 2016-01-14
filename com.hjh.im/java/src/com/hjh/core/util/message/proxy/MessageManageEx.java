/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:2260806429@qq.com 
 */
package com.hjh.core.util.message.proxy;

public class MessageManageEx<T> extends MessageManage {

	private ThreadLocal<T> locals = new ThreadLocal<T>();

	public T get() {
		return locals.get();
	}

	public void set(T val) {
		locals.set(val);
	}
}
