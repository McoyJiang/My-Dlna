package com.jiang.mydlna.service;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.support.avtransport.AVTransportException;
import org.fourthline.cling.support.avtransport.AbstractAVTransportService;
import org.fourthline.cling.support.lastchange.LastChange;
import org.fourthline.cling.support.model.DIDLObject.Class;
import org.fourthline.cling.support.model.DeviceCapabilities;
import org.fourthline.cling.support.model.MediaInfo;
import org.fourthline.cling.support.model.PositionInfo;
import org.fourthline.cling.support.model.TransportAction;
import org.fourthline.cling.support.model.TransportInfo;
import org.fourthline.cling.support.model.TransportSettings;
import org.fourthline.cling.support.model.item.Item;

import com.jiang.mydlna.activity.MusicPlayerActivity;
import com.jiang.mydlna.activity.VideoPlayActivity;
import com.jiang.mydlna.utils.ContentItemUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class MyAVTransportService extends AbstractAVTransportService {
	private final String TAG = "MyAVTransportService";
	
	// the boolean value that used to say wheather the player is playing
	private boolean hasBeenCreated = false;
	// this indicate which activity we should turn to due to the uri
	private int INTENT_ACTIVITY = 0;
	private final int INTENT_TO_PHOTO = 1;
	private final int INTENT_TO_MUSIC = 2;
	private final int INTENT_TO_VIDEO = 3;
	
	private String mCurrentUri;
	
	private Context mContext;
	
	public MyAVTransportService(Context context) {
		mContext = context;
	}
	
	@Override
	public UnsignedIntegerFourBytes[] getCurrentInstanceIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected TransportAction[] getCurrentTransportActions(
			UnsignedIntegerFourBytes arg0) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@UpnpAction(out = {
			@UpnpOutputArgument(name = "PlayMedia", stateVariable = "PossiblePlaybackStorageMedia", getterName = "getPlayMediaString"),
			@UpnpOutputArgument(name = "RecMedia", stateVariable = "PossibleRecordStorageMedia", getterName = "getRecMediaString"),
			@UpnpOutputArgument(name = "RecQualityModes", stateVariable = "PossibleRecordQualityModes", getterName = "getRecQualityModesString") })
	public DeviceCapabilities getDeviceCapabilities(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0)
			throws AVTransportException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@UpnpAction(out = {
			@UpnpOutputArgument(name = "NrTracks", stateVariable = "NumberOfTracks", getterName = "getNumberOfTracks"),
			@UpnpOutputArgument(name = "MediaDuration", stateVariable = "CurrentMediaDuration", getterName = "getMediaDuration"),
			@UpnpOutputArgument(name = "CurrentURI", stateVariable = "AVTransportURI", getterName = "getCurrentURI"),
			@UpnpOutputArgument(name = "CurrentURIMetaData", stateVariable = "AVTransportURIMetaData", getterName = "getCurrentURIMetaData"),
			@UpnpOutputArgument(name = "NextURI", stateVariable = "NextAVTransportURI", getterName = "getNextURI"),
			@UpnpOutputArgument(name = "NextURIMetaData", stateVariable = "NextAVTransportURIMetaData", getterName = "getNextURIMetaData"),
			@UpnpOutputArgument(name = "PlayMedium", stateVariable = "PlaybackStorageMedium", getterName = "getPlayMedium"),
			@UpnpOutputArgument(name = "RecordMedium", stateVariable = "RecordStorageMedium", getterName = "getRecordMedium"),
			@UpnpOutputArgument(name = "WriteStatus", stateVariable = "RecordMediumWriteStatus", getterName = "getWriteStatus") })
	public MediaInfo getMediaInfo(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0)
			throws AVTransportException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@UpnpAction(out = {
			@UpnpOutputArgument(name = "Track", stateVariable = "CurrentTrack", getterName = "getTrack"),
			@UpnpOutputArgument(name = "TrackDuration", stateVariable = "CurrentTrackDuration", getterName = "getTrackDuration"),
			@UpnpOutputArgument(name = "TrackMetaData", stateVariable = "CurrentTrackMetaData", getterName = "getTrackMetaData"),
			@UpnpOutputArgument(name = "TrackURI", stateVariable = "CurrentTrackURI", getterName = "getTrackURI"),
			@UpnpOutputArgument(name = "RelTime", stateVariable = "RelativeTimePosition", getterName = "getRelTime"),
			@UpnpOutputArgument(name = "AbsTime", stateVariable = "AbsoluteTimePosition", getterName = "getAbsTime"),
			@UpnpOutputArgument(name = "RelCount", stateVariable = "RelativeCounterPosition", getterName = "getRelCount"),
			@UpnpOutputArgument(name = "AbsCount", stateVariable = "AbsoluteCounterPosition", getterName = "getAbsCount") })
	public PositionInfo getPositionInfo(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0)
			throws AVTransportException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@UpnpAction(out = {
			@UpnpOutputArgument(name = "CurrentTransportState", stateVariable = "TransportState", getterName = "getCurrentTransportState"),
			@UpnpOutputArgument(name = "CurrentTransportStatus", stateVariable = "TransportStatus", getterName = "getCurrentTransportStatus"),
			@UpnpOutputArgument(name = "CurrentSpeed", stateVariable = "TransportPlaySpeed", getterName = "getCurrentSpeed") })
	public TransportInfo getTransportInfo(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0)
			throws AVTransportException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@UpnpAction(out = {
			@UpnpOutputArgument(name = "PlayMode", stateVariable = "CurrentPlayMode", getterName = "getPlayMode"),
			@UpnpOutputArgument(name = "RecQualityMode", stateVariable = "CurrentRecordQualityMode", getterName = "getRecQualityMode") })
	public TransportSettings getTransportSettings(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0)
			throws AVTransportException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@UpnpAction
	public void next(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0)
			throws AVTransportException {
		// TODO Auto-generated method stub

	}

	@Override
	@UpnpAction
	public void pause(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0)
			throws AVTransportException {
		Log.e(TAG, "pause()");
	}

	@Override
	@UpnpAction
	public void play(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0,
			@UpnpInputArgument(name = "Speed", stateVariable = "TransportPlaySpeed") String arg1)
			throws AVTransportException {
		Log.e(TAG, "play()---the arg0 = " + arg0 + " arg1 = " + arg1);
		Log.e(TAG, "the INTENT_ACTIVITY = " + INTENT_ACTIVITY);
		switch (INTENT_ACTIVITY) {
		case INTENT_TO_PHOTO:
			turnToPhotoActivity();
			break;
		case INTENT_TO_MUSIC:
			turnToMusicActivity();
			break;
		case INTENT_TO_VIDEO:
			turnToVedioActivity();
			break;
		}
	}

	@Override
	@UpnpAction
	public void previous(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0)
			throws AVTransportException {
		// TODO Auto-generated method stub

	}

	@Override
	@UpnpAction
	public void record(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0)
			throws AVTransportException {
		// TODO Auto-generated method stub

	}

	@Override
	@UpnpAction
	public void seek(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0,
			@UpnpInputArgument(name = "Unit", stateVariable = "A_ARG_TYPE_SeekMode") String arg1,
			@UpnpInputArgument(name = "Target", stateVariable = "A_ARG_TYPE_SeekTarget") String arg2)
			throws AVTransportException {
		// TODO Auto-generated method stub

	}

	@Override
	@UpnpAction
	public void setAVTransportURI(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0,
			@UpnpInputArgument(name = "CurrentURI", stateVariable = "AVTransportURI") String uri,
			@UpnpInputArgument(name = "CurrentURIMetaData", stateVariable = "AVTransportURIMetaData") String arg2)
			throws AVTransportException {
		Log.e(TAG, "setAVTransportURI()--the uri is " + uri + " and arg2 is " + arg2);
		hasBeenCreated = false;
		mCurrentUri = uri;
		Class clazz = new Class(arg2);
		Item item = new Item();
		item.setClazz(clazz);
		String objectClass = item.getClazz().getValue();
		if (ContentItemUtil.isAudioItem(item)) {
			INTENT_ACTIVITY = INTENT_TO_MUSIC;
		} else if (ContentItemUtil.isPictureItem(item)) {
			INTENT_ACTIVITY = INTENT_TO_PHOTO;
		} else if (ContentItemUtil.isVideoItem(item)) {
			INTENT_ACTIVITY = INTENT_TO_VIDEO;
		}
	}

	@Override
	@UpnpAction
	public void setNextAVTransportURI(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0,
			@UpnpInputArgument(name = "NextURI", stateVariable = "AVTransportURI") String arg1,
			@UpnpInputArgument(name = "NextURIMetaData", stateVariable = "AVTransportURIMetaData") String arg2)
			throws AVTransportException {
		// TODO Auto-generated method stub

	}

	@Override
	@UpnpAction
	public void setPlayMode(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0,
			@UpnpInputArgument(name = "NewPlayMode", stateVariable = "CurrentPlayMode") String arg1)
			throws AVTransportException {
		// TODO Auto-generated method stub

	}

	@Override
	@UpnpAction
	public void setRecordQualityMode(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0,
			@UpnpInputArgument(name = "NewRecordQualityMode", stateVariable = "CurrentRecordQualityMode") String arg1)
			throws AVTransportException {
		// TODO Auto-generated method stub

	}

	@Override
	@UpnpAction
	public void stop(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0)
			throws AVTransportException {
		Log.e(TAG, "stop()");
	}
	
	private void turnToVedioActivity() {
		if (!hasBeenCreated) {
			Intent intent = new Intent("com.jiang.dlna.action.VIDEO_VIEW");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse(mCurrentUri), "video/*");
			mContext.startActivity(intent);
			hasBeenCreated = true;
		}else {
			String action = VideoPlayActivity.ACTION_VIDEO_PAUSE_PLAYBACK;
			Intent intent = new Intent(action);
			mContext.sendBroadcast(intent);
		}
	}

	private void turnToPhotoActivity() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse(mCurrentUri), "image/*");
		mContext.startActivity(intent);
	}

	private void turnToMusicActivity() {
		if (!hasBeenCreated) {
			Intent intent = new Intent("com.jiang.dlna.action.MUSIC_VIEW");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.parse(mCurrentUri), "audio/*");
			mContext.startActivity(intent);
			hasBeenCreated = true;
		} else {
			String action = MusicPlayerActivity.ACTION_AUDIO_PAUSE_PLAYBACK;
			Intent intent = new Intent(action);
			mContext.sendBroadcast(intent);
		}
	}


}
