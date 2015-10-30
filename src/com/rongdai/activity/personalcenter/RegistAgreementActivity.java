package com.rongdai.activity.personalcenter;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.rongdai.R;
import com.rongdai.base.BaseActivity;

public class RegistAgreementActivity extends BaseActivity {
	@Override
	public void initData() {
		super.initData();
		setContentView(R.layout.activity_registagreement);
		
		TextView tv_registagreement = (TextView)findViewById(R.id.tv_registagreement);
		ImageView registagreement_back = (ImageView) findViewById(R.id.registagreement_back);
		registagreement_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				RegistAgreementActivity.this.finish();
			}
		});
	}
}
