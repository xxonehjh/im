/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.im.core.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.hjh.core.util.LogHelper;
import com.hjh.im.core.IMConfig;
import com.hjh.im.core.net.NetUser;

public class SimpleIMServerHandler implements IMServerHandler {

	private Map<String, NetUser> users = Collections
			.synchronizedMap(new HashMap<String, NetUser>());

	@Override
	public boolean join(NetUser user, String name) {

		if (users.containsKey(name)) {
			user.getClientMessage().initLogin(false, null, "用户名已经存在");
			return false;

		} else {

			for (NetUser item : users.values()) {
				item.getClientMessage().join(name);
			}
			user.init(name);
			users.put(name, user);
			LogHelper.info(name + ":登陆");
			user.getClientMessage().initLogin(true,
					users.keySet().toArray(new String[users.size()]), null);
			return true;
		}

	}

	@Override
	public void say(NetUser user, String toName, String msgStr) {

		if (IMConfig.USER_ALL.equals(toName)) {
			for (NetUser item : users.values()) {
				item.getClientMessage().from(user.getId(), msgStr);
			}
		} else {
			NetUser to = users.get(toName);
			if (null != to) {
				to.getClientMessage().from(user.getId(), msgStr);
			} else {
				LogHelper.info("无用户 :" + toName);
			}
		}

	}

	@Override
	public void exit(NetUser user, boolean exception) {

		users.remove(user.getId());
		for (NetUser item : users.values()) {
			item.getClientMessage().leave(user.getId());
		}
		LogHelper.info(user.getId() + ":登出" + (exception ? "(异常)" : ""));

	}

	@Override
	public void shutdown() {
		LogHelper.info("服务器关闭");
	}

}
