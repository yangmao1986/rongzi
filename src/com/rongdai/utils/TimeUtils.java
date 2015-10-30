package com.rongdai.utils;

public class TimeUtils {
	public static String getFomatTime(long time){
		long day = time/(60*60*24);
    	long hour = (time%(60*60*24))/(60*60);
    	long mins = ((time%(60*60*24))%(60*60))/60;
    	long sencend = ((time%(60*60*24))%(60*60))%60;
		return day+"天"+hour+"小时"+mins+"分"+sencend+"秒";
	}
}
