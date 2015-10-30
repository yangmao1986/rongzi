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
import com.rongdai.domain.ModifyLoginPwdInfo;
import com.rongdai.domain.MyAccountInfo;
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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyUserActivity extends Activity implements OnClickListener  {
	private ImageButton myuser_back;
	private RelativeLayout pay_relativelayout,getmoney_relativelayout;
	private TextView totalMoney_TexvView,canUseBal_TextView,frzBal_TextView,investAmount_TextView,incomeAmount_TextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_user);
	
		
		myuser_back = (ImageButton)findViewById(R.id.myuser_back);
		myuser_back.setOnClickListener(this);
		
		pay_relativelayout = (RelativeLayout)findViewById(R.id.pay_relativelayout);
		pay_relativelayout.setOnClickListener(this);
		
		getmoney_relativelayout = (RelativeLayout)findViewById(R.id.getmoney_relativelayout);
		getmoney_relativelayout.setOnClickListener(this);
		
		totalMoney_TexvView= (TextView)findViewById(R.id.totalMoney_TexvView);
		canUseBal_TextView= (TextView)findViewById(R.id.canUseBal_TextView);
		frzBal_TextView= (TextView)findViewById(R.id.frzBal_TextView);
		investAmount_TextView= (TextView)findViewById(R.id.investAmount_TextView);
		incomeAmount_TextView= (TextView)findViewById(R.id.incomeAmount_TextView);
		myAccount();
	}
	
	

	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {	
		case R.id.myuser_back:
			this.finish();
			break;	
		case R.id.pay_relativelayout:
			intent = new Intent(MyUserActivity.this,PayDetailedActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MyUserActivity.this.startActivity(intent);
			break;
		case R.id.getmoney_relativelayout:
			intent = new Intent(MyUserActivity.this,GetMoneyActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			MyUserActivity.this.startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	
private void myAccount() {
		
	final SharedPreferences sp = getSharedPreferences(
			PersonInfoConstans.PERSONINFO, Context.MODE_PRIVATE);
	String rongdai_account = sp.getString(PersonInfoConstans.RONGDAI_ACCOUNT, "");	
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();	
		params.addBodyParameter("rongdaiAccount", rongdai_account);		
		httpUtils.send(HttpMethod.POST, Constants.myAccount, params,
				new RequestCallBack<String>() {
					
					public void onFailure(HttpException arg0, String arg1) {
						 ToastUtils.show(MyUserActivity.this, "网络异常");
					}
				
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						Gson gson = new Gson();						
						try {
							MyAccountInfo myAccountInfo = gson.fromJson(json, MyAccountInfo.class);
							
							Log.e("message", myAccountInfo.message);
							double a=Double.valueOf(myAccountInfo.totalMoney).doubleValue();
							double b=Double.valueOf(myAccountInfo.canUseBal.replaceAll(",","")).doubleValue();
							double c=Double.valueOf(myAccountInfo.frzBal).doubleValue();
							double d=Double.valueOf(myAccountInfo.investAmount).doubleValue();
							double e=Double.valueOf(myAccountInfo.incomeAmount).doubleValue();
							
							totalMoney_TexvView.setText("￥"+String.format("%.2f", a));
							canUseBal_TextView.setText("￥"+String.format("%.2f", b));
							frzBal_TextView.setText("￥"+String.format("%.2f", c));
							investAmount_TextView.setText("￥"+String.format("%.2f", d));
							
							incomeAmount_TextView.setText("￥"+String.format("%.2f", e));
							/*queryBalance_textview.setText("￥"+investAndEarn.data.canUseBal);*/
						} catch (Exception e) {
							ToastUtils.show(MyUserActivity.this, "网络异常");
							e.printStackTrace();
						}
						
						
					}
				});
		
	}
	
}
