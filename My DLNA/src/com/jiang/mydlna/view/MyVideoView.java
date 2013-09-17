package com.jiang.mydlna.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.VideoView;

import com.jiang.mydlna.DlnaApplication;

public class MyVideoView extends VideoView {
	private final String TAG = "MyVideoView";
	
	private DlnaApplication mApplication;

	public MyVideoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mApplication = (DlnaApplication) context.getApplicationContext();
	}

	@Override
	public void pause() {
		super.pause();
		Log.e(TAG, "MyVideoView---pause()");
//		mApplication.setCurrentTransportState(TransportState.PAUSED_PLAYBACK);
	}
	
	@Override
	public void start() {
		super.start();
		Log.e(TAG, "MyVideoView---start()");
//		mApplication.setCurrentTransportState(TransportState.PLAYING);
	}
}
