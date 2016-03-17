package com.wxxiaomi.electricbicycle.view.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wxxiaomi.electricbicycle.GlobalParams;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.bean.format.Register;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.engine.UserEngineImpl;
import com.wxxiaomi.electricbicycle.engine.common.ResultByGetDataListener;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

public class RegisterTwoActivity extends BaseActivity {

	private EditText et_password_one;
	private EditText et_password_two;
	private EditText et_name;
	private Button btn_ok;
	
	/**
	 * 上一个activity带过的phone
	 */
	private String phone;
	private int carid;
	private UserEngineImpl engine;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_register_two);
		et_password_one = (EditText) findViewById(R.id.et_password_one);
		et_password_two = (EditText) findViewById(R.id.et_password_two);
		et_name = (EditText) findViewById(R.id.et_name);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		
		//https://github.com/122627018/ElectricBicycle.git
	}

	@Override
	protected void initData() {
		engine = new UserEngineImpl(ct);
		carid = getIntent().getIntExtra("carid", 0);
		Log.i("wang", "registeractivity中carid="+carid);
		phone = getIntent().getBundleExtra("value").getString("phone");
//		Log.i("wang", "在第二个注册页面的initData()中取得的phone="+phone);
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			showLoadingDialog("正在注册");
			String passwordOne = et_password_one.getText().toString().trim();
			String passwordTwo = et_password_two.getText().toString().trim();
			String name = et_name.getText().toString().trim();
			boolean checkParsResult = checkPars(passwordOne,passwordTwo,name);
			if(checkParsResult){
//				连接服务器进行注册
				RegisterFromServer(phone,passwordOne,name);
			}
			
//			showLoadingDialog("正在注册..");
			//确定按钮
//			String username = et_username.getText().toString().trim();
//			String password = et_password.getText().toString().trim();
//			String name = et_name.getText().toString().trim();
//			RegisterFromServer(username,password,name);
			
			break;

		default:
			break;
		}
		
	}

	/**
	 * 检查所有需要输入的参数
	 * @param passwordOne
	 * @param passwordTwo
	 * @param name
	 * @return
	 */
	private boolean checkPars(String passwordOne, String passwordTwo, String name) {
		boolean flag = false;
		if("".equals(passwordOne)){
			//密码为空
		}else if("".equals(passwordTwo)){
			//验证密码为空
		}else if("".equals(name)){
			//姓名为空
		}else{
			boolean checkResult = checkSameAsPassword(passwordOne,passwordTwo);
			if(checkResult){
				//俩个密码一样
				flag = true;
			}else{
				//俩个密码不一样
			}
		}
		return flag;
	}


	/**
	 * 检查俩个密码是否相同
	 * @param passwordOne
	 * @param passwordTwo
	 */
	private boolean checkSameAsPassword(String passwordOne, String passwordTwo) {
		boolean flag = false;
		if(passwordOne.equals(passwordTwo)){
			flag = true;
		}
		return flag;
		
	}

	private void RegisterFromServer(final String username, final String password,
			final String name) {
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
//						Intent intent = new Intent(ct,HomeActivity2.class);
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
		engine.Register(username, password, name, new ResultByGetDataListener<Register>() {
			
			@Override
			public void success(ReceiceData<Register> result) {
				if(result.state == 200){
//					//登录成功
					GlobalParams.user = result.infos.userInfo;
					Intent intent = new Intent(ct,HomeActivity2.class);
					if(carid == 0){
						closeLoadingDialog();
						startActivity(intent);
						finish();
					}else{
						setloadingViewContent("正在绑定车子");
						bundCar(carid,result.infos.userInfo.id);
					}
//					
				}else{
//					Log.i("wang", "登录失败，错误信息："+result.error);
					showMsgDialog("注册失败"+result.error);
				}
				
			}
			
			@Override
			public void error(String error) {
				showMsgDialog("注册失败，连接不上服务器");
			}
		});
	}

	/**
	 * 绑定车子
	 * @param carid2
	 * @param id
	 */
	protected void bundCar(int carid2, int userid) {
		engine.BundCar(userid, carid2, new ResultByGetDataListener<String>() {
			
			@Override
			public void success(ReceiceData<String> result) {
				closeLoadingDialog();
				if(result.state == 200){
					showMsgDialog("绑定成功");
					Intent intent = new Intent(ct,HomeActivity2.class);
					startActivity(intent);
					finish();
				}else{
					showMsgDialog("绑定失败");
				}
				
			}
			
			@Override
			public void error(String error) {
				showMsgDialog("连接不上服务器");
				
			}
		});
		
	}

}
