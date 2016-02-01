package com.wxxiaomi.electricbicycle.view.activity.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wxxiaomi.electricbicycle.view.custom.LoadingDialog;


public abstract class BaseActivity extends AppCompatActivity implements
		OnClickListener {

	protected Context ct;
	protected View loadingView;
	protected ImageButton rightBtn;
	protected TextView leftImgBtn;
	protected ImageButton rightImgBtn;
	protected TextView titleTv;
	protected TextView rightbutton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ct = this;
		initView();
		initData();
	}

	protected void initTitleBar() {
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		AppManager.getAppManager().finishActivity(this);
	}

	@Override
	public void onClick(View v) {
		processClick(v);
		switch (v.getId()) {
//		case R.id.activity_selectimg_back:
//			this.finish();
//			break;
		default:
			break;
		}
		

	}

//	protected void showToast(String msg) {
//		showToast(msg, 0);
//	}

//	protected void showToast(String msg, int time) {
//		CustomToast customToast = new CustomToast(ct, msg, time);
//		customToast.show();
//	}
//
//	protected CustomProgressDialog dialog;
//
//	protected void showProgressDialog(String content) {
//		if (dialog == null && ct != null) {
//			dialog = (CustomProgressDialog) DialogUtils.createProgressDialog(ct,
//					content);
//		}
//		dialog.show();
//	}
//
//	protected void closeProgressDialog() {
//		if (dialog != null)
//			dialog.dismiss();
//	}
//
//	public void showLoadingView() {
//		if (loadingView != null)
//			loadingView.setVisibility(View.VISIBLE);
//	}
//
//	public void dismissLoadingView() {
//		if (loadingView != null)
//			loadingView.setVisibility(View.INVISIBLE);
//	}
//
//	public void showLoadFailView() {
//		if (loadingView != null) {
//			loadingView.setVisibility(View.VISIBLE);
//			loadfailView.setVisibility(View.VISIBLE);
//		}
//
//	}
//
//	public void dismissLoadFailView() {
//		if (loadingView != null)
//			loadfailView.setVisibility(View.INVISIBLE);
//	}
	
	AlertDialog msgDialog ;
	protected void showMsgDialog(String content){
		msgDialog = new AlertDialog.Builder(ct).setMessage(content).setPositiveButton("确定", null).create();
//		msgDialog.set
		msgDialog.show();
	}
	
	protected void closeMsgDialog(){
		if(msgDialog != null){
			msgDialog.dismiss();
		}
	}
	
	LoadingDialog dialog;
	/**
	 * 显示加载dialog
	 * @param content  加载dialog显示的内容
	 */
	protected void showLoadingDialog(String content){
		dialog = new LoadingDialog(ct).builder().setMessage(content);
		dialog.show();
	}
	
	/**
	 * 关闭加载dialog
	 */
	protected void closeLoadingDialog(){
		if(dialog != null){
			dialog.dismiss();
		}
	}

	protected abstract void initView();

	protected abstract void initData();

	protected abstract void processClick(View v);


	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}
	

	
}
