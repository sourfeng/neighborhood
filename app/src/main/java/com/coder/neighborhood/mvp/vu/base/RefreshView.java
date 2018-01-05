package com.coder.neighborhood.mvp.vu.base;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.coder.neighborhood.R;
import com.coder.neighborhood.mvp.listener.OnEmptyListener;
import com.coder.neighborhood.mvp.vu.BaseView;
import com.qmuiteam.qmui.widget.QMUIEmptyView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;
import ww.com.core.ScreenUtil;
import ww.com.core.widget.CustomRecyclerView;
import ww.com.core.widget.CustomSwipeRefreshLayout;

/**
 * @author feng
 * @Date 2018/1/5.
 */

public class RefreshView extends BaseView {

    @Nullable
    @BindView(R.id.crv)
    protected CustomRecyclerView crv;
    @Nullable
    @BindView(R.id.csr)
    protected CustomSwipeRefreshLayout csr;

    protected OnEmptyListener emptyListener;

    public void setEmptyListener(OnEmptyListener emptyListener) {
        this.emptyListener = emptyListener;
    }

    @Override
    public void onAttach(@NonNull Activity preActivity, @NonNull View contentView) {
        super.onAttach(preActivity, contentView);
        attach();
    }

    @Optional
    public void attach() {
        if (crv!=null){
            LinearLayoutManager manager = new LinearLayoutManager(preActivity);
            crv.setLayoutManager(manager);
            crv.setItemAnimator(new DefaultItemAnimator());

            View emptyView = LayoutInflater.from(preActivity).inflate(R.layout.layout_empty, null);
            QMUIEmptyView qmuiEmptyView = ButterKnife.findById(emptyView,R.id.empty_view);
            qmuiEmptyView.setButton(getResources().getString(R.string.str_empty_btn), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (emptyListener!=null){
                        emptyListener.onButton();
                    }
                }
            });
            ScreenUtil.scale(emptyView);
            crv.addEmpty(emptyView);
        }

        if (csr!=null){
            csr.setRefreshView(crv);
            csr.setColorSchemeColors(new int[]{Color.parseColor("#14191d"), -65536});
            csr.setEnableRefresh(false);
        }

    }

    @Optional
    public CustomRecyclerView getCrv() {
        return crv;
    }

    @Optional
    public CustomSwipeRefreshLayout getCsr() {
        return csr;
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

}
