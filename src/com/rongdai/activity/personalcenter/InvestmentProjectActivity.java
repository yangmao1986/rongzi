package com.rongdai.activity.personalcenter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
import com.rongdai.base.BaseApplication;
import com.rongdai.domain.InvestmentProjectListInfo;

import com.rongdai.domain.InvestmentProjectListInfo.loanProjectListData;
import com.rongdai.domain.ModifyLoginPwdInfo;
import com.rongdai.domain.investAndEarn;

import com.rongdai.utils.Constants;
import com.rongdai.utils.ImageLoadUtils;
import com.rongdai.utils.PersonInfoConstans;
import com.rongdai.utils.SwitchPager;
import com.rongdai.utils.ToastUtils;
import com.rongdai.utils.UserInfoUtils;
import com.rongdai.utils.XListView;
import com.rongdai.utils.XListView.IXListViewListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class InvestmentProjectActivity extends Activity implements OnClickListener,IXListViewListener {
	private ImageButton about_us_back;
	private XListView mListView;
	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> items = new ArrayList<String>();
	private List<Map<String, String>> list = new ArrayList<Map<String, String>>();
	private Handler mHandler;
	private int start = 1;
	private static int refreshCnt = 0;
	List<loanProjectListData> listjson;
	Context mContext;
	private MyAdapter adapter;
	private String moreid="0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_investment_project);
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
		investmentProjectListPost(start);
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
				mAdapter = new ArrayAdapter<String>(InvestmentProjectActivity.this, R.layout.list_item, items);
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
	
	
private void investmentProjectListPost(int refreshCnt2) {
		
	
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();	
		params.addBodyParameter("rongDaiAccount", UserInfoUtils.getRongdaiAccount(BaseApplication.getContext()));
		params.addBodyParameter("pageNo", refreshCnt2+"");
		httpUtils.send(HttpMethod.POST, Constants.investmentProjectList, params,
				new RequestCallBack<String>() {

					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						//System.out.println(json);
						 Log.e("json", json);
						Gson gson = new Gson();						
						
				            Type type = new TypeToken<InvestmentProjectListInfo>() {}.getType();
				            InvestmentProjectListInfo investmentProjectListInfo = gson.fromJson(json, InvestmentProjectListInfo.class);
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
				            
//						 String jsons = investmentProjectListInfo.data.get(0).borrowName;
//						 Log.e("jsons", jsons);
						 
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
			view = View.inflate(mContext, R.layout.list_item, null);
		}else{
			view = contentView;
		}
	
		TextView borrowName_textview = (TextView) view.findViewById(R.id.borrowName_textview);
		TextView investmentDate_textview = (TextView) view.findViewById(R.id.investmentDate_textview);
		TextView borrowYearRate_textview = (TextView) view.findViewById(R.id.borrowYearRate_textview);
		TextView borrowLimit_textview = (TextView) view.findViewById(R.id.borrowLimit_textview);
		ImageView state_iamgeview = (ImageView) view.findViewById(R.id.state_iamgeview);
		TextView investmentAccount_textview = (TextView) view.findViewById(R.id.investmentAccount_textview);
		TextView hadrRturnedMoney_textview = (TextView) view.findViewById(R.id.hadrRturnedMoney_textview);
		TextView waitingRturnedMoney_textview = (TextView) view.findViewById(R.id.waitingRturnedMoney_textview);
		
		
		borrowName_textview.setText(listjson.get(position).borrowName);
		investmentDate_textview.setText("投标时间："+listjson.get(position).investmentDate);
		borrowYearRate_textview.setText( "利率："+listjson.get(position).borrowYearRate);
		borrowLimit_textview.setText("期限："+listjson.get(position).borrowLimit+"月");

		
		if(listjson.get(position).borrowStatus.equals("回款中")){
			state_iamgeview.setImageResource(R.drawable.back_pic);
	}else if(listjson.get(position).borrowStatus.equals("投资中")){
		state_iamgeview.setImageResource(R.drawable.tou_pic);	
	}else if(listjson.get(position).borrowStatus.equals("已完成")){
		state_iamgeview.setImageResource(R.drawable.finish_pic);	
	}
		investmentAccount_textview.setText("购买价："+listjson.get(position).investmentAccount);
		Double result = Double.parseDouble(listjson.get(position).hadrRturnedMoney);
		 hadrRturnedMoney_textview.setText("已回款："+ String.format("%.2f", result));
		 //hadrRturnedMoney_textview.setText("已回款："+ listjson.get(position).hadrRturnedMoney);
		waitingRturnedMoney_textview.setText("待回款："+listjson.get(position).waitingRturnedMoney);

	
		return view;
	}
}

	
	
	
}
