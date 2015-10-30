package com.rongdai.activity.personalcenter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;







import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.R;
import com.rongdai.R.id;
import com.rongdai.R.layout;
import com.rongdai.base.BaseActivity;
import com.rongdai.domain.LoginInfo;
import com.rongdai.domain.ModifyLoginPwdInfo;
import com.rongdai.domain.investAndEarn;
import com.rongdai.utils.Constants;
import com.rongdai.utils.CustomProgressDialog;
import com.rongdai.utils.PersonInfoConstans;
import com.rongdai.utils.ToastUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class EditPasswordActivity extends BaseActivity   implements OnClickListener  {
	private ImageButton editpassword_back;
	private Button changepassword_button;
	private CustomProgressDialog progressDialog = null;
	private EditText old_password_EditText,new_password_EditText,again_password_EditText;
	
	public void initData() {
		super.initData();
		setContentView(R.layout.activity_edit_password);
	
		
	/*    progressDialog=new CustomProgressDialog(this);
			progressDialog.setCancelable(false);*/

		editpassword_back = (ImageButton)findViewById(R.id.editpassword_back);
		editpassword_back.setOnClickListener(this);
	
		changepassword_button = (Button)findViewById(R.id.changepassword_button);
		changepassword_button.setOnClickListener(this);
		
		old_password_EditText = (EditText)findViewById(R.id.old_password_EditText);
		new_password_EditText= (EditText)findViewById(R.id.new_password_EditText);		
		again_password_EditText= (EditText)findViewById(R.id.again_password_EditText);
	}
	
	
	private void ModifyLoginPwdDo() {
		
		final SharedPreferences sp = getSharedPreferences(
				PersonInfoConstans.PERSONINFO, Context.MODE_PRIVATE);
		String rongdai_account = sp.getString(PersonInfoConstans.RONGDAI_ACCOUNT, "");	
		String rongdai_password = sp.getString(PersonInfoConstans.PASSWORD, "");	
		final String old_password = old_password_EditText.getText().toString().trim();
		final String new_password = new_password_EditText.getText().toString().trim();
		final String again_password = again_password_EditText.getText().toString().trim();
		
		if(!rongdai_password.equals(old_password)){
			
			 ToastUtils.show(EditPasswordActivity.this, "旧密码不正确");
			
		}else if(new_password.equals("")||again_password.equals("")){
			 ToastUtils.show(EditPasswordActivity.this, "密码不能为空");
		}else if(!new_password.equals(again_password)){
			 ToastUtils.show(EditPasswordActivity.this, "两次密码输入不同");
		}else{
		
		
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();	
		params.addBodyParameter("rongdaiAccount", rongdai_account);
		params.addBodyParameter("password", new_password);
		httpUtils.send(HttpMethod.POST, Constants.ModifyLoginPwdInfo, params,
				new RequestCallBack<String>() {
					
					public void onFailure(HttpException arg0, String arg1) {
						 Log.e("onFailure", "onFailure");
					}
				
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						//System.out.println(json);
						Log.e("onSuccess", info.result);
						Gson gson = new Gson();						
						ModifyLoginPwdInfo modifyLoginPwdInfo = gson.fromJson(json, ModifyLoginPwdInfo.class);
                        Log.e("message", modifyLoginPwdInfo.message);
                        if(modifyLoginPwdInfo.message.equals("修改失败")){
                        	ToastUtils.show(EditPasswordActivity.this, "修改失败");
                        }else{
                        	 Editor editor = sp.edit();
     						editor.putString(PersonInfoConstans.PASSWORD, new_password);
     						editor.commit();
                            ToastUtils.show(EditPasswordActivity.this, "修改成功");
     						finish();
                        	
                        }

                     
                        /*queryBalance_textview.setText("￥"+investAndEarn.data.canUseBal);*/
						
						
					}
				});
		}
	}
	

	@Override
	public void onClick(View view) {
		Intent intent ;
		switch (view.getId()) {	
		case R.id.editpassword_back:
			this.finish();
			break;
		case R.id.changepassword_button:
			ModifyLoginPwdDo();
			/* soap thread = new soap();  
			 thread.wait.start();  */
			
			break;
		

		default:
			break;
		}
	}
	
	/*//�߳̿�ʼ
	 
		 Handler handlers = new Handler() {    		 		
			 @Override 
				public void handleMessage(Message msg) {
		                 switch (msg.what) {
		                 case 1:                                        
		                                     
		                	 progressDialog.show();
		                	 ModifyLoginPwdDo();
		                	 break;
		  
		                 case 2:// ����  

		                	 progressDialog.dismiss();
		                	

		                	
		                     break;
		                 }
		         }
		 };
		 
		 
		 private class soap implements Runnable {
				
				Thread read ,wait;
				soap(){					
					wait = new Thread(this);
					read = new Thread(this);
				}				
				 @Override 
				 public void run() {					 
					 if(Thread.currentThread()==wait)
					 {						 
						 read.start();
						 while(read.isAlive()==false){
							
						 }
					 try {
						read.join();
						
					} catch (InterruptedException e) {
						
					}
					 try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							
						}
					 
					 Message msg = new Message();  
					 msg.what = 2;// ��ʼ
					 handlers.sendMessage(msg); 
					 
					 }
					 else if(Thread.currentThread()==read){
						
						 Message msg = new Message();  
						 msg.what = 1;// ��ʼ��ȡ
						 handlers.sendMessage(msg);
						 try {
								Thread.sleep(500);	
								
							} catch (InterruptedException e) {
								
							}
						 
					
						
						 }
					 
					 
					 HttpUtils httpUtils = new HttpUtils();
						RequestParams params = new RequestParams();	
						params.addBodyParameter("rongDaiAccount", "63053704092");
						params.addBodyParameter("password", "6000060000834209");
						httpUtils.send(HttpMethod.POST, Constants.ModifyLoginPwdInfo, params,
								new RequestCallBack<String>() {
							@Override
									public void onFailure(HttpException arg0, String arg1) {
										 Log.e("onFailure", "onFailure");
									}
									@Override
									public void onSuccess(ResponseInfo<String> info) {
										String json = info.result;
										//System.out.println(json);
										ModifyLoginPwdInfo modifyLoginPwdInfo=null;
										try {
											Gson gson = new Gson();
											 modifyLoginPwdInfo = gson.fromJson(json, ModifyLoginPwdInfo.class);
										} catch (JsonSyntaxException e) {
											e.printStackTrace();
										}
										
				                        Log.e("message", modifyLoginPwdInfo.message);
										queryBalance_textview.setText("￥"+investAndEarn.data.canUseBal);
										
										
									}
								});
					

					
					 
					 }

					
				}
*/
	
	
	
}
