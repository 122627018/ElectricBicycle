package com.wxxiaomi.electricbicycle.view.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BikingRouteOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
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
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.wxxiaomi.electricbicycle.EMHelper;
import com.wxxiaomi.electricbicycle.GlobalParams;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.bean.format.NearByPerson;
import com.wxxiaomi.electricbicycle.bean.format.NearByPerson.UserLocatInfo;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.engine.MapEngineImpl;
import com.wxxiaomi.electricbicycle.view.activity.BaiduNavActivity.DemoRoutePlanListener;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

/**
 * 主页面
 * @author Mr.W
 *
 */
public class HomeActivity2 extends BaseActivity implements OnGetRoutePlanResultListener {

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	// private BitmapDescriptor mCurrentMarker;

	private MapView mMapView;
	private BaiduMap mBaiduMap;

	// UI相关
	private Button btn_contact;
	private Button btn_nav;

	boolean isFirstLoc = true; // 是否首次定位
	
	private Button btn_go;
	/**
	 * 查询路线 
	 * startactivity的statecode
	 */
	public static int GETROUTERESULT = 11; 

	@Override
	protected void initView() {
		setContentView(R.layout.activity_home);
		mCurrentMode = LocationMode.NORMAL;
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.mpaview);
		mBaiduMap = mMapView.getMap();
		btn_contact = (Button) findViewById(R.id.btn_contact);
		btn_contact.setOnClickListener(this);
		btn_nav = (Button) findViewById(R.id.btn_nav);
		btn_nav.setOnClickListener(this);
		btn_go = (Button) findViewById(R.id.btn_go);
		btn_go.setOnClickListener(this);
		mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
	}

	@Override
	protected void initData() {
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		/**
		 * mode - 定位图层显示方式, 默认为 LocationMode.NORMAL 普通态 enableDirection -
		 * 是否允许显示方向信息 customMarker - 设置用户自定义定位图标，可以为 null
		 */
		mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
				mCurrentMode, true, null));
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		initNav();
	}

	protected void initSetting() {
		 BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
		    BNaviSettingManager.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
		    BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);	    
	        BNaviSettingManager.setPowerSaveMode(BNaviSettingManager.PowerSaveMode.DISABLE_MODE);
	        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
		
	}
	
	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_contact:
			// 联系人
			Intent intent = new Intent(ct, ContactActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_nav:
			//根据当前路线进入导航页面
			
			routeplanToNavi(CoordinateType.BD09LL);
//			Intent intent1 = new Intent(ct,BaiduNavActivity.class);
//			startActivity(intent1);
			break;
		case R.id.btn_go:
			Intent intent2 = new Intent(ct,SearchActivity.class);
//			intent2.putExtra("sd", HomeActivity2.this);
////			startActivity(intent2);
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("",  HomeActivity2.this);
			startActivityForResult(intent2, GETROUTERESULT);
			break;
		default:
			break;
		}

	}

	private void routeplanToNavi(CoordinateType wgs84) {
		//算路设置起、终点，算路偏好，是否模拟导航等参数
		BNRoutePlanNode sNode;
		BNRoutePlanNode eNode;
		sNode = new BNRoutePlanNode( GlobalParams.longitude,GlobalParams.latitude, "嘉应学院", null, wgs84);
		//24.2726780000,116.0893520000
		eNode = new BNRoutePlanNode( GlobalParams.poiInf.location.longitude,GlobalParams.poiInf.location.latitude, "终点", null, wgs84);
		Log.i("wang", "导航的方法中routeplanToNavi->起点:"+GlobalParams.longitude+","+GlobalParams.latitude);
		Log.i("wang", "导航的方法中routeplanToNavi->终点:"+GlobalParams.poiInf.location.latitude+","+GlobalParams.poiInf.location.longitude);
		//起始点和终点俩个点
		List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
		list.add(sNode);
		list.add(eNode);
				//主要代码
		BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
		
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
//			for (Activity ac : activityList) {
//				if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {
//					return;
//				}
//			}
			Log.i("wang", "onJumpToNavigator()");
			Intent intent = new Intent(ct, BaiduGuideActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("routePlanNode", (BNRoutePlanNode) mBNRoutePlanNode);
			intent.putExtras(bundle);
			startActivity(intent);	
		}
		@Override
		public void onRoutePlanFailed() {
			// TODO Auto-generated method stub
//			Toast.makeText(BNDemoMainActivity.this, "算路失败", Toast.LENGTH_SHORT).show();
			Log.i("wang", "onRoutePlanFailed()->"+"算路失败");
		}
	}

	/**
	 * 获取附近的人 发送自己的坐标给服务器 服务器回送附近的人
	 * 
	 * @param latitude
	 *            经度
	 * @param longitude
	 *            纬度
	 */
	private void getNearByFromServer(final double latitude,
			final double longitude) {
		new AsyncTask<String, Void, ReceiceData<NearByPerson>>() {
			@Override
			protected ReceiceData<NearByPerson> doInBackground(String... params) {
				MapEngineImpl engine = new MapEngineImpl();
				return engine.getNearByFromServer(latitude, longitude);
			}

			@Override
			protected void onPostExecute(ReceiceData<NearByPerson> result) {
				if (result != null) {
					if (result.state == 200) {
						// 新增覆盖物
						processNearByData(result.infos.userLocatList);
					} else {
						Log.i("wang", "登录失败，错误信息：" + result.error);
					}
				} else {
					Log.i("wang", "登录失败，连接不上服务器");
				}
			}
		}.execute();

	}

	// privaye List<Marker>
	// private Marker mMarkerA;
	protected void processNearByData(List<UserLocatInfo> userLocatList) {
		Marker mMarker;
		// 初始化全局 bitmap 信息，不用时及时 recycle
		BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka);
		for (UserLocatInfo user : userLocatList) {
			LatLng point = new LatLng(user.locat[0], user.locat[1]);
			// 构建MarkerOption，用于在地图上添加Marker
			MarkerOptions ooA = new MarkerOptions().position(point).icon(bdA)
					.zIndex(9).draggable(true);
			ooA.animateType(MarkerAnimateType.drop);
			mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
			mMarker.setZIndex(user.userCommonInfo.userid);
		}
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				// Log.i("wang", "点击了：id="+marker.getZIndex());
				return false;
			}
		});

	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
