package com.rongdai.iwantloan.pager;

import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.R;
import com.rongdai.base.BaseApplication;
import com.rongdai.domain.WriteLoanInfoBean;
import com.rongdai.utils.Constants;

public class WriteLoanInfo extends Activity implements OnClickListener {
	/**
	 * 联系人
	 */
	private EditText nameTextView;
	/**
	 * 联系人电话
	 */
	private EditText phoneTextView;
	/**
	 * 联系人email
	 */
	private EditText emailTextView;
	/**
	 * 联系人户籍
	 */
	private EditText hujiTextView;
	/**
	 * 联系人现居地
	 */
	private EditText localTextView;
	/**
	 * 企业名称
	 */
	private EditText companyTextView;
	/**
	 * 意向融资
	 */
	private EditText moneyTextView;
	/**
	 * 融资期限
	 */
	private EditText timeTextView;
	/**
	 * 备注说明
	 */
	private EditText commentTextView;
	/**
	 * 提交
	 */
	private Button commitTextView;
	/**
	 * 返回键 
	 */
	private ImageView backImageButton;
	/**
	 * 我要贷款页面传递过来的贷款类型
	 */
	private String loanTypeId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.i_want_loan_write_info);
		Intent intent = getIntent();
		loanTypeId = intent.getStringExtra("loanTypeId");
		
		backImageButton = (ImageView) findViewById(R.id.ib_back);
		nameTextView = (EditText) findViewById(R.id.write_info_name);
		phoneTextView = (EditText) findViewById(R.id.write_info_phone);
		emailTextView = (EditText) findViewById(R.id.write_info_email);
		hujiTextView = (EditText) findViewById(R.id.write_info_huji);
		localTextView = (EditText) findViewById(R.id.write_info_local);
		companyTextView = (EditText) findViewById(R.id.write_info_company);
		moneyTextView = (EditText) findViewById(R.id.write_info_money);
		timeTextView = (EditText) findViewById(R.id.write_info_time);
		commentTextView = (EditText) findViewById(R.id.write_info_comment);
		commitTextView = (Button) findViewById(R.id.write_info_commit);
		backImageButton.setOnClickListener(this);
		commitTextView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			this.finish();
			break;
		case R.id.write_info_commit:
			if(checkIsEmpty()){
				Toast.makeText(this, "提交成功", 0).show();
				sendDataToNet();
			}else{
				return ;
			}
			break;
			
		default:
			break;
		}
	}

	/**
	 * 请服务器发送表单
	 */
	private void sendDataToNet() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		
		params.addBodyParameter("bmTypeId", loanTypeId);
		
		params.addBodyParameter("contactMan", nameTextView.getText().toString().trim());
		
		params.addBodyParameter("contactType", phoneTextView.getText().toString().trim());
		
		params.addBodyParameter("email", emailTextView.getText().toString().trim());
		
		params.addBodyParameter("birthAddress", hujiTextView.getText().toString().trim());
		
		params.addBodyParameter("liveAddress", localTextView.getText().toString().trim());
		
		params.addBodyParameter("companyName", companyTextView.getText().toString().trim());
		
		params.addBodyParameter("bmAccount", moneyTextView.getText().toString().trim());
		
		params.addBodyParameter("bmLimit", timeTextView.getText().toString().trim());
		
		params.addBodyParameter("bmRemark", commentTextView.getText().toString().trim());
		
		httpUtils.send(HttpMethod.POST, Constants.loanProject, params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				progcessData(responseInfo.result);
			}
		});
	}

	
	/**
	 * 解析网络数据
	 * @param json
	 */
	protected void progcessData(String json) {
		Gson gson = new Gson();
		try {
			WriteLoanInfoBean loanInfoBean = gson.fromJson(json, WriteLoanInfoBean.class);
			if("留言成功".equals(loanInfoBean.message)){
				Toast.makeText(this, "提交成功", 0).show();
				this.finish();
			}else{
				Toast.makeText(this, "提交失败", 0).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private boolean checkIsEmpty() {
		//匹配手机号
		String phoneNumber = phoneTextView.getText().toString().trim();
		String email = emailTextView.getText().toString().trim();
		Pattern p = Pattern
				.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
		boolean ismatch = p.matcher(phoneNumber).matches();
		
		// 匹配邮箱
		String check = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		Pattern regex = Pattern.compile(check);
		boolean isMatched = regex.matcher(email).matches();
		
		if(TextUtils.isEmpty(nameTextView.getText().toString().trim())){
			Toast.makeText(this, "请填写联系人", 0).show();
			return false;
		}
		if(TextUtils.isEmpty(phoneTextView.getText().toString().trim())){
			Toast.makeText(this, "请填写手机号", 0).show();
			return false;
		}else if(!ismatch){
			Toast.makeText(this, "请正确的手机号", 0).show();
			return false;
		}
		if(!TextUtils.isEmpty(emailTextView.getText().toString().trim())&&!isMatched){
			Toast.makeText(this, "请输入正确的邮箱或者不填写邮箱", 0).show();
			return false;
		}
		if(TextUtils.isEmpty(hujiTextView.getText().toString().trim())){
			Toast.makeText(this, "请填写户籍", 0).show();
			return false;
		}
		if(TextUtils.isEmpty(localTextView.getText().toString().trim())){
			Toast.makeText(this, "请填写您的现居地", 0).show();
			return false;
		}
		if(TextUtils.isEmpty(companyTextView.getText().toString().trim())){
			Toast.makeText(this, "请填写企业名称", 0).show();
			return false;
		}
		if(TextUtils.isEmpty(moneyTextView.getText().toString().trim())){
			Toast.makeText(this, "请填写意向融资金额", 0).show();
			return false;
		}
		if(TextUtils.isEmpty(timeTextView.getText().toString().trim())){
			Toast.makeText(this, "请填写预期融资期限", 0).show();
			return false;
		}
		if(TextUtils.isEmpty(commentTextView.getText().toString().trim())){
			Toast.makeText(this, "请填写备注说明", 0).show();
			return false;
		}
		else{
			return true;
		}
	}
}
