package com.wxxiaomi.electricbicycle.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wxxiaomi.electricbicycle.AppManager;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.bean.Bicycle;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.engine.BicycleEngineImpl;
import com.wxxiaomi.electricbicycle.engine.common.ResultByGetDataListener;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {

	private Button btn_scan;
	private Button btn_login;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private BicycleEngineImpl engine;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_welcome);
		AppManager.getAppManager().addActivity(this);
		btn_scan = (Button) findViewById(R.id.btn_scan);
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		btn_scan.setOnClickListener(this);

	}

	@Override
	protected void initData() {
		engine = new BicycleEngineImpl(ct);
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_scan:
			Intent intent = new Intent();
			intent.setClass(ct, ScanCodeActivity1.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
			break;
		case R.id.btn_login:
			Intent intent2 = new Intent(ct, LoginActivity.class);
			startActivity(intent2);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				// 显示扫描到的内容
				// et_device_number.setText(bundle.getString("result"));
				String scanResult = bundle.getString("result");
				Log.i("wang", "扫描到的内容:" + scanResult);
				getBicycleInfo(scanResult);
			}
			break;

		}
	}

	/**
	 * 根据扫描到的连接获取车辆信息
	 * 
	 * @param scanResult
	 */
	private void getBicycleInfo(String scanResult) {
		try {
			int cardid = Integer.valueOf(scanResult);
			if ((cardid < 6)) {
				showLoading2Dialog(this, "正在获取车辆信息");
				// 连接服务器
				engine.getBicycleInfo(scanResult,
						new ResultByGetDataListener<Bicycle>() {

							@Override
							public void success(ReceiceData<Bicycle> result) {
								closeLoading1Dialog();
								if (result.state == 200) {
									Intent intent = new Intent(ct,
											BicycleWelcomeInfoActivity.class);
									intent.putExtra("value", result.infos);
									startActivity(intent);
								} else {
									showErrorDialog(result.error);
								}
							}

							@Override
							public void error(String error) {
								closeLoading1Dialog();
								showErrorDialog("连接服务器失败");
							}
						});
			}else{
				showErrorDialog("非法二维码");
			}
		} catch (Exception e) {
			showErrorDialog("非法二维码");
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		AppManager.getAppManager().finishActivity(this);
		super.onDestroy();

	}
}
