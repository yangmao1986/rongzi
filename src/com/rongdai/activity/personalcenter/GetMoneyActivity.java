package com.rongdai.activity.personalcenter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.R;
import com.rongdai.R.layout;
import com.rongdai.domain.investAndEarn;
import com.rongdai.utils.Constants;
import com.rongdai.utils.NetWorkAvaiable;
import com.rongdai.utils.PersonInfoConstans;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class GetMoneyActivity extends Activity implements OnClickListener  {

	private TextView getmoneyinfo_text;
	private ImageButton pay_back;
	private Button goto_getmoney_webview;
	private String rongDaiAccount,usrcustId;
	private TextView queryBalance_textview;
	/**
	 * 提现的金额
	 */
	private EditText extract_money;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_money);
		SharedPreferences sp = getSharedPreferences(
				PersonInfoConstans.PERSONINFO, Context.MODE_PRIVATE);
		rongDaiAccount=sp.getString(PersonInfoConstans.RONGDAI_ACCOUNT, "");
		usrcustId=sp.getString(PersonInfoConstans.USRCUSTID, "");
		
		getmoneyinfo_text = (TextView)findViewById(R.id.getmoneyinfo_text);
		getmoneyinfo_text.setOnClickListener(this);
		
		extract_money = (EditText) findViewById(R.id.extract_money);
		queryBalance_textview = (TextView)findViewById(R.id.queryBalance_textview);
		
		
		pay_back = (ImageButton)findViewById(R.id.pay_back);
		pay_back.setOnClickListener(this);
		
		goto_getmoney_webview = (Button)findViewById(R.id.goto_getmoney_webview);
		goto_getmoney_webview.setOnClickListener(this);
		
		
		queryBalanceDo();
	}
	
	

	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {	
		case R.id.goto_getmoney_webview:
			//正则判断输入保留2位小数
			String str = extract_money.getText().toString().trim() ;
			Pattern p = Pattern.compile("^\\+?(\\d*\\.\\d{2})$");
			Matcher m = p.matcher(str);
			boolean isValid = m.matches();
			if("".equals(extract_money.getText().toString().trim())){
				Toast.makeText(GetMoneyActivity.this, "请输入提现金额", Toast.LENGTH_LONG).show();
				return ;
			}
			if(!isValid){
				Toast.makeText(GetMoneyActivity.this, "输入格式不正确", Toast.LENGTH_LONG).show();
				return ;
			}
			
			String can= queryBalance_textview.getText().toString().trim() ;
			if(can.contains("￥")){
				can = can.replace("￥", "");
			}
			if(can.contains(",")){
				can = can.replace(",", "");
			}
			if(Double.parseDouble(str) > Double.parseDouble(can)){
				Toast.makeText(GetMoneyActivity.this, "对不起您的余额不足，", Toast.LENGTH_LONG).show();
				return ;
			}
			
			
			boolean workAvaiable = NetWorkAvaiable.isNetWorkAvaiable(this);
			if(!workAvaiable){
				Toast.makeText(this, "网络没有开启", 0).show();
				return ;
			}
			intent = new Intent(this,GetMoneyWebviewActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("extractMoney", extract_money.getText().toString().trim());
			this.startActivity(intent);
			break;
		case R.id.getmoneyinfo_text:
			intent = new Intent(this,GetMoneyInfoActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(intent);
			break;
		case R.id.pay_back:
			this.finish();
			break;	
		default:
			break;
		}
	}

private void queryBalanceDo() {
		
		
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("rongDaiAccount", rongDaiAccount);	
		params.addBodyParameter("usrcustId", usrcustId);		
		httpUtils.send(HttpMethod.POST, Constants.investAndEarn, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}
					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						//System.out.println(json);
						/*Gson gson = new Gson();
						
						investAndEarn investAndEarn = gson.fromJson(json, investAndEarn.class);*/
						Gson gson = new Gson();
			            java.lang.reflect.Type type = new TypeToken<investAndEarn>() {}.getType();
			            investAndEarn investAndEarn = gson.fromJson(json, type);
						
						Log.e("余额", investAndEarn.message);
						queryBalance_textview.setText("￥"+investAndEarn.data.canUseBal);
						
					
						
					}
				});

	}
	
	
	
}
