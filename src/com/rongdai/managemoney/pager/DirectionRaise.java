package com.rongdai.managemoney.pager;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.R;
import com.rongdai.activity.personalcenter.CustomServiceActivity;
import com.rongdai.base.BaseApplication;
import com.rongdai.domain.ChinaUniteCarTrade;
import com.rongdai.domain.DrectionRaiseBean;
import com.rongdai.domain.ManagerMoneyGoods;
import com.rongdai.domain.RecommendProjectBean;
import com.rongdai.domain.RecommendProjectBean.RecommendProjectData;
import com.rongdai.domain.RongyingbaoPojectBean;
import com.rongdai.domain.RongyingbaoPojectBean.RongyingbaoPojectData;
import com.rongdai.domain.YiBangStore;
import com.rongdai.utils.Constants;
import com.rongdai.view.MyListView;
import com.rongdai.view.PullToRefreshView;
import com.rongdai.view.PullToRefreshView.OnFooterRefreshListener;
import com.rongdai.view.PullToRefreshView.OnHeaderRefreshListener;
import com.rongdai.view.RoundProgressBar;

public class DirectionRaise extends Activity implements OnItemClickListener,
		OnClickListener, OnHeaderRefreshListener, OnFooterRefreshListener {
	private ListView mListView;
	/**
	 * 返回键
	 */
	private ImageView backImageButton;
	/**
	 * 页面标题
	 */
	private TextView titleTextView;
	/**
	 * 联系客服
	 */
	private ImageView serviceImageButton;
	private FrameLayout fl;
	private View loadingPager;
	private View emptyPager;
	private View errorPager;
	private View listviewPager;
	private PullToRefreshView mPullToRefreshView;
	private int refreshState = 0;
	private int pagerNumber = 1;
	private Button errorButton;
	private String areaType;
	private ManagerMoneyGoods managerMoneyGoods;
	private RecommendProjectBean drectionRaiseBean;
	private ChinaUniteCarTrade chinaUniteCarTrade;
	private List<RecommendProjectData> tempBean;
	private List<RecommendProjectData> recommendProjectData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_direction_raise);
		loadingPager = View.inflate(this, R.layout.loading_page_loading, null);
		emptyPager = View.inflate(this, R.layout.loading_page_empty, null);
		errorPager = View.inflate(this, R.layout.loading_page_error, null);
		listviewPager = View.inflate(this, R.layout.direction_raise_listview,
				null);

		Intent intent = getIntent();
		areaType = intent.getStringExtra("areaType");

		fl = (FrameLayout) findViewById(R.id.dirction_fl);
		fl.addView(loadingPager);

		backImageButton = (ImageView) findViewById(R.id.ib_back);
		titleTextView = (TextView) findViewById(R.id.tv_title);
		serviceImageButton = (ImageView) findViewById(R.id.ib_service);

		errorButton = (Button) errorPager.findViewById(R.id.page_bt);

		mPullToRefreshView = (PullToRefreshView) listviewPager
				.findViewById(R.id.pull_refresh_view);
		mListView = (ListView) listviewPager
				.findViewById(R.id.direction_raise_lv);

		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);

		serviceImageButton.setVisibility(View.GONE);
		serviceImageButton.setOnClickListener(this);
		backImageButton.setOnClickListener(this);
		errorButton.setOnClickListener(this);

		mListView.setOnItemClickListener(this);
		getDataFromNet();
	}

	/**
	 * 访问网络获取数据
	 */
	/**
	 * 
	 */
	private void getDataFromNet() {
		//System.out.println("areaType6666666666666666" + areaType);
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		httpUtils.configTimeout(3000);

		if (refreshState == 1 || refreshState == 0) {
			pagerNumber = 1;
		} else if (refreshState == 2) {
			++pagerNumber;
		}

		// 需要修改参数
		//params.addBodyParameter("areaType", areaType);
		params.addBodyParameter("pageNo", pagerNumber + "");
		httpUtils.send(HttpMethod.POST, Constants.getRongyingbaoPoject, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						fl.removeAllViews();
						fl.addView(errorPager);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						processInfo(responseInfo.result);
					}
				});
	}

	/**
	 * 解析网络数据
	 * 
	 * @param json
	 */
	protected void processInfo(String json) {
		//System.out.println(json);
		Gson gson = new Gson();
		try {
			if ("0".equals(areaType)) {
				managerMoneyGoods = gson
						.fromJson(json, ManagerMoneyGoods.class);
				//recommendProjectData = managerMoneyGoods.licaichanping;
				titleTextView.setText("理财产品");
			} else if ("1".equals(areaType)) {
				drectionRaiseBean = gson
						.fromJson(json, RecommendProjectBean.class);
				titleTextView.setText("融影宝");
				recommendProjectData = drectionRaiseBean.data;
			} else if ("2".equals(areaType)) {
				chinaUniteCarTrade = gson.fromJson(json,
						ChinaUniteCarTrade.class);
				titleTextView.setText("中联汽贸");
				//recommendProjectData = chinaUniteCarTrade.zhongliangqimao;
			} else if ("0".equals(areaType)) {
				YiBangStore yiBangStore = gson
						.fromJson(json, YiBangStore.class);
				titleTextView.setText("一邦商城");
				//recommendProjectData = yiBangStore.yibangshangcheng;
			}
			if (refreshState == 0) {
				if (recommendProjectData == null
						|| recommendProjectData.size() == 0) {
					fl.removeAllViews();
					fl.addView(emptyPager);
					return;
				} else {
					tempBean = recommendProjectData;
				}
			} else if (refreshState == 1) {
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy年MM月dd日    HH:mm:ss     ");
				Date date = new Date(System.currentTimeMillis());
				String time = format.format(date);
				mPullToRefreshView.onHeaderRefreshComplete(time);
				refreshState = 0;
				if (recommendProjectData == null
						|| recommendProjectData.size() == 0) {
					fl.removeAllViews();
					fl.addView(emptyPager);
					return;
				} else {
					tempBean = recommendProjectData;
				}
				Toast.makeText(BaseApplication.getContext(), "刷新成功", 0).show();
			} else if (refreshState == 2) {
				mPullToRefreshView.onFooterRefreshComplete();
				refreshState = 0;
				if (recommendProjectData == null
						|| recommendProjectData.size() == 0) {
					--pagerNumber;
					Toast.makeText(BaseApplication.getContext(), "没有更多数据了", 0)
							.show();
				} else {
					tempBean.addAll(recommendProjectData);
				}
			}

			fl.removeAllViews();
			fl.addView(listviewPager);
			DirectionRaiseAdapter adapter = new DirectionRaiseAdapter();
			mListView.setAdapter(adapter);
		} catch (Exception e) {
			fl.removeAllViews();
			fl.addView(errorPager);
		}
		// gson.fromJson(json, classOfT)
	}

	class DirectionRaiseAdapter extends BaseAdapter {

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

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return tempBean.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = View.inflate(BaseApplication.getContext(),
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
			moneyTextView = (TextView) view.findViewById(R.id.recommend_money);
			timeTextView = (TextView) view.findViewById(R.id.recommend_time);
			harvestTextView = (TextView) view
					.findViewById(R.id.recommend_harvest);

			mRoundProgressBar = (RoundProgressBar) view
					.findViewById(R.id.recommend_rpb);

			titleTextView.setText(tempBean.get(position).borrowName);
			moneyTextView.setText(tempBean.get(position).borrowAccount);
			timeTextView.setText(tempBean.get(position).borrowLimit);
			harvestTextView.setText(tempBean.get(position).borrowYearRate);

			mRoundProgressBar.setMax((int) Double.parseDouble(tempBean
					.get(position).borrowAccount));
			mRoundProgressBar.setProgress((int) Double.parseDouble(tempBean
					.get(position).borrowHadRongZi));

			if ("企业经营贷".equals(tempBean.get(position).borrowType)) {
				companyTextView.setVisibility(View.VISIBLE);
				consumeTextView.setVisibility(View.GONE);
				dingxiangTextView.setVisibility(View.GONE);
				timingTextView.setVisibility(View.GONE);
			}
			if ("消费周转贷".equals(tempBean.get(position).borrowType)) {
				companyTextView.setVisibility(View.GONE);
				consumeTextView.setVisibility(View.VISIBLE);
				dingxiangTextView.setVisibility(View.GONE);
				timingTextView.setVisibility(View.GONE);
			}
			if ("定向标".equals(tempBean.get(position).borrowType)) {
				companyTextView.setVisibility(View.GONE);
				consumeTextView.setVisibility(View.GONE);
				dingxiangTextView.setVisibility(View.VISIBLE);
				timingTextView.setVisibility(View.GONE);
			}
			if ("定时标".equals(tempBean.get(position).borrowType)) {
				companyTextView.setVisibility(View.GONE);
				consumeTextView.setVisibility(View.GONE);
				dingxiangTextView.setVisibility(View.GONE);
				timingTextView.setVisibility(View.VISIBLE);
			}
			return view;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, ProjectDetail.class);
		intent.putExtra("bean", tempBean.get(position));
		this.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			this.finish();
			break;
		case R.id.ib_service:
//			Intent intent = new Intent(this,CustomServiceActivity.class);
//			startActivity(intent);
			break;
		case R.id.page_bt:
			getDataFromNet();
			break;

		default:
			break;
		}
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		refreshState = 1;
		getDataFromNet();
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		refreshState = 2;
		getDataFromNet();
	}

}
