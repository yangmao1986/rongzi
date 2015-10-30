package com.rongdai.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * 基类的pager
 * 当想要使用初始化View控件以及实现View中的控件逻辑时可是使用initData()方法
 * @author Administrator
 *
 */
public abstract class BasePager {
	public Context mContext;
	public View RootView;
	public BasePager(Context mContext) {
		this.mContext = mContext;
		RootView = initView(mContext);
	}
	/**
	 * 初始化View 在其中可以对一些控件的查找
	 * @param context 上下文
	 * @return 返回一个View
	 */
	public abstract View initView(Context context);
	
	/**
	 * 获取View 
	 * @return 返回一个view
	 */
	public View getRootView(){
		
		return RootView;
	}
	
	/**
	 * 初始化数据，用来处理访问网络等操作
	 */
	public void initData(){
		
	}
}
