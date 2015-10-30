package com.rongdai.activity.personalcenter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.R;
import com.rongdai.R.id;
import com.rongdai.R.layout;
import com.rongdai.activity.personalcenter.InvestmentProjectActivity.MyAdapter;
import com.rongdai.base.BaseApplication;
import com.rongdai.domain.InvestmentProjectListInfo;
import com.rongdai.domain.InvestmentProjectListInfo.loanProjectListData;
import com.rongdai.domain.MoneyRecordListInfo;
import com.rongdai.domain.MoneyRecordListInfo.MoneyRecordListData;
import com.rongdai.utils.Constants;
import com.rongdai.utils.SwitchPager;
import com.rongdai.utils.ToastUtils;
import com.rongdai.utils.UserInfoUtils;
import com.rongdai.utils.XListView;
import com.rongdai.utils.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MoneyRecordActivity extends Activity implements OnClickListener,IXListViewListener{
	private ImageButton about_us_back;
	private XListView mListView;
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> items = new ArrayList<String>();
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private Handler mHandler;
	private int start = 1;
	private static int refreshCnt = 0;
	List<MoneyRecordListData> listjson;
	Context mContext;
	private MyAdapter adapter;
	private String moreid="0";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_money_record);
		mContext=getBaseContext();
		
		about_us_back = (ImageButton)findViewById(R.id.about_us_back);
		about_us_back.setOnClickListener(this);
	
		
		
		
		mListView = (XListView) findViewById(R.id.xListView);
		mListView.setPullLoadEnable(true);
	
		
		
		adapter=new MyAdapter();
		mListView.setAdapter(adapter);
//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
		mListView.setXListViewListener(this);
		mHandler = new Handler();
	
	
		geneItems();
		
	}

	
	private void geneItems() {
		if(moreid.equals("0")){
		MoneyRecordListPost(start);
		}else{
			Toast.makeText(mContext, "没有更多的数据",Toast.LENGTH_SHORT).show();
			
		}
	}
	
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime("刚刚");
	}
	
	@Override
	public void onRefresh() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start = ++refreshCnt;
				listjson.clear();
				geneItems();
				// mAdapter.notifyDataSetChanged();
			
				mListView.setAdapter(adapter);
				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				start=start+1;
				geneItems();
				//adapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}
	
	
	
	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {	
		case R.id.about_us_back:
			this.finish();
			break;
		default:
			break;
		}
	}
	
	
private void MoneyRecordListPost(int refreshCnt2) {
		
	
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();	
		if("".equals(UserInfoUtils.getRongdaiAccount(BaseApplication.getContext()))||null == UserInfoUtils.getRongdaiAccount(BaseApplication.getContext())){
			ToastUtils.show(this, "请先登录");
			return ;
		}
		params.addBodyParameter("rongDaiAccount",  UserInfoUtils.getRongdaiAccount(BaseApplication.getContext()));
		params.addBodyParameter("pageNo", refreshCnt2+"");
		httpUtils.send(HttpMethod.POST, Constants.MoneyRecordList, params,
				new RequestCallBack<String>() {

					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						System.out.println("资金记录"+json);
						 Log.e("json", json);
						Gson gson = new Gson();						
						
				            Type type = new TypeToken<MoneyRecordListInfo>() {}.getType();
				            MoneyRecordListInfo investmentProjectListInfo = gson.fromJson(json, MoneyRecordListInfo.class);
				            if(listjson==null){
				            listjson =investmentProjectListInfo.data;}
				            else if(investmentProjectListInfo.message.equals("没有更多数据...")){				            	
				            	Toast.makeText(mContext, "没有更多的数据",Toast.LENGTH_SHORT).show();
				            	listjson.addAll(investmentProjectListInfo.data);
				            	moreid="1";
				            }else{
				            	 listjson.addAll(investmentProjectListInfo.data);	
				            	 Toast.makeText(mContext, "加载成功",Toast.LENGTH_SHORT).show();
				            }
						 //geneItems();
						
						 adapter.notifyDataSetChanged();
					}
					
					
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						//System.out.println("访问网络失败");
						new SwitchPager((android.app.Activity)mContext){
							@Override
							public void update() {
								
							}
						}.loadErrorPager();			
					}
				});
		
		
		
	}

class MyAdapter extends BaseAdapter{

	@Override
	public int getCount() {
		if(listjson == null || listjson.size() == 0){
			return 0;
		}
		return listjson.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup group) {
		View view;
		if(contentView == null){
			view = View.inflate(mContext, R.layout.moneyrecord_list_item, null);
		}else{
			view = contentView;
		}
	
		TextView accountRecordName_textview = (TextView) view.findViewById(R.id.accountRecordName_textview);
		TextView accountRecordMoney_textview = (TextView) view.findViewById(R.id.accountRecordMoney_textview);
		TextView accountRecordType_textview = (TextView) view.findViewById(R.id.accountRecordType_textview);
		TextView accountRecordDate_textview = (TextView) view.findViewById(R.id.accountRecordDate_textview);
		
			if (listjson.get(position).accountRecordType.equals("充值")){
			accountRecordMoney_textview.setText(listjson.get(position).accountRecordMoney);
			accountRecordMoney_textview.setTextColor(Color.rgb(60, 133, 62));
	}else if(listjson.get(position).accountRecordType.equals("提现")){
		accountRecordMoney_textview.setText(listjson.get(position).accountRecordMoney);
		accountRecordMoney_textview.setTextColor(Color.rgb(194, 129, 35));
	}else if(listjson.get(position).accountRecordType.equals("投标冻结")){
		accountRecordMoney_textview.setText(listjson.get(position).accountRecordMoney);	
		accountRecordMoney_textview.setTextColor(Color.rgb(52, 130, 169));
	}else{
		accountRecordMoney_textview.setText(listjson.get(position).accountRecordMoney);	
	}
		
		accountRecordName_textview.setText(listjson.get(position).accountRecordName);
		
		accountRecordType_textview.setText(listjson.get(position).accountRecordType);
		accountRecordDate_textview.setText(listjson.get(position).accountRecordDate);
		return view;
	}
}

	
	
	
}
