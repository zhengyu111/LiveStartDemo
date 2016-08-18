package com.letv.recorder.ui;

import com.letv.recorder.ui.logic.UiObservable;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class RecorderDialog extends Dialog {

	private Bundle bundle;
	
	private UiObservable observable;

	public RecorderDialog(Context context, int theme) {
			super(context, theme);
		}

	protected RecorderDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
			super(context, cancelable, cancelListener);
		}

	public RecorderDialog(Context context) {
			super(context);
		}

	public Bundle getBundle() {
		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	public UiObservable getObservable() {
		return observable;
	}

	public void setObservable(UiObservable observable) {
		this.observable = observable;
	}

}
