package com.rongdai.activity.personalcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
import com.rongdai.domain.LoginInfo;
import com.rongdai.utils.Constants;
import com.rongdai.utils.PersonInfoConstans;
import com.rongdai.utils.ToastUtils;
import com.rongdai.utils.UserInfoUtils;

/**
 * 登陆界面
 * @author Administrator
 *
 */
public class LoginActivity extends BaseActivity implements OnClickListener {
	/**
	 * 回退按钮
	 */
	private ImageView login_back;
	/**
	 * 密码
	 */
	private EditText login_password;
	/**
	 * 用户名
	 */
	private EditText login_username;
	/**
	 * 登陆
	 */
	private Button login;
	/**
	 * 注册
	 */
	private Button regist;
	/**
	 * 忘记密码
	 */
	private Button forget_password;
	/**
	 * 密码可见
	 */
	private CheckBox psw_see;

	@Override
	public void initData() {
		super.initData();
		setContentView(R.layout.activity_login);
		login_back = (ImageView) findViewById(R.id.login_back);
		login_password = (EditText) findViewById(R.id.login_password);
		login_username = (EditText) findViewById(R.id.login_username);
		login = (Button) findViewById(R.id.login);
		regist = (Button) findViewById(R.id.regist);
		forget_password = (Button) findViewById(R.id.forget_password);
		psw_see = (CheckBox) findViewById(R.id.psw_see);

		login_back.setOnClickListener(this);
		login.setOnClickListener(this);
		regist.setOnClickListener(this);
		forget_password.setOnClickListener(this);

		psw_see.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (isChecked) {
					login_password
							.setInputType(InputType.TYPE_TEXT_VARIATION_NORMAL);
				} else {
					login_password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
	}

	@Override
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.login_back:
			this.finish();
			break;
		case R.id.login:
			saveUsername();
			setHandPsw();
			loginByNet();
			break;
		case R.id.regist:
			intent = new Intent(this, RegistActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		case R.id.forget_password:
			intent = new Intent(this, ForgetPswActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		}
	}

	/**
	 * 访问网络 登陆账号
	 */
	private void loginByNet() {
		final String userName = login_username.getText().toString().trim();
		final String password = login_password.getText().toString().trim();
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
						System.out.println(json);
						LoginInfo loginInfo = null;
						try {
							Gson gson = new Gson();
							loginInfo = gson.fromJson(json, LoginInfo.class);
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						}
						//登陆成功后 保存用户信息
						if("登录成功".equals(loginInfo.message)){
							SharedPreferences sp = getSharedPreferences(PersonInfoConstans.PERSONINFO,Context.MODE_PRIVATE);
							Editor editor = sp.edit();
							editor.putString(PersonInfoConstans.USER_NAME, userName);
							editor.putString(PersonInfoConstans.PASSWORD, password);
							editor.putString(PersonInfoConstans.RONGDAI_ACCOUNT, loginInfo.rongdaiAccount);
							editor.putString(PersonInfoConstans.USRCUSTID, loginInfo.UsrCustId);
							editor.putString(PersonInfoConstans.IMAGEURI, loginInfo.imgUrl);	
							long timeMillis = System.currentTimeMillis();
							editor.putLong(PersonInfoConstans.DATE, timeMillis);
							editor.commit();
							//登陆成功后 进入设置手势密码界面
							Intent intent = new Intent(LoginActivity.this, SetHandPasswordActivity.class);
							startActivity(intent);
							ToastUtils.show(LoginActivity.this, "登录成功");
							LoginActivity.this.finish();
						}else{
							ToastUtils.show(LoginActivity.this, "登录失败");
						}
					}
				});

	}
	
	/**
	 * 判断是不是从忘记手势密码界面过来的，如果是则清除之前的手势密码，并跳转到创建手势密码界面
	 * 如果不是  不做任何处理
	 */
	public void setHandPsw(){
		SharedPreferences sp = getSharedPreferences(PersonInfoConstans.PERSONINFO, Context.MODE_PRIVATE);
		if(!TextUtils.isEmpty(sp.getString(PersonInfoConstans.FORGET_HNADPASSWORD, ""))){
			Editor editor = sp.edit();
			//清空之前的手势密码
			editor.putString(PersonInfoConstans.HNADPASSWORD, "");
			editor.commit();
			Intent intent = new Intent(this,SetHandPasswordActivity.class);
			startActivity(intent);
		}
	}
	
	/**
	 * 登陆成功后保存登录状态(保存当前用户的用户名到sp文件中)
	 */
	public void saveUsername(){
		SharedPreferences sp = getSharedPreferences(PersonInfoConstans.PERSONINFO, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(PersonInfoConstans.HNADPASSWORD, "");
		editor.commit();
	}
}
