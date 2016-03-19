package com.wxxiaomi.electricbicycle.view.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.wxxiaomi.electricbicycle.GlobalParams;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.bean.User;
import com.wxxiaomi.electricbicycle.bean.format.Register;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.engine.UserEngineImpl;
import com.wxxiaomi.electricbicycle.engine.common.ResultByGetDataListener;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

/**
 * 注册页面一
 * @author Administrator
 *
 */
public class RegisterOneActivity extends BaseActivity {

	private TextInputLayout til_username;
	private TextInputLayout til_password;
	
	/**
	 * 输入验证码之后确认按钮
	 */
	private Button btn_ok;
	private int carid;
	
	private UserEngineImpl engine;
	private Toolbar toolbar;
	private String username;
	private String password;
	

	@Override
	protected void initView() {
		setContentView(R.layout.activity_register_one);
//		et_username = (EditText) findViewById(R.id.et_username);
		til_username = (TextInputLayout) findViewById(R.id.til_username);
		til_password = (TextInputLayout) findViewById(R.id.til_password);
		
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		toolbar = (Toolbar) this.findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

	}

	@Override
	protected void initData() {
		engine = new UserEngineImpl(ct);
		carid = getIntent().getIntExtra("carid", 0);
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			username = til_username.getEditText().getText().toString().trim();
			password = til_password.getEditText().getText().toString().trim();
			showLoadingDialog("正在验证....");
			getPhoneCodeMessage(username,password);
			break;
		case R.id.btn_debug:
			
			Intent intent1 = new Intent(ct, HomeActivity2.class);
			startActivity(intent1);
				break;
		default:
			break;
		}

	}


	/**
	 * 连接服务器检测手机号是否已被注册 如果未被注册，则获取验证码
	 * 
	 * @param phone
	 *            发送号码
	 */
	private void getPhoneCodeMessage(final String username,final String password) {
		engine.registerUser(username,password, new ResultByGetDataListener<Register>() {
			
			@Override
			public void success(ReceiceData<Register> result) {
				closeLoadingDialog();
				if (result.state == 200) {
//					// 获取短信成功,也就是此手机号可以注册
					GlobalParams.user = result.infos.userInfo;
					GoToNext(result.infos.userInfo);
//					btn_ok.setVisibility(View.VISIBLE);
				} else {
					showMsgDialog(result.error);
				}
				
			}
			
			@Override
			public void error(String error) {
				// TODO Auto-generated method stub
				showMsgDialog("连接不上服务器");
			}
		});
	}

	protected void GoToNext(User userInfo) {
		Intent intent = new Intent(RegisterOneActivity.this,
				RegisterTwoActivity.class);
		Log.i("wang", "userInfo.tostring="+userInfo.toString());
		intent.putExtra("userInfo", userInfo);
		intent.putExtra("carid", carid);
		startActivity(intent);
		finish();
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
