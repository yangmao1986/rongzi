package com.rongdai.domain;

import java.util.List;

public class loanProjectList {
	public List<loanProjectListData	> data;
	public String errorCode;
	public String message;
	public class loanProjectListData{
		public String loanCollSpeed;
		public String loanCost;
		public String loanDate;
		public String loanName;
		public String loanTypeId;
		public String loantArea;
	}
}
