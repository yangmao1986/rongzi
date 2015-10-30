package com.rongdai.utils;

import com.rongdai.base.BaseApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 管理配置文件的类，可用setValue来存储键值对，用getValue来获取值。
 * @author 郑阳阳
 *
 */
public class ConfigFileUtils {
	private static SharedPreferences sp;
	/**
	 * 根据键来获取配置文件中的值
	 * @param fileName 配置文件名
	 * @param key 存储时使用的键
	 * @param defValue 当没有时默认返回的值
	 * @return
	 */
	public static String getValue(String fileName,String key,String defValue){
		sp = BaseApplication.getContext().getSharedPreferences(fileName, BaseApplication.MODE_PRIVATE);
		String value = sp.getString(key, defValue);
		return value;
	}
	
	
	/**
	 * 向配置文件中存储键值对
	 * @param fileName 配置文件名
	 * @param key 存储的键
	 * @param value 存储的值
	 */
	public static void setValue(String fileName,String key,String value){
		sp = BaseApplication.getContext().getSharedPreferences(fileName, BaseApplication.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	/**
	 * 清楚配置文件中的内容
	 * @param fileName 配置文件名
	 */
	public static void clearConfigFile(String fileName){
		sp = BaseApplication.getContext().getSharedPreferences(fileName, BaseApplication.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.clear();
		edit.commit();
	}
}
