package com.rongdai.pager;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.MainActivity;
import com.rongdai.R;
import com.rongdai.activity.NoticConsult.NoticeInfo;
import com.rongdai.activity.personalcenter.CertificateHuiFu;
import com.rongdai.activity.personalcenter.GetMoneyActivity;
import com.rongdai.activity.personalcenter.InvestmentProjectActivity;
import com.rongdai.activity.personalcenter.LoginActivity;
import com.rongdai.activity.personalcenter.MoneyRecordActivity;
import com.rongdai.activity.personalcenter.MyRedEnvelopeActivity;
import com.rongdai.activity.personalcenter.MyUserActivity;
import com.rongdai.activity.personalcenter.PayActivity;
import com.rongdai.activity.personalcenter.RecentDealActivity;
import com.rongdai.activity.personalcenter.RegistActivity;
import com.rongdai.activity.personalcenter.SecurityCenterActivity;
import com.rongdai.activity.personalcenter.SettingActivity;
import com.rongdai.base.BaseActivity;
import com.rongdai.base.BaseApplication;
import com.rongdai.base.BasePager;
import com.rongdai.domain.investAndEarn;
import com.rongdai.utils.Constants;
import com.rongdai.utils.ImageLoadUtils;
import com.rongdai.utils.PersonInfoConstans;
import com.rongdai.utils.ToastUtils;
import com.rongdai.utils.UserInfoUtils;


public class PersonCenter extends BasePager implements OnClickListener {

	/**
	 * 去登陆
	 */
	private LinearLayout goto_login;
	/**
	 * 设置
	 */
	private RelativeLayout goto_setting;
	/**
	 * 安全中心
	 */
	private RelativeLayout goto_security_center;

	/**
	 * 我的账户
	 */
	private RelativeLayout goto_MyUser_relativelayout;
	
	/**
	 * 我的红包
	 */
	private RelativeLayout goto_MyRedEnvelope;
	
	/**
	 * 最近交易
	 */
	private RelativeLayout goto_recentdeal;
	
	/**
	 * 资金记录
	 */
	private RelativeLayout goto_MoneyRecord;
	
	

	/**
	 * 最近交易
	 */
	private RelativeLayout goto_InvestmentProject;
	

	/**
	 * 我的消息
	 */
	private RelativeLayout my_message;
	
	
	
	
	
	/**
	 * 登陆检测
	 */
	private LinearLayout check_login;
	
	
	/**
	 * 检测汇付认证
	 */
	private LinearLayout check_hf_login;
	
	
	/**
	 * 充值
	 * 提现
	 */
	
	private Button pay_button,getmoney_button;
	
	/**
	 * 余额,账号,赚,投
	 * 
	 */
	
	private TextView queryBalance_textview,user_textview,earn_textview,investmentCount_textview,waitEarn_textview,waitInvestmentCount_textview;
	
	private String check_login_string,check_hf_login_string;
	/**
	 * 用户头像
	 * 
	 */
	public static ImageView user_imageview;
	
	/**
	 * 上传头像
	 * 
	 */
	private LinearLayout updata_image;
	
	private PopupWindow popupWindow;
	private View allView;
	private Button picture;
	private Button back;
	private Button camera;
	

	
	Activity mActivity;
	
	public PersonCenter(Context mContext) {
		super(mContext);
	}

