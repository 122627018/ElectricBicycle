package com.wxxiaomi.electricbicycle.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

public class RegisterOneActivity extends BaseActivity {

	private EditText et_phone;
//	private EditText et_code;
	private Button btn_ok;
	private Button btn_send;
    private String phone;
//    private String code;
    private Button btn_hava;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_register_one);
		et_phone = (EditText) findViewById(R.id.et_phone);
//		et_code = (EditText) findViewById(R.id.et_code);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(this);
		btn_hava = (Button) findViewById(R.id.btn_hava);
		btn_hava.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			//确定按钮
			phone = et_phone.getText().toString().trim();
			Log.i("wang", "输入的手机号码是："+phone);
			
			showLoadingDialog("正在验证..");
			new Thread(new Runnable() {
				@Override
				public void run() {
					long start = System.currentTimeMillis();
					long costTime = System.currentTimeMillis() - start;
					if (2000 - costTime > 0) {
						try {
							Thread.sleep(2000 - costTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					closeLoadingDialog();
					Intent intent = new Intent(RegisterOneActivity.this,RegisterTwoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("phone", phone);
					intent.putExtra("value", bundle);
					startActivity(intent);
					finish();
					
				}
			}).start();
			break;
		case R.id.btn_send:
			//发送短信
			
		case R.id.btn_hava:
			break;
		default:
			break;
		}
		
	}

//	private void RegisterFromServer(final String username, final String password,
//			final String name) {
//		new AsyncTask<String, Void, ReceiceData<Register>>() {
//			@Override
//			protected ReceiceData<Register> doInBackground(String... params) {
//				UserEngineImpl engine = new UserEngineImpl();
//				return engine.Register(username, password, name);
//			}
//
//			@Override
//			protected void onPostExecute(ReceiceData<Register> result) {
//				closeLoadingDialog();
//				if (result!=null) {
//					if(result.state == 200){
//						//登录成功
//						GlobalParams.user = result.infos.userInfo;
//						Intent intent = new Intent(ct,HomeActivity.class);
//						startActivity(intent);
//						finish();
//					}else{
////						Log.i("wang", "登录失败，错误信息："+result.error);
//						showMsgDialog("注册失败"+result.error);
//					}
//				} else {
//					showMsgDialog("注册失败，连接不上服务器");
////					Log.i("wang", "登录失败，连接不上服务器");
//				}
//			}
//		}.execute();
//		
//	}

}
