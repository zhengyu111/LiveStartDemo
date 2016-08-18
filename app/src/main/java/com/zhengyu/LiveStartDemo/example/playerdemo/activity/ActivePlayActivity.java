package com.zhengyu.LiveStartDemo.example.playerdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhengyu.LiveStartDemo.R;
import com.zhengyu.LiveStartDemo.letv.simple.utils.LetvParamsUtils;

public class ActivePlayActivity extends Activity implements OnClickListener {
    private ImageView back;
    private TextView title;
    private Button startActive;
    private EditText activeIdContent;
    boolean hasSkin;
    //	String active_id = "A201603280000074";
    String active_id = "A2016041800000o8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.active_play);
        hasSkin = getIntent().getBooleanExtra("hasSkin", false);
        initView();
        setOnClickListener();
    }

    private void setOnClickListener() {
        // TODO Auto-generated method stub
        back.setOnClickListener(this);
        startActive.setOnClickListener(this);
    }

    private void initView() {
        // TODO Auto-generated method stub
        activeIdContent = (EditText) findViewById(R.id.active_content);
        activeIdContent.setText(active_id);
        startActive = (Button) findViewById(R.id.startActive);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        if (hasSkin) {
            title.setText(getResources().getString(R.string.active_player_hasSkin));
        } else {
            title.setText(getResources().getString(R.string.active_player_noSkin));
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
                ActivePlayActivity.this.finish();
                break;
            case R.id.startActive:
                startLeCloudActionLive();
                break;
            default:
                break;
        }
    }

    /**
     * 乐视云活动直播
     */
    private void startLeCloudActionLive() {
        Intent intent = getStartActivity();
        boolean useHls = false;
//        boolean useHls = !SharedPreferenceUtil.getInstance(this).getBoolean("rtmp", true);
        intent.putExtra(PlayActivity.DATA, LetvParamsUtils.setActionLiveParams(activeIdContent.getText().toString().trim(), useHls));
        startActivity(intent);
    }

    private Intent getStartActivity() {
        return hasSkin ? new Intent(ActivePlayActivity.this, PlayActivity.class) : new Intent(ActivePlayActivity.this, PlayNoSkinActivity.class);
    }
}
