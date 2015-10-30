package com.rongdai.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.rongdai.R;

/**
 * 用ImageLoader夹加载图片的参数
 */
public class ImageOptionsUtils {
	public static DisplayImageOptions getOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_default) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_default) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_default) // 设置图片加载或解码过程中发生错误显示的图片
				// 设置下载的图片是否缓存在内存中
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在SD卡中
				.cacheOnDisk(true).cacheOnDisc(true)
				// .displayer(new RoundedBitmapDisplayer(20)) 设置成圆角图片
				.build();
		return options;
	}
}
