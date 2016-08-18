package com.zhengyu.LiveStartDemo.letv.push;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.letv.recorder.controller.Publisher;
import com.letv.recorder.ui.RecorderView;
import com.letv.recorder.ui.mobile.RecorderSkinMobile;
import com.letv.recorder.util.LeLog;
import com.zhengyu.LiveStartDemo.R;

/**
 *	移动直播推流界面  
 *	在移动直播中，推流器只认识推流地址。
 *	这个Activity 需要用户传入三个值 isVertical 是否竖屏录制、streamUrl 推流地址、pushName 推流名称
 */
public class RecorderActivity extends Activity{
	private RecorderView rv;
	private RecorderSkinMobile recorderSkinMobile;
	private ImageView focusView;
	private boolean isVertical = false;
	private Publisher publisher;
	
	private String pushSteamUrl;
	private String pushName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_recorder);
		
		focusView = (ImageView) findViewById(R.id.focusView);
		rv = (RecorderView) findViewById(R.id.rv);//获取rootView
		
		isVertical = getIntent().getBooleanExtra("isVertical", false);
		pushSteamUrl = getIntent().getStringExtra("streamUrl");
		pushName = getIntent().getStringExtra("pushName");
		if(pushSteamUrl == null || "".equals(publisher)){
			Toast.makeText(this, "不能传入空的推流地址", Toast.LENGTH_SHORT).show();
		}
		LeLog.w("推流地址是:"+pushSteamUrl);
		
		/**
		 *  1、 初始化推流器，在移动直播中使用的是Publisher 对象
		 */
		initPublish();
		/**
		 *  2、初始化皮肤,在移动直播中使用的是RecorderSkinMobile 对象，并且需要传入不同的参数
		 */
		initSkin(isVertical);
		/**
		 * 3、绑定推流器
		 */
		bindingPublish();
	}
	@Override
	protected void onResume() {
		super.onResume();
		 /**
		  * 设置为横屏
		  */
		 if(!isVertical && getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		 }else if(isVertical && getRequestedOrientation()!=ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 }
		/**
		 * onResume的时候需要做一些事情
		 */
		if (recorderSkinMobile != null) {
			recorderSkinMobile.onResume();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * onPause的时候要作的一些事情
		 */
		if (recorderSkinMobile != null) {
			recorderSkinMobile.onPause();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(recorderSkinMobile!=null){
			recorderSkinMobile.onDestroy();
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 皮肤于推流器关联
	 */
	private void bindingPublish() {
		recorderSkinMobile.BindingPublisher(publisher);
		publisher.setCameraView(recorderSkinMobile.getCameraView());
		publisher.setFocusView(focusView);
	}

	/**
	 * 初始化皮肤
	 */
	private void initSkin(boolean isScreenOrientation) {
		recorderSkinMobile = new RecorderSkinMobile();
		recorderSkinMobile.setPushSteamUrl(pushSteamUrl);
		recorderSkinMobile.setStreamName(pushName);
		if(isScreenOrientation){
			recorderSkinMobile.build(this, rv,ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else{
			recorderSkinMobile.build(this, rv,ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	/**
	 * 初始化推流器
	 */
	private void initPublish() {
		publisher = Publisher.getInstance();
		if(isVertical){
			/// 竖屏状态
			publisher.getRecorderContext().setUseLanscape(false);
		}else{
			/// 横屏状态
			publisher.getRecorderContext().setUseLanscape(true);
		}
		publisher.initPublisher(this);
	}

}
