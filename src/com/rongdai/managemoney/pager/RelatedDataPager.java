package com.rongdai.managemoney.pager;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


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
import com.rongdai.R;
import com.rongdai.base.BaseApplication;
import com.rongdai.base.BasePager;
import com.rongdai.constants.ConfigConstants;
import com.rongdai.domain.ImageBean;
import com.rongdai.domain.RelatedDataBean;
import com.rongdai.domain.RelatedDataBean.RelatedDataBeanData.danbao;
import com.rongdai.domain.RelatedDataBean.RelatedDataBeanData.description;
import com.rongdai.domain.RelatedDataBean.RelatedDataBeanData.identityCard;
import com.rongdai.utils.ConfigFileUtils;
import com.rongdai.utils.Constants;
import com.rongdai.utils.ImageOptionsUtils;
import com.rongdai.view.MyGrideView;
import com.rongdai.view.PullToRefreshView;

public class RelatedDataPager extends BasePager {
	private View view;
	/**
	 * 项目id
	 */
	private String projectId;
	/**
	 * 访问页面
	 */
	private RelatedDataBean relatedDataBean;
	private List<danbao> listDanBao;
	private List<description> listDscription;
	private List<identityCard> listIdentityCard;

	private int pagerNumber = 1;
	private int HEADER = 1;
	private int refreshState = 0;
	private PullToRefreshView mPullToRefreshView;
	private HorizontalScrollView identity_card_hsv;
	private HorizontalScrollView danbao_hsv;
	private HorizontalScrollView yewu_data_hsv;
	private HorizontalScrollView company_envirement_hsv;
	private LinearLayout identity_card_ll;
	private LinearLayout danbao_ll;
	private LinearLayout yewu_data_ll;
	private LinearLayout company_envirement_ll;
	private MyGrideView mGridView;
	private ImageBean mImageBean;
	private ImageLoader mImageLoader;
	private DisplayImageOptions options;
	/**
	 * url地址存储
	 */
	private String[] dataimages;

	public RelatedDataPager(Context mContext) {
		super(mContext);
	}

	@Override
	public View initView(Context context) {
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration
				.createDefault(BaseApplication.getContext()));
		options = ImageOptionsUtils.getOptions();
		view = View.inflate(BaseApplication.getContext(),
				R.layout.activity_project_detial_related_information_pager,
				null);
		mGridView = (MyGrideView) view.findViewById(R.id.relater_information_gv);
		
		this.mGridView.setOnItemClickListener(new OnItemClickListenerImpl());
		return view;
	}
	
	

	@Override
	public void initData() {
		String json = ConfigFileUtils.getValue(
				ConfigConstants.PROJECTDETAILPAGER,
				ConfigConstants.RELATEDDATAPAGER, "");
		if ("".equals(json)) {
			getDataFromNet(HEADER,
					new PullToRefreshView(BaseApplication.getContext()));
		} else {
			processData(json);
		}
	}

	/**
	 * 访问网络获取相关资料数据
	 */
	public void getDataFromNet(int refreshState,
			PullToRefreshView mPullToRefreshView) {
		this.refreshState = refreshState;
		this.mPullToRefreshView = mPullToRefreshView;
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("sort", "3");
		params.addQueryStringParameter("pageNo", pagerNumber + "");
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
						//System.out.println("相关资料数据;;;;;;;"+responseInfo.result);
						ConfigFileUtils.setValue(
								ConfigConstants.PROJECTDETAILPAGER,
								ConfigConstants.RELATEDDATAPAGER,
								responseInfo.result);
						processData(responseInfo.result);
						
						
					}
				});

	}
	
	/**
	 * 保存图片地址
	 */
	private void saveData() {
		 dataimages=new String[mImageBean.data.imgUrl.size()];
		for(int i=0;i<mImageBean.data.imgUrl.size();i++){
			dataimages[i]="http://www.rongtoudai.cn/"+mImageBean.data.imgUrl.get(i).split(":")[1];
			//dataimages[i]="http://avatar.csdn.net/1/8/C/1_t12x3456.jpg";
			
		}
		
	
	}
	

	/**
	 * 解析数据
	 */
	protected void processData(String json) {
		Gson gson = new Gson();
		try {
			mImageBean = gson.fromJson(json, ImageBean.class);
			if (this.refreshState == 1) {
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy年MM月dd日    HH:mm:ss     ");
				Date date = new Date(System.currentTimeMillis());
				String time = format.format(date);
				mPullToRefreshView.onHeaderRefreshComplete(time);
				refreshState = 0;
			} else if (refreshState == 2) {
				mPullToRefreshView.onFooterRefreshComplete();
				refreshState = 0;
				Toast.makeText(BaseApplication.getContext(), "没有更多了", 0).show();
				return;
			}
			showGrideView();
			saveData();
		} catch (Exception e) {
		}
	}

	/**
	 * 展示GrideView
	 */
	private void showGrideView() {
		MyGrideAdapter myGrideAdapter = new MyGrideAdapter();
		mGridView.setAdapter(myGrideAdapter);
	}

	public class MyGrideAdapter extends BaseAdapter {

		private ImageView imageView;
		private TextView textView;

		@Override
		public int getCount() {
			return mImageBean.data.imgUrl.size();
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
				view = View
						.inflate(
								BaseApplication.getContext(),
								R.layout.activity_project_detail_relater_information_item,
								null);

			}else{
				view = convertView;
			}
			System.out.println(mImageBean.data.imgUrl.get(position));
			imageView = (ImageView) view.findViewById(R.id.relater_image);
			textView = (TextView) view.findViewById(R.id.relater_tv);
			String[] strs = mImageBean.data.imgUrl.get(position).split(":");
			System.out.println("url地址========="+strs[1]);
			mImageLoader.displayImage("http://www.rongtoudai.cn/"+strs[1], imageView, options);
//			Bitmap bitmap = mImageLoader.loadImageSync(strs[1]);
//			Drawable drawable = new BitmapDrawable(bitmap);
//			imageView.setBackgroundDrawable(drawable);
//			textView.setText(strs[0]);
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
	
	
	/**
	 * 
	 * 点击事件
	 * 
	 */
	
	private class OnItemClickListenerImpl implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
		/*	ImageView showImg = new ImageView(MyGridViewDemo.this);
			showImg.setScaleType(ImageView.ScaleType.CENTER); // 图片居中显示 
			showImg.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			showImg.setImageResource(MyGridViewDemo.this.picRes[position]); // 设置显示图片
			Dialog dialog = new AlertDialog.Builder(MyGridViewDemo.this)
					.setIcon(R.drawable.pic_m).setTitle("查看图片")
					.setView(showImg).setNegativeButton("关闭", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					}).create();
			dialog.show() ;*/
			
			
			Intent it = new Intent(BaseApplication.getContext(), GalleryImageActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.putExtra("position", String.valueOf(position));		
			it.putExtra("dataimage",dataimages);	
			BaseApplication.getContext().startActivity(it);
		
			
		}

	}

	
	
}
