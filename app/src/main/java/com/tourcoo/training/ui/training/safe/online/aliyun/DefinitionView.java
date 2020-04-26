package com.tourcoo.training.ui.training.safe.online.aliyun;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.aliyun.player.nativeclass.TrackInfo;
import com.tourcoo.training.R;
import com.tourcoo.training.entity.training.VideoStream;
import com.tourcoo.training.widget.AliYunVodPlayerView;
import com.tourcoo.training.widget.aliplayer.listener.QualityValue;
import com.tourcoo.training.widget.aliplayer.theme.ITheme;
import com.tourcoo.training.widget.aliplayer.view.quality.QualityItem;

import java.util.LinkedList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :自定义清晰度切换控件
 * @company :途酷科技
 * @date 2020年04月26日14:59
 * @Email: 971613168@qq.com
 */
public class DefinitionView extends FrameLayout implements ITheme {

    //显示清晰度的列表
    private ListView mListView;
    private BaseAdapter mAdapter;
    //adapter的数据源
    private List<VideoStream> videoStreamList;
    //当前播放的清晰度，高亮显示
    private String currentQuality;
    //清晰度项的点击事件
    private OnDefinitionClickListener mOnDefinitionClickListener;

    public OnDefinitionClickListener getOnDefinitionClickListener() {
        return mOnDefinitionClickListener;
    }

    public void setOnDefinitionClickListener(OnDefinitionClickListener mOnDefinitionClickListener) {
        this.mOnDefinitionClickListener = mOnDefinitionClickListener;
    }

    //是否是mts源
    private boolean isMtsSource = false;
    //默认的主题色
    private int themeColorResId = R.color.alivc_player_theme_blue;

    public DefinitionView(@NonNull Context context) {
        super(context);
        init();
    }

    public DefinitionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DefinitionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DefinitionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        //初始化布局
        LayoutInflater.from(getContext()).inflate(R.layout.video_definition_change_layout, this, true);
        mListView = (ListView) findViewById(R.id.definitionView);

        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //不显示滚动条，保证全部被显示
        mListView.setVerticalScrollBarEnabled(false);
        mListView.setHorizontalScrollBarEnabled(false);

