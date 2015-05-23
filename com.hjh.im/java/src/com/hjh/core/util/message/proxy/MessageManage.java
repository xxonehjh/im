/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.core.util.message.proxy;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.hjh.core.util.JsonUtil;
import com.hjh.core.util.LogHelper;

public class MessageManage {

	private Map<String, Object> handlers = new HashMap<String, Object>();

	public void reg(Object obj) {
		for (Class<?> item : obj.getClass().getInterfaces()) {
			LogHelper.debug("注册消息处理器:" + item.getSimpleName());
			handlers.put(item.getSimpleName(), obj);
		}
	}

	public void handle(Message msg) throws MessageHandlerException {

		String[] cmds = msg.getCmd().split("\\.");
		if (null == handlers.get(cmds[0])) {
			throw new MessageHandlerException("无处理类:" + cmds[0]);
		}

		Object handle = handlers.get(cmds[0]);
		Method method = findMethod(
				findInterface(handle.getClass().getInterfaces(), cmds[0]),
				cmds[1]);

		if (null == method) {
			throw new MessageHandlerException("类:" + cmds[0] + ":无处理方法:"
					+ cmds[1]);
		}

		int paramSize = method.getParameterTypes().length;
		Object[] params = new Object[paramSize];

		Annotation[][] arrs = method.getParameterAnnotations();
		int index = 0;
		for (Annotation[] item : arrs) {
			Param param = findParam(item);
			String key = null == param ? "__" + index : param.value();
			params[index] = toTypeValue(msg.getObject(key),
					method.getParameterTypes()[index]);
			index++;
		}

		try {
			method.invoke(handle, params);
		} catch (Exception e) {
			throw new MessageHandlerException(e);
		}

	}

	@SuppressWarnings("unchecked")
	public static <T> T createMessageCaller(final MessageSender sender,
			final Class<? extends T> cls) {
		return (T) Proxy.newProxyInstance(MessageManage.class.getClassLoader(),
				new Class<?>[] { cls }, new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {

						Message msg = new Message();
						msg.setCmd(cls.getSimpleName() + "." + method.getName());

						Annotation[][] arrs = method.getParameterAnnotations();
						int index = 0;
						for (Annotation[] item : arrs) {
							Param param = findParam(item);
							if (null == param) {
								msg.put("__" + index, args[index]);
							} else {
								msg.put(param.value(), args[index]);
							}
							index++;
						}

						sender.send(msg);
						return null;
					}
				});
	}

	private static Param findParam(Annotation[] items) {
		if (null == items || 0 == items.length) {
			return null;
		}
		for (Annotation item : items) {
			if (Param.class.isAssignableFrom(item.annotationType())) {
				return (Param) item;
			}
		}
		return null;
	}

	private Object toTypeValue(Object value, Class<?> cls)
			throws MessageHandlerException {
		if (null == value || value.getClass().equals(cls)) {
			return null == value ? getDefaultValue(cls) : value;
		}
		if (cls.equals(String.class)) {
			return value.toString();
		}
		if (cls == int.class || cls == Integer.class) {
			return Integer.parseInt(value.toString());
		}
		if (cls == long.class || cls == Long.class) {
			return Long.parseLong(value.toString());
		}
		if (cls == float.class || cls == Float.class) {
			return Float.parseFloat(value.toString());
		}
		if (cls == double.class || cls == Double.class) {
			return Double.parseDouble(value.toString());
		}
		if (cls == boolean.class || cls == Boolean.class) {
			return Boolean.parseBoolean(value.toString());
		}

		try {
			return JsonUtil.fromJson(JsonUtil.toJson(value), cls);
		} catch (IOException e) {
			LogHelper.error(e);
			throw new MessageHandlerException("类型转换失败:"
					+ value.getClass().getName() + ":TO:" + cls.getName());
		}

	}

	private Class<?> findInterface(Class<?>[] interfaces, String name) {
		for (Class<?> item : interfaces) {
			if (name.equals(item.getSimpleName())) {
				return item;
			}
		}
		return null;
	}

	private Method findMethod(Class<?> cls, String name) {
		if (null == cls) {
			return null;
		}
		for (Method m : cls.getMethods()) {
			if (m.getName().equals(name)) {
				return m;
			}
		}
		return null;
	}

	public static Object getDefaultValue(Class<?> type) {
		if (type == int.class || type == Integer.class) {
			return 0;
		}
		if (type == long.class || type == Long.class) {
			return 0;
		}
		if (type == float.class || type == Float.class) {
			return 0;
		}
		if (type == double.class || type == Double.class) {
			return 0;
		}
		if (type == boolean.class || type == Boolean.class) {
			return false;
		}
		return null;
	}

}
