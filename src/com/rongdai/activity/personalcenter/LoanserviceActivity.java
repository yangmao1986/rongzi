package com.rongdai.activity.personalcenter;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.rongdai.R;
import com.rongdai.base.BaseActivity;

/**
 * 贷款客服
 * @author Administrator
 *
 */
public class LoanserviceActivity extends BaseActivity implements OnClickListener {
	/**
	 * 返回按钮
	 */
	private ImageView loanservice_back;

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		setContentView(R.layout.activity_loanservice);
		loanservice_back = (ImageView) findViewById(R.id.loanservice_back);
		loanservice_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.loanservice_back:
			this.finish();
			break;

		default:
			break;
		}
	}
}
