package com.rongdai.activity.personalcenter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.R;
import com.rongdai.base.BaseActivity;
import com.rongdai.view.PullToRefreshView;
import com.rongdai.view.PullToRefreshView.OnFooterRefreshListener;
import com.rongdai.view.PullToRefreshView.OnHeaderRefreshListener;

public class NotUsedRedActivity extends BaseActivity implements
		OnClickListener, OnCheckedChangeListener, OnHeaderRefreshListener,
		OnFooterRefreshListener {
	private RadioGroup rg_orderstate;
	private ImageView red_back;
	private PullToRefreshView refreshlist;
	private ListView red_lv_order;
	private MyListViewAdapter adapter;

	@Override
	public void initData() {
		super.initData();

		setContentView(R.layout.activity_notusedred);
		red_back = (ImageView) findViewById(R.id.red_back);
		red_lv_order = (ListView) findViewById(R.id.red_lv_order);
		rg_orderstate = (RadioGroup) findViewById(R.id.red_rg_orderstate);
		rg_orderstate.setOnCheckedChangeListener(this);
		red_back.setOnClickListener(this);

		refreshlist = (PullToRefreshView) findViewById(R.id.red_refreshlist);
		refreshlist.setOnHeaderRefreshListener(this);
		refreshlist.setOnFooterRefreshListener(this);
		
		adapter = new MyListViewAdapter();
		red_lv_order.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.red_back:
			this.finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 获取订单列表数据
	 * 
	 * @param number
	 *            页码数
	 */
	private void getOrderListFromNet(String sort) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		httpUtils.send(HttpMethod.GET, "", params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String json = responseInfo.result;
						Gson gson = new Gson();
					}
				});
	}

	/**
	 * 获取更多订单列表数据
	 * 
	 * @param number
	 *            页码数
	 */
	private void getMoreOrderListFromNet() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		httpUtils.send(HttpMethod.GET, "", params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String json = responseInfo.result;
						Gson gson = new Gson();

					}

				});
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.notused:
			break;
		case R.id.used:
			break;
		case R.id.outdate:
			break;
		}
	}
	
	class MyListViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
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
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub

	}

}
