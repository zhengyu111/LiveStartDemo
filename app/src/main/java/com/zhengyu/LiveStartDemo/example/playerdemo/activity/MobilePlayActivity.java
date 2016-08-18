package com.zhengyu.LiveStartDemo.example.playerdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class MobilePlayActivity extends Activity implements OnClickListener {
    private static final String TAG = "MobilePlayActivity";
    private ImageView back;
    private TextView title;
    private Button startMobileLive;
    private EditText path_content;
    boolean hasSkin;
    //	String playpath = "rtmp://183.mpull.live.lecloud.com/live/sdjjdhh?tm=20160415171459&sign=b3c61964f261b1966fa550ef9be1d6fc";
    String playpath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate()===");
        setContentView(R.layout.mobile_live);
        hasSkin = getIntent().getBooleanExtra("hasSkin", false);
        initView();
        setOnClickListener();
    }

    private void setOnClickListener() {
        // TODO Auto-generated method stub
        back.setOnClickListener(this);
        startMobileLive.setOnClickListener(this);
    }

    private void initView() {
        // TODO Auto-generated method stub
        path_content = (EditText) findViewById(R.id.path_content);
        path_content.setText(playpath);
        startMobileLive = (Button) findViewById(R.id.startMobileLive);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        if (hasSkin) {
            title.setText(getResources().getString(R.string.mobile_player_hasSkin));
        } else {
            title.setText(getResources().getString(R.string.mobile_player_noSkin));
        }
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
                MobilePlayActivity.this.finish();
                break;
            case R.id.startMobileLive:
                if(TextUtils.isEmpty(path_content.getText().toString().trim())){
                    Toast.makeText(MobilePlayActivity.this,"播放地址为空,不能播放..",Toast.LENGTH_SHORT).show();
                    return;
                }
                startLeCloudMobileLive();
                break;
            default:
                break;
        }
    }

    /**
     * 乐视云移动直播
     */
    private void startLeCloudMobileLive() {
        Intent intent = getStartActivity();
        Bundle mBundle = null;
        mBundle = new Bundle();
        mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);
        mBundle.putString("path", path_content.getText().toString().trim());
        intent.putExtra(PlayActivity.DATA, mBundle);
        startActivity(intent);
    }

    private Intent getStartActivity() {
        return hasSkin ? new Intent(MobilePlayActivity.this, PlayActivity.class)
                : new Intent(MobilePlayActivity.this, PlayNoSkinActivity.class);
    }
}
