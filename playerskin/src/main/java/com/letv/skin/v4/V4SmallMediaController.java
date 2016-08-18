package com.letv.skin.v4;

import java.util.Observable;
import java.util.Observer;

import com.letv.controller.interfacev1.ISplayerController;
import com.letv.skin.base.BasePlayBtn;
import com.letv.skin.base.BasePlayerSeekBar;
import com.letv.skin.controller.BaseMediaController;
import com.letv.skin.utils.PlayerTimer;
import com.letv.skin.widget.TextTimerView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

public class V4SmallMediaController extends BaseMediaController {
	
	private BasePlayerSeekBar seekbar;
	private TextTimerView timerView;

    public V4SmallMediaController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public V4SmallMediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public V4SmallMediaController(Context context) {
        super(context);
    }

    @Override
    protected void onSetLayoutId() {
        layoutId = "letv_skin_v4_controller_layout";
        childId.add("vnew_play_btn");
        childId.add("vnew_chg_btn");
        childId.add("vnew_seekbar");
        childId.add("vnew_text_duration_ref");
    }

    @Override
    protected void onInitView() {
    	seekbar = (BasePlayerSeekBar) childViews.get(2);
    	timerView = (TextTimerView) childViews.get(3);
    }

    @Override
    protected void initPlayer() {
    	initTimer(player);
        BasePlayBtn playBtn = (BasePlayBtn) childViews.get(0);
        playBtn.setPlayBtnType(BasePlayBtn.play_btn_type_vod);// 设置按钮模式

        V4ChgScreenBtn chgBtn = (V4ChgScreenBtn) childViews.get(1);
        chgBtn.showZoomInState();
    }
    
    
    private void initTimer(ISplayerController player) {
//      TimerUtils.initTextFormatter();
      seekbar.getPlayerTimer().getObserver().addObserver(new Observer() {
          @Override
          public void update(Observable observable, Object data) {
              Bundle bundle = (Bundle) data;
              if (bundle.getInt("state") == PlayerTimer.TIMER_HANDLER_PER_TIME) {
                  int position = bundle.getInt(PlayerTimer.key_position);
                  int duration = bundle.getInt(PlayerTimer.key_duration);
                  timerView.setTextTimer(position, duration);
              }
          }
      });
  }

}
