package com.wxxiaomi.electricbicycle.view.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
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
import com.wxxiaomi.electricbicycle.engine.ImageEngineImpl.HeadImageGetSuccess;
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
	private LocationClient mLocClient;
	private CoordinatorLayout sn_layout;
	/**
	 * 定位完成后的监听
	 */
	public MyLocationListenner myListener = new MyLocationListenner();
	/**
	 * 定位的模式
	 */
	private LocationMode mCurrentMode;

	/**
	 * 百度view
	 */
	private MapView mMapView;
	/**
	 * 百度view对应的map空间
	 */
	private BaiduMap mBaiduMap;

	boolean isFirstLoc = true; // 是否首次定位

	/**
	 * 导航按钮
	 */
	private FloatingActionButton btn_go;

	/**
	 * 地图引擎，负责与服务器打交道的工具
	 */
	private MapEngineImpl engine;
	/**
	 * 附近的人列表
	 */
	private List<UserLocatInfo> userLocatList;

	/**
	 * 附近的人的控件上的名字控件
	 */
	// private TextView tv_near_name;
	/**
	 * 当前所点击的附近的人的信息
	 */
	private UserCommonInfo currentNearPerson;

	/**
	 * 附近的人的view
	 */
	// private LinearLayout rl_nearby_view;
	private LinearLayout ll_nearby_view;
	/**
	 * 附近的人的头像view
	 */
	// private CircularImageView iv_head;
	/**
	 * 附近的人的控件上的添加按钮
	 */
	// private ImageView iv_near_add;
	/*
	 * 附近的人的控件上的删除按钮
	 */
	// private ImageView iv_near_cancle;
	/**
	 * 用于装附近的人view的一个容器
	 */
	/**
	 * 图片引擎
	 */
	private ImageEngineImpl imageEngine;

	private ImageView iv_contact;
	private TextView tv_name;

	/**
	 * 附近的人view显示位移动画
	 */
	private TranslateAnimation mShowAction;
	/**
	 * 附近的人view隐藏位移动画
	 */
	private TranslateAnimation mHiddenAction;
	/**
	 * 当前附近的人infoView是否可见
	 */
	private boolean isNearViewVis = false;
	
	private CircularImageView near_iv_head;
	private TextView tv_near_name;
	private TextView tv_near_description;

	@SuppressLint("InflateParams")
	@Override
	protected void initView() {
		setContentView(R.layout.activity_home);
		AppManager.getAppManager().finishAllActivity();
		sn_layout = (CoordinatorLayout) findViewById(R.id.sn_layout);
		ll_nearby_view = (LinearLayout) findViewById(R.id.ll_nearby_view);
		near_iv_head = (CircularImageView) ll_nearby_view.findViewById(R.id.near_iv_head);
		tv_near_name = (TextView) ll_nearby_view.findViewById(R.id.tv_near_name);
		tv_near_description = (TextView) ll_nearby_view.findViewById(R.id.tv_near_description);
		mMapView = (MapView) findViewById(R.id.mpaview);
		iv_contact = (ImageView) findViewById(R.id.iv_contact);
		tv_name = (TextView) findViewById(R.id.tv_name);
		btn_go = (FloatingActionButton) findViewById(R.id.btn_go);
		mCurrentMode = LocationMode.NORMAL;
		mBaiduMap = mMapView.getMap();
		btn_go.setOnClickListener(this);
		iv_contact.setOnClickListener(this);

	}

	/**
	 * 把地图的缩放控件隐藏
	 */
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

	/**
	 * 显示SnackBar
	 * 
	 * @param content
	 */
	private void showSnackBar(String content) {
		Snackbar.make(sn_layout, content, Snackbar.LENGTH_LONG).show();
	}

	@Override
	protected void initData() {
		initAnimation();
		setZoomInVis();
		imageEngine = new ImageEngineImpl(ct);
		initLocationPars();
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				mBaiduMap.hideInfoWindow();
				setNearPersonViewHid(false);
			}
		});
		initMapMarkerClickListener();
		tv_name.setText(GlobalParams.user.userCommonInfo.name);
	}

	/**
	 * 初始化动画
	 */
	private void initAnimation() {
		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);
		mShowAction.setInterpolator(new OvershootInterpolator());
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				1.0f);
		mHiddenAction.setDuration(500);
		mHiddenAction.setInterpolator(new OvershootInterpolator());
		

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
						showErrorDialog("不能连接服务器");
					}
				});
	}

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
				.fromResource(R.drawable.ic_person_pin_black_36dp);

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

	}

	/**
	 * 初始化百度地图上的覆盖物的点击事件
	 */
	private void initMapMarkerClickListener() {

		// 附近的人marker点击事件
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(final Marker marker) {
				onNearMarkerClick(marker, userLocatList.get(marker.getZIndex()).userCommonInfo);
				return true;
			}
		});
		// // 附件的人view的添加按钮的点击事件
		// iv_near_add.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // Log.i("wang", "添加" + currentNearPerson.name + "为好友");
		//
		// }
		// });
		// // 附近的人view的删除按钮的点击事件
		// iv_near_cancle.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// currentNearPerson = null;
		// mBaiduMap.hideInfoWindow();
		// }
		// });

	}

	/**
	 * 相应附近的人marker的点击事件
	 * 
	 * @param marker
	 * @param currentNearPerson2
	 */
	private void onNearMarkerClick(final Marker marker,
			final UserCommonInfo currentNearPerson2) {
		Log.i("wang", "currentNearPerson2.name="+currentNearPerson2.name);
		if(isNearViewVis){
			//view可见
			mHiddenAction.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {}
				@Override
				public void onAnimationRepeat(Animation animation) {}
				@Override
				public void onAnimationEnd(Animation animation) {
//					Log.i("wang", "currentNearPerson.name="+currentNearPerson.name);
//					if(currentNearPerson != currentNearPerson2){
						imageEngine.getHeadImageByUrl(near_iv_head, currentNearPerson2.head);
						tv_near_name.setText(currentNearPerson2.name);
						tv_near_description.setText("生活就像海洋,只有意志坚定的人才能到彼岸");
//					}
					setNearPersonViewVis();
				}
			});
			setNearPersonViewHid(true);
		}else{
			//view不可见
//			Log.i("wang", "currentNearPerson.name="+currentNearPerson.name);
			if(currentNearPerson != currentNearPerson2){
				imageEngine.getHeadImageByUrl(near_iv_head, currentNearPerson2.head);
				tv_near_name.setText(currentNearPerson2.name);
				tv_near_description.setText("生活就像海洋,只有意志坚定的人才能到彼岸");
			}
			setNearPersonViewVis();
		}
		currentNearPerson = currentNearPerson2;
		// tv_near_name.setText(currentNearPerson.name);
		// imageEngine.getHeadImageBySimple(iv_head, currentNearPerson.head,
		// new HeadImageGetSuccess() {
		// @Override
		// public void success(Bitmap arg0) {
		// LatLng ll = marker.getPosition();
		// mInfoWindow = new InfoWindow(rl_nearby_view, ll, -47);
		// mBaiduMap.showInfoWindow(mInfoWindow);
		// }
		// });
		// LatLng ll = marker.getPosition();
		// mInfoWindow = new InfoWindow(rl_nearby_view, ll, -47);
		// mBaiduMap.showInfoWindow(mInfoWindow);
	}


	/**
	 * 设置最近的人的信息view可见
	 */
	private void setNearPersonViewVis() {
		ll_nearby_view.clearAnimation();
		ll_nearby_view.setAnimation(mShowAction);
		ll_nearby_view.startAnimation(mShowAction);
		ll_nearby_view.setVisibility(View.VISIBLE);
		isNearViewVis = !isNearViewVis;
	}

	/**
	 * 设置最近的人信息view不可见
	 */
	private void setNearPersonViewHid(boolean needShow) {
		if (isNearViewVis) {
			if(!needShow){
				mHiddenAction.setAnimationListener(null);
			}
			ll_nearby_view.clearAnimation();
			ll_nearby_view.setAnimation(mHiddenAction);
			ll_nearby_view.startAnimation(mHiddenAction);
			ll_nearby_view.setVisibility(View.GONE);
			isNearViewVis = !isNearViewVis;
		}
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
		// case R.id.btn_contact:
		// // 联系人
		// Intent intent = new Intent(ct, ContactActivity.class);
		// startActivity(intent);
		// break;
		case R.id.btn_nav:
			break;
		case R.id.btn_go:
			Intent intent2 = new Intent(ct, SearchActivity.class);
			startActivity(intent2);
			break;
		case R.id.iv_contact:
			showSnackBar("beta版本不包含此功能");
		default:
			break;
		}

	}
	
	
}
