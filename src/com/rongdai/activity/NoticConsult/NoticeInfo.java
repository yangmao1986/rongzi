package com.rongdai.activity.NoticConsult;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.R;
import com.rongdai.base.BaseActivity;
import com.rongdai.domain.NoticeInfoBean;
import com.rongdai.domain.NoticeInfoBean.Data.ReplyInfo;
import com.rongdai.domain.NoticeInfoBean.Data.ReplyInfo.Replys;
import com.rongdai.utils.Constants;
import com.rongdai.utils.ImageLoadUtils;
import com.rongdai.utils.ListViewAdapter;
import com.rongdai.utils.SwitchPager;
import com.rongdai.utils.ToastUtils;
import com.rongdai.utils.UTFDecoder;
import com.rongdai.utils.UserInfoUtils;
import com.rongdai.utils.XUtilsConstans;
import com.rongdai.view.PullToRefreshView;
import com.rongdai.view.PullToRefreshView.OnFooterRefreshListener;
import com.rongdai.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 咨询详情
 * 
 * @author Administrator
 * 
 */
public class NoticeInfo extends BaseActivity implements OnClickListener, OnHeaderRefreshListener, OnFooterRefreshListener {
	/**
	 * 标题
	 */
	private TextView noticeinfo_title;
	/**
	 * 关键字
	 */
	private TextView newsKeyWord;
	/**
	 * 时间
	 */
	private TextView noticeinfo_time;
	/**
	 * 内容
	 */
	private TextView noticeinfo_content;
	/**
	 * 文章来源
	 */
	private TextView resouce;
	/**
	 * 输入文字(我来说两句)
	 */
	private EditText noticeinfo_say;
	/**
	 * 回退按钮
	 */
	private ImageButton noiticeinfo_back;
	/**
	 * 分享按钮
	 */
	private ImageButton noticeinfo_share;
	private String pageNo;
	private String newsId;
	/**
	 * 评论的listView
	 */
	private ListView lv_evaluation; 
	/**
	 * 评论信息
	 */
	private List<ReplyInfo> replyInfos;
	private String title;
	private String newsContent;
	private String newsSendTime;
	private String newsKeyWords;
	private String newsCome;
	private String newsImg;

