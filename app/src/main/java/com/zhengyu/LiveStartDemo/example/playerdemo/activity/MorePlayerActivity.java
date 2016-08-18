package com.zhengyu.LiveStartDemo.example.playerdemo.activity;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.letv.controller.LetvPlayer;
import com.letv.controller.PlayContext;
import com.letv.controller.PlayProxy;
import com.letv.universal.iplay.EventPlayProxy;
import com.letv.universal.iplay.IPlayer;
import com.letv.universal.iplay.OnPlayStateListener;
import com.zhengyu.LiveStartDemo.R;

public class MorePlayerActivity extends Activity{
	private RelativeLayout mainLayout ;
	private RelativeLayout assistLayout;
	private LetvPlayer mainPlayer;
	private LetvPlayer assistPlayer;
	private SurfaceView mainSurface;
	private SurfaceView assistSurface;
	private boolean mainSuspend = false;
	private boolean assistSuspend = false;

	private boolean isBack = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_player_layout);
		initView();
		initPlayer();
	}

	private void initPlayer() {
		mainPlayer = new LetvPlayer();
		assistPlayer = new LetvPlayer();
		PlayContext mainPlayerContext = new PlayContext(this);
		PlayContext assistPlayerContext = new PlayContext(this);
		mainPlayerContext.setVideoContentView(mainSurface);
		assistPlayerContext.setVideoContentView(assistSurface);

		assistPlayerContext.setUsePlayerProxy(false);

		mainPlayer.setPlayContext(mainPlayerContext);
		assistPlayer.setPlayContext(assistPlayerContext);

		mainPlayer.init();
		assistPlayer.init();

		Bundle bundle = new Bundle();
		bundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_VOD);
		bundle.putString(PlayProxy.PLAY_UUID,  "603143efd0");
		bundle.putString(PlayProxy.PLAY_VUID, "2d89a1d5c7");
		mainPlayer.setParameter(mainPlayer.getPlayerId(), bundle);

		assistPlayer.setVolume(0, 0);
		assistPlayer.setDataSource("http://g3.letv.com/vod/v1/MTgwLzYvNDQvbGV0di1ndWcvMTcvdmVyXzAwXzIyLTMzMjA4NTQxLWF2Yy0yMTg2MzMtYWFjLTMyMTczLTE1MDAwLTQ4ODI3Mi1jNmRmNDQwNzk4NjZkODkwMzk1ZGViZTU2YjA3YWM0Mi0xNDQyODkwNTkzNTQxLm1wNA==?platid=100&splatid=10000&gugtype=1&mmsid=35210139&type=m_liuchang_mp4&playid=0&termid=2&pay=0&hwtype=Che1-CL20&ostype=android&m3v=1&tss=ios");

		mainPlayer.setOnPlayStateListener(mainListener);
		assistPlayer.setOnPlayStateListener(assistListener);

		mainSurface.post(new Runnable() {

			@Override
			public void run() {
				mainPlayer.prepareAsync();
			}
		});
		assistSurface.postDelayed(new Runnable() {

			@Override
			public void run() {
				assistPlayer.prepareAsync();
			}
		},2000);
	}

	private void initView() {
		mainLayout = (RelativeLayout) findViewById(R.id.root_layout);
		assistLayout = (RelativeLayout) findViewById(R.id.ass_layout);

		mainSurface = new SurfaceView(this);
		assistSurface = new SurfaceView(this);

		mainSurface.getHolder().addCallback(mainCallback);
		assistSurface.getHolder().addCallback(assistCallback);

		mainLayout.addView(mainSurface,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		assistSurface.setZOrderOnTop(true);
		assistSurface.setZOrderMediaOverlay(true);
		assistSurface.getHolder().setFormat(PixelFormat.TRANSPARENT);
		mainSurface.getHolder().setKeepScreenOn(true);
		assistLayout.addView(assistSurface,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(isBack){
			if(!mainSuspend && mainPlayer != null){
				mainPlayer.start();
			}
			if(!assistSuspend && assistPlayer != null){
				assistPlayer.start();
			}
		}
		isBack = false;
	}
	@Override
	protected void onPause() {
		super.onPause();
		isBack = true;
		if(!mainSuspend && mainPlayer != null) mainPlayer.pause();
		if(!assistSuspend && assistPlayer != null) assistPlayer.pause();
	}
	protected void onDestroy() {
		super.onDestroy();
		if(mainPlayer != null){
			mainPlayer.stop();
			mainPlayer.release();
		}
		if(assistPlayer != null){
			assistPlayer.stop();
			assistPlayer.release();
		}
	};

	private OnPlayStateListener mainListener = new OnPlayStateListener() {

		@Override
		public void videoState(int state, Bundle bundle) {
			switch (state) {
			case IPlayer.MEDIA_EVENT_PREPARE_COMPLETE:
					mainPlayer.start();
				break;
			case IPlayer.MEDIA_EVENT_VIDEO_SIZE:
				double ratio = Math.min(((double)mainLayout.getWidth())/mainPlayer.getVideoWidth(),((double)mainLayout.getHeight())/mainPlayer.getVideoHeight());
				LayoutParams params = new LayoutParams((int)(ratio*mainPlayer.getVideoWidth()),(int) (ratio*mainPlayer.getVideoHeight()));
				params.addRule(RelativeLayout.CENTER_IN_PARENT);
				mainSurface.setLayoutParams(params);
				break;
			default:
				break;
			}
		}
	};
	private OnPlayStateListener assistListener = new OnPlayStateListener() {

		@Override
		public void videoState(int state, Bundle bundle) {
			switch (state) {
			case IPlayer.MEDIA_EVENT_PREPARE_COMPLETE:
				assistPlayer.start();
				break;
			case IPlayer.MEDIA_EVENT_VIDEO_SIZE:
				double ratio = Math.min(((double)assistLayout.getWidth())/assistPlayer.getVideoWidth(),((double)assistLayout.getHeight())/assistPlayer.getVideoHeight());
				LayoutParams params = new LayoutParams((int)(ratio*assistPlayer.getVideoWidth()),(int)( ratio*assistPlayer.getVideoHeight()));
				params.addRule(RelativeLayout.CENTER_IN_PARENT);
				assistSurface.setLayoutParams(params);
				break;

			default:
				break;
			}
		}
	};
	private SurfaceHolder.Callback mainCallback = new SurfaceHolder.Callback() {

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if(mainPlayer != null){
				mainPlayer.suspend();
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			if(mainPlayer != null){
				mainPlayer.setDisplay(holder.getSurface());
				mainPlayer.regain();
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

		}
	};
	private SurfaceHolder.Callback assistCallback = new SurfaceHolder.Callback() {

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			if(assistPlayer != null){
				assistPlayer.regain();
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			if(assistPlayer != null){
				assistPlayer.setDisplay(holder.getSurface());
				assistPlayer.regain();
			}
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

		}
	};
}
