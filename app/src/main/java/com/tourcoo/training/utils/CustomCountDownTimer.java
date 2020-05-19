package com.tourcoo.training.utils;

import android.os.CountDownTimer;
import android.os.Handler;

import com.dyhdyh.support.countdowntimer.ITimerSupport;
import com.dyhdyh.support.countdowntimer.OnCountDownTimerListener;
import com.dyhdyh.support.countdowntimer.TimerState;
import com.tourcoo.training.core.log.TourCooLogUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * author  dengyuhan
 * created 2017/5/16 11:32
 */
public class CustomCountDownTimer implements ITimerSupport {
    private Timer mTimer;

    private Handler mHandler;

    /**
     * 倒计时时间
     */
    private long mMillisInFuture;

    /**
     * 间隔时间
     */
    private long mCountDownInterval;
    /**
     * 倒计时剩余时间
     */
    private long mMillisUntilFinished;

    private OnCountDownTimerListener mOnCountDownTimerListener;

    private TimerState mTimerState = TimerState.FINISH;

    @Deprecated
    public CustomCountDownTimer() {
        this.mHandler = new Handler();
    }

    public CustomCountDownTimer(long millisInFuture, long countDownInterval) {
        this.setMillisInFuture(millisInFuture);
        this.setCountDownInterval(countDownInterval);
        this.mHandler = new Handler();
    }

    @Override
    public void start() {
        //防止重复启动 重新启动要先reset再start
        if (mTimer == null && mTimerState != TimerState.START) {
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(createTimerTask(), 0, mCountDownInterval);
            mTimerState = TimerState.START;
        }
    }

    @Override
    public void pause() {
        if (mTimer != null && mTimerState == TimerState.START) {
            cancelTimer();
            mTimerState = TimerState.PAUSE;
        }
    }

    @Override
    public void resume() {
        if (mTimerState == TimerState.PAUSE) {
            start();
        }
    }

    @Override
    public void stop() {
        if (mTimer != null) {
            cancelTimer();



            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    TourCooLogUtil.d(mOnCountDownTimerListener);
                    TourCooLogUtil.d(mOnCountDownTimerListener);
                    if (mOnCountDownTimerListener != null&& mMillisUntilFinished<=0 ) {
                        mOnCountDownTimerListener.onFinish();
                    }
                    mMillisUntilFinished = mMillisInFuture;
                    mTimerState = TimerState.FINISH;
                }
            });
        }
    }

    @Override
    public void reset() {
        if (mTimer != null) {
            cancelTimer();
        }
        mMillisUntilFinished = mMillisInFuture;
        mTimerState = TimerState.FINISH;
    }


    private void cancelTimer() {
        mTimer.cancel();
        mTimer.purge();
        mTimer = null;
    }

    public boolean isStart() {
        return mTimerState == TimerState.START;
    }

    public boolean isFinish() {
        return mTimerState == TimerState.FINISH;
    }

    /**
     * @deprecated 使用构造方法
     * @param millisInFuture
     */
    @Deprecated
    public void setMillisInFuture(long millisInFuture) {
        this.mMillisInFuture = millisInFuture;
        this.mMillisUntilFinished = mMillisInFuture;
    }

    /**
     * @deprecated 使用构造方法
     * @param countDownInterval
     */
    @Deprecated
    public void setCountDownInterval(long countDownInterval) {
        this.mCountDownInterval = countDownInterval;
    }

    public void setOnCountDownTimerListener(OnCountDownTimerListener listener) {
        this.mOnCountDownTimerListener = listener;
    }

    public long getMillisUntilFinished() {
        return mMillisUntilFinished;
    }

    public TimerState getTimerState() {
        return mTimerState;
    }

    /**
     * @param millisInFuture
     * @param countDownInterval
     * @return
     * @deprecated 已更换Timer
     */
    @Deprecated
    protected CountDownTimer createCountDownTimer(long millisInFuture, long countDownInterval) {
        return null;
    }

    protected TimerTask createTimerTask() {
        return new TimerTask() {
            private long startTime = -1;

            @Override
            public void run() {
                if (startTime < 0) {
                    //第一次回调 记录开始时间

                    startTime = scheduledExecutionTime() - (mMillisInFuture - mMillisUntilFinished);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mOnCountDownTimerListener != null) {
                                mOnCountDownTimerListener.onTick(mMillisUntilFinished);
                            }
                        }
                    });
                } else {
                    //剩余时间
                    mMillisUntilFinished = mMillisInFuture - (scheduledExecutionTime() - startTime);

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mOnCountDownTimerListener != null) {
                                mOnCountDownTimerListener.onTick(mMillisUntilFinished);
                            }
                        }
                    });
                    if (mMillisUntilFinished <= 0) {
                        //如果没有剩余时间 就停止
                        stop();
                    }
                }
            }
        };
    }

}