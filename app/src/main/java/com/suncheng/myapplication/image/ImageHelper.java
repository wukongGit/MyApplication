package com.suncheng.myapplication.image;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.suncheng.commontools.utils.FileUtil;

import java.io.File;

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

    public void initImageloader(Context context) {
        int memClass = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int cacheSize = Math.max(2, memClass / 8) * 1024 * 1024;
        File cache = FileUtil.getCacheDirectory(context, Environment.DIRECTORY_PICTURES);
        File reserveCache = FileUtil.getReserveDiskCacheDir(context, Environment.DIRECTORY_PICTURES);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPriority(Thread.NORM_PRIORITY -2)
                .discCache(new UnlimitedDiskCache(cache, reserveCache, new Md5FileNameGenerator()))
                .memoryCacheSize(cacheSize)
                .build();//开始构建
        ImageLoader.getInstance().init(config);
    }

    public void displayImage(String url, ImageView view, int defaultImageResId) {
        DisplayImageOptions options = buildDefaultImageOption(defaultImageResId);
        ImageLoader.getInstance().displayImage(url, view, options);
    }

}
