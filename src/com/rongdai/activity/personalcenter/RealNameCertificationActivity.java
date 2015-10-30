package com.rongdai.activity.personalcenter;

import com.rongdai.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class RealNameCertificationActivity extends Activity  implements OnClickListener  {

	private ImageButton realname_certification_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_real_name_certification);
	
		realname_certification_back = (ImageButton)findViewById(R.id.realname_certification_back);
		realname_certification_back.setOnClickListener(this);
	
	
	}
	
	

	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {	
		case R.id.realname_certification_back:
			this.finish();
			break;
		

		default:
			break;
		}
	}
	
}
