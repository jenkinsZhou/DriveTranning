package com.tourcoo.training.ui.home;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tourcoo.training.R;
import com.tourcoo.training.adapter.mine.MineItemAdapter;
import com.tourcoo.training.core.base.fragment.BaseTitleFragment;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.StatusBarUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.entity.mine.MineItem;
import com.tourcoo.training.ui.account.FindPassActivity;
import com.tourcoo.training.ui.account.UploadIdCardActivity;
import com.tourcoo.training.ui.exam.OnlineExamActivity;
import com.tourcoo.training.ui.face.DialogFaceRecognitionActivity;
import com.tourcoo.training.ui.training.online.StudyOnlineActivity;
import com.tourcoo.training.widget.dialog.pay.MultiplePayDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年12月27日17:39
 * @Email: 971613168@qq.com
 */
public class MineTabFragment extends BaseTitleFragment implements View.OnClickListener {
    @Override
    public int getContentLayout() {
        return R.layout.fragement_mine;
    }

    private RelativeLayout rlTitle;
    private SmartRefreshLayout smartRefreshLayoutCommon;
    private MineItemAdapter accountAdapter;
    private MineItemAdapter achievementAdapter;
    private RecyclerView rvMyAccount;
    private RecyclerView rvStudyAchievement;
    private MultiplePayDialog multiplePayDialog;


    @Override
    public void initView(Bundle savedInstanceState) {
        rlTitle = mContentView.findViewById(R.id.rlTitleBar);
        rvMyAccount = mContentView.findViewById(R.id.rvMyAccount);
        rvStudyAchievement = mContentView.findViewById(R.id.rvStudyAchievement);
        smartRefreshLayoutCommon = mContentView.findViewById(R.id.smartRefreshLayoutCommon);
        setMarginTop();
        smartRefreshLayoutCommon.setRefreshHeader(new ClassicsHeader(mContext));
        accountAdapter = new MineItemAdapter();
        achievementAdapter = new MineItemAdapter();
        accountAdapter.bindToRecyclerView(rvMyAccount);
        achievementAdapter.bindToRecyclerView(rvStudyAchievement);
        rvMyAccount.setLayoutManager(new GridLayoutManager(mContext, 3));
        rvStudyAchievement.setLayoutManager(new GridLayoutManager(mContext, 4));
        mContentView.findViewById(R.id.ivSetting).setOnClickListener(this);
        mContentView.findViewById(R.id.llAvatar).setOnClickListener(this);
        mContentView.findViewById(R.id.ivAddCar).setOnClickListener(this);
        mContentView.findViewById(R.id.llGoldLevel).setOnClickListener(this);
        initItemClick();
        setStatusBarModeWhite(this);
    }

    @Override
    public void loadData() {
        loadMineAccount();
        loadAchievement();
    }

    private void loadMineAccount() {
        MineItem accountItem = new MineItem();
        accountItem.setIconId(R.drawable.icon_account);
        accountItem.setItemName("账户");
        MineItem chargeItem = new MineItem();
        chargeItem.setIconId(R.drawable.icon_recharge);
        chargeItem.setItemName("充值");
        MineItem orderItem = new MineItem();
        orderItem.setIconId(R.drawable.icon_indent);
        orderItem.setItemName("订单");
        List<MineItem> accountList = new ArrayList<>();
        accountList.add(accountItem);
        accountList.add(chargeItem);
        accountList.add(orderItem);
        accountAdapter.setNewData(accountList);
    }

    private void loadAchievement() {
        MineItem studyItem = new MineItem();
        studyItem.setIconId(R.drawable.icon_record);
        studyItem.setItemName("学习记录");
        MineItem dataItem = new MineItem();
        dataItem.setIconId(R.drawable.icon_data);
        dataItem.setItemName("学习数据");
        MineItem medalItem = new MineItem();
        medalItem.setIconId(R.drawable.icon_medal);
        medalItem.setItemName("学习勋章");
        MineItem studyCertifyItem = new MineItem();
        studyCertifyItem.setIconId(R.drawable.icon_credential);
        studyCertifyItem.setItemName("学习证书");
        List<MineItem> accountList = new ArrayList<>();
        accountList.add(studyItem);
        accountList.add(dataItem);
        accountList.add(medalItem);
        accountList.add(studyCertifyItem);
        achievementAdapter.setNewData(accountList);
    }

    public static MineTabFragment newInstance() {
        Bundle args = new Bundle();
        MineTabFragment fragment = new MineTabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setVisibility(View.GONE);
    }


    private void setMarginTop() {
        ViewGroup.LayoutParams layoutParams = rlTitle.getLayoutParams();
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) layoutParams).setMargins(0, getMarginTop(), 0, 0);
        } else if (layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) layoutParams).setMargins(0, getMarginTop(), 0, 0);
        } else if (layoutParams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) layoutParams).setMargins(0, getMarginTop(), 0, 0);
        }
    }

    @Override
    public int getMarginTop() {
        return StatusBarUtil.getStatusBarHeight();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivSetting:
             /*   BottomShareDialog shareDialog = new BottomShareDialog(mContext).create();
                ShareEntity shareEntity = new ShareEntity("微信", R.drawable.icon_wx);
                ShareEntity shareEntity1 = new ShareEntity("朋友圈", R.drawable.icon_pyq);
                ShareEntity shareEntity2 = new ShareEntity("QQ", R.drawable.icon_qq);
                ShareEntity shareEntity3 = new ShareEntity("QQ空间", R.drawable.icon_qz);
                shareDialog.addData(shareEntity).addData(shareEntity1).addData(shareEntity2).addData(shareEntity3).show();*/
              /*   multiplePayDialog = new MultiplePayDialog(mContext).create();
                multiplePayDialog.show();*/
                CommonUtil.startActivity(mContext, DialogFaceRecognitionActivity.class);
                break;
            case R.id.llAvatar:
//                CommonUtil.startActivity(mContext, IndustrialRegisterActivity.class);
                CommonUtil.startActivity(mContext, UploadIdCardActivity.class);
                break;
            case R.id.ivAddCar:
                CommonUtil.startActivity(mContext, OnlineExamActivity.class);
                break;
            case R.id.llGoldLevel:
                CommonUtil.startActivity(mContext, FindPassActivity.class);
                break;
            default:
                break;
        }
    }

  /*  private void listenBackPress(){
        MainTabActivity activity = (MainTabActivity) mContext;
        activity.setOnBackPressListener(new OnBackPressListener() {
            @Override
            public void onBackPress() {
                if(multiplePayDialog != null &&multiplePayDialog. isShowing()){
                    ToastUtil.show("关闭弹窗");
                    multiplePayDialog.dismiss();
                }else {
                    ToastUtil.show("multiplePayDialog为空");
                }
            }
        });
    }*/

    public void closePayDialog(){
        if(multiplePayDialog != null){
            ToastUtil.show("执行了");

            multiplePayDialog.dismiss();
        }
    }

private void initItemClick(){
    achievementAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            switch (position) {
                case 0:
                    CommonUtil.startActivity(mContext, StudyOnlineActivity.class);
                    break;
                default:
                    break;
            }
        }
    });
}
}
