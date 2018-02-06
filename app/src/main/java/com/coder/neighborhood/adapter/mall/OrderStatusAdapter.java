package com.coder.neighborhood.adapter.mall;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coder.neighborhood.R;
import com.coder.neighborhood.bean.user.OrderBean;

import butterknife.BindView;
import ww.com.core.adapter.RvAdapter;
import ww.com.core.adapter.RvViewHolder;

/**
 *
 * @author feng
 * @date 2018/1/9
 */

public class OrderStatusAdapter extends RvAdapter<OrderBean>{

    public OrderStatusAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutResId(int viewType) {
        return R.layout.item_order_status;
    }

    @Override
    protected RvViewHolder<OrderBean> getViewHolder(int viewType, View view) {
        return new CommentDetailViewHolder(view);
    }

    class CommentDetailViewHolder extends RvViewHolder<OrderBean>{
        @BindView(R.id.tv_goods_name)
        TextView tvGoodsName;
        @BindView(R.id.tv_goods_price)
        TextView tvGoodsPrice;
        @BindView(R.id.tv_goods_type)
        TextView tvGoodsType;
        @BindView(R.id.tv_goods_num)
        TextView tvGoodsNum;
        @BindView(R.id.btn_order)
        Button btnOrder;

        public CommentDetailViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindData(int position, OrderBean bean) {
            /**
             *
             * 订单状态(1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭',)
             */
            tvGoodsPrice.setText(bean.getOrderPayment()+"元");
            showStatus(bean.getOrderStatus());
        }

        private void showStatus(int status){
            switch (status){
                case 1:
                    btnOrder.setText("待付款");
                    break;
                case 2:
                    btnOrder.setText("催单");
                    break;
                case 3:
                    btnOrder.setText("确认收货");
                    break;
                case 4:
                    btnOrder.setText("评价");
                    break;
                case 5:
                    btnOrder.setText("退货");
                    break;
                case 6:
                    btnOrder.setText("已关闭");
                    break;
                default:
                    break;
            }
        }



    }
}
