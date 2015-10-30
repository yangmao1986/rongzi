package com.rongdai.activity.personalcenter;

import io.rong.imkit.RongIM;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.rongdai.R;
import com.rongdai.base.BaseActivity;

/**
 * 客服选择
 * 
 * @author Administrator
 * 
 */
public class CustomServiceActivity extends BaseActivity implements
		OnClickListener {

	@Override
	public void onClick(View v) {
		
	}
//	private ImageView customservice_back;
//	/**
//	 * 贷款服务
//	 */
//	private RelativeLayout rl_loanservice;
//	/**
//	 * 理财服务
//	 */
//	private RelativeLayout rl_financingservice;
//	// 用来实现 UI 线程的更新。
//	Handler mHandler;
//
//	@Override
//	public void initData() {
//		// TODO Auto-generated method stub
//		super.initData();
//		setContentView(R.layout.activity_customservice);
//		mHandler = new Handler();
//		customservice_back = (ImageView) findViewById(R.id.customservice_back);
//		rl_loanservice = (RelativeLayout) findViewById(R.id.rl_loanservice);
//		rl_financingservice = (RelativeLayout) findViewById(R.id.rl_financingservice);
//
//		customservice_back.setOnClickListener(this);
//		rl_loanservice.setOnClickListener(this);
//		rl_financingservice.setOnClickListener(this);
//	}
//
//	@Override
//	public void onClick(View view) {
//		switch (view.getId()) {
//		case R.id.customservice_back:
//			this.finish();
//			break;
//		case R.id.rl_loanservice:
//			mHandler.post(new Runnable() {
//				@Override
//				public void run() {
//					//对方的id
//					String userId = "111";
//					String title = "贷款客服";
//					RongIM rongIM = RongIM.getInstance();
//					// rongIM.startPrivateChat(MainActivity.this, userId, title);
//					rongIM.startCustomerServiceChat(CustomServiceActivity.this,
//							userId, title);
//				}
//			});
//			break;
//		case R.id.rl_financingservice:
//			mHandler.post(new Runnable() {
//				@Override
//				public void run() {
//					String userId = "111";
//					String title = "理财客服";
//					RongIM rongIM = RongIM.getInstance();
//					// rongIM.startPrivateChat(MainActivity.this, userId, title);
//					rongIM.startCustomerServiceChat(CustomServiceActivity.this,
//							userId, title);
//				}
//			});
//			break;
//		}
//	}

}
