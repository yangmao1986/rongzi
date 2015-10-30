package com.rongdai.domain;

public class ProjectInfoBean {
	public projectInfoData data;
	/**
	 * 失败信息
	 */
	public String errorCode;
	/**
	 * 成功信息
	 */
	public String message;
	/**
	 * 项目基本信息
	 * @author 郑阳阳
	 *
	 */
	public class projectInfoData{
		/**
		 * 年龄
		 */
		public String age;
		/**
		 * 已经还款次数
		 */
		public String alreayPaymentCount;
		/**
		 * 出生地
		 */
		public String birthAddress;
		/**
		 * 借款描述
		 */
		public String bmDesc;
		/**
		 * 融资项目金额
		 */
		public String borrowAccount;
		/**
		 * 融资项目最后时间
		 */
		public String borrowEndTime;
		/**
		 * 当前已融资金额
		 */
		public String borrowHadRongZi;
		/**
		 * 最高受限额度
		 */
		public String borrowHighLevel;
		/**
		 * 融资项目id
		 */
		public String borrowId;
		/**
		 * 融资项目期限
		 */
		public String borrowLimit;
		/**
		 * 融资项目名称
		 */
		public String borrowName;
		/**
		 * 还款日期
		 */
		public String borrowPayDate;
		/**
		 * 项目情况（包括企业情况，调查情况，担保情况
		 */
		public String borrowProSituation;
		/**
		 * 融资类型名称
		 */
		public String borrowType;
		/**
		 * 借款资金用途
		 */
		public String borrowUse;
		/**
		 * 融资项目的年化率
		 */
		public String borrowYearRate;
		/**
		 * 商业保险
		 */
		public String commerialInsurance;
		/**
		 * 信用卡额度
		 */
		public String creditCardAccountLevel;
		/**
		 * 征信报告
		 */
		public String creditInformationReports;
		/**
		 * 房产
		 */
		public String houseProperty;
		/**
		 * 是否已婚
		 */
		public String isMarry;
		/**
		 * 文化水平
		 */
		public String knowledgeLevel;
		/**
		 * 
		 */
		public String loantArea;
		/**
		 * 长期居住地
		 */
		public String longTimeLiveAddress;
		/**
		 * 职业
		 */
		public String profession;
		/**
		 * 性别
		 */
		public String sex;
		/**
		 * 社保年限
		 */
		public String socialSecurityTime;
		/**
		 * 成功借入
		 */
		public String successLoanCount;
		/**
		 * 职称
		 */
		public String title;
		/**
		 * 待还款次数
		 */
		public String waitingPayment;
		/**
		 * 工作年限
		 */
		public String workTime;
		/**
		 * 年收入
		 */
		public String yearIncome;
	}
}
