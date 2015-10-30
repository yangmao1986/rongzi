package com.rongdai.activity.personalcenter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.R;
import com.rongdai.R.layout;
import com.rongdai.domain.LoginInfo;
import com.rongdai.pager.PersonCenter;
import com.rongdai.utils.Constants;
import com.rongdai.utils.PersonInfoConstans;
import com.rongdai.utils.ToastUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class FeedBackActivity extends Activity implements OnClickListener  {

	/**
	 * 去登陆
	 */
	private Button post_Feedback_button;
	/**
	 * 设置
	 */
	private ImageButton feedback_back;
	/**
	 * 评论内容
	 */
	private EditText contents_edittext;
	/**
	 * 联系方式
	 */
	private EditText contactType_edittext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		
		feedback_back = (ImageButton)findViewById(R.id.feedback_back);
		feedback_back.setOnClickListener(this);
		
		post_Feedback_button = (Button) findViewById(R.id.post_Feedback_button);
		post_Feedback_button.setOnClickListener(this);
		
		contents_edittext = (EditText) findViewById(R.id.contents_edittext);
		contactType_edittext = (EditText) findViewById(R.id.contactType_edittext);
		
	}
	
	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {
		case R.id.feedback_back:
			this.finish();
			break;
		case R.id.post_Feedback_button:
			feedbackpost();
			break;

		default:
			break;
		}
	}
	
	
	/**
	 * 访问网络 登陆账号
	 */
	private void feedbackpost() {
		 SharedPreferences sp = getSharedPreferences(
					PersonInfoConstans.PERSONINFO, Context.MODE_PRIVATE);
		 
		final String contents = contents_edittext.getText().toString().trim();
		final String contactType = contactType_edittext.getText().toString().trim();
		//System.out.println(userName+"---"+password);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("rongDaiAccount",sp.getString(PersonInfoConstans.RONGDAI_ACCOUNT, ""));
		params.addBodyParameter("contents", contents);
		params.addBodyParameter("contactType", contactType);
		httpUtils.send(HttpMethod.POST, Constants.suggestionFeekback, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ToastUtils.show(FeedBackActivity.this, "提交失败");
					}
					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						System.out.println(json);
						Log.e("feedback", info.result);
						LoginInfo loginInfo = null;
						try {
							Gson gson = new Gson();
							loginInfo = gson.fromJson(json, LoginInfo.class);
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						}
						//登陆成功后 保存用户信息
						if("意见反馈提交成功".equals(loginInfo.message)){
							ToastUtils.show(FeedBackActivity.this, "意见反馈提交成功");
							FeedBackActivity.this.finish();
						}else{
							ToastUtils.show(FeedBackActivity.this, "提交失败");
						}
					}
				});

	}
	
}
