package com.coder.neighborhood.activity.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.coder.neighborhood.R;
import com.coder.neighborhood.activity.BaseActivity;
import com.coder.neighborhood.adapter.user.TranslateTabAdapter;
import com.coder.neighborhood.fragment.home.FindThingsFragment;
import com.coder.neighborhood.fragment.home.LostThingsFragment;
import com.coder.neighborhood.mvp.aop.CheckUser;
import com.coder.neighborhood.mvp.model.VoidModel;
import com.coder.neighborhood.mvp.vu.VoidView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ww.com.core.widget.TranslateTabBar;

/**
 * @author feng
 * @date 2018/1/8
 */

@SuppressLint("Registered")
public class FindThingsActivity extends BaseActivity<VoidView, VoidModel> {

    @BindView(R.id.translate)
    TranslateTabBar translate;
    @BindView(R.id.vp)
    ViewPager vp;

    private List<Fragment> fragments;
    private FragmentManager fragmentManager;
    private TranslateTabAdapter adapter;
    private int type = 1;

    public static void start(Context context) {
        Intent intent = new Intent(context, FindThingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_find_things;
    }

    @Override
    protected void init() {
        translate.setOnTabChangeListener(new TranslateTabBar.OnTabChangeListener() {
            @Override
            public void onChange(int index) {
                vp.setCurrentItem(index);
            }
        });
//
        fragmentManager = getSupportFragmentManager();
        addFragment();
        adapter = new TranslateTabAdapter(fragmentManager, fragments);
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(2);

        vp.setOnPageChangeListener(pageChangeListener);
    }

    @Override
    public void onTitleLeft() {
        finish();
    }

    private void addFragment() {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        fragments.add(new FindThingsFragment());
        fragments.add(new LostThingsFragment());
    }


    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int
                positionOffsetPixels) {

            type = position + 1;
        }

        @Override
        public void onPageSelected(int position) {
            translate.setCurrentIndex(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @CheckUser
    @OnClick({R.id.btn_ask})
    public void onFindThings(View v) {
        switch (v.getId()) {
            case R.id.btn_ask:
                PublishPicQuestionActivity.start(this, type);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x21 && resultCode == Activity.RESULT_OK) {
            int type = data.getIntExtra("type",0);
            if (type > 0){
                fragments.get(type-1).onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
