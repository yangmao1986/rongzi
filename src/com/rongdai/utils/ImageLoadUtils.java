package com.rongdai.utils;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.rongdai.R;

/**
 * 用imageLoader加载图片的工具类
 * 
 * @author Administrator
 * 
 */
public class ImageLoadUtils {
	private static ImageLoader imageLoader;

	public static void loadimage(Context context, String uri,
			ImageView imageAware) {
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_default) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_default) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_default) // 设置图片加载或解码过程中发生错误显示的图片
				// 设置下载的图片是否缓存在内存中
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在SD卡中
				.cacheOnDisk(true).cacheOnDisc(true)
//				 .displayer(new RoundedBitmapDisplayer(20)) 设置成圆角图片
				.build();
		imageLoader.displayImage(uri, imageAware, options);
	}
	public static void destroy(){
		if(imageLoader!= null){
			imageLoader.destroy();
		}
	}
}
