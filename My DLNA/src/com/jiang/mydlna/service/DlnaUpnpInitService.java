package com.jiang.mydlna.service;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.android.AndroidUpnpServiceConfiguration;
import org.fourthline.cling.android.AndroidWifiSwitchableRouter;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ModelUtil;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.protocol.ProtocolFactory;
import org.fourthline.cling.registry.Registry;
import org.fourthline.cling.transport.Router;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;


public class DlnaUpnpInitService extends Service {
	private final String TAG = "DlnaUpnpInitService";
	
	private final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	private final String MODEL_NAME = "MediaServer";
	private final String MODEL_DESCRIPTION = "MediaServer for Android";
	private final String MODEL_NUMBER = "v1";

	protected UpnpService mUpnpService;

	private LocalDevice mLocalDevice;
	private LocalDevice mLocalRendererDevice;
	
	private UpnpBinder binder = new UpnpBinder();

	@Override
	public void onCreate() {
		super.onCreate();
		final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		mUpnpService = new UpnpServiceImpl(createConfiguration(wifiManager)) {
			@Override
			protected Router createRouter(ProtocolFactory protocolFactory,
					Registry registry) {
				AndroidWifiSwitchableRouter router = DlnaUpnpInitService.this
						.createRouter(getConfiguration(), protocolFactory,
								wifiManager, connectivityManager);
				if (!ModelUtil.ANDROID_EMULATOR) {
					// Only register for network connectivity changes if we are
					// not running on emulator
					registerReceiver(router.getBroadcastReceiver(),
							new IntentFilter(CONNECTIVITY_CHANGE_ACTION));
				}
				return router;
			}

		};
		createDevice();
	}
	
	private void createDevice() {
		DeviceType typeServer = new UDADeviceType("MediaServer", 1);
		DeviceType typeRenderer = new UDADeviceType("MediaRenderer", 1);

		// create device's UDN.
		UDN mUDN = UDN.uniqueSystemIdentifier("Jiang-MediaServer");
		UDN mRendererUDN = UDN.uniqueSystemIdentifier("Jiang-MediaRenderer");
		
		// create device's Details.
				DeviceDetails details = new DeviceDetails(android.os.Build.MODEL,
						new ManufacturerDetails(android.os.Build.MANUFACTURER),
						new ModelDetails(MODEL_NAME, MODEL_DESCRIPTION, MODEL_NUMBER));
				
		// create device's loacl service.
		LocalService<MyContentDirectoryService> contentService = new AnnotationLocalServiceBinder()
				.read(MyContentDirectoryService.class);
		contentService
				.setManager(new DefaultServiceManager<MyContentDirectoryService>(
						contentService, MyContentDirectoryService.class) {
					@Override
					protected MyContentDirectoryService createServiceInstance()
							throws Exception {
						return new MyContentDirectoryService();
					}
				});
		
		LocalService<MyAVTransportService> transportService = new AnnotationLocalServiceBinder()
				.read(MyAVTransportService.class);
		transportService
				.setManager(new DefaultServiceManager<MyAVTransportService>(
						transportService, MyAVTransportService.class) {
					@Override
					protected MyAVTransportService createServiceInstance()
							throws Exception {
						return new MyAVTransportService(getApplicationContext());
					}
				});
		
		LocalService<MyRendererControlService> renderService = new AnnotationLocalServiceBinder()
				.read(MyRendererControlService.class);
		renderService
				.setManager(new DefaultServiceManager<MyRendererControlService>(
						renderService, MyRendererControlService.class) {
					@Override
					protected MyRendererControlService createServiceInstance()
							throws Exception {
						return new MyRendererControlService();
					}
				});
		
		// create a loacl device has a DMS.
		try {
			mLocalDevice = new LocalDevice(new DeviceIdentity(mUDN), typeServer,
					details, contentService);
			
			mLocalRendererDevice = new LocalDevice(
					new DeviceIdentity(mRendererUDN), typeRenderer, details,
					new LocalService[] { renderService, transportService });
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

	protected AndroidUpnpServiceConfiguration createConfiguration(
			WifiManager wifiManager) {
		// find which service devices, if not implement getExclusiveServiceTypes function,
		// all upnp device in the net work can be got.
		return new AndroidUpnpServiceConfiguration(wifiManager) {
		 @Override
		 public ServiceType[] getExclusiveServiceTypes() {
		         return new ServiceType[] { 
		        		 new UDAServiceType("AVTransport"), 
		        		 new UDAServiceType("ContentDirectory"),
		                 new UDAServiceType("RenderingControl") };
		 }
		};
	}

	protected AndroidWifiSwitchableRouter createRouter(
			UpnpServiceConfiguration configuration,
			ProtocolFactory protocolFactory, WifiManager wifiManager,
			ConnectivityManager connectivityManager) {
		return new AndroidWifiSwitchableRouter(configuration, protocolFactory,
				wifiManager, connectivityManager);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public class UpnpBinder extends Binder{
		public UpnpService getAndroidUpnpService() {
			return mUpnpService != null ? mUpnpService : null;
		}
		
		public LocalDevice getLocalServer() {
			return mLocalDevice != null ? mLocalDevice : null;
		}
		
		public LocalDevice getLocalRenderer() {
			return mLocalRendererDevice != null ? mLocalRendererDevice : null;
		}
	}
}
