package com.rongdai.activity.personalcenter;

import java.util.regex.Pattern;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.rongdai.domain.MessageData;
import com.rongdai.utils.Constants;
import com.rongdai.utils.ToastUtils;

public class ForgetPswActivity extends BaseActivity implements OnClickListener {
	private ImageView forgetpsw_back;
	private EditText number;
	private Button forgetpsw_sure;
	private MessageData messageData;

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		setContentView(R.layout.activity_forgetpsw);

		forgetpsw_back = (ImageView) findViewById(R.id.forgetpsw_back);
		number = (EditText) findViewById(R.id.number);
		forgetpsw_sure = (Button) findViewById(R.id.forgetpsw_sure);

		forgetpsw_back.setOnClickListener(this);
		forgetpsw_sure.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.forgetpsw_back:
			this.finish();
			break;
		case R.id.forgetpsw_sure:
			Pattern p = Pattern
			.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(177)|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
		boolean ismatch = p.matcher(number.getText().toString().trim()).matches();
		if(!ismatch){
			ToastUtils.show(this, "电话号码不正确");
			return ;
		}
			getVerificationFromNeet();
			break;
		}
	}

	/**
	 * 获取验证码
	 */
	private void getVerificationFromNeet() {
		String mobole = number.getText().toString().trim();
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("mobile", mobole);
		httpUtils.send(HttpMethod.POST, Constants.forgetPassword, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						//System.out.println(info.result);
						processMessageData(info.result);
						
						Log.e("code", info.result);
					}
				});
	}

	/**
	 * 解析
	 */
	protected void processMessageData(String json) {
		Gson gson = new Gson();
		try {
			
			messageData = gson.fromJson(json, MessageData.class);
			if("".equals(messageData.message)){
				ToastUtils.show(this, messageData.errorCode);		
			}else{
				Intent intent = new Intent(this, VerificationActivity.class);
				intent.putExtra("phoneNumber",number.getText().toString().trim());
				intent.putExtra("MessageCode",messageData.message );
				startActivity(intent);
				this.finish();
			}
			
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
