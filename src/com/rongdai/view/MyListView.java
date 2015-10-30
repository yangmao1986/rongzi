package com.rongdai.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.widget.ListView;

public class MyListView extends ListView {

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public MyListView(Context context) {
		super(context);
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}
	/*@Override
	public boolean onTouchEvent(MotionEvent ev) {
		this.getParent().requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(ev);
	}*/
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}
