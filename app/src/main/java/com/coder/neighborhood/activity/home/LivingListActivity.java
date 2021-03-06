package com.coder.neighborhood.activity.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.coder.neighborhood.R;
import com.coder.neighborhood.activity.BaseActivity;
import com.coder.neighborhood.adapter.home.LivingListAdapter;
import com.coder.neighborhood.mvp.model.VoidModel;
import com.coder.neighborhood.mvp.vu.home.LivingListView;

import java.util.Arrays;

import butterknife.OnClick;

/**
 * @author feng
 * @Date 2018/1/5.
 */

@SuppressLint("Registered")
public class LivingListActivity extends BaseActivity<LivingListView,VoidModel> {

    private LivingListAdapter adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, LivingListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_living_list;
    }

    @Override
    protected void init() {
        adapter = new LivingListAdapter(this);
        v.getCrv().setAdapter(adapter);
        adapter.addList(Arrays.asList("1","2","3"));
    }

    @Override
    public void onTitleLeft() {
        super.onTitleLeft();
        finish();
    }

    @OnClick({R.id.btn_open_living})
    public void onLiving(View v){
        switch (v.getId()){
            case R.id.btn_open_living:
                LivingCertificationActivity.start(this);
                break;
        }
    }
}
