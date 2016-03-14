package com.wxxiaomi.electricbicycle.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

/**
 * 扫码界面
 * @author Mr.W
 *
 */
public class ScanCodeActivity extends BaseActivity {

	/**
	 * 模拟扫描一辆未被注册过的车
	 */
//	private Button btn_unregister;
	/**
	 * 模拟扫描一辆已经被注册过的车
	 */
//	private Button btn_register;
	/**
	 * 已有帐号?直接登陆
	 */
	private Button btn_login;
	
	/**
	 * 车辆id
	 */
	private EditText et_carid;
	
	/**
	 * 扫码操作
	 */
	private Button btn_scan;
	
	private Button btn_debug;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_scancode);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		et_carid = (EditText) findViewById(R.id.et_carid);
		btn_scan = (Button) findViewById(R.id.btn_scan);
		btn_scan.setOnClickListener(this);
		btn_debug = (Button) findViewById(R.id.btn_debug);
		btn_debug.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			//已有帐号?直接登陆
			Intent intent = new Intent(ct,LoginActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_scan:
			String carid = et_carid.getText().toString().trim();
			simulationScan(carid);
			//模拟扫描
		case R.id.btn_debug:
			Intent intent1 = new Intent(ct, HomeActivity2.class);
			startActivity(intent1);
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 模拟扫描
	 * @param carid 车辆id
	 */
	private void simulationScan(String carid) {
		//连接服务器
		
	}

}
