package com.rongdai.activity.personalcenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.R;
import com.rongdai.base.BaseActivity;
import com.rongdai.domain.MessageData;
import com.rongdai.domain.RegistInfo;
import com.rongdai.utils.Constants;
import com.rongdai.utils.PersonInfoConstans;
import com.rongdai.utils.ToastUtils;
import com.rongdai.utils.UserInfoUtils;
import com.rongdai.utils.XUtilsConstans;

/**
 * 注册界面
 * 
 * @author Administrator
 * 
 */
public class RegistActivity extends BaseActivity implements OnClickListener {
	/**
	 * 返回键
	 */
	private ImageView regist_back;
	/**
	 * 用户名
	 */
	private EditText regist_username;
	/**
	 * 密码
	 */
	private EditText regist_psw;
	/**
	 * 确认密码
	 */
	private EditText regist_repsw;
	/**
	 * 电话号码
	 */
	private EditText regist_phone;
	/**
	 * 验证码
	 */
	private EditText regist_verification;
	/**
	 * 获取验证码
	 */
	private Button regist_getverification;
	/**
	 * 注册
	 */
	private Button regist;
	private int time = 60;
	private Timer timer = new Timer();
	private TimerTask task;
	private ImageView regist_headimage;
	private PopupWindow popupWindow;
	private View allView;
	private Button picture;
	private Button back;
	private Button camera;
	private Bitmap bitmap;
	private String imgPath;
	/**
	 * 打开相机 拍摄照片的名字
	 */
	private String cameraPicName;
	/**
	 * 照相机拍摄照片的路径
	 */
	private Uri imageUri;
	private Button openlater;
	private Button goopen;
	private Dialog dialog;
	private Uri mUri;
	private Uri photoFile;
	private String userName;
	private String password;
	private String phone;
	private String verification;
	/**
	 * 融投贷注册协议
	 */
	private TextView regist_agreement;
	private MessageData messageData;
	
	/**
	 * 是否上传头像标记
	 */
	private String photoimage="0";

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		setContentView(R.layout.activity_regist);
	
		regist_back = (ImageView) findViewById(R.id.regist_back);
		regist_headimage = (ImageView) findViewById(R.id.regist_headimage);
		regist_username = (EditText) findViewById(R.id.regist_username);
		regist_psw = (EditText) findViewById(R.id.regist_psw);
		regist_repsw = (EditText) findViewById(R.id.regist_repsw);
		regist_phone = (EditText) findViewById(R.id.regist_phone);
		regist_verification = (EditText) findViewById(R.id.regist_verification);
		regist_getverification = (Button) findViewById(R.id.regist_getverification);
		regist = (Button) findViewById(R.id.regist);
		userName = regist_username.getText().toString().trim();
		password = regist_psw.getText().toString().trim();
		phone = regist_phone.getText().toString().trim();
		verification = regist_verification.getText().toString().trim();
		regist_agreement = (TextView) findViewById(R.id.regist_agreement);
		
		
		regist_back.setOnClickListener(this);
		regist_getverification.setOnClickListener(this);
		regist.setOnClickListener(this);
		regist_headimage.setOnClickListener(this);
		regist_agreement.setOnClickListener(this);
		
