package com.zhengyu.LiveStartDemo.MyView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.letv.controller.PlayProxy;
import com.letv.proxy.LeCloudProxy;
import com.letv.universal.iplay.EventPlayProxy;
import com.zhengyu.LiveStartDemo.R;
import com.zhengyu.LiveStartDemo.example.playerdemo.activity.PlayActivity;

public class MyHomeActivity extends Activity {
    private EditText yuming;
    private String url;
    private static final String DEFAULT_PLAY="rtmp://8070.mpull.live.lecloud.com/live/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_home);
        yuming= (EditText) findViewById(R.id.playName);
    }
    
    public void startlive(View view){
        Intent intent =  new Intent(MyHomeActivity.this, PlayActivity.class);
        if(yuming.getText().toString() != null && !"".equals(yuming.getText().toString())){

            url=yuming.getText().toString();
            Bundle mBundle = null;
            mBundle = new Bundle();
            mBundle.putInt(PlayProxy.PLAY_MODE, EventPlayProxy.PLAYER_LIVE);
            mBundle.putString("path", DEFAULT_PLAY+url);
            intent.putExtra(PlayActivity.DATA, mBundle);
            startActivity(intent);
//            boolean useHls = false;
//            intent.putExtra(PlayActivity.DATA, LetvParamsUtils.setActionLiveParams("rtmp://8070.mpull.live.lecloud.com/live/zheng", useHls));
//            Log.i("-----url----",DEFAULT_PLAY+url);
//            startActivity(intent);
        }else{  
            Toast.makeText(MyHomeActivity.this,"域名不能为空!",Toast.LENGTH_SHORT).show();
        }
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
}
