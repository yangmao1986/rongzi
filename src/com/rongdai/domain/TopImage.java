package com.rongdai.domain;

import java.util.List;

public class TopImage {
	public List<ImageLink> data;
	public String errorCode;
	public String message;
	public class ImageLink{
		public String newsId;
		public String phUrl;
	}
}
