package com.zhengyu.LiveStartDemo.example.playerdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.letv.proxy.LeCloudProxy;
import com.zhengyu.LiveStartDemo.R;
import com.zhengyu.LiveStartDemo.letv.simple.utils.ReadFileFromAssertUtil;
import com.zhengyu.LiveStartDemo.letv.simple.utils.SharedPreferenceUtil;

import java.io.File;

public class HomeActivity extends Activity implements OnClickListener, CopyDocFinishListener {
    private Button vod_hasSkin;
    private Button vod_noSkin;
    private Button live_hasSkin;
    private Button live_noSkin;
    private Button active_player_hasSkin;
    private Button active_player_noSkin;
    private Button mobile_player_hasSkin;
    private Button mobile_player_noSkin;
    private Button other;
    private Button description;
    private String docAssertPath = "doc";
    private String saveDocPath = Environment.getExternalStorageDirectory()
            + File.separator + "doc";
    ReadFileFromAssertUtil rfau;
    private static final String TAG = "HomeActivity";
    public boolean copyFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setOnClickListener();
        setCopyDocFinishListener(this);
        copyDoc();
    }

    private void initView() {
        vod_hasSkin = (Button) findViewById(R.id.vod_hasSkin);
        vod_noSkin = (Button) findViewById(R.id.vod_noSkin);
        live_hasSkin = (Button) findViewById(R.id.live_hasSkin);
        live_noSkin = (Button) findViewById(R.id.live_noSkin);
        active_player_hasSkin = (Button) findViewById(R.id.active_player_hasSkin);
        active_player_noSkin = (Button) findViewById(R.id.active_player_noSkin);
        mobile_player_hasSkin = (Button) findViewById(R.id.mobile_player_hasSkin);
        mobile_player_noSkin = (Button) findViewById(R.id.mobile_player_noSkin);
        other = (Button) findViewById(R.id.other);
        description = (Button) findViewById(R.id.description);
    }

    private void setOnClickListener() {
        vod_hasSkin.setOnClickListener(this);
        vod_noSkin.setOnClickListener(this);
        live_hasSkin.setOnClickListener(this);
        live_noSkin.setOnClickListener(this);
        active_player_hasSkin.setOnClickListener(this);
        active_player_noSkin.setOnClickListener(this);
        mobile_player_hasSkin.setOnClickListener(this);
        mobile_player_noSkin.setOnClickListener(this);
        other.setOnClickListener(this);
        description.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vod_hasSkin: {
                jumpVodActivity(true);
                break;
            }
            case R.id.vod_noSkin: {
                jumpVodActivity(false);
                break;
            }
            case R.id.live_hasSkin: {
                jumpLiveActivity(true);
                break;
            }
            case R.id.live_noSkin: {
                jumpLiveActivity(false);
                break;
            }
            case R.id.active_player_hasSkin: {
                jumpActivePlayActivity(true);
                break;
            }
            case R.id.active_player_noSkin: {
                jumpActivePlayActivity(false);
                break;
            }
            case R.id.mobile_player_hasSkin: {
                //移动直播有皮肤
                jumpMobileLiveActivity(true);
                break;
            }
            case R.id.mobile_player_noSkin: {
                //移动直播无皮肤
                jumpMobileLiveActivity(false);
                break;
            }
            case R.id.other: {
                Intent other = new Intent(HomeActivity.this, OtherActivity.class);
                startActivity(other);
                break;
            }
            case R.id.description: {
                if (!copyFinished) {
                    Toast.makeText(HomeActivity.this, "正在加载文档...", Toast.LENGTH_LONG).show();
                } else {
                    openDescriptionDoc(saveDocPath + "/index.html");
                }

                break;
            }
            case R.id.play_local: {
                startLocal();
                break;
            }
            default:
                break;
        }
    }

    private void startLocal() {
        // ckPanoVideo.isChecked()是否通过Url全景播放
        // et_url 通过Url播放的时候的url地址
        // Bundle mBundle = new Bundle();
        // Intent intent = getStartActivity();
        // mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_VOD);
        // mBundle.putBoolean(LetvParamsUtils.IS_LOCAL_PANO,
        // ckPanoVideo.isChecked());
        // EditText text = (EditText) findViewById(R.id.et_url);
        // mBundle.putString("path", text.getText().toString().trim());
        // intent.putExtra(PlayActivity.DATA, mBundle);
        // startActivity(intent);
    }

//	private Intent getStartActivity() {
//		return true ? new Intent(HomeActivity.this, PlayActivity.class)
//				: new Intent(HomeActivity.this, PlayNoSkinActivity.class);
//	}

    private void openDescriptionDoc(String path) {

        try {
            Uri uri = Uri.fromFile(new File(path));
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setClassName("com.android.browser",
                    "com.android.browser.BrowserActivity");
            browserIntent.setData(uri);
            startActivity(browserIntent);
        } catch (Exception e) {
            File f = new File(path);
            Uri uri = Uri.fromFile(f);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setDataAndType(uri, "text/html");
            startActivity(browserIntent);
        }
    }

    private void jumpVodActivity(boolean hasSkin) {
        Intent mIntent = new Intent(HomeActivity.this, VodActivity.class);
        mIntent.putExtra("hasSkin", hasSkin);
        startActivity(mIntent);
    }

    private void jumpLiveActivity(boolean hasSkin) {
        Intent mIntent = new Intent(HomeActivity.this, LiveActivity.class);
        mIntent.putExtra("hasSkin", hasSkin);
        startActivity(mIntent);
    }

    private void jumpActivePlayActivity(boolean hasSkin) {
        Intent mIntent = new Intent(HomeActivity.this, ActivePlayActivity.class);
        mIntent.putExtra("hasSkin", hasSkin);
        startActivity(mIntent);
    }

    private void jumpMobileLiveActivity(boolean hasSkin) {
        Intent mIntent = new Intent(HomeActivity.this, MobilePlayActivity.class);
        mIntent.putExtra("hasSkin", hasSkin);
        startActivity(mIntent);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        exit();
    }

    private void exit() {
        // 应用程序彻底退出销毁cde service
        LeCloudProxy.destory();
        System.exit(0);
    }

    private void copyDoc() {
        // 检查是否复制Doc目录
        final int verCode = getAppVersionCode(this);
        int copiedVerCode = SharedPreferenceUtil.getInstance(this).getInt("doc_copied_version_code", -1);
        if (verCode != copiedVerCode) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        ReadFileFromAssertUtil.deleteFile(new File(saveDocPath));
                        ReadFileFromAssertUtil.copyFromAssets(HomeActivity.this, docAssertPath, saveDocPath);
                        copyDocFinishListener.setCopyDoc(true);
                        SharedPreferenceUtil.getInstance(HomeActivity.this).putInt("doc_copied_version_code", verCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else {
            copyDocFinishListener.setCopyDoc(true);
        }
    }

    private int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionCode;
    }

    CopyDocFinishListener copyDocFinishListener;

    public void setCopyDocFinishListener(CopyDocFinishListener copyListener) {
        copyDocFinishListener = copyListener;
    }

    @Override
    public void setCopyDoc(boolean copyFinish) {
        copyFinished = copyFinish;
    }


}
