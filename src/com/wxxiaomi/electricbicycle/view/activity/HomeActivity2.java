package com.wxxiaomi.electricbicycle.view.activity;

import java.util.List;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ZoomControls;

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
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.wxxiaomi.electricbicycle.EMHelper;
import com.wxxiaomi.electricbicycle.GlobalParams;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.bean.format.NearByPerson;
import com.wxxiaomi.electricbicycle.bean.format.NearByPerson.UserLocatInfo;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.engine.MapEngineImpl;
import com.wxxiaomi.electricbicycle.engine.common.ResultByGetDataListener;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

/**
 * 主页面
 * 
 * @author Mr.W
 * 
 */
public class HomeActivity2 extends BaseActivity {

	/**
	 * 定位相关
	 */
	LocationClient mLocClient;
	/**
	 * 定位完成后的监听
	 */
	public MyLocationListenner myListener = new MyLocationListenner();
	/**
	 * 定位的模式
	 */
	private LocationMode mCurrentMode;
	// private BitmapDescriptor mCurrentMarker;

	/**
	 * 百度view
	 */
	private MapView mMapView;
	/**
	 * 百度view对应的map空间
	 */
	private BaiduMap mBaiduMap;

	/**
	 * 联系人按钮
	 */
	private Button btn_contact;
//	private Button btn_nav;

	boolean isFirstLoc = true; // 是否首次定位

	private FloatingActionButton btn_go;

	/**
	 * 查询路线 startactivity的statecode
	 */
	// public static int GETROUTERESULT = 11;
	
	MapEngineImpl engine;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_home);
		mCurrentMode = LocationMode.NORMAL;
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.mpaview);
		mBaiduMap = mMapView.getMap();
		btn_contact = (Button) findViewById(R.id.btn_contact);
		btn_contact.setOnClickListener(this);
		btn_go = (FloatingActionButton) findViewById(R.id.btn_go);
		btn_go.setOnClickListener(this);
		setZoomInVis();
	}
	
	private void setZoomInVis(){
		   int childCount = mMapView.getChildCount();
           View zoom = null;
           for (int i = 0; i < childCount; i++) {
                   View child = mMapView.getChildAt(i);
                   if (child instanceof ZoomControls) {
                           zoom = child;
                           break;
                   }
           }
           zoom.setVisibility(View.GONE);
	}

	@Override
	protected void initData() {
		initLocationPars();
	}

	/**
	 * 初始化定位相关的参数
	 */
	private void initLocationPars() {
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
		option.setScanSpan(2000);
		mLocClient.setLocOption(option);
		mLocClient.start();
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
//		RequestQueue mQueue = Volley.newRequestQueue(this); 
//		String url = ConstantValue.SERVER_URL+"ActionServlet?action=getnearby&userid=19"
//				+"&latitude="+latitude
//				+"&longitude="+longitude;
//		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,  
//		        new Response.Listener<JSONObject>() {  
//		            @Override  
//		            public void onResponse(JSONObject response) {  
////		                Log.d("TAG", response.toString());  
//		            	Log.i("wang", " response.toString()="+response.toString());
//		            }  
//		        }, new Response.ErrorListener() {  
//		            @Override  
//		            public void onErrorResponse(VolleyError error) {  
//		                Log.i("wang", error.getMessage(), error);  
//		            }  
//		        });  
//		mQueue.add(jsonObjectRequest);  
//		new AsyncTask<String, Void, ReceiceData<NearByPerson>>() {
//			@Override
//			protected ReceiceData<NearByPerson> doInBackground(String... params) {
//				MapEngineImpl engine = new MapEngineImpl();
//				return engine.getNearByFromServer(latitude, longitude);
//			}
//
//			@Override
//			protected void onPostExecute(ReceiceData<NearByPerson> result) {
//				if (result != null) {
//					if (result.state == 200) {
//						// 新增覆盖物
//						processNearByData(result.infos.userLocatList);
//					} else {
//						Log.i("wang", "登录失败，错误信息：" + result.error);
//					}
//				} else {
//					Log.i("wang", "登录失败，连接不上服务器");
//				}
//			}
//		}.execute();
		engine = new MapEngineImpl(this);
		engine.getNearByFromServer1(latitude, longitude, new ResultByGetDataListener<NearByPerson>() {
			
			@Override
			public void success(ReceiceData<NearByPerson> result) {
				// TODO Auto-generated method stub
				processNearByData(result.infos.userLocatList);
			}
			
			@Override
			public void error(String error) {
				// TODO Auto-generated method stub
//				Log.i("wang", "：" + error);
				showMsgDialog("不能连接服务器");
			}
		});
		
//		engine.getNearByFromServer1(latitude, longitude, new NetListener<NearByPerson>() {
//			
//			@Override
//			public void success(ReceiceData<NearByPerson> data) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void error(String error) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}

	/**
	 * ->服务器
	 * 处理定位返回的结果
	 * @param userLocatList
	 */
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
			// Log.i("wang", "location.getLocType()=" + location.getLocType());
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			// location.
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			// 测试用
			GlobalParams.latitude = latitude;
			GlobalParams.longitude = longitude;
//			Log.i("wang", "HomeAcivity里定位成功:" + latitude + "," + longitude);

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
				// MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				// mBaiduMap.animateMapStatus(u);
				getNearByFromServer(latitude, longitude);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	/**
	 * em收到消息监听
	 */
	EMMessageListener messageListener = new EMMessageListener() {

		@Override
		public void onMessageReceived(List<EMMessage> messages) {
			// 提示新消息
			for (EMMessage message : messages) {
				// DemoHelper.getInstance().getNotifier().onNewMsg(message);
				Log.i("wang", "提示新消息:" + messages.get(0).toString());
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
		EMClient.getInstance().chatManager()
				.addMessageListener(messageListener);
	}

	@Override
	protected void onDestroy() {
		EMClient.getInstance().chatManager()
				.removeMessageListener(messageListener);
		// 退出时销毁定位

		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
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
			break;
		case R.id.btn_go:
			Intent intent2 = new Intent(ct, SearchActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}

	}
}
