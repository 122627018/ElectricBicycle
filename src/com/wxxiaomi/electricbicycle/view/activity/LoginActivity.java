package com.wxxiaomi.electricbicycle.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.wxxiaomi.electricbicycle.GlobalParams;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.bean.User;
import com.wxxiaomi.electricbicycle.bean.format.InitUserInfo;
import com.wxxiaomi.electricbicycle.bean.format.Login;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.engine.UserEngineImpl;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

public class LoginActivity extends BaseActivity {

	private EditText et_username;
	private EditText et_password;
	private Button btn_ok;
	private Button btn_register;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_login);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		
	}

	/**
	 * 处理点击事件
	 */
	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			//点击确定按钮
			
			String username = et_username.getText().toString().trim();
			String password = et_password.getText().toString().trim();
			if(checkInputString(username,password)){
				showLoadingDialog("正在登录..");
				LoginFromServer(username,password);
			}
				
			break;
		case R.id.btn_register:
			Intent intent = new Intent(ct,RegisterTwoActivity.class);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}

	}

	/**
	 * 连接服务器，执行登录操作
	 * @param username
	 * @param password
	 */
	private void LoginFromServer(final String username, final String password) {
		new AsyncTask<String, Void, ReceiceData<Login>>() {
			@Override
			protected ReceiceData<Login> doInBackground(String... params) {
				UserEngineImpl engine = new UserEngineImpl();
				return engine.Login(username, password,true);
			}

			@Override
			protected void onPostExecute(ReceiceData<Login> result) {
//				closeLoadingDialog();
				if (result!=null) {
					if(result.state == 200){
						//登录成功
//						GlobalParams.user = result.infos.userInfo;
//						Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
//						startActivity(intent);
//						finish();
						LoginFromEM(result.infos.userInfo);
					}else{
						showMsgDialog("登录失败"+result.error);
					}
				} else {
					showMsgDialog("登录失败，连接不上服务器");
				}
			}
		}.execute();
		
	}

	/**
	 * 登陆em服务器
	 * @param userInfo
	 */
	protected void LoginFromEM(final User userInfo) {
		EMClient.getInstance().login(userInfo.username, userInfo.password, new EMCallBack() {

			@Override
			public void onSuccess() {
				closeLoadingDialog();
//				if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
//					pd.dismiss();
//				}
				
				// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
				// ** manually load all local groups and
			    EMClient.getInstance().groupManager().loadAllGroups();
			    EMClient.getInstance().chatManager().loadAllConversations();
				
				// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
//				boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
//						DemoApplication.currentUserNick.trim());
//				if (!updatenick) {
//					Log.e("LoginActivity", "update current user nick fail");
//				}
				//异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
//				DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

				// 进入主页面
//				Intent intent = new Intent(LoginActivity.this,
//						HomeActivity.class);
//				startActivity(intent);
//				finish();
			    
			    AfterLoginCheck(userInfo);
			    
			}

			@Override
			public void onProgress(int progress, String status) {
//				Log.d(TAG, "login: onProgress");
			}

			@Override
			public void onError(final int code, final String message) {
//				Log.d(TAG, "login: onError: " + code);
//				if (!progressShow) {
//					return;
//				}
//				runOnUiThread(new Runnable() {
//					public void run() {
//						pd.dismiss();
//						Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
//								Toast.LENGTH_SHORT).show();
//					}
//				});
				closeLoadingDialog();
				showMsgDialog("登陆聊天服务器失败，请重新登陆");
			}
		});
	}
	
	/**
	 * 检查本地是否有此账号相关的信息
	 * 如果有就取出
	 * 如果没有就连接服务器获取
	 */
	protected void AfterLoginCheck(final User userInfo) {
		// 先设置没有
		showLoadingDialog("正在初始化账号相关信息");
		new AsyncTask<String, Void, ReceiceData<InitUserInfo>>() {
			@Override
			protected ReceiceData<InitUserInfo> doInBackground(String... params) {
				UserEngineImpl engine = new UserEngineImpl();
				return engine.initUserInfoData(userInfo.username, userInfo.password);
			}

			@Override
			protected void onPostExecute(ReceiceData<InitUserInfo> result) {
//				closeLoadingDialog();
				if (result!=null) {
					if(result.state == 200){
						//登录成功
						GlobalParams.user = userInfo;
						GlobalParams.friendList = result.infos.friendList;
						Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
						startActivity(intent);
						finish();
//						LoginFromEM(result.infos.userInfo);
					}else{
						showMsgDialog("初始化失败"+result.error);
					}
				} else {
					showMsgDialog("初始化失败，连接不上服务器");
				}
			}
		}.execute();
		
	}

	/**
	 * 对输入的用户名和密码进行字符校验
	 * @param username
	 * @param password
	 * @return
	 */
	private boolean checkInputString(String username,String password){
		
		boolean flag = true;
		if("".equals(username)){
			//用户名不能为空
			showMsgDialog("用户名不能为空");
			flag = false;
		}else if("".equals(password)){
			//密码不能为空
			showMsgDialog("密码不能为空");
			flag = false;
		}else if(username.contains(" ")){
			//用户名出现空格
			showMsgDialog("用户名出现空格");
			flag = false;
		}else if(username.length()<6){
			//用户名长度少于6位
			showMsgDialog("用户名长度少于6位");
			flag = false;
		}else if(password.length()<6){
			//密码少于6位
			showMsgDialog("密码少于6位");
			flag = false;
		}
		return flag;
	}

}
