package com.suncheng.myapplication.image;

import android.graphics.Bitmap;
import android.widget.ImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class ImageHelper {

    private static ImageHelper sInstance;

    private ImageHelper() {

    }

    private DisplayImageOptions buildDefaultImageOption(int defaultImageResId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultImageResId) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(defaultImageResId)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(defaultImageResId)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
//.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//设置图片加入缓存前，对bitmap进行设置
//.preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//                .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成
        return options;

    }

    public static ImageHelper getInstance() {
        if (sInstance == null) {
            synchronized (ImageHelper.class) {
                if (sInstance == null) {
                    sInstance = new ImageHelper();
                }
            }
        }
        return sInstance;
    }

    public void displayImage(String url, ImageView view, int defaultImageResId) {
        DisplayImageOptions options = buildDefaultImageOption(defaultImageResId);
        ImageLoader.getInstance().displayImage(url, view, options);
    }

}
