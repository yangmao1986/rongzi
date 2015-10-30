package com.rongdai.activity.personalcenter;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.rongdai.R;
import com.rongdai.base.BaseActivity;

/**
 * 理财服务
 * 
 * @author Administrator
 * 
 */
public class FinancingserviceActivity extends BaseActivity implements
		OnClickListener {
	/**
	 * 返回按钮
	 */
	private ImageView financingservice_back;

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		setContentView(R.layout.activity_financingservice);
		financingservice_back = (ImageView) findViewById(R.id.financingservice_back);
		financingservice_back.setOnClickListener(this);
		{
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.financingservice_back:
			FinancingserviceActivity.this.finish();
			break;

		default:
			break;
		}
	}
}
