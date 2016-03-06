package com.wxxiaomi.electricbicycle.view.activity;

import android.view.View;
import android.widget.ListView;

import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

/**
 * 联系人activity
 * @author Administrator
 *
 */
public class ContactActivity extends BaseActivity{

	private ListView lv_listview;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_contact);
		lv_listview = (ListView) findViewById(R.id.lv_listview);
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
