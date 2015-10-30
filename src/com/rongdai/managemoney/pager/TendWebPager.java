package com.rongdai.managemoney.pager;

import com.rongdai.R;
import com.rongdai.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class TendWebPager extends Activity implements OnClickListener {

	private WebView wv;
	private String data;
	private ImageView imageButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tenderwebpager);
		Intent intent = getIntent();
		data = intent.getStringExtra("data");
		initView();
	}

	private void initView() {
		TextView textView = (TextView) findViewById(R.id.tv_title);
		imageButton = (ImageView) findViewById(R.id.ib_back);
		wv = (WebView) findViewById(R.id.wv);
		textView.setVisibility(View.INVISIBLE);
		imageButton.setOnClickListener(this);
		//System.out.println(Constants.tendrweb+"?"+data);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setDefaultTextEncodingName("GBK");
		wv.loadUrl(Constants.tendrweb+"?"+data);
	}

	@Override
	public void onClick(View v) {
		this.finish();	
	}

}
