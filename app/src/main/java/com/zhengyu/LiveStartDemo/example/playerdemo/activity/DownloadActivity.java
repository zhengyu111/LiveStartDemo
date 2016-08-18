package com.zhengyu.LiveStartDemo.example.playerdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lecloud.download.control.DownloadCenter;
import com.lecloud.download.info.LeDownloadInfo;
import com.lecloud.download.observer.LeDownloadObserver;
import com.letv.controller.PlayProxy;
import com.letv.universal.iplay.EventPlayProxy;
import com.zhengyu.LiveStartDemo.R;

import java.util.List;

public class DownloadActivity extends Activity {

	private String TAG = "DownloadActivity";

	private String uu1 = "3a9d21720d";
	private String vu1 = "f524458b4f";

	private String uu2 = "7a4f55c18a";
	private String vu2 = "769312c218";

	private String uu3 = "3a9d21720d";
	private String vu3 = "4260c4a13c";

	// uu:3a9d21720d
	// vu:f524458b4f

	// uu:7a4f55c18a
	// vu:d013d06536

	// uu:7a4f55c18a
	// vu:4fba7b8b30

	// uu:ab417c1571
	// vu:8f84a17f11

	// uu:ab417c1571
	// vu:22e7d27a06

	private ListView mListView;
	private MyAdapter mAdapter;
	private List<LeDownloadInfo> mDownloadInfos;
	// private LeDownloadManager downloadManager;
	private DownloadCenter mDownloadCenter;
	private Button downloadBtn1, downloadBtn2, downloadBtn3;
	private EditText uuET1, vuET1, uuET2, vuET2, uuET3, vuET3;
	private ImageView back;
	private TextView title;
	private LeDownloadObserver observer = new LeDownloadObserver() {

		@Override
		public void onDownloadSuccess(LeDownloadInfo info) {
			Log.e(TAG, "onDownloadSuccess" + info.getFileName());
			notifyData();
		}

		@Override
		public void onDownloadStop(LeDownloadInfo info) {
			Log.e(TAG, "onDownloadStop" + info.getFileName());
			notifyData();
		}

		@Override
		public void onDownloadStart(LeDownloadInfo info) {
			Log.e(TAG, "onDownloadStart" + info.getFileName());
			notifyData();
		}

		@Override
		public void onDownloadProgress(LeDownloadInfo info) {
			Log.e(TAG, "onDownloadProgress" + info.getFileName() + ",progress:" + info.getProgress());
			notifyData();
		}

		@Override
		public void onDownloadFailed(LeDownloadInfo info, String msg) {
			notifyData();
		}

		@Override
		public void onDownloadCancel(LeDownloadInfo info) {
			notifyData();
		}


		@Override
		public void onDownloadInit(LeDownloadInfo info, String msg) {
			notifyData();
		}

		@Override
		public void onDownloadWait(LeDownloadInfo info) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onGetVideoInfoRate(LeDownloadInfo info, List<String> rates) {
			// TODO Auto-generated method stub
			
		}
	};

	private void notifyData() {
		mDownloadInfos = mDownloadCenter.getDownloadInfoList();
		mAdapter.setData(mDownloadInfos);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_download);
		mDownloadCenter = DownloadCenter.getInstances(this.getApplicationContext());
		mDownloadCenter.registerDownloadObserver(observer);

