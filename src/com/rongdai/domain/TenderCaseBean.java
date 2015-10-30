package com.rongdai.domain;

import java.io.Serializable;
import java.util.List;

public class TenderCaseBean implements Serializable {

	private static final long serialVersionUID = 1L;
	public TenderCaseData data;
	public String errorCode;
	public String message;
	public class TenderCaseData implements Serializable{
		private static final long serialVersionUID = 1L;
		public String borrowAccount;
		public String borrowEndTime;
		public String borrowHadRongZi;
		public String borrowId;
		public String borrowLimit;
		public String borrowName;
		public String borrowType;
		public String borrowYearRate;
		public List<TenderCaseDataInfo> data2;
		public class TenderCaseDataInfo{
			public String investmentAccount;
			public String investmentDate;
			public String investmentMemName;
		}
	}
}
