package com.rongdai;

import java.io.FileOutputStream;
import java.io.InputStream;

import cn.jpush.android.api.JPushInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;

public class SplishActivity extends Activity {
	private LinearLayout splishLinearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splish);
		splishLinearLayout = (LinearLayout) findViewById(R.id.splish);
		showAnimation();
	}

	private void showAnimation() {
		AnimationSet animationSet = new AnimationSet(true);

		AlphaAnimation animation = new AlphaAnimation(0.1f, 1);
		animation.setDuration(3500);
		animation.setFillAfter(true);
		// animationSet.addAnimation(rotateAnimation);
		animationSet.addAnimation(animation);

		splishLinearLayout.startAnimation(animationSet);
		animationSet.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				toMainUi();

			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}
	protected void toMainUi() {
		Intent intent = new Intent(SplishActivity.this, MainActivity.class);
		startActivity(intent);
		this.finish();
	}
}
