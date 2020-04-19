package com.tourcoo.training.widget.neplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.netease.neliveplayer.sdk.NELivePlayer;
import com.netease.neliveplayer.sdk.model.NEDynamicLoadingConfig;
import com.netease.neliveplayer.sdk.model.NESDKConfig;
import com.tourcoo.training.core.app.MyApplication;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.util.ToastUtil;

import java.io.IOException;
import java.util.Map;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年04月19日10:11
 * @Email: 971613168@qq.com
 */
public class TestPlayControl implements NELivePlayer.OnPreparedListener, NELivePlayer.OnInfoListener, NELivePlayer.OnErrorListener {
    public static final String TAG =  "TestPlayControl";
    /**
     * sdk controller
     */
    private NELivePlayer mMediaPlayer = null;

    private NEVideoView surfaceView;

    private SurfaceHolder mSurfaceHolder;

    private Uri mUri;

    private Handler mHandler;
    /**
     * views
     */
    private Context mContext;

    private String mVideoPath;

    //states refer to MediaPlayer
    private static final int IDLE = 0;
    private static final int INITIALIZED = 1;
    private static final int PREPARING = 2;
    private static final int PREPARED = 3;
    private static final int STARTED = 4;
    private static final int PAUSED = 5;

    /**
     * 处理surfaceView生命周期
     * 在此处控制进入后台时的数据存储
     */
    private SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mSurfaceHolder = holder;

                    openVideo();
//                    mUi.showLoading(true);


            }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mSurfaceHolder = holder;
            if (mMediaPlayer != null) {
                mMediaPlayer.setDisplay(mSurfaceHolder);
            }
            surfaceView.onSurfaceChanged(width, height);
            ToastUtil.show("继续播放");
            start();
         /*   if (!isHwDecoder && mPauseInBackground && mIsPrepared && mVideoView.isSizeNormal()) {
                if (isManualPaused()) {
                    pause();
                } else {
                    start();
                }
            }
            if (mMediaControlLayout != null) {
                mMediaControlLayout.show();
            }*/
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            TourCooLogUtil.d(TAG, "onSurfaceDestroyed");
            mSurfaceHolder = null;
            if (mMediaPlayer != null) {
                mMediaPlayer.setDisplay(null);
            }
        }
    };


    private void openVideo() {

        if (mUri == null || mSurfaceHolder == null) {
            // not ready for playback just yet, will try again later
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    openVideo();
                }
            }, 200);
            return;
        }

        // Tell the music playback service to pause
        // TODO: these constants need to be published somewhere in the framework
        Intent i = new Intent("com.android.music.musicservicecommand");
        i.putExtra("command", "pause");
        mContext.sendBroadcast(i);
        release_resource();
        NESDKConfig config = new NESDKConfig();

        //如果需要开启动态加载功能，那么设置dynamicLoadingConfig，默认关闭
        config.dynamicLoadingConfig = new NEDynamicLoadingConfig();
        config.dynamicLoadingConfig.isDynamicLoading = false;
        //使用Armeabiv7a架构示例，使用其他架构可以参考修改
        config.dynamicLoadingConfig.isArmeabiv7a = false;
        //动态加载准备完成时回调
        NELivePlayer.init(mContext,config);
        NELivePlayer neMediaPlayer = NELivePlayer.create();
        neMediaPlayer.setBufferStrategy(2);//设置缓冲策略，0为直播低延时，1为点播抗抖动
        neMediaPlayer.setHardwareDecoder(false);//设置是否开启硬件解码，0为软解，1为硬解
      /*  neMediaPlayer.setOnPreparedListener(sdkStateListener);
        neMediaPlayer.setOnVideoSizeChangedListener(sdkStateListener);
        neMediaPlayer.setOnCompletionListener(sdkStateListener);
        neMediaPlayer.setOnErrorListener(sdkStateListener);
        neMediaPlayer.setOnBufferingUpdateListener(sdkStateListener);
        neMediaPlayer.setOnInfoListener(sdkStateListener);
        neMediaPlayer.setOnSeekCompleteListener(sdkStateListener);
        neMediaPlayer.setOnVideoParseErrorListener(sdkStateListener);*/
        mMediaPlayer = neMediaPlayer;
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnErrorListener(this);
        surfaceView.getHolder().addCallback(mSHCallback);
//        attachControlLayout();
        boolean ret;
        try {
            ret = neMediaPlayer.setDataSource("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4");
            if (ret) {
              /*  if (isLiveStream()) {
                    mUi.onError("地址非法，请输入网易视频云官方地址!");
                }*/
                release_resource();
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.showFailed(e.toString());
//            sdkStateListener.onError(mMediaPlayer, -1, 0);
        }
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mCurrState = INITIALIZED;
                    mNextState = PREPARING;
                    mMediaPlayer.setPlaybackTimeout(30000);
                    mMediaPlayer.setDisplay(mSurfaceHolder);
                    mMediaPlayer.setScreenOnWhilePlaying(true);
                    mMediaPlayer.prepareAsync();
                    mCurrState = PREPARING;
                } catch (Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            sdkStateListener.onError(mMediaPlayer, -1, 0);
                        }
                    });
                }
            }
        }).start();*/
    }


    public void release_resource() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
            ToastUtil.show("资源被释放");
//            mCurrState = IDLE;
        }
    }

    public void init(){
        mContext = MyApplication.getContext();
    }

    public TestPlayControl(NEVideoView videoView) {
        init();
        surfaceView = videoView;
        mVideoPath = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f30.mp4";
        mHandler = new Handler(Looper.getMainLooper());
    }


    public void start() {
        if (mMediaPlayer != null ) {
            mMediaPlayer.start();

        }
    }

    @Override
    public void onPrepared(NELivePlayer mp) {
        ToastUtil.show("onPrepared");
    }

    @Override
    public boolean onInfo(NELivePlayer mp, int what, int extra) {
        ToastUtil.show("onInfo");
        return false;
    }

    @Override
    public boolean onError(NELivePlayer mp, int what, int extra) {
        ToastUtil.show("onError");
        return false;
    }


    public void reOpenVideo() {
//        needResumeWhenNetworkConnect = false;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "on network validate");
                openVideo();

            }
        });
    }
}
