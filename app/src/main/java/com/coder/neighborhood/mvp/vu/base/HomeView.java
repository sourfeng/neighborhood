package com.coder.neighborhood.mvp.vu.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.coder.neighborhood.BaseApplication;
import com.coder.neighborhood.R;
import com.coder.neighborhood.activity.home.SecondHandMarketActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youth.banner.Banner;

import java.util.List;

import butterknife.ButterKnife;
import ww.com.core.ScreenUtil;

/**
 * @author feng
 * @Date 2018/1/5.
 */

public class HomeView extends RefreshView {

    Banner banner;
    private View bannerView;
    private List<String> urls;

    @Override
    public void attach() {
        super.attach();
        bannerView = LayoutInflater.from(preActivity).inflate(R.layout.layout_home_header,null);
        onBannerPosition();
        ScreenUtil.scale(bannerView);
        banner = ButterKnife.findById(bannerView,R.id.banner);
    }


    public void addBanner(){
        crv.addHeadView(bannerView);
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }


    public void startBanner(){
        banner.setImageLoader(new com.youth.banner.loader.ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                ImageLoader.getInstance().displayImage((String) path, imageView, BaseApplication.getDisplayImageOptions(R.mipmap.pic_default));
            }
        });

        if (urls!=null&& urls.size()>0){
            banner.setImages(urls);
            banner.start();
        }
    }

    public Banner getBanner() {
        return banner;
    }


    private void onBannerPosition(){
        ButterKnife.findById(bannerView,R.id.ll_second_market).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBannerClick(2);
            }
        });
    }

    private void onBannerClick(int position){
        switch (position){
            case 1:
                break;
            case 2:
                SecondHandMarketActivity.start(preActivity);
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            default:
                break;
        }
    }
}