//			Log.i("wang", "location.getLocType()=" + location.getLocType());
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
//			location.
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			//测试用
			GlobalParams.latitude = latitude;
			GlobalParams.longitude = longitude;
			
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(0)
					// .accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatus.Builder builder = new MapStatus.Builder();
				builder.target(ll).zoom(18.0f);
				mBaiduMap.animateMapStatus(MapStatusUpdateFactory
						.newMapStatus(builder.build()));
				getNearByFromServer(latitude, longitude);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}
	
	

	EMMessageListener messageListener = new EMMessageListener() {

		@Override
		public void onMessageReceived(List<EMMessage> messages) {
			// 提示新消息
			for (EMMessage message : messages) {
				// DemoHelper.getInstance().getNotifier().onNewMsg(message);
				Log.i("wang", "提示新消息:"+messages.get(0).toString());
				EMHelper.getInstance().getNotifier().onNewMsg(message);
			}
			// refreshUIWithMessage();
		}

		@Override
		public void onCmdMessageReceived(List<EMMessage> messages) {
		}

		@Override
		public void onMessageReadAckReceived(List<EMMessage> messages) {
		}

		@Override
		public void onMessageDeliveryAckReceived(List<EMMessage> message) {
		}

		@Override
		public void onMessageChanged(EMMessage message, Object change) {
		}
	};

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
		EMClient.getInstance().chatManager().addMessageListener(messageListener);
	}

	@Override
	protected void onDestroy() {
		EMClient.getInstance().chatManager().removeMessageListener(messageListener);
		// 退出时销毁定位
		
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
		   case 11:
//			   PoiInfo poiInfo = (PoiInfo) data.getBundleExtra("value").get("route"); //data为B中回传的Intent
			   Log.i("wang", "GlobalParams.poiInf="+GlobalParams.poiInf.toString());
			   processGoResult(GlobalParams.poiInf.location);
		    break;
		default:
		    break;
		    }
		}

	 RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
	
	/**
	 * 处理搜索结果
	 * @param location
	 */
	private void processGoResult(LatLng location) {
		
		PlanNode enNode = PlanNode.withLocation(location);
		PlanNode sNode = PlanNode.withLocation(new LatLng(GlobalParams.latitude, GlobalParams.longitude));
		mSearch.bikingSearch((new BikingRoutePlanOption())
                .from(sNode).to(enNode));
	}
	
	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
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

	private class MyBikingRouteOverlay extends BikingRouteOverlay {
	        public  MyBikingRouteOverlay(BaiduMap baiduMap) {
	            super(baiduMap);
	        }

	        @Override
	        public BitmapDescriptor getStartMarker() {
	            if (true) {
	                return BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
	            }
	            return null;
	        }

	        @Override
	        public BitmapDescriptor getTerminalMarker() {
	            if (true) {
	                return BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher);
	            }
	            return null;
	        }


	    }

	@Override
	public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
		 if (bikingRouteResult == null || bikingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
//	            Toast.makeText(RoutePlanDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
			 Log.i("wang", "抱歉，未找到结果");
	        }
	        if (bikingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
	            // result.getSuggestAddrInfo()
	            return;
	        }
	        if (bikingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
//	            nodeIndex = -1;
//	            mBtnPre.setVisibility(View.VISIBLE);
//	            mBtnNext.setVisibility(View.VISIBLE);
//	            route = bikingRouteResult.getRouteLines().get(0);
	            BikingRouteOverlay overlay = new MyBikingRouteOverlay(mBaiduMap);
//	            routeOverlay = overlay;
	            mBaiduMap.setOnMarkerClickListener(overlay);
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
	}
	
