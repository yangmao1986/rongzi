package com.rongdai.activity.personalcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.webkit.WebView;

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
import com.rongdai.utils.SwitchPager;
import com.rongdai.utils.UserInfoUtils;

public class CertificateHuiFu extends BaseActivity {
	private WebView certifycation;

	@Override
	public void initData() {
		super.initData();
		setContentView(R.layout.activity_certificatehuifu);
		
		certifycation = (WebView) findViewById(R.id.wv_certifycation);
		certifycation.getSettings().setJavaScriptEnabled(true);
		goopen();
	}
	
	
	/**
	 * 开通第三方资金托管账户
	 */
	protected void goopen() {
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		params.addBodyParameter("usrId", UserInfoUtils.getRongdaiAccount(this));
		httpUtils.send(HttpMethod.POST, Constants.registerHuiFu, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						new SwitchPager(CertificateHuiFu.this){
							@Override
							public void update() {
								goopen();
							}
						}.loadErrorPager();	
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						//System.out.println(info.result);
						Gson gson = new Gson();
						RegistOpen registOpen = gson.fromJson(info.result, RegistOpen.class);
						certifycation.loadUrl(Constants.tendrweb+"?"+registOpen.data);
						//System.out.println(Constants.tendrweb+"?"+registOpen.data);
						
						new SwitchPager(CertificateHuiFu.this){
							@Override
							public void update() {
							}
						}.loadSuccessPager();
					}
					
				}); 

	}
	
	@Override
	protected void onStop() {
		super.onRestart();
		loginByNet();
	}
	
	/**
	 * 访问网络 登陆账号
	 */
	private void loginByNet() {
		final String userName = UserInfoUtils.getUserName(this);
		final String password = UserInfoUtils.getPassword(this);
		//System.out.println(userName+"---"+password);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("name", userName);
		params.addBodyParameter("password", password);
		httpUtils.send(HttpMethod.POST, Constants.rongDaiLogin, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}
					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						//System.out.println(json);
						Gson gson = new Gson();
						LoginInfo loginInfo = gson.fromJson(json, LoginInfo.class);
						//登陆成功后 保存用户信息
						SharedPreferences sp = getSharedPreferences(PersonInfoConstans.PERSONINFO,Context.MODE_PRIVATE);
						Editor editor = sp.edit();
						editor.putString(PersonInfoConstans.USER_NAME, userName);
						editor.putString(PersonInfoConstans.PASSWORD, password);
						editor.putString(PersonInfoConstans.PERSONINFO, loginInfo.rongdaiAccount);
						editor.putString(PersonInfoConstans.USRCUSTID, loginInfo.UsrCustId);
						//System.out.println(loginInfo.rongdaiAccount);
						//System.out.println(loginInfo.UsrCustId);
						editor.commit();
						
						Intent intent =new Intent(CertificateHuiFu.this,MainActivity.class);
						intent.putExtra("from_certificate", "from_certificate");
						CertificateHuiFu.this.startActivity(intent);
						CertificateHuiFu.this.finish();
					}
				});

	}
	
}
