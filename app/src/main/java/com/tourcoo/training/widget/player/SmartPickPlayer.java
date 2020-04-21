package com.tourcoo.training.widget.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shuyu.gsyvideoplayer.GSYVideoBaseManager;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.listener.GSYMediaPlayerListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.tourcoo.training.R;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.core.widget.dialog.loading.IosLoadingDialog;
import com.tourcoo.training.entity.training.VideoStream;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年04月19日23:04
 * @Email: 971613168@qq.com
 */
public class SmartPickPlayer extends StandardGSYVideoPlayer {

    //记住切换数据源类型
    private int mType = 0;
    //数据源
    private int mSourcePosition = 0;
    private int mPreSourcePosition = 0;

    private String mTypeText = "标准";

    private GSYVideoManager mTmpManager;


    //切换过程中最好弹出loading，不给其他任何操作
    private IosLoadingDialog mLoadingDialog;

    private boolean isChanging;

    private List<VideoStream> mUrlList = new ArrayList<>();

    private TextView mSwitchSize;

    private OnPlayStatusListener onPlayStatusListener;

    private int currentCourseId;

    public int getCurrentCourseId() {
        return currentCourseId;
    }

    public void setCurrentCourseId(int currentCourseId) {
        this.currentCourseId = currentCourseId;
    }

    public OnPlayStatusListener getOnPlayStatusListener() {
        return onPlayStatusListener;
    }

    public void setOnPlayStatusListener(OnPlayStatusListener onPlayStatusListener) {
        this.onPlayStatusListener = onPlayStatusListener;
    }

    public SmartPickPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public SmartPickPlayer(Context context) {
        super(context);
    }

    public SmartPickPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void init(Context context) {
        super.init(context);
        initView();
    }


