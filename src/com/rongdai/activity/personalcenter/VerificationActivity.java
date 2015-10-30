package com.rongdai.activity.personalcenter;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.R;
import com.rongdai.base.BaseActivity;
import com.rongdai.base.BaseApplication;
import com.rongdai.domain.MessageData;
import com.rongdai.domain.RegistInfo;
import com.rongdai.utils.Constants;
import com.rongdai.utils.PersonInfoConstans;
import com.rongdai.utils.ToastUtils;
import com.rongdai.utils.XUtilsConstans;

public class VerificationActivity extends BaseActivity implements OnClickListener {
	
	private ImageView verification_back;
	private EditText verification;
	private Button verificartion_sure;
	private String rongdaiAccount;
	private String phoneNumber;
	/**
	 * 获取验证码
	 */
	private Button regist_getverification;
	private TimerTask task;
	private int time = 60;
	private Timer timer = new Timer();
	private MessageData messageData;
	private String messageCode;
	
	@Override
	public void initData() {
		super.initData();
		setContentView(R.layout.activity_verification);
		Intent intent = getIntent();
		phoneNumber = intent.getStringExtra("phoneNumber");
		rongdaiAccount = intent.getStringExtra("MessageCode");
		verification_back = (ImageView) findViewById(R.id.verification_back);
		verification = (EditText) findViewById(R.id.verification);
		verificartion_sure = (Button) findViewById(R.id.verificartion_sure);
		regist_getverification = (Button) findViewById(R.id.regist_getverification);
		verification_back.setOnClickListener(this);
		verificartion_sure.setOnClickListener(this);
		regist_getverification.setOnClickListener(this);
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.verification_back:
			this.finish();
			break;
		case R.id.verificartion_sure:
			if (TextUtils.isEmpty(verification.getText().toString().trim())) {
				Toast.makeText(BaseApplication.getContext(), "请输入验证码", 0).show();
				return ;
			}
			if(!verification.getText().toString().trim().equals(messageCode)){
				Toast.makeText(BaseApplication.getContext(), "验证码不正确", 0).show();
				return ;
			}
			Intent intent = new Intent(this,SetNewPswActivity.class);
			intent.putExtra("rongdaiAccount",rongdaiAccount );			
			startActivity(intent);
			this.finish();
			break;
			
		case R.id.regist_getverification:
			boolean b = getVerificationFromNet();
			if(!b){
				return ;
			}
			regist_getverification.setEnabled(false);
			regist_getverification
					.setBackgroundResource(R.drawable.getverification_wait);
			regist_getverification.setText("");
			task = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (time <= 0) {
								regist_getverification.setEnabled(true);
								regist_getverification
										.setBackgroundResource(R.drawable.get_verification_bg);
								regist_getverification.setText("获取验证码");
								regist_getverification.setTextSize(14);
								regist_getverification
										.setTextColor(Color.WHITE);
								task.cancel();
							} else {
								regist_getverification.setText(time + "秒");
								regist_getverification.setTextSize(20);
							}
							time--;
						}
					});
				}
			};
			time = 60;
			timer.schedule(task, 0, 1000);
			break;
			
			
		}
	}
	
	

	/**
	 * 获取验证码
	 */
	private boolean getVerificationFromNet() {
		//电话号码的合法性
		Pattern p = Pattern
				.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(177)|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
		boolean ismatch = p.matcher(phoneNumber.trim()).matches();
		if(!ismatch){
			ToastUtils.show(VerificationActivity.this, "手机号码不正确");
			return false;
		}
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("mobile", phoneNumber.trim());
		httpUtils.send(HttpMethod.POST, Constants.getMobileCode, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Log.e("MobileCode", responseInfo.result);
						//System.out.println("获取到的手机验证码"+responseInfo.result);
						processMessageData(responseInfo.result);
					}
				});
		return true;

	}

	

	/**
	 * 获取短信验证码数据
	 */
	protected void processMessageData(String json) {
		Gson gson = new Gson();
		try {
			messageData = gson.fromJson(json, MessageData.class);
			messageCode=messageData.code;
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
	}

	
	
}
