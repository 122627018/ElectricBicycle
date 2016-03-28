package com.wxxiaomi.electricbicycle.view.activity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

public class UserInfoActivity extends BaseActivity {

	private Toolbar toolbar;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_userinfo);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		// 标题的文字需在setSupportActionBar之前，不然会无效
		toolbar.setTitle("王浩明");
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true); // 设置返回键可用
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub

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
