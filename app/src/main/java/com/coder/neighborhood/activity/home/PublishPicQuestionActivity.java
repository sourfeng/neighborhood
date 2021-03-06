package com.coder.neighborhood.activity.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.coder.neighborhood.BaseApplication;
import com.coder.neighborhood.R;
import com.coder.neighborhood.activity.BaseActivity;
import com.coder.neighborhood.activity.rx.HttpSubscriber;
import com.coder.neighborhood.adapter.home.ImageQuestionAdapter;
import com.coder.neighborhood.bean.UserBean;
import com.coder.neighborhood.bean.home.ImageQuestionBean;
import com.coder.neighborhood.mvp.model.home.HomeModel;
import com.coder.neighborhood.mvp.vu.VoidView;
import com.coder.neighborhood.utils.ToastUtils;
import com.trello.rxlifecycle.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ww.com.core.widget.CustomRecyclerView;

/**
 *
 * @author feng
 * @date 2018/1/8
 */

@SuppressLint("Registered")
public class PublishPicQuestionActivity extends BaseActivity<VoidView,HomeModel> {

    @BindView(R.id.crv)
    CustomRecyclerView crv;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.et_question)
    EditText etQuestion;
    @BindView(R.id.btn_publish)
    Button btnPublish;
    @BindView(R.id.et_phone)
    EditText etPhone;

    private ImageQuestionAdapter adapter;

    private int type;


    public static void start(Activity context, int type) {
        Intent intent = new Intent(context, PublishPicQuestionActivity.class);
        intent.putExtra("type",type);
        context.startActivityForResult(intent,0x21);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_publish_pic_question;
    }

    @Override
    protected void init() {
       initView();
       initListener();
       initData();
    }


    private void initView(){

    }

    private void initData(){
        type = getIntent().getIntExtra("type",0);
        if (type == 1){
            btnPublish.setText("寻物启事");
        }else if (type ==2){
            btnPublish.setText("失物招领");
        }
        adapter = new ImageQuestionAdapter(this);
        crv.setLayoutManager(new GridLayoutManager(this,3));
        List<ImageQuestionBean> images = new ArrayList<>();
        images.add(new ImageQuestionBean(ImageQuestionAdapter.IMAGE_ADD));
        adapter.addList(images);
        crv.setAdapter(adapter);

        UserBean user = (UserBean) BaseApplication.getInstance().getUserInfo();
        if (user!=null){
            etPhone.setText(user.getPhone());
        }
    }


    private void initListener(){
        etQuestion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvNum.setText(s.toString().length()+"/200");
            }
        });
    }


    @Override
    public void onTitleLeft() {
        super.onTitleLeft();
        finish();
    }

    private void publishQuestion(){
        String content = etQuestion.getText().toString().trim();
        if (content.length()<5){
            ToastUtils.showToast("不得少于5个字");
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

        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            ToastUtils.showToast("请输入联系方式");
            return;
        }

        if (paths.size()==0){
            ToastUtils.showToast("请添加图片");
            return;
        }

        UserBean user = (UserBean) BaseApplication.getInstance().getUserInfo();
        if (user!=null){
            String userId = user.getUserId();
            m.addLostThing(userId, type+"", phone, content, paths, bindUntilEvent(ActivityEvent.DESTROY)
                    , new HttpSubscriber<String>(this,true) {
                        @Override
                        public void onNext(String s) {
                            ToastUtils.showToast(s,true);
                            Intent intent = getIntent();
                            setResult(Activity.RESULT_OK,intent);
                            finish();
                        }
                    });
        }

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

    @OnClick({R.id.btn_publish})
    public void onPublish(View view){
        switch (view.getId()){
            case R.id.btn_publish:
                publishQuestion();
                break;
            default:
                break;
        }
    }

}
