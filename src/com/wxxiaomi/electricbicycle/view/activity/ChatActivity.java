package com.wxxiaomi.electricbicycle.view.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.util.EasyUtils;
import com.wxxiaomi.electricbicycle.R;
import com.wxxiaomi.electricbicycle.view.fragment.ChatFragment;

/**
 * 聊天页面
 * @author Mr.W
 *
 */
public class ChatActivity extends FragmentActivity {

	public static ChatActivity activityInstance;
	private EaseChatFragment chatFragment;
	private String toChatUsername;

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_chat);
		
//		EaseUI.getInstance().init(this);
		activityInstance = this;
		// 聊天人或群id
		toChatUsername = getIntent().getExtras().getString("userId");
		// 可以直接new EaseChatFratFragment使用
		chatFragment = new ChatFragment();
		// 传入参数
//		chatFragment.setArguments(getIntent().getExtras());
		
		Bundle args = new Bundle();
//		 args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
		 args.putString(EaseConstant.EXTRA_USER_ID, toChatUsername);
		 chatFragment.setArguments(args);
		 
		 getSupportFragmentManager().beginTransaction().add(R.id.container,
		 chatFragment).commit();
//		 EMClient.getInstance().chatManager().addMessageListener(msgListener);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		activityInstance = null;
//		EMClient.getInstance().chatManager().removeMessageListener(msgListener);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// 点击notification bar进入聊天页面，保证只有一个聊天页面
		String username = intent.getStringExtra("userId");
		if (toChatUsername.equals(username))
			super.onNewIntent(intent);
		else {
			finish();
			startActivity(intent);
		}

	}

	@Override
	public void onBackPressed() {
		chatFragment.onBackPressed();
		if (EasyUtils.isSingleActivity(this)) {
			// Intent intent = new Intent(this, MainActivity.class);
			// startActivity(intent);
		}
	}

	public String getToChatUsername() {
		return toChatUsername;
	}
	
//	EMMessageListener msgListener = new EMMessageListener() {
//		 
//		@Override
//		public void onMessageReceived(List<EMMessage> messages) {
//			//收到消息
//			Log.i("wang", "chatactivity收到消息,"+messages.get(0).toString());
//		}
//	 
//		@Override
//		public void onCmdMessageReceived(List<EMMessage> messages) {
//			//收到透传消息
//		}
//	 
//		@Override
//		public void onMessageReadAckReceived(List<EMMessage> messages) {
//			//收到已读回执
//		}
//	 
//		@Override
//		public void onMessageDeliveryAckReceived(List<EMMessage> message) {
//			//收到已送达回执
//		}
//	 
//		@Override
//		public void onMessageChanged(EMMessage message, Object change) {
//			//消息状态变动
//		}
//	};
	 

}
