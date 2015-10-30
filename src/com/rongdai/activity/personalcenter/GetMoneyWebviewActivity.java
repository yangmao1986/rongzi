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
import com.rongdai.domain.BindBanckCard;
import com.rongdai.domain.rechargeInfo;
import com.rongdai.utils.Constants;
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
import android.webkit.WebView;
import android.widget.ImageButton;

public class GetMoneyWebviewActivity extends Activity implements
		OnClickListener {
	private ImageButton pay_back;
	private String money, usrcustId, Webviewurl;
	private WebView pay_webview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_money_webview);

		SharedPreferences sp = getSharedPreferences(
				PersonInfoConstans.PERSONINFO, Context.MODE_PRIVATE);
		usrcustId = sp.getString(PersonInfoConstans.USRCUSTID, "");
		Intent intent = getIntent();
		money = intent.getStringExtra("extractMoney");
		pay_back = (ImageButton) findViewById(R.id.pay_back);
		pay_back.setOnClickListener(this);
		queryBalanceDo();

	}

	private void WebViewDo() {

		pay_webview = (WebView) findViewById(R.id.pay_webview);
		pay_webview.getSettings().setJavaScriptEnabled(true);
		pay_webview.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		pay_webview.requestFocus();
		pay_webview.loadUrl(Webviewurl);

	}

	@Override
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
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
		params.addBodyParameter("money", money);
		params.addBodyParameter("usrcustId", usrcustId);
		httpUtils.send(HttpMethod.POST, Constants.withdraw, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						//System.out.println(json);

						rechargeInfo investAndEarn = null;

						try {
							Gson gson = new Gson();
							investAndEarn = gson.fromJson(json,
									rechargeInfo.class);
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						}
						if ("".equals(investAndEarn.bankAccount)) {
							getBanckCardFromNet();
						} else {
							Webviewurl = Constants.tendrweb + "?"
									+ investAndEarn.data;
							WebViewDo();
						}
						Log.e("余额", investAndEarn.data);
						// queryBalance_textview.setText("￥"+investAndEarn.data.canUseBal);

					}
				});

	}

	/**
	 * 获取银行卡签名
	 */
	protected void getBanckCardFromNet() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("usrcustId", usrcustId);
		httpUtils.send(HttpMethod.POST, Constants.bindingMyBank, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						//System.out.println("绑定银行卡" + responseInfo.result);
						BindBanckCard bindBanckCard = null;
						try {
							Gson gson = new Gson();
							bindBanckCard = gson.fromJson(responseInfo.result,
									BindBanckCard.class);
						} catch (JsonSyntaxException e) {
							e.printStackTrace();
						}
						Webviewurl = Constants.tendrweb + "?"
								+ bindBanckCard.data;
						WebViewDo();
					}
				});
	}
}
