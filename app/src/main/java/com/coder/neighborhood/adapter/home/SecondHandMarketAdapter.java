package com.coder.neighborhood.adapter.home;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.coder.neighborhood.R;
import com.coder.neighborhood.activity.mall.GoodsTypeActivity;
import com.coder.neighborhood.adapter.mall.CategoryGoodsItemAdapter;
import com.coder.neighborhood.bean.mall.CategoryGoodsBean;

import butterknife.BindView;
import ww.com.core.adapter.RvAdapter;
import ww.com.core.adapter.RvViewHolder;

/**
 * @author feng
 * @Date 2018/1/5.
 */

public class SecondHandMarketAdapter extends RvAdapter<CategoryGoodsBean> {

    public SecondHandMarketAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutResId(int viewType) {
        return R.layout.item_second_hand_market;
    }

    @Override
    protected RvViewHolder<CategoryGoodsBean> getViewHolder(int viewType, View view) {
        return new SecondHandMarketViewHolder(view);
    }

    class SecondHandMarketViewHolder extends RvViewHolder<CategoryGoodsBean> {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.rv)
        RecyclerView rv;
        @BindView(R.id.tv_goods_more)
        TextView tvGoodsMore;

        public SecondHandMarketViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindData(int position, CategoryGoodsBean bean) {
            tvTitle.setText(bean.getItemCategoryName());
            GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
            rv.setLayoutManager(manager);
            rv.setItemAnimator(new DefaultItemAnimator());
            CategoryGoodsItemAdapter adapter = new CategoryGoodsItemAdapter(getContext());
            if (bean.getItem()!=null&& bean.getItem().size() > 2) {
                adapter.addList(bean.getItem().subList(0, 2));
            } else {
                adapter.addList(bean.getItem());
            }
            rv.setAdapter(adapter);

            tvGoodsMore.setOnClickListener(v -> {
                GoodsTypeActivity.start(getContext(), "2", bean.getItemCategoryId(), bean
                        .getItemCategoryName());
            });
        }
    }
}
