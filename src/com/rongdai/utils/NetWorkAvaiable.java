package com.rongdai.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkAvaiable {
	public static boolean isNetWorkAvaiable(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
		if(cm == null){
			return false;
		}else{
			NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
			if (networkInfos != null && networkInfos.length > 0)
            {
                for (int i = 0; i < networkInfos.length; i++)
                {
                    //System.out.println(i + "===状态===" + networkInfos[i].getState());
                    //System.out.println(i + "===类型===" + networkInfos[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
		}
		return  false;
	}
}
