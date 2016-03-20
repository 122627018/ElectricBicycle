package com.wxxiaomi.electricbicycle.view.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wxxiaomi.electricbicycle.AppManager;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.bean.Bicycle;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

public class BicycleWelcomeInfoActivity extends BaseActivity {

	private Button btn_bund;
	private Bicycle bike;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_bicycle_welcome);
		AppManager.getAppManager().addActivity(this);
		btn_bund = (Button) findViewById(R.id.btn_bund);
		btn_bund.setOnClickListener(this);
		bike = (Bicycle) getIntent().getExtras().get("value");
	}

	@Override
	protected void initData() {
		bike = (Bicycle) getIntent().getExtras().get("value");
		Log.i("wang", "weclcome中bikeid=" + bike.id);
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_bund:
			Intent intent = new Intent(ct, RegisterOneActivity.class);
			intent.putExtra("carid", bike.id);
			startActivity(intent);
			// finish();
			break;

		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		AppManager.getAppManager().finishActivity(this);
		super.onDestroy();

	}

}
