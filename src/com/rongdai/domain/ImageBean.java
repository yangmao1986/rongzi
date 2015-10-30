package com.rongdai.domain;

import java.util.List;

public class ImageBean {
	public String errorCode;
	public String message;
	public ImageBeanData data;
	public class ImageBeanData{
		public String borrowAccount;
		public String borrowEndTime;
		public String borrowHadRongZi;
		public String borrowId;
		public String borrowLimit;
		public String borrowName;
		public String borrowType;
		public String borrowYearRate;
		public List<String> imgUrl;
	}
}
