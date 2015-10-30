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
import com.rongdai.R.id;
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

public class PayActivity extends Activity implements OnClickListener  {
	private ImageButton pay_back;
	private String rongDaiAccount,usrcustId;
	private TextView queryBalance_textview;
	Button goto_pay_button;
	/**
	 * 充值的金额
	 */
	private EditText recharge_money;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		
		SharedPreferences sp = getSharedPreferences(
				PersonInfoConstans.PERSONINFO, Context.MODE_PRIVATE);
		rongDaiAccount=sp.getString(PersonInfoConstans.RONGDAI_ACCOUNT, "");
		usrcustId=sp.getString(PersonInfoConstans.USRCUSTID, "");
		pay_back = (ImageButton)findViewById(R.id.pay_back);
		pay_back.setOnClickListener(this);
		
		goto_pay_button = (Button)findViewById(R.id.goto_pay_button);
		goto_pay_button.setOnClickListener(this);
		
		recharge_money = (EditText) findViewById(R.id.recharge_money);
		queryBalance_textview = (TextView)findViewById(R.id.queryBalance_textview);
		
		queryBalanceDo();
		
	}
	
	

	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {	
		case R.id.pay_back:
			this.finish();
			break;	
		case R.id.goto_pay_button:
			//正则判断输入保留2位小数
			String str = recharge_money.getText().toString().trim() ;
	/*		Pattern p = Pattern.compile("^[1-9]*[0-9][0-9]*(.[0-9]{2})$");*/
			
		
	        Log.e("isValid", "isValid");
			Pattern a = Pattern.compile("^\\+?(\\d*\\.\\d{2})$");
			Matcher s = a.matcher(str);
			boolean isValidd = s.matches();
			
			if("".equals(recharge_money.getText().toString().trim())){
				Toast.makeText(PayActivity.this, "请输入充值金额", Toast.LENGTH_LONG).show();
				return ;
			}
			if(!isValidd){
				
				Toast.makeText(PayActivity.this, "输入格式不正确", Toast.LENGTH_LONG).show();
				return ;
				
			}
			boolean workAvaiable = NetWorkAvaiable.isNetWorkAvaiable(this);
			if(!workAvaiable){
				Toast.makeText(this, "网络没有开启", 0).show();
				return ;
			}
			intent = new Intent(this, PayWebViewActivity.class);
			intent.putExtra("rechargeMoney", recharge_money.getText().toString().trim());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			
			
			break;	
		default:
			break;
		}
	}

	private void queryBalanceDo() {
		
		
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
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
