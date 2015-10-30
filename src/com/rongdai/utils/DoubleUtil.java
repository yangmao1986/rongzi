package com.rongdai.utils;

import java.text.DecimalFormat;

public class DoubleUtil {
	/**
	 * 保留两位精度的double工具
	 * 
	 * @param num
	 *            需要转换精度的double
	 * @return 转换后的double
	 */
	public static String getDecimal(double num) {
		if (Double.isNaN(num)) {
			return "";
		}
		DecimalFormat format = new DecimalFormat("#0.00");
		String str = format.format(num);
//		BigDecimal bd = new BigDecimal(num);
//		num = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return str;
	}
}
