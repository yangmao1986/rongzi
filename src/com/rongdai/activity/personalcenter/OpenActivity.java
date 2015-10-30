package com.rongdai.activity.personalcenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.MainActivity;
import com.rongdai.R;
import com.rongdai.base.BaseActivity;
import com.rongdai.domain.LoginInfo;
import com.rongdai.domain.RegistOpen;
import com.rongdai.utils.Constants;
import com.rongdai.utils.PersonInfoConstans;
import com.rongdai.utils.UserInfoUtils;

public class OpenActivity extends BaseActivity{
	private Button openlater;
	private Button goopen;
	private Dialog dialog;
	@Override
	public void initData() {
		super.initData();
		setContentView(R.layout.activity_open);
		
		showDialog();
	}
	
	/**
	 * 注册成功的对话框
	 */
	private void showDialog() {
		View view = View.inflate(this, R.layout.dialog_regist, null);
		openlater = (Button) view.findViewById(R.id.openlater);
		goopen = (Button) view.findViewById(R.id.goopen);
		dialog = new Dialog(this, R.style.mydialogstyle);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		/*
		 * 将对话框的大小按屏幕大小的百分比设置
		 */
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);

		WindowManager m = getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
		p.height = (int) (d.getHeight() * 0.3); // 高度设置为屏幕的0.6
		p.width = (int) (d.getWidth() * 0.85); // 宽度设置为屏幕的0.65
		dialogWindow.setAttributes(p);
		openlater.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.cancel();
				OpenActivity.this.finish();
				Intent intent =new Intent(OpenActivity.this,MainActivity.class);
				intent.putExtra("from_certificate", "from_certificate");
				OpenActivity.this.startActivity(intent);
				OpenActivity.this.finish();
			}
		});
		goopen.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.cancel();
				Intent intent = new Intent(OpenActivity.this,CertificateHuiFu.class);
				startActivity(intent);
				OpenActivity.this.finish();
			}
		});
		dialog.show();
	}
	
}
