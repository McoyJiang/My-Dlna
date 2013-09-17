package com.jiang.mydlna.service;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
import org.fourthline.cling.model.types.UnsignedIntegerTwoBytes;
import org.fourthline.cling.support.lastchange.LastChange;
import org.fourthline.cling.support.model.Channel;
import org.fourthline.cling.support.renderingcontrol.AbstractAudioRenderingControl;
import org.fourthline.cling.support.renderingcontrol.RenderingControlException;

import android.content.Context;

public class MyRendererControlService extends AbstractAudioRenderingControl {

	@Override
	public UnsignedIntegerFourBytes[] getCurrentInstanceIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Channel[] getCurrentChannels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@UpnpAction(out = @UpnpOutputArgument(name = "CurrentMute", stateVariable = "Mute"))
	public boolean getMute(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0,
			@UpnpInputArgument(name = "Channel") String arg1)
			throws RenderingControlException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@UpnpAction(out = @UpnpOutputArgument(name = "CurrentVolume", stateVariable = "Volume"))
	public UnsignedIntegerTwoBytes getVolume(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0,
			@UpnpInputArgument(name = "Channel") String arg1)
			throws RenderingControlException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@UpnpAction
	public void setMute(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0,
			@UpnpInputArgument(name = "Channel") String arg1,
			@UpnpInputArgument(name = "DesiredMute", stateVariable = "Mute") boolean arg2)
			throws RenderingControlException {
		// TODO Auto-generated method stub

	}

	@Override
	@UpnpAction
	public void setVolume(
			@UpnpInputArgument(name = "InstanceID") UnsignedIntegerFourBytes arg0,
			@UpnpInputArgument(name = "Channel") String arg1,
			@UpnpInputArgument(name = "DesiredVolume", stateVariable = "Volume") UnsignedIntegerTwoBytes arg2)
			throws RenderingControlException {
		// TODO Auto-generated method stub

	}

}
