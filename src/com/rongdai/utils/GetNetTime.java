package com.rongdai.utils;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间日期工具类
 * @author 郑阳阳
 *
 */
public class GetNetTime {
	
	private static String dayStr;
	private static String hourStr;
	private static String minuteStr;

	private static String userTime;
	/**
	 * 获取网络时间
	 * @throws Exception
	 */
	public static String  getTime() throws Exception {
		new Thread(){
			public void run() {
				
				URL url;
				try {
					url = new URL("http://www.bjtime.cn");
					
					URLConnection uc = url.openConnection();// 生成连接对象
					
					uc.connect(); // 发出连接
					
					long ld = uc.getDate(); // 取得网站日期时间
					
					Date date = new Date(ld); // 转换为标准时间对象
					
					SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 24小时制,12小时制则HH为hh
					
					userTime = sdformat.format(date);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}// 取得资源对象
			};
		}.start();
		return userTime;
	}

	/**
	 * 将String类型的日期转换成时间
	 * @param oldData String类型日期
	 * @return 返回时间
	 * @throws ParseException
	 */
	public static Long DataToTime(String oldData) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		Date date = format.parse(oldData);
		//System.out.print("Format To times:" + date.getTime());
		return date.getTime();
	}

	/**
	 * 将str类型的时间转换成日期
	 * @param str
	 * @return
	 */
	public static String StrToData(String str) {
		Long ld = new Long(str);
		Date date = new Date(ld); // 转换为标准时间对象

		SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");// 24小时制,12小时制则HH为hh

		String UserTime = sdformat.format(date);
		//System.out.println(UserTime + "");
		return UserTime;
	}
	
	
	/** 
	 * 计算相差时间
	 * @param currentTime 当前时间 
	 * @param willTime 将来的时间
	 * @return 返回时间差
	 * @throws ParseException
	 */
	public static long showTime(String currentTime, String willTime) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = dateFormat.parse(currentTime);
		Date date2 = dateFormat.parse(willTime);
		//System.out.println(date.getTime());
		//System.out.println(date2.getTime());
		long time = date.getTime()-date2.getTime();
//		long day = time/(24*60*60000);
//		long hour = (time%(24*60*60000))/(60*60000);
//		long minute = ((time%(24*60*60000))%(60*60000))/600000;
//		if(day == 0){
//			dayStr = "";
//		}else{
//			dayStr = day + "天";
//		}
//		if(hour == 0){
//			hourStr = "";
//		}else{
//			hourStr = hour+"小时";
//		}
//		if(minute == 0){
//			minuteStr = "";
//		}else{
//			minuteStr = minute+"分";
//		}
		return time;
	}

}
