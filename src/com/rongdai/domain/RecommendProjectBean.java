package com.rongdai.domain;

import java.io.Serializable;
import java.util.List;

public class RecommendProjectBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<RecommendProjectData> data;
	public String errorCode;
	public String message;
	public class RecommendProjectData implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * 融资项目开始时间 
		 */
		public String borrowDate;
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
		 * 融资项目id
		 */
		public String borrowId;
		/**
		 * 融资项目的期限
		 */
		public String borrowLimit;
		/**
		 * 融资项目名称
		 */
		public String borrowName;
		/**
		 * 融资类型名称
		 */
		public String borrowType;
		/**
		 * 融资项目的年化率
		 */
		public String borrowYearRate;
	}
}