    private void initView() {
        mSwitchSize = (TextView) findViewById(R.id.switchSize);
        //切换视频清晰度
        mSwitchSize.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHadPlay && !isChanging) {
                    showSwitchDialog();
                }
            }
        });
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param title         title
     * @return
     */
    public boolean setUp(List<VideoStream> url, boolean cacheWithPlay, String title) {
        mUrlList = url;
        return setUp(url.get(mSourcePosition).getURL(), cacheWithPlay, title);
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @param title         title
     * @return
     */
    public boolean setUp(List<VideoStream> url, boolean cacheWithPlay, File cachePath, String title) {
        mUrlList = url;
        return setUp(url.get(mSourcePosition).getURL(), cacheWithPlay, cachePath, title);
    }

    @Override
    public int getLayoutId() {
        return R.layout.sample_video_pick;
    }


    /**
     * 全屏时将对应处理参数逻辑赋给全屏播放器
     *
     * @param context
     * @param actionBar
     * @param statusBar
     * @return
     */
    @Override
    public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
        SmartPickPlayer sampleVideo = (SmartPickPlayer) super.startWindowFullscreen(context, actionBar, statusBar);
        sampleVideo.mSourcePosition = mSourcePosition;
        sampleVideo.mListItemRect = mListItemRect;
        sampleVideo.mListItemSize = mListItemSize;
        sampleVideo.mType = mType;
        sampleVideo.mUrlList = mUrlList;
        sampleVideo.mTypeText = mTypeText;
        sampleVideo.onPlayStatusListener = onPlayStatusListener;
        sampleVideo.mSwitchSize.setText(mTypeText);
        sampleVideo.currentCourseId = currentCourseId;
        return sampleVideo;
    }

    /**
     * 退出全屏时将对应处理参数逻辑返回给非全屏播放器
     *
     * @param oldF
     * @param vp
     * @param gsyVideoPlayer
     */
    @Override
    protected void resolveNormalVideoShow(View oldF, ViewGroup vp, GSYVideoPlayer gsyVideoPlayer) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer);
        if (gsyVideoPlayer != null) {
            SmartPickPlayer sampleVideo = (SmartPickPlayer) gsyVideoPlayer;
            mSourcePosition = sampleVideo.mSourcePosition;
            mType = sampleVideo.mType;
            mTypeText = sampleVideo.mTypeText;
            mSwitchSize.setText(mTypeText);
            setUp(mUrlList, mCache, mCachePath, mTitle);
        }
    }

    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
        releaseTmpManager();
        if(onPlayStatusListener != null){
            onPlayStatusListener.onAutoPlayComplete(getCurrentCourseId());
        }
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
        releaseTmpManager();
        if(onPlayStatusListener != null){
            onPlayStatusListener.onPlayComplete(getCurrentCourseId());
        }

    }

    /**
     * 弹出切换清晰度
     */
    private void showSwitchDialog() {
        if (!mHadPlay) {
            return;
        }
        SwitchVideoTypeDialog switchVideoTypeDialog = new SwitchVideoTypeDialog(getContext());
        switchVideoTypeDialog.initList(mUrlList, new SwitchVideoTypeDialog.OnListItemClickListener() {
            @Override
            public void onItemClick(int position) {
                resolveStartChange(position);
            }
        });
        switchVideoTypeDialog.show();
    }


    private void resolveChangeUrl(boolean cacheWithPlay, File cachePath, String url) {
        if (mTmpManager != null) {
            mCache = cacheWithPlay;
            mCachePath = cachePath;
            mOriginUrl = url;
            this.mUrl = url;
        }
    }


    private GSYMediaPlayerListener gsyMediaPlayerListener = new GSYMediaPlayerListener() {
        @Override
        public void onPrepared() {
            if (mTmpManager != null) {
                mTmpManager.start();
                mTmpManager.seekTo(getCurrentPositionWhenPlaying());
            }
        }

        @Override
        public void onAutoCompletion() {

        }

        @Override
        public void onCompletion() {

        }

        @Override
        public void onBufferingUpdate(int percent) {

        }

        @Override
        public void onSeekComplete() {
            if (mTmpManager != null) {
                GSYVideoBaseManager manager = GSYVideoManager.instance();
                GSYVideoManager.changeManager(mTmpManager);
                mTmpManager.setLastListener(manager.lastListener());
                mTmpManager.setListener(manager.listener());

                manager.setDisplay(null);

                Debuger.printfError("**** showDisplay onSeekComplete ***** " + mSurface);
                Debuger.printfError("**** showDisplay onSeekComplete isValid***** " + mSurface.isValid());
                mTmpManager.setDisplay(mSurface);
                changeUiToPlayingClear();
                resolveChangedResult();
                manager.releaseMediaPlayer();
            }
        }

        @Override
        public void onError(int what, int extra) {
            mSourcePosition = mPreSourcePosition;
            if (mTmpManager != null) {
                mTmpManager.releaseMediaPlayer();
            }
            post(new Runnable() {
                @Override
                public void run() {
                    resolveChangedResult();
                    Toast.makeText(mContext, "change Fail", Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onInfo(int what, int extra) {

        }

        @Override
        public void onVideoSizeChanged() {

        }

        @Override
        public void onBackFullscreen() {

        }

        @Override
        public void onVideoPause() {

        }

        @Override
        public void onVideoResume() {

        }

        @Override
        public void onVideoResume(boolean seek) {

        }
    };

    private void resolveStartChange(int position) {
        final String name = CommonUtil.getNotNullValue(mUrlList.get(position).getDefinition());
        if (mSourcePosition != position) {
            if ((mCurrentState == GSYVideoPlayer.CURRENT_STATE_PLAYING
                    || mCurrentState == GSYVideoPlayer.CURRENT_STATE_PAUSE)) {
                showLoading();
                final String url = mUrlList.get(position).getURL();
                cancelProgressTimer();
                hideAllWidget();
                if (mTitle != null && mTitleTextView != null) {
                    mTitleTextView.setText(mTitle);
                }
                mPreSourcePosition = mSourcePosition;
                isChanging = true;
                mTypeText = name;
                mSwitchSize.setText(name);
                mSourcePosition = position;
                //创建临时管理器执行加载播放
                mTmpManager = GSYVideoManager.tmpInstance(gsyMediaPlayerListener);
                mTmpManager.initContext(getContext().getApplicationContext());
                resolveChangeUrl(mCache, mCachePath, url);
                mTmpManager.prepare(mUrl, mMapHeadData, mLooping, mSpeed, mCache, mCachePath, null);
                changeUiToPlayingBufferingShow();
            }
        } else {
            Toast.makeText(getContext(), "已经是 " + name, Toast.LENGTH_LONG).show();
        }
    }

    private void resolveChangedResult() {
        isChanging = false;
        mTmpManager = null;
        final String name =CommonUtil.getNotNullValue( mUrlList.get(mSourcePosition).getDefinition());
        final String url = mUrlList.get(mSourcePosition).getURL();
        mTypeText = name;
        mSwitchSize.setText(name);
        resolveChangeUrl(mCache, mCachePath, url);
        hideLoading();
    }

    private void releaseTmpManager() {
        if (mTmpManager != null) {
            mTmpManager.releaseMediaPlayer();
            mTmpManager = null;
        }
    }

    private void showLoading() {
        hideLoading();
        mLoadingDialog = new IosLoadingDialog(mContext);
        mLoadingDialog.show();
    }

    private void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    @Override
    public boolean onSurfaceDestroyed(Surface surface) {
        //清空释放
        setDisplay(null);
        //同一消息队列中去release
        //todo 需要处理为什么全屏时全屏的surface会被释放了
        //releaseSurface(surface);
        return true;
    }


}
