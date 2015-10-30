package com.rongdai.managemoney.pager;

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
import com.rongdai.domain.RecommendProjectBean;
import com.rongdai.domain.TenderCaseBean;
import com.rongdai.domain.TenderCaseBean.TenderCaseData.TenderCaseDataInfo;
import com.rongdai.utils.ConfigFileUtils;
import com.rongdai.utils.Constants;
import com.rongdai.view.MyListView;
import com.rongdai.view.PullToRefreshView;

public class TenderCase extends BasePager {
	private View view;
	/**
	 * 还款模型信息显示
	 */
	private MyListView mListView;
	/**
	 * 投标时间
	 */
	private TextView titleTime;
	/**
	 * 金额
	 */
	private TextView titleMode;
	/**
	 * 用户
	 */
	private TextView moneyTime;
	/**
	 * 项目id
	 */
	private String projectId;
	/**
	 * 访问页面
	 */
	private TenderCaseBean tenderCaseBean;
	private List<TenderCaseDataInfo> listInfo;
	private int pagerNumber = 0;
	private int HEADER = 1;
	private int refreshState = 0;
	private PullToRefreshView mPullToRefreshView;
	private RepaymentModeAdapter adapter;
	
	public TenderCase(Context mContext) {
		super(mContext);
	}

	@Override
	public View initView(Context context) {
		view = View.inflate(context,
				R.layout.activity_project_detail_repayment_mode, null);
		View lineView = view.findViewById(R.id.project_detail_line);
		titleTime = (TextView) view.findViewById(R.id.repayment_mode_time);
		titleMode = (TextView) view.findViewById(R.id.repayment_mode);
		moneyTime = (TextView) view.findViewById(R.id.repayment_mode_money);
		
		mListView = (MyListView) view.findViewById(R.id.repayment_mode_lv);
		
		lineView.setBackgroundResource(R.drawable.fengexian_toubiaoqingkuang);
		titleTime.setText("投标时间");
		moneyTime.setText("用户");
		titleMode.setText("金额（元）");
		return view;
	}

	@Override
	public void initData() {
		String json = ConfigFileUtils.getValue(
				ConfigConstants.PROJECTDETAILPAGER,
				ConfigConstants.TENDERCASE, "");
		if ("".equals(json)) {
			getDataFromNet(refreshState,new PullToRefreshView(BaseApplication.getContext()));
		} else {
			processData(json);
		}
	}

	/**
	 * 访问网络获取投标情况数据
	 */
	public void getDataFromNet(int refreshState,PullToRefreshView mPullToRefreshView) {
		this.refreshState = refreshState;
		this.mPullToRefreshView = mPullToRefreshView;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		if(refreshState == HEADER){
			pagerNumber = 1;
		}else{
			pagerNumber = pagerNumber+1;
		}
		params.addQueryStringParameter("sort", "4");
		params.addQueryStringParameter("pageNo", pagerNumber+"");
		params.addQueryStringParameter("borrowId", getProjectId());
		httpUtils.send(HttpMethod.GET, Constants.pojectDetail, params,
				new RequestCallBack<String>() {
					/** 访问失败 */
					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					/** 访问成功 */
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						//System.out.println(responseInfo.result);
						ConfigFileUtils.setValue(
								ConfigConstants.PROJECTDETAILPAGER,
								ConfigConstants.TENDERCASE, responseInfo.result);
						processData(responseInfo.result);
					}
				});

	}

	/**
	 * 解析数据
	 */
	protected void processData(String json) {
		Gson gson = new Gson();
		tenderCaseBean = gson.fromJson(json, TenderCaseBean.class);
		try {
			if(this.refreshState==0){
				listInfo = tenderCaseBean.data.data2;
			}else if(this.refreshState==1){
				SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
				Date date = new Date(System.currentTimeMillis());
				String time = format.format(date);
				mPullToRefreshView.onHeaderRefreshComplete(time);
				refreshState = 0;
				listInfo = tenderCaseBean.data.data2;
				Toast.makeText(BaseApplication.getContext(), "刷新成功", 0).show();
			}else if(refreshState == 2){
				mPullToRefreshView.onFooterRefreshComplete();
				refreshState = 0;
				if(tenderCaseBean.data.data2.size()==0){
					Toast.makeText(BaseApplication.getContext(),"没有更多了",0).show();
				}else{
					listInfo.addAll(tenderCaseBean.data.data2);
				}
			}
			showListView();
			
		} catch (Exception e) {
			
		}
	}

	/**
	 * 展示listView
	 */
	private void showListView() {
		if(adapter == null){
			adapter = new RepaymentModeAdapter();
			mListView.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
	}

	
	
	class RepaymentModeAdapter extends BaseAdapter {
		/**
		 * 显示投标
		 */
		private TextView timeTextView;
		/**
		 * 显示金额
		 */
		private TextView modeTextView;
		/**
		 * 显示用户
		 */
		private TextView moneyTextView;
		private String userName;

		@Override
		public int getCount() {
			return listInfo.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = View.inflate(BaseApplication.getContext(),
						R.layout.activity_project_detail_repayment_mode_item,
						null);
			} else {
				view = convertView;
			}
			timeTextView = (TextView) view
					.findViewById(R.id.repayment_mode_item_time);
			modeTextView = (TextView) view
					.findViewById(R.id.repayment_mode_item);
			moneyTextView = (TextView) view
					.findViewById(R.id.repayment_mode_item_money);

			timeTextView.setText(listInfo.get(position).investmentDate);
			modeTextView.setText(listInfo.get(position).investmentAccount);
			
			//-----------将用户名转换成星号代替------------//
			if (listInfo.get(position).investmentMemName.length()==2) {
				 String startStr = listInfo.get(position).investmentMemName.substring(0, 1);
				 String endStr = listInfo.get(position).investmentMemName.substring(1);
				 userName = startStr+"**"+endStr;
			}else if (listInfo.get(position).investmentMemName.length()==3) {
				 String startStr = listInfo.get(position).investmentMemName.substring(0, 2);
				 String endStr = listInfo.get(position).investmentMemName.substring(2);
				 userName = startStr+"**"+endStr;
			}else if (listInfo.get(position).investmentMemName.length()>=4) {
				int end = (listInfo.get(position).investmentMemName.length()-1)/2;
				 String startStr = listInfo.get(position).investmentMemName.substring(0, end);
				 String endStr = listInfo.get(position).investmentMemName.substring(end+2);
				 userName = startStr+"**"+endStr;
			}
			moneyTextView.setText(userName);
			return view;
		}

	}

	/**
	 * 获取项目id
	 * 
	 * @return 返回项目id
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * 设置项目id
	 * 
	 * @param projectId
	 *            项目id
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
