package com.rongdai.managemoney.pager;


import com.rongdai.R;
import com.rongdai.R.color;
import com.rongdai.R.drawable;
import com.rongdai.R.id;
import com.rongdai.R.layout;
import com.rongdai.base.BaseApplication;
import com.rongdai.utils.DensityUtils;
import com.rongdai.utils.ImageDownloader;
import com.rongdai.utils.ImageLoadUtils;
import com.rongdai.utils.PersonInfoConstans;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class GalleryImageActivity extends Activity {
	Gallery mGallery;
    LinearLayout viewGroup;
    ImageView imageView,imageViewsd;
    ImageView[] imageViews;
    private String[] datas;
  
    private ImageView change_1,change_2,change_3;
    private String positionnumble;
    private ProgressDialog mypDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery_image);
		
		

        mGallery = (Gallery)findViewById(R.id.gly_images);
        viewGroup=(LinearLayout)findViewById(R.id.viewGroup);
   
        Intent it = super.getIntent() ;	
     
        positionnumble=it.getStringExtra("position");
     
        datas=it.getStringArrayExtra("dataimage");
        
        imageViews = new ImageView[datas.length];
        mGallery.setAdapter(new ImageAdapter(GalleryImageActivity.this));
        int positionID = Integer.parseInt(positionnumble); 

        mGallery.setSelection(positionID);
        
        this.mGallery.setOnItemClickListener(new OnItemClickListenerImpl()) ;
        for (int i = 0; i <datas.length; i++) {
        	imageView=new ImageView(this);
        	String abc=(String) getBaseContext().getResources().getText(R.dimen.DIMEN_18PX);
        	String abcd=(String) getBaseContext().getResources().getText(R.dimen.DIMEN_12PX);
        	String bcd = abc.replace("dip","");
        	String bcda = abcd.replace("dip","");
			Log.e("bcd", bcd);
			float sourceF = Float.valueOf(bcd);
			float sourceE = Float.valueOf(bcda);
			int ac=DensityUtils.dp2px(getBaseContext(),sourceF);
			int ab=DensityUtils.dp2px(getBaseContext(),sourceE);
        	imageView.setLayoutParams(new LayoutParams(ac,ab));
        	imageViews[i] = imageView;
        	
        	
			if (i == 0) {
			
				imageViews[i].setBackgroundResource(R.drawable.selectedflag);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.unselectedflag);
			}
			
			viewGroup.addView(imageView);
			
		}
        
        
        mGallery.setOnItemSelectedListener(new OnItemSelectedListener()
		{
        	
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				
				for (int i = 0; i < imageViews.length; i++) {
						imageViews[arg2].setBackgroundResource(R.drawable.selectedflag);
					if (arg2 != i) {
						imageViews[i].setBackgroundResource(R.drawable.unselectedflag);
					}
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub
				
			}
		}
        );
		
		
		
		
	}
	

	   private class ImageAdapter extends BaseAdapter{
		   private final ImageDownloader imageDownloader;
	        private Context mContext;
	        private int i=0;
	        
	        public ImageAdapter(Context c){
	        	imageDownloader = new ImageDownloader(c);
	            mContext = c;
	        }
	        @Override
	        public int getCount() {
	            return datas.length;
	        }

	        @Override
	        public Object getItem(int position) {
	            // TODO Auto-generated method stub
	        	
	            return position;
	        }

	        @Override
	        public long getItemId(int position) {
	            // TODO Auto-generated method stub
	        	
	            return position;
	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            // TODO Auto-generated method stub
	        	View view;
				if (convertView == null) {
					view = View
							.inflate(
									BaseApplication.getContext(),
									R.layout.image_item,
									null);

				}else{
					view = convertView;
				}
				
				imageViewsd = (ImageView) view.findViewById(R.id.imageViewsd);
				imageViewsd.setScaleType(ImageView.ScaleType.FIT_XY);
				
				String abc=(String) getBaseContext().getResources().getText(R.dimen.DIMEN_600PX);
				//abc.replace("dip", "");
				Log.e("getBaseContext", abc);
				String bcd = abc.replace("dip","");
				Log.e("bcd", bcd);
				float sourceF = Float.valueOf(bcd);
				 double b=Double.valueOf(bcd).doubleValue();
				 String acc=String.format("%.0f", b);
				Log.e("acc", acc);
				int ac=DensityUtils.dp2px(mContext,sourceF);
				//imageViewsd.setLayoutParams(new Gallery.LayoutParams(600,600));
				imageViewsd.setLayoutParams(new Gallery.LayoutParams(ac,ac));
				ImageLoadUtils.loadimage(mContext,datas[position], imageViewsd);	
				//imageDownloader.download(datas[position], imageViewsd);
				
	        	 //ImageView i = new ImageView (mContext);
		         //i.setImageResource(R.drawable.logo_luache);
		         //i.setScaleType(ImageView.ScaleType.FIT_XY);
		         //i.setLayoutParams(new Gallery.LayoutParams(600,600));
		         //imageDownloader.download(datas[position], i);
	           /* ImageView i = new ImageView (mContext);
	            imageDownloader.download(datas[position], i);
	            i.setScaleType(ImageView.ScaleType.FIT_XY);
	            i.setLayoutParams(new Gallery.LayoutParams(600,600));*/
	            
	            
	            return view;
	        }
	        
	    };

	    private class OnItemClickListenerImpl implements OnItemClickListener {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				GalleryImageActivity.this.finish() ;
				/*Toast.makeText(GalleryCommodityActivity.this, String.valueOf(position),
						Toast.LENGTH_SHORT).show();*/
			}
		}
	    
	
	
}