	@Override
	public View initView(Context context) {
		mActivity=(Activity) context;
		return View.inflate(context, R.layout.fragment_person_center, null);
		
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		
		 check_login_string = MainActivity.check_login;
		 check_hf_login_string = MainActivity.check_hf_login;
		RelativeLayout recommend_friend = (RelativeLayout) RootView
				.findViewById(R.id.recommend_friend);
		Button login = (Button) RootView.findViewById(R.id.personcenter_login);
		Button regist = (Button) RootView.findViewById(R.id.personcenter_regist);
		Button open = (Button) RootView.findViewById(R.id.personcenter_open);
		login.setOnClickListener(this);
		regist.setOnClickListener(this);
		open.setOnClickListener(this);
		recommend_friend.setOnClickListener(this);
		
		if (check_login_string == null || check_login_string.equals("")) {
			check_login = (LinearLayout)RootView.findViewById(R.id.check_login);
		    check_login.setVisibility(View.VISIBLE);		
		}else if (check_hf_login_string == null || check_hf_login_string.equals("")) {
			check_hf_login = (LinearLayout)RootView.findViewById(R.id.check_hf_login);
			check_hf_login.setVisibility(View.VISIBLE);		
		}else{
			
			goto_setting = (RelativeLayout) RootView.findViewById(R.id.goto_setting);
			goto_setting.setOnClickListener(PersonCenter.this);
			
			goto_security_center = (RelativeLayout) RootView.findViewById(R.id.goto_security_center);
			goto_security_center.setOnClickListener(PersonCenter.this);
			
			goto_MyUser_relativelayout = (RelativeLayout) RootView.findViewById(R.id.goto_MyUser_relativelayout);
			goto_MyUser_relativelayout.setOnClickListener(PersonCenter.this);
			
			updata_image = (LinearLayout) RootView.findViewById(R.id.updata_image);
			updata_image.setOnClickListener(PersonCenter.this);
			
			SharedPreferences sp = mContext.getSharedPreferences(
					PersonInfoConstans.PERSONINFO, Context.MODE_PRIVATE);
			
			
			user_imageview = (ImageView) RootView.findViewById(R.id.user_imageview);
			
			if(sp.getString(PersonInfoConstans.IMAGEURI, "")!=null){
				ImageLoadUtils.loadimage(mContext,sp.getString(PersonInfoConstans.IMAGEURI, ""), user_imageview);	
			}
			
			
			
/*			goto_MyRedEnvelope = (RelativeLayout) RootView.findViewById(R.id.goto_MyRedEnvelope);
			goto_MyRedEnvelope.setOnClickListener(PersonCenter.this);*/
			
			
			my_message = (RelativeLayout) RootView.findViewById(R.id.my_message);
			my_message.setOnClickListener(PersonCenter.this);
			
			goto_MoneyRecord = (RelativeLayout) RootView.findViewById(R.id.goto_MoneyRecord);	
			goto_MoneyRecord.setOnClickListener(PersonCenter.this);
			
			goto_recentdeal = (RelativeLayout) RootView.findViewById(R.id.goto_recentdeal);	
			goto_recentdeal.setOnClickListener(PersonCenter.this);
			
			goto_InvestmentProject = (RelativeLayout) RootView.findViewById(R.id.goto_InvestmentProject);
			goto_InvestmentProject.setOnClickListener(PersonCenter.this);
			
			pay_button = (Button) RootView.findViewById(R.id.pay_button);
			pay_button.setOnClickListener(PersonCenter.this);
			
			getmoney_button = (Button) RootView.findViewById(R.id.getmoney_button);
			getmoney_button.setOnClickListener(PersonCenter.this);
			
			queryBalanceDo();			
			queryBalance_textview = (TextView) RootView.findViewById(R.id.queryBalance_textview);
			
			user_textview = (TextView) RootView.findViewById(R.id.user_textview);
			user_textview.setText(MainActivity.check_user);
			
			earn_textview = (TextView) RootView.findViewById(R.id.earn_textview);
			
			investmentCount_textview = (TextView) RootView.findViewById(R.id.investmentCount_textview);
			
			waitEarn_textview= (TextView) RootView.findViewById(R.id.waitEarn_textview);
			
			waitInvestmentCount_textview= (TextView) RootView.findViewById(R.id.waitInvestmentCount_textview);
			
			
			allView = View.inflate(mContext, R.layout.activity_regist, null);
			allView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					dismissPopupWindow();
					return false;
				}
			});
			
		}
		
		
	
		
		
		
		
		
		
		

		
	}

	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {
		case R.id.goto_setting:
			intent = new Intent(mContext,SettingActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
			break;
		case R.id.goto_security_center:
			intent = new Intent(mContext,SecurityCenterActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
			break;
		case R.id.goto_MyUser_relativelayout:
			intent = new Intent(mContext,MyUserActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
			break;
		case R.id.pay_button:
			//到快捷绑定须知页面
//			intent = new Intent(mContext,PayDetailedActivity.class);
			intent = new Intent(mContext,PayActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
			break;
		case R.id.getmoney_button:
			intent = new Intent(mContext,GetMoneyActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
			break;
		/*case R.id.goto_MyRedEnvelope:
			intent = new Intent(mContext,MyRedEnvelopeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
			break;*/
		case R.id.goto_recentdeal:
			intent = new Intent(mContext,RecentDealActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
			break;
		case R.id.goto_MoneyRecord:
			intent = new Intent(mContext,MoneyRecordActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
			break;
			
		case R.id.goto_InvestmentProject:
			intent = new Intent(mContext,InvestmentProjectActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			mContext.startActivity(intent);
			break;
			
		case R.id.personcenter_login:
			intent = new Intent(mContext,LoginActivity.class);
			mContext.startActivity(intent);
			break;
			
		case R.id.personcenter_regist:	
			intent = new Intent(mContext,RegistActivity.class);
			mContext.startActivity(intent);
			break;
			
		case R.id.personcenter_open:	
			intent = new Intent(mContext,CertificateHuiFu.class);
			mContext.startActivity(intent);
			break;
		case R.id.recommend_friend:	
			showShare();
			break;
		case R.id.my_message:	
			
			Toast.makeText(mContext, "暂无", 2000).show();
			
			break;
        case R.id.updata_image:	
        	
        	WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
			int height = manager.getDefaultDisplay().getHeight();
			int width = manager.getDefaultDisplay().getWidth();
			if (popupWindow == null || !popupWindow.isShowing()) {
				View contentView = View.inflate(mContext, R.layout.popupwindow,
						null);
				picture = (Button) contentView.findViewById(R.id.picture);
				camera = (Button) contentView.findViewById(R.id.camera);
				back = (Button) contentView.findViewById(R.id.back);
				picture.setOnClickListener(this);
				camera.setOnClickListener(this);
				back.setOnClickListener(this);
				popupWindow = new PopupWindow(contentView);
				popupWindow.setWidth(width);
				popupWindow.setHeight(height / 3);// 设置弹出框大小
				popupWindow.showAsDropDown(allView, 0, height / 3 * 2);
				popupWindow.setOutsideTouchable(true);
			
			
			break;
	
			}
        case R.id.picture:
			dismissPopupWindow();
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			intent.putExtra("crop", "true");
			Constants.mUri= Uri.fromFile(new File(Environment
					.getExternalStorageDirectory() + "/Images/", "cameraImg"
					+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Constants.mUri);
			mActivity.startActivityForResult(intent, 2);

			break;
		case R.id.camera:
			dismissPopupWindow();
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Toast.makeText(mContext, "SD卡不存在,请检查SD卡", Toast.LENGTH_SHORT).show();
				return;
			}
			intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/Images");
			if (!file.exists()) {
				file.mkdirs();
			}
			Constants.mUri = Uri.fromFile(new File(Environment
					.getExternalStorageDirectory() + "/Images/", "cameraImg"
					+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Constants.mUri);
			intent.putExtra("return-data", true);
			mActivity.startActivityForResult(intent, 1);

			break;
		case R.id.back:
			dismissPopupWindow();
			break;
			
			
			
		default:
			break;
		}
	}
	
	
	/**
	 * shareSDK分享
	 */
		private void showShare() {
			 ShareSDK.initSDK(BaseApplication.getContext());
			 OnekeyShare oks = new OnekeyShare();
			 //关闭sso授权
			 oks.disableSSOWhenAuthorize(); 

			// 分享时Notification的图标和文字
			 oks.setNotification(R.drawable.logo_luache, BaseApplication.getContext().getString(R.string.app_name));
			 // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
			 oks.setTitle(BaseApplication.getContext().getString(R.string.share));
			 // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
			 oks.setTitleUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.rongdai");
			 // text是分享文本，所有平台都需要这个字段
			 oks.setText("http://a.app.qq.com/o/simple.jsp?pkgname=com.rongdai");
			 // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
			 oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
			 // url仅在微信（包括好友和朋友圈）中使用
			 oks.setUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.rongdai");
			 // comment是我对这条分享的评论，仅在人人网和QQ空间使用
			 oks.setComment("我是测试评论文本");
			 // site是分享此内容的网站名称，仅在QQ空间使用
			 oks.setSite(BaseApplication.getContext().getString(R.string.app_name));
			 // siteUrl是分享此内容的网站地址，仅在QQ空间使用
			 oks.setSiteUrl("http://a.app.qq.com/o/simple.jsp?pkgname=com.rongdai");

			// 启动分享GUI
			 oks.show(BaseApplication.getContext());
			 }

	private void queryBalanceDo() {
	
	
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("rongDaiAccount", check_login_string);	
		params.addBodyParameter("usrcustId", check_hf_login_string);	
		httpUtils.send(HttpMethod.POST, Constants.investAndEarn, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}
					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						//System.out.println(json);
						/*Gson gson = new Gson();
						
						investAndEarn investAndEarn = gson.fromJson(json, investAndEarn.class);*/
						Gson gson = new Gson();
			            java.lang.reflect.Type type = new TypeToken<investAndEarn>() {}.getType();
			            investAndEarn investAndEarn = gson.fromJson(json, type);
						
						Log.e("余额", investAndEarn.message);
						
						 double a=Double.valueOf(investAndEarn.data.canUseBal.replaceAll(",","")).doubleValue();
						 double b=Double.valueOf(investAndEarn.data.earn).doubleValue();
						 double c=Double.valueOf(investAndEarn.data.investmentCount).doubleValue();
						 double d=Double.valueOf(investAndEarn.data.waitEarn).doubleValue();
						 double e=Double.valueOf(investAndEarn.data.waitInvestmentCount).doubleValue();
						queryBalance_textview.setText("￥"+String.format("%.2f", a));
						
						earn_textview.setText(String.format("%.2f", b));
						
						investmentCount_textview.setText(String.format("%.2f", c));
						
						waitEarn_textview.setText(String.format("%.2f", d)+"[待收收益]");
						waitInvestmentCount_textview.setText(String.format("%.2f", e)+"[待收本金]");
						
					}
				});

	}
	
	
	/**
	 * 关闭当前界面的弹出窗体
	 */
	private void dismissPopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}
	
	
	
	
}
