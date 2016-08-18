package com.letv.skin.v4;

import com.letv.skin.base.BasePlayerSeekBar;

import android.content.Context;
import android.util.AttributeSet;

public class V4PlayerSeekBar extends BasePlayerSeekBar {
    
    public V4PlayerSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4PlayerSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4PlayerSeekBar(Context context) {
        super(context);
    }

    @Override
    public String getLayout() {
        return "letv_skin_v4_small_seekbar_layout";
    }

}
