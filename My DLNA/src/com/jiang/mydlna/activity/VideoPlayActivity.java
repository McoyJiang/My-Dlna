package com.jiang.mydlna.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

import com.jiang.mydlna.DlnaApplication;
import com.jiang.mydlna.R;
import com.jiang.mydlna.view.MyVideoView;

public class VideoPlayActivity extends Activity implements OnPreparedListener {
	private final String TAG = "FxRendererVideoPlayActivity";
	public final static String ACTION_VIDEO_PAUSE_PLAYBACK = "com.phicomm.dlna.action.ACTION_VIDEO_PAUSE_PLAYBACK";
	public final static String ACTION_VIDEO_PLAYBACK_AFTER_PAUSED = "com.phicomm.dlna.action.ACTION_VIDEO_PLAYBACK_AFTER_PAUSED";
	public final static String ACTION_VIDEO_PLAYBACK_SEEK = "com.phicomm.dlna.action.ACTION_VIDEO_PLAYBACK_SEEK";
	public final static String ACTION_VIDEO_PLAYBACK_SET_VOLUME = "com.phicomm.dlna.action.ACTION_VIDEO_PLAYBACK_SET_VOLUME";
	public final static String ACTION_VIDEO_PLAYBACK_SET_MUTE = "com.phicomm.dlna.action.ACTION_VIDEO_PLAYBACK_SET_MUTE";

	private MyVideoView videoView;
	private Uri mUri;

	private int mDuration;

	private AudioManager mAudioManager;
	// create a handler to refresh the seek bar every 0.5 second
	private Handler mProgressRefresher;

	private DlnaApplication mApplication;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (DlnaApplication) getApplication();
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		Intent intent = getIntent();
		if (intent == null) {
			finish();
			return;
		}
		mUri = intent.getData();
		if (mUri == null) {
			Log.e(TAG, "the mUri is null");
			finish();
			return;
		}
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video_play_layout);

		mProgressRefresher = new Handler();

		videoView = (MyVideoView) findViewById(R.id.VideoView01);
		videoView.setMediaController(new MediaController(
				VideoPlayActivity.this));
		videoView.requestFocus();
		videoView.setOnPreparedListener(this);
		videoView.setVideoURI(mUri);

		IntentFilter f = new IntentFilter();
		// register the renderer control broadcast receiver
		f.addAction(ACTION_VIDEO_PAUSE_PLAYBACK);
		f.addAction(ACTION_VIDEO_PLAYBACK_AFTER_PAUSED);
		f.addAction(ACTION_VIDEO_PLAYBACK_SEEK);
		f.addAction(ACTION_VIDEO_PLAYBACK_SET_VOLUME);
		registerReceiver(mVideoRenderListener, f);

	}

	private BroadcastReceiver mVideoRenderListener = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.e(TAG, "onReceive()");
			String action = intent.getAction();
			if (ACTION_VIDEO_PAUSE_PLAYBACK.equals(action)
					|| ACTION_VIDEO_PLAYBACK_AFTER_PAUSED.equals(action)) {
				playPauseClicked(null);
			} else if (ACTION_VIDEO_PLAYBACK_SEEK.equals(action)) {
				if (videoView != null) {
					int progress = (int) intent.getLongExtra("timePosition", 0);
					videoView.seekTo(progress);
				}
			} else if (ACTION_VIDEO_PLAYBACK_SET_VOLUME.equals(action)) {
				if (videoView != null && mAudioManager != null) {
					int currentVolume = (int) intent.getLongExtra(
							"currentVolume", 0);
//					mApplication.setCurrentVolume(currentVolume);
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							currentVolume, 0);
				}
			} else if (ACTION_VIDEO_PLAYBACK_SET_MUTE.equals(action)) {
				if (videoView != null && mAudioManager != null) {
					boolean isMute = intent.getBooleanExtra("isMute", false);
					mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC,
							isMute);
				}
			}
		}

	};

	@Override
	public Object onRetainNonConfigurationInstance() {
		VideoView vView = videoView;
		videoView = null;
		return vView;
	}

	@Override
	public void onDestroy() {
		stopPlayback();
		unregisterReceiver(mVideoRenderListener);
		super.onDestroy();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	private void stopPlayback() {
		if (mProgressRefresher != null) {
			mProgressRefresher.removeCallbacksAndMessages(null);
		}
		if (videoView != null) {
			videoView.stopPlayback();
			videoView = null;
		}
	}

	@Override
	public void onUserLeaveHint() {
		stopPlayback();
		finish();
		super.onUserLeaveHint();
	}

	private void start() {
		videoView.start();
//		mApplication.setCurrentVolume(mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
		mProgressRefresher.postDelayed(new ProgressRefresher(), 20);
	}

	public void playPauseClicked(View v) {
		if (videoView != null && videoView.isPlaying()) {
			videoView.pause();
//			mApplication.setCurrentTransportState(TransportState.PAUSED_PLAYBACK);
		} else if (videoView != null) {
			start();
//			mApplication.setCurrentTransportState(TransportState.PLAYING);
		}
	}

	class ProgressRefresher implements Runnable {

		public void run() {
			if (videoView != null) {
				long currPos = videoView.getCurrentPosition();
//				mApplication.setCurrentPosition(currPos / 1000);
			}
			mProgressRefresher.removeCallbacksAndMessages(null);
			mProgressRefresher.postDelayed(new ProgressRefresher(), 500);
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		Log.e(TAG, "onPrepared()");
		mDuration = mp.getDuration();
//		mApplication.setDuration(mDuration);
		start();
	}
	
}
