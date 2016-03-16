package com.wxxiaomi.electricbicycle.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BikingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManager.NaviInitListener;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.wxxiaomi.electricbicycle.GlobalParams;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

/**
 * 路线展示activity
 * 
 * @author Mr.W
 * 
 */
public class RoutePlanActivity extends BaseActivity implements OnGetRoutePlanResultListener {

	private FloatingActionButton btn_nav;
	RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用

	MapView mMapView = null; // 地图View
	BaiduMap mBaidumap = null;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_routeplan);
		mMapView = (MapView) findViewById(R.id.map);
		mBaidumap = mMapView.getMap();
		btn_nav = (FloatingActionButton) findViewById(R.id.btn_nav);
		btn_nav.setOnClickListener(this);
		mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        showLoadingDialog("正在加载路线");
	}

	@Override
	protected void initData() {
		initNav();
		PlanNode enNode = PlanNode.withLocation(GlobalParams.poiInf.location);
		PlanNode sNode = PlanNode.withLocation(new LatLng(GlobalParams.latitude, GlobalParams.longitude));
		mSearch.bikingSearch((new BikingRoutePlanOption())
                .from(sNode).to(enNode));
		closeLoadingDialog();
	}



	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_nav:
			// 开始导航
			routeplanToNavi(CoordinateType.BD09LL);
			break;

		default:
			break;
		}

	}
	
	 private void initNav() {
		 BaiduNaviManager.getInstance().init(this, getSdcardDir(), "BNSDKSimpleDemo", new NaviInitListener() {
				@Override
				public void onAuthResult(int status, String msg) {
					if (0 == status) {
//						authinfo = "key校验成功!";
						Log.i("wang", "key校验成功");
					} else {
//						authinfo = "key校验失败, " + msg;
						Log.i("wang", "key校验失敗:" + msg);
					}
//					BNDemoMainActivity.this.runOnUiThread(new Runnable() {
	//
//						@Override
//						public void run() {
//							Toast.makeText(BNDemoMainActivity.this, authinfo, Toast.LENGTH_LONG).show();
//						}
//					});
				}

				public void initSuccess() {
//					Toast.makeText(BNDemoMainActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
//					initSetting();
					Log.i("wang", "百度导航引擎初始化成功");
					initSetting();
				}

				public void initStart() {
					Log.i("wang", "百度导航引擎初始化开始");
//					Toast.makeText(BNDemoMainActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
				}

				public void initFailed() {
					Log.i("wang", "百度导航引擎初始化失败");
//					Toast.makeText(BNDemoMainActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
				}
			},  null, null, null);
	}
	 
	 protected void initSetting() {
		 BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
		    BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
		    BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);	    
	        BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
	        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
		
	}
	 
	 
	 private String getSdcardDir() {
			if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
				return Environment.getExternalStorageDirectory().toString();
			}
			return null;
		}

	/**
	 * 开始导航
	 * @param bd09ll
	 */
	private void routeplanToNavi(CoordinateType bd09ll) {
		BNRoutePlanNode sNode = new BNRoutePlanNode(GlobalParams.longitude,
				GlobalParams.latitude, "起点", null,bd09ll);
		BNRoutePlanNode eNode = new BNRoutePlanNode(
				GlobalParams.poiInf.location.longitude,
				GlobalParams.poiInf.location.latitude, "终点", null,
				bd09ll);
		// 起始点和终点俩个点
		List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
		list.add(sNode);
		list.add(eNode);
		// 主要代码
		BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true,
				new DemoRoutePlanListener(sNode));
	}
	
	public class DemoRoutePlanListener implements RoutePlanListener {
		private BNRoutePlanNode mBNRoutePlanNode = null;

		public DemoRoutePlanListener(BNRoutePlanNode node) {
			mBNRoutePlanNode = node;
		}

		@Override
		public void onJumpToNavigator() {
			/*
			 * 设置途径点以及resetEndNode会回调该接口
			 */
			// for (Activity ac : activityList) {
			// if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {
			// return;
			// }
			// }
//			Log.i("wang", "onJumpToNavigator()");
			Intent intent = new Intent(ct, BaiduGuideActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("routePlanNode",
					(BNRoutePlanNode) mBNRoutePlanNode);
			intent.putExtras(bundle);
			startActivity(intent);
		}

		@Override
		public void onRoutePlanFailed() {
			// TODO Auto-generated method stub
			// Toast.makeText(BNDemoMainActivity.this, "算路失败",
			// Toast.LENGTH_SHORT).show();
			Log.i("wang", "onRoutePlanFailed()->" + "算路失败");
		}
	}
	
	
	
	

	@Override
	public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
		if (bikingRouteResult == null || bikingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(RoutePlanDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
		 Log.i("wang", "抱歉，未找到结果");
        }
        if (bikingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            // result.getSuggestAddrInfo()
            return;
        }
        if (bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
//            nodeIndex = -1;
//            mBtnPre.setVisibility(View.VISIBLE);
//            mBtnNext.setVisibility(View.VISIBLE);
//            route = bikingRouteResult.getRouteLines().get(0);
            BikingRouteOverlay overlay = new MyBikingRouteOverlay(mBaidumap);
//            routeOverlay = overlay;
            mBaidumap.setOnMarkerClickListener(overlay);
            overlay.setData(bikingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	private class MyBikingRouteOverlay extends BikingRouteOverlay {
        public  MyBikingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @SuppressWarnings("unused")
        @Override
        public BitmapDescriptor getStartMarker() {
            if (true) {
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
            }
            return null;
        }

        @SuppressWarnings("unused")
		@Override
        public BitmapDescriptor getTerminalMarker() {
            if (true) {
                return BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
            }
            return null;
        }


    }

}
