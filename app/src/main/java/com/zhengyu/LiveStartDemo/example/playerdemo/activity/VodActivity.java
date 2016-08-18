package com.zhengyu.LiveStartDemo.example.playerdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.letv.controller.PlayProxy;
import com.letv.universal.iplay.EventPlayProxy;
import com.zhengyu.LiveStartDemo.R;
import com.zhengyu.LiveStartDemo.letv.simple.utils.LetvParamsUtils;

public class VodActivity extends Activity implements OnClickListener {
	private Button vodId;
	private Button vodPath;
	private EditText uuid_content;
	private EditText vuid_content;
	private EditText vodPath_content;
	private View vod_uuid;
	private View vod_vuid;
	private View vod_playPath;
	private Button startVod;
//	String uuid = "4d707a5d3f";
//	String vuid = "8f549b8c7e";
	String uuid = "hxn7psp8ot";
	String vuid = "49bf3407cb";
	private ImageView back;
	private TextView title;
	boolean hasSkin;
//	String playPath = "http://cache.utovr.com/201601131107187320.mp4";
	String playPath = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vod);
		hasSkin = getIntent().getBooleanExtra("hasSkin", false);
		initView();
		setOnClickListener();
		vodId.setSelected(true);
		vodPath.setSelected(false);
		vodPath_content.setText(playPath);
	}

	private void initView() {
		vodId = (Button) findViewById(R.id.vodId);
		vodPath = (Button) findViewById(R.id.vodPath);
		startVod = (Button) findViewById(R.id.startVod);
		uuid_content = (EditText) findViewById(R.id.uuid_content);
		uuid_content.setText(uuid);
		vuid_content = (EditText) findViewById(R.id.vuid_content);
		vuid_content.setText(vuid);
		vodPath_content = (EditText) findViewById(R.id.vodPath_content);
		vod_uuid = findViewById(R.id.vod_uuid);
		vod_vuid = findViewById(R.id.vod_vuid);
		vod_playPath = findViewById(R.id.vod_playPath);
		back = (ImageView) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.title);
		if(hasSkin){
			title.setText(getResources().getString(R.string.vod_hasSkin));
		}else{
			title.setText(getResources().getString(R.string.vod_noSkin));
		}
	}

	private void setOnClickListener() {
		vodId.setOnClickListener(this);
		vodPath.setOnClickListener(this);
		startVod.setOnClickListener(this);
		vod_uuid.setOnClickListener(this);
		vod_vuid.setOnClickListener(this);
		vod_playPath.setOnClickListener(this);
		back.setOnClickListener(this);
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
		case R.id.vodId:
			vodId.setSelected(true);
			vodPath.setSelected(false);
			vod_playPath.setVisibility(View.GONE);
			vod_uuid.setVisibility(View.VISIBLE);
			vod_vuid.setVisibility(View.VISIBLE);
			break;
		case R.id.vodPath:
			vodId.setSelected(false);
			vodPath.setSelected(true);
			vod_playPath.setVisibility(View.VISIBLE);
			vod_uuid.setVisibility(View.GONE);
			vod_vuid.setVisibility(View.GONE);
			break;
		case R.id.startVod:
			// 开始点播
			if(vodPath.isSelected()&&TextUtils.isEmpty(vodPath_content.getText().toString().trim())){
				Toast.makeText(VodActivity.this,"播放地址为空,不能播放..",Toast.LENGTH_SHORT).show();
				return;
			}
			startLecloudVod();
			break;
		case R.id.back:
			VodActivity.this.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 乐视云点播
	 */
	private void startLecloudVod() {
		Intent intent = getStartActivity();
		Bundle mBundle = null;
		if(vodId.isSelected()){
			mBundle = LetvParamsUtils.setVodParams(uuid_content.getText()
					.toString().trim(), vuid_content.getText().toString().trim(),
					"", "151398", "");
			
		}else{
			mBundle = new Bundle();
			mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_VOD);
			mBundle.putString("path", vodPath_content.getText().toString().trim());
		}
		intent.putExtra(PlayActivity.DATA, mBundle);
		startActivity(intent);
	}
	private Intent getStartActivity() {
		return hasSkin ? new Intent(VodActivity.this, PlayActivity.class)
				: new Intent(VodActivity.this, PlayNoSkinActivity.class);
	}
	
}
