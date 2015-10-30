package com.rongdai.pager;

import java.util.ArrayList;
import java.util.List;

import com.rongdai.R;
import com.rongdai.base.BaseApplication;
import com.rongdai.base.BasePager;
import com.rongdai.managemoney.pager.OtherProject;
import com.rongdai.managemoney.pager.RecommendProject;
import com.rongdai.view.PullToRefreshView;
import com.rongdai.view.PullToRefreshView.OnFooterRefreshListener;
import com.rongdai.view.PullToRefreshView.OnHeaderRefreshListener;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
public class ManagerMenoyCenter extends BasePager implements OnCheckedChangeListener, OnHeaderRefreshListener, OnFooterRefreshListener {
	private View view;
	private List<ImageView> imageViewList;
	/**
	 * 推荐项目
	 */
	private RadioButton recommendRadioButton;
	/**
	 * 其他项目
	 */
	private RadioButton otherRadioButton;
	/**
	 * 轮播广告Viewpager
	 */
	private ViewPager mViewPager;
	/**
	 * 用来填充里面的布局
	 */
	private FrameLayout fl;
	/**
	 * 用来包裹radiobutton
	 */
	private RadioGroup mRadioGroup;
	/**
	 * 推荐项目
	 */
	private RecommendProject recommendProject;
	/**
	 * 其他项目
	 */
	private OtherProject otherProject;
	private PullToRefreshView mPullToRefreshView;
	private int refreshState = 0;
	private int flags = 1;
	private WindowManager wm;
	private int width;

	public ManagerMenoyCenter(Context mContext) {
		super(mContext);
	}

	@Override
	public View initView(Context context) {
		imageViewList = new ArrayList<ImageView>();
		view = View.inflate(BaseApplication.getContext(),R.layout.fragment_manager_money_center, null);
		mRadioGroup = (RadioGroup) view
				.findViewById(R.id.manager_money_recommend_rg);
		
		recommendRadioButton = (RadioButton) view
				.findViewById(R.id.manager_money_recommend);
		otherRadioButton = (RadioButton) view
				.findViewById(R.id.manager_money_other);
		mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_refresh_view);
		wm = (WindowManager) BaseApplication.getContext().getSystemService(BaseApplication.getContext().WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		fl = (FrameLayout) view.findViewById(R.id.manager_money_fl);
		
		/**给radiobutton设置背景色*/
		Drawable[] drawables = recommendRadioButton.getCompoundDrawables();
		drawables[3].setBounds(0, 0, width/4, 5);
		recommendRadioButton.setCompoundDrawables(null, null, null, drawables[3]);
		
		Drawable[] otherDrawables = otherRadioButton.getCompoundDrawables();
		otherDrawables[3].setBounds(0, 0, width/4, 5);
		otherRadioButton.setCompoundDrawables(null, null, null, otherDrawables[3]);
		recommendProject = new RecommendProject(BaseApplication.getContext());
		otherProject = new OtherProject(BaseApplication.getContext());
		return view;
	}

	@Override
	public void initData() {
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		
		//获取焦点
		mRadioGroup.setFocusable(true);
		mRadioGroup.setFocusableInTouchMode(true);
		mRadioGroup.requestFocus();
		
		
		recommendRadioButton.setChecked(true);
		recommendProject.initData();
		fl.removeAllViews();
		fl.addView(recommendProject.getRootView());
		mRadioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.manager_money_recommend:
			flags = 1;
			fl.removeAllViews();
			recommendProject.initData();
			fl.addView(recommendProject.getRootView());
			break;
		case R.id.manager_money_other:
			if(RecommendProject.task!=null){
				RecommendProject.task.cancel();
				RecommendProject.task = null;
			}
			flags = 2;
			fl.removeAllViews();
			otherProject.initData();
			fl.addView(otherProject.getRootView());
			break;

		default:
			break;
		}
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		refreshState = 1;
		if(flags == 1){
			recommendProject.getDataFromNet(refreshState, mPullToRefreshView);
		}
		if(flags == 2){
			otherProject.getDataFromNet(refreshState, mPullToRefreshView);
		}
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		refreshState = 2;
		if(flags == 1){
			recommendProject.getDataFromNet(refreshState, mPullToRefreshView);
		}else if(flags == 2){
			otherProject.getDataFromNet(refreshState, mPullToRefreshView);
		}
	}

}