	/**
	 * 外部评论listView的adapter
	 */
	private MyEvaluationAdapter adapter;
	/**
	 * 内部评论listView的adapter
	 */
	private MyEvaluationAdapters adapters;
	/**
	 * 每一条评论中的子评论
	 */
	private List<Replys> replys;
	private String evaluation_type = "0";
	private List<ReplyInfo> mReplyInfo;
	private PullToRefreshView refreshView;
	private String userId;
	private String content;
	private InputMethodManager inputManager;
	private String replyId;
	private NoticeInfoBean noticeInfo;
	@Override
	public void initData() {
		super.initData();
		setContentView(R.layout.activity_noticeinfo);
		Intent intent = getIntent();
		pageNo = intent.getStringExtra("pageNo");
		newsId = intent.getStringExtra("newsId");
		userId = UserInfoUtils.getRongdaiAccount(NoticeInfo.this);
		noticeinfo_title = (TextView) findViewById(R.id.noticeinfo_title);
		newsKeyWord = (TextView) findViewById(R.id.newsKeyWords);
		noticeinfo_time = (TextView) findViewById(R.id.noticeinfo_time);
		noticeinfo_content = (TextView) findViewById(R.id.noticeinfo_content);
		resouce = (TextView) findViewById(R.id.resouce);
		noticeinfo_say = (EditText) findViewById(R.id.noticeinfo_say);
		noticeinfo_say.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_SEND){  
					content = noticeinfo_say.getText().toString().trim();
					if(TextUtils.isEmpty(userId)){
						ToastUtils.show(NoticeInfo.this, "你还没有登陆，请先登录");
						return false;
					}
					if(TextUtils.isEmpty(content)){
						ToastUtils.show(NoticeInfo.this, "评论内容不能为空");
						return false;
					}
					getEvaluationDataFromNet(newsId, userId, content, evaluation_type,
							replyId);
				}
				return false;
			}
		});
		noiticeinfo_back = (ImageButton) findViewById(R.id.noiticeinfo_back);
		noticeinfo_share = (ImageButton) findViewById(R.id.noticeinfo_share);
		lv_evaluation = (ListView) findViewById(R.id.lv_evaluation);
		noiticeinfo_back.setOnClickListener(this);
		//上拉加载 下拉刷新
		refreshView = (PullToRefreshView) findViewById(R.id.ptrv);
		refreshView.setOnHeaderRefreshListener(this);
		refreshView.setOnFooterRefreshListener(this);
		
		getNoticeDataFromNet(pageNo, newsId);
	}

	/**
	 * 获取咨询详情
	 */
	private void getNoticeDataFromNet(final String pageNo, final String newsId) {
		pagerNumber = 1;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("pageNo", pageNo);
		params.addBodyParameter("newsId", newsId);
		httpUtils.send(HttpMethod.POST, Constants.getNoticeDetail, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						new SwitchPager(NoticeInfo.this) {
							@Override
							public void update() {
								getNoticeDataFromNet(pageNo, newsId);
							}
						}.loadErrorPager();
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						
						Gson gson = new Gson();
						noticeInfo = gson.fromJson(json, NoticeInfoBean.class);
						NoticeInfo.this.mReplyInfo = noticeInfo.data.reply;
						setData();
						new SwitchPager(NoticeInfo.this) {
							@Override
							public void update() {
							}
						}.loadSuccessPager();
						
						if (fromRefresh == true) {
							refreshView.onHeaderRefreshComplete();
							Toast.makeText(NoticeInfo.this, "刷新成功",
									Toast.LENGTH_SHORT).show();
							fromRefresh = false;
						}

					}

					private void setData() {
						title = noticeInfo.data.newsTitle;
						newsContent = noticeInfo.data.newsContent;
						//System.out.println("新闻公告"+newsContent);
						newsSendTime = noticeInfo.data.newsSendTime;
						newsKeyWords = noticeInfo.data.newsKeyWords;
						newsCome = noticeInfo.data.newsCome;
						newsImg = noticeInfo.data.newsImg;
						replyInfos = noticeInfo.data.reply;
						noticeinfo_title.setText("    " + title);
						
						newsContent = newsContent.replace("％","%");
						UTFDecoder decoder = new UTFDecoder();
						newsContent = decoder.UTFDecoder(newsContent);
						noticeinfo_content.setText(Html.fromHtml(newsContent));
						
//						noticeinfo_content.setText("    " + newsContent);
						noticeinfo_time.setText(newsSendTime);
						newsKeyWord.setText(newsKeyWords);
						resouce.setText("文章来源: " + newsCome);

						adapter = new MyEvaluationAdapter();
						lv_evaluation.setAdapter(adapter);
						lv_evaluation.setDividerHeight(0);
						ListViewAdapter.setListViewHeightBasedOnChildren(lv_evaluation);
					}
				});
	}
	
	/**
	 * 获取更多咨询详情
	 */
	private void getMoreNoticeDataFromNet(final String pageNo, final String newsId) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("pageNo", pageNo);
		params.addBodyParameter("newsId", newsId);
		httpUtils.send(HttpMethod.POST, Constants.getNoticeDetail, params,
				new RequestCallBack<String>() {
			private NoticeInfoBean noticeInfo2;
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						new SwitchPager(NoticeInfo.this) {
							@Override
							public void update() {
								getNoticeDataFromNet(pageNo, newsId);
							}
						}.loadErrorPager();
					}
					
					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						Gson gson = new Gson();
						noticeInfo2 = gson.fromJson(json, NoticeInfoBean.class);
						List<ReplyInfo> infos2 = noticeInfo2.data.reply;
						if(infos2 == null || infos2.size() == 0){
							Toast.makeText(NoticeInfo.this, "没有更多数据了",Toast.LENGTH_SHORT).show();
						}else{
							mReplyInfo.addAll(infos2);
							Toast.makeText(NoticeInfo.this, "加载成功",Toast.LENGTH_SHORT).show();
						}
						refreshView.onFooterRefreshComplete();
						setData();
						new SwitchPager(NoticeInfo.this) {
							@Override
							public void update() {
							}
						}.loadSuccessPager();
						//根据listView的实际高度 设置高度
						ListViewAdapter.setListViewHeightBasedOnChildren(lv_evaluation);
						adapter.notifyDataSetChanged();
					}
					
					private void setData() {
						title = noticeInfo2.data.newsTitle;
						newsContent = noticeInfo2.data.newsContent;
						newsSendTime = noticeInfo2.data.newsSendTime;
						newsKeyWords = noticeInfo2.data.newsKeyWords;
						newsCome = noticeInfo2.data.newsCome;
						newsImg = noticeInfo2.data.newsImg;
						replyInfos = mReplyInfo;
						newsContent = newsContent.replace("％","%");
						UTFDecoder decoder = new UTFDecoder();
						newsContent = decoder.UTFDecoder(newsContent);
						noticeinfo_content.setText(Html.fromHtml(newsContent));
						
						noticeinfo_title.setText("    " + title);
//						noticeinfo_content.setText("    " + newsContent);
						noticeinfo_time.setText(newsSendTime);
						newsKeyWord.setText(newsKeyWords);
						resouce.setText("文章来源: " + newsCome);

						adapter = new MyEvaluationAdapter();
						lv_evaluation.setAdapter(adapter);
						lv_evaluation.setDividerHeight(0);
						ListViewAdapter.setListViewHeightBasedOnChildren(lv_evaluation);
					}
				});
	}
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.noiticeinfo_back:
			this.finish();
			break;
		default:
			break;
		}
	}

	class MyEvaluationAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			if (replyInfos != null && replyInfos.size() > 0) {
				return replyInfos.size();
			}
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = View.inflate(NoticeInfo.this, R.layout.item_evaluation,
						null);
			} else {
				view = convertView;
			}
			ImageView evaluation_imagehead = (ImageView) view
					.findViewById(R.id.evaluation_imagehead);
			TextView evaluation_name = (TextView) view
					.findViewById(R.id.evaluation_name);
			TextView evaluation_time = (TextView) view
					.findViewById(R.id.evaluation_time);
			TextView evaluation_content = (TextView) view
					.findViewById(R.id.evaluation_content);
			Button evaluation_say = (Button) view
					.findViewById(R.id.evaluation_say);
			ListView lv_evaluations = (ListView) view
					.findViewById(R.id.lv_evaluations);

			ImageLoadUtils.loadimage(NoticeInfo.this,
					replyInfos.get(position).imgUrl, evaluation_imagehead);
			evaluation_name.setText(replyInfos.get(position).oneName);
			evaluation_time.setText(replyInfos.get(position).replyDate);
			evaluation_content.setText(replyInfos.get(position).replyContent);
			replys = replyInfos.get(position).replys;

			if (replys != null && replys.size() > 0) {
				adapters = new MyEvaluationAdapters();
				lv_evaluations.setDividerHeight(0);
				lv_evaluations.setAdapter(adapters);
				ListViewAdapter
						.setListViewHeightBasedOnChildren(lv_evaluations);
			}

			evaluation_say.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(TextUtils.isEmpty(userId)){
						ToastUtils.show(NoticeInfo.this, "你还没有登陆，请先登录");
						return;
					}
					evaluation_type = "1";
					noticeinfo_say.setImeOptions(EditorInfo.IME_ACTION_SEND);
					//弹出软键盘
					if(inputManager == null){
						inputManager = (InputMethodManager) noticeinfo_say.getContext()
								.getSystemService(Context.INPUT_METHOD_SERVICE);
					}
					inputManager.showSoftInput(noticeinfo_say, 0);
					noticeinfo_say.setOnEditorActionListener(new OnEditorActionListener() {
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							if(actionId == EditorInfo.IME_ACTION_SEND){  
								content = noticeinfo_say.getText().toString().trim();
								if(TextUtils.isEmpty(content)){
									ToastUtils.show(NoticeInfo.this, "评论内容不能为空");
									return false;
								}
								replyId = replyInfos.get(position).replyId;
								getEvaluationDataFromNet(newsId, userId, content, evaluation_type,
										replyId);
							}
							return false;
						}
					});
				}
			});
			return view;
		}

	}

	/**
	 * 每一条评论中的子评论
	 * 
	 * @author Administrator
	 * 
	 */
	class MyEvaluationAdapters extends BaseAdapter {

		@Override
		public int getCount() {
			if (replys != null && replys.size() > 0) {
				return replys.size();
			}
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			if (convertView == null) {
				view = View.inflate(NoticeInfo.this, R.layout.item_evaluations,
						null);
			} else {
				view = convertView;
			}
			TextView name_from = (TextView) view
					.findViewById(R.id.evaluate_from);
			TextView name_to = (TextView) view.findViewById(R.id.evaluate_to);
			TextView evaluations_content = (TextView) view
					.findViewById(R.id.evaluations_content);
			Button evaluations_say = (Button) view
					.findViewById(R.id.evaluations_say);
			name_from.setText(replys.get(position).twoName);
			name_to.setText(replys.get(position).oneName);
			evaluations_content.setText(replys.get(position).replyContent);

			content = noticeinfo_say.getText().toString().trim();
			evaluations_say.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(TextUtils.isEmpty(userId)){
						ToastUtils.show(NoticeInfo.this, "你还没有登陆，请先登录");
						return;
					}
					evaluation_type = "1";
					inputManager.showSoftInput(noticeinfo_say, 0);
					//弹出软键盘
					if(inputManager == null){
						inputManager = (InputMethodManager) noticeinfo_say.getContext()
								.getSystemService(Context.INPUT_METHOD_SERVICE);
					}
					noticeinfo_say.setImeOptions(EditorInfo.IME_ACTION_SEND);
					noticeinfo_say.setOnEditorActionListener(new OnEditorActionListener() {
						@Override
						public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
							if(actionId == EditorInfo.IME_ACTION_SEND){
								content = noticeinfo_say.getText().toString().trim();
								if(TextUtils.isEmpty(content)){
									ToastUtils.show(NoticeInfo.this, "评论内容不能为空");
									return false;
								}
								replyId = replyInfos.get(position).replyId;
								getEvaluationDataFromNet(newsId, userId, content, evaluation_type,
										replyId);
							}
							return false;
						}
					});
				}
			});

			return view;
		}

	}

	/**
	 * 提交(咨询/公告)评论
	 */
	private void getEvaluationDataFromNet(final String newsId, String userId,
			String content, String type, String parent) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("newsId", newsId);
		params.addBodyParameter("userId", userId);
		params.addBodyParameter("content", content);
		params.addBodyParameter("type", type);
		// 此评论为评论内容 加上外部评论的id
		if ("1".equals(evaluation_type)) {
			params.addBodyParameter("parent", parent);
		}
		httpUtils.send(HttpMethod.POST, Constants.replyNews, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						//System.out.println(json);
						getNoticeDataFromNet(pageNo, newsId);
						adapter.notifyDataSetChanged();
						//发表评论成功  输入框设为空
						noticeinfo_say.setText("");
					}
				});
	}
	int pagerNumber = 1;
	private boolean fromRefresh;
	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		pagerNumber ++;
		getMoreNoticeDataFromNet(pagerNumber+"",newsId);
		
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		fromRefresh = true;
		getNoticeDataFromNet("1",newsId);
	}

}