		mListView = (ListView) findViewById(R.id.listview);
		mDownloadInfos = mDownloadCenter.getDownloadInfoList();
		mAdapter = new MyAdapter(mDownloadInfos);
		mListView.setAdapter(mAdapter);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DownloadActivity.this.finish();
			}
		});
		title = (TextView) findViewById(R.id.title);
		title.setText("下载列表");
		uuET1 = (EditText) findViewById(R.id.et_uu1);
		uuET1.setText(uu1);
		vuET1 = (EditText) findViewById(R.id.et_vu1);
		vuET1.setText(vu1);
		uuET2 = (EditText) findViewById(R.id.et_uu2);
		uuET2.setText(uu2);
		vuET2 = (EditText) findViewById(R.id.et_vu2);
		vuET2.setText(vu2);
		uuET3 = (EditText) findViewById(R.id.et_uu3);
		uuET3.setText(uu3);
		vuET3 = (EditText) findViewById(R.id.et_vu3);
		vuET3.setText(vu3);

		downloadBtn1 = (Button) findViewById(R.id.download1);
		downloadBtn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadClick(uuET1.getText().toString(), vuET1.getText().toString());
			}
		});
		downloadBtn2 = (Button) findViewById(R.id.download2);
		downloadBtn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadClick(uuET2.getText().toString(), vuET2.getText().toString());
			}
		});
		downloadBtn3 = (Button) findViewById(R.id.download3);
		downloadBtn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				downloadClick(uuET3.getText().toString(), vuET3.getText().toString());
			}
		});

	}

	private void downloadClick(String uu, String vu) {
		DownloadCenter downloadCenter = DownloadCenter.getInstances(this.getApplicationContext());
//		downloadCenter.allowShowMsg(false);
		downloadCenter.downloadVideo("", uu, vu);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mDownloadCenter != null) {
			mDownloadCenter.unregisterDownloadObserver(observer);
		}
	}

	private void pauseClick(LeDownloadInfo downloadInfo2) {
		if (downloadInfo2.getDownloadState() == LeDownloadObserver.DOWLOAD_STATE_DOWNLOADING) {
			mDownloadCenter.stopDownload(downloadInfo2);
		} else if (downloadInfo2.getDownloadState() == LeDownloadObserver.DOWLOAD_STATE_STOP) {
			mDownloadCenter.resumeDownload(downloadInfo2);
		} else if(downloadInfo2.getDownloadState() == LeDownloadObserver.DOWLOAD_STATE_FAILED){
//			mDownloadCenter.downloadVideo("",downloadInfo2.getUu(), downloadInfo2.getVu());
			mDownloadCenter.retryDownload(downloadInfo2);
		}else if(downloadInfo2.getDownloadState() == LeDownloadObserver.DOWLOAD_STATE_SUCCESS){
			Intent intent = new Intent(DownloadActivity.this,PlayActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_VOD);
			mBundle.putString(PlayProxy.PLAY_UUID, downloadInfo2.getUu());
			mBundle.putString(PlayProxy.PLAY_VUID, downloadInfo2.getVu());
			intent.putExtra(PlayActivity.DATA, mBundle);
			DownloadActivity.this.startActivity(intent);
		}else if(downloadInfo2.getDownloadState() == LeDownloadObserver.DOWLOAD_STATE_URL_REQUEST_FAILED){
			mDownloadCenter.retryDownload(downloadInfo2);
		}else if(downloadInfo2.getDownloadState() == LeDownloadObserver.DOWLOAD_STATE_NO_PERMISSION){
			Toast.makeText(getApplicationContext(), "没有权限下载啊啊啊啊", 0).show();
		}
	}

	private class MyAdapter extends BaseAdapter {

		private List<LeDownloadInfo> data;

		public MyAdapter(List<LeDownloadInfo> infos) {
			data = infos;
		}

		public void setData(List<LeDownloadInfo> infos) {
			data = infos;
		}

		@Override
		public int getCount() {
			if (data == null) {
				return 0;
			}
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			if (data == null) {
				return null;
			}
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = View.inflate(DownloadActivity.this, R.layout.list_item_layout, null);
			final LeDownloadInfo info = data.get(position);

			TextView name = (TextView) v.findViewById(R.id.file_name);
			name.setText(info.getFileName());
			TextView progressValue = (TextView) v.findViewById(R.id.progress_value);

			ProgressBar progress = (ProgressBar) v.findViewById(R.id.progressBar1);
//			Log.e(TAG,
//					"===========getView:" + info.getFileName() + "," + info.getProgress() + "," + info.getFileLength() + ","
//							+ info.getDownloadState());
			progress.setMax((int) info.getFileLength());
			progress.setProgress((int) info.getProgress());

			float p = 0;
			if (info.getFileLength() > 0) {
				p = (float) info.getProgress() / (float) info.getFileLength() * 100;
			}
			progressValue.setText(String.valueOf((int) p) + "%");

			Button pause = (Button) v.findViewById(R.id.pause_resume_btn);
			if (info.getDownloadState() == LeDownloadObserver.DOWLOAD_STATE_WAITING) {
				pause.setText("等待中");
				pause.setEnabled(false);
			} else if (info.getDownloadState() == LeDownloadObserver.DOWLOAD_STATE_DOWNLOADING) {
				pause.setText("暂停");
				pause.setEnabled(true);
			} else if (info.getDownloadState() == LeDownloadObserver.DOWLOAD_STATE_STOP) {
				pause.setText("继续");
				pause.setEnabled(true);
			} else if (info.getDownloadState() == LeDownloadObserver.DOWLOAD_STATE_SUCCESS) {
				pause.setText("播放");
				pause.setEnabled(true);
			}else if (info.getDownloadState() == LeDownloadObserver.DOWLOAD_STATE_NO_DISPATCH) {
				pause.setText("正在调度");
				pause.setEnabled(true);
			}else if (info.getDownloadState() == LeDownloadObserver.DOWLOAD_STATE_NO_PERMISSION) {
				pause.setText("没有权限");
				pause.setEnabled(true);
			}else if (info.getDownloadState() == LeDownloadObserver.DOWLOAD_STATE_URL_REQUEST_FAILED) {
				pause.setText("请求失败");
				pause.setEnabled(true);
			}else if(info.getDownloadState() == LeDownloadObserver.DOWLOAD_STATE_DISPATCHING){
				pause.setText("调度中");
				pause.setEnabled(true);
			}else{
				pause.setText("重试");
				pause.setEnabled(true);
			}
			pause.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					pauseClick(info); 
				}
			});

			Button remove = (Button) v.findViewById(R.id.remove_btn);
			remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mDownloadCenter.cancelDownload(info, true);
				}
			});
			return v;
		}

	}
}
