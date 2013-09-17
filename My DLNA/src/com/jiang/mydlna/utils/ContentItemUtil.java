package com.jiang.mydlna.utils;
import java.util.Calendar;

import org.fourthline.cling.support.model.item.Item;
//format Item's size or duration time, judge whether a Item is videoItem, audioItem or imageItem
public class ContentItemUtil {
	public final static String DLNA_OBJECTCLASS_CONTAINERID = "object.container";
	public final static String DLNA_OBJECTCLASS_MUSICID = "object.item.audioItem";
	public final static String DLNA_OBJECTCLASS_VIDEOID = "object.item.videoItem";
	public final static String DLNA_OBJECTCLASS_PHOTOID = "object.item.imageItem";
	private static final String COLON = ":";
	private static final String DASH = "-";
	
	public static int formatSizeString(String sizeString){
		int size = 0;
		if (sizeString == null || sizeString.length() < 1){
			return size;
		}
		try {
			size =Integer.parseInt(sizeString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	// format time to YYYY-MM-DD
	public static  long formatTimeString(String timeString){
		long time = 0;
		if (timeString == null || timeString.length() < 1){
			return time;
		}
		try {
			String []array = timeString.split(DASH);
			int year = Integer.valueOf(array[0]);
			int month = Integer.valueOf(array[1]);
			int day = Integer.valueOf(array[2]);
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, day);
			
			time = calendar.getTimeInMillis();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return time;
	}

	//format string type time HH:MM:SS to int type time
	public static int formatDurationString(String durationString){
		int duration = 0;
		if (durationString == null || durationString.length() == 0){
			return duration;
		}
		
		try {
			String sArray[] = durationString.split(COLON);
			double hour = Double.valueOf(sArray[0]);
			double minute = Double.valueOf(sArray[1]);
			double second = Double.valueOf(sArray[2]);		
			
			return (int) (((hour * 60 + minute) * 60 + second) * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return duration;
	}
	
	//format long type time to MM:SS string type time
	public static String formateTime(long millis) {
		String formatResult = "";
		String formatExpression = "%02d:%02d";
		int time = (int) (millis / 1000);
		int second = time % 60;
		int minute = time / 60;

		formatResult = String.format(formatExpression, minute, second);

		return formatResult;

	}
	
	public static String formatSecondToHHMMSS(long s) {
		int hours;
		int minutes;
		int seconds;
		
		hours = (int)(s/3600);
		int remainder = (int)(s%3600);
		minutes = remainder/60;
		seconds = remainder%60;
		return (hours<10 ? "0" : "")+hours+":"
				+(minutes<10 ? "0" : "")+minutes+":"
				+(seconds<10 ? "0" : "")+seconds; 

	}
	
	public static boolean isAudioItem(Item item) {
		String objectClass = item.getClazz().getValue();
		if (objectClass != null && objectClass.contains(DLNA_OBJECTCLASS_MUSICID)) {
			return true;
		}		
		return false;
	}
	
	public static boolean isAudioItem(String itemClass) {
		if (itemClass != null && itemClass.contains(DLNA_OBJECTCLASS_MUSICID)) {
			return true;
		}		
		return false;
	}
	
	public static boolean isVideoItem(Item item) {
		String objectClass = item.getClazz().getValue();
		if (objectClass != null && objectClass.contains(DLNA_OBJECTCLASS_VIDEOID)) {
			return true;
		}		
		return false;
	}
	
	public static boolean isVideoItem(String itemClass) {
		if (itemClass != null && itemClass.contains(DLNA_OBJECTCLASS_VIDEOID)) {
			return true;
		}		
		return false;
	}
	
	public static boolean isPictureItem(Item item) {
		String objectClass = item.getClazz().getValue();
		if (objectClass != null && objectClass.contains(DLNA_OBJECTCLASS_PHOTOID))	{
			return true;
		}		
		return false;
	}
	
	public static boolean isPictureItem(String itemClass) {
		if (itemClass != null && itemClass.contains(DLNA_OBJECTCLASS_PHOTOID))	{
			return true;
		}		
		return false;
	}
	
}