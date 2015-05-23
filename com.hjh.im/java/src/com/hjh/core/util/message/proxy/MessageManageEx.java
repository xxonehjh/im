/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
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
