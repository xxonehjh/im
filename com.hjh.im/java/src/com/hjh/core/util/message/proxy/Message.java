/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.core.util.message.proxy;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;

import com.hjh.core.util.JsonUtil;
import com.hjh.core.util.LogHelper;

public class Message {

	public static final String MESSAGE_EXIT = "exit";
	public static final String ENCODE = "utf-8";

	protected String cmd;
	protected Map<String, Object> params = new HashMap<String, Object>();

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public void put(String key, Object value) {
		params.put(key, value);
	}

	public String get(String key) {
		return (String) params.get(key);
	}

	public Object getObject(String key) {
		return params.get(key);
	}

	public String toString() {
		try {
			return JsonUtil.toJson(this);
		} catch (Exception e) {
			LogHelper.error(e);
			throw new RuntimeException(e);
		}
	}

	public byte[] toByteArray() {
		try {
			return toString().getBytes(Message.ENCODE);
		} catch (UnsupportedEncodingException e) {
			LogHelper.error(e);
			throw new RuntimeException(e);
		}
	}

	public static Message fromArray(byte[] arr) {
		try {
			return JsonUtil.fromJson(new String(arr, Message.ENCODE),
					Message.class);
		} catch (JsonParseException ex) {
			LogHelper.error(ex.getMessage());
		} catch (Exception e) {
			LogHelper.error(e);
		}
		return null;
	}

	public static Message newInstance(String cmd, Object... params) {
		Message msg = new Message();
		msg.cmd = cmd;
		if (null != params && 0 != params.length) {
			if (params.length % 2 != 0) {
				throw new RuntimeException("参数个数错误");
			}
			int len = params.length;
			for (int i = 0; i < len; i += 2) {
				msg.params.put((String) params[i], params[i + 1]);
			}
		}
		return msg;
	}

}
