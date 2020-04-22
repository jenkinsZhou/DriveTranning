package com.tourcoo.training.widget.dialog.common;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tourcoo.training.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月22日11:23
 * @Email: 971613168@qq.com
 */
public class CommonSuccessAlert {
    private Context mContext;
    private Dialog dialog;
    private int width = 0;
    private TextView tvAlertConfirm;
    private TextView tvAlertTitle;
    private TextView tvAlertContent;

    public CommonSuccessAlert(Context context) {
        this.mContext = context;
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
    }

    public CommonSuccessAlert create() {
        // 获取Dialog布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.alert_result_success_common, null);
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(width);
        tvAlertConfirm = view.findViewById(R.id.tvAlertConfirm);
        tvAlertTitle = view.findViewById(R.id.tvAlertTile);
        tvAlertContent = view.findViewById(R.id.tvAlertContent);
        // 定义Dialog布局和参数
        dialog = new Dialog(mContext, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager m = window.getWindowManager();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            //宽高可设置具体大小
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            // 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
            // 注释：dialog.onWindowAttributesChanged(lp);
            window.setAttributes(lp);
            // 获取屏幕宽、高用
            Display d = m.getDefaultDisplay();
            // 获取对话框当前的参数值
            WindowManager.LayoutParams p = window.getAttributes();
            Display display = m.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            // 宽度设置为屏幕的0.8
            p.width = (int) (width * 0.85);
            window.setAttributes(p);
            tvAlertConfirm.setOnClickListener(v -> dismiss());
        }
        return this;
    }


    public CommonSuccessAlert setPositiveButtonText(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        if (tvAlertConfirm != null) {
            tvAlertConfirm.setText(text);
        }
        return this;
    }

    public CommonSuccessAlert setPositiveButtonClick(CharSequence text, View.OnClickListener onClickListener) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        if (tvAlertConfirm != null) {
            tvAlertConfirm.setText(text);
            tvAlertConfirm.setOnClickListener(onClickListener);
        }
        return this;
    }

    public CommonSuccessAlert setTitleClick(CharSequence text, View.OnClickListener onClickListener) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        if (tvAlertTitle != null) {
            tvAlertTitle.setText(text);
            tvAlertTitle.setOnClickListener(onClickListener);
        }
        return this;
    }

    public CommonSuccessAlert setContentClick(CharSequence text, View.OnClickListener onClickListener) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        if (tvAlertContent != null) {
            tvAlertContent.setText(text);
            tvAlertContent.setOnClickListener(onClickListener);
        }
        return this;
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public CommonSuccessAlert setContent(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        if (tvAlertContent != null) {
            tvAlertContent.setText(text);
        }
        return this;
    }
}
