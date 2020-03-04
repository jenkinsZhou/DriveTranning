package com.tourcoo.training.core.base.entity;

import android.view.View;

import com.flyco.labelview.LabelView;
import com.tourcoo.training.R;
import com.tourcoo.training.core.manager.GlideManager;


/**
 * @Author: JenkinsZhou on 2018/8/10 9:53
 * @E-Mail: 971613168@qq.com
 * Function:
 * Description:
 */
public class SubjectMovieAdapter extends BaseItemTouchQuickAdapter<SubjectsEntity, BaseItemTouchViewHolder> {

    boolean isShowTop;

    public SubjectMovieAdapter(boolean isShowTop) {
        super(R.layout.item_subject_movie);
        this.isShowTop = isShowTop;
    }

    @Override
    protected void convert(BaseItemTouchViewHolder helper, SubjectsEntity item) {
        helper.setText(R.id.tv_titleMovie, item.title)
                .setText(R.id.tv_typeMovie, "题材:" + item.getGenres())
                .setText(R.id.tv_yearMovie, "年份:" + item.year)
                .setText(R.id.tv_directorMovie, "导演:" + item.getDirectors())
                .setText(R.id.tv_castMovie, "主演:" + item.getCasts());
        GlideManager.loadImg(item.images.large, helper.getView(R.id.iv_coverMovie));
        LabelView labelView = helper.getView(R.id.lv_topMovie);
        labelView.setText("Top" + (helper.getLayoutPosition() + 1));
        labelView.setVisibility(isShowTop ? View.VISIBLE : View.GONE);
    }
}
