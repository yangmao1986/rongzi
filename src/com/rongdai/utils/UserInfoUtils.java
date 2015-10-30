package com.rongdai.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 获取用户信息的工具类
 * 
 * @author Administrator
 * 
 */
public class UserInfoUtils {
	private static SharedPreferences sp;

	/**
	 * 获取电话号码
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserPhone(Context context) {
		if (sp == null) {
			sp = context.getSharedPreferences(PersonInfoConstans.PERSONINFO,
					Context.MODE_PRIVATE);
		}
		return sp.getString(PersonInfoConstans.PHONE, "");
	}

	/**
	 * 获取用户名
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserName(Context context) {
		if (sp == null) {
			sp = context.getSharedPreferences(PersonInfoConstans.PERSONINFO,
					Context.MODE_PRIVATE);
		}
		return sp.getString(PersonInfoConstans.USER_NAME, "");
	}

	/**
	 * 获取密码
	 * 
	 * @param context
	 * @return
	 */
	public static String getPassword(Context context) {
		if (sp == null) {
			sp = context.getSharedPreferences(PersonInfoConstans.PERSONINFO,
					Context.MODE_PRIVATE);
		}
		return sp.getString(PersonInfoConstans.PASSWORD, "");
	}

	/**
	 * 获取融贷账号
	 * 
	 * @param context
	 * @return
	 */
	public static String getRongdaiAccount(Context context) {
		if (sp == null) {
			sp = context.getSharedPreferences(PersonInfoConstans.PERSONINFO,
					Context.MODE_PRIVATE);
		}
		return sp.getString(PersonInfoConstans.RONGDAI_ACCOUNT, "");
	}

	/**
	 * 获取汇付账号
	 * 
	 * @param context
	 * @return
	 */
	public static String getUsrCustId(Context context) {
		if (sp == null) {
			sp = context.getSharedPreferences(PersonInfoConstans.PERSONINFO,
					Context.MODE_PRIVATE);
		}
		return sp.getString(PersonInfoConstans.USRCUSTID, "");
	}
}
