package com.rongdai.activity.personalcenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.rongdai.R;

public class MyRedEnvelopeActivity extends Activity implements OnClickListener{
	private ImageButton myuser_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_red_envelope);
	
		myuser_back = (ImageButton)findViewById(R.id.myuser_back);
		myuser_back.setOnClickListener(this);
		
		RelativeLayout red_envelope = (RelativeLayout) findViewById(R.id.red_envelope);
		red_envelope.setOnClickListener(this);
	
	}
	
	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {	
		case R.id.myuser_back:
			this.finish();
			break;	
		case R.id.red_envelope:
			intent = new Intent(this,NotUsedRedActivity.class);
			this.finish();
			break;	
		default:
			break;
		}
	}
	
	
}
