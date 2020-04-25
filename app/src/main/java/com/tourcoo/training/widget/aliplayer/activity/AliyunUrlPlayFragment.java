package com.tourcoo.training.widget.aliplayer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.didichuxing.doraemonkit.zxing.activity.CaptureActivity;
import com.tourcoo.training.R;
import com.tourcoo.training.widget.aliplayer.constants.PlayParameter;
import com.tourcoo.training.widget.aliplayer.listener.OnNotifyActivityListener;
import com.tourcoo.training.widget.aliplayer.utils.FixedToastUtils;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * vid设置界面
 * Created by Mulberry on 2018/4/4.
 */
public class AliyunUrlPlayFragment extends Fragment implements OnClickListener{
    private static final int REQ_CODE_PERMISSION = 0x1111;
    ImageView ivQRcode;
    EditText etPlayUrl;

    /**
     * 返回给上个activity的resultcode: 100为vid播放类型, 200为URL播放类型
     */
    private static final int CODE_RESULT_URL = 200;

    private OnNotifyActivityListener onNotifyActivityListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player_urlplay_layout, container, false);

        ivQRcode =(ImageView)v.findViewById(R.id.iv_qrcode);
        etPlayUrl = (EditText)v.findViewById(R.id.et_play_url);
        ivQRcode.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_qrcode){
            if (ContextCompat.checkSelfPermission(AliyunUrlPlayFragment.this.getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
                // Do not have the permission of camera, request it.
                ActivityCompat.requestPermissions(AliyunUrlPlayFragment.this.getActivity(),
                    new String[] {Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
            } else {
                // Have gotten the permission
                startCaptureActivityForResult();
            }
        }
    }

    /**
     * start player by Url
     */
    public void startPlayerByUrl(){
        if (!TextUtils.isEmpty(etPlayUrl.getText().toString())){
            Intent intent = new Intent();
            intent.setClass(this.getActivity(), AliyunPlayerSkinActivity.class);
            String testUrl = "https://course.vod.ggjtaq.com/20d650dd19664ab9947b93cbe4f1e397/481ae69632e2f8e66eea8ee4846e5cc7-fd-encrypt-stream.m3u8";
//            testUrl+="?MtsHlsUriToken="+"lLR1wqWwlfx63beDUHzHr474BmzYKSis8IacgggLKZUYmNOAL9O1s7RVa%2F%2FBWCo9PDOFB9FNNnXDlHOsr1uZhm52GjzZoBt6%2F%2FUuTf9Ov%2BrS01r%2BfJdB1swWZWLhs5Sq%2BFMocedI4oXYJJo8ih8OioHLg9vvAsiupvOcRKpbZz9YoNKnJ3pXX0d2nuxJkrVy0gpkqJKwKZzdgElPYe3HAs%2FM9dex9BCrVmDk8rI4%2FN45fxTfMBz4jd6%2Fd9hDOSylLri2QDZKrxtOgR8Q9yz5o%2Bz2NG3AJ5Pt2YM%2BYKM7sVZqNqYmDUNIzNNSYn7rDtn1o%2BUEOCoAz4zSwgr5lorvi%2F3sTFEPst%2FkY66lEYvg8Nzj0mWuvgliKjPK2ho%2FLx6tClA7ybNv9fLVFiZLRALgoA%3D%3D";
            PlayParameter.PLAY_PARAM_TYPE = "localSource";
//            PlayParameter.PLAY_PARAM_URL = etPlayUrl.getText().toString();
            PlayParameter.PLAY_PARAM_URL = testUrl;

            //getActivity().setResult(CODE_RESULT_URL);
            //getActivity().finish();
            if (onNotifyActivityListener != null) {
                onNotifyActivityListener.onNotifyActivity();
            }
        } else {
            FixedToastUtils.show(this.getActivity().getApplicationContext(), R.string.play_url_null_toast);
        }

    }

    /**
     * Jump to CaptureActivity and pass some parameters,  set requestCode to receive the returned data
     */
    private void startCaptureActivityForResult() {
        Intent intent = new Intent(AliyunUrlPlayFragment.this.getActivity(), CaptureActivity.class);
        Bundle bundle = new Bundle();
//        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
//        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
//        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
//        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
//        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
//        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
//        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
//        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
//        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // User agree the permission
                    startCaptureActivityForResult();
                } else {
                    // User disagree the permission
                    FixedToastUtils.show(this.getActivity(), "You must agree the camera permission request before you use the code scan function");
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
        /*    case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                            //or do sth
                            etPlayUrl.setText(data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN));
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            // for some reason camera is not working correctly
                            etPlayUrl.setText(data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN));
                        }
                        break;
                    default:
                        break;
                }*/
            default:
                break;
        }
    }

    public void setOnNotifyActivityListener(OnNotifyActivityListener listener) {
        this.onNotifyActivityListener = listener;
    }
}
