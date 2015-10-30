package com.rongdai.activity.personalcenter;

import com.rongdai.R;
import com.rongdai.R.id;
import com.rongdai.R.layout;
import com.rongdai.pager.PersonCenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class PayDetailedActivity extends Activity implements OnClickListener {
	private TextView paydetailed_textview;
	private Button next_button;
	
	private ImageButton MyBankCard_back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_detailed);
		
		 String html="<html><body>"  
	                +"<p>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 为保障您账户资金的安全性，每个用户只能绑定一张银行卡作为快捷卡（以下称为“快捷卡”）。通过网站进行网银充值不受此限制，仍可使用多张银行卡。一旦绑定快捷卡后，只可以提现到快捷卡。" 
	                +"<br><br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 如之前绑定的提现银行卡中，已包含快捷卡，不需要重新绑定；如不包含快捷卡，需重新绑定快捷卡作为提现卡，其他绑定的提现卡将失效。提现申请时选择非快捷卡，提现将失败。" +
	                "<br><br>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp 绑定快捷卡后，如需解除绑定或者更换绑定其他银行卡，请联系融投贷客服。" +
	                "</p></body></html>";  
		 
		paydetailed_textview = (TextView)findViewById(R.id.paydetailed_textview);
		
		paydetailed_textview.setText(Html.fromHtml(html));  
		
		MyBankCard_back = (ImageButton)findViewById(R.id.MyBankCard_back);
		MyBankCard_back.setOnClickListener(this);
		
		next_button = (Button)findViewById(R.id.next_button);
		next_button.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {
		case R.id.next_button:
			intent = new Intent(this,PayActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			this.startActivity(intent);
			break;
		case R.id.MyBankCard_back:
		     this.finish();
			break;

		default:
			break;
		}
	}
	
	
}
