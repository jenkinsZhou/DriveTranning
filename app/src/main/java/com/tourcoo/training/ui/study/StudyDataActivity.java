package com.tourcoo.training.ui.study;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.github.mikephil.charting.charts.LineChart;
import com.tourcoo.training.R;
import com.tourcoo.training.adapter.page.CommonFragmentPagerAdapter;
import com.tourcoo.training.config.RequestConfig;
import com.tourcoo.training.core.base.activity.BaseTitleActivity;
import com.tourcoo.training.core.base.entity.BaseResult;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.retrofit.BaseLoadingObserver;
import com.tourcoo.training.core.retrofit.repository.ApiRepository;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.util.ResourceUtil;
import com.tourcoo.training.core.util.TimeUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.core.widget.view.bar.TitleBarView;
import com.tourcoo.training.entity.study.StudyDataEntity;
import com.tourcoo.training.entity.study.StudyDataInfo;
import com.tourcoo.training.widget.viewpager.WrapContentHeightViewPager;
import com.trello.rxlifecycle3.android.ActivityEvent;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月08日8:59
 * @Email: 971613168@qq.com
 */
public class StudyDataActivity extends BaseTitleActivity implements View.OnClickListener {
    private CommonFragmentPagerAdapter pagerAdapter;
    private OptionsPickerView pvCustomOptions;
    private ArrayList<Integer> options1Items = new ArrayList<>();
    private int currentPosition = 0;
    private int selectYear = 0;
    private TextView tvSelectYear;
    private TextView tvTotalStudyLength;
    private TextView tvSelectDateShow;
    private WrapContentHeightViewPager wrapViewpager;
    private StudyDataEntity mStudyDataEntity;
    public static final String EXTRA_STUDY_DATA_LIST_KEY = "EXTRA_STUDY_DATA_LIST_KEY";
    private LineChart lineChartView;
    private LineChartManager oilLineChartManager;

    private List<String> monthTimeList = new ArrayList<>();

    @Override
    public int getContentLayout() {
        return R.layout.activity_study_data;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        tvSelectYear = findViewById(R.id.tvSelectYear);
        tvSelectDateShow = findViewById(R.id.tvSelectDateShow);
        lineChartView = findViewById(R.id.lineChartView);
        oilLineChartManager = new LineChartManager(lineChartView);
        findViewById(R.id.ivArrowRight).setOnClickListener(this);
        findViewById(R.id.ivArrowLeft).setOnClickListener(this);
        wrapViewpager = findViewById(R.id.wrapViewpager);
        wrapViewpager.setSlidingEnable(false);
        wrapViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tvTotalStudyLength = findViewById(R.id.tvTotalStudyLength);
        tvSelectYear.setOnClickListener(this);
        initPicker();
        selectYear = options1Items.get(0);
        requestStudyData(selectYear + "");
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("学习数据");
    }


    private void requestStudyData(String year) {
        TourCooLogUtil.i("year=" + year);
        ApiRepository.getInstance().requestStudyDataList(year).compose(bindUntilEvent(ActivityEvent.DESTROY)).subscribe(new BaseLoadingObserver<BaseResult<StudyDataEntity>>() {
            @Override
            public void onSuccessNext(BaseResult<StudyDataEntity> entity) {
                if (entity == null) {
                    return;
                }
                if (entity.getCode() == RequestConfig.CODE_REQUEST_SUCCESS) {
                    showStudyData(entity.getData());
                } else {
                    ToastUtil.show(entity.getMsg());
                }

            }
        });
    }


    private void initPicker() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        for (int i = currentYear; i > currentYear - 3; i--) {
            options1Items.add(i);
        }
        pvCustomOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                selectYear = options1Items.get(options1);
                showSelectYear(selectYear);
                //关键点 选择年份后重新请求数据
                requestStudyData(selectYear + "");
            }
        }).setCyclic(false, false, false)
                .isDialog(false)
                .setContentTextSize(18)
                .setLineSpacingMultiplier(2.0f)
                .setDividerColor(CommonUtil.getColor(R.color.transparent))
                .setTextColorOut(CommonUtil.getColor(R.color.gray999999))
                .setTextColorCenter(CommonUtil.getColor(R.color.black333333))
                .build()
        ;

        pvCustomOptions.setPicker(options1Items);
        //添加数据
    }

    /**
     * 显示选择年份
     */
    private void showSelectYear(int year) {
        tvSelectYear.setText(year + "年度");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSelectYear:
                pvCustomOptions.show();
                break;
            case R.id.ivArrowRight:
                doSkipNext();
                break;
            case R.id.ivArrowLeft:
                doSkipLast();
                break;
        }
    }

    private void showStudyData(StudyDataEntity detail) {
        if (detail == null) {
            return;
        }
        mStudyDataEntity = detail;
        mStudyDataEntity.setTotalHour(TimeUtil.secondToHour(detail.getTotalHour()));
        loadChart(detail);
        tvSelectDateShow.setText(mStudyDataEntity.getMonths().get(currentPosition).getTime());
        tvTotalStudyLength.setText(CommonUtil.doubleTransStringZhenOneZero(detail.getTotalHour()) + "小时");
        List<Fragment> fragmentList = new ArrayList<>();
        for (StudyDataInfo month : detail.getMonths()) {
            fragmentList.add(StudyDataFragment.newInstance(month.getDay()));
        }
        pagerAdapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        wrapViewpager.setAdapter(pagerAdapter);
        wrapViewpager.setOffscreenPageLimit(fragmentList.size() + 1);
        Calendar calendar = Calendar.getInstance();
        //默认显示当前月份数据
        int currentMonth = calendar.get(Calendar.MONTH);
        tvSelectDateShow.setText(mStudyDataEntity.getMonths().get(currentMonth).getTime());
        wrapViewpager.setCurrentItem(currentMonth);
    }

    private void doSkipNext() {
        if (currentPosition >= pagerAdapter.getCount() - 1) {
            return;
        }
        tvSelectDateShow.setText(mStudyDataEntity.getMonths().get(currentPosition + 1).getTime());
        wrapViewpager.setCurrentItem(currentPosition + 1, true);
    }

    private void doSkipLast() {
        if (currentPosition <= 0 || mStudyDataEntity == null || mStudyDataEntity.getMonths() == null) {
            return;
        }
        tvSelectDateShow.setText(mStudyDataEntity.getMonths().get(currentPosition - 1).getTime());
        wrapViewpager.setCurrentItem(currentPosition - 1, true);

    }

    private void loadChart(StudyDataEntity entity) {
        if (entity.getMonths() == null) {
            return;
        }
        List<String> list = new ArrayList<>();
        List<Float> floatList = new ArrayList<>();
        int count = 1;
        for (StudyDataInfo month : entity.getMonths()) {
            if (month == null) {
                continue;
            }
            count++;
            list.add(CommonUtil.getNotNullValue(count + ""));

            BigDecimal bd = new BigDecimal(TimeUtil.secondToHour(month.getStudyHour()));
            BigDecimal bd2 = bd.setScale(1, BigDecimal.ROUND_HALF_UP);

            floatList.add(bd2.floatValue());
        }
        oilLineChartManager.showLineChart(list, floatList, "学习时长", ResourceUtil.getColor(R.color.blue5087FF));
    }
}
