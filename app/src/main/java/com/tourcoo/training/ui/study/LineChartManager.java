package com.tourcoo.training.ui.study;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.tourcoo.training.R;
import com.tourcoo.training.utils.TourCooUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2019年07月19日12:11
 * @Email: 971613168@qq.com
 */
public class LineChartManager {
    private LineChart lineChart;
    private YAxis rightAxis;
    /**
     * X轴
     */
    private XAxis xAxis;
    /**
     * 左侧Y轴
     */
    private YAxis leftYAxis;
    /**
     * 右侧Y轴
     */
    private YAxis rightYaxis;

    /**
     * 图例
     */
    private Legend legend;
    /**
     * 限制线
     */
    private LimitLine limitLine;

    public LineChartManager(LineChart lineChart) {
        this.lineChart = lineChart;
        rightAxis = lineChart.getAxisRight();
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYaxis = lineChart.getAxisRight();
    }

    /**
     * 初始化图表
     */
    private void initChart(LineChart lineChart) {
        /***图表设置***/
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        //是否显示边界
        lineChart.setDrawBorders(true);
        //是否可以拖动
        lineChart.setDragEnabled(true);
        //是否有触摸事件
        lineChart.setTouchEnabled(true);
        //设置XY轴动画效果
        lineChart.animateY(2500);
        lineChart.animateX(1500);

        /***XY轴的设置***/
        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYaxis = lineChart.getAxisRight();
        //X轴设置显示位置在底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        //保证Y轴从0开始，不然会上移一点
        leftYAxis.setAxisMinimum(0f);
        rightYaxis.setAxisMinimum(0f);
        /***折线图例 标签 设置***/
        legend = lineChart.getLegend();
        //设置显示类型，LINE CIRCLE SQUARE EMPTY 等等 多种方式，查看LegendForm 即可
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12f);
        //显示位置 左下方
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //是否绘制在图表里面
        legend.setDrawInside(false);

        lineChart.setBackgroundColor(Color.WHITE);
//是否显示边界
        lineChart.setDrawBorders(false);
        //是否展示网格线
        lineChart.setDrawGridBackground(false);
        xAxis.setDrawGridLines(false);
        rightYaxis.setDrawGridLines(false);
        leftYAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (int) value + "";
            }
        });
        leftYAxis.setDrawAxisLine(false);
        rightYaxis.setEnabled(false);
        xAxis.setEnabled(true);
    }


    /**
     * 曲线初始化设置 一个LineDataSet 代表一条曲线
     *
     * @param lineDataSet 线条
     * @param color       线条颜色
     */
    private void initLineDataSet(LineDataSet lineDataSet, int color) {
        lineDataSet.setColor(color);
        lineDataSet.setCircleColor(color);
        lineDataSet.setLineWidth(1f);
        lineDataSet.setCircleRadius(3f);
        //设置曲线值的圆点是实心还是空心
        lineDataSet.setDrawCircleHole(false);
        lineDataSet.setValueTextSize(10f);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFormLineWidth(1.2f);
        lineDataSet.setFormSize(15.f);
        //设置折线
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

    }

    /**
     * 展示曲线
     *
     * @param dataList 数据集合
     * @param name     曲线名称
     * @param color    曲线颜色
     */
    private void showChart(List<Float> dataList, String name, int color) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            /**
             * 在此可查看 Entry构造方法，可发现 可传入数值 Entry(float x, float y)
             * 也可传入Drawable， Entry(float x, float y, Drawable icon) 可在XY轴交点 设置Drawable图像展示
             */
            Entry entry = new Entry(i, dataList.get(i));
            entries.add(entry);
        }
        // 每一个LineDataSet代表一条线
        LineDataSet lineDataSet = new LineDataSet(entries, name);
        initLineDataSet(lineDataSet, color);
        LineData lineData = new LineData(lineDataSet);
        lineChart.animateX(1000); // 立即执行的动画,x轴
        lineChart.setData(lineData);
    }


    public void showLineChart(List<String> xValues, List<Float> dataList, String name, int color) {
        initChart(lineChart);
        xAxis.setGranularity(0.1f);//设置最小间隔，防止当放大时，出现重复标签
        xAxis.setLabelCount(xValues.size(), false);
        xAxis.setTextColor(Color.parseColor("#999999"));//设置X轴字体颜色
        IndexAxisValueFormatter xAxisFormatter = new IndexAxisValueFormatter(xValues);
        xAxis.setValueFormatter(xAxisFormatter);
        showChart(dataList, name, color);
        setChartFillDrawable(TourCooUtil.getDrawable(R.drawable.fade_yellow));
        Description description = new Description();
        description.setText("");
        lineChart.setDescription(description);
        Matrix m = new Matrix();
        lineChart.getViewPortHandler().refresh(m, lineChart, false);//将图表动画显示之前进行缩放
        lineChart.animateX(1000); // 立即执行的动画,x轴
        lineChart.notifyDataSetChanged();
        //设置x轴间距
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(dataList.size(),true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return  (int)value+1+"";
            }
        });
    }


    /**
     * 设置线条填充背景颜色
     *
     * @param drawable
     */
    public void setChartFillDrawable(Drawable drawable) {
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            LineDataSet lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            //避免在 initLineDataSet()方法中 设置了 lineDataSet.setDrawFilled(false); 而无法实现效果
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFillDrawable(drawable);
            lineChart.invalidate();
        }
    }
}
