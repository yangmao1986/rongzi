package com.rongdai.base;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import cn.jpush.android.api.JPushInterface;

import com.rongdai.R;
import com.rongdai.calculator.FloatWindowService;
import com.rongdai.constants.ConfigConstants;
import android.app.Application;
import android.content.Context;
import android.util.Log;

public class BaseApplication extends Application {
	private static Context mContext;
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
//		initChat();
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);
	}

	public static Context getContext() {
		return mContext;
	}
	
	private void initChat() {
		// 初始化。
		RongIM.init(this, "8luwapkvu0p7l", R.drawable.ic_launche);
		// 从您的应用服务器请求，以获取 Token。在本示例中我们直接在下面 hardcode 给 token 赋值。
		// String token = getTokenFromAppServer();

		// 此处直接 hardcode 给 token 赋值，请替换为您自己的 Token。
		String token = "PA8JdWLSUw9oCMSFv1ZiU44MsbPvOtKulSQ7ATbBWkjqz1ViICSWFoBOxmpqv2HnyeL05fHeIzWJ/PZWb4hJtg==";

		// 连接融云服务器。
		try {
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				@Override
				public void onSuccess(String s) {
					// 此处处理连接成功。
					Log.d("Connect:", "Login successfully.");
				}

				@Override
				public void onError(ErrorCode errorCode) {
					// 此处处理连接错误。
					Log.d("Connect:", "Login failed.");
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