		allView = View.inflate(this, R.layout.activity_regist, null);
		allView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				dismissPopupWindow();
				return false;
			}
		});
	}

	@Override
	public void onClick(View view) {
		Intent intent;
		
		switch (view.getId()) {
		case R.id.regist_back:
			this.finish();
			break;
		case R.id.regist_getverification:
			boolean b = getVerificationFromNet();
			if(!b){
				return ;
			}
			regist_getverification.setEnabled(false);
			regist_getverification
					.setBackgroundResource(R.drawable.getverification_wait);
			regist_getverification.setText("");
			task = new TimerTask() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (time <= 0) {
								regist_getverification.setEnabled(true);
								regist_getverification
										.setBackgroundResource(R.drawable.get_verification_bg);
								regist_getverification.setText("获取验证码");
								regist_getverification.setTextSize(14);
								regist_getverification
										.setTextColor(Color.WHITE);
								task.cancel();
							} else {
								regist_getverification.setText(time + "秒");
								regist_getverification.setTextSize(20);
							}
							time--;
						}
					});
				}
			};
			time = 60;
			timer.schedule(task, 0, 1000);
			break;
		case R.id.regist:
			verification();
			// showDialog();
			break;
		case R.id.regist_headimage:
			WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			int height = manager.getDefaultDisplay().getHeight();
			int width = manager.getDefaultDisplay().getWidth();
			if (popupWindow == null || !popupWindow.isShowing()) {
				View contentView = View.inflate(this, R.layout.popupwindow,
						null);
				picture = (Button) contentView.findViewById(R.id.picture);
				camera = (Button) contentView.findViewById(R.id.camera);
				back = (Button) contentView.findViewById(R.id.back);
				picture.setOnClickListener(this);
				camera.setOnClickListener(this);
				back.setOnClickListener(this);
				popupWindow = new PopupWindow(contentView);
				popupWindow.setWidth(width);
				popupWindow.setHeight(height / 3);// 设置弹出框大小
				popupWindow.showAsDropDown(allView, 0, height / 3 * 2);
				popupWindow.setOutsideTouchable(true);
				break;
			}
		case R.id.picture:
			dismissPopupWindow();
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/*");
			intent.putExtra("crop", "true");
			photoFile= Uri.fromFile(new File(Environment
					.getExternalStorageDirectory() + "/Images/", "cameraImg"
					+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile);
			startActivityForResult(intent, 2);

			break;
		case R.id.camera:
			dismissPopupWindow();
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
				Toast.makeText(this, "SD卡不存在,请检查SD卡", Toast.LENGTH_SHORT).show();
				return;
			}
			intent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/Images");
			if (!file.exists()) {
				file.mkdirs();
			}
			mUri = Uri.fromFile(new File(Environment
					.getExternalStorageDirectory() + "/Images/", "cameraImg"
					+ String.valueOf(System.currentTimeMillis()) + ".jpg"));
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, 1);

			break;
		case R.id.back:
			dismissPopupWindow();
			break;
		case R.id.regist_agreement:
			intent = new Intent(this,RegistAgreementActivity.class);
			startActivity(intent);
			break;
		}
	}

	/**
	 * 验证 密码和确认密码是否相同 手机号是否合法 验证码是否正确
	 */
	private void verification() {
		// 用户名
		if (TextUtils.isEmpty(regist_username.getText().toString().trim())
				|| TextUtils.isEmpty(regist_repsw.getText().toString().trim())) {
			ToastUtils.show(RegistActivity.this, "用户名不能为空");
			return;
		}
		// 密码和确认密码
		if (TextUtils.isEmpty(regist_psw.getText().toString().trim())
				|| TextUtils.isEmpty(regist_repsw.getText().toString().trim())) {
			ToastUtils.show(RegistActivity.this, "确认密码或者密码不能为空");
			return;
		}
		if(!regist_psw.getText().toString().trim().equals(regist_repsw.getText().toString().trim())){
			ToastUtils.show(RegistActivity.this, "确认密码和密码不相同");
			return;
		}
		//电话号码的合法性
		Pattern p = Pattern
				.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(177)|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
		boolean ismatch = p.matcher(regist_phone.getText().toString().trim()).matches();
		if(!ismatch){
			ToastUtils.show(RegistActivity.this, "电话号码不正确");
			return;
		}
		
		//验证码是否正确
		if(TextUtils.isEmpty(regist_verification.getText().toString().trim())){
			Toast.makeText(this, "请输入验证码", 0).show();
			return ;
		}
		if(!regist_verification.getText().toString().trim().equals(messageData.code)){
			Toast.makeText(this, "验证码错误", 0).show();
			return ;
		}
		
		//去注册
		RegistByNet();
	}

	/**
	 * 获取验证码
	 */
	private boolean getVerificationFromNet() {
		//电话号码的合法性
		Pattern p = Pattern
				.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(177)|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
		boolean ismatch = p.matcher(regist_phone.getText().toString().trim()).matches();
		if(!ismatch){
			ToastUtils.show(RegistActivity.this, "手机号码不正确");
			return false;
		}
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		params.addBodyParameter("mobile", regist_phone.getText().toString().trim());
		httpUtils.send(HttpMethod.POST, Constants.getMobileCode, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Log.e("MobileCode", responseInfo.result);
						//System.out.println("获取到的手机验证码"+responseInfo.result);
						processMessageData(responseInfo.result);
					}
				});
		return true;

	}

	/**
	 * 获取短信验证码数据
	 */
	protected void processMessageData(String json) {
		Gson gson = new Gson();
		try {
			messageData = gson.fromJson(json, MessageData.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 注册
	 */
	private void RegistByNet() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		userName = regist_username.getText().toString().trim();
		password = regist_psw.getText().toString().trim();
		phone = regist_phone.getText().toString().trim();
		
		params.addBodyParameter("name", userName);
		params.addBodyParameter("password", password);
		params.addBodyParameter("phone", phone);
		if(photoimage.equals("1")){
		params.addBodyParameter("uploadFile", new File(photoFile.toString().replace("file://", "")));
		}
		httpUtils.send(HttpMethod.POST, Constants.register, params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						//System.out.println("注册失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> info) {
						String json = info.result;
						//System.out.println(json);
						Gson gson = new Gson();
						RegistInfo registInfo = gson.fromJson(json, RegistInfo.class);
						String message = registInfo.message;
						if("注册成功".equals(message)){
							// 注册成功后 把之前的 sp存储的用户手势密码删除(如果本手机存在之前的用户信息)
							SharedPreferences sp = getSharedPreferences(
									PersonInfoConstans.PERSONINFO,
									Context.MODE_PRIVATE);
							Editor editor = sp.edit();
							editor.putString(PersonInfoConstans.HNADPASSWORD, "");
							//存储用户信息
							editor.putString(PersonInfoConstans.USER_NAME, userName);
							editor.putString(PersonInfoConstans.PASSWORD, password);
							editor.putString(PersonInfoConstans.PHONE, phone);
							editor.commit();
							
							//注册成功后进入登陆界面
							RegistActivity.this.finish();
						}else{
							ToastUtils.show(RegistActivity.this, message);
							return;
						}
					}
				});

	}

	/**
	 * 关闭当前界面的弹出窗体
	 */
	private void dismissPopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	// 点击头像 打开 相机或者相册时 设置头像
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			switch (resultCode) {
			case Activity.RESULT_OK:
				ContentResolver cr = this.getContentResolver();
				try {
					Bitmap cameraBitmap = BitmapFactory.decodeStream(cr
							.openInputStream(mUri));
					if (cameraBitmap != null) {
						regist_headimage.setImageBitmap(cameraBitmap);
						//postHeadImage(new File(mUri.toString().replace("file://", "")));
						photoFile=mUri;
						photoimage="1";
						
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case Activity.RESULT_CANCELED:
				break;
			}
		} else if (requestCode == 2) {
			switch (resultCode) {
			case Activity.RESULT_OK:

				ContentResolver cr = this.getContentResolver();
				try {
					Bitmap cameraBitmap = BitmapFactory.decodeStream(cr
							.openInputStream(photoFile));
					if (cameraBitmap != null) {
						regist_headimage.setImageBitmap(cameraBitmap);
						//postHeadImage(new File(photoFile.toString().replace("file://", "")));
						photoimage="1";
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/*	
				bitmap = (Bitmap) data.getExtras().get("data");
				if (bitmap != null) {
					regist_headimage.setImageBitmap(bitmap);
				}
				Uri uri = null;
				if (data.getData() != null) {
					
					uri = data.getData();
				} else {
					uri = photoFile;
							uri =	Uri.parse(MediaStore.Images.Media.insertImage(
							getContentResolver(), bitmap, "", ""));
				}*/
				
				
				/*String[] proj = { MediaStore.Images.Media.DATA };
				@SuppressWarnings("deprecation")
				Cursor cursor = managedQuery(uri, proj, null, null, null);
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToFirst();
					imgPath = cursor.getString(column_index);
					postHeadImage(new File(imgPath));
				}*/
				
				
			case Activity.RESULT_CANCELED:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 上传头像
	 * 
	 * @param file
	 */
	private void postHeadImage(File file) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		String imageName = file.toString().substring(file.toString().lastIndexOf("/"), file.toString().length());
		//System.out.println(imageName);
		RequestParams params = new RequestParams();
		params.addBodyParameter("rongDaiAccount", UserInfoUtils.getRongdaiAccount(this));
		params.addBodyParameter("uploadFile", file);
		params.addBodyParameter("uploadFileFileName", ".jpg");
		httpUtils.send(HttpMethod.POST, Constants.modifyHeadImg, params, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ResponseInfo<String> info) {
				//System.out.println(info.result);
				
			}
		});
	}
}
