package com.rongdai.activity.personalcenter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rongdai.MainActivity;
import com.rongdai.R;
import com.rongdai.base.BaseActivity;
import com.rongdai.utils.ExitManager;
import com.rongdai.utils.PersonInfoConstans;
import com.rongdai.utils.UserInfoUtils;
import com.rongdai.view.ContentView;
import com.rongdai.view.Drawl.GestureCallBack;

public class SetHandPasswordActivity extends BaseActivity implements
		OnClickListener {
	private FrameLayout body_layout;
	private ContentView content;
	public static TextView sethand_title;
	public static TextView sethand_info;
	private Button openlater;
	private Button goopen;
	private Dialog dialog;

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		setContentView(R.layout.activity_sethand_psw);
		body_layout = (FrameLayout) findViewById(R.id.body_layout);
		sethand_title = (TextView) findViewById(R.id.sethand_title);
		sethand_info = (TextView) findViewById(R.id.sethand_info);
		setHandPassword();
	}

	/**
	 * 设置手势密码
	 */
	private void setHandPassword() {
		// 初始化一个显示各个点的viewGroup
		content = new ContentView(this, "14569", new GestureCallBack() {

			@Override
			public void checkedSuccess() {

			}

			@Override
			public void checkedFail() {

			}

			@Override
			public void setSuccess() {
				Toast.makeText(SetHandPasswordActivity.this, "设置手势密码成功",
						Toast.LENGTH_SHORT).show();
				SharedPreferences sp = getSharedPreferences(
						PersonInfoConstans.PERSONINFO, Context.MODE_PRIVATE);
				Editor editor;
				// editor.putString(PersonInfoConstans.HNADPASSWORD,
				// PersonInfoConstans.HNADPASSWORD);
				// editor.commit();
				if (!TextUtils.isEmpty(sp.getString(
						PersonInfoConstans.FORGET_HNADPASSWORD, ""))) {
					editor = sp.edit();
					// 保证从其他界面跳转到登陆界面点击登录时不会触发此方法
					editor.putString(PersonInfoConstans.FORGET_HNADPASSWORD, "");
					editor.commit();
				
				}
				if(TextUtils.isEmpty(UserInfoUtils.getUsrCustId(SetHandPasswordActivity.this))){
					Intent intent = new Intent(SetHandPasswordActivity.this,OpenActivity.class);
					startActivity(intent);
				}else{
					/*Intent intent =new Intent(SetHandPasswordActivity.this,MainActivity.class);
					intent.putExtra("from_certificate", "from_certificate");
					intent.putExtra("asdsad", "sadasfaf");
					SetHandPasswordActivity.this.startActivity(intent);*/
					System.exit(0);
					
				}
				SetHandPasswordActivity.this.finish();
			}

			@Override
			public void setFail() {
				Toast.makeText(SetHandPasswordActivity.this, "设置手势密码失败",
						Toast.LENGTH_SHORT).show();
				SharedPreferences sp = getSharedPreferences(
						PersonInfoConstans.PERSONINFO, Context.MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putString(PersonInfoConstans.HNADPASSWORD, "");
				editor.commit();
				sethand_title.setText("创建手势密码");
				sethand_info.setText("手势密码将在您进入APP时启动");
			}
		});
		// 设置手势解锁显示到哪个布局里面
		content.setParentView(body_layout);

	}


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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
