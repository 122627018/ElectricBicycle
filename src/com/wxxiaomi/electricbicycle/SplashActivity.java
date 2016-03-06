package com.wxxiaomi.electricbicycle;

import com.wxxiaomi.electricbicycle.util.SharePrefUtil;
import com.wxxiaomi.electricbicycle.view.activity.RegisterOneActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

/**
 * 入口activity
 * @author Mr.W
 *
 */
public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(3000);
//					
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//		}).start();
		init();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				long start = System.currentTimeMillis();
				long costTime = System.currentTimeMillis() - start;
				if (2000 - costTime > 0) {
					try {
						Thread.sleep(2000 - costTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				boolean isFirst = SharePrefUtil.getBoolean(SplashActivity.this, "firstGo", true);
				if(isFirst){
					//第一次使用
					Intent intent = new Intent(SplashActivity.this,RegisterOneActivity.class);
					startActivity(intent);
					SharePrefUtil.saveBoolean(SplashActivity.this, "firstGo", false);
					finish();
				}else{
					//检测本地是否得到账号
					boolean isRemUser = SharePrefUtil.getBoolean(SplashActivity.this, "isRemUser", false);
					if(isRemUser){
						//本地有记住账号,实现登录功能
						//->DemoHelper.getInstance().isLoggedIn()再判断em有没有登陆过
					}else{
						//检测不到本地账号的话就去注册页面(里面有登录功能)
						Intent intent = new Intent(SplashActivity.this,RegisterOneActivity.class);
						startActivity(intent);
						finish();
					}
					
				}
				
			}
		}).start();
		
		
	}
	//https://github.com/122627018/ElectricBicycle.git

	/**
	 * 初始化各类参数
	 * 决定程序往哪里走
	 */
	private void init() {
//		Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//		startActivity(intent);
//		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
