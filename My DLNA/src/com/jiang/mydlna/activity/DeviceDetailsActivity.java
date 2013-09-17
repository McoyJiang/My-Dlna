package com.jiang.mydlna.activity;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.jiang.mydlna.DlnaApplication;
import com.jiang.mydlna.activity.callbacks.MyContentBrowseActionCallback;
import com.jiang.mydlna.modle.ContentItem;
import com.jiang.mydlna.utils.ContentItemUtil;

public class DeviceDetailsActivity extends ListActivity {

	private final String MUSIC_PLAYER_ACTION = "com.jiang.dlna.action.MUSIC_VIEW";
	private final String VIDEO_PLAYER_ACTION = "com.jiang.dlna.action.VIDEO_VIEW";
	
	private UpnpService upnpService;
	
	private ArrayAdapter<ContentItem> mListAdapter;
	
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			ContentItem content = mListAdapter.getItem(position);
			if (content.isContainer()) {
				upnpService.getControlPoint().execute(
						new MyContentBrowseActionCallback(DeviceDetailsActivity.this,
								content.getService(), content.getContainer(),
								mListAdapter));
			} else {
				Item item = (Item)content.getItem();
				String uri = item.getFirstResource().getValue();
				Log.e("DeviceDetailsActivity", "the uri is " + uri);
				if(ContentItemUtil.isAudioItem(item)) {
					Intent intent = new Intent(MUSIC_PLAYER_ACTION);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setDataAndType(Uri.parse(uri), "audio/*");
					startActivity(intent);
				} else if(ContentItemUtil.isVideoItem(item)) {
					Intent intent = new Intent(VIDEO_PLAYER_ACTION);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setDataAndType(Uri.parse(uri), "video/*");
					startActivity(intent);
				}else if(ContentItemUtil.isPictureItem(item)) {
					
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mListAdapter = new ArrayAdapter<ContentItem>(this, android.R.layout.simple_list_item_1);
		Device currentDevice = ((DlnaApplication)getApplication()).getmSelectedDevice();
		Service service = currentDevice.findService(new UDAServiceType(
		"ContentDirectory"));
		upnpService = ((DlnaApplication)getApplication()).getUpnpService();
		upnpService.getControlPoint().execute(
		new MyContentBrowseActionCallback(DeviceDetailsActivity.this, service,
				createRootContainer(service), mListAdapter));
		
		setListAdapter(mListAdapter);
		getListView().setOnItemClickListener(mOnItemClickListener);
	}
	
	protected Container createRootContainer(Service service) {
		Container rootContainer = new Container();
		rootContainer.setId("0");
		rootContainer.setTitle("Content Directory on "
				+ service.getDevice().getDisplayString());
		return rootContainer;
	}
}
