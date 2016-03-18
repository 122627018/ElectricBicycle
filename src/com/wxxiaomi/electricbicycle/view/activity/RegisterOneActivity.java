package com.wxxiaomi.electricbicycle.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wxxiaomi.electricbicycle.R;
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

	private EditText et_phone;
	// private EditText et_code;
	/**
	 * 输入验证码之后确认按钮
	 */
	private Button btn_ok;

	/**
	 * 发送短信按钮
	 */
//	private Button btn_send;
	/**
	 * 用户输入的手机号码
	 */
	private String phone;
	// private String code;

	/**
	 * 已有账号，转移到登陆页面按钮
	 */
//	private Button btn_hava;
	
	/**
	 * 进入地图调试模式
	 */
//	private Button btn_debug;

	// private boolean itCanRegister = false;
	private int carid;
	
	private UserEngineImpl engine;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_register_one);
		et_phone = (EditText) findViewById(R.id.et_phone);
		// et_code = (EditText) findViewById(R.id.et_code);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
//		btn_send = (Button) findViewById(R.id.btn_send);
//		btn_send.setOnClickListener(this);
//		btn_ok.setVisibility(View.GONE);
//		btn_hava = (Button) findViewById(R.id.btn_hava);
//		btn_hava.setOnClickListener(this);
//		btn_debug = (Button) findViewById(R.id.btn_debug);
//		btn_debug.setOnClickListener(this);

	}

	@Override
	protected void initData() {
		engine = new UserEngineImpl(ct);
		carid = getIntent().getIntExtra("carid", 0);
//		Log.i("wang", "RegisterOne中carid="+carid);
		//为输入号码的edittext设置文字改变的监听
//		et_phone.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//				phoneChangeAfterSend();
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				
//			}
//		});
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			phone = et_phone.getText().toString().trim();
			showLoadingDialog("正在验证....");
			getPhoneCodeMessage(phone);
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					long start = System.currentTimeMillis();
//					long costTime = System.currentTimeMillis() - start;
//					if (2000 - costTime > 0) {
//						try {
//							Thread.sleep(2000 - costTime);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//					closeLoadingDialog();
//					Intent intent = new Intent(RegisterOneActivity.this,
//							RegisterTwoActivity.class);
//					Bundle bundle = new Bundle();
//					bundle.putString("phone", phone);
//					intent.putExtra("value", bundle);
//					intent.putExtra("carid", carid);
//					startActivity(intent);
//					finish();
//
//				}
//			}).start();
			break;
//		case R.id.btn_send:
//			// 发送短信
//			// 确定按钮
//			phone = et_phone.getText().toString().trim();
////			Log.i("wang", "输入的手机号码是：" + phone);
//
//			showLoadingDialog("正在发送..");
//			getPhoneCodeMessage(phone);
//			break;
//		case R.id.btn_hava:
//			// 进入登陆页面
//			Intent intent = new Intent(ct, LoginActivity.class);
//			startActivity(intent);
//			finish();
//			break;
		case R.id.btn_debug:
			
			Intent intent1 = new Intent(ct, HomeActivity2.class);
			startActivity(intent1);
				break;
		default:
			break;
		}

	}

//	/**
//	 * 设置确定按钮不可见
//	 */
//	private void phoneChangeAfterSend() {
//		btn_ok.setVisibility(View.GONE);
//	}

	/**
	 * 连接服务器检测手机号是否已被注册 如果未被注册，则获取验证码
	 * 
	 * @param phone
	 *            发送号码
	 */
	private void getPhoneCodeMessage(final String phone) {
//		new AsyncTask<String, Void, ReceiceData<String>>() {
//			@Override
//			protected ReceiceData<String> doInBackground(String... params) {
//				UserEngineImpl engine = new UserEngineImpl();
//				return engine.getPhoneCodeMsg(phone);
//			}
//
//			@Override
//			protected void onPostExecute(ReceiceData<String> result) {
//				closeLoadingDialog();
//				if (result != null) {
//					if (result.state == 200) {
//						// 获取短信成功,也就是此手机号可以注册
//						btn_ok.setVisibility(View.VISIBLE);
//					} else {
//						showMsgDialog(result.error);
//					}
//				} else {
//					showMsgDialog("连接不上服务器");
//				}
//			}
//		}.execute();
		engine.getPhoneCodeMsg(phone, new ResultByGetDataListener<String>() {
			
			@Override
			public void success(ReceiceData<String> result) {
				closeLoadingDialog();
				if (result.state == 200) {
//					// 获取短信成功,也就是此手机号可以注册
					GoToNext();
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

	protected void GoToNext() {
		Intent intent = new Intent(RegisterOneActivity.this,
				RegisterTwoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("phone", phone);
		intent.putExtra("value", bundle);
		intent.putExtra("carid", carid);
		startActivity(intent);
		finish();
		
	}

}
