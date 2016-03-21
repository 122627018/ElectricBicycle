package com.wxxiaomi.electricbicycle.view.activity;

import java.util.List;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
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
import com.wxxiaomi.electricbicycle.AppManager;
import com.wxxiaomi.electricbicycle.EMHelper;
import com.wxxiaomi.electricbicycle.GlobalParams;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.bean.User.UserCommonInfo;
import com.wxxiaomi.electricbicycle.bean.format.NearByPerson;
import com.wxxiaomi.electricbicycle.bean.format.NearByPerson.UserLocatInfo;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.engine.ImageEngineImpl;
import com.wxxiaomi.electricbicycle.engine.MapEngineImpl;
import com.wxxiaomi.electricbicycle.engine.common.ResultByGetDataListener;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;
import com.wxxiaomi.electricbicycle.view.custom.CircularImageView;

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
	// private Button btn_nav;

	boolean isFirstLoc = true; // 是否首次定位

	private FloatingActionButton btn_go;

	/**
	 * 查询路线 startactivity的statecode
	 */
	// public static int GETROUTERESULT = 11;

	MapEngineImpl engine;
	private CircularImageView iv_head;

	private LinearLayout ll_nearbyview;
	private TextView tv_near_name;
	private UserCommonInfo currentNearPerson;

	private ImageView iv_near_add;
	private ImageView iv_near_cancle;
	private TranslateAnimation mShowAction;
	private TranslateAnimation mHiddenAction;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_home);
		AppManager.getAppManager().finishAllActivity();
		mCurrentMode = LocationMode.NORMAL;
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.mpaview);
		mBaiduMap = mMapView.getMap();
		btn_contact = (Button) findViewById(R.id.btn_contact);
		btn_contact.setOnClickListener(this);
		btn_go = (FloatingActionButton) findViewById(R.id.btn_go);
		btn_go.setOnClickListener(this);
		ll_nearbyview = (LinearLayout) findViewById(R.id.ll_nearbyview);
		tv_near_name = (TextView) findViewById(R.id.tv_near_name);
		iv_near_add = (ImageView) findViewById(R.id.iv_near_add);
		iv_near_cancle = (ImageView) findViewById(R.id.iv_near_cancle);
		iv_head = (CircularImageView) findViewById(R.id.iv_head);
		setZoomInVis();
		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f);
		mHiddenAction.setDuration(500);
		
		
	}

	private void setNearByViewGone(){
		ll_nearbyview.clearAnimation();
		ll_nearbyview.setAnimation(mHiddenAction);
		ll_nearbyview.startAnimation(mHiddenAction);
		ll_nearbyview.setVisibility(View.INVISIBLE);
	}

	private void setNearByViewVis() {
		ll_nearbyview.clearAnimation();
		ll_nearbyview.setAnimation(mShowAction);
		ll_nearbyview.startAnimation(mShowAction);
		ll_nearbyview.setVisibility(View.VISIBLE);
	}

	private void setZoomInVis() {
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
		imageEngine = new ImageEngineImpl(ct);
		initLocationPars();
		// mBaiduMap.setOnMapTouchListener(new OnMapTouchListener() {
		// @Override
		// public void onTouch(MotionEvent arg0) {
		// ll_nearbyview.setVisibility(View.GONE);
		// arg0.
		// }
		// });
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
//				ll_nearbyview.setVisibility(View.GONE);
				setNearByViewGone();

			}
		});
		iv_near_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.i("wang", "添加" + currentNearPerson.name + "为好友");

			}
		});
		iv_near_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				ll_nearbyview.setVisibility(View.GONE);
				currentNearPerson = null;
				setNearByViewGone();

			}
		});
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
		engine = new MapEngineImpl(this);
		engine.getNearByFromServer1(latitude, longitude,
				new ResultByGetDataListener<NearByPerson>() {

					@Override
					public void success(ReceiceData<NearByPerson> result) {
						// TODO Auto-generated method stub
						processNearByData(result.infos.userLocatList);
					}

					@Override
					public void error(String error) {
						// TODO Auto-generated method stub
						// Log.i("wang", "：" + error);
						// showMsgDialog("不能连接服务器");
						showErrorDialog("不能连接服务器");
					}
				});
	}

	List<UserLocatInfo> userLocatList;

	/**
	 * ->服务器 处理定位返回的结果
	 * 
	 * @param userLocatList
	 */
	protected void processNearByData(List<UserLocatInfo> list) {
		this.userLocatList = list;
		Marker mMarker;
		// 初始化全局 bitmap 信息，不用时及时 recycle
		BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(R.drawable.track_nav_statistic_icon);
		for (int i = 0; i < list.size(); i++) {
			UserLocatInfo user = list.get(i);
			LatLng point = new LatLng(user.locat[0], user.locat[1]);
			// 构建MarkerOption，用于在地图上添加Marker
			MarkerOptions ooA = new MarkerOptions().position(point).icon(bdA)
					.zIndex(9).draggable(true);
			ooA.animateType(MarkerAnimateType.drop);
			mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
			mMarker.setZIndex(i);
		}
		// for (UserLocatInfo user : userLocatList) {
		//
		// }
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				if (currentNearPerson == userLocatList.get(marker.getZIndex()).userCommonInfo) {
					return false;
				} else {
					currentNearPerson = userLocatList.get(marker.getZIndex()).userCommonInfo;
					showNearUserInfo(currentNearPerson);
					return false;
				}
			}
		});

	}

	ImageEngineImpl imageEngine;

	protected void showNearUserInfo(UserCommonInfo userCommonInfo) {
		if(ll_nearbyview.getVisibility() != View.VISIBLE){
			setNearByViewVis();
		}
		tv_near_name.setText(userCommonInfo.name);
//		Log.i("wang", "userCommonInfo.head=" + userCommonInfo.head);
		imageEngine.getHeadImageByUrl(iv_head, userCommonInfo.head);
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
			// Log.i("wang", "HomeAcivity里定位成功:" + latitude + "," + longitude);

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
