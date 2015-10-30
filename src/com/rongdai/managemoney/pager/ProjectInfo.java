package com.rongdai.managemoney.pager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Date;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.rongdai.R;
import com.rongdai.base.BaseApplication;
import com.rongdai.base.BasePager;
import com.rongdai.constants.ConfigConstants;
import com.rongdai.domain.ProjectInfoBean;
import com.rongdai.domain.ProjectInfoBean.projectInfoData;
import com.rongdai.utils.ConfigFileUtils;
import com.rongdai.utils.Constants;
import com.rongdai.utils.UTFDecoder;
import com.rongdai.view.PullToRefreshView;

public class ProjectInfo extends BasePager {
	private View view;
	/**
	 * 借款类型
	 */
	private TextView loanTextView;
	/**
	 * 性别
	 */
	private TextView sexTextView;
	/**
	 * 年龄
	 */
	private TextView ageTextView;
	/**
	 * 婚姻状况
	 */
	private TextView marriedTextView;
	/**
	 * 文化程度
	 */
	private TextView cultureTextView;
	/**
	 * 户籍
	 */
	private TextView familyTextView;
	/**
	 * 借款描述
	 */
	private TextView descriptionTextView;
	/**
	 * 工作年限
	 */
	private TextView worktimeTextView;
	/**
	 * 商业保险
	 */
	private TextView insuranceTextView;
	/**
	 * 职业
	 */
	private TextView workTextView;
	/**
	 * 社保年限
	 */
	private TextView timeTextView;
	/**
	 * 职称
	 */
	private TextView job_titleTextView;
	/**
	 * 房产
	 */
	private TextView houseTextView;
	/**
	 * 长期居住地
	 */
	private TextView localTextView;
	/**
	 * 年收入
	 */
	private TextView incomeTextView;
	/**
	 * 贷款记录
	 */
	private TextView recordTextView;
	/**
	 * 信用卡额度
	 */
	private TextView blueCardTextView;
	/**
	 * 征信报告
	 */
	private TextView reportTextView;
	/**
	 * 成功借入
	 */
	private TextView successTextView;
	/**
	 * 已还款数
	 */
	private TextView repaymentTextView;
	/**
	 * 待还款数
	 */
	private TextView repaymentNumberTextView;
	/**
	 * 逾期未还
	 */
	private TextView noGetTextView;
	/**
	 * 项目名称
	 */
	private TextView projectNameTextView;
	/**
	 * 本期融资金额
	 */
	private TextView timeMoneyTextView;
	/**
	 * 最高授信额度
	 */
	private TextView reputationTextView;
	/**
	 * 还款日期
	 */
	private TextView getTimeTextView;
	/**
	 * 年华利率
	 */
	private TextView lixiTextView;
	/**
	 * 资金用途
	 */
	private TextView yongtuTextView;
	/**
	 * 投标截止时间
	 */
	private TextView stopTimeTextView;
	/**
	 * 企业情况
	 */
//	private TextView companyCaseTextView;
	/**
	 * 项目id
	 */
	private String projectId;
	/**
	 * 访问页面
	 */
	private int pagerNumber = 1;
	private projectInfoData projectInfoData;
	private ProjectInfoBean projectInfoBean;
	private int HEADER = 0;
	private int refreshState = 0;
	private PullToRefreshView mPullToRefreshView;
	private String descriptionText;
	private String proSituation;
	
	public ProjectInfo(Context mContext) {
		super(mContext);
	}

