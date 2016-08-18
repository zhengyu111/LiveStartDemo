package com.zhengyu.LiveStartDemo.example.playerdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.letv.controller.PlayProxy;
import com.letv.universal.iplay.EventPlayProxy;
import com.zhengyu.LiveStartDemo.R;
import com.zhengyu.LiveStartDemo.letv.simple.utils.LetvParamsUtils;
import com.zhengyu.LiveStartDemo.letv.simple.utils.SharedPreferenceUtil;

public class LiveActivity extends Activity implements OnClickListener {
	private ImageView back;
	private TextView title;
	private Button liveId;
	private Button streamId;
	private Button playPath;
	private Button startLive;
	private RadioButton rtmp;
	private RadioButton hls;
	private EditText domain_content;
	private EditText playpath_content;
	boolean hasSkin;
	private View radioGroup;
	private View live_path;
	private View live_ID;
//	String canPlayLiveId = "201504213000012";
//	String canPlayStreamId = "20150421300001210";
	String canPlayLiveId = "201604183000000z4";
	String canPlayStreamId = "201604183000000z416";
	String playpath = "http://cache.utovr.com/201601131107187320.mp4";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live);
		hasSkin = getIntent().getBooleanExtra("hasSkin", false);
		initView();
		setOnClickListener();
		streamId.setSelected(false);
		liveId.setSelected(true);
		playPath.setSelected(false);
		rtmp.setChecked(SharedPreferenceUtil.getInstance(this).getBoolean(
				"rtmp", true));
		hls.setChecked(!SharedPreferenceUtil.getInstance(this).getBoolean(
				"rtmp", true));
	}

	private void setOnClickListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(this);
		liveId.setOnClickListener(this);
		streamId.setOnClickListener(this);
		playPath.setOnClickListener(this);
		startLive.setOnClickListener(this);
		rtmp.setOnClickListener(this);
		hls.setOnClickListener(this);
	}

	private void initView() {
		// TODO Auto-generated method stub
		live_ID = findViewById(R.id.live_ID);
		live_path = findViewById(R.id.live_path);
		radioGroup = findViewById(R.id.radioGroup);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		if (hasSkin) {
			title.setText(getResources().getString(R.string.live_hasSkin));
		} else {
			title.setText(getResources().getString(R.string.live_noSkin));
		}
		liveId = (Button) findViewById(R.id.liveId);
		streamId = (Button) findViewById(R.id.streamId);
		playPath = (Button) findViewById(R.id.playPath);
		startLive = (Button) findViewById(R.id.startLive);
		rtmp = (RadioButton) findViewById(R.id.rtmp);
		hls = (RadioButton) findViewById(R.id.hls);
		domain_content = (EditText) findViewById(R.id.domain_content);
		domain_content.setText(canPlayLiveId);
		playpath_content = (EditText) findViewById(R.id.path_content);
		playpath_content.setText(playpath);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			LiveActivity.this.finish();
			break;
		case R.id.liveId:
			domain_content.setText(canPlayLiveId);
			streamId.setSelected(false);
			liveId.setSelected(true);
			playPath.setSelected(false);
			live_ID.setVisibility(View.VISIBLE);
			radioGroup.setVisibility(View.GONE);
			live_path.setVisibility(View.GONE);
			break;
		case R.id.streamId:
			domain_content.setText(canPlayStreamId);
			streamId.setSelected(true);
			liveId.setSelected(false);
			playPath.setSelected(false);
			live_ID.setVisibility(View.VISIBLE);
			radioGroup.setVisibility(View.GONE);
			live_path.setVisibility(View.GONE);
			break;
		case R.id.playPath:
			streamId.setSelected(false);
			liveId.setSelected(false);
			playPath.setSelected(true);
			live_ID.setVisibility(View.GONE);
			radioGroup.setVisibility(View.GONE);
			live_path.setVisibility(View.VISIBLE);
			break;
		case R.id.startLive:
			startLeCloudLive();
			break;
		case R.id.rtmp:
			SharedPreferenceUtil.getInstance(this).putBoolean("rtmp", true);
			break;
		case R.id.hls:
			SharedPreferenceUtil.getInstance(this).putBoolean("rtmp", false);
			break;
		default:
			break;
		}
	}

	/**
	 * 乐视云直播
	 */
	private void startLeCloudLive() {
		Intent intent;
		if (hasSkin) {
			intent = new Intent(LiveActivity.this, PlayActivity.class);
		} else {
			intent = new Intent(LiveActivity.this, PlayNoSkinActivity.class);
		}
		Bundle mBundle = null;
		if (playPath.isSelected()) {
			mBundle = new Bundle();
			mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);
			mBundle.putString("path", playpath_content.getText().toString().trim());
		} else {
			String lId = null;
			String sId = null;
			if (liveId.isSelected()) {
				lId = domain_content.getText().toString().trim();
			} else {
				sId = domain_content.getText().toString().trim();
			}
			mBundle = LetvParamsUtils.setLiveParams(sId, lId, hls.isChecked(),
					false);

		}
		intent.putExtra(PlayActivity.DATA, mBundle);
		startActivity(intent);

	}

}
