package com.tourcoo.training.widget.aliplayer.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tourcoo.training.R;
import com.tourcoo.training.widget.aliplayer.constants.PlayParameter;
import com.tourcoo.training.widget.aliplayer.listener.OnNotifyActivityListener;
import com.tourcoo.training.widget.aliplayer.utils.FixedToastUtils;
import com.tourcoo.training.widget.aliplayer.utils.VidStsUtil;

import java.lang.ref.WeakReference;

/**
 * vid设置界面
 * Created by Mulberry on 2018/4/4.
 */
public class AliyunVidPlayFragment extends Fragment {

    EditText etVid;
    EditText etAkId;
    EditText etAkSecret;
    EditText etScuToken;
    /**
     * get StsToken stats
     */
    private boolean inRequest;

    /**
     * 返回给上个activity的resultcode: 100为vid播放类型, 200为URL播放类型
     */
    private static final int CODE_RESULT_VID = 100;

    private OnNotifyActivityListener onNotifyActivityListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player_vidplay_layout, container, false);

        initStsView(v);
        return v;
    }

    private void initStsView(View v){
        etVid = (EditText)v.findViewById(R.id.vid);
        etAkId = (EditText)v.findViewById(R.id.akId);
        etAkSecret = (EditText)v.findViewById(R.id.akSecret);
        etScuToken = (EditText)v.findViewById(R.id.scuToken);
    }

    public void startToPlayerByVid(){
        String mVid = etVid.getText().toString();
        String akId = etAkId.getText().toString();
        String akSecret = etAkSecret.getText().toString();
        String scuToken = etScuToken.getText().toString();
        PlayParameter.PLAY_PARAM_TYPE = "vidsts";
        if (TextUtils.isEmpty(mVid) || TextUtils.isEmpty(akId) || TextUtils.isEmpty(akSecret) || TextUtils.isEmpty(scuToken)) {
            if(inRequest){
                return;
            }

            inRequest = true;
            VidStsUtil.getVidSts(mVid,new MyStsListener(this));

        } else {

            PlayParameter.PLAY_PARAM_VID= mVid;
            PlayParameter.PLAY_PARAM_AK_ID = akId;
            PlayParameter.PLAY_PARAM_AK_SECRE = akSecret;
            PlayParameter.PLAY_PARAM_SCU_TOKEN = scuToken;

            getActivity().setResult(CODE_RESULT_VID);
            getActivity().finish();
        }
    }

    private static class MyStsListener implements VidStsUtil.OnStsResultListener{

        private WeakReference<AliyunVidPlayFragment> weakctivity;

        public MyStsListener(AliyunVidPlayFragment view)
        {
            weakctivity = new WeakReference<AliyunVidPlayFragment>(view);
        }

        @Override
        public void onSuccess(String vid, String akid, String akSecret, String token) {
            AliyunVidPlayFragment fragment = weakctivity.get();
            if(fragment != null){
                fragment.onStsSuccess(vid,akid,akSecret,token);
            }
        }

        @Override
        public void onFail() {
            AliyunVidPlayFragment fragment = weakctivity.get();
            if(fragment != null){
                fragment.onStsFail();
            }
        }
    }

    private void onStsFail() {
        if (getContext() != null) {
            FixedToastUtils.show(getContext().getApplicationContext(), R.string.request_vidsts_fail);
        }
        inRequest = false;
    }

    private void onStsSuccess(String mVid,String akid, String akSecret, String token) {

        PlayParameter.PLAY_PARAM_VID= mVid;
        PlayParameter.PLAY_PARAM_AK_ID = akid;
        PlayParameter.PLAY_PARAM_AK_SECRE = akSecret;
        PlayParameter.PLAY_PARAM_SCU_TOKEN = token;

        if (onNotifyActivityListener != null) {
            onNotifyActivityListener.onNotifyActivity();
        }

        //getActivity().setResult(CODE_RESULT_VID);
        //getActivity().finish();
        //

        inRequest = false;
    }

    public void setOnNotifyActivityListener(OnNotifyActivityListener listener) {
        this.onNotifyActivityListener = listener;
    }
}
