package com.tourcoo.training.widget.dialog.pay;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.LogUtils;
import com.tourcoo.training.R;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.SizeUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月21日15:19
 * @Email: 971613168@qq.com
 */
public class MultiplePayDialog {
    private Context mContext;
    private List<String> options1Items = new ArrayList<>();
    private Dialog dialog;
    private int width = 0;
    private ArrayList<View> viewList = new ArrayList<>();
    private ViewPager vpContainer;
    private MyAdapter viewPagerAdapter;
    private MagicIndicator mMagicIndicator;
    private OptionsPickerView pvCustomOptions;
    private TextView tvCoinPayment;

    public MultiplePayDialog(Context context) {
        this.mContext = context;
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        int size = 20;
        for (int i = 0; i < size; i++) {
            options1Items.add("个人学币数 : 3" + i);
        }
    }


    public MultiplePayDialog create() {
        // 获取Dialog布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_payment_multipel, null);
        vpContainer = view.findViewById(R.id.vpContainer);
        mMagicIndicator = view.findViewById(R.id.magicIndicator);
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(width);
//        rvShare = view.findViewById(R.id.rvShare);
//        rvShare.setLayoutManager(new GridLayoutManager(mContext,4));
        // 获取自定义Dialog布局中的控件
    /*    tvCancel =  view.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/
        // 定义Dialog布局和参数
        dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        viewList = new ArrayList<>();
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.layout_payment_type_picker_coin, null);
        View view2 = LayoutInflater.from(mContext).inflate(R.layout.layout_payment_type_cash, null);
        tvCoinPayment = view1.findViewById(R.id.tvCoinPayment);
        LinearLayout rootView = view1.findViewById(R.id.optionspicker);
        viewList.add(view1);
        viewList.add(view2);
        //3.设置适配器
        viewPagerAdapter = new MyAdapter(mContext, viewList);
        vpContainer.setAdapter(viewPagerAdapter);
        initMagicIndicator();
        //设置默认选中位置
        vpContainer.setCurrentItem(0);
        //设置滑动监听事件
        vpContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initPicker(rootView);
        dialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dismiss();
            }
            return true;
        });
        return this;
    }

    private class MyAdapter extends PagerAdapter {
        private Context context;
        private ArrayList<View> list;

        private MyAdapter(Context context, ArrayList<View> list) {
            this.context = context;
            this.list = list;
        }


        //返回长度
        @Override
        public int getCount() {
            return list.size();
        }

        //判断视图
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        //初始化视图
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //将对应view添加到容器中
            container.addView(list.get(position));
            //返回集合对应的View对象
            return list.get(position);
        }

        //销毁视图
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //容器中移除视图View
            container.removeView(list.get(position));
        }

    }

    public void show() {
        pvCustomOptions.show();
        dialog.show();
    }

    private void initMagicIndicator() {
        CommonNavigator mCommonNavigator = new CommonNavigator(mContext);
        mCommonNavigator.setSkimOver(true);
        List<String> titleList = new ArrayList<>();
        titleList.add("学币支付");
        titleList.add("现金支付");
        mMagicIndicator.setNavigator(mCommonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, vpContainer);
        mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setPadding(SizeUtil.dp2px(30), 0, SizeUtil.dp2px(30), 0);
                clipPagerTitleView.setText(titleList.get(index));
                clipPagerTitleView.setTextSize(SizeUtil.sp2px(14));
                clipPagerTitleView.setTextColor(CommonUtil.getColor(R.color.gray999999));
                clipPagerTitleView.setClipColor(CommonUtil.getColor(R.color.blue5087FF));
                clipPagerTitleView.setOnClickListener(v -> vpContainer.setCurrentItem(index));
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                linePagerIndicator.setLineWidth(SizeUtil.dp2px(40));
                linePagerIndicator.setColors(CommonUtil.getColor(R.color.blue4287FF));
                linePagerIndicator.setRoundRadius(UIUtil.dip2px(context, 5));
                linePagerIndicator.setStartInterpolator(new AccelerateInterpolator());
                linePagerIndicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                return linePagerIndicator;
            }
        });
    }

    /**
     * @description 注意事项：
     * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
     * 具体可参考demo 里面的两个自定义layout布局。
     */
    private void initPicker(ViewGroup rootView) {
        pvCustomOptions = new OptionsPickerBuilder(mContext, (options1, option2, options3, v) -> {
        }).setCyclic(true, true, true)
                .isDialog(false)
                .setContentTextSize(18)
                .setLineSpacingMultiplier(2.0f)
                .setDividerColor(CommonUtil.getColor(R.color.transparent))
                .setTextColorOut(CommonUtil.getColor(R.color.gray999999))
                .setTextColorCenter(CommonUtil.getColor(R.color.black333333))
                .setLayoutRes(R.layout.layout_custom_picker, v -> {
                })
                .setDecorView(rootView)
                .setOutSideCancelable(false)
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        tvCoinPayment.setText(options1Items.get(options1));
                    }
                })
                .setItemVisibleCount(3)
                .build();
        pvCustomOptions.getDialogContainerLayout();
        pvCustomOptions.setPicker(options1Items);
        //添加数据
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

    public void dismiss() {
        if (dialog != null && pvCustomOptions != null) {
            pvCustomOptions.dismiss();
            dialog.dismiss();
        }
    }
}
