package com.rongdai.activity.personalcenter;

import com.rongdai.R;
import com.rongdai.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class SecurityCenterActivity extends Activity implements OnClickListener {


	
	private ImageButton securitycenter_back;
	
	/**
	 * 
	 * 实名认证
	 **/	
	private RelativeLayout real_name_certification;
	
	/**
	 * 
	 * 绑定手机号
	 **/
	
	private RelativeLayout binding_phone_number_back_relativelayout;
	/**
	 * 
	 * 我的银行卡
	 **/
	private RelativeLayout mybankcard_relativelayout;
	
	/**
	 * 
	 * 修改登录密码
	 **/
	private RelativeLayout editpassword_relativelayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_security_center);
		
		
		securitycenter_back = (ImageButton)findViewById(R.id.securitycenter_back);
		securitycenter_back.setOnClickListener(this);
		
		real_name_certification = (RelativeLayout)findViewById(R.id.real_name_certification);
		real_name_certification.setOnClickListener(this);
		
		
		binding_phone_number_back_relativelayout = (RelativeLayout)findViewById(R.id.binding_phone_number_back_relativelayout);
		binding_phone_number_back_relativelayout.setOnClickListener(this);
		
		mybankcard_relativelayout = (RelativeLayout)findViewById(R.id.mybankcard_relativelayout);
		mybankcard_relativelayout.setOnClickListener(this);
		
		editpassword_relativelayout = (RelativeLayout)findViewById(R.id.editpassword_relativelayout);
		editpassword_relativelayout.setOnClickListener(this);
		
		
		
	}
	

	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {	
		case R.id.securitycenter_back:
			this.finish();
			break;
		case R.id.real_name_certification:
/*			intent = new Intent(this,RealNameCertificationActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(intent);*/
			break;
		case R.id.binding_phone_number_back_relativelayout:
/*			intent = new Intent(this,BindingPhoneNumberActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(intent);*/
			break;
		case R.id.mybankcard_relativelayout:
/*			intent = new Intent(this,MyBankCardActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(intent);*/
			break;
		case R.id.editpassword_relativelayout:
			intent = new Intent(this,EditPasswordActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	
	
}
