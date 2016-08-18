package com.zhengyu.LiveStartDemo.MyView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zhengyu.LiveStartDemo.R;
import com.zhengyu.LiveStartDemo.example.playerdemo.activity.PlayActivity;
import com.zhengyu.LiveStartDemo.letv.simple.utils.LetvParamsUtils;

public class MyPlayActivity extends Activity {
    private Button startActive;
    private EditText activeIdContent;
    //	String active_id = "A201603280000074";
    String active_id = "A2016041800000o8";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play2);
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        activeIdContent = (EditText) findViewById(R.id.active_my_content);
        activeIdContent.setText(active_id);
        startActive = (Button) findViewById(R.id.mystartActive);
        startActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLeCloudActionLive();
            }
        });
    }
    /**
     * 乐视云活动直播
     */
    private void startLeCloudActionLive() {
        Intent intent = new Intent(MyPlayActivity.this, PlayActivity.class);
        boolean useHls = false;
        intent.putExtra(PlayActivity.DATA, LetvParamsUtils.setActionLiveParams(activeIdContent.getText().toString().trim(), useHls));
        startActivity(intent);
    }
    
    
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
}
