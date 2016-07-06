package com.andview.example.ui;

import java.util.ArrayList;
import java.util.List;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.andview.example.DensityUtil;
import com.andview.example.R;
import com.bumptech.glide.Glide;

/**
 * 广告图片自动轮播控件</br>
 * 
 */
public class ImageCycleView extends LinearLayout {
	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 图片轮播视图
	 */
	private ViewPager mAdvPager = null;
	/**
	 * 滚动图片视图适配
	 */
	public ImageCycleAdapter mAdvAdapter;
	/**
	 * 图片轮播指示器控件
	 */
	private ViewGroup mGroup;

	/**
	 * 图片轮播指示个图
	 */
	private ImageView mImageView = null;

	/**
	 * 滚动图片指示视图列表
	 */
	private ImageView[] mImageViews = null;

	/**
	 * 图片滚动当前图片下标
	 */

	private boolean isStop;

	/**
	 * 游标是圆形还是长条，要是设置为0是长条，要是1就是圆形 默认是圆形
	 */
	public int stype=1;

	/**
	 * @param context
	 */
	public ImageCycleView(Context context) {
		super(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	@SuppressLint("Recycle")
	public ImageCycleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.ad_cycle_view, this);
		mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
		mAdvPager.addOnPageChangeListener(new GuidePageChangeListener());
		//mAdvPager.setOnPageChangeListener(new GuidePageChangeListener());
		// 滚动图片右下指示器视
		mGroup = (ViewGroup) findViewById(R.id.viewGroup);		
	}

	/**
	 * 触摸停止计时器，抬起启动计时器
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_UP){
			// 开始图片滚动
			startImageTimerTask();
		}else{
			// 停止图片滚动
			stopImageTimerTask();
		}
		return super.dispatchTouchEvent(event);
	}
	
	
	public void updataData(List<String> imageUrlList){
		stopImageTimerTask();
		this.imageUrlList = imageUrlList;
		mGroup.removeAllViews();

		// 图片广告数量
		final int imageCount = imageUrlList.size();
		mImageViews = new ImageView[imageCount];
		for (int i = 0; i < imageCount; i++) {
			mImageView = new ImageView(mContext);
			LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin= DensityUtil.dip2px(mContext, 10);
			mImageView.setScaleType(ScaleType.CENTER_CROP);
			mImageView.setLayoutParams(params);

			mImageViews[i] = mImageView;
			if (i == 0) {
				if(this.stype==1)
					mImageViews[i].setBackgroundResource(R.mipmap.banner_dian_focus);
				else
					mImageViews[i].setBackgroundResource(R.mipmap.ic_circle_selected);
			} else {
				if(this.stype==1)
					mImageViews[i].setBackgroundResource(R.mipmap.banner_dian_blur);
				else
					mImageViews[i].setBackgroundResource(R.mipmap.ic_circle_normal);
			}
			mGroup.addView(mImageViews[i]);
		}
		
		mAdvAdapter.notifyDataSetChanged();
		startImageTimerTask();
	}
	/**
	 * 装填图片数据
	 * 
	 * @param imageUrlList
	 * @param imageCycleViewListener
	 */
	public void setImageResources(List<String> imageUrlList ,
			ImageCycleViewListener imageCycleViewListener,int stype){
		this.stype=stype;
		this.imageUrlList = imageUrlList;
		// 清除
		mGroup.removeAllViews();
		// 图片广告数量
		final int imageCount = imageUrlList.size();
		mImageViews = new ImageView[imageCount];
		for (int i = 0; i < imageCount; i++) {
			mImageView = new ImageView(mContext);
			LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.leftMargin=30; 
			mImageView.setScaleType(ScaleType.CENTER_CROP);
			mImageView.setLayoutParams(params);

			mImageViews[i] = mImageView;
			if (i == 0) {
				if(this.stype==1)
					mImageViews[i].setBackgroundResource(R.mipmap.banner_dian_focus);
				else
					mImageViews[i].setBackgroundResource(R.mipmap.ic_circle_selected);
			} else {
				if(this.stype==1)
					mImageViews[i].setBackgroundResource(R.mipmap.banner_dian_blur);
				else
					mImageViews[i].setBackgroundResource(R.mipmap.ic_circle_normal);
			}
			mGroup.addView(mImageViews[i]);
		}

		mAdvAdapter = new ImageCycleAdapter(mContext, imageCycleViewListener);
		mAdvPager.setAdapter(mAdvAdapter);
		mAdvPager.setCurrentItem(Integer.MAX_VALUE/2);
		startImageTimerTask();
	}