        mAdapter = new DefinitionView.QualityAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击之后就隐藏
                hide();
                //回调监听
                if (mOnDefinitionClickListener != null && videoStreamList != null) {
                    mOnDefinitionClickListener.onDefinitionClick(videoStreamList.get(position));
                }
            }
        });

        hide();
    }

    @Override
    public void setTheme(AliYunVodPlayerView.Theme theme) {

    }

    public interface OnDefinitionClickListener {
        /**
         * 清晰度点击事件
         * @param videoStream 点中的清晰度
         */
        void onDefinitionClick(VideoStream videoStream);
    }


    /**
     * 清晰度列表的适配器
     */
    private class QualityAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (videoStreamList != null) {
                return videoStreamList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return videoStreamList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.ratetype_item, null);
            if (videoStreamList != null) {
                VideoStream trackInfo = videoStreamList.get(position);
                String quality = trackInfo.getDefinition();
                view.setText(trackInfo.getDefinition());
                //默认白色，当前清晰度为主题色。
                if (quality.equals(currentQuality)) {
                    view.setTextColor(ContextCompat.getColor(getContext(), themeColorResId));
                } else {
                    view.setTextColor(ContextCompat.getColor(getContext(), R.color.alivc_common_font_white_light));
                }
            }
            return view;
        }
    }


    /**
     * 在某个控件的上方显示
     * @param anchor 控件
     */
    public void showAtTop(View anchor) {

        LayoutParams listViewParam = (LayoutParams) mListView.getLayoutParams();
        listViewParam.width = anchor.getWidth();
        listViewParam.height = getResources().getDimensionPixelSize(R.dimen.alivc_player_rate_item_height) * videoStreamList.size();
        int[] location = new int[2];
        anchor.getLocationInWindow(location);
        listViewParam.leftMargin = location[0];
        listViewParam.topMargin = getHeight() - listViewParam.height - anchor.getHeight() - 20;
        mListView.setLayoutParams(listViewParam);

        mListView.setVisibility(VISIBLE);

    }

    /**
     * 隐藏
     */
    public void hide() {
        if (mListView != null && mListView.getVisibility() == VISIBLE) {
            mListView.setVisibility(GONE);
        }
    }

    /**
     * 触摸之后就隐藏
     * @param event 事件
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mListView.getVisibility() == VISIBLE) {
            hide();
            return true;
        }

        return super.onTouchEvent(event);
    }


    /**
     * 设置清晰度
     * @param qualities 所有支持的清晰度
     * @param currentQuality 当前的清晰度
     */
    public void setQuality(List<VideoStream> qualities, String currentQuality) {
       /* //排序之后显示出来
        mQualityItems = sortQuality(qualities);*/
        videoStreamList = qualities;
        this.currentQuality = currentQuality;
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setVideoStreamList(List<VideoStream> videoStreamList){
        this.videoStreamList = videoStreamList;
    }

    /**
     * SQ，HQ，FD，LD，SD，HD，2K，4K，OD
     */
    private List<VideoStream> sortQuality(List<VideoStream> qualities) {

        //MTS的源不需要排序
        if (isMtsSource) {
            return qualities;
        }

        VideoStream ld = null, sd = null, hd = null, fd = null, k2 = null, k4 = null, od = null,sq = null,hq = null;
        for (VideoStream quality : qualities) {
            if (QualityValue.QUALITY_FLUENT.equals(quality.getDefinition())) {
//                fd = QualityValue.QUALITY_FLUENT;
                fd = quality;
            } else if (QualityValue.QUALITY_LOW.equals(quality.getDefinition())) {
//                ld = QualityValue.QUALITY_LOW;
                ld = quality;
            } else if (QualityValue.QUALITY_STAND.equals(quality.getDefinition())) {
//                sd = QualityValue.QUALITY_STAND;
                sd = quality;
            } else if (QualityValue.QUALITY_HIGH.equals(quality.getDefinition())) {
//                hd = QualityValue.QUALITY_HIGH;
                hd = quality;
            } else if (QualityValue.QUALITY_2K.equals(quality.getDefinition())) {
//                k2 = QualityValue.QUALITY_2K;
                k2 = quality;
            } else if (QualityValue.QUALITY_4K.equals(quality.getDefinition())) {
//                k4 = QualityValue.QUALITY_4K;
                k4 = quality;
            } else if (QualityValue.QUALITY_ORIGINAL.equals(quality.getDefinition())) {
//                od = QualityValue.QUALITY_ORIGINAL;
                od = quality;
            } else if (QualityValue.QUALITY_SQ.equals(quality.getDefinition())) {
                sq = quality;
            } else if(QualityValue.QUALITY_HQ.equals(quality.getDefinition())){
                hq = quality;
            }
        }

        //清晰度按照fd,ld,sd,hd,2k,4k,od排序
        List<VideoStream> sortedQuality = new LinkedList<>();
//        if (!TextUtils.isEmpty(fd)) {
//            sortedQuality.add(fd);
//        }
//
//        if (!TextUtils.isEmpty(ld)) {
//            sortedQuality.add(ld);
//        }
//        if (!TextUtils.isEmpty(sd)) {
//            sortedQuality.add(sd);
//        }
//        if (!TextUtils.isEmpty(hd)) {
//            sortedQuality.add(hd);
//        }
//
//        if (!TextUtils.isEmpty(k2)) {
//            sortedQuality.add(k2);
//        }
//        if (!TextUtils.isEmpty(k4)) {
//            sortedQuality.add(k4);
//        }
//        if (!TextUtils.isEmpty(od)) {
//            sortedQuality.add(od);
//        }
        if(sq != null){
            sortedQuality.add(sq);
        }

        if(hq != null){
            sortedQuality.add(hq);
        }

        if (fd != null) {
            sortedQuality.add(fd);
        }

        if (ld != null) {
            sortedQuality.add(ld);
        }
        if (sd != null) {
            sortedQuality.add(sd);
        }
        if (hd != null) {
            sortedQuality.add(hd);
        }

        if (k2 != null) {
            sortedQuality.add(k2);
        }
        if (k4 != null) {
            sortedQuality.add(k4);
        }
        if (od != null) {
            sortedQuality.add(od);
        }


        return sortedQuality;
    }
}
