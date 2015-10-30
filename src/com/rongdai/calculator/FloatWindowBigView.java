package com.rongdai.calculator;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

import com.rongdai.R;
import com.rongdai.base.BaseApplication;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FloatWindowBigView extends LinearLayout {

	/**
	 * 用于更新小悬浮窗的位置
	 */
	private WindowManager windowManager;
	/**
	 * 小悬浮窗的参数
	 */
	private WindowManager.LayoutParams mParams;

	/**
	 * 记录系统状态栏的高度
	 */
	private static int statusBarHeight;

	/**
	 * 记录当前手指位置在屏幕上的横坐标值
	 */
	private float xInScreen;

	/**
	 * 记录当前手指位置在屏幕上的纵坐标值
	 */
	private float yInScreen;

	/**
	 * 记录手指按下时在屏幕上的横坐标的值
	 */
	private float xDownInScreen;

	/**
	 * 记录手指按下时在屏幕上的纵坐标的值
	 */
	private float yDownInScreen;

	/**
	 * 记录手指按下时在小悬浮窗的View上的横坐标的值
	 */
	private float xInView;

	/**
	 * 记录手指按下时在小悬浮窗的View上的纵坐标的值
	 */
	private float yInView;

	/**
	 * 记录大悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录大悬浮窗的高度
	 */
	public static int viewHeight;

	/**
	 * 投资金额
	 */
	private EditText moneyEditText;

	/**
	 * 年化收益
	 */
	private EditText interestEditText;

	/**
	 * 计息时长
	 */
	private EditText timeEditText;

	/**
	 * 结果显示
	 */
	private TextView resultTextView;

	public FloatWindowBigView(final Context context) {
		super(context);
		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		LayoutInflater.from(context).inflate(R.layout.float_window_big, this);
		View view = findViewById(R.id.big_window_layout);
		
		view.measure(View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
				.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
		
		viewWidth = view.getMeasuredWidth();
		viewHeight = view.getMeasuredHeight();

		moneyEditText = (EditText) findViewById(R.id.calculator_money);
		interestEditText = (EditText) findViewById(R.id.calculator_interest);
		timeEditText = (EditText) findViewById(R.id.calculator_time);
		resultTextView = (TextView) findViewById(R.id.calculator_result);

		ImageView close = (ImageView) findViewById(R.id.close_calculator);
		Button calculate = (Button) findViewById(R.id.calculate);
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击关闭悬浮窗的时候，移除所有悬浮窗，并停止Service
				MyWindowManager.removeBigWindow(context);
				// MyWindowManager.removeSmallWindow(context);
				// Intent intent = new Intent(getContext(),
				// FloatWindowService.class);
				// context.stopService(intent);
			}
		});
		calculate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String money = moneyEditText.getText().toString().trim();
				String insterst = interestEditText.getText().toString().trim();
				String time = timeEditText.getText().toString().trim();
				if(TextUtils.isEmpty(money)||TextUtils.isEmpty(insterst)||TextUtils.isEmpty(time)){
					Toast.makeText(BaseApplication.getContext(), "请填写全部信息", 0).show();
					return ;
				}else{
					double dMoney = Double.parseDouble(money);
					double dInsterst = Double.parseDouble(insterst);
					double dTime = Double.parseDouble(time);
					double showMoney = dMoney*dInsterst*dTime/36500;
					DecimalFormat df = new DecimalFormat("#.00");
					resultTextView.setText(df.format(showMoney)+"");
				}
				Toast.makeText(BaseApplication.getContext(), "计算了", 0).show();

			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
			xInView = event.getX();
			yInView = event.getY();
			xDownInScreen = event.getRawX();
			yDownInScreen = event.getRawY() - getStatusBarHeight();
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			break;
		case MotionEvent.ACTION_MOVE:
			xInScreen = event.getRawX();
			yInScreen = event.getRawY() - getStatusBarHeight();
			// 手指移动的时候更新小悬浮窗的位置
			updateViewPosition();
			break;
		/*
		 * case MotionEvent.ACTION_UP: //
		 * 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等
		 * ，则视为触发了单击事件。 break;
		 */
		default:
			break;
		}
		return true;
	}

	/**
	 * 更新大悬浮窗在屏幕中的位置。
	 */
	private void updateViewPosition() {
		mParams.x = (int) (xInScreen - xInView);
		mParams.y = (int) (yInScreen - yInView);
		windowManager.updateViewLayout(this, mParams);
	}

	/**
	 * 用于获取状态栏的高度。
	 * 
	 * @return 返回状态栏高度的像素值。
	 */
	private int getStatusBarHeight() {
		if (statusBarHeight == 0) {
			try {
				Class<?> c = Class.forName("com.android.internal.R$dimen");
				Object o = c.newInstance();
				Field field = c.getField("status_bar_height");
				int x = (Integer) field.get(o);
				statusBarHeight = getResources().getDimensionPixelSize(x);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return statusBarHeight;
	}

	/**
	 * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
	 * 
	 * @param params
	 *            小悬浮窗的参数
	 */
	public void setParams(WindowManager.LayoutParams params) {
		mParams = params;
	}

}
