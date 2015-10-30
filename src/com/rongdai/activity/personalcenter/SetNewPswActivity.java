package com.rongdai.activity.personalcenter;

import android.content.Intent;
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
import com.rongdai.domain.SetNewPsw;
import com.rongdai.utils.Constants;

public class SetNewPswActivity extends BaseActivity implements OnClickListener {
	/**
	 * 返回
	 */
	private ImageView setnewpsw_back;
	/**
	 * 密码
	 */
	private EditText setnewpsw_psw;
	/**
	 * 确认密码
	 */
	private EditText setnewpsw_repsw;
	/**
	 * 确定
	 */
	private Button setnewpsw_sure;
	/**
	 * 用户名
	 */
	private EditText userName;
	/**
	 * 融贷账号
	 */
	private String rongdaiAccount;
	
	
	@Override
	public void initData() {
		super.initData();
		setContentView(R.layout.activity_setnewpsw);
		
		Intent intent = getIntent();
		rongdaiAccount = intent.getStringExtra("rongdaiAccount");
		
		//userName = (EditText) findViewById(R.id.setnewpsw_user_name);
		setnewpsw_back = (ImageView) findViewById(R.id.setnewpsw_back);
		setnewpsw_psw = (EditText) findViewById(R.id.setnewpsw_psw);
		setnewpsw_repsw = (EditText) findViewById(R.id.setnewpsw_repsw);
		setnewpsw_sure = (Button) findViewById(R.id.setnewpsw_sure);
		
		setnewpsw_back.setOnClickListener(this);
		setnewpsw_sure.setOnClickListener(this);
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.setnewpsw_back:
			this.finish();
			break;
		case R.id.setnewpsw_sure:
			setNewPswToNet();
//			Intent intent = new Intent(this,SetNewPswActivity.class);
//			startActivity(intent);
//			this.finish();
			break;
		}
	}
	/**
	 * 访问网络修改密码
	 */
	private void setNewPswToNet() {
		String strUserName = rongdaiAccount.trim();
		String strNewPsw = setnewpsw_psw.getText().toString().trim();
		String strReNewPsw = setnewpsw_repsw.getText().toString().trim();
		if(TextUtils.isEmpty(strUserName)){
			Toast.makeText(this, "请输入用户名", 0).show();
			return ; 
		}
		if(TextUtils.isEmpty(strNewPsw)){
			Toast.makeText(this, "请输入密码", 0).show();
			return ; 
		}
		if(TextUtils.isEmpty(strReNewPsw)){
			Toast.makeText(this, "请输入确认密码", 0).show();
			return ; 
		}
		if(strReNewPsw.equals(strNewPsw)){		
		}else{
			Toast.makeText(this, "两次输入的密码不一致", 0).show();
			return ; 
		}
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("rongdaiAccount", strUserName);
		params.addBodyParameter("password", strNewPsw);
		httpUtils.send(HttpMethod.POST, Constants.ModifyLoginPwdInfo,params,new RequestCallBack<String>() {

			private SetNewPsw setNewPsw;

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Log.e("password", responseInfo.result);
				//System.out.println(responseInfo.result);
				Gson gson = new Gson();
				try {
					setNewPsw = gson.fromJson(responseInfo.result,SetNewPsw.class);
					Toast.makeText(BaseApplication.getContext(),setNewPsw.message, Toast.LENGTH_LONG).show();
					SetNewPswActivity.this.finish();
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
