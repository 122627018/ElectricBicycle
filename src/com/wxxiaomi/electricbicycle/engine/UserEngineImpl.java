package com.wxxiaomi.electricbicycle.engine;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wxxiaomi.electricbicycle.ConstantValue;
import com.wxxiaomi.electricbicycle.bean.format.Login;
import com.wxxiaomi.electricbicycle.bean.format.Register;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.util.HttpClientUtil;

public class UserEngineImpl {

	/**
	 * 执行登录操作
	 * @param username  账号
	 * @param password  密码
	 * @return
	 */
	public ReceiceData<Login> Login(String username,String password){
		String url = ConstantValue.SERVER_URL+ConstantValue.LOGIN_URL+"&username="+username+"&password="+password;
		Log.i("wang", "url="+url);
		String json = HttpClientUtil.doGet(url);
		try {
			Gson gson = new Gson();
			ReceiceData<Login> fromJson = gson.fromJson(json, new TypeToken<ReceiceData<Login>>(){}.getType());
			return fromJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * 注册一个用户
	 * @param username 账号
	 * @param password 密码
	 * @param name 用户名
	 * @return
	 */
	public ReceiceData<Register> Register(String username,String password,String name){
		String url = ConstantValue.SERVER_URL+"ActionServlet?action=register&name="+name+"&username="+username+"&password="+password;
		Log.i("wang", "url="+url);
		String json = HttpClientUtil.doGet(url);
		try {
			Gson gson = new Gson();
			ReceiceData<Register> fromJson = gson.fromJson(json, new TypeToken<ReceiceData<Register>>(){}.getType());
			return fromJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
