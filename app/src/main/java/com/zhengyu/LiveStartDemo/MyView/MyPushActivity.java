package com.zhengyu.LiveStartDemo.MyView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.zhengyu.LiveStartDemo.R;
import com.zhengyu.LiveStartDemo.letv.data.CreateStreamData;
import com.zhengyu.LiveStartDemo.letv.data.LetvStreamData;
import com.zhengyu.LiveStartDemo.letv.data.StreamData;

public class MyPushActivity extends Activity {
    private static final String DEFAULT_DOMAINNAME = "8070.mpush.live.lecloud.com";
    private static final String DEFAULT_APPKEY = "BKO0YMASR2XD4ZRI9A4G";
    private static final String DEFAULT_PUSHSTREAM = "rtmp://8070.mpush.live.lecloud.com/live/";

    private static final String DEFAULT_PLAY="rtmp://8070.mpull.live.lecloud.com/live/";

    private TextView username;
    private TextView password;

    private TextView hostname;
    private TextView hostmessage;

    private Button login_button;

    private CreateStreamData createStreamData;
    private StreamData streamData;
    private LetvStreamData letvStreamData;

    private String str_host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_push);
    }
}
