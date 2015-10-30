package com.rongdai;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.jpush.android.api.InstrumentedActivity;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.activity.personalcenter.CustomServiceActivity;
import com.rongdai.activity.personalcenter.InputHandPasswordActivity;
import com.rongdai.activity.personalcenter.LoginActivity;
import com.rongdai.base.BasePager;
import com.rongdai.calculator.FloatWindowService;
import com.rongdai.managemoney.pager.RecommendProject;
import com.rongdai.pager.IWantLoan;
import com.rongdai.pager.ManagerMenoyCenter;
import com.rongdai.pager.NoticConsult;
import com.rongdai.pager.PersonCenter;
import com.rongdai.utils.Constants;
import com.rongdai.utils.ExitManager;
import com.rongdai.utils.ImageLoadUtils;
import com.rongdai.utils.PersonInfoConstans;
import com.rongdai.utils.UserInfoUtils;
import com.rongdai.view.NoScrollViewPager;

/**
 * 程序的入口
 * 
 * @author Administrator
 * 
 */
public class MainActivity extends InstrumentedActivity implements
		OnCheckedChangeListener, OnClickListener, OnPageChangeListener {
	/**
	 * 触摸不能滚动的viewpager
	 */
	private NoScrollViewPager mViewPager;
	/**
	 * 存放pager的list集合
	 */
	private List<BasePager> pagers;
	/**
	 * 存放点击的radiobutton的位置
	 */
	private int index;
	/**
	 * 客服控件 用来点击到客服页面的
	 */
	private ImageView serviceImageButton;
	/**
	 * 存放radiobutton的控件
	 */
	private RadioGroup mRadioGroup;
	/**
	 * 理财中心的radiobutton
	 */
	private RadioButton manage_money_center;
	private RadioButton i_want_loan;
	private RadioButton notice_consult;
	private RadioButton person_center;
	/**
	 * 显示标题的控件
	 */
	private TextView titleTextView;
	private ManagerMenoyCenter managerMenoyCenter;
	private IWantLoan iWantLoan;
	private NoticConsult noticConsult;
	private PersonCenter personCenter;
	public static Intent serviceIntent;
	private WindowManager wm;
	private int width;
	public static Activity mainActivity;
	SharedPreferences sp;

	/**
	 * 检测登陆状态信息,认证状态信息,用户账号
	 */
	public static String check_login, check_hf_login, check_user;

	/**
	 * 是否从认证汇付天下页面过来
	 */
	private String from_certificate;
	private int intArrays[];
	private String sadasfaf="sada";
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainActivity = this;

		// ------判断手势密码------//

		 sp = getSharedPreferences(
				PersonInfoConstans.PERSONINFO, Context.MODE_PRIVATE);
		long currentTimeMillis = System.currentTimeMillis() / 1000;
		long longTime = sp.getLong(PersonInfoConstans.DATE, 0) / 1000;
		long i = (currentTimeMillis - longTime) / (24 * 60 * 60);
		if (i >= 1) {
			Editor edit = sp.edit();
			edit.clear();
			edit.commit();
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		} else if ("".equals(from_certificate) || from_certificate == null) {
			// 判断是否设置过手势密码，如果设置过，则跳转到输入手势密码界面
			if (!"".equals(sp.getString(PersonInfoConstans.HNADPASSWORD, ""))) {
				Intent intent = new Intent(this,
						InputHandPasswordActivity.class);
				startActivity(intent);
			}
		}

		check_user = sp.getString(PersonInfoConstans.USER_NAME, "");

		Intent intent = getIntent();
		from_certificate = intent.getStringExtra("from_certificate");
		sadasfaf = intent.getStringExtra("asdsad");

		check_login = sp.getString(PersonInfoConstans.RONGDAI_ACCOUNT, "");
		check_hf_login = sp.getString(PersonInfoConstans.USRCUSTID, "");

		/** 初始化View */
		initView();
		/** 加入应用队列 */
		ExitManager.getInstance().addActivity(this);
	}

	private void initView() {

		setContentView(R.layout.activity_main);

		intArrays = new int[] { R.id.manage_money_center, R.id.i_want_loan,
				R.id.notice_consult, R.id.person_center };

		/** 初始化Listview */
		pagers = new ArrayList<BasePager>();
		managerMenoyCenter = new ManagerMenoyCenter(this);
		iWantLoan = new IWantLoan(this);
		noticConsult = new NoticConsult(this);
		personCenter = new PersonCenter(this);
		pagers.add(managerMenoyCenter);
		pagers.add(iWantLoan);
		pagers.add(noticConsult);
		pagers.add(personCenter);
		/**
		 * 开启服务
		 */
		serviceIntent = new Intent(this, FloatWindowService.class);
//		startService(serviceIntent);

		Log.e("startService","startService");
		serviceImageButton = (ImageView) findViewById(R.id.main_service);
		titleTextView = (TextView) findViewById(R.id.tv_logo_title);

		mViewPager = (NoScrollViewPager) findViewById(R.id.main_viewpager);

		mRadioGroup = (RadioGroup) findViewById(R.id.main_rg);
		manage_money_center = (RadioButton) findViewById(R.id.manage_money_center);
		i_want_loan = (RadioButton) findViewById(R.id.i_want_loan);
		notice_consult = (RadioButton) findViewById(R.id.notice_consult);
		person_center = (RadioButton) findViewById(R.id.person_center);

		wm = (WindowManager) this.getSystemService(this.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();

		Drawable[] drawables = manage_money_center.getCompoundDrawables();
		drawables[1].setBounds(0, 0, (int) (width * 0.083),
				(int) (width * 0.083));
		manage_money_center
				.setCompoundDrawables(null, drawables[1], null, null);
		manage_money_center.setChecked(true);

		Drawable[] iWantDdrawables = i_want_loan.getCompoundDrawables();
		iWantDdrawables[1].setBounds(0, 0, (int) (width * 0.083),
				(int) (width * 0.083));
		i_want_loan.setCompoundDrawables(null, iWantDdrawables[1], null, null);

		Drawable[] noticDrawables = notice_consult.getCompoundDrawables();
		noticDrawables[1].setBounds(0, 0, (int) (width * 0.083),
				(int) (width * 0.083));
		notice_consult
				.setCompoundDrawables(null, noticDrawables[1], null, null);

		Drawable[] personDrawables = person_center.getCompoundDrawables();
		personDrawables[1].setBounds(0, 0, (int) (width * 0.083),
				(int) (width * 0.083));
		person_center
				.setCompoundDrawables(null, personDrawables[1], null, null);

		/** 给viewpager填冲view */
		MyPagerAdapter adapter = new MyPagerAdapter();
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(this);

		/** radiogroup添加监听 */
		mRadioGroup.setOnCheckedChangeListener(this);
		serviceImageButton.setOnClickListener(this);

		if ("from_certificate".equals(from_certificate)) {
			from_certificate = "";
			pagers.get(3).initData();
			mRadioGroup.check(R.id.person_center);
			mViewPager.setCurrentItem(3);
			if (serviceIntent != null) {
				stopService(serviceIntent);
				serviceIntent = null;
				Log.e("onPause", "onPause");
			}
		} else {
			
			pagers.get(0).initData();
			mRadioGroup.check(R.id.manage_money_center);
		}
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = pagers.get(position);
			View rootView = pager.getRootView();
			container.addView(rootView);
			// pager.initData();
			return rootView;
		}

		@Override
		public int getCount() {
			return pagers.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, final int checkedId) {

		new Thread() {
			private Message msg;

			public void run() {
				switch (checkedId) {
				// 理财中心
				case R.id.manage_money_center:
					if (serviceIntent == null) {
						serviceIntent = new Intent(MainActivity.this,
								FloatWindowService.class);
						startService(serviceIntent);
					}
					index = 0;
					break;
				// 我要贷款
				case R.id.i_want_loan:
					index = 1;
					break;
				// 公告咨询

				case R.id.notice_consult:
					index = 2;
					break;

				// 个人中心
				case R.id.person_center:
					index = 3;
					break;
				}
				if (index != 0) {
					if (RecommendProject.task != null) {
						RecommendProject.task.cancel();
					}
					if (serviceIntent != null) {
						stopService(serviceIntent);
						serviceIntent = null;
					}
				}
				msg = Message.obtain();
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (index == 0) {
				titleTextView.setText("理财中心");
			} else if (index == 1) {
				titleTextView.setText("我要贷款");
			} else if (index == 2) {
				titleTextView.setText("公告资讯");
			} else if (index == 3) {
				titleTextView.setText("个人中心");
			}
			pagers.get(index).initData();
			mViewPager.setCurrentItem(index);
		};
	};

	@Override
	public void onClick(View v) {
		// Intent intent = new Intent(this, CustomServiceActivity.class);
		// startActivity(intent);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		SharedPreferences sp = getSharedPreferences(
				PersonInfoConstans.PERSONINFO, Context.MODE_PRIVATE);
		// 判断是否已经设置过手势密码
		if (!TextUtils.isEmpty(sp
				.getString(PersonInfoConstans.HNADPASSWORD, ""))) {
			String flag = sp.getString(PersonInfoConstans.FORGET_HNADPASSWORD,
					"");
			// 判断是否是忘记手势密码 但是没有创建新的手势密码
			if (!TextUtils.isEmpty(flag)) {
				Intent intent = new Intent(this,
						InputHandPasswordActivity.class);
				startActivity(intent);
			}
		}
		Log.e("onRestart", "onRestart");
		if (serviceIntent != null) {
			stopService(serviceIntent);
			serviceIntent = null;
			
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		BasePager pager = pagers.get(position);
		pager.initData();
		mRadioGroup.check(intArrays[position]);
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (serviceIntent == null && index == 0) {
			serviceIntent = new Intent(this, FloatWindowService.class);
			startService(serviceIntent);
			Log.e("onResume", "onResume");
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (serviceIntent != null) {
			stopService(serviceIntent);
			serviceIntent = null;
			Log.e("onPause", "onPause");
		}
	}
	
	// 点击头像 打开 相机或者相册时 设置头像
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			Log.e("onActivityResult", "onActivityResult");
			if (requestCode == 1) {
				switch (resultCode) {
				case Activity.RESULT_OK:
					ContentResolver cr = this.getContentResolver();
					try {
						Bitmap cameraBitmap = BitmapFactory.decodeStream(cr
								.openInputStream(Constants.mUri));
						if (cameraBitmap != null) {
							//regist_headimage.setImageBitmap(cameraBitmap);
							//user_imageview.setImageBitmap(cameraBitmap);
							postHeadImage(new File(Constants.mUri.toString().replace("file://", "")));
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case Activity.RESULT_CANCELED:
					break;
				}
			} else if (requestCode == 2) {
				switch (resultCode) {
				case Activity.RESULT_OK:

					ContentResolver cr = this.getContentResolver();
					try {
						Bitmap cameraBitmap = BitmapFactory.decodeStream(cr
								.openInputStream(Constants.mUri));
						if (cameraBitmap != null) {
							//regist_headimage.setImageBitmap(cameraBitmap);
							//user_imageview.setImageBitmap(cameraBitmap);
							postHeadImage(new File(Constants.mUri.toString().replace("file://", "")));
							//photoimage="1";
						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
					
				case Activity.RESULT_CANCELED:
					break;
				}
			}
			
		}
	

		/**
		 * 上传头像
		 * 
		 * @param file
		 */
		private void postHeadImage(File file) {
			HttpUtils httpUtils = new HttpUtils();
			httpUtils.configCurrentHttpCacheExpiry(0);
			httpUtils.configDefaultHttpCacheExpiry(0);
			String imageName = file.toString().substring(file.toString().lastIndexOf("/"), file.toString().length());
			//System.out.println(imageName);
			RequestParams params = new RequestParams();
			params.addBodyParameter("rongDaiAccount", UserInfoUtils.getRongdaiAccount(this));
			params.addBodyParameter("uploadFile", file);
			params.addBodyParameter("uploadFileFileName", ".jpg");
			httpUtils.send(HttpMethod.POST, Constants.modifyHeadImg, params, new RequestCallBack<String>() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onSuccess(ResponseInfo<String> info) {
					System.out.println(info.result);
					ImageLoadUtils.loadimage(MainActivity.this,sp.getString(PersonInfoConstans.IMAGEURI, ""), PersonCenter.user_imageview);	
				}
			});
		}
		
		

}
