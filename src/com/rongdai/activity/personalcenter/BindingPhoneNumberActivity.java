package com.rongdai.activity.personalcenter;

import com.rongdai.R;
import com.rongdai.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class BindingPhoneNumberActivity extends Activity implements OnClickListener {
	private ImageButton binding_phone_number_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binding_phone_number);
		
		binding_phone_number_back = (ImageButton)findViewById(R.id.binding_phone_number_back);
		binding_phone_number_back.setOnClickListener(this);
	
	}
	
	

	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {	
		case R.id.binding_phone_number_back:
			this.finish();
			break;
		default:
			break;
		}
	}
	
	
}
