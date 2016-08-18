package com.zhengyu.LiveStartDemo.example.playerdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhengyu.LiveStartDemo.R;


public class OtherActivity extends Activity implements OnClickListener{
	private ImageView back;
	private TextView title;
	private Button adtest;
	private Button video_list;
	private Button two_player;
	private Button download_list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other);
		initView();
		setOnClickListener();
	}
	private void initView(){
		back = (ImageView) findViewById(R.id.back);
		adtest = (Button) findViewById(R.id.adtest);
		video_list = (Button) findViewById(R.id.video_list);
		two_player = (Button) findViewById(R.id.two_player);
		download_list = (Button) findViewById(R.id.download_list);
		title = (TextView) findViewById(R.id.title);
		title.setText(getResources().getString(R.string.other));
	}
	
	private void setOnClickListener() {
		back.setOnClickListener(this);
		adtest.setOnClickListener(this);
		video_list.setOnClickListener(this);
		two_player.setOnClickListener(this);
		download_list.setOnClickListener(this);
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
			OtherActivity.this.finish();
			break;
		case R.id.adtest:
			starAdViewVod();
			break;
		case R.id.video_list:
			startListViewVod();
			break;
		case R.id.two_player:
			startMorePlayerVod();
			break;
		case R.id.download_list:
			Intent downd = new Intent(OtherActivity.this, DownloadActivity.class);
            startActivity(downd);
			break;
		default:
			break;
		}
	}
	/**
     * 在listView 中添加视频
     */
    private void startListViewVod() {
        Intent intent = new Intent(OtherActivity.this, ListPlayerViewActivity.class);
        startActivity(intent);
    }
	/**
     * 在listView 中添加视频
     */
    private void starAdViewVod() {
        Intent intent = new Intent(OtherActivity.this, AdPlayerViewActivity.class);
        startActivity(intent);
    }
	/**
     * 启动双播放器
     */
    private void startMorePlayerVod() {
        Intent intent = new Intent(OtherActivity.this, MorePlayerActivity.class);
        startActivity(intent);
    }

}
