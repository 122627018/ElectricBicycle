package com.wxxiaomi.electricbicycle.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wxxiaomi.electricbicycle.GlobalParams;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.bean.format.Login;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.engine.UserEngineImpl;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

public class LoginActivity extends BaseActivity {

	private EditText et_username;
	private EditText et_password;
	private Button btn_ok;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_login);
		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
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
			showLoadingDialog("正在登录..");
			String username = et_username.getText().toString().trim();
			String password = et_password.getText().toString().trim();
			LoginFromServer(username,password);
			
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
				return engine.Login(username, password);
			}

			@Override
			protected void onPostExecute(ReceiceData<Login> result) {
				closeLoadingDialog();
				if (result!=null) {
					if(result.state == 200){
						//登录成功
						GlobalParams.user = result.infos.userInfo;
						Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
						startActivity(intent);
						finish();
					}else{
//						Log.i("wang", "登录失败，错误信息："+result.error);
						showMsgDialog("登录失败"+result.error);
					}
				} else {
					showMsgDialog("登录失败，连接不上服务器");
//					Log.i("wang", "登录失败，连接不上服务器");
				}
			}
		}.execute();
		
	}

}
