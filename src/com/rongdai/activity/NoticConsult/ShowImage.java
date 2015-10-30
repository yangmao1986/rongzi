package com.rongdai.activity.NoticConsult;

import com.rongdai.R;
import com.rongdai.utils.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowImage extends Activity implements OnClickListener {
	private WebView wv;
	private TextView tv_title;
	private ImageView ib_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_image);
		Intent intent = getIntent();
		String newsId = intent.getStringExtra("newsId");
		wv = (WebView) findViewById(R.id.wv);
		tv_title = (TextView) findViewById(R.id.tv_title);
		ib_back = (ImageView) findViewById(R.id.ib_back);
		
		tv_title.setText("新闻资讯");
		wv.getSettings().setUseWideViewPort(true);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		wv.requestFocus();
//		wv.loadUrl("http://www.eoeandroid.com/forum-208-1.html");
		wv.getSettings().setSupportZoom(true);
		wv.getSettings().setBuiltInZoomControls(true);
		wv.loadUrl(newsId);
		
		ib_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		this.finish();
	}
}
