package com.wxxiaomi.electricbicycle.view.activity;

import java.util.List;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup.OnCheckedChangeListener;

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
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MarkerOptions.MarkerAnimateType;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.bean.format.NearByPerson;
import com.wxxiaomi.electricbicycle.bean.format.NearByPerson.UserLocatInfo;
import com.wxxiaomi.electricbicycle.bean.format.common.ReceiceData;
import com.wxxiaomi.electricbicycle.engine.MapEngineImpl;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

public class HomeActivity2 extends BaseActivity {

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;
//	private static final int accuracyCircleFillColor = 0xAAFFFF88;
//	private static final int accuracyCircleStrokeColor = 0xAA00FF00;

	MapView mMapView;
	BaiduMap mBaiduMap;

	// UI相关
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true; // 是否首次定位

	@Override
	protected void initView() {
		setContentView(R.layout.activity_home);
		mCurrentMode = LocationMode.NORMAL;
	
	}

	@Override
	protected void initData() {
		// 地图初始化
		mMapView = (MapView) findViewById(R.id.mpaview);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		/**
		 * mode - 定位图层显示方式, 默认为 LocationMode.NORMAL 普通态
enableDirection - 是否允许显示方向信息
customMarker - 设置用户自定义定位图标，可以为 null

		 */
		mBaiduMap
        .setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, null));
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		Log.i("wang", "mLocClient.start()的上面");
		mLocClient.start();
		
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub

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

//	privaye List<Marker>
//	private Marker mMarkerA;
	protected void processNearByData(List<UserLocatInfo> userLocatList) {
		Marker mMarker;
		// 初始化全局 bitmap 信息，不用时及时 recycle
		BitmapDescriptor bdA = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_marka);
		for(UserLocatInfo user : userLocatList){
			LatLng point = new LatLng(user.locat[0], user.locat[1]);
			//构建MarkerOption，用于在地图上添加Marker  
			MarkerOptions ooA = new MarkerOptions().position(point).icon(bdA)
					.zIndex(9).draggable(true);
			ooA.animateType(MarkerAnimateType.drop);
			mMarker = (Marker) (mBaiduMap.addOverlay(ooA));
			mMarker.setZIndex(user.userCommonInfo.userid);
		}
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
//				Log.i("wang", "点击了：id="+marker.getZIndex());
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
			Log.i("wang", "location.getLocType()="+location.getLocType());
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(0)
//					.accuracy(location.getRadius())
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
				getNearByFromServer(latitude,longitude);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;
		super.onDestroy();
	}

}
