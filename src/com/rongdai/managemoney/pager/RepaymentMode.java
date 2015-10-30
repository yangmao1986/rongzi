package com.rongdai.managemoney.pager;

import io.rong.imkit.fragment.ShowDownloadImageFragment;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.rongdai.domain.RepaymentModeBean;
import com.rongdai.domain.RepaymentModeBean.RepaymentModeBeanData.RepaymentModeBeanDataInfo;
import com.rongdai.utils.ConfigFileUtils;
import com.rongdai.utils.Constants;
import com.rongdai.view.MyListView;
import com.rongdai.view.PullToRefreshView;

public class RepaymentMode extends BasePager {
	private View view;
	/**
	 * 还款模型信息显示
	 */
	private MyListView mListView;
	/**
	 * 项目id
	 */
	private String projectId;
	/**
	 * 访问页面
	 */
	private RepaymentModeBean repaymentModeBean;
	private List<RepaymentModeBeanDataInfo> listInfo;
	private List<RepaymentModeBeanDataInfo> tempListInfo;
	
	private int pagerNumber = 0;
	private int HEADER = 1;
	private int refreshState = 0;
	private PullToRefreshView mPullToRefreshView;
	private RepaymentModeAdapter adapter;

	
	
	public RepaymentMode(Context mContext) {
		super(mContext);
	}

	@Override
	public View initView(Context context) {
		view = View.inflate(context,
				R.layout.activity_project_detail_repayment_mode, null);
		mListView = (MyListView) view.findViewById(R.id.repayment_mode_lv);
//		tempListInfo = new ArrayList<RepaymentModeBeanDataInfo>();
		return view;
	}

	@Override
	public void initData() {
		/** 如果配置文件中有内容就直接去解析，如果没有就去访问网络 */
		String json = ConfigFileUtils.getValue(
				ConfigConstants.PROJECTDETAILPAGER,
				ConfigConstants.REPAYMODE, "");
		if ("".equals(json)) {
			getDataFromNet(refreshState,new PullToRefreshView(BaseApplication.getContext()));
		} else {
			processData(json);
		}
	}

	/**
	 * 访问网络获取还款模型数据
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
		params.addQueryStringParameter("sort", "2");
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
						/**将网络数据放到缓存中*/
						ConfigFileUtils.setValue(
								ConfigConstants.PROJECTDETAILPAGER,
								ConfigConstants.REPAYMODE, responseInfo.result);
						processData(responseInfo.result);
					}
				});

	}

	/**
	 * 解析数据
	 */
	protected void processData(String json) {
		Gson gson = new Gson();
		try {
			repaymentModeBean = gson.fromJson(json, RepaymentModeBean.class);
			if(this.refreshState==0){
				tempListInfo = repaymentModeBean.data.data2;
			}else if(this.refreshState==1){
				SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
				Date date = new Date(System.currentTimeMillis());
				String time = format.format(date);
				mPullToRefreshView.onHeaderRefreshComplete(time);
				refreshState = 0;
				tempListInfo = repaymentModeBean.data.data2;
				Toast.makeText(BaseApplication.getContext(), "刷新成功", 0).show();
			}else if(refreshState == 2){
				mPullToRefreshView.onFooterRefreshComplete();
				refreshState = 0;
				if(repaymentModeBean.data.data2.size()!=0){
					tempListInfo.addAll(repaymentModeBean.data.data2);
				}else{
					Toast.makeText(BaseApplication.getContext(), "没有更多可以加载了",0).show();
				}
			}
			showList();
		} catch (Exception e) {
			
		}
	}

	/**
	 * 展示listView
	 */
	private void showList() {
		if(adapter==null){
			adapter = new RepaymentModeAdapter();
			mListView.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
	}

	class RepaymentModeAdapter extends BaseAdapter {
		/**
		 * 显示还款时间
		 */
		private TextView timeTextView;
		/**
		 * 显示类型
		 */
		private TextView modeTextView;
		/**
		 * 显示还款金额
		 */
		private TextView moneyTextView;

		@Override
		public int getCount() {
			return tempListInfo.size();
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
			
			 double a=Double.valueOf(tempListInfo.get(position).borrowShouldCount).doubleValue();
			 
			timeTextView.setText(tempListInfo.get(position).borrowExpectDate);
			modeTextView.setText(tempListInfo.get(position).borrowPayType);
			
			moneyTextView.setText(String.format("%.2f", a));
			
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
