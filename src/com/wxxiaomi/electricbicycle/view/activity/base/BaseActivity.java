package com.wxxiaomi.electricbicycle.view.activity.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wxxiaomi.electricbicycle.R;
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
	protected static UIHandler handler = new UIHandler(Looper.getMainLooper());

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		ct = this;
		setHandler();
		initView();
		initData();
		
	}
	
	  private void setHandler() {
	        handler.setHandler(new IHandler() {
	            public void handleMessage(Message msg) {
	                handler(msg);//有消息就提交给子类实现的方法
	            }
	        });     
	    }
	  //让子类处理消息
	    

	protected void handler(Message msg) {
		switch (msg.what) {
		case 1:
			showLoadingDialog("正在登陆");
			break;
		case 2:
			closeLoadingDialog();
			break;
		default:
			break;
		}
	}
	
	protected void showLoginLoadding(){
		Message msg = new Message();
		msg.what = 1;
		handler(msg);
	}
	protected void closeLoginLoadding(){
		Message msg = new Message();
		msg.what = 1;
		handler(msg);
	}

	protected void initTitleBar() {
	}


	@Override
	public void onClick(View v) {
		processClick(v);
	}
	AlertDialog msgDialog ;
	protected void showMsgDialog(String content){
		msgDialog = new AlertDialog.Builder(ct,R.style.MingDialog).setMessage(content).setPositiveButton("确定", null).create();
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
			Log.i("wang", "closeLoadingDialog2");
			dialog.dismiss();
		}
	}
	
	protected void setloadingViewContent(String content){
		if(dialog != null){
			dialog.setMessage(content);
		}
	}

	
	protected abstract void initView();

	protected abstract void initData();

	protected abstract void processClick(View v);


	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	
	

	
}
