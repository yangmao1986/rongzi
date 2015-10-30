package com.rongdai.utils;

import com.rongdai.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 加载不同界面的工具类
 * 
 * @author Administrator
 * 
 */
public abstract class SwitchPager {

	/**
	 * 加载错误界面
	 */
	private View errorPager;
	/**
	 * 正在加载的界面
	 */
	private View loadingPager;
	/**
	 * 加载数据为空的界面
	 */
	private View emptyPager;
	/**
	 * 加载数据不为空的界面
	 */
	private View dataPager;
	private Activity view;

	public SwitchPager(Activity view) {
		this.view = view;
		getView();
	}

	public void loadErrorPager() {
		emptyPager.setVisibility(View.GONE);
		errorPager.setVisibility(View.VISIBLE);
		loadingPager.setVisibility(View.GONE);
		dataPager.setVisibility(View.GONE);

		Button button = (Button) errorPager.findViewById(R.id.page_bt);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadingPager.setVisibility(View.VISIBLE);
				errorPager.setVisibility(View.GONE);
				update();
			}
		});
	}

	public void loadLoadingPager() {
		if (notNull()) {
			emptyPager.setVisibility(View.GONE);
			errorPager.setVisibility(View.GONE);
			loadingPager.setVisibility(View.VISIBLE);
			dataPager.setVisibility(View.VISIBLE);
		}
	}

	public void loadEmptyPager() {
		if (notNull()) {
			emptyPager.setVisibility(View.VISIBLE);
			errorPager.setVisibility(View.GONE);
			loadingPager.setVisibility(View.GONE);
			dataPager.setVisibility(View.GONE);
		}
	}

	public void loadSuccessPager() {
		if (notNull()) {
			dataPager.setVisibility(View.VISIBLE);
			emptyPager.setVisibility(View.GONE);
			errorPager.setVisibility(View.GONE);
			loadingPager.setVisibility(View.GONE);
		}
	}

	private void getView() {
		errorPager = view.findViewById(R.id.loading_page_error);
		loadingPager = view.findViewById(R.id.loading_page_loading);
		emptyPager = view.findViewById(R.id.loading_page_empty);
		dataPager = view.findViewById(R.id.loading_page_data);
	}

	private boolean notNull() {
		return (dataPager != null && emptyPager != null && errorPager != null && loadingPager != null);
	}

	public abstract void update();
}
