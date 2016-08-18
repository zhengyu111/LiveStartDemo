package com.zhengyu.LiveStartDemo.example.playerdemo.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.letv.controller.LetvPlayer;
import com.letv.controller.PlayContext;
import com.letv.controller.PlayProxy;
import com.letv.universal.iplay.EventPlayProxy;
import com.letv.universal.iplay.IPlayer;
import com.letv.universal.iplay.OnPlayStateListener;
import com.zhengyu.LiveStartDemo.R;

public class ListPlayerViewActivity extends Activity{
	private ListView listView;
	private static final String TAG = "ListPlayerViewActivity";
	private String[][] videos = new String[][]{
			{"487c884e76","e5a4fb751e"},{"3a9d21720d","f524458b4f"},
			{"7a4f55c18a","769312c218"},{"3a9d21720d","4260c4a13c"},
			{"487c884e76","e5a4fb751e"},{"3a9d21720d","f524458b4f"},
			{"7a4f55c18a","769312c218"},{"3a9d21720d","4260c4a13c"},
			{"487c884e76","e5a4fb751e"},{"3a9d21720d","f524458b4f"},
			{"7a4f55c18a","769312c218"},{"3a9d21720d","4260c4a13c"},
			{"487c884e76","e5a4fb751e"},{"3a9d21720d","f524458b4f"},
			{"7a4f55c18a","769312c218"},{"3a9d21720d","4260c4a13c"},
			{"487c884e76","e5a4fb751e"},{"3a9d21720d","f524458b4f"},
			{"7a4f55c18a","769312c218"},{"3a9d21720d","4260c4a13c"},
			{"487c884e76","e5a4fb751e"},{"3a9d21720d","f524458b4f"},
			{"7a4f55c18a","769312c218"},{"3a9d21720d","4260c4a13c"}
	};
	
	private RelativeLayout mCurrentView = null;
	private int mCurrentItem = -1;
	private LetvPlayer player;
	PlayContext playContext;
	private int itemWidth = 0;
	private int itemHeight = 0;
	private SurfaceView surfaceView ;
	private RelativeLayout landscapeLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.activity_list_view_layout);
		initView();
		initPlayer();
	}

	private void initPlayer() {
		player = new LetvPlayer();
		playContext = new PlayContext(this);
		player.setPlayContext(playContext);
		player.init();
		player.setOnPlayStateListener(playerListener);
	}

	private void initView() {
		listView = (ListView) findViewById(R.id.list_view);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(listener);
		
		landscapeLayout = (RelativeLayout) findViewById(R.id.landscape_root);
		mCurrentView = null;
	};
	private int getScWidth(){
		return getWindowManager().getDefaultDisplay().getWidth();
	}
	private int getScHeight(){
		return getWindowManager().getDefaultDisplay().getHeight();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		switch (newConfig.orientation) {
		case Configuration.ORIENTATION_PORTRAIT:
			Log.e(TAG, "这里执行了一次ORIENTATION_PORTRAIT");
			landscapeLayout.removeAllViews();
			landscapeLayout.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			if(mCurrentView != null){
				addSurfaceView(mCurrentView);
			}
			break;
		case Configuration.ORIENTATION_LANDSCAPE:
			Log.e(TAG, "这里执行了一次ORIENTATION_LANDSCAPE");
			if(mCurrentView != null){
				mCurrentView.removeAllViews();
			}
			listView.setVisibility(View.GONE);
			landscapeLayout.setVisibility(View.VISIBLE);
			addSurfaceView(landscapeLayout);
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(player != null){
			player.release();
		}
	}
	
	private void addText(RelativeLayout view){
		view.removeAllViews();
		TextView text = new TextView(ListPlayerViewActivity.this);
		text.setText("点击item可以播放");
		text.setTextColor(0xffffffff);
		text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		view.addView(text, params);
	}
	private void addSurfaceView(RelativeLayout view) {
		view.removeAllViews();
		surfaceView = new SurfaceView(this);
		surfaceView.getHolder().addCallback(callback);
		surfaceView.setBackgroundColor(0x00000000);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		view.addView(surfaceView, params);
		playContext.setVideoContentView(surfaceView);
	}
	
	private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
		
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			Log.e(TAG, "surfaceview 销毁了");
			if(player != null && player.isPlaying()){
				player.stop();
			}
		}
		
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Log.e(TAG, "surfaceview 创建了");
			if(player != null){
				player.setDisplay(holder.getSurface());
				new Handler(getMainLooper()).postDelayed(new Runnable() {
					
					@Override
					public void run() {
						player.prepareAsync();
					}
				}, 1000);
			}
		}
		
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			
		}
	};
	
	private OnPlayStateListener playerListener = new OnPlayStateListener() {
		
		@Override
		public void videoState(int state, Bundle bundle) {
			switch (state) {
			case IPlayer.MEDIA_EVENT_VIDEO_SIZE:
				listView.post(new Runnable() {
					
					@Override
					public void run() {
						double ratio = 4.0/3;
						if(ListPlayerViewActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
							ratio = Math.min(((double)getScWidth()/player.getVideoWidth()), (double)getScHeight()/player.getVideoHeight());
						}else{
							ratio = Math.min(((double)itemWidth/player.getVideoWidth()), (double)itemHeight/player.getVideoHeight());
						}
						RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams((int)(player.getVideoWidth()*ratio),(int)(player.getVideoHeight()*ratio));
						params.addRule(RelativeLayout.CENTER_IN_PARENT);
						if(surfaceView != null){
							surfaceView.setLayoutParams(params);
						}
					}
				});
				break;
			case IPlayer.MEDIA_EVENT_PREPARE_COMPLETE:
					player.start();
				break;

			default:
				break;
			}
		}
	};
	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(mCurrentView != null){
				addText(mCurrentView);
			}
			mCurrentView = (RelativeLayout) view;
			mCurrentItem = position;
			Bundle bundle = new Bundle();
			bundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_VOD);
			bundle.putString(PlayProxy.PLAY_UUID, videos[position][0]);
			bundle.putString(PlayProxy.PLAY_VUID,  videos[position][1]);
			addSurfaceView((RelativeLayout) view);
			player.setParameter(player.getPlayerId(), bundle);
		}

	};
	
	
	private BaseAdapter adapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = new RelativeLayout(ListPlayerViewActivity.this);
				itemWidth = getScWidth();
				itemHeight = (int) (getScWidth()/(4.0/3));
				convertView.setLayoutParams(new AbsListView.LayoutParams(itemWidth, itemHeight));
			}
			if(mCurrentItem == position){
				mCurrentView = (RelativeLayout) convertView; 
				addSurfaceView((RelativeLayout) convertView);
			}else{
				addText((RelativeLayout) convertView);
			}
			
			return convertView;
		}
		
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public Object getItem(int position) {
			return "uu:"+videos[position][0]+";vu:"+videos[position][1];
		}
		
		@Override
		public int getCount() {
			return videos.length;
		}
	};
	
}
