package com.tourcoo.training.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tourcoo.training.R;
import com.tourcoo.training.core.util.ToastUtil;


/**
 * @Description：
 * @author：Bux on 2017/11/15 15:55
 * @email: 471025316@qq.com
 */

public class AddSubtractView extends LinearLayout {

    ImageView mIvSubtract;//－号
    ImageView mIvAdd;//+
    TextView mTvSPNumber;//输入众筹数量

    //最大值
    private int mMin = 0;
    //最小值
    private int mMax = Integer.MAX_VALUE;
    //当前值
    private int mCurrent;

    private Context mContext;

    private OnAddSubtractListener mListener;

    public AddSubtractView(Context context) {
        this(context, null);
    }

    public AddSubtractView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddSubtractView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }


    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.add_subtract_layout, this, true);
        mIvSubtract = findViewById(R.id.b2b_subtract);
        mIvAdd = findViewById(R.id.b2b_add);
        mTvSPNumber = findViewById(R.id.sp_number);

        mTvSPNumber.setText(String.valueOf(mCurrent));
        //监听加减事件
        mIvSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --mCurrent;
                setControl();
            }
        });

        mIvAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ++mCurrent;
                setControl();
            }
        });

        //弹出对话框输入数量
        mTvSPNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    public AddSubtractView setClickEnable(boolean enable) {
        if(enable) {
            checkDataSetView();
            mTvSPNumber.setEnabled(true);

        }else{
            mIvAdd.setEnabled(false);
            mIvSubtract.setEnabled(false);
            mTvSPNumber.setEnabled(false);
        }
        return this;

    }

    /**
     * 设置相关参数
     *
     * @param min
     * @param max
     * @param current
     * @return
     */
    public AddSubtractView setLimit(int min, int max, int current) {
        mMin = min;
        mMax = max;
        mCurrent = current;
        checkDataSetView();
        return this;
    }

    /**
     * 校验数据，设置加减号是否可用 当达到最大或者最小时 按钮不可点击
     */
    private void checkDataSetView() {

        mIvAdd.setEnabled(mCurrent <= mMax);
        mIvSubtract.setEnabled(mCurrent >= mMin);

        if (mCurrent > mMax) {
            mCurrent = mMax;
        }
        if (mCurrent < mMin) {
            mCurrent = mMin;
        }
        mTvSPNumber.setText(String.valueOf(mCurrent));

    }

    /**
     * 检查数据　并通知数据变化
     */
    private void setControl() {
        //最小时通知
        if (mCurrent < mMin) {
            if (mListener != null) {
                mListener.onLessMin();
            }
        }
        //最大值通知
        if (mCurrent > mMax) {
            if (mListener != null) {
                mListener.onMoreMax();
            }

        }

        checkDataSetView();
        if (mListener != null) {
            mListener.onNumberChange(mCurrent);
        }
    }


    public void setIvSubtract(ImageView ivSubtract) {
        mIvSubtract = ivSubtract;
    }


    public void setIvAdd(ImageView ivAdd) {
        mIvAdd = ivAdd;
    }


    public void setTvSPNumber(TextView tvSPNumber) {
        mTvSPNumber = tvSPNumber;
    }


    public AddSubtractView setListener(OnAddSubtractListener listener) {
        mListener = listener;
        return this;
    }

    /**
     * 事件回调
     */
    public interface OnAddSubtractListener {
        /**
         * 达到最大
         */
        void onMoreMax();

        /**
         * 达到最小
         */
        void onLessMin();

        /**
         * 数字改变
         *
         * @param number 当前数字
         */
        void onNumberChange(int number);
    }
}