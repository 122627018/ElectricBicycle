package com.wxxiaomi.electricbicycle.engine;


import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wxxiaomi.electricbicycle.ConstantValue;
import com.wxxiaomi.electricbicycle.bean.format.InitUserInfo;
import com.wxxiaomi.electricbicycle.bean.format.Login;
import com.wxxiaomi.electricbicycle.bean.format.Register;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.engine.common.ResultByGetDataListener;

public class UserEngineImpl {
	
	@SuppressWarnings("unused")
	private Context context;
	RequestQueue mQueue;
	
	
	
	public UserEngineImpl(Context context) {
		super();
		this.context = context;
		mQueue = Volley.newRequestQueue(context);
	}

	public void initUserInfoData(String username,String password,final ResultByGetDataListener<InitUserInfo> lis){
		String url = ConstantValue.SERVER_URL+"ActionServlet?action=inituserinfo"+"&username="+username+"&password="+password;
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// processJsonResult(response.toString());
						Gson gson = new Gson();
						ReceiceData<InitUserInfo> result = gson.fromJson(
								response.toString(),
								new TypeToken<ReceiceData<InitUserInfo>>() {
								}.getType());
						lis.success(result);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("wang", error.toString(), error);
						lis.error(error.getMessage());
					}
				});
		mQueue.add(jsonObjectRequest);
	}
	
	/**
	 * 连接服务器检测手机号是否已被注册 如果未被注册，则获取验证码
	 * 
	 * @param phone
	 *            发送号码
	 */
	public void registerUser(String username,String password,final ResultByGetDataListener<Register> lis){
		String url = ConstantValue.SERVER_URL+"ActionServlet?action=register"+"&username="+username+"&password="+password;
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// processJsonResult(response.toString());
						Gson gson = new Gson();
						ReceiceData<Register> result = gson.fromJson(
								response.toString(),
								new TypeToken<ReceiceData<Register>>() {
								}.getType());
						lis.success(result);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("wang", error.toString(), error);
						lis.error(error.getMessage());
					}
				});
		mQueue.add(jsonObjectRequest);
	}

	/**
	 * 执行登录操作
	 * @param username  账号
	 * @param password  密码
	 * @return
	 */
	public void Login(String username,String password,boolean isFirst
			,final ResultByGetDataListener<Login> lis){
		String url ;
		if(isFirst){
			url= ConstantValue.SERVER_URL+ConstantValue.LOGIN_URL+"&username="+username+"&password="+password+"&isfirst=y";
		}else{
			url = ConstantValue.SERVER_URL+ConstantValue.LOGIN_URL+"&username="+username+"&password="+password+"&isfirst=n";
		}
//		String json = HttpClientUtil.doGet(url);
//		Log.i("wang", "登陆的json="+json);
//		try {
//			Gson gson = new Gson();
//			ReceiceData<Login> fromJson = gson.fromJson(json, new TypeToken<ReceiceData<Login>>(){}.getType());
//			return fromJson;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// processJsonResult(response.toString());
						Gson gson = new Gson();
						ReceiceData<Login> result = gson.fromJson(
								response.toString(),
								new TypeToken<ReceiceData<Login>>() {
								}.getType());
						lis.success(result);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("wang", error.toString(), error);
						lis.error(error.getMessage());
					}
				});
		mQueue.add(jsonObjectRequest);
		
	}
	
//	public void RegisterWithCar(String username,String password,String name,int carid
//			,final ResultByGetDataListener<Register> lis){
//		String url = ConstantValue.SERVER_URL+"ActionServlet?action=register&name="+name+"&username="+username+"&password="+password;
//	}
	
	public void BundCar(int userid,int carid
			,final ResultByGetDataListener<String> lis){
		String url = ConstantValue.SERVER_URL+"ActionServlet?action=bundbicycle&userid="+userid+"&cardid="+carid;
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// processJsonResult(response.toString());
						Gson gson = new Gson();
						ReceiceData<String> result = gson.fromJson(
								response.toString(),
								new TypeToken<ReceiceData<String>>() {
								}.getType());
						lis.success(result);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("wang", error.toString(), error);
						lis.error(error.getMessage());
					}
				});
		mQueue.add(jsonObjectRequest);
	}
	
	/**
	 * 注册一个用户
	 * @param userid 
	 * @param username 账号
	 * @param password 密码
	 * @param name 用户名
	 * @param headUrl2 
	 * @return
	 */
	public void ImproveUserInfo(int userid, String username,String name,String description,String headUrl
			, final ResultByGetDataListener<Register> lis){
		String url = ConstantValue.SERVER_URL+"ActionServlet?action=improveuserinfo&name="
			+name+"&description="+description+"&headUrl="+headUrl
			+"&userid="+userid+"&username="+username;
		Log.i("wang", "url="+url);
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// processJsonResult(response.toString());
						Gson gson = new Gson();
						ReceiceData<Register> result = gson.fromJson(
								response.toString(),
								new TypeToken<ReceiceData<Register>>() {
								}.getType());
						lis.success(result);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.i("wang", error.toString(), error);
						lis.error(error.getMessage());
					}
				});
		mQueue.add(jsonObjectRequest);
//		
	}
}
