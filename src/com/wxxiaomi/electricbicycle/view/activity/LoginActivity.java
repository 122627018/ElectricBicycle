package com.wxxiaomi.electricbicycle.view.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.wxxiaomi.electricbicycle.AppManager;
import com.wxxiaomi.electricbicycle.GlobalParams;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.bean.User;
import com.wxxiaomi.electricbicycle.bean.format.InitUserInfo;
import com.wxxiaomi.electricbicycle.bean.format.Login;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.engine.UserEngineImpl;
import com.wxxiaomi.electricbicycle.engine.common.ResultByGetDataListener;
import com.wxxiaomi.electricbicycle.util.MyUtils;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

/**
 * 登陆页面
 * 
 * @author Mr.W
 * 
 */
public class LoginActivity extends BaseActivity {

	private TextInputLayout til_username;
	private TextInputLayout til_password;
	private Button btn_ok;
	private UserEngineImpl engine;
	private Toolbar toolbar;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_login);
		AppManager.getAppManager().addActivity(this);
		til_username = (TextInputLayout) findViewById(R.id.til_username);
		til_password = (TextInputLayout) findViewById(R.id.til_password);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		toolbar = (Toolbar) this.findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true); // 设置返回键可用
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void initData() {
		engine = new UserEngineImpl(ct);
		til_username.getEditText().addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				til_username.setError("");
				til_username.setEnabled(false);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
//				checkFormat(til_username);
			}
		});
		til_password.getEditText().addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				til_password.setError("");
				til_password.setEnabled(false);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
//				checkFormat(til_password);
			}
		});
	}

	/**
	 * 处理点击事件
	 */
	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			
			String username = til_username.getEditText().getText().toString()
					.trim();
			String password = til_password.getEditText().getText().toString()
					.trim();
			if (checkFormat(til_username) && checkFormat(til_password)) {
				showLoading1Dialog("正在登陆");
				LoginFromServer(username, password);
				return;
			} else {
//				closeLoading1Dialog();
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 连接服务器，执行登录操作
	 * 
	 * @param username
	 * @param password
	 */
	private void LoginFromServer(final String username, final String password) {
		engine.Login(username, password, true,
				new ResultByGetDataListener<Login>() {

					@Override
					public void success(ReceiceData<Login> result) {

						if (result.state == 200) {
							// //登录成功
							GlobalParams.user = result.infos.userInfo;
							LoginFromEM(result.infos.userInfo);
						} else {
							closeLoading1Dialog();
							showErrorDialog("登录失败" + result.error);
						}
					}

					@Override
					public void error(String error) {
						closeLoading1Dialog();
						showErrorDialog("登录失败，连接不上服务器");
					}
				});

	}

	/**
	 * 登陆em服务器
	 * 
	 * @param userInfo
	 */
	protected void LoginFromEM(final User userInfo) {
		setLoadingContent("正在登陆聊天服务器");
		EMClient.getInstance().login(userInfo.username, userInfo.password,
				new EMCallBack() {
					@Override
					public void onSuccess() {
						// closeLoadingDialog();
						// if (!LoginActivity.this.isFinishing() &&
						// pd.isShowing()) {
						// pd.dismiss();
						// }

						// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
						// ** manually load all local groups and
						EMClient.getInstance().groupManager().loadAllGroups();
						EMClient.getInstance().chatManager()
								.loadAllConversations();

						// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
						// boolean updatenick =
						// EMClient.getInstance().updateCurrentUserNick(
						// DemoApplication.currentUserNick.trim());
						// if (!updatenick) {
						// Log.e("LoginActivity",
						// "update current user nick fail");
						// }
						// 异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
						// DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

						// 进入主页面
						// Intent intent = new Intent(LoginActivity.this,
						// HomeActivity.class);
						// startActivity(intent);
						// finish();
						Log.i("wang", "登陆em服务器成功");
						AfterLoginCheck(userInfo);

					}

					@Override
					public void onProgress(int progress, String status) {
						// Log.d(TAG, "login: onProgress");
					}

					@Override
					public void onError(final int code, final String message) {
						Log.i("wang", "login: onError: " + code + "---message="
								+ message);
						closeLoading1Dialog();
						showErrorDialog("登陆聊天服务器失败，请尝试重新登陆");
					}
				});
	}

	/**
	 * 检查本地是否有此账号相关的信息 如果有就取出 如果没有就连接服务器获取
	 */
	protected void AfterLoginCheck(final User userInfo) {
		// 本地是否有该账号信息d
		// boolean isHasUserInfo = false;
		// 先默认没有该账号信息
		// showLoadingDialog("正在初始化账号相关信息");
		setLoadingContent("正在初始化帐号相关信息");
		engine.initUserInfoData(userInfo.username, userInfo.password,
				new ResultByGetDataListener<InitUserInfo>() {

					@Override
					public void success(ReceiceData<InitUserInfo> result) {
						if (result.state == 200) {
							closeLoading1Dialog();
							// //登录成功
							GlobalParams.user = userInfo;
							GlobalParams.friendList = result.infos.friendList;
							Intent intent = new Intent(LoginActivity.this,
									HomeActivity2.class);
							startActivity(intent);
							finish();
						} else {
							closeLoading1Dialog();
							GlobalParams.user = userInfo;
							Intent intent = new Intent(LoginActivity.this,
									HomeActivity2.class);
							startActivity(intent);
							finish();
						}

					}

					@Override
					public void error(String error) {
						closeLoading1Dialog();
						showErrorDialog("连接不上服务器");
					}
				});
	}

	private boolean checkFormat(TextInputLayout strLayout) {
		String str = strLayout.getEditText().getText().toString().trim();
		if ("".equals(str)){
			strLayout.setError("不能为空");
			return false;
		}else if(str.contains(" ")){
			strLayout.setError("出现非法字符");
			return false;
		}else if(MyUtils.checkChainse(str)){
			strLayout.setError("不能包含中文");
			return false;
		}
		else if(str.length() < 6){
			strLayout.setError("长度小于6");
			return false;
		}else{
			strLayout.setEnabled(false);
			return true;
		}
	}

//	/**
//	 * 对输入的用户名和密码进行字符校验
//	 * 
//	 * @param username
//	 * @param password
//	 * @return
//	 */
//	private boolean checkInputString(String username, String password) {
//
//		if ("".equals(username)) {
//			// 用户名不能为空
//			// showMsgDialog("用户名不能为空");
//			// showErrorDialog("用户名不能为空");
//			til_username.setError("用户名不能为空");
//
//			return false;
//		} else if ("".equals(password)) {
//			// 密码不能为空
//			showErrorDialog("密码不能为空");
//			return false;
//		} else if (username.contains(" ")) {
//			// 用户名出现空格
//			showErrorDialog("用户名出现空格");
//			return false;
//		} else if (username.length() < 6) {
//			// 用户名长度少于6位
//			showErrorDialog("用户名长度少于6位");
//			return false;
//		} else if (password.length() < 6) {
//			// 密码少于6位
//			showErrorDialog("密码少于6位");
//			return false;
//
//		} else {
//			return true;
//		}
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		AppManager.getAppManager().finishActivity(this);
		super.onDestroy();

	}

}
