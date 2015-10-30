package com.rongdai.domain;

import java.io.Serializable;
import java.util.List;

public class RepaymentModeBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public RepaymentModeBeanData data;
	public String errorCode;
	public String message;
	public class RepaymentModeBeanData implements Serializable{
		private static final long serialVersionUID = 1L;
		public String borrowAccount;
		public String borrowEndTime;
		public String borrowHadRongZi;
		public String borrowId;
		public String borrowLimit;
		public String borrowName;
		public String borrowType;
		public String borrowYearRate;
		public List<RepaymentModeBeanDataInfo> data2;
		public class RepaymentModeBeanDataInfo{
			public String borrowExpectDate;
			public String borrowPayType;
			public String borrowShouldCount;
		}
	}
}
