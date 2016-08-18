package com.zhengyu.LiveStartDemo.example.playerdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lecloud.test.usetime.UseTimeResult;
import com.letv.skin.v4.V4PlaySkin;
import com.letv.universal.iplay.ISplayer;
import com.zhengyu.LiveStartDemo.R;
import com.zhengyu.LiveStartDemo.letv.simple.utils.LetvNormalAndPanoHelper;
import com.zhengyu.LiveStartDemo.letv.simple.utils.LetvParamsUtils;

public class PlayActivity extends Activity {
    public final static String DATA = "data";

    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////

    private V4PlaySkin skin;
    //如果不使用全景 请将LetvNormalAndPanoHelper 换成 LetvNormalVideoHelper
    private LetvNormalAndPanoHelper playHelper;
    private Bundle bundle;
    private TextView console;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        loadDataFromIntent();// load data
        skin = (V4PlaySkin) findViewById(R.id.videobody);
        playHelper = new LetvNormalAndPanoHelper();
        playHelper.init(this.getApplicationContext(), bundle, skin);
        console = (TextView) findViewById(R.id.console);
        playHelper.renderCallback = new LetvNormalAndPanoHelper.PlayerRenderCallback() {
            @Override
            public void onRender() {
                console.setText(UseTimeResult.print());
            }
        };
        initBtn();

    }

    private void loadDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            bundle = intent.getBundleExtra("data");
            if (bundle == null) {
                Toast.makeText(this, "no data", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (playHelper != null) {
            playHelper.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (playHelper != null) {
            playHelper.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (playHelper != null) {
            playHelper.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (playHelper != null) {
            playHelper.onConfigurationChanged(newConfig);
        }
    }

    private void initBtn() {
        findViewById(R.id.btn2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playHelper != null && v.isShown()) {
                    ISplayer player = playHelper.getPlayer();
                    if (player != null) {
                        player.stop();
                        player.reset();

                        String uuid = "";
                        String vuid = "";
                        uuid = "2b686d84e3";
                        vuid = "15d2678091";
                        player.setParameter(player.getPlayerId(), LetvParamsUtils.setVodParams(uuid, vuid, "", "151398", ""));
                        player.prepareAsync();
                    }
                }
            }
        });
    }
}
