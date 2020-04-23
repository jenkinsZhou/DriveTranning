package com.tourcoo.training.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.blankj.utilcode.util.LogUtils;
import com.tourcoo.training.R;
import com.tourcoo.training.core.base.activity.BaseMainActivity;
import com.tourcoo.training.core.base.entity.FrameTabEntity;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.view.tab.CommonTabLayout;
import com.tourcoo.training.ui.account.register.RecognizeIdCardActivity;
import com.tourcoo.training.ui.home.MainTabFragment;
import com.tourcoo.training.ui.home.MineTabFragment;
import com.tourcoo.training.ui.home.MineTabFragmentNew;
import com.tourcoo.training.ui.home.NewsTabFragment;
import com.tourcoo.training.ui.home.StudyTabFragment;
import com.tourcoo.training.widget.dialog.IosAlertDialog;
import com.tourcoo.training.widget.websocket.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年12月27日17:31
 * @Email: 971613168@qq.com
 */
public class MainTabActivity extends BaseMainActivity implements EasyPermissions.PermissionCallbacks {
    public static final int REQUEST_CODE_PERMISSION = 1006;
    private CommonTabLayout mTabLayout;
    private ArrayList<FrameTabEntity> mTabEntities;

    @Nullable
    @Override
    public List<FrameTabEntity> getTabList() {
        mTabEntities = new ArrayList<>();
        mTabEntities.add(new FrameTabEntity("发现", R.drawable.ic_home_normal, R.drawable.ic_home_selected, NewsTabFragment.Companion.newInstance()));
        mTabEntities.add(new FrameTabEntity("主页", R.drawable.ic_home_normal, R.drawable.ic_home_selected, StudyTabFragment.Companion.newInstance()));
        mTabEntities.add(new FrameTabEntity("我的", R.drawable.ic_home_normal, R.drawable.ic_home_selected, MineTabFragmentNew.Companion.newInstance()));
        return mTabEntities;
    }

    @Override
    public boolean isSwipeEnable() {
        return false;
    }

    @Override
    public boolean isSlideEnable() {
        return false;
    }

    @Override
    public void setTabLayout(CommonTabLayout tabLayout) {
        mTabLayout = tabLayout;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTabLayout = findViewById(R.id.commonTabLayout);
        mTabLayout.setCurrentTab(1);
        checkPermission();
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) { //这个方法有个前提是，用户点击了“不再询问”后，才判断权限没有被获取到
            baseHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSetting();
                }
            }, 300);
        } else if (!EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //这里响应的是除了AppSettingsDialog这个弹出框，剩下的两个弹出框被拒绝或者取消的效果
            ToastUtil.show("您未授予权限 应用即将退出");
            baseHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);

        }
    }


    private void showSetting() {
        IosAlertDialog dialog = new IosAlertDialog(mContext)
                .init()
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setTitle("权限申请")
                .setMsg("应用需要您授予相关权限 请前往授权管理页面授权")
                .setPositiveButton("前往授权", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        skipDetailSettingIntent();
                    }
                })
                .setNegativeButton("退出应用", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        dialog.show();

    }


    private void skipDetailSettingIntent() {
        Intent intent = new Intent();
        //        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        try {
            startActivityForResult(intent, RecognizeIdCardActivity.REQUEST_PERMISSION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查权限
     */
    @AfterPermissionGranted(REQUEST_CODE_PERMISSION)
    private void checkPermission() {
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                {
                    if (!EasyPermissions.hasPermissions(MainTabActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        EasyPermissions.requestPermissions(MainTabActivity.this, "请授予应用所需要的相关权限", REQUEST_CODE_PERMISSION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                    } else {
                        //拥有权限 什么也不做
                    }
                }
            }
        }, 500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //将结果传入EasyPermissions中

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }





}
