package com.rongdai.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public NoScrollViewPager(Context context) {
		super(context);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return super.onInterceptTouchEvent(arg0);
	}
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return super.onTouchEvent(arg0);
	}
}
