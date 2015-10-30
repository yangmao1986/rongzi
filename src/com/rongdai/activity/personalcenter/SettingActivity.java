package com.rongdai.activity.personalcenter;

import com.rongdai.R;
import com.rongdai.R.layout;
import com.rongdai.pager.PersonCenter;
import com.rongdai.utils.PersonInfoConstans;
import com.rongdai.utils.ToastUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class SettingActivity extends Activity implements OnClickListener {
	
	/**
	 * 退出登录
	 */
	private ImageButton setting_back;
	/**
	 * 意见反馈
	 */
	private RelativeLayout feedback;
	/**
	 * 关于我们
	 */
	private RelativeLayout aboutus_relativeLayout;
	/**
	 * 退出登录状态
	 */
	private Button quitback_button;
	/**
	 * 理财热线
	 */
	private RelativeLayout phonecall;
	/**
	 * 清除缓存
	 */
	private RelativeLayout clearcache;
	/**
	 * 检测更新
	 */
	private RelativeLayout updata_version;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		
		setting_back = (ImageButton)findViewById(R.id.setting_back);
		setting_back.setOnClickListener(this);
		
		feedback = (RelativeLayout)findViewById(R.id.feedback);
		feedback.setOnClickListener(this);
		
		aboutus_relativeLayout = (RelativeLayout)findViewById(R.id.aboutus_relativeLayout);
		aboutus_relativeLayout.setOnClickListener(this);
		
		phonecall = (RelativeLayout)findViewById(R.id.phonecall);
		phonecall.setOnClickListener(this);
		
		clearcache = (RelativeLayout)findViewById(R.id.clearcache);
		clearcache.setOnClickListener(this);
		
		updata_version = (RelativeLayout)findViewById(R.id.updata_version);
		updata_version.setOnClickListener(this);
		
		
		
		
		quitback_button= (Button)findViewById(R.id.quitback_button);
		quitback_button.setOnClickListener(this);
		
	}
	

	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {	
		case R.id.setting_back:
			this.finish();
			break;
		case R.id.quitback_button:
			SharedPreferences sp = getSharedPreferences(PersonInfoConstans.PERSONINFO,Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString(PersonInfoConstans.USER_NAME, "");
			editor.putString(PersonInfoConstans.PASSWORD, "");
			editor.putString(PersonInfoConstans.RONGDAI_ACCOUNT, "");
			editor.putString(PersonInfoConstans.USRCUSTID, "");
			editor.putString(PersonInfoConstans.HNADPASSWORD, "");
			editor.commit();
			System.exit(0);
			
				break;
		case R.id.feedback:
			intent = new Intent(this,FeedBackActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(intent);
			break;
		case R.id.aboutus_relativeLayout:
			intent = new Intent(this,AboutUsActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(intent);
			break;
		case R.id.phonecall:
			intent =  new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+"4008690311 "));   			
			this.startActivity(intent);
			break;
		case R.id.clearcache:
			ToastUtils.show(SettingActivity.this, "清除缓存成功");
			break;
			
		case R.id.updata_version:
			ToastUtils.show(SettingActivity.this, "目前已经是最新版本");
			break;
			
			
			
			
		default:
			break;
		}
	}
	
	
}
