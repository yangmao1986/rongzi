package com.rongdai.managemoney.pager;

import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import android.R.color;
import android.app.Activity;
import android.content.Entity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.rongdai.MainActivity;
import com.rongdai.R;
import com.rongdai.activity.personalcenter.CustomServiceActivity;
import com.rongdai.base.BaseApplication;
import com.rongdai.constants.ConfigConstants;
import com.rongdai.constants.IntentConstants;
import com.rongdai.domain.RecommendProjectBean;
import com.rongdai.domain.RecommendProjectBean.RecommendProjectData;
import com.rongdai.utils.ConfigFileUtils;
import com.rongdai.utils.GetNetTime;
import com.rongdai.utils.UserInfoUtils;
import com.rongdai.view.PullToRefreshView;
import com.rongdai.view.PullToRefreshView.OnFooterRefreshListener;
import com.rongdai.view.PullToRefreshView.OnHeaderRefreshListener;
import com.rongdai.view.RoundProgressBar;

/**
 * @author Administrator
 *
 */
public class ProjectDetail extends Activity implements OnClickListener,
		OnCheckedChangeListener, OnFooterRefreshListener,
		OnHeaderRefreshListener {

	private ProjectInfo projectInfo;
	private int time = 60;
	private Timer timer = new Timer();
	private TimerTask task;
	private static boolean flag = true;
	/**
	 * 企业经营贷
	 */
	private TextView companyTextView;
	/**
	 * 消费周转贷
	 */
	private TextView consumeTextView;
	/**
	 * 定时标
	 */
	private TextView timingTextView;
	/**
	 * 定向标
	 */
	private TextView dingxiangTextView;
	/**
	 * 标名
	 */
	private TextView titleTextView;
	/**
	 * 融资金额
	 */
	private TextView moneyTextView;
	/**
	 * 融资期限
	 */
	private TextView timeTextView;
	/**
	 * 年华收益
	 */
	private TextView harvestTextView;
	/**
	 * 圆形进度条
	 */
	private RoundProgressBar mRoundProgressBar;
	/**
	 * 盛放四个RadioButton的RadioGroup
	 */
	private RadioGroup mRadioGroup;
	/**
	 * 项目信息
	 */
	private RadioButton infoRadioButton;
	/**
	 * 还款模型
	 */
	private RadioButton modeRadioButton;
	/**
	 * 相关资料
	 */
	private RadioButton dataRadioButton;
	/**
	 * 投标情况
	 */
	private RadioButton caseRadioButton;

	private FrameLayout fl;
	/**
	 * 返回键
	 */
	private ImageView backImageButton;
	// 页面标题
	private TextView pagerTitle;
	/**
	 * 联系客服
	 */
	private ImageView serviceImageButton;
	/**
	 * 还款模型页面
	 */
	private RepaymentMode repaymentMode;
	/**
	 * 投标情况页面
	 */
	private TenderCase tenderCase;
	/**
	 * 相关资料页面
	 */
	private RelatedDataPager mRelatedDataPager;
	/**
	 * 剩余时间
	 */
	private static TextView project_detail_info_residue_time;
	/**
	 * 马上投资
	 */
	private static Button project_detail_info_immediately;
	/**
	 * 传递过来的bean
	 */
	private static RecommendProjectData recommendProjectBean;
	/**
	 * 传递过来的position
	 */
	private int position;
	/**
	 * 标信息
	 */
	private List<RecommendProjectData> borrowData;
	private int repaymentModeNumber = 1;
	private int tenderCaseNumber = 1;
	private int mRelatedDataPagerNumber = 1;
	private int flags = 0;
	private int refreshState = 0;

	private PullToRefreshView mPullToRefreshView;

	private LinearLayout mLinearLayout;
	private static int TIME = 1;
	private static Thread subThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		projectInfo = new ProjectInfo(this);
		repaymentMode = new RepaymentMode(this);
		tenderCase = new TenderCase(this);
		mRelatedDataPager = new RelatedDataPager(this);
		flag = true;
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_project_detail);

		Intent intent = getIntent();
		recommendProjectBean = (RecommendProjectData) intent
				.getSerializableExtra("bean");
		/** 给项目信息页面传递pagernumber和projectId */
		projectInfo.setProjectId(recommendProjectBean.borrowId);

		/** 给项还款模型页面传递pagernumber和projectId */
		repaymentMode.setProjectId(recommendProjectBean.borrowId);

		/** 给相关资料页面传递pagernumber和projectId */
		mRelatedDataPager.setProjectId(recommendProjectBean.borrowId);

		/** 给投标情况页面传递pagernumber和projectId */
		tenderCase.setProjectId(recommendProjectBean.borrowId);
		
		mLinearLayout = (LinearLayout) findViewById(R.id.other_project_item_one);
		mLinearLayout.setFocusable(true);
		mLinearLayout.setFocusableInTouchMode(true);
		mLinearLayout.requestFocus();
		
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_refresh_view);
		
		project_detail_info_residue_time = (TextView) findViewById(R.id.project_detail_info_residue_time);
		project_detail_info_immediately = (Button) findViewById(R.id.project_detail_info_immediately);

		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);

		backImageButton = (ImageView) findViewById(R.id.ib_back);
		pagerTitle = (TextView) findViewById(R.id.tv_title);
		serviceImageButton = (ImageView) findViewById(R.id.ib_service);
		serviceImageButton.setVisibility(View.GONE);
		companyTextView = (TextView) findViewById(R.id.recommend_company);
		consumeTextView = (TextView) findViewById(R.id.recommend_consume);
		dingxiangTextView = (TextView) findViewById(R.id.recommend_dingxiang);
		timingTextView = (TextView) findViewById(R.id.recommend_timing);

		titleTextView = (TextView) findViewById(R.id.recommend_item_loan_title);
		moneyTextView = (TextView) findViewById(R.id.recommend_money);
		timeTextView = (TextView) findViewById(R.id.recommend_time);
		harvestTextView = (TextView) findViewById(R.id.recommend_harvest);

		mRoundProgressBar = (RoundProgressBar) findViewById(R.id.recommend_rpb);

		mRadioGroup = (RadioGroup) findViewById(R.id.project_detail_rg);
		infoRadioButton = (RadioButton) findViewById(R.id.project_detail_info);
		modeRadioButton = (RadioButton) findViewById(R.id.project_detail_mode);
		dataRadioButton = (RadioButton) findViewById(R.id.project_detail_data);
		caseRadioButton = (RadioButton) findViewById(R.id.project_detail_case);

		fl = (FrameLayout) findViewById(R.id.project_detail_fl);

		infoRadioButton.setChecked(true);
		pagerTitle.setText("项目详情");
		showProjectInfo();

		projectInfo.initData();
		fl.addView(projectInfo.getRootView());

		backImageButton.setOnClickListener(this);
		serviceImageButton.setOnClickListener(this);
		project_detail_info_immediately.setOnClickListener(this);
		mRadioGroup.setOnCheckedChangeListener(this);
	}

	/**
	 * 展示项目的基本信息
	 */
	private void showProjectInfo() {
		maxP = Integer.parseInt(recommendProjectBean.borrowAccount) * 10000;
		mRoundProgressBar.setMax(maxP);
		if (recommendProjectBean.borrowHadRongZi == null
				|| "".equals(recommendProjectBean.borrowHadRongZi)) {
			currentP = 0;
			mRoundProgressBar.setProgress(currentP);
		} else {
			currentP = (int) Double.parseDouble(recommendProjectBean.borrowHadRongZi);
			mRoundProgressBar.setProgress(currentP);
		}

		titleTextView.setText(recommendProjectBean.borrowName);
		moneyTextView.setText(recommendProjectBean.borrowAccount + "万元");
		timeTextView.setText(recommendProjectBean.borrowLimit + "个月");
		harvestTextView.setText(recommendProjectBean.borrowYearRate);
		companyTextView.setVisibility(View.GONE);
		consumeTextView.setVisibility(View.GONE);
		dingxiangTextView.setVisibility(View.GONE);
		timingTextView.setVisibility(View.GONE);

		if ("融影宝".equals(recommendProjectBean.borrowType)) {
			companyTextView.setVisibility(View.VISIBLE);
			companyTextView.setText("融影宝");
			timingTextView.setVisibility(View.VISIBLE);
		}else{
			if ("企业经营贷".equals(recommendProjectBean.borrowType)) {
				companyTextView.setVisibility(View.VISIBLE);
			}
			if ("消费周转贷".equals(recommendProjectBean.borrowType)) {
				consumeTextView.setVisibility(View.VISIBLE);
			}
//			if ("定向标".equals(recommendProjectBean.borrowType)) {
//				dingxiangTextView.setVisibility(View.VISIBLE);
//			}
			if ("定时标".equals(recommendProjectBean.borrowType)) {
				timingTextView.setVisibility(View.VISIBLE);
			}
		}
		endTime = recommendProjectBean.borrowEndTime;
		startTime = recommendProjectBean.borrowDate;
		System.out.println("开始时间"+startTime);
	
		
		if(currentP>=maxP){
			project_detail_info_residue_time.setText("投标已结束");
			project_detail_info_immediately.setClickable(false);
			project_detail_info_immediately.setBackgroundColor(Color.GRAY);
			
		}else{
			try {
				getTime();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 处理点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/** 返回 */
		case R.id.ib_back:
			flag = false;
			this.finish();
			break;

		/** 到客服 */
		case R.id.ib_service:
//			Intent intent = new Intent(this, CustomServiceActivity.class);
//			startActivity(intent);
			break;

		/** 马上投资 */
		case R.id.project_detail_info_immediately:
			if(currentP >= maxP){
				return ;
			}
			if (TextUtils.isEmpty(UserInfoUtils.getRongdaiAccount(this))) {
				Toast.makeText(BaseApplication.getContext(), "未注册融贷账号", 0)
						.show();
				return;
			}
			if (TextUtils.isEmpty(UserInfoUtils.getUsrCustId(this))) {
				Toast.makeText(BaseApplication.getContext(), "未注册汇付天下账号", 0)
						.show();
				return;
			}
			Intent immediatelyintent = new Intent(BaseApplication.getContext(),
					ConfirmTender.class);
			immediatelyintent.putExtra(IntentConstants.BMID,
					recommendProjectBean.borrowId);
			immediatelyintent.putExtra(IntentConstants.BORROWTYPE,
					recommendProjectBean.borrowType);
			immediatelyintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			BaseApplication.getContext().startActivity(immediatelyintent);
			break;

		default:
			break;
		}
	}

	/**
	 * 处理RadioButton的点击事件
	 */
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		/** 加载项目信息页面 */
		case R.id.project_detail_info:
			flags = 0;
			fl.removeAllViews();
			projectInfo.initData();
			fl.addView(projectInfo.getRootView());
			break;

		/** 加载还款模型页面 */
		case R.id.project_detail_mode:
			flags = 1;
			fl.removeAllViews();
			repaymentMode.initData();
			fl.addView(repaymentMode.getRootView());

			break;

		/** 加载相关资料页面 */
		case R.id.project_detail_data:
			flags = 2;
			fl.removeAllViews();
			mRelatedDataPager.initData();
			fl.addView(mRelatedDataPager.getRootView());
			break;

		/** 加载投标情况页面 */
		case R.id.project_detail_case:
			flags = 3;
			fl.removeAllViews();
			tenderCase.initData();
			fl.addView(tenderCase.getRootView());
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ConfigFileUtils.clearConfigFile(ConfigConstants.PROJECTDETAILPAGER);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		refreshState = 1;
		if (flags == 0) {
			projectInfo.getDataFromNet(refreshState, mPullToRefreshView);
		} else if (flags == 1) {
			repaymentMode.getDataFromNet(refreshState, mPullToRefreshView);
		} else if (flags == 2) {
			mRelatedDataPager.getDataFromNet(refreshState, mPullToRefreshView);
		} else if (flags == 3) {
			tenderCase.getDataFromNet(refreshState, mPullToRefreshView);
		}
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		refreshState = 2;
		if (flags == 0) {
			projectInfo.getDataFromNet(refreshState, mPullToRefreshView);
		} else if (flags == 1) {
			repaymentMode.getDataFromNet(refreshState, mPullToRefreshView);
		} else if (flags == 2) {
			mRelatedDataPager.getDataFromNet(refreshState, mPullToRefreshView);
		} else if (flags == 3) {
			tenderCase.getDataFromNet(refreshState, mPullToRefreshView);
		}
	}

	private static String lastTime;
	private static long td;
	private static Long ltime;
	private static Long stime;
	private static long subTime;
	private static Handler handlerTime = new Handler() {

		public void handleMessage(Message msg) {
			try {
//				endTime.replace("　", " ");
				 endTime = formatData(endTime);
				 startTime = formatData(startTime);
				ltime = DataToTime(endTime);
				stime = DataToTime(startTime);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			td = (Long) msg.obj;
			
			Log.e("stime", stime+"");
			if(stime>td){
				changeButton();
				return ;
			}
			subTime = (ltime - td) / 1000;
			if(subTime<=0){
				project_detail_info_residue_time.setText("投标已结束");
				project_detail_info_immediately.setClickable(false);
				project_detail_info_immediately.setBackgroundColor(Color.GRAY);
				return ;
			}
			subThread = new Thread(new MyThread());
			subThread.start();
		};
	};

	final static Handler handler = new Handler() { // handle
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				subTime--;
				long day = subTime / (60 * 60 * 24);
				long hour = (subTime % (60 * 60 * 24)) / (60 * 60);
				long mins = ((subTime % (60 * 60 * 24)) % (60 * 60)) / 60;
				long sencend = ((subTime % (60 * 60 * 24)) % (60 * 60)) % 60;

				project_detail_info_residue_time.setText(day + "天" + hour
						+ "小时" + mins + "分" + sencend + "秒");
			}
			super.handleMessage(msg);
		}
	};

	private static String endTime;
	private static String startTime;
	private int maxP;
	private int currentP;

	public static class MyThread implements Runnable { // thread
		@Override
		public void run() {
			while (flag) {
				try {
					Thread.sleep(1000); // sleep 1000ms
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 获取网络时间
	 * 
	 * @throws Exception
	 */
	public static void getTime() throws Exception {
		new Thread() {
			public void run() {

				URL url;
				try {
					  
					java.util.Locale locale=java.util.Locale.CHINA;
					  String pattern = "yyyy-MM-dd kk:mm:ss zZ";
					  java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(pattern,locale);
					  java.util.Date date = new java.util.Date();
					  String bjTime = df.format(date);
					  
					long ld = stringToLong(bjTime, pattern);
					
				       
					System.out.println("网络的日期时间" + ld);
					Message msg = Message.obtain();
					msg.what = TIME;
					msg.obj = ld;
					handlerTime.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}// 取得资源对象
			};
		}.start();
	}
	
	 // string类型转换为date类型
 	// strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
 	// HH时mm分ss秒，
 	// strTime的时间格式必须要与formatType的时间格式相同
 	public static Date stringToDate(String strTime, String formatType)
 			throws ParseException {
 		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
 		Date date = null;
 		date = formatter.parse(strTime);
 		return date;
 	}
	
	// string类型转换为long类型
		 	// strTime要转换的String类型的时间
		 	// formatType时间格式
		 	// strTime的时间格式和formatType的时间格式必须相同
		 	public static long stringToLong(String strTime, String formatType)
		 			throws ParseException {
		 		Date date = stringToDate(strTime, formatType); // String类型转成date类型
		 		if (date == null) {
		 			return 0;
		 		} else {
		 			long currentTime = dateToLong(date); // date类型转成long类型
		 			return currentTime;
		 		}
		 	}
		 	
		 	 // date类型转换为long类型
		 	// date要转换的date类型的时间
		 	public static long dateToLong(Date date) {
		 		return date.getTime();
		 	}
	
	
	
	/**
	 * 改变button样式
	 */
	protected static void changeButton() {
		project_detail_info_residue_time.setText("投标未开始");
		project_detail_info_immediately.setClickable(false);
		project_detail_info_immediately.setBackgroundColor(Color.GRAY);
	}

	protected static String formatData(String date) {
		if (date.contains("　")) {
			String times[] = endTime.split("　");
			date = times[0] + " " + times[1];
		}
		if (date.length() == 15) {
			date = date + "0";
		}
		return date;
	}

	/**
	 * 日期转换为时间
	 * @param oldData
	 * @return
	 * @throws ParseException
	 */
	public static long DataToTime(String oldData) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time = oldData;
		Date date = format.parse(time);
		//System.out.print("Format To times:" + date.getTime());
		return date.getTime();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			flag = false;
			this.finish();
			//System.out.println("返回键按下了");
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
