/**
 * @author huangjinhong 
 * qq:2260806429 
 * email:xxonehjh@163.com 
 */
package com.hjh.im.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.hjh.core.util.JsonUtil;

public class JsonTester {

	public static void main(String argv[]) throws IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("abc", "aa");
		params.put("bb", "cc");

		String json = JsonUtil.toJson(params);
		System.out.println(json);

		System.out.println(JsonUtil.fromJson(json, Map.class).get("abc"));
	}
}
