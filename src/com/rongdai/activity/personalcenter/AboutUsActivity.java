package com.rongdai.activity.personalcenter;

import com.rongdai.R;
import com.rongdai.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class AboutUsActivity extends Activity implements OnClickListener {

	private TextView aboutus_textview;
	
	private ImageButton about_us_back;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		
		 String html="<html><body>"  
	                +"<p>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp “融投贷”<font color=\"#28b8d2\">（www.rongtoudai.cn）</font>是河北省青年创业促进会发起的，河北安凯投资有限公司运营的P2P"
	                +"网贷平台。河北省青年创业促进会是由河北省工商联合会主管，经省民政厅批准成立的非营利组织河北安凯投资有限公司成立于2013年11月，注册资本1000万元。"  
	                +"<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 理念：信用创造价值。<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 经营原则：诚信经营、风控至上、专注服务、创新不止。<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 核心价值观：规范、激情、专业、尊重。" +
	                "<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 人才理念：人尽其才，共享成功。<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 企业愿景：助您成功的综合金融服务商。<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 企业愿景：助您成功的综合金融服务商。<br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 企业使命：融希望、投梦想、贷未来 即融汇投资者的希望，投出创业者的梦想，贷出双方的精彩未来。" +
	                "<br><font color=\"#888888\">官方网网址：</font><font color=\"#ff8400\">www.rongtoudai.cn</font>" +
	                "<br><font color=\"#888888\">微信服务号：</font><font color=\"#ff8400\">rongtoudai</font>" +
	                "<br><font color=\"#888888\">客服QQ群：</font><font color=\"#ff8400\">304932554</font>" +
	                "<br><font color=\"#888888\">理财热线：</font><font color=\"#ff8400\">400-869-0311</font></p></body></html>";  
		
		aboutus_textview = (TextView)findViewById(R.id.aboutus_textview);
		
		aboutus_textview.setText(Html.fromHtml(html));    
		
		about_us_back = (ImageButton)findViewById(R.id.about_us_back);
		about_us_back.setOnClickListener(this);
		
	}
	


	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {	
		case R.id.about_us_back:
			this.finish();
			break;
		default:
			break;
		}
	}
	
	
	
}
