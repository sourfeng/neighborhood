package com.coder.neighborhood.fragment.circle;

import android.content.Intent;
import android.text.TextUtils;

import com.coder.neighborhood.BaseApplication;
import com.coder.neighborhood.R;
import com.coder.neighborhood.activity.circle.PublishTopicActivity;
import com.coder.neighborhood.activity.rx.HttpSubscriber;
import com.coder.neighborhood.adapter.circle.TopicAdapter;
import com.coder.neighborhood.bean.UserBean;
import com.coder.neighborhood.bean.circle.TopicBean;
import com.coder.neighborhood.config.Constants;
import com.coder.neighborhood.fragment.BaseFragment;
import com.coder.neighborhood.mvp.aop.CheckUser;
import com.coder.neighborhood.mvp.model.CircleModel;
import com.coder.neighborhood.mvp.vu.base.RefreshView;

import java.util.List;

import butterknife.OnClick;
import ww.com.core.widget.CustomRecyclerView;
import ww.com.core.widget.CustomSwipeRefreshLayout;

/**
 * @Author feng
 * @Date 2018/1/16
 */

public class TopicFragment extends BaseFragment<RefreshView,CircleModel> {

    private TopicAdapter adapter;
    private int page = 1;

    private CustomSwipeRefreshLayout csr;
    private CustomRecyclerView crv;

    private final int REQUEST_CODE = 0x19;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_topic;
    }

    @Override
    protected void init() {
        initView();
        initListener();
        initData();
    }

    private void initView() {
        adapter = new TopicAdapter(getContext());
        csr = v.getCsr();
        crv = v.getCrv();
        csr.setEnableRefresh(true);
        csr.setFooterRefreshAble(false);
    }

    private void initListener() {
        csr.setOnSwipeRefreshListener(new CustomSwipeRefreshLayout.OnSwipeRefreshLayoutListener() {
            @Override
            public void onHeaderRefreshing() {
                page = 1;
                csr.setEnableRefresh(true);
                csr.setFooterRefreshAble(false);
                topics();
            }

            @Override
            public void onFooterRefreshing() {
                v.getCrv().addFooterView(v.getFooterView());
                topics();
            }
        });
    }

    private void initData() {
        crv.setAdapter(adapter);
        topics();
    }

    private void topics(){
        UserBean user = (UserBean) BaseApplication.getInstance().getUserInfo();
        if (user !=null && !TextUtils.isEmpty(user.getUserId())){
            m.topics(user.getUserId(), page + "", Constants.PAGE_SIZE + "",
                    new HttpSubscriber<List<TopicBean>>(getContext(),false) {

                        @Override
                        public void onNext(List<TopicBean> topicBeans) {
                            v.getCsr().setRefreshFinished();
                            if (topicBeans != null && topicBeans.size() > 0) {
                                if (page == 1) {
                                    adapter.addList(topicBeans);
                                    csr.setFooterRefreshAble(true);
                                } else {
                                    v.getCrv().removeFooterView(v.getFooterView());
                                    adapter.appendList(topicBeans);
                                }

                                if (topicBeans.size() == Constants.PAGE_SIZE) {
                                    page++;
                                } else {
                                    v.getCsr().setFooterRefreshAble(false);
                                }
                            } else {
                                v.getCsr().setFooterRefreshAble(false);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            v.getCsr().setRefreshFinished();
                        }
                    });
        }
    }

    @CheckUser
    @OnClick(R.id.btn_ask)
    public void onPublish(){
        PublishTopicActivity.startForResult(getPresenterActivity(),REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE){
            page = 1;
            topics();
        }
    }

}
