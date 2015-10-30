package com.rongdai.pager;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.rongdai.base.BaseApplication;
import com.rongdai.base.BasePager;
import com.rongdai.constants.ConfigConstants;
import com.rongdai.domain.loanProjectList;
import com.rongdai.domain.loanProjectList.loanProjectListData;
import com.rongdai.iwantloan.pager.WriteLoanInfo;
import com.rongdai.utils.ConfigFileUtils;
import com.rongdai.utils.Constants;
import com.rongdai.view.MyListView;
import com.rongdai.view.PullToRefreshView;
import com.rongdai.view.PullToRefreshView.OnFooterRefreshListener;
import com.rongdai.view.PullToRefreshView.OnHeaderRefreshListener;

public class IWantLoan extends BasePager implements OnFooterRefreshListener, OnHeaderRefreshListener, OnClickListener {
	private View view;
	private Context mContext;
	private View loadingPage;
	private View emptyPage;
	private View errorPage;
	private View listViewPage;
	private FrameLayout fl;
	private Button errorButton;
	private int refreshState = 0;
	private int pagerNumber = 0;
	private int cacheFlags = 0;
	private static boolean falg = false;
	/**
	 * 我要贷款页面listview
	 */
	private ListView mListView;
	private loanProjectList listInfo;
	private PullToRefreshView mPullToRefreshView;
	private List<loanProjectListData> listData;
	private List<loanProjectListData> tempListData;
	private myAdapter adapter;

	public IWantLoan(Context mContext) {
		super(mContext);
		this.mContext = mContext;
	}

	@Override
	public View initView(Context context) {
		view = View.inflate(BaseApplication.getContext(),
				R.layout.manage_money_recommend_project, null);
		fl = (FrameLayout) view.findViewById(R.id.manage_money_center_fl);
		loadingPage = View.inflate(BaseApplication.getContext(),
				R.layout.loading_page_loading, null);
		errorPage = View.inflate(BaseApplication.getContext(),
				R.layout.loading_page_error, null);
		emptyPage = View.inflate(BaseApplication.getContext(),
				R.layout.loading_page_empty, null);
		listViewPage = View.inflate(BaseApplication.getContext(),
				R.layout.i_want_loan_listview, null);
		mListView = (ListView) listViewPage
				.findViewById(R.id.want_loan_lv);
		mPullToRefreshView = (PullToRefreshView) listViewPage.findViewById(R.id.pull_refresh_view);
		errorButton = (Button) errorPage.findViewById(R.id.page_bt);
		errorButton.setOnClickListener(this);
		return view;
	}

	@Override
	public void initData() {
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		/**判断配置文件中是否有缓存，如果有直接用，没有再去访问网络*/
		if(falg == false){
			fl.addView(loadingPage);
			getDataFromNet();
			falg = true;
		}else{
			String json = ConfigFileUtils.getValue(ConfigConstants.PAGER,
				ConfigConstants.IWANTLOAN, "");
			if ("".equals(json)) {
				fl.removeAllViews();
				fl.addView(loadingPage);
				getDataFromNet();
				cacheFlags = 0;
				falg = true;
			} else {
				cacheFlags = 1;
				processData(json);
			}
		}
	}

	/**
	 * 从网络获取我要贷款页面数据
	 */
	public void getDataFromNet() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		if(refreshState==1||refreshState==0){
			pagerNumber=1;
		}else{
			if(cacheFlags==1){
				pagerNumber = 1;
			}else{
				++pagerNumber;
				//System.out.println("pagerNumber ====== "+pagerNumber);
			}
		}
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("pageNo", pagerNumber+"");
		httpUtils.send(HttpMethod.GET, Constants.loanProjectList, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						fl.removeAllViews();
						fl.addView(errorPage);
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						//System.out.println(responseInfo.result);
						processData(responseInfo.result);
					}
				});
	}

	/**
	 * 解析我要贷款页面数据
	 * 
	 * @param json
	 *            json字符串
	 */
	protected void processData(String json) {
		Gson gson = new Gson();
		listInfo = gson.fromJson(json, loanProjectList.class);
		listData = listInfo.data;
		try {
			if(refreshState==0){
				isRepleceData(json);
			}else if(refreshState==1){
				SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
				Date date = new Date(System.currentTimeMillis());
				String time = format.format(date);
				mPullToRefreshView.onHeaderRefreshComplete(time);
				refreshState = 0;
				isRepleceData(json);
				Toast.makeText(BaseApplication.getContext(),"刷新成功", 0).show();
				
			}else if(refreshState == 2){
				mPullToRefreshView.onFooterRefreshComplete();
				refreshState = 0;
				if(cacheFlags==1){
					isRepleceData(json);
					cacheFlags = 0;
				}else if(listData.size()==0){
					--pagerNumber;
					Toast.makeText(BaseApplication.getContext(),"没有更多加载了", 0).show();
				}else{
					tempListData.addAll(listData);
				}
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
		if(listData.size()==0){
			fl.removeAllViews();
			fl.addView(emptyPage);
		}else{
			tempListData = listData;
			/**数据存储在配置文件中*/
			ConfigFileUtils.setValue(ConfigConstants.PAGER,
					ConfigConstants.IWANTLOAN, json);
		}
	}

	/**
	 * 展示listView
	 */
	private void showListView() {
		if(adapter==null){
			adapter = new myAdapter();
			mListView.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * listView的适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class myAdapter extends BaseAdapter {

		/**
		 * 立即申请
		 * */
		private Button mButton;
		/**
		 * 贷款项目名称
		 */
		private TextView loanTitleTextView;
		/**
		 * 适用区域
		 */
		private TextView localTextView;
		/**
		 * 借款期限
		 */
		private TextView timeTextView;
		/**
		 * 放款速度
		 */
		private TextView speedTextView;
		/**
		 * 借款成本
		 */
		private TextView costingTextView;

		@Override
		public int getCount() {
			return tempListData.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = View.inflate(mContext, R.layout.i_want_loan_item, null);
			} else {
				view = convertView;
			}

			loanTitleTextView = (TextView) view
					.findViewById(R.id.want_loan_item_title);
			localTextView = (TextView) view
					.findViewById(R.id.want_loan_item_local);
			timeTextView = (TextView) view
					.findViewById(R.id.want_loan_item_time);
			speedTextView = (TextView) view
					.findViewById(R.id.want_loan_item_speed);
			costingTextView = (TextView) view
					.findViewById(R.id.want_loan_item_costing);
			mButton = (Button) view.findViewById(R.id.immeiately_apply);

			loanTitleTextView.setText(tempListData.get(position).loanName);
			localTextView.setText(tempListData.get(position).loantArea);
			timeTextView.setText(tempListData.get(position).loanDate);
			speedTextView.setText(tempListData.get(position).loanCollSpeed);
			costingTextView.setText(tempListData.get(position).loanCost);

			mButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, WriteLoanInfo.class);
					intent.putExtra("loanTypeId",
							tempListData.get(position).loanTypeId);
					intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
					mContext.startActivity(intent);
					//System.out.println(position + "-----------"+ tempListData.get(position).loanTypeId);
				}
			});
			return view;
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.page_bt:
			fl.removeAllViews();
			fl.addView(loadingPage);
			getDataFromNet();
			break;

		default:
			break;
		}
	}
}
