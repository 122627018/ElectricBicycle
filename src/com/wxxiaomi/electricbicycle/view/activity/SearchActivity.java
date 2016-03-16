package com.wxxiaomi.electricbicycle.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.wxxiaomi.electricbicycle.GlobalParams;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.view.activity.base.BaseActivity;
import com.wxxiaomi.electricbicycle.view.adapter.PoiSearchResultAdapter;
import com.wxxiaomi.electricbicycle.view.adapter.PoiSearchResultAdapter.MyPoiSuggrestionResultClickListener;

/**
 * 搜索地点页面
 * 
 * @author Mr.W
 * 
 */
public class SearchActivity extends BaseActivity implements
		OnGetSuggestionResultListener {

	private Toolbar toolbar;
	private RecyclerView mRecyclerView;
	private LinearLayoutManager mLayoutManager;
	private EditText et_serach;

	private SuggestionSearch mSuggestionSearch = null;
	private PoiSearch mPoiSearch = null;
	/**
	 * 搜索建议结果adapter
	 */
	private PoiSearchResultAdapter sugAdapter;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_search);
		mRecyclerView = (RecyclerView) findViewById(android.R.id.list);
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);
		mRecyclerView.setItemAnimator(new DefaultItemAnimator());
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		// 标题的文字需在setSupportActionBar之前，不然会无效
		toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
		toolbar.setTitle("");
		et_serach = (EditText) findViewById(R.id.et_serach);
		et_serach.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		setSupportActionBar(toolbar);
		getSupportActionBar().setHomeButtonEnabled(true); // 设置返回键可用
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mPoiSearch = PoiSearch.newInstance();
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	protected void initData() {
		sugAdapter = new PoiSearchResultAdapter(ct,new MyPoiSuggrestionResultClickListener() {
			@Override
			public void click(int position) {
				showLoadingDialog("正在加载中");
				Log.i("wang", "position="+position);
				
				SuggestionInfo suggestionInfo = sugAdapter.getInfoList().get(position);
				LatLng pt = suggestionInfo.pt;
//				showLoadingDialog("正在加载路线");
				PoiInfo poiInf = new PoiInfo();
				poiInf.location = pt;
				GlobalParams.poiInf = poiInf;
				Intent intent = new Intent(ct,
						RoutePlanActivity.class);
				closeLoadingDialog();
				startActivity(intent);
//				finish();
//				mPoiSearch.searchInCity((new PoiCitySearchOption())
//						.city("梅州")
//						.keyword(et_serach.getText().toString().trim())
//						.pageNum(1));
			}
		});
		mRecyclerView.setAdapter(sugAdapter);
		et_serach.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() <= 0) {
					return;
				}
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(s.toString()).city("梅州"));

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		et_serach.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					showLoadingDialog("正在加载路线");
					mPoiSearch.searchInCity((new PoiCitySearchOption())
							.city("梅州")
							.keyword(et_serach.getText().toString().trim())
							.pageNum(1));
				}
				return false;
			}
		});

		mPoiSearch
				.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

					@Override
					public void onGetPoiResult(PoiResult result) {
						closeLoadingDialog();
						if (result == null
								|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
							showMsgDialog("未找到结果");
							// Toast.makeText(PoiSearchDemo.this, "未找到结果",
							// Toast.LENGTH_LONG)
							// .show();
							// return;
						}
						if (result.error == SearchResult.ERRORNO.NO_ERROR) {
							// mBaiduMap.clear();
							// PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
							// mBaiduMap.setOnMarkerClickListener(overlay);
							// overlay.setData(result);
							// overlay.addToMap();
							// overlay.zoomToSpan();
							// Intent intent = getIntent();
							// // intent.pute
							// Bundle bundle = new Bundle();
							// bundle.putSerializable("route", (Serializable)
							// result.getAllPoi().get(0));
							// Log.i("wang",
							// "result.getAllPoi().size()="+result.getAllPoi().size());
							//
							Log.i("wang", "address="
									+ result.getAllPoi().get(0).address);
							Log.i("wang", "result.getAllPoi().get(0).location="
									+ result.getAllPoi().get(0).location);
							// intent.putExtra("value", bundle);
							// setResult(11, intent);
							
							GlobalParams.poiInf = result.getAllPoi().get(0);
							// setResult(11);
							Intent intent = new Intent(ct,
									RoutePlanActivity.class);
							startActivity(intent);
//							finish();
							return;
						}
						if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

							// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
							// String strInfo = "在";
							// for (CityInfo cityInfo :
							// result.getSuggestCityList()) {
							// strInfo += cityInfo.city;
							// strInfo += ",";
							// }
							// strInfo += "找到结果";
							// Toast.makeText(PoiSearchDemo.this, strInfo,
							// Toast.LENGTH_LONG)
							// .show();
						}

					}

					@Override
					public void onGetPoiDetailResult(PoiDetailResult result) {
						if (result.error != SearchResult.ERRORNO.NO_ERROR) {
							// 抱歉，未找到结果
							showMsgDialog("抱歉未找到结果");
						} else {
							Log.i("wang",
									"PoiSearchDemo.this, result.getName()  + result.getAddress()="
											+ result.getName() + ": "
											+ result.getAddress());
						}

					}
				});

	}

	// GetRouteSuccessListener getRouteSuccessListener;
	//
	// public interface GetRouteSuccessListener{
	// void processRoute(PoiInfo poiInfo);
	// }

	@Override
	protected void processClick(View v) {
		// switch (v.getId()) {
		// case R.id.btn_ok:
		//
		// break;
		//
		// default:
		// break;
		// }
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
			if (info.key != null) {
				// Log.i("wang", "info.key="+info.key);
				sugAdapter.addInfo(info);
			}

		}
		sugAdapter.notifyDataSetChanged();
	}

}
