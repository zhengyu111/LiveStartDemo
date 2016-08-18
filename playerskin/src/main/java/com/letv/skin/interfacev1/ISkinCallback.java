package com.letv.skin.interfacev1;

public interface ISkinCallback {
	/**
	 * 改变屏幕方向
	 */
	public final static int CHANGE_SCREEN_ORIENTATION_STATE = 0;
	
	public void callback(int state, Object... objs);
}
