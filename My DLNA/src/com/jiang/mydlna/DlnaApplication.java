package com.jiang.mydlna;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.model.meta.Device;

import android.app.Application;

public class DlnaApplication extends Application {

	private UpnpService upnpService;

	private Device mSelectedDevice;

	public UpnpService getUpnpService() {
		return upnpService;
	}

	public void setUpnpService(UpnpService upnpService) {
		this.upnpService = upnpService;
	}

	public Device getmSelectedDevice() {
		return mSelectedDevice;
	}

	public void setmSelectedDevice(Device mSelectedDevice) {
		this.mSelectedDevice = mSelectedDevice;
	}
}
