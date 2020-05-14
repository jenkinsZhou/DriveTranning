package com.tourcoo.training.ui.study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tourcoo.training.R;
import com.tourcoo.training.adapter.study.StudyDataAdapter;
import com.tourcoo.training.core.base.fragment.BaseFragment;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.entity.study.StudyDataInfo;
import com.tourcoo.training.ui.home.MainTabFragment;

import java.util.ArrayList;
import java.util.List;

import static com.tourcoo.training.ui.study.StudyDataActivity.EXTRA_STUDY_DATA_LIST_KEY;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月08日9:52
 * @Email: 971613168@qq.com
 */
public class StudyDataFragment extends BaseFragment {
    private RecyclerView rvCommon;
    private StudyDataAdapter adapter;
    private List<StudyDataInfo> studyDataInfoList;

    @Override
    public int getContentLayout() {
        return R.layout.frame_layout_recycler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        studyDataInfoList = (List<StudyDataInfo>) getArguments().getSerializable(EXTRA_STUDY_DATA_LIST_KEY);
        rvCommon = mContentView.findViewById(R.id.rvCommon);
        rvCommon.setLayoutManager(new LinearLayoutManager(mContext));
        rvCommon.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new StudyDataAdapter();
        View emptyView = LayoutInflater.from(mContext).inflate(R.layout.empty_view_layout,null);
        adapter.setEmptyView(emptyView);
        adapter.bindToRecyclerView(rvCommon);
    }

    @Override
    public void loadData() {
        adapter.setNewData(studyDataInfoList);
    }


    public static StudyDataFragment newInstance(ArrayList<StudyDataInfo> list) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_STUDY_DATA_LIST_KEY,list);
        StudyDataFragment fragment = new StudyDataFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
