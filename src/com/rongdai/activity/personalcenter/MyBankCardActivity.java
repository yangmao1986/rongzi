package com.rongdai.activity.personalcenter;

import com.rongdai.R;
import com.rongdai.R.id;
import com.rongdai.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MyBankCardActivity extends Activity implements OnClickListener{
	private ImageButton MyBankCard_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_bank_card);
	
		MyBankCard_back = (ImageButton)findViewById(R.id.MyBankCard_back);
		MyBankCard_back.setOnClickListener(this);
	
	
	
	
	
	}
	


	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {	
		case R.id.MyBankCard_back:
			this.finish();
			break;
		default:
			break;
		}
	}
	
	
}
