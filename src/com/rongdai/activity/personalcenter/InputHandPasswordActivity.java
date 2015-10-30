package com.rongdai.activity.personalcenter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.rongdai.MainActivity;
import com.rongdai.R;
import com.rongdai.base.BaseActivity;
import com.rongdai.utils.ExitManager;
import com.rongdai.utils.PersonInfoConstans;
import com.rongdai.view.ContentView;
import com.rongdai.view.Drawl.GestureCallBack;

public class InputHandPasswordActivity extends BaseActivity {
	private FrameLayout body_layout;
	private ContentView content;
	ImageView handpsw_headimage;

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		setContentView(R.layout.activity_inputhand_psw);
		handpsw_headimage = (ImageView) findViewById(R.id.handpsw_headimage);
		body_layout = (FrameLayout) findViewById(R.id.set_body_layout);
		Button gorget_handpsw = (Button) findViewById(R.id.gorget_handpsw);
		gorget_handpsw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				SharedPreferences sp = getSharedPreferences(
//						PersonInfoConstans.PERSONINFO, Context.MODE_PRIVATE);
//				Editor editor = sp.edit();
//				editor.putString(PersonInfoConstans.FORGET_HNADPASSWORD,
//						PersonInfoConstans.FORGET_HNADPASSWORD);
//				editor.putString(PersonInfoConstans.HNADPASSWORD,
//						"");
//				editor.commit();
				SharedPreferences sp = getSharedPreferences(PersonInfoConstans.PERSONINFO,Context.MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putString(PersonInfoConstans.USER_NAME, "");
				editor.putString(PersonInfoConstans.PASSWORD, "");
				editor.putString(PersonInfoConstans.RONGDAI_ACCOUNT, "");
				editor.putString(PersonInfoConstans.USRCUSTID, "");
				editor.putString(PersonInfoConstans.HNADPASSWORD, "");
				editor.commit();
				System.exit(0);
				Intent intent = new Intent(InputHandPasswordActivity.this,
						LoginActivity.class);
				startActivity(intent);
				InputHandPasswordActivity.this.finish();
			}
		});
		inputPassword();
	}

	private void inputPassword() {
		// 初始化一个显示各个点的viewGroup
		content = new ContentView(this, "", new GestureCallBack() {

			@Override
			public void checkedSuccess() {
				InputHandPasswordActivity.this.finish();
			}

			@Override
			public void checkedFail() {
				Toast.makeText(InputHandPasswordActivity.this, "校验失败",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void setSuccess() {
				// TODO Auto-generated method stub

			}

			@Override
			public void setFail() {
				// TODO Auto-generated method stub

			}
		});
		// 设置手势解锁显示到哪个布局里面
		content.setParentView(body_layout);

	}

	// private long exitTime = 0;
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() ==
	// KeyEvent.ACTION_DOWN){
	// if((System.currentTimeMillis()-exitTime) > 3000){
	// Toast.makeText(getApplicationContext(), "再按一次退出程序",
	// Toast.LENGTH_SHORT).show();
	// exitTime = System.currentTimeMillis();
	// } else {
	// finish();
	// System.exit(0);
	// }
	// return true;
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	long waitTime = 2000;
	long touchTime = 0;

	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - touchTime) >= waitTime) {
			Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		} else {
			ExitManager.getInstance().exit();
		}
	}

}
