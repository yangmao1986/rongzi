package com.rongdai.domain;

import java.io.Serializable;
import java.util.List;

public class RelatedDataBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public RelatedDataBeanData data;
	public String errorCode;
	public String message;
	public class RelatedDataBeanData implements Serializable{
		private static final long serialVersionUID = 1L;
		public String borrowAccount;
		public String borrowEndTime;
		public String borrowHadRongZi;
		public String borrowId;
		public String borrowLimit;
		public String borrowName;
		public String borrowType;
		public String borrowYearRate;
		public List<danbao> 担保协议;
		public List<description> 经营环境;
		public List<identityCard> 身份证信息;
		public class danbao{
			public String investmentPicName;
			public String investmentPicUrl;
		}
		public class description{
			public String investmentPicName;
			public String investmentPicUrl;
		}
		public class identityCard{
			public String investmentPicName;
			public String investmentPicUrl;
		}
	}
}
