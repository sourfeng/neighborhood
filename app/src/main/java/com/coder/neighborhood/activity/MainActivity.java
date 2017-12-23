package com.coder.neighborhood.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.coder.neighborhood.BaseApplication;
import com.coder.neighborhood.R;
import com.coder.neighborhood.fragment.CircleFragment;
import com.coder.neighborhood.fragment.HomeFragment;
import com.coder.neighborhood.fragment.MallFragment;
import com.coder.neighborhood.fragment.MessageFragment;
import com.coder.neighborhood.fragment.UserFragment;
import com.coder.neighborhood.mvp.model.VoidModel;
import com.coder.neighborhood.mvp.vu.VoidView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import ww.com.core.adapter.MenuTabAdapter;

public class MainActivity extends BaseActivity<VoidView, VoidModel> {

    @BindViews({R.id.tab_home_layout, R.id.tab_cart_layout, R.id.tab_circle_layout, R.id
            .tab_message_layout, R.id.tab_user_layout})
    List<View> menus;
    @BindViews({R.id.tab_home_image, R.id.tab_cart_image, R.id.tab_circle_image, R.id
            .tab_message_image, R.id.tab_user_image})
    List<View> images;
    @BindViews({R.id.tab_home_text, R.id.tab_cart_text, R.id.tab_circle_text, R.id
            .tab_message_text, R.id.tab_user_text})
    List<View> texts;


    private MenuTabAdapter adapter;
    private List<Fragment> fragments;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new MallFragment());
        fragments.add(new CircleFragment());
        fragments.add(new MessageFragment());
        fragments.add(new UserFragment());

        adapter = new MenuTabAdapter(this, menus, fragments, R.id.main_content);

        adapter.setOnMenuClickListener(new MenuTabAdapter.OnMenuClickListener() {
            @Override
            public void onDoubleClick(View view) {

            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.tab_home_layout:
                        adapter.changeMenuStatus(0);
                        changeMenuStatus(0);
                        adapter.changeMenu(0);
                        break;
                    case R.id.tab_cart_layout:
                        adapter.changeMenuStatus(1);
                        changeMenuStatus(1);
                        adapter.changeMenu(1);
                        break;
                    case R.id.tab_circle_layout:
                        adapter.changeMenuStatus(2);
                        changeMenuStatus(2);
                        adapter.changeMenu(2);
                        break;
                    case R.id.tab_message_layout:
                        adapter.changeMenuStatus(3);
                        adapter.changeMenu(3);
                        changeMenuStatus(3);
                        break;
                    case R.id.tab_user_layout:
                        adapter.changeMenuStatus(4);
                        adapter.changeMenu(4);
                        changeMenuStatus(4);
                        break;
                }
            }
        });


        adapter.changeMenuStatus(0);
        changeMenuStatus(0);
        adapter.changeMenu(0);

    }


    public void changeMenuStatus(int index) {
        int imageSize = images.size();
        for (int i = 0; i < imageSize; i++) {
            if (i == index) {
                this.images.get(i).setSelected(true);
                this.texts.get(i).setSelected(true);
            } else {
                this.images.get(i).setSelected(false);
                this.texts.get(i).setSelected(false);
            }
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 3000) {
                Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                BaseApplication.getInstance().clearTopTask(MainActivity.this);
                onBackPressed();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}