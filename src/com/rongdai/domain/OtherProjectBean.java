package com.rongdai.domain;

import java.io.Serializable;
import java.util.List;

import com.rongdai.domain.RecommendProjectBean.RecommendProjectData;

public class OtherProjectBean implements Serializable {
	private static final long serialVersionUID = 1L;
	public String errorCode;
	public String message;
	public List<RecommendProjectData> licaichanping;
	public List<RecommendProjectData> zhongliangqimao;
	public List<RecommendProjectData> dingxiangmuji;
	public List<RecommendProjectData> yibangshangcheng;
}
