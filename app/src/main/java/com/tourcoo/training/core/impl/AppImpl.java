package com.tourcoo.training.core.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tourcoo.training.R;
import com.tourcoo.training.core.interfaces.IRefreshLoadView;
import com.tourcoo.training.core.interfaces.LoadMoreFoot;
import com.tourcoo.training.core.interfaces.LoadingDialog;
import com.tourcoo.training.core.interfaces.MultiStatusView;
import com.tourcoo.training.core.interfaces.ObserverControl;
import com.tourcoo.training.core.interfaces.QuitAppControl;
import com.tourcoo.training.core.interfaces.RecyclerViewControl;
import com.tourcoo.training.core.interfaces.TitleBarViewControl;
import com.tourcoo.training.core.interfaces.ToastControl;
import com.tourcoo.training.core.retrofit.BaseObserver;
import com.tourcoo.training.core.util.DrawableUtil;
import com.tourcoo.training.core.util.SizeUtil;
import com.tourcoo.training.core.util.StackUtil;
import com.tourcoo.training.core.util.StatusBarUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.dialog.LoadingDialogWrapper;
import com.tourcoo.training.core.widget.dialog.loading.FrameLoadingDialog;
import com.tourcoo.training.core.widget.view.FrameLoadMoreView;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.core.widget.view.radius.RadiusTextView;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年12月27日15:16
 * @Email: 971613168@qq.com
 */
public class AppImpl implements DefaultRefreshHeaderCreator, LoadMoreFoot, RecyclerViewControl, MultiStatusView, LoadingDialog,
        TitleBarViewControl, QuitAppControl, ToastControl, ObserverControl {

    private Context mContext;
    private String TAG = this.getClass().getSimpleName();

    public AppImpl(@Nullable Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
        layout.setEnableHeaderTranslationContent(false)
                .setPrimaryColorsId(R.color.colorAccent)
                .setEnableOverScrollDrag(false);
        ClassicsHeader classicsHeader = new ClassicsHeader(mContext);
        return classicsHeader;
    }

    @Nullable
    @Override
    public LoadMoreView createDefaultLoadMoreView(BaseQuickAdapter adapter) {
        if (adapter != null) {
            //设置动画是否一直开启
            adapter.isFirstOnly(false);
            //设置动画
            adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
            adapter.openLoadAnimation();
        }
        //方式一:设置FastLoadMoreView--可参考FastLoadMoreView.Builder相应set方法
        //默认配置请参考FastLoadMoreView.Builder(mContext)里初始化
        return new FrameLoadMoreView.Builder(mContext)
                .setLoadingTextFakeBold(true)
                .setLoadingSize(SizeUtil.dp2px(20))
//                                .setLoadTextColor(Color.MAGENTA)
//                                //设置Loading 颜色-5.0以上有效
//                                .setLoadingProgressColor(Color.MAGENTA)
//                                //设置Loading drawable--会使Loading颜色失效
//                                .setLoadingProgressDrawable(R.drawable.dialog_loading_wei_bo)
//                                //设置全局TextView颜色
//                                .setLoadTextColor(Color.MAGENTA)
//                                //设置全局TextView文字字号
//                                .setLoadTextSize(SizeUtil.dp2px(14))
//                                .setLoadingText("努力加载中...")
//                                .setLoadingTextColor(Color.GREEN)
//                                .setLoadingTextSize(SizeUtil.dp2px(14))
//                                .setLoadEndText("我是有底线的")
//                                .setLoadEndTextColor(Color.GREEN)
//                                .setLoadEndTextSize(SizeUtil.dp2px(14))
//                                .setLoadFailText("哇哦!出错了")
//                                .setLoadFailTextColor(Color.RED)
//                                .setLoadFailTextSize(SizeUtil.dp2px(14))
                .build();
        //方式二:使用adapter自带--其实我默认设置的和这个基本一致只是提供了相应设置方法
//                        return new SimpleLoadMoreView();
        //方式三:参考SimpleLoadMoreView或FastLoadMoreView完全自定义自己的LoadMoreView
//                        return MyLoadMoreView();
    }

    @Nullable
    @Override
    public LoadingDialogWrapper createLoadingDialog(@Nullable Activity activity) {
      /*  return new LoadingDialogWrapper(activity,
                new FrameLoadingDialog.WeBoBuilder(activity)
                        .setMessage("加载中")
                        .create())
                .setCanceledOnTouchOutside(false)
                .setMessage("请求数据中,请稍候...");*/
        return new LoadingDialogWrapper(activity, new FrameLoadingDialog(activity));
    }

    @Override
    public void setMultiStatusView(StatusLayoutManager.Builder statusView, IRefreshLoadView iRefreshLoadView) {

    }

    @Override
    public boolean onError(BaseObserver o, Throwable e) {
        return false;
    }

    /**
     * @param isFirst  是否首次提示
     * @param activity 操作的Activity
     * @return 延迟间隔--如不需要设置两次提示可设置0--最佳方式是直接在回调中执行想要的操作
     */
    @Override
    public long quipApp(boolean isFirst, Activity activity) {
        //默认配置
        if (isFirst) {
            ToastUtil.show(R.string.frame_quit_app);
        } else {
            StackUtil.getInstance().exit(false);
        }
        return 2000;
    }

    @Override
    public void setRecyclerView(RecyclerView recyclerView, Class<?> cls) {

    }

    @Override
    public boolean createTitleBarViewControl(TitleBarView titleBar, Class<?> cls) {
        //默认的MD风格返回箭头icon如使用该风格可以不用设置
        Drawable mDrawable = DrawableUtil.setTintDrawable(ContextCompat.getDrawable(mContext, R.drawable.fast_ic_back),
                ContextCompat.getColor(mContext, R.color.colorTitleText));
        //是否支持状态栏白色
        boolean isSupport = StatusBarUtil.isSupportStatusBarFontChange();
        boolean isActivity = Activity.class.isAssignableFrom(cls);
        Activity activity = StackUtil.getInstance().getActivity(cls);
        //设置TitleBarView 所有TextView颜色
        titleBar.setStatusBarLightMode(isSupport)
                //不支持黑字的设置白透明
                .setStatusAlpha(isSupport ? 0 : 102)
                .setLeftTextDrawable(isActivity ? mDrawable : null)
                .setDividerHeight(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ? SizeUtil.dp2px(0.5f) : 0);
        if (activity != null) {
            titleBar.setTitleMainText(activity.getTitle())
                    .setOnLeftTextClickListener(v -> activity.finish());
        }
      /*  if (activity instanceof BaseW) {
            return false;
        }*/
        //海拔效果
//        ViewCompat.setElevation(titleBar, mContext.getResources().getDimension(R.dimen.dp_elevation));
        return false;
    }

    @Override
    public Toast getToast() {
        return null;
    }

    @Override
    public void setToast(Toast toast, RadiusTextView textView) {

    }
}
