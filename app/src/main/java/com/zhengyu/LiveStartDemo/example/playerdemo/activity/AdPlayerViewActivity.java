package com.zhengyu.LiveStartDemo.example.playerdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.letv.controller.LetvPlayer;
import com.letv.controller.PlayContext;
import com.letv.controller.PlayProxy;
import com.letv.universal.iplay.EventPlayProxy;
import com.letv.universal.iplay.IPlayer;
import com.letv.universal.iplay.OnPlayStateListener;
import com.zhengyu.LiveStartDemo.R;
import com.zhengyu.LiveStartDemo.letv.simple.utils.LetvParamsUtils;

public class AdPlayerViewActivity extends Activity{
	protected static final String TAG = "ListViewActivity";
	private TextureView textureView;
	
	private PlayContext playContext;
	private LetvPlayer player;
	private int videoHeight = 0;
	private int videoWidth = 0;
	private boolean isBackground = false;
	private ADPlayer adPlayer;
	private RelativeLayout rootPlayer;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ad_view_layout);
		initView();
		initPlayer();
	}
	private void initPlayer() {
		playContext = new PlayContext(this);
		player = new LetvPlayer();
		player.setPlayContext(playContext);
		player.init();
		Bundle  bundle = new Bundle();
		bundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_VOD);
		bundle.putString(PlayProxy.PLAY_UUID, "2b686d84e3");
		bundle.putString(PlayProxy.PLAY_VUID, "15d2678091");
		player.setParameter(player.getPlayerId(), bundle);
		player.setOnPlayStateListener(listener);
		textureView.post(new Runnable() {
			
			@Override
			public void run() {
				player.prepareAsync();
			}
		});
		playContext.setVideoContentView(textureView);
	}
	
	private void initView() {
		textureView =  (TextureView) findViewById(R.id.texture_view_tv);
		textureView.setSurfaceTextureListener(surfaceListener);
		rootPlayer = (RelativeLayout) findViewById(R.id.root_player);
		rootPlayer.setLayoutParams(new LinearLayout.LayoutParams(scWidth(), (int) (scWidth()*9.0/16)));
		
		   findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                    if (player != null) {
	                        player.stop();
	                        player.reset();
	                        String uuid = "";
	                        String vuid = "";
	                        uuid = "7a4f55c18a";
	                        vuid = "769312c218";
	                        if(adPlayer != null){
	                        	adPlayer.onDestroy();
	                        }
	                        player.setParameter(player.getPlayerId(), LetvParamsUtils.setVodParams(uuid, vuid, "", "151398", ""));
	                        player.prepareAsync();
	                    }
	            }
	        });
	}
	
	private int scHeight(){
		Display display = getWindowManager().getDefaultDisplay();
		return display.getHeight();
	}
	private int scWidth(){
		Display display = getWindowManager().getDefaultDisplay();
		return display.getWidth();
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(adPlayer != null){
			adPlayer.onResume();
		}
		if(player != null && isBackground){
			textureView.post(new Runnable() {
				
				@Override
				public void run() {
					Log.e(TAG, "调用了start方法");
					player.start();
					isBackground = false;
				}
			});
		}
	}
	@Override
	protected void onPause() {
		super.onPause();
		if(adPlayer != null){
			adPlayer.onPause();
		}
		if (player != null && player.isPlaying()) {
			player.pause();
			isBackground = true;
		}
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		double ratio = 4.0/3;
		if(videoHeight != 0 && videoWidth != 0){
			ratio = Math.min(((double)scWidth()/videoWidth), (double)scHeight()/videoHeight);
		}
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(videoWidth*ratio),(int)(videoHeight*ratio));
		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
		}else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		}
		textureView.setLayoutParams(params);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(player != null){
			player.stop();
			player.release();
		}
	}
	private OnPlayStateListener listener = new OnPlayStateListener() {
		
		@Override
		public void videoState(int state, Bundle bundle) {
			if(player == null) return;
			switch (state) {
			case IPlayer.MEDIA_ERROR_DECODE_ERROR:
				Log.e(TAG, "播放器事件：视频流解码错误");
				break;
			case IPlayer.MEDIA_ERROR_NO_STREAM:
				Log.e(TAG, "播放器事件：没有找到视频流");
				break;
			case IPlayer.MEDIA_EVENT_BUFFER_END:
				Log.e(TAG, "播放器事件：视频缓冲结束");
				break;
			case IPlayer.MEDIA_EVENT_BUFFER_START:
				Log.e(TAG, "播放器事件：视频开始缓冲");
				break;
			case IPlayer.MEDIA_EVENT_FIRST_RENDER:
				Log.e(TAG, "播放器事件：获取第一针画面");
				break;
			case IPlayer.MEDIA_EVENT_PLAY_COMPLETE:
				Log.e(TAG, "播放器事件：播放完成");
				break;
			case IPlayer.MEDIA_EVENT_PREPARE_COMPLETE:
				Log.e(TAG, "播放器事件：准备完成");
				adPlayer = new ADPlayer(player,textureView, AdPlayerViewActivity.this);
					// 准备完成 那么我就直接去播放吧
				adPlayer.onStart();
//				player.start();
				break;
			case IPlayer.MEDIA_EVENT_SEEK_COMPLETE:
				Log.e(TAG, "播放器事件：seek完成");
				break;
			case IPlayer.MEDIA_EVENT_VIDEO_SIZE:
				Log.e(TAG, "播放器事件：获取视频的尺寸");
				videoWidth = player.getVideoWidth();
				videoHeight = player.getVideoHeight();
				double ratio = Math.min(((double)scWidth()/videoWidth), (double)scHeight()/videoHeight);
				textureView.setLayoutParams(new RelativeLayout.LayoutParams((int)(videoWidth*ratio),(int)(videoHeight*ratio)));
				break;
			default:
				break;
			}
		}
	};
	private TextureView.SurfaceTextureListener surfaceListener = new TextureView.SurfaceTextureListener() {
		
		@Override
		public void onSurfaceTextureUpdated(SurfaceTexture surface) {
			
		}
		
		@Override
		public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
				int height) {
			if(adPlayer != null){
				adPlayer.onChanged(width, height);
			}
		}
		
		@Override
		public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
			Log.e(TAG, "TextureView 销毁");
			if(player != null){
				player.suspend();
			}
			return true;
		}
		
		@Override
		public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
			Surface sur = new Surface(surface);
			if(player != null){
				player.setDisplay(sur);
				player.regain();
			}
		}
	};
	
	
	
	
	
	
	
	class ADPlayer{
		private PopupWindow pw;
		private TextureView surface;
		private Context context;
		private int width;
		private int height;
		private TextureView adSurface;
		private LetvPlayer player;
		private LetvPlayer adPlayer;
		
		public ADPlayer(LetvPlayer player,TextureView surface,Context context){
			this.context = context;
			this.surface = surface;
			this.player = player;
			adPlayer = new LetvPlayer();
			PlayContext playContext = new PlayContext(context);
			adPlayer.setPlayContext(playContext);
			playContext.setUsePlayerProxy(false);
			adPlayer.init();
			
			adPlayer.setOnPlayStateListener(listener);
			
			pw = new PopupWindow((View) this.surface.getParent(),RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
			pw.setTouchable(true);
			
			adSurface = new TextureView(context);
			adSurface.setSurfaceTextureListener(textureListener);
			pw.setContentView(adSurface);
		}
		public void onStart(){
			showPopupWindow();
			adSurface.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					if(adPlayer != null){
						adPlayer.setDataSource("http://g3.letv.com/vod/v1/MTgwLzYvNDQvbGV0di1ndWcvMTcvdmVyXzAwXzIyLTMzMjA4NTQxLWF2Yy0yMTg2MzMtYWFjLTMyMTczLTE1MDAwLTQ4ODI3Mi1jNmRmNDQwNzk4NjZkODkwMzk1ZGViZTU2YjA3YWM0Mi0xNDQyODkwNTkzNTQxLm1wNA==?platid=100&splatid=10000&gugtype=1&mmsid=35210139&type=m_liuchang_mp4&playid=0&termid=2&pay=0&hwtype=Che1-CL20&ostype=android&m3v=1&tss=ios");
						adPlayer.prepareAsync();
					}
				}
			},100);
		}
		
		private void showPopupWindow(){
			if(pw != null && pw.isShowing()){
				pw.dismiss();
			}
			width = surface.getWidth();
			height = surface.getHeight();
			pw.setWidth(width);
			pw.setHeight(height);
			adSurface.setLayoutParams(new WindowManager.LayoutParams(width, height));
				Log.e(TAG, "显示poputWindow ........... ");
			pw.showAsDropDown(surface,0,-height);
		}
		public void onDestroy(){
			if(adPlayer != null){
				adPlayer.stop();
				adPlayer.release();
				adPlayer = null;
			}
			if(pw != null && pw.isShowing()){
				pw.dismiss();
			}
		}
		
		public void onResume(){
			if(adPlayer != null ){
				adPlayer.start();
			}
		}
		public void onPause(){
			if(adPlayer != null){
				adPlayer.pause();
			}
		}
		
		public void onChanged(int width, int height){
			if(ADPlayer.this.width != width || ADPlayer.this.height != height){
				ADPlayer.this.width = width;
				ADPlayer.this.height = height;
				showPopupWindow();
			}
		}
		
		private OnPlayStateListener listener = new OnPlayStateListener() {
			
			@Override
			public void videoState(int state, Bundle bundle) {
				switch (state) {
				case IPlayer.MEDIA_EVENT_PLAY_COMPLETE:
					Log.e(TAG, "测试广告播放完成");
					onDestroy();
					surface.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							if(player != null)
								player.start();
						}
					}, 100);
					break;
				case IPlayer.MEDIA_EVENT_PREPARE_COMPLETE:
					Log.e(TAG, "测试广告播放准备完成");
					if(adPlayer!=null){
					    adPlayer.start();
					}
					break;
				case IPlayer.MEDIA_EVENT_VIDEO_SIZE:
					Log.e(TAG, "测试广告获取视频的宽和高");
					if(adPlayer!=null){
					    double ratio = Math.min(((double)width/adPlayer.getVideoWidth()), (double)height/adPlayer.getVideoHeight());
					    RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams((int)(adPlayer.getVideoWidth()*ratio),(int)(adPlayer.getVideoHeight()*ratio));
					    params.addRule(RelativeLayout.CENTER_IN_PARENT);
					    adSurface.setLayoutParams(params);
					}
					break;
					
				default:
					break;
				}
			}
		};
		
		private TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
			
			@Override
			public void onSurfaceTextureUpdated(SurfaceTexture surface) {
				
			}
			
			@Override
			public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
					int height) {
				
			}
			
			@Override
			public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
				Log.e(TAG, "测试广告销毁surfaceview");
				if(adPlayer != null){
					adPlayer.suspend();
				}
				return true;
			}
			
			@Override
			public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
				Log.e(TAG, "测试广告创建surfaceview");
				Surface sur = new Surface(surface);
				if(adPlayer != null){
					adPlayer.setDisplay(sur);
					adPlayer.regain();
				}
			}
		};
	}
	
}
