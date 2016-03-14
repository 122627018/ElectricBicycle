package com.wxxiaomi.electricbicycle.view.activity;

import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

/**
 * 路线展示activity
 * @author Mr.W
 *
 */
public class RoutePlanActivity extends BaseActivity {

	 MapView mMapView = null;    // 地图View
	 BaiduMap mBaidumap = null;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_routeplan);
		mMapView = (MapView) findViewById(R.id.map);
        mBaidumap = mMapView.getMap();
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
