package com.tourcoo.training.ui.account;

import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tourcoo.training.R;
import com.tourcoo.training.core.base.activity.BaseRefreshLoadActivity;
import com.tourcoo.training.core.base.entity.BaseMovieEntity;
import com.tourcoo.training.core.base.entity.SubjectMovieAdapter;
import com.tourcoo.training.core.base.mvp.BaseMvpRefreshLoadActivity;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.ui.account.contract.MessageContract;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月24日19:15
 * @Email: 971613168@qq.com
 */
public class TestMvpActivity extends BaseMvpRefreshLoadActivity<MessagePresenter, BaseMovieEntity> implements MessageContract.View {
    @Override
    public int getContentLayout() {
        return R.layout.frame_layout_title_refresh_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }

    @Override
    public boolean isStatusBarDarkMode() {
        return true;
    }

    @Override
    public BaseQuickAdapter getAdapter() {
        return new SubjectMovieAdapter(false);
    }

    @Override
    public void loadData(int page) {
        presenter.getMovie(page*mDefaultPage, mDefaultPageSize);
    }


    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }

/*
    private void testRequest(int page) {
        ApiRepository.getInstance().getMovie( page * mDefaultPage, mDefaultPageSize)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new BaseLoadingObserver<BaseMovieEntity>() {
                    @Override
                    public void onDoNext(BaseMovieEntity entity) {
                        ToastUtil.show("解析成功");
                    }

                    @Override
                    public void onDoError(Throwable e) {
                        super.onDoError(e);
                        ToastUtil.show(e.toString());
                    }
                });


    }*/

    @Override
    protected void loadPresenter() {

    }

    @Override
    protected MessagePresenter createPresenter() {
        return new MessagePresenter();
    }
}
