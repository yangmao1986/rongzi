package com.rongdai.pager;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.MainActivity;
import com.rongdai.R;
import com.rongdai.activity.NoticConsult.NoticeInfo;
import com.rongdai.base.BaseApplication;
import com.rongdai.base.BasePager;
import com.rongdai.domain.NoticConsultBean;
import com.rongdai.domain.NoticeInfoBean;
import com.rongdai.domain.NoticConsultBean.NoticConsultInfo;
import com.rongdai.utils.Constants;
import com.rongdai.utils.ImageLoadUtils;
import com.rongdai.utils.SwitchPager;
import com.rongdai.utils.XUtilsConstans;
import com.rongdai.view.PullToRefreshView;
import com.rongdai.view.PullToRefreshView.OnFooterRefreshListener;
import com.rongdai.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 公告咨询界面
 * 
 * @author Administrator
 * @param <Activity>
 * 
 */
public class NoticConsult<Activity> extends BasePager implements
		OnItemClickListener, OnHeaderRefreshListener, OnFooterRefreshListener {

	private RadioGroup rg_notice_consult;
	/**
	 * 网站公告
	 */
	private RadioButton notic_webnotic;
	/**
	 * 行业资讯
	 */
	private RadioButton notic_consult;
	/**
	 * 咨询条目
	 */
	private ListView lv_notice_consult;
	private MyAdapter adapter;
	/**
	 * 公告咨询 集合
	 */
	private List<NoticConsultInfo> noticConsultInfos;
	/**
	 * 公告咨询 集合
	 */
	private List<NoticConsultInfo> noticConsultInfos2;
	private PullToRefreshView refreshView;

	private WindowManager wm;
	private int width;
	private int type = 1;
	private boolean fromRefresh;
	private int pagerNumber = 1;
	private View view;

	public NoticConsult(Context mContext) {
		super(mContext);
	}

	@Override
	public View initView(Context context) {
		view = View.inflate(context, R.layout.fragment_notice_consult, null);
		rg_notice_consult = (RadioGroup) view
				.findViewById(R.id.rg_notice_consult);

		notic_webnotic = (RadioButton) view.findViewById(R.id.notic_webnotic);
		notic_consult = (RadioButton) view.findViewById(R.id.notic_consult);

		wm = (WindowManager) BaseApplication.getContext().getSystemService(
				BaseApplication.getContext().WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();

		/** 给radiobutton设置背景色 */
		Drawable[] drawables = notic_webnotic.getCompoundDrawables();
		drawables[3].setBounds(0, 0, width / 4, 5);
		notic_webnotic.setCompoundDrawables(null, null, null, drawables[3]);

		Drawable[] otherDrawables = notic_consult.getCompoundDrawables();
		otherDrawables[3].setBounds(0, 0, width / 4, 5);
		notic_consult.setCompoundDrawables(null, null, null, otherDrawables[3]);
		lv_notice_consult = (ListView) view
				.findViewById(R.id.lv_notice_consult);
		refreshView = (PullToRefreshView) view.findViewById(R.id.ptrv);
		return view;
	}

	@Override
	public void initData() {

		onSubThread();
	}

	/**
	 * 在子线程中执行
	 */
	private void onSubThread() {
		/*new Thread() {
			public void run() {*/
				adapter = new MyAdapter();
				lv_notice_consult.setAdapter(adapter);
				rg_notice_consult.check(R.id.notic_webnotic);
				getWebnoticConsult(1, 1);

				rg_notice_consult
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(RadioGroup group,
									int position) {
								switch (position) {
								case R.id.notic_webnotic:
									type = 1;
									getWebnoticConsult(1, type);
									break;
								case R.id.notic_consult:
									type = 2;
									getWebnoticConsult(1, type);
									break;
								}

							}
						});

				// 上拉加载 下拉刷新
				Message msg = Message.obtain();
				handler.sendEmptyMessage(0);
		/*	};
		}.start();*/
	}
	private Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			refreshListener();
		};
	};
	/**
	 * 刷新监听
	 */
	protected void refreshListener() {
		lv_notice_consult.setOnItemClickListener(this);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);
	}

	/**
	 * 获取网站公告 / 行业资讯
	 * 
	 * @param type
	 *            (1-网站公告，2-行业咨询)
	 */
	private void getWebnoticConsult(final int pager, final int type) {
		pagerNumber = 1;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("pageNo", pager + "");
		params.addBodyParameter("type", type + "");
		httpUtils.send(HttpMethod.POST, Constants.getWebNotice, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// System.out.println("访问网络失败");
						new SwitchPager((android.app.Activity) mContext) {
							@Override
							public void update() {
								getWebnoticConsult(1, type);
							}
						}.loadErrorPager();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						// System.out.println(json);
						processData(json);

						adapter.notifyDataSetChanged();
						new SwitchPager((android.app.Activity) mContext) {
							@Override
							public void update() {
							}
						}.loadSuccessPager();

						if (fromRefresh == true) {
							refreshView.onHeaderRefreshComplete();
							Toast.makeText(mContext, "刷新成功", Toast.LENGTH_SHORT)
									.show();
							fromRefresh = false;
						}
					}
				});

	}

	/**
	 * 获取更多网站公告 / 行业资讯
	 * 
	 * @param type
	 *            (1-网站公告，2-行业咨询)
	 */
	private void getMoreWebnoticConsult(final int pager, final int type) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("pageNo", pager + "");
		params.addBodyParameter("type", type + "");
		httpUtils.send(HttpMethod.POST, Constants.getWebNotice, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// System.out.println("访问网络失败");
						new SwitchPager((android.app.Activity) mContext) {
							@Override
							public void update() {
								getWebnoticConsult(1, type);
							}
						}.loadErrorPager();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						Gson gson = new Gson();
						NoticConsultBean noticConsultBean2 = gson.fromJson(
								json, NoticConsultBean.class);
						noticConsultInfos2 = noticConsultBean2.data;
						if (noticConsultInfos2 == null
								|| noticConsultInfos2.size() == 0) {
							Toast.makeText(mContext, "没有更多数据了",
									Toast.LENGTH_SHORT).show();
						} else {
							if (noticConsultInfos != null) {
								noticConsultInfos.addAll(noticConsultInfos2);
								Toast.makeText(mContext, "加载成功",
										Toast.LENGTH_SHORT).show();
							} else {
								noticConsultInfos = noticConsultInfos2;
								Toast.makeText(mContext, "加载成功",
										Toast.LENGTH_SHORT).show();
							}
						}
						refreshView.onFooterRefreshComplete();

						adapter.notifyDataSetChanged();
						new SwitchPager((android.app.Activity) mContext) {
							@Override
							public void update() {
							}
						}.loadSuccessPager();
					}
				});

	}

	protected void processData(String json) {
		Gson gson = new Gson();
		NoticConsultBean noticConsultBean = gson.fromJson(json,
				NoticConsultBean.class);
		noticConsultInfos = noticConsultBean.data;
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (noticConsultInfos == null || noticConsultInfos.size() == 0) {
				return 0;
			}
			return noticConsultInfos.size();
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
			if (contentView == null) {
				view = View.inflate(mContext, R.layout.item_notice_consult,
						null);
			} else {
				view = contentView;
			}
			ImageView iv = (ImageView) view.findViewById(R.id.iv);
			TextView notice_consult_info = (TextView) view
					.findViewById(R.id.notice_consult_info);
			TextView consult_type = (TextView) view
					.findViewById(R.id.consult_type);
			System.out.println(noticConsultInfos.get(position).newsImg);
			// BitmapUtils bitmapUtils = new
			// BitmapUtils(BaseApplication.getContext());
			// bitmapUtils.display(iv, noticConsultInfos.get(position).newsImg);

			ImageLoadUtils.loadimage(mContext,
					noticConsultInfos.get(position).newsImg, iv);
			notice_consult_info
					.setText(noticConsultInfos.get(position).newsTitle);
			consult_type.setText(noticConsultInfos.get(position).newsKeyWords);
			return view;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long arg3) {
		System.out.println("点击Item了");
		Intent intent = new Intent(mContext, NoticeInfo.class);
		intent.putExtra("pageNo", "1");
		intent.putExtra("newsId", noticConsultInfos.get(position).newsId);
		mContext.startActivity(intent);
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		pagerNumber++;
		getMoreWebnoticConsult(pagerNumber, type);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		fromRefresh = true;
		getWebnoticConsult(1, type);
	}
}
