package com.rongdai.utils;

import android.net.Uri;

public class Constants {
	// 正式接口 182.92.107.95:8080
	// 测试接口192.168.0.208
	/**
	 * 17.汇付天下的web页面连接
	 */
	// 测试
	// public static final String tendrweb =
	// "http://mertest.chinapnr.com/muser/publicRequests";
	// 正式
	public static final String tendrweb = "https://lab.chinapnr.com/muser/publicRequests";

	/**
	 * 1.获得推荐借款项目
	 */
	public static final String getRecommendPoject = "http://182.92.107.95:8080/rongdai/zero/getRecommendPoject_MoneyManagement.do";
	/**
	 * 2.获取借款项目详情(之一:项目信息 sort必须为1。之二:还款模型 sort必须为2。4.之三:相关资料 sort必须为3。之四:投标情况
	 * sort必须为4)
	 */
	public static final String pojectDetail = "http://182.92.107.95:8080/rongdai/zero/pojectDetail_MoneyManagement.do";
	/**
	 * 3.其他项目信息
	 */
	public static final String getOtherProject = "http://182.92.107.95:8080/rongdai/zero/getOtherProject_MoneyManagement.do";
	/**
	 * 融影宝
	 */
	public static final String getRongyingbaoPoject = "http://182.92.107.95:8080/rongdai/zero/getRongyingbaoPoject_MoneyManagement.do";
	/**
	 * 4.每个种类的项目信息
	 */
	public static final String getAreaProject = "http://182.92.107.95:8080/rongdai/zero/getAreaProject_MoneyManagement.do";
	/**
	 * 5.马上投资
	 */
	public static final String Investment = "http://182.92.107.95:8080/rongdai/zero/investment_PersonalCenter.do";
	/**
	 * 6.获取贷款种类信息
	 */
	public static final String loanProjectList = "http://182.92.107.95:8080/rongdai/zero/loanProjectList_Loan.do";
	/**
	 * 7.我要贷款
	 */
	public static final String loanProject = "http://182.92.107.95:8080/rongdai/zero/loanProject_Loan.do";
	/**
	 * 8．网站公告/行业资讯
	 */
	public static final String getWebNotice = "http://182.92.107.95:8080/rongdai/zero/getWebNotice_Information.do";
	/**
	 * 9．资讯详情
	 */
	public static final String getNoticeDetail = "http://182.92.107.95:8080/rongdai/zero/getNoticeDetail_Information.do";
	/**
	 * 10．评论资讯
	 */
	public static final String replyNews = "http://182.92.107.95:8080/rongdai/zero/replyNews_Information.do";
	/**
	 * 11.融贷账户注册
	 */
	public static final String register = "http://182.92.107.95:8080/rongdai/zero/registerRondai_PersonalCenter.do";
	/**
	 * 12.首页图片轮播
	 */
	public static final String picChange = "http://182.92.107.95:8080/rongdai/zero/picChange_MoneyManagement.do";
	/**
	 * 14.登录接口
	 */
	public static final String rongDaiLogin = "http://182.92.107.95:8080/rongdai/zero/rongDaiLogin_PersonalCenter.do";
	/**
	 * 15.汇付天下注册接口
	 */
	public static final String registerHuiFu = "http://182.92.107.95:8080/rongdai/zero/registerHuiFu_PersonalCenter.do";
	/**
	 * 16.到时提醒
	 */
	public static final String sendMessage = "http://182.92.107.95:8080/rongdai/zero/sendMessage_MoneyManagement.do";

	/**
	 * 极光推送的apkey
	 */
	public final static String appKey = "b3d8a52f7dc3f12f6b327060";
	/**
	 * 极光推送的masterSecret
	 */
	public final static String masterSecret = "8c589a02d42de927273c06d8";

	/**
	 * 4_28个人中心的赚和投,余额
	 */
	public static final String investAndEarn = "http://182.92.107.95:8080/rongdai/zero/investAndEarn_PersonalCenter.do";

	/**
	 * 4_8修改登陆密码
	 */
	public static final String ModifyLoginPwdInfo = "http://182.92.107.95:8080/rongdai/zero/modifyLoginPwd_PersonalCenter.do";

	/**
	 * 4_20我的账户
	 */
	public static final String myAccount = "http://182.92.107.95:8080/rongdai/zero/myAccount_PersonalCenter.do";
	/**
	 * 获取手机验证码
	 */
	public static final String getMobileCode = "http://182.92.107.95:8080/rongdai/zero/getMobileCode_PersonalCenter.do";
	/**
	 * 修改密码时确认手机号码
	 */
	public static final String forgetPassword = "http://182.92.107.95:8080/rongdai/zero/forgetPassword_PersonalCenter.do";
	/**
	 * 修改头像
	 */
	public static final String modifyHeadImg = "http://182.92.107.95:8080/rongdai/zero/modifyHeadImg_PersonalCenter.do";

	/**
	 * 4_26投资项目
	 */
	public static final String investmentProjectList = "http://182.92.107.95:8080/rongdai/zero/investmentProjectList_PersonalCenter.do";

	/**
	 * 4_24资金记录
	 */
	public static final String MoneyRecordList = "http://182.92.107.95:8080/rongdai/zero/accountRecord_PersonalCenter.do";

	/**
	 * 4_25最近交易
	 */
	public static final String RecentDealList = "http://182.92.107.95:8080/rongdai/zero/recentlyDeal_PersonalCenter.do";

	/**
	 * 4_12充值签名
	 */
	public static final String recharge = "http://182.92.107.95:8080/rongdai/zero/recharge_PersonalCenter.do";

	/**
	 * 4_14提现签名
	 */
	public static final String withdraw = "http://182.92.107.95:8080/rongdai/zero/withdraw_PersonalCenter.do";

	/**
	 * 绑定我的银行卡签名
	 */
	public static final String bindingMyBank = "http://182.92.107.95:8080/rongdai/zero/bindingMyBank_PersonalCenter.do";

	/**
	 * 获得最小时间接口：
	 */
	public static final String getMinTime = "http://182.92.107.95:8080/rongdai/zero/getMinTime_MoneyManagement.do";
	/**
	 * 意见反馈接口：
	 */
	public static final String suggestionFeekback = "http://182.92.107.95:8080/rongdai/zero/suggestionFeekback_PersonalCenter.do";
	
	/**
	 * 全局静态url资源地址
	 */
	
	public static Uri mUri=null;

}