	@Override
	public View initView(Context context) {
		view = View.inflate(BaseApplication.getContext(),
				R.layout.activity_project_detail_info, null);
		loanTextView = (TextView) view
				.findViewById(R.id.project_detail_info_loan);
		sexTextView = (TextView) view
				.findViewById(R.id.project_detail_info_sex);
		ageTextView = (TextView) view
				.findViewById(R.id.project_detail_info_age);
		marriedTextView = (TextView) view
				.findViewById(R.id.project_detail_info_married);
		cultureTextView = (TextView) view
				.findViewById(R.id.project_detail_info_culture);

		familyTextView = (TextView) view
				.findViewById(R.id.project_detail_info_family);
		descriptionTextView = (TextView) view
				.findViewById(R.id.project_detail_info_description);
		worktimeTextView = (TextView) view
				.findViewById(R.id.project_detail_info_worktime);
		insuranceTextView = (TextView) view
				.findViewById(R.id.project_detail_info_insurance);
		workTextView = (TextView) view
				.findViewById(R.id.project_detail_info_work);

		timeTextView = (TextView) view
				.findViewById(R.id.project_detail_info_time);
		job_titleTextView = (TextView) view
				.findViewById(R.id.project_detail_info_job_title);
		houseTextView = (TextView) view
				.findViewById(R.id.project_detail_info_house);
		localTextView = (TextView) view
				.findViewById(R.id.project_detail_info_local);
		incomeTextView = (TextView) view
				.findViewById(R.id.project_detail_info_income);

		recordTextView = (TextView) view
				.findViewById(R.id.project_detail_info_record);
		blueCardTextView = (TextView) view
				.findViewById(R.id.project_detail_info_blue_card);
		reportTextView = (TextView) view
				.findViewById(R.id.project_detail_info_report);
		successTextView = (TextView) view
				.findViewById(R.id.project_detail_info_success);
		repaymentTextView = (TextView) view
				.findViewById(R.id.project_detail_info_repayment);

		repaymentNumberTextView = (TextView) view
				.findViewById(R.id.project_detail_info_repayment_number);
		noGetTextView = (TextView) view
				.findViewById(R.id.project_detail_info_no_get);
		projectNameTextView = (TextView) view
				.findViewById(R.id.project_detail_info_project_name);
		timeMoneyTextView = (TextView) view
				.findViewById(R.id.project_detail_info_time_money);
		reputationTextView = (TextView) view
				.findViewById(R.id.project_detail_info_reputation);

		getTimeTextView = (TextView) view
				.findViewById(R.id.project_detail_info_get_time);
		lixiTextView = (TextView) view
				.findViewById(R.id.project_detail_info_lixi);
		yongtuTextView = (TextView) view
				.findViewById(R.id.project_detail_info_yongtu);
		stopTimeTextView = (TextView) view
				.findViewById(R.id.project_detail_info_stop_time);
//		companyCaseTextView = (TextView) view
//				.findViewById(R.id.project_detail_info_company_case);

		return view;
	}

	/**
	 * 找控件并且初始话界面
	 */
	@Override
	public void initData() {
		/** 如果配置文件中有内容就直接去解析，如果没有就去访问网络 */
		String json = ConfigFileUtils.getValue(
				ConfigConstants.PROJECTDETAILPAGER,
				ConfigConstants.PROJECTINFO, "");
		if ("".equals(json)) {
			getDataFromNet(refreshState,new PullToRefreshView(BaseApplication.getContext()));
		} else {
			processData(json);
		}
	}

