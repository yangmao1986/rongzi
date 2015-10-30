package com.rongdai.calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class FloatWindowService extends Service {
	
	/**
	 * 用于在线程中创建或移除悬浮窗。
	 */
	private Handler handler = new Handler();

	/**
	 * 定时器，定时进行检测当前应该创建还是移除悬浮窗。
	 */
	private Timer timer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		//System.out.println("服务开启了");
		new RefreshTask().run();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// 开启定时器，每隔0.5秒刷新一次
		if (timer == null) {
			timer = new Timer();
			timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		MyWindowManager.removeSmallWindow(getApplicationContext());
		MyWindowManager.removeBigWindow(getApplicationContext());
		// Service被终止的同时也停止定时器继续运行
		timer.cancel();
		timer = null;
		Log.e("onDestroy","onDestroy");
	}

	class RefreshTask extends TimerTask {

		@Override
		public void run() {
			/*// 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
			if (isHome() && MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MyWindowManager.removeSmallWindow(getApplicationContext());
						MyWindowManager.removeBigWindow(getApplicationContext());
					}
				});
			}*/
			// 当前界面是桌面，且有悬浮窗显示，则移除悬浮窗。
			if (isHome() && !MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MyWindowManager.createSmallWindow(getApplicationContext());
						
					}
				});
			}
			
			//当前应用在后台
			if (isHome()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MyWindowManager.removeSmallWindow(getApplicationContext());
						MyWindowManager.removeBigWindow(getApplicationContext());
					}
				});
			}
			// 当前应用在前台
			else if (!isHome()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MyWindowManager.createSmallWindow(getApplicationContext());
						
					}
				});
			}
			
			// 当前应用在前台，并且有浮标
			else if (!isHome() && MyWindowManager.isWindowShowing()) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						MyWindowManager.updateUsedPercent(getApplicationContext());
					}
				});
			}
		}

	}

	/**
	 * 判断当前界面是否是桌面
	 */
	private boolean isHome() {
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = mActivityManager.getRunningTasks(1);
		
		  if (!rti.isEmpty()) {
	            ComponentName topActivity = rti.get(0).topActivity;
	            if (!topActivity.getPackageName().equals(this.getPackageName())) {
	                return true;
	            }
	        }

		
		
//		return getHomes().contains(rti.get(0).topActivity.getPackageName());
		  return false;
	}

	/**
	 * 获得属于桌面的应用的应用包名称
	 * 
	 * @return 返回包含所有包名的字符串列表
	 */
	private List<String> getHomes() {
		List<String> names = new ArrayList<String>();
		PackageManager packageManager = this.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo ri : resolveInfo) {
			names.add(ri.activityInfo.packageName);
		}
		return names;
	}
}
