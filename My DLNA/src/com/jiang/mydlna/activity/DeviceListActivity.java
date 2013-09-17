package com.jiang.mydlna.activity;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.jiang.mydlna.DlnaApplication;
import com.jiang.mydlna.R;
import com.jiang.mydlna.modle.DeviceItem;
import com.jiang.mydlna.service.DlnaUpnpInitService;

public class DeviceListActivity extends ListActivity {
	private final String TAG = "DeviceListActivity";
	
	protected UpnpService mUpnpService;
	
	private ArrayAdapter<DeviceItem> mListAdapter;
	
	private BrowseRegistryListener mRegistryListener = new BrowseRegistryListener();
	
	private ServiceConnection serviceConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.e(TAG, "Connected to UPnP Service");
			DlnaUpnpInitService.UpnpBinder binder = (DlnaUpnpInitService.UpnpBinder) service;
			mUpnpService = binder.getAndroidUpnpService();
			if (mUpnpService == null) {
				Log.e(TAG, "mUpnpService is null");
				return;
			} else {
				((DlnaApplication)getApplication()).setUpnpService(mUpnpService);
			}
			LocalDevice loalServer = binder.getLocalServer();
			LocalDevice localRenderer = binder.getLocalRenderer();
			if(loalServer != null && localRenderer != null) {
				Log.e(TAG, "the local device is not null!!");
			}
			
			// Getting ready for future device advertisements
			mUpnpService.getRegistry().addListener(mRegistryListener);

			// Search asynchronously for all devices
			mUpnpService.getControlPoint().search();
			
			mUpnpService.getRegistry().addDevice(binder.getLocalServer());
			mUpnpService.getRegistry().addDevice(binder.getLocalRenderer());
		}
		 
		public void onServiceDisconnected(ComponentName className) {
			mUpnpService = null;
		}
	};
	
	OnItemClickListener mDeviceItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			Log.e(TAG, "onItemClick()");
			Device device = mListAdapter.getItem(position).getDevice();
			Service ser = device.findService(new UDAServiceType("ContentDirectory"));
			for(Action action : ser.getActions()) {
				Log.e(TAG, "the selected action is " + action);
			}
			((DlnaApplication)getApplication()).setmSelectedDevice(device);
			Intent intent = new Intent(DeviceListActivity.this, DeviceDetailsActivity.class);
			startActivity(intent);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		boolean isWifiConnected = isWifiConnected();
		if(!isWifiConnected) {
			String dialogMessage = "Wifi is not opened, shall we open wifi?";
            AlertDialog.Builder b = new AlertDialog.Builder(this).setTitle(R.string.app_name)
                    .setMessage(dialogMessage).setPositiveButton("OK", new OnClickListener() {                                                           
                        @Override                      
                        public void onClick(DialogInterface dialog, int which) {
                        	DeviceListActivity.this.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                            finish();
                        }
                    }).setNegativeButton("Cancel", new OnClickListener() {                                                                                    
                        @Override                      
                        public void onClick(DialogInterface dialog, int which) {                                                                                         
                        	DeviceListActivity.this.finish();                  
                        }
                    });
            b.show();
		} else {
			mListAdapter = new ArrayAdapter<DeviceItem>(this,
					android.R.layout.simple_list_item_1);
			
			setListAdapter(mListAdapter);
			getListView().setOnItemClickListener(mDeviceItemClickListener);
			
			getApplicationContext().bindService(new Intent(DeviceListActivity.this, DlnaUpnpInitService.class),
    				serviceConnection, Context.BIND_AUTO_CREATE);
		}
	}

	private boolean isWifiConnected() {
		WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		return wifiManager.isWifiEnabled();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mUpnpService != null) {
			mUpnpService.getRegistry().removeAllLocalDevices();
		}
	}
	
	class BrowseRegistryListener extends DefaultRegistryListener {

		@Override
		public void remoteDeviceDiscoveryStarted(Registry registry,
				RemoteDevice device) {
			deviceAdded(device);
		}

		@Override
		public void remoteDeviceDiscoveryFailed(Registry registry,
				final RemoteDevice device, final Exception ex) {
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(
							DeviceListActivity.this,
							"Discovery failed of '"
									+ device.getDisplayString()
									+ "': "
									+ (ex != null ? ex.toString()
											: "Couldn't retrieve device/service descriptors"),
							Toast.LENGTH_LONG).show();
				}
			});
			deviceRemoved(device);
		}

		@Override
		public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
			Log.e(TAG, "remoteDeviceAdded()");
			for(Service service : device.getServices()) {
				Log.e(TAG, "remote device found : " + service.getServiceType().getType());
			}
			deviceAdded(device);
		}

		@Override
		public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
			deviceRemoved(device);
		}

		@Override
		public void localDeviceAdded(Registry registry, LocalDevice device) {
//			deviceAdded(device);
		}

		@Override
		public void localDeviceRemoved(Registry registry, LocalDevice device) {
			deviceRemoved(device);
		}
		
		public void deviceAdded(final Device device) {
			runOnUiThread(new Runnable() {
				public void run() {
					DeviceItem d = new DeviceItem(device);
					int position = mListAdapter.getPosition(d);
					if (position >= 0) {
						// Device already in the list, re-set new value at same
						// position
						mListAdapter.remove(d);
						mListAdapter.insert(d, position);
					} else {
						mListAdapter.add(d);
					}
				}
			});
		}

		public void deviceRemoved(final Device device) {
			runOnUiThread(new Runnable() {
				public void run() {
					mListAdapter.remove(new DeviceItem(device));
				}
			});
		}
		
	}

}
