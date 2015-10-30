package com.rongdai.managemoney.pager;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

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
import com.rongdai.base.BasePager;
import com.rongdai.constants.ConfigConstants;
import com.rongdai.domain.OtherProjectBean;
import com.rongdai.domain.RecommendProjectBean;
import com.rongdai.domain.TopImage;
import com.rongdai.domain.RecommendProjectBean.RecommendProjectData;
import com.rongdai.utils.ConfigFileUtils;
import com.rongdai.utils.Constants;
import com.rongdai.utils.ImageOptionsUtils;
import com.rongdai.utils.NetWorkAvaiable;
import com.rongdai.view.MyListView;
import com.rongdai.view.PullToRefreshView;
import com.rongdai.view.RoundProgressBar;

public class OtherProject extends BasePager implements 
		OnPageChangeListener, OnItemClickListener, OnClickListener {
	private Timer timer = new Timer();
	public  TimerTask task;
	private static boolean falg = false;
	private static boolean contentFalg = false;
	private ViewPager mViewPager;
	private List<ImageView> imageViewList;
	private View view;
	private View loadingPage;
	private View emptyPage;
	private View errorPage;
	private View listViewPage;
	private FrameLayout fl;
	private Button errorButton;
	private int cacheflags = 0;
	private int pagerNumber = 0;
	private int pageNo = 1;
	private int HEADER = 1;
	private int refreshState = 0;
	private PullToRefreshView mPullToRefreshView;
	private List<String> listStr;;
	/**
	 * 显示其他项目的listview
	 */
	private MyListView mListView;
	private RecommendProjectBean tempRongyingbaoPojectBean;
	private List<RecommendProjectData> rongyingbaoPojectBean;
	private TopImage topImage;
	private OtherProjectAdapter adapter;
	private static int currentPage;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	private MyPagerAdapter pagerAdapter;

	public OtherProject(Context mContext) {
		super(mContext);
	}

	@Override
	public View initView(Context context) {
		listStr = new ArrayList<String>();
		imageViewList = new ArrayList<ImageView>();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration
				.createDefault(BaseApplication.getContext()));
		options = ImageOptionsUtils.getOptions();
		WindowManager wm = (WindowManager) BaseApplication.getContext().getSystemService(BaseApplication.getContext().WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		view = View.inflate(BaseApplication.getContext(),
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
		errorButton.setOnClickListener(this);
		fl = (FrameLayout) view.findViewById(R.id.manage_money_center_fl);
		/** 向集合中存放四张图片来让viewpager显示 */
		imageViewList.add(new ImageView(BaseApplication.getContext()));
		imageViewList.add(new ImageView(BaseApplication.getContext()));
		imageViewList.add(new ImageView(BaseApplication.getContext()));
		imageViewList.add(new ImageView(BaseApplication.getContext()));
		rongyingbaoPojectBean = new ArrayList<RecommendProjectBean.RecommendProjectData>();
		return view;
	}

	@Override
	public void initData() {
		String imageJson = ConfigFileUtils.getValue(ConfigConstants.PAGER,
				ConfigConstants.IMAGEVIEWPAGER, "");
		if(falg== false){
			if(NetWorkAvaiable.isNetWorkAvaiable(BaseApplication.getContext())){
				processImageData(imageJson);
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
	 * 访问网络获取ImageJson的数据
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
	 * 解析ImageJson字符串
	 * 
	 * @param imageJson
	 */
	private void processImageData(String imageJson) {
		Gson gson = new Gson();
		try {
			topImage = gson.fromJson(imageJson, TopImage.class);
			if (topImage.data.size() == 0) {
				Toast.makeText(BaseApplication.getContext(), "没有轮播图", 0).show();
			} else {
				ConfigFileUtils.setValue(ConfigConstants.PAGER,
						ConfigConstants.IMAGEVIEWPAGER, imageJson);
			}

		} catch (Exception e) {
			return;
		}

		/** 查看是否有缓存，如果有直接用缓存，如果没有就访问网络 */
		String json = ConfigFileUtils.getValue(ConfigConstants.PAGER,
				ConfigConstants.OHTERPAGER, "");
		if(contentFalg == false){
			if(NetWorkAvaiable.isNetWorkAvaiable(BaseApplication.getContext())){
				fl.addView(loadingPage);
				getDataFromNet(refreshState,
						new PullToRefreshView(BaseApplication.getContext()));
				contentFalg = true;
				
			}else{
				if ("".equals(json)) {
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
	}

	/**
	 * 访问网络获取其他项目的数据
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
		params.addQueryStringParameter("pageNo",  pagerNumber + "");
		httpUtils.send(HttpMethod.GET, Constants.getRongyingbaoPoject,params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(mContext, "网络异常", 0).show();
						if (refreshState == HEADER) {
							mPullToRefreshView.onHeaderRefreshComplete();
						} else if (refreshState == 2) {
							mPullToRefreshView.onFooterRefreshComplete();
						}
						fl.removeAllViews();
						fl.addView(errorPage);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						processData(responseInfo.result);
						Log.e("rongyingbao", responseInfo.result);
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
//			listStr.clear();
			Log.e("解析的json", json);
			tempRongyingbaoPojectBean = gson.fromJson(json, RecommendProjectBean.class);
			if (this.refreshState == 0) {
				rongyingbaoPojectBean = tempRongyingbaoPojectBean.data;
			}
			else if (this.refreshState == 1) {
					SimpleDateFormat format = new SimpleDateFormat(
							"yyyy年MM月dd日    HH:mm:ss     ");
					Date date = new Date(System.currentTimeMillis());
					String time = format.format(date);
					mPullToRefreshView.onHeaderRefreshComplete(time);
					refreshState = 0;
					Toast.makeText(BaseApplication.getContext(), "刷新成功", 0).show();
					rongyingbaoPojectBean = tempRongyingbaoPojectBean.data;
				} else if (refreshState == 2) {
					
					if (tempRongyingbaoPojectBean.data.size() == 0) {
						Toast.makeText(BaseApplication.getContext(), "没有更多数据了", 0).show();
						mPullToRefreshView.onFooterRefreshComplete();
						refreshState = 0;
						return ;
					} else{
						rongyingbaoPojectBean.addAll(tempRongyingbaoPojectBean.data);
					}
				}
			Log.e("解析的json对象个数", tempRongyingbaoPojectBean.data.size()+"");
			fl.removeAllViews();
			fl.addView(listViewPage);
			showList();
		} catch (Exception e) {
			Log.e("rongyingbao", "解析错误");
			fl.removeAllViews();
			fl.addView(errorPage);
		}
	}

	/**
	 * 展示ListView
	 */
	private void showList() {
		if (adapter == null) {
			adapter = new OtherProjectAdapter();
			mListView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		mListView.setOnItemClickListener(this);
	}

	class OtherProjectAdapter extends BaseAdapter {

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
		 * 用来点击跳转到详情中
		 */
		private RelativeLayout mRelativeLayout;
		/**
		 * 定向募集
		 */
		private TextView title;
		/**
		 * 其他工程只中的第一个显示的标
		 */
		private LinearLayout oneLinearLayout;

		@Override
		public int getCount() {
			return rongyingbaoPojectBean.size() + 2;
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
			} else if (position == 1) {
				return 1;
			}else if (position == 2) {
				return 2;
			}else if (position == 3) {
				return 3;
			}else{
				return 4;
			}
		}

		@Override
		public int getViewTypeCount() {
			return 5;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View otherView;

			int type = getItemViewType(position);
			if (type == 0) {
				if (convertView == null) {
					otherView = View.inflate(mContext,
							R.layout.other_project_header, null);
				} else {
					otherView = convertView;
				}
				mViewPager = (ViewPager) otherView
						.findViewById(R.id.other_project_vp);
				showViewPager();

			} else if(type == 1){
				if (convertView == null) {
					otherView = View.inflate(mContext,
							R.layout.manage_money_other_project_item, null);
				} else {
					otherView = convertView;
				}
				mRelativeLayout = (RelativeLayout) otherView
						.findViewById(R.id.other_project_rl);
			}
			else{
				if (convertView == null) {
					otherView = View.inflate(BaseApplication.getContext(),
							R.layout.manage_money_recommend_item, null);
				} else {
					otherView = convertView;
				}
				oneLinearLayout = (LinearLayout) otherView
						.findViewById(R.id.other_project_item_one);

				mRelativeLayout = (RelativeLayout) otherView
						.findViewById(R.id.other_project_rl);
				title = (TextView) otherView
						.findViewById(R.id.other_project_title);
				// 四组button
				companyTextView = (TextView) otherView
						.findViewById(R.id.recommend_company);
				consumeTextView = (TextView) otherView
						.findViewById(R.id.recommend_consume);
				dingxiangTextView = (TextView) otherView
						.findViewById(R.id.recommend_dingxiang);
				timingTextView = (TextView) otherView
						.findViewById(R.id.recommend_timing);
				// 四组数字
				titleTextView = (TextView) otherView
						.findViewById(R.id.recommend_item_loan_title);
				moneyTextView = (TextView) otherView
						.findViewById(R.id.recommend_money);
				timeTextView = (TextView) otherView
						.findViewById(R.id.recommend_time);
				harvestTextView = (TextView) otherView
						.findViewById(R.id.recommend_harvest);

				// 两个圆形进度条
				mRoundProgressBar = (RoundProgressBar) otherView
						.findViewById(R.id.recommend_rpb);
				companyTextView.setVisibility(View.GONE);
				consumeTextView.setVisibility(View.GONE);
				dingxiangTextView.setVisibility(View.GONE);
				timingTextView.setVisibility(View.GONE);
				
				titleTextView
				.setText(rongyingbaoPojectBean.get(position-2).borrowName);
		moneyTextView
				.setText(rongyingbaoPojectBean.get(position-2).borrowAccount);
		timeTextView
				.setText(rongyingbaoPojectBean.get(position-2).borrowLimit);
		harvestTextView.setText(rongyingbaoPojectBean.get(position-2).borrowYearRate);
		mRoundProgressBar
				.setProgress((int) Double
						.parseDouble(rongyingbaoPojectBean.get(position-2).borrowHadRongZi));
		mRoundProgressBar
				.setMax((int) Double
						.parseDouble(rongyingbaoPojectBean.get(position-2).borrowAccount)*10000);
		
			companyTextView.setVisibility(View.VISIBLE);
			companyTextView.setText("融影宝");
			timingTextView.setVisibility(View.VISIBLE);
			}
			return otherView;
		}
	}

	private void showViewPager() {
		if (pagerAdapter==null) {
			pagerAdapter = new MyPagerAdapter();
			showViewPageToTime();
			mViewPager.setAdapter(pagerAdapter);
		}
		mViewPager.setOnPageChangeListener(this);
	}

	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return topImage.data.size();
//			return 0;
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
			ImageView imageView = new ImageView(BaseApplication.getContext());
			imageView.setScaleType(ScaleType.FIT_XY);
			container.addView(imageView);
			
			mImageLoader.displayImage(topImage.data.get(position).phUrl,
					imageView, options);
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext,ShowImage.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("newsId", topImage.data.get(position).newsId);
					mContext.startActivity(intent);
				}
			});
			return imageView;
		}
	}

	@Override
	public void onClick(View v) {
		fl.removeAllViews();
		fl.addView(loadingPage);
		getDataFromNet(refreshState,
				new PullToRefreshView(BaseApplication.getContext()));
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		currentPage = arg0;
	}

	public void showViewPageToTime() {
		task = new TimerTask() {
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
		timer.schedule(task, 5000, 5000);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 1) {
			Intent intent = new Intent(BaseApplication.getContext(),
					DirectionRaise.class);
			intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("areaType", "1");
			BaseApplication.getContext().startActivity(intent);
		}else if(position > 1){
			Intent oneIntent = new Intent(BaseApplication.getContext(),
					ProjectDetail.class);
			oneIntent.setFlags(oneIntent.FLAG_ACTIVITY_NEW_TASK);
				oneIntent.putExtra("bean",
						rongyingbaoPojectBean.get(position - 2));
			BaseApplication.getContext().startActivity(oneIntent);
		}
	}
	
	
}
