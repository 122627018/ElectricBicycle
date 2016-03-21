package com.wxxiaomi.electricbicycle.view.activity;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wxxiaomi.electricbicycle.AppManager;
import com.wxxiaomi.electricbicycle.GlobalParams;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.bean.User;
import com.wxxiaomi.electricbicycle.bean.format.Register;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.engine.UserEngineImpl;
import com.wxxiaomi.electricbicycle.engine.common.ResultByGetDataListener;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

public class RegisterTwoActivity extends BaseActivity {

//	private EditText et_name;
	private Button btn_ok;
	
	/**
	 * 上一个activity带过的phone
	 */
//	private String userid;
	private User userInfo;
	private int carid;
	private UserEngineImpl engine;
	private TextInputLayout til_name;
	private TextInputLayout til_description;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_register_two);
		AppManager.getAppManager().addActivity(this);
		til_name = (TextInputLayout) findViewById(R.id.til_name);
		til_description = (TextInputLayout) findViewById(R.id.til_description);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		
	}

	@Override
	protected void initData() {
		engine = new UserEngineImpl(ct);
		carid = getIntent().getIntExtra("carid", 0);
		userInfo = (User) getIntent().getExtras().get("userInfo");
		Log.i("wang", "userInfo.id="+userInfo.id);
//		Log.i("wang", "在第二个注册页面的initData()中取得的phone="+phone);
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			showLoading1Dialog("正在注册");
			String name = til_name.getEditText().getText().toString();
			String description = til_description.getEditText().getText().toString();
//			boolean checkParsResult = checkPars(name,description,name);
			if(true){
//				连接服务器进行注册
				improveUserInfo(name,description,"",userInfo.id,userInfo.username);
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
	 * @param username 
	 * @param userid 
	 * @return
	 */
//	private boolean checkPars(String passwordOne, String passwordTwo, String name) {
//		boolean flag = false;
//		if("".equals(passwordOne)){
//			//密码为空
//		}else if("".equals(passwordTwo)){
//			//验证密码为空
//		}else if("".equals(name)){
//			//姓名为空
//		}else{
//			boolean checkResult = checkSameAsPassword(passwordOne,passwordTwo);
//			if(checkResult){
//				//俩个密码一样
//				flag = true;
//			}else{
//				//俩个密码不一样
//			}
//		}
//		return flag;
//	}



	private void improveUserInfo(final String name, final String description,
			final String headUrl, int userid, String username) {
		engine.ImproveUserInfo(userid,username,name, description, headUrl, new ResultByGetDataListener<Register>() {
			
			@Override
			public void success(ReceiceData<Register> result) {
				if(result.state == 200){
					GlobalParams.user.userCommonInfo = result.infos.userInfo.userCommonInfo;
					Intent intent = new Intent(ct,HomeActivity2.class);
					if(carid == 0){
//						closeLoadingDialog();
						closeLoading1Dialog();
						startActivity(intent);
						finish();
					}else{
//						setloadingViewContent("正在绑定车子");
						setLoadingContent("正在绑定车子");
						bundCar(carid,GlobalParams.user.id);
					}
//					
				}else{
//					showMsgDialog("注册失败"+result.error);
					showErrorDialog("注册失败"+result.error);
				}
				
			}
			
			@Override
			public void error(String error) {
//				showMsgDialog("注册失败，连接不上服务器");
				showErrorDialog("注册失败，连接不上服务器");
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
				closeLoading1Dialog();
				if(result.state == 200){
//					showMsgDialog("绑定成功");
					showErrorDialog("绑定成功");
					Intent intent = new Intent(ct,HomeActivity2.class);
					startActivity(intent);
					finish();
				}else{
					showErrorDialog("绑定失败");
				}
				
			}
			
			@Override
			public void error(String error) {
//				showMsgDialog("连接不上服务器");
				showErrorDialog("连接不上服务器");

				
			}
		});
		
	}
	
	@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
		AppManager.getAppManager().finishActivity(this);
			super.onDestroy();
			
		}

}
