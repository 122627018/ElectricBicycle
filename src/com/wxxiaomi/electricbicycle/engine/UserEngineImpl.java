package com.wxxiaomi.electricbicycle.engine;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wxxiaomi.electricbicycle.ConstantValue;
import com.wxxiaomi.electricbicycle.bean.format.InitUserInfo;
import com.wxxiaomi.electricbicycle.bean.format.Login;
import com.wxxiaomi.electricbicycle.bean.format.Register;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.util.HttpClientUtil;

public class UserEngineImpl {
	
	public ReceiceData<InitUserInfo> initUserInfoData(String username,String password){
		String url = ConstantValue.SERVER_URL+"ActionServlet?action=inituserinfo"+"&username="+username+"&password="+password;
		String json = HttpClientUtil.doGet(url);
		try {
//			Log.i("wang", "检查手机号后返回的json="+json);
			Gson gson = new Gson();
			ReceiceData<InitUserInfo> fromJson = gson.fromJson(json, new TypeToken<ReceiceData<InitUserInfo>>(){}.getType());
			return fromJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 连接服务器检测手机号是否已被注册 如果未被注册，则获取验证码
	 * 
	 * @param phone
	 *            发送号码
	 */
	public ReceiceData<String> getPhoneCodeMsg(String phone){
		String url = ConstantValue.SERVER_URL+"ActionServlet?action=checkphone"+"&phone="+phone;
		String json = HttpClientUtil.doGet(url);
		try {
			Log.i("wang", "检查手机号后返回的json="+json);
			Gson gson = new Gson();
			ReceiceData<String> fromJson = gson.fromJson(json, new TypeToken<ReceiceData<String>>(){}.getType());
			return fromJson;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 执行登录操作
	 * @param username  账号
	 * @param password  密码
	 * @return
	 */
	public ReceiceData<Login> Login(String username,String password,boolean isFirst){
		String url ;
		if(isFirst){
			url= ConstantValue.SERVER_URL+ConstantValue.LOGIN_URL+"&username="+username+"&password="+password+"&isfirst=y";
		}else{
			url = ConstantValue.SERVER_URL+ConstantValue.LOGIN_URL+"&username="+username+"&password="+password+"&isfirst=n";
		}
		String json = HttpClientUtil.doGet(url);
		Log.i("wang", "登陆的json="+json);
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