	/**
	 * 从网络获取项目信息数据
	 */
	public void getDataFromNet(int refreshState,PullToRefreshView mPullToRefreshView) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configCurrentHttpCacheExpiry(0);
		httpUtils.configDefaultHttpCacheExpiry(0);
		RequestParams params = new RequestParams();
		this.refreshState = refreshState;
		this.mPullToRefreshView = mPullToRefreshView;
		params.addQueryStringParameter("sort", "1");
		params.addQueryStringParameter("pageNo", pagerNumber+"");
		params.addQueryStringParameter("borrowId", getProjectId());
		httpUtils.send(HttpMethod.GET, Constants.pojectDetail, params,
				new RequestCallBack<String>() {
					/** 访问失败 */
					@Override
					public void onFailure(HttpException arg0, String arg1) {

					}

					/** 访问成功 */
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
//						//System.out.println(responseInfo.result);
						/** 将网络数据放到缓存中 */
						ConfigFileUtils.setValue(
								ConfigConstants.PROJECTDETAILPAGER,
								ConfigConstants.PROJECTINFO,
								responseInfo.result);
						processData(responseInfo.result);
					}
				});
	}

	/**
	 * 解析网络数据
	 * 
	 * @param json
	 */
	protected void processData(String json) {
		Gson gson = new Gson();
		try {
			if(refreshState == 1){
				SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
				Date date = new Date(System.currentTimeMillis());
				String time = format.format(date);
				mPullToRefreshView.onHeaderRefreshComplete(time);
				refreshState = 0;
				Toast.makeText(BaseApplication.getContext(), "刷新成功", 0).show();
			}else if(refreshState == 2){
				mPullToRefreshView.onFooterRefreshComplete();
				refreshState = 0;
				Toast.makeText(BaseApplication.getContext(),"没有更多了",0).show();
				return ;
			}
			projectInfoBean = gson.fromJson(json, ProjectInfoBean.class);
//			//System.out.println("projectInfoBean"
//					+ projectInfoBean.data.borrowAccount);
			projectInfoData = projectInfoBean.data;
			showInfo();
		} catch (Exception e) {
//			getDataFromNet(refreshState, mPullToRefreshView);
		}
	}

	/**
	 * 展示项目的基本信息
	 */
	private void showInfo() {
		loanTextView.setText(projectInfoData.borrowType);
		sexTextView.setText(projectInfoData.sex);
		ageTextView.setText(projectInfoData.age);
		marriedTextView.setText(projectInfoData.isMarry);
		cultureTextView.setText(projectInfoData.knowledgeLevel);
		familyTextView.setText(projectInfoData.birthAddress);
		
		descriptionText = projectInfoData.bmDesc.replace("％","%");
		UTFDecoder decoder = new UTFDecoder();
		descriptionText = decoder.UTFDecoder(descriptionText);
		descriptionTextView.setText(Html.fromHtml(descriptionText));
		
		worktimeTextView.setText(projectInfoData.workTime);
		insuranceTextView.setText(projectInfoData.commerialInsurance);
		workTextView.setText(projectInfoData.profession);
		timeTextView.setText(projectInfoData.socialSecurityTime);
		job_titleTextView.setText(projectInfoData.title);
		houseTextView.setText(projectInfoData.houseProperty);
		localTextView.setText(projectInfoData.longTimeLiveAddress);
		incomeTextView.setText(projectInfoData.yearIncome);
		// 贷款记录
		// sexTextView.setText(projectInfoData.);
		blueCardTextView.setText(projectInfoData.creditCardAccountLevel);
		reportTextView.setText(projectInfoData.creditInformationReports);
		successTextView.setText(projectInfoData.successLoanCount);
		repaymentTextView.setText(projectInfoData.alreayPaymentCount);
		repaymentNumberTextView.setText(projectInfoData.waitingPayment);
		// 逾期未还
		// blueCardTextView.setText(projectInfoData.);
		projectNameTextView.setText(projectInfoData.borrowName);
		timeMoneyTextView.setText(projectInfoData.borrowAccount);
		reputationTextView.setText(projectInfoData.borrowHighLevel);
		getTimeTextView.setText(projectInfoData.borrowPayDate);
		lixiTextView.setText(projectInfoData.borrowYearRate);
		yongtuTextView.setText(projectInfoData.borrowUse);
		stopTimeTextView.setText(projectInfoData.borrowEndTime);
		
		proSituation = projectInfoData.borrowProSituation.replace("％", "%");
		UTFDecoder decoderp = new UTFDecoder();
		proSituation = decoderp.UTFDecoder(proSituation);
//		//System.out.println("项目环境。。。。。。。。"+proSituation);
//			companyCaseTextView.setText(Html.fromHtml(proSituation));
	}

	/**
	 * 获取项目id
	 * 
	 * @return 返回项目id
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * 设置项目id
	 * 
	 * @param projectId
	 *            项目id
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
}
