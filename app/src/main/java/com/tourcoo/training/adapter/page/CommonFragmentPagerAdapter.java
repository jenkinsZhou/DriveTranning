package com.tourcoo.training.adapter.page;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月09日17:20
 * @Email: 971613168@qq.com
 */
public class CommonFragmentPagerAdapter extends FragmentStatePagerAdapter {

  /*  private List<Fragment> fragmentList;
    public CommonFragmentPagerAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }*/

    private List<Fragment> mFragments;

    public CommonFragmentPagerAdapter( FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        mFragments = fragmentList != null ? fragmentList : new ArrayList<>();
    }

    public void setData(List<Fragment> mFragments){
        this.mFragments = mFragments;
        notifyDataSetChanged();
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }





}
