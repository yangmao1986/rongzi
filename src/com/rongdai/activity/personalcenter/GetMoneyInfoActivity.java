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

public class GetMoneyInfoActivity extends Activity  implements OnClickListener {
	private TextView paydetailed_textview;
	private ImageButton MyBankCard_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_money_info);
		
		 String html="<html><body>"  
	                +"<p>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 1.身份认证、账户托管开通、提现银行设置均成功后，才能进行提现操作；" 
	                +"<br><br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 2.提现费用为每笔2元；" +
	                "<br><br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 3.因融投贷无法触及用户资金账户，无法收取用户任何费用，为防止套现，所充资金必须经投标回款后才能提现；" +
	                "<br><br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 4.资金到达用户账户的当日（包括周六/周日/法定节假日）即可发起提现申请；" +
	                "<br><br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 5.工作日（周六/周日/法定节假日均除外）21:00之前申请的提现，当日审核完毕并预计于下一工作日12:00-16:00到账， 否则将于下一工作日审核并预计于再下一工作日12:00-16:00到账，实际到账时间依据账户托管方（第三方支付平台）及提现银行而有所差异。" +
	                "</p></body></html>";  
		 
		paydetailed_textview = (TextView)findViewById(R.id.paydetailed_textview);
		
		paydetailed_textview.setText(Html.fromHtml(html));  
		
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