	/**
	 * 图片轮播(手动控制自动轮播与否，便于资源控件）
	 */
	public void startImageCycle() {
		startImageTimerTask();
	}

	/**
	 * 暂停轮播—用于节省资源
	 */
	public void pushImageCycle() {
		stopImageTimerTask();
	}

	/**
	 * 图片滚动任务
	 */
	private void startImageTimerTask() {
		stopImageTimerTask();
		// 图片滚动
		mHandler.postDelayed(mImageTimerTask, 3000);
	}

	/**
	 * 停止图片滚动任务
	 */
	private void stopImageTimerTask() {
		isStop=true;
		mHandler.removeCallbacks(mImageTimerTask);
	}

	private Handler mHandler = new Handler();

	/**
	 * 图片自动轮播Task
	 */
	private Runnable mImageTimerTask = new Runnable() {
		@Override
		public void run() {
			if (mImageViews != null) {
				mAdvPager.setCurrentItem(mAdvPager.getCurrentItem()+1);
				if(!isStop){  //if  isStop=true   //当你退出后 要把这个给停下来 不然 这个一直存在 就一直在后台循环 
					mHandler.postDelayed(mImageTimerTask, 3000);
				}

			}
		}
	};

	/**
	 * 轮播图片监听
	 * 
	 * @author minking
	 */
	private final class GuidePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE)
				startImageTimerTask(); 
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int index) {
			index=index%mImageViews.length;
			// 设置当前显示的图片
			// 设置图片滚动指示器背
			if(stype==1)
				mImageViews[index].setBackgroundResource(R.mipmap.banner_dian_focus);
			else
				mImageViews[index].setBackgroundResource(R.mipmap.ic_circle_selected);
			for (int i = 0; i < mImageViews.length; i++) {
				if (index != i) {
					if(stype==1)
						mImageViews[i].setBackgroundResource(R.mipmap.banner_dian_blur);
					else
						mImageViews[i].setBackgroundResource(R.mipmap.ic_circle_normal);
				}
			}
		}
	}

	
	//图片视图缓存列表
	private ArrayList<ImageView> mImageViewCacheList;
	//图片资源列表
	private List<String> imageUrlList = new ArrayList<String>();
	
	class ImageCycleAdapter extends PagerAdapter {

//		/**
//		 * 图片视图缓存列表
//		 */
//		private ArrayList<SmartImageView> mImageViewCacheList;
//
//		/**
//		 * 图片资源列表
//		 */
//		private ArrayList<AdvertisementPojo> mAdList = new ArrayList<AdvertisementPojo>();
		

		/**
		 * 广告图片点击监听
		 */
		private ImageCycleViewListener mImageCycleViewListener;

		private Context mContext;
		public ImageCycleAdapter(Context context,
				ImageCycleViewListener imageCycleViewListener) {
			this.mContext = context;
			mImageCycleViewListener = imageCycleViewListener;
			mImageViewCacheList = new ArrayList<ImageView>();
		}

		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			final String imageUrl = imageUrlList.get(position%imageUrlList.size());
			ImageView imageView = null;
			if (mImageViewCacheList.isEmpty()) {
				imageView = new ImageView(mContext);
				imageView.setLayoutParams(new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				//test
				imageView.setScaleType(ScaleType.CENTER_CROP);
				// 设置图片点击监听
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mImageCycleViewListener.onImageClick(imageUrl,position%imageUrlList.size());
					}
				});
			} else {
				imageView = mImageViewCacheList.remove(0);
			}

			//imageView.setTag(imageUrl);
			container.addView(imageView);
			//imageView.setImageUrl(imageUrl);

			//Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
			Glide.with(mContext).load(imageUrl).into(imageView);
//			Glide.with(mContext)
//					.load(imageUrl)
//					.centerCrop()
//					.placeholder(R.raw.circles)
//					.crossFade()
//					.into(imageView);

			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ImageView view = (ImageView) object;
			mAdvPager.removeView(view);
			//mImageViewCacheList.add(view);

		}

	}

	/**
	 * 轮播控件的监听事件
	 * 
	 * @author minking
	 */
	public static interface ImageCycleViewListener {

		/**
		 * 单击图片事件
		 * 
		 * @param url
		 * @param title
		 */
		public void onImageClick(String url, int position);
	}

}
