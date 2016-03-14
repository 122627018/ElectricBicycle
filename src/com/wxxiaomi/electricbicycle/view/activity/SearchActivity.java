package com.wxxiaomi.electricbicycle.view.activity;

import java.io.Serializable;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.wxxiaomi.electricbicycle.GlobalParams;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;

/**
 * 搜索地点页面
 * @author Mr.W
 *
 */
public class SearchActivity extends BaseActivity implements OnGetSuggestionResultListener{

	private AutoCompleteTextView et_endlocation;
	private Button btn_ok;
	
	private ArrayAdapter<String> sugAdapter = null;
	
	private SuggestionSearch mSuggestionSearch = null;
	private PoiSearch mPoiSearch = null;
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_search);
		et_endlocation = (AutoCompleteTextView) findViewById(R.id.et_endlocation);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(this);
		mPoiSearch = PoiSearch.newInstance();
		
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		sugAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
		et_endlocation.setAdapter(sugAdapter);
	}

	@Override
	protected void initData() {
		et_endlocation.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() <= 0) {
					return;
				}
//				String city = ((EditText) findViewById(R.id.city)).getText()
//						.toString();
				String city = "梅州";
				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(city));
			}
		});
		
		
		mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
			
			@Override
			public void onGetPoiResult(PoiResult result) {
				if (result == null
						|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
//					Toast.makeText(PoiSearchDemo.this, "未找到结果", Toast.LENGTH_LONG)
//					.show();
//					return;
				}
				if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//					mBaiduMap.clear();
//					PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
//					mBaiduMap.setOnMarkerClickListener(overlay);
//					overlay.setData(result);
//					overlay.addToMap();
//					overlay.zoomToSpan();
//					Intent intent = getIntent();
////					intent.pute
//					Bundle bundle = new Bundle();
//					bundle.putSerializable("route", (Serializable) result.getAllPoi().get(0));
//					Log.i("wang", "result.getAllPoi().size()="+result.getAllPoi().size());
//					
					Log.i("wang", "address="+result.getAllPoi().get(0).address);
					Log.i("wang", "result.getAllPoi().get(0).location="+result.getAllPoi().get(0).location);
//					intent.putExtra("value", bundle);
//					setResult(11, intent);
					GlobalParams.poiInf = result.getAllPoi().get(0);
					setResult(11);
					finish();
					return;
				}
				if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
		
					// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
//					String strInfo = "在";
//					for (CityInfo cityInfo : result.getSuggestCityList()) {
//						strInfo += cityInfo.city;
//						strInfo += ",";
//					}
//					strInfo += "找到结果";
//					Toast.makeText(PoiSearchDemo.this, strInfo, Toast.LENGTH_LONG)
//							.show();
				}
				
			}
			
			@Override
			public void onGetPoiDetailResult(PoiDetailResult result) {
				if (result.error != SearchResult.ERRORNO.NO_ERROR) {
//				Toast.makeText(PoiSearchDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
//						.show();
			} else {
//				Toast.makeText(PoiSearchDemo.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
//				.show();
				Log.i("wang", "PoiSearchDemo.this, result.getName()  + result.getAddress()="+result.getName() + ": " + result.getAddress());
			}
				
			}
		});

	}
	
//	GetRouteSuccessListener getRouteSuccessListener;
//	
//	public interface GetRouteSuccessListener{
//		void processRoute(PoiInfo poiInfo);
//	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			mPoiSearch.searchInCity((new PoiCitySearchOption())
					.city("梅州")
					.keyword(et_endlocation.getText().toString())
					.pageNum(1));
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mPoiSearch.destroy();
		mSuggestionSearch.destroy();
		super.onDestroy();
	}
	
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		Log.i("wang", "onGetSuggestionResult(SuggestionResult res)");
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null)
				Log.i("wang", "info.key="+info.key);
				sugAdapter.add(info.key);
		}
		sugAdapter.notifyDataSetChanged();
	}


//	/**
//	 * OnGetPoiSearchResultListener
//	 * 获取Place详情页检索结果
//	 */
//	@Override
//	public void onGetPoiDetailResult(PoiDetailResult result) {
//		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
////			Toast.makeText(PoiSearchDemo.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
////					.show();
//		} else {
////			Toast.makeText(PoiSearchDemo.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
////			.show();
//		}
//		
//	}
//
//	/**
//	 * OnGetPoiSearchResultListener
//	 * 获取POI检索结果  
//	 */
//	@Override
//	public void onGetPoiResult(PoiResult result) {
//		if (result == null
//				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
////			Toast.makeText(PoiSearchDemo.this, "未找到结果", Toast.LENGTH_LONG)
////			.show();
////			return;
//		}
//		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
////			mBaiduMap.clear();
////			PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
////			mBaiduMap.setOnMarkerClickListener(overlay);
////			overlay.setData(result);
////			overlay.addToMap();
////			overlay.zoomToSpan();
//			return;
//		}
//		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
//
//			// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
////			String strInfo = "在";
////			for (CityInfo cityInfo : result.getSuggestCityList()) {
////				strInfo += cityInfo.city;
////				strInfo += ",";
////			}
////			strInfo += "找到结果";
////			Toast.makeText(PoiSearchDemo.this, strInfo, Toast.LENGTH_LONG)
////					.show();
//		}
//		
//	}

}
