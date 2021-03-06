package com.coder.neighborhood.activity.mall;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coder.neighborhood.BaseApplication;
import com.coder.neighborhood.R;
import com.coder.neighborhood.activity.BaseActivity;
import com.coder.neighborhood.activity.rx.HttpSubscriber;
import com.coder.neighborhood.adapter.home.ImageQuestionAdapter;
import com.coder.neighborhood.adapter.user.CategoryAdapter;
import com.coder.neighborhood.bean.UserBean;
import com.coder.neighborhood.bean.home.ImageQuestionBean;
import com.coder.neighborhood.bean.mall.CategoryBean;
import com.coder.neighborhood.mvp.model.home.HomeModel;
import com.coder.neighborhood.mvp.vu.VoidView;
import com.coder.neighborhood.utils.ToastUtils;
import com.coder.neighborhood.widget.CashierInputFilter;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ww.com.core.ScreenUtil;
import ww.com.core.widget.CustomRecyclerView;

/**
 * @author feng
 * @Date 2018/1/16
 */

@SuppressLint("Registered")
public class AddSecondHandActivity extends BaseActivity<VoidView, HomeModel> {

    @BindView(R.id.ll_add_show)
    LinearLayout llAddShow;
    @BindView(R.id.rl_image_show)
    RelativeLayout rlImageShow;
    @BindView(R.id.iv_image)
    ImageView ivImage;

    @BindView(R.id.et_goods_name)
    EditText etGoodsName;
    @BindView(R.id.et_goods_price)
    EditText etGoodsPrice;
    @BindView(R.id.et_goods_num)
    EditText etGoodsNum;
//    @BindView(R.id.et_goods_type)
//    EditText etGoodsType;
    @BindView(R.id.ll_goodsType_selector)
    LinearLayout llGoodsTypeSelector;
    @BindView(R.id.tv_goods_type)
    TextView tvGoodsType;
    @BindView(R.id.et_goods_detail)
    EditText etGoodsDetail;
    @BindView(R.id.crv)
    CustomRecyclerView crv;


    private String categoryId;
    private BottomSheetDialog sheetDialog;
    private List<CategoryBean> categoryBeans;
    private ImageQuestionAdapter adapter;


    public static void startForResult(Activity context,int requestCode) {
        Intent intent = new Intent(context, AddSecondHandActivity.class);
        context.startActivityForResult(intent,requestCode);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_second_hand;
    }

    @Override
    protected void init() {
        goodsType();
        initViews();
        initListener();
    }

    private void initListener(){
        InputFilter[] filters={new CashierInputFilter()};
        etGoodsPrice.setFilters(filters);

    }

    private void initViews(){
        adapter = new ImageQuestionAdapter(this);
        crv.setLayoutManager(new GridLayoutManager(this,3));
        List<ImageQuestionBean> images = new ArrayList<>();
        images.add(new ImageQuestionBean(ImageQuestionAdapter.IMAGE_ADD));
        adapter.addList(images);
        crv.setAdapter(adapter);
    }


    @Override
    public void onTitleLeft() {
        super.onTitleLeft();
        finish();
    }

    @OnClick({ R.id.if_close,R.id.btn_publish_goods,R.id.ll_goodsType_selector})
    public void onAddSecond(View view) {
        switch (view.getId()) {
            case R.id.if_close:
                ivImage.setImageResource(R.mipmap.pic_default);
                showImageType(true);
                break;
            case R.id.btn_publish_goods:
                publishGoods();
                break;
            case R.id.ll_goodsType_selector:
                showSheetDialog();
                break;
            default:
                break;
        }

    }



    private void showImageType(boolean isAdd) {
        llAddShow.setVisibility(isAdd ? View.VISIBLE : View.GONE);
        rlImageShow.setVisibility(!isAdd ? View.VISIBLE : View.GONE);

    }




    private void publishGoods(){
        String name = etGoodsName.getText().toString().trim();
        String price= etGoodsPrice.getText().toString().trim();
        String num = etGoodsNum.getText().toString().trim();
        String detail = etGoodsDetail.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            ToastUtils.showToast("请输入商品名称");
            return;
        }
        if (TextUtils.isEmpty(price)){
            ToastUtils.showToast("请输入商品价格");
            return;
        }

        if (TextUtils.isEmpty(num)){
            ToastUtils.showToast("请输入商品数量");
            return;
        }

        if (TextUtils.isEmpty(categoryId)){
            ToastUtils.showToast("请选择商品类型");
            return;
        }
        if (TextUtils.isEmpty(detail)){
            ToastUtils.showToast("请输入商品详情");
            return;
        }


        List<ImageQuestionBean> imageQuestionBeans = adapter.getList();
        List<String> paths = new ArrayList<>();
        if (imageQuestionBeans!=null && imageQuestionBeans.size()>0){
            for (ImageQuestionBean imageQuestionBean : imageQuestionBeans) {
                if (imageQuestionBean.getImageType() == ImageQuestionAdapter.IMAGE_SHOW){
                    paths.add(imageQuestionBean.getUrl());
                }
            }
        }

        UserBean user = (UserBean) BaseApplication.getInstance().getUserInfo();
        if (user!=null && !TextUtils.isEmpty(user.getUserId())){
            m.addSecondGoods(user.getUserId(), name, price,num , categoryId, detail, paths, bindUntilEvent(ActivityEvent.DESTROY)

                    , new HttpSubscriber<String>(this,true) {
                        @Override
                        public void onNext(String s) {
                            ToastUtils.showToast(s);
                            finishActivity();
                        }
                    });
        }
    }

    private void finishActivity(){
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK,intent);
        finish();
    }


    private void goodsType(){
        m.goodsType(bindUntilEvent(ActivityEvent.DESTROY), new HttpSubscriber<List<CategoryBean>>(this,true) {

            @Override
            public void onNext(List<CategoryBean> categoryBeans) {
                AddSecondHandActivity.this.categoryBeans = categoryBeans;
            }
        });
    }


    private void showSheetDialog() {
        if (sheetDialog == null) {
            sheetDialog = new BottomSheetDialog(this,R.style.CustomerDialogStyle);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_community, null);
            ScreenUtil.scale(view);
            CustomRecyclerView crv = ButterKnife.findById(view, R.id.crv);
            crv.setLayoutManager(new LinearLayoutManager(this));
            CategoryAdapter adapter = new CategoryAdapter(this);
            adapter.setOnActionListener((position, view1) -> {
                CategoryBean bean = adapter.getItem(position);
                tvGoodsType.setText(bean.getItemCategoryName());
                categoryId = bean.getItemCategoryId();
                sheetDialog.dismiss();
            });
            adapter.addList(categoryBeans);
            crv.setAdapter(adapter);
            sheetDialog.setContentView(view);
        }
        sheetDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        adapter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.onActivityResult(requestCode, requestCode, data);
    }
}
