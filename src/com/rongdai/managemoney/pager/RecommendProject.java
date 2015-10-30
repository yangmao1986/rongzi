package com.rongdai.managemoney.pager;

import java.net.URL;
import java.net.URLConnection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.rongdai.MainActivity;
import com.rongdai.R;
import com.rongdai.activity.NoticConsult.ShowImage;
import com.rongdai.base.BaseApplication;
import com.rongdai.constants.ConfigConstants;
import com.rongdai.domain.MinTime;
import com.rongdai.domain.RecommendProjectBean;
import com.rongdai.domain.RecommendProjectBean.RecommendProjectData;
import com.rongdai.domain.TopImage;
import com.rongdai.utils.ConfigFileUtils;
import com.rongdai.utils.Constants;
import com.rongdai.utils.GetNetTime;
import com.rongdai.utils.ImageOptionsUtils;
import com.rongdai.utils.NetWorkAvaiable;
import com.rongdai.utils.TimeUtils;
import com.rongdai.view.MyListView;
import com.rongdai.view.MyViewPager;
import com.rongdai.view.PullToRefreshView;
import com.rongdai.view.RoundProgressBar;

public class RecommendProject implements OnItemClickListener, OnClickListener,
		OnPageChangeListener {
	private int item = 3;
	private int count = 1;
	private static boolean falg = false;
	private static boolean contentFalg = false;
	private static long time = 0;
	private Timer timer = new Timer();
	public  static  TimerTask task;
	private Timer timerTwo = new Timer();
	public  TimerTask taskTwo;
	private View RootView;
	private View loadingPage;
	private View emptyPage;
	private View errorPage;
	private View listViewPage;
	private FrameLayout fl;
	private Button errorButton;
	private Context mContext;
	private MyViewPager mViewPager;
	private List<ImageView> imageViewList;
	private int pagerNumber = 0;
	private int refreshState = 0;
	private int HEADER = 1;
	private PullToRefreshView mPullToRefreshView;
	private int cacheflags = 0;
	private static int currentPage = 0;
	/**
	 * 下次开标时间
	 */
	private static TextView lastTimeTextView;
	/**
	 * 到时提醒
	 */
	private Button notifyButton;
	/***
	 * 展示开标的ListView
	 */
	private MyListView mListView;
	private List<RecommendProjectData> borrowData;
	private List<RecommendProjectData> tempBorrowData;
	private RecommendProjectBean recommendProjectBean;
	private TopImage topImage;
	private RecommendBaseAdapter adapter;

	public RecommendProject(Context mContext) {
		this.mContext = mContext;
		imageViewList = new ArrayList<ImageView>();
		initView(mContext);
	}

	public void initView(Context context) {
				mImageLoader = ImageLoader.getInstance();
				mImageLoader.init(ImageLoaderConfiguration
						.createDefault(BaseApplication.getContext()));
				options = ImageOptionsUtils.getOptions();
				WindowManager wm = (WindowManager) BaseApplication.getContext().getSystemService(BaseApplication.getContext().WINDOW_SERVICE);
				int width = wm.getDefaultDisplay().getWidth();
				int height = wm.getDefaultDisplay().getHeight();
				RootView = View.inflate(BaseApplication.getContext(),
						R.layout.manage_money_recommend_project, null);
				loadingPage = View.inflate(BaseApplication.getContext(),
						R.layout.loading_page_loading, null);
				errorPage = View.inflate(BaseApplication.getContext(),
						R.layout.loading_page_error, null);
				emptyPage = View.inflate(BaseApplication.getContext(),
						R.layout.loading_page_empty, null);
				listViewPage = View.inflate(BaseApplication.getContext(),
						R.layout.manager_money_center_listview, null);
				LayoutParams params = new LayoutParams(width, (int)(height*0.762));
				loadingPage.setLayoutParams(params);
				errorPage.setLayoutParams(params);
				emptyPage.setLayoutParams(params);
				mListView = (MyListView) listViewPage
						.findViewById(R.id.manager_money_lv);
				errorButton = (Button) errorPage.findViewById(R.id.page_bt);
				fl = (FrameLayout) RootView.findViewById(R.id.manage_money_center_fl);
	}

	/**
	 * 初始化数据
	 */
	public void initData() {
		errorButton.setOnClickListener(this);
		getMinTimeFromNet();
		String imageJson = ConfigFileUtils.getValue(ConfigConstants.PAGER,
				ConfigConstants.IMAGEVIEWPAGER, "");
		if(falg== false){
			if(NetWorkAvaiable.isNetWorkAvaiable(BaseApplication.getContext())){
				getImageDateFromNet();
				falg = true;
			}else{
				if ("".equals(imageJson)) {
					getImageDateFromNet();
					falg = true;
				} else {
					processImageData(imageJson);
				}
			}
		}else{
			if ("".equals(imageJson)) {
				getImageDateFromNet();
				falg = true;
			} else {
				processImageData(imageJson);
			}
		}
	}

	/**
	 * 获取最小时间 
	 */
	private void getMinTimeFromNet() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST,Constants.getMinTime, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(BaseApplication.getContext(),"网络异常获取倒计时失败", 0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
					//System.out.println("最小的时间是"+responseInfo.result);
					processMinTime(responseInfo.result);
			}
		});	
	}

	/**
	 * 解析最小的时间
	 */
	protected void processMinTime(String json) {
		Gson gson = new  Gson();
		MinTime minTime = gson.fromJson(json, MinTime.class);
		minStr = minTime.minTime;
		if("".equals(minStr)){
			return ;
		}
		replaceStr = minStr.replace("　", " ");
		if(replaceStr.trim().length() == 15){
			replaceStr = replaceStr+"0";
		}
		try {
			min = GetNetTime.DataToTime(replaceStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		minData = GetNetTime.StrToData(min+"");
		try {
			getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从网络获取轮播图
	 */
	private void getImageDateFromNet() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.POST, Constants.picChange,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(BaseApplication.getContext(), "网络异常", 0)
								.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						processImageData(responseInfo.result);
					}
				});
	}

	/**
	 * 解析网络获取的json字符串
	 * 
	 * @param result
	 *            json字符串
	 */
	protected void processImageData(String result) {
		Gson gson = new Gson();
		try {
			topImage = gson.fromJson(result, TopImage.class);
			if (topImage.data.size() == 0) {
				
				Toast.makeText(BaseApplication.getContext(), "没有轮播图", 0).show();
			} else {
				ConfigFileUtils.setValue(ConfigConstants.PAGER,
						ConfigConstants.IMAGEVIEWPAGER, result);
			}

		} catch (Exception e) {
			return;
		}
		
		/** 判断配置文件中是否有缓存，如果有直接用，没有再去访问网络 */
		String json = ConfigFileUtils.getValue(ConfigConstants.PAGER,
				ConfigConstants.RECOMMENDPAGER, "");
		if(contentFalg==false){
			
			if(NetWorkAvaiable.isNetWorkAvaiable(BaseApplication.getContext())){
				fl.addView(loadingPage);
				getDataFromNet(refreshState,
						new PullToRefreshView(BaseApplication.getContext()));
				contentFalg = true;
			}else{
				if ("".equals(json)) {
					fl.removeAllViews();
					fl.addView(loadingPage);
					cacheflags = 0;
					getDataFromNet(refreshState,
							new PullToRefreshView(BaseApplication.getContext()));
					contentFalg = true;
				} else {
					cacheflags = 1;
					processData(json);
				}
			}
		}else{
			if ("".equals(json)) {
				fl.removeAllViews();
				fl.addView(loadingPage);
				cacheflags = 0;
				getDataFromNet(refreshState,
						new PullToRefreshView(BaseApplication.getContext()));
			} else {
				cacheflags = 1;
				processData(json);
			}
		}
	}

	/**
	 * 从网络获取数据
	 */
	public void getDataFromNet(final int refreshState,
			final PullToRefreshView mPullToRefreshView) {
		this.refreshState = refreshState;
		this.mPullToRefreshView = mPullToRefreshView;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		if (refreshState == HEADER || refreshState == 0) {
			pagerNumber = 1;
		} else {
			if (cacheflags == 1) {
				pagerNumber = 1;
			} else {
				++pagerNumber;
			}
		}
		params.addQueryStringParameter("pageNo", pagerNumber + "");
		httpUtils.send(HttpMethod.GET, Constants.getRecommendPoject, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						if (refreshState == 1) {
							mPullToRefreshView.onHeaderRefreshComplete();
						} else if (refreshState == 2) {
							mPullToRefreshView.onFooterRefreshComplete();
						}
						fl.removeAllViews();
						fl.addView(errorPage);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						System.out.println(responseInfo.result);
						processData(responseInfo.result);
					}
				});
	}

	/**
	 * 解析网络数据
	 * 
	 * @param json
	 *            json字符串
	 */
	protected void processData(String json) {
		Gson gson = new Gson();
		try {
			recommendProjectBean = gson.fromJson(json,
					RecommendProjectBean.class);
			borrowData = recommendProjectBean.data;
			if (refreshState == 0) {
				isRepleceData(json);
			} else if (this.refreshState == 1) {
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy年MM月dd日    HH:mm:ss     ");
				Date date = new Date(System.currentTimeMillis());
				String time = format.format(date);
				mPullToRefreshView.onHeaderRefreshComplete(time);
				refreshState = 0;
				isRepleceData(json);
				Toast.makeText(BaseApplication.getContext(), "刷新成功",0).show();
			} else if (refreshState == 2) {
				mPullToRefreshView.onFooterRefreshComplete();
				refreshState = 0;
				if (borrowData == null || borrowData.size() == 0) {
					--pagerNumber;
					Toast.makeText(BaseApplication.getContext(), "没有更多数据了...", 0).show();
					return ;
				} else if (cacheflags == 1) {
					isRepleceData(json);
					cacheflags = 0;
				} else {
					tempBorrowData.addAll(borrowData);
				}
			}
			if(task!=null){
				task.cancel();
				task = null;
			}
			fl.removeAllViews();
			fl.addView(listViewPage);
			showListView();

		} catch (Exception e) {
			fl.removeAllViews();
			fl.addView(errorPage);
		}

	}

	private void isRepleceData(String json) {
		if (borrowData.size() == 0) {
			fl.removeAllViews();
			fl.addView(emptyPage);
		} else {
			tempBorrowData = borrowData;
			/** 数据存储在配置文件中 */
			ConfigFileUtils.setValue(ConfigConstants.PAGER,
					ConfigConstants.RECOMMENDPAGER, json);
		}
	}
	/**
	 * 展示ListView
	 */
	private void showListView() {
		mListView.setOnItemClickListener(this);
		if (adapter == null) {
			adapter = new RecommendBaseAdapter();
			mListView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		if(!"".equals(minStr)){
			if(adapter != null){
				showTime();
			}
		}
	}

	/**
	 * ListView的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class RecommendBaseAdapter extends BaseAdapter {

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
		private View view;

		@Override
		public int getCount() {
			return tempBorrowData.size() + 1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public int getItemViewType(int position) {
			if (position == 0) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			int type = getItemViewType(position);
			if (type == 0) {
				if (convertView == null) {
					view = View.inflate(mContext,
							R.layout.recommend_project_header, null);
				} else {
					view = convertView;
				}
				mViewPager = (MyViewPager) view
						.findViewById(R.id.manager_money_vp);
				showViewPager();
				lastTimeTextView = (TextView) view
						.findViewById(R.id.manager_money_show_time);
				notifyButton = (Button) view
						.findViewById(R.id.manager_money_notify_time);
				if(Double.parseDouble(borrowData.get(0).borrowHadRongZi)%Double.parseDouble(borrowData.get(0).borrowAccount)==1){
				if("".equals(minStr)){
					lastTimeTextView.setText("暂时没有开标项目");
					notifyButton.setTextColor(Color.rgb(153, 153, 153));
					notifyButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Toast.makeText(BaseApplication.getContext(), "暂时没有开标项目", 0)
							.show();
						}
					});
				}else{
					notifyButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							requestDataToNet();
							Toast.makeText(BaseApplication.getContext(), "已提醒", 0)
							.show();
						}
					});
				}
				}else{
					
					lastTimeTextView.setText("有开标项目");
                    notifyButton.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Toast.makeText(BaseApplication.getContext(), "有开标项目", 0)
							.show();
						}
					});
					
				}
				

			} else {
				if (convertView == null) {
					view = View.inflate(mContext,
							R.layout.manage_money_recommend_item, null);
				} else {
					view = convertView;
				}
				companyTextView = (TextView) view
						.findViewById(R.id.recommend_company);
				consumeTextView = (TextView) view
						.findViewById(R.id.recommend_consume);
				dingxiangTextView = (TextView) view
						.findViewById(R.id.recommend_dingxiang);
				timingTextView = (TextView) view
						.findViewById(R.id.recommend_timing);

				titleTextView = (TextView) view
						.findViewById(R.id.recommend_item_loan_title);
				moneyTextView = (TextView) view
						.findViewById(R.id.recommend_money);
				timeTextView = (TextView) view
						.findViewById(R.id.recommend_time);
				harvestTextView = (TextView) view
						.findViewById(R.id.recommend_harvest);

				mRoundProgressBar = (RoundProgressBar) view
						.findViewById(R.id.recommend_rpb);

				mRoundProgressBar.setMax(Integer.parseInt(tempBorrowData
						.get(position - 1).borrowAccount)*10000);

				if (tempBorrowData.get(position - 1).borrowHadRongZi == null
						|| "".equals(tempBorrowData.get(position - 1).borrowHadRongZi)) {
					mRoundProgressBar.setProgress(0);
				} else {
					mRoundProgressBar
							.setProgress((int) Double
									.parseDouble(tempBorrowData
											.get(position - 1).borrowHadRongZi));
				}
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,
								ProjectDetail.class);
						intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//						intent.putExtra("bean",recommendProjectBean.data.get(position - 1));
						intent.putExtra("bean",tempBorrowData.get(position - 1));
						mContext.startActivity(intent);
					}
				});
				titleTextView
						.setText(tempBorrowData.get(position - 1).borrowName);
				moneyTextView
						.setText(tempBorrowData.get(position - 1).borrowAccount
								+ "万元");
				timeTextView
						.setText(tempBorrowData.get(position - 1).borrowLimit
								+ "个月");
				harvestTextView
						.setText(tempBorrowData.get(position - 1).borrowYearRate);
				companyTextView.setVisibility(View.GONE);
				consumeTextView.setVisibility(View.GONE);
				dingxiangTextView.setVisibility(View.GONE);
				timingTextView.setVisibility(View.GONE);
				
				if ("融影宝".equals(tempBorrowData.get(position - 1).borrowType)) {
					companyTextView.setVisibility(View.VISIBLE);
					companyTextView.setText("融影宝");
					timingTextView.setVisibility(View.VISIBLE);
				}else{
					if ("企业经营贷".equals(tempBorrowData.get(position - 1).borrowType)) {
						companyTextView.setText("企业经营贷");
						companyTextView.setVisibility(View.VISIBLE);
						companyTextView.getText().toString().trim();
					}
					if ("消费周转贷".equals(tempBorrowData.get(position - 1).borrowType)) {
						consumeTextView.setVisibility(View.VISIBLE);
					}
//					if ("定向标".equals(tempBorrowData.get(position - 1).borrowType)) {
//						dingxiangTextView.setVisibility(View.VISIBLE);
//					}
					if ("定时标".equals(tempBorrowData.get(position - 1).borrowType)) {
						timingTextView.setVisibility(View.VISIBLE);
					}
				}
				
				
			}
			return view;
		}
	}

	public View getRootView() {
		return RootView;
	}

	public void showTime() {
//		time = 60;
		task = new TimerTask() {
			@Override
			public void run() {
				(MainActivity.mainActivity).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (time <= 0) {
							if(lastTimeTextView!=null){
								lastTimeTextView.setVisibility(View.INVISIBLE);
							}
							task.cancel();
							return ;
						} else {
							if(lastTimeTextView!=null){
								lastTimeTextView.setText(TimeUtils.getFomatTime(time));
							}
							adapter.notifyDataSetChanged();
						}
						time--;
					}
				});
			}
		};
		timer.schedule(task, 0, 1000);
	}
	
	private static Handler handlerTime = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2:
				long currentTime = (Long) msg.obj;
				if(min!=null&&!"".equals(min)){
					time = (min-currentTime )/1000;
				}
				break;

			default:
				break;
			}
		};
	};
	private void showViewPager() {
		if(pagerAdapter == null){
			pagerAdapter = new MyPagerAdapter();
			mViewPager.setAdapter(pagerAdapter);
			showViewPageToTime();
		}
		mViewPager.setOnPageChangeListener(this);
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return topImage.data.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			ImageView topImageView = new ImageView(BaseApplication.getContext());
			topImageView.setScaleType(ScaleType.FIT_XY);
			container.addView(topImageView);
			try {
				mImageLoader.displayImage(topImage.data.get(position).phUrl, topImageView, options);
			} catch (Exception e) {
				e.printStackTrace();
				mImageLoader.cancelDisplayTask(topImageView);
			}
			
			topImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,ShowImage.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("newsId", topImage.data.get(position).newsId);
					mContext.startActivity(intent);
				}
			});
			return topImageView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
	}

	public void showViewPageToTime() {
		taskTwo = new TimerTask() {
			@Override
			public void run() {
				(MainActivity.mainActivity).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						int currentItem = mViewPager.getCurrentItem();
						if(currentItem >= 4){
							currentItem = -1;
						}
						mViewPager.setCurrentItem(currentItem+1);
						adapter.notifyDataSetChanged();
					}
				});
			}
		};
		
		timerTwo.schedule(taskTwo,5000, 5000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_bt:
			fl.removeAllViews();
			fl.addView(loadingPage);
			getDataFromNet(refreshState,
					new PullToRefreshView(BaseApplication.getContext()));
			break;

		default:
			break;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		currentPage = arg0;

	}

	/**
	 * 到时提醒
	 */
	protected void requestDataToNet() {
		String Tag = ((TelephonyManager) BaseApplication.getContext()
				.getSystemService(
						BaseApplication.getContext().TELEPHONY_SERVICE))
				.getDeviceId();
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("endTime", minData);
		params.addBodyParameter("alias", Tag);
		httpUtils.send(HttpMethod.POST, Constants.sendMessage,params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				Toast.makeText(BaseApplication.getContext(), "网络异常",0).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//System.out.println(responseInfo.result);
				Toast.makeText(BaseApplication.getContext(), "到时提醒", 0)
				.show();
			}
			
		});
	}
	
	private static final int MSG_SET_ALIAS = 1001;
	private static final int MSG_SET_TAGS = 1002;
	private static final String TAG = "JPush";
	private static Long min;
	private Long max;
	private static String minData;
	private String replaceStr;
	private static String userTime;
	private static long endTime;
	private static int TIME = 2;
	private DisplayImageOptions options;
	private ImageLoader mImageLoader;
	private static String minStr;
	private MyPagerAdapter pagerAdapter;
	
	
	/**
	 * 获取网络时间
	 * @throws Exception
	 */
	public static void getTime() throws Exception {
		new Thread(){
			public void run() {
				
				URL url;
				try {
					url = new URL("http://www.bjtime.cn");
					
					URLConnection uc = url.openConnection();// 生成连接对象
					
					uc.connect(); // 发出连接
					
					long ld = uc.getDate(); // 取得网站日期时间
					
					Message msg = Message.obtain();
					msg.what = TIME ;
					msg.obj = ld;
					handlerTime.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}// 取得资源对象
			};
		}.start();
	}
}
