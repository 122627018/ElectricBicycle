package com.wxxiaomi.electricbicycle.engine;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;
import com.wxxiaomi.electricbicycle.R;

public class ImageEngineImpl {
	Context context;
	RequestQueue mQueue;
	public ImageEngineImpl(Context context) {
		super();
		this.context = context;
		mQueue = Volley.newRequestQueue(context);
	}

	public void getHeadImageByUrl(ImageView view,String url){
		ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
		ImageListener imageListener = ImageLoader.getImageListener(view, R.drawable.ic_launcher, R.drawable.ic_launcher);
		try{
			imageLoader.get(url, imageListener);
		}catch(Exception e){
			return ;
		}
		
	}
	
	public class BitmapCache implements ImageCache {  
		  
	    private LruCache<String, Bitmap> mCache;  
	  
	    public BitmapCache() {  
	        int maxSize = 10 * 1024 * 1024;  
	        mCache = new LruCache<String, Bitmap>(maxSize) {  
	            @Override  
	            protected int sizeOf(String key, Bitmap bitmap) {  
	                return bitmap.getRowBytes() * bitmap.getHeight();  
	            }  
	        };  
	    }  
	  
	    @Override  
	    public Bitmap getBitmap(String url) {  
	        return mCache.get(url);  
	    }  
	  
	    @Override  
	    public void putBitmap(String url, Bitmap bitmap) {  
	        mCache.put(url, bitmap);  
	    }  
	  
	}  
}


