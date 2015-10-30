package com.rongdai.managemoney.pager;

import java.lang.reflect.Type;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.R;
import com.rongdai.activity.personalcenter.PayActivity;
import com.rongdai.base.BaseApplication;
import com.rongdai.constants.IntentConstants;
import com.rongdai.domain.BindBanckCard;
import com.rongdai.domain.ConfirmTenderBean;
import com.rongdai.domain.investAndEarn;
import com.rongdai.utils.Constants;
import com.rongdai.utils.DoubleUtil;
import com.rongdai.utils.NetWorkAvaiable;
import com.rongdai.utils.PersonInfoConstans;
import com.rongdai.utils.UserInfoUtils;

public class ConfirmTender extends Activity implements OnClickListener {
	/**
	 * 返回键
	 */
	private ImageView backImageButton;
	/**
	 * 标题显示
	 */
	private TextView titleTextView;
	/**
	 * 可用余额
	 */
	private TextView canUseMoney;
	/**
	 * 充值
	 */
	private Button recharge;
	/**
	 * 投标金额
	 */
	private EditText tenderMoney;
	/**
	 * 确认投标
	 */
	private Button confirmTender;
	/**
	 * 获取意图传递来的融资项目的id
	 */
	private String bmId;
	private String borrowType;
	private ConfirmTenderBean confirmTenderBean;
	/**
	 * 配置文件
	 */
	private SharedPreferences sp;
	private TextView small_money;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_tender);
		Intent intent = getIntent();
		bmId = intent.getStringExtra(IntentConstants.BMID);
		borrowType = intent.getStringExtra(IntentConstants.BORROWTYPE);
		//System.out.println("类型"+borrowType);
		backImageButton = (ImageView) findViewById(R.id.ib_back);
		small_money = (TextView) findViewById(R.id.small_money);
		titleTextView = (TextView) findViewById(R.id.tv_title);
		canUseMoney = (TextView) findViewById(R.id.tender_can_use_money);
		recharge = (Button) findViewById(R.id.tender_but_recharge);
		tenderMoney = (EditText) findViewById(R.id.tender_money);
		confirmTender = (Button) findViewById(R.id.tender_confirm);
		backImageButton.setOnClickListener(this);
		recharge.setOnClickListener(this);
		confirmTender.setOnClickListener(this);
		sp = getSharedPreferences(PersonInfoConstans.PERSONINFO,
				Context.MODE_PRIVATE);
		getCanUseMoneyFromNet();
		titleTextView.setText("投标");
		if("融影宝".equals(borrowType)){
			small_money.setText("“融影宝”类型最低投资金额为1000元,并且是1000的倍数");
			small_money.setGravity(Gravity.CENTER);
		}
	}

	/**
	 * 从网络获取可用余额
	 */
	private void getCanUseMoneyFromNet() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("rongDaiAccount",
				sp.getString(PersonInfoConstans.RONGDAI_ACCOUNT, ""));
		params.addBodyParameter("usrcustId",
				sp.getString(PersonInfoConstans.USRCUSTID, ""));
		httpUtils.send(HttpMethod.POST, Constants.investAndEarn, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						canUseMoney.setText("0");
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						//System.out.println("余额" + responseInfo.result);
						Gson gson = new Gson();
						Type type = new TypeToken<investAndEarn>() {
						}.getType();
						investAndEarn investAndEarn = gson.fromJson(
								responseInfo.result, type);
						canUseMoney.setText("￥" + investAndEarn.data.canUseBal);
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			this.finish();
			break;
		case R.id.tender_but_recharge:
			Intent intent = new Intent(this, PayActivity.class);
			startActivity(intent);
			break;
		case R.id.tender_confirm:
			confirmDataToNet();
			break;

		default:
			break;
		}
	}

	/**
	 * 确认投标，提交数据到网络
	 */
	private void confirmDataToNet() {
		
		/** 投资的钱 */
		String tender = tenderMoney.getText().toString().trim();
		
		/** 现在拥有的钱 */
		String can = canUseMoney.getText().toString().trim();
		if(can.contains("￥")){
			can = can.replace("￥", "");
		}
		if(can.contains(",")){
			can = can.replace(",", "");
		}
		if("".equals(can)){
			Toast.makeText(BaseApplication.getContext(), "请输入投标金额", 0).show();
			return ;
		}
		if (Double.parseDouble(tender) > Double.parseDouble(can)) {
			Toast.makeText(BaseApplication.getContext(), "对不起您的金额不够，请充值后在投资吧！",
					0).show();
			return;
		}
		Pattern p = Pattern
				.compile("^[0-9]*[1-9][0-9]*(.?[0-9]{2})$");
		boolean ismatch = p.matcher(tender).matches();
		if(!ismatch){
			Toast.makeText(BaseApplication.getContext(), "请输入正确格式金额",
					0).show();
			return ;
		}
		if ("融影宝".equals(borrowType)&&Double.parseDouble(tender)<1000&&Double.parseDouble(tender)%1000 != 0) {
			Toast.makeText(BaseApplication.getContext(),"融影宝系列投资金额为1000的整数倍",0).show();
			return ;
		}
		if (Double.parseDouble(tender)<100) {
			Toast.makeText(BaseApplication.getContext(),"投资的金额不能小于100",0).show();
			return ;
		}
		//System.out.println(Double.valueOf(tender));
		String decimal = DoubleUtil.getDecimal(Double.valueOf(tender));
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("bmId", bmId);
		//System.out.println(decimal);
		params.addBodyParameter("money", decimal);
		params.addBodyParameter("usrcustId", UserInfoUtils.getUsrCustId(this));
		params.addBodyParameter("rongdaiAccount",
				UserInfoUtils.getRongdaiAccount(this));
		// params.addBodyParameter("rongdaiAccount",
		// UserInfoUtils.getRongdaiAccount(this));

		httpUtils.send(HttpMethod.POST, Constants.Investment, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(ConfirmTender.this, "网络异常", 0).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						processData(responseInfo.result);
					}
				});
	}

	/**
	 * 解析数据
	 * 
	 * @param json
	 *            json字符串
	 */
	protected void processData(String json) {
		//System.out.println(json);
		Gson gson = new Gson();
		boolean workAvaiable = NetWorkAvaiable.isNetWorkAvaiable(this);
		if(!workAvaiable){
			Toast.makeText(this, "网络没有开启", 0).show();
			return ;
		}
		try {
			confirmTenderBean = gson.fromJson(json, ConfirmTenderBean.class);
//			if ("".equals(confirmTenderBean.bankAccount)) {
//				getBanckCardFromNet();
//			}else{
				Intent intent = new Intent(this, TendWebPager.class);
				intent.putExtra("data", confirmTenderBean.data);
				startActivity(intent);
//			}
		} catch (Exception e) {
			Toast.makeText(BaseApplication.getContext(), "网络异常", 0).show();
		}
	}

	/**
	 * 获取银行卡签名 
	 */
	protected void getBanckCardFromNet() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("usrcustId", UserInfoUtils.getUsrCustId(this));
		httpUtils.send(HttpMethod.POST, Constants.bindingMyBank, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//System.out.println("绑定银行卡"+responseInfo.result);
				BindBanckCard bindBanckCard = null;
				try {
					Gson gson = new Gson();
					bindBanckCard = gson.fromJson(responseInfo.result, BindBanckCard.class);
				} catch (JsonSyntaxException e) {
					e.printStackTrace();
				}
				Intent intent = new Intent(ConfirmTender.this, TendWebPager.class);
				intent.putExtra("data", bindBanckCard.data);
				startActivity(intent);
			}
		});
	}
}
