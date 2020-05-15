package com.tourcoo.training.widget.dialog.training;

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
 * @date 2020年04月23日20:14
 * @Email: 971613168@qq.com
 */
public class CommonSuccessAlert {

    private Context mContext;
    private Dialog dialog;
    private int width = 0;
    private TextView tvAlertContent;
    private TextView tvAlertTile;
    private TextView tvAlertConfirm;

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
        View view = LayoutInflater.from(mContext).inflate(R.layout.alert_success_common, null);
        tvAlertConfirm = view.findViewById(R.id.tvAlertConfirm);
        tvAlertContent = view.findViewById(R.id.tvAlertContent);
        tvAlertConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvAlertTile = view.findViewById(R.id.tvAlertTile);
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(width);
        // 定义Dialog布局和参数
        dialog = new Dialog(mContext, R.style.AlertDialogStyle);
        dialog.setContentView(view);
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
            // 宽度设置为屏幕的0.75
            p.width = (int) (width * 0.75);
            window.setAttributes(p);
        }
        return this;
    }

    public CommonSuccessAlert setConfirmClick(View.OnClickListener onClickListener) {
        if (tvAlertConfirm != null) {
            tvAlertConfirm.setOnClickListener(onClickListener);
        }
        return this;
    }

    public CommonSuccessAlert setAlertTitle(CharSequence title) {
        if (tvAlertTile != null) {
            tvAlertTile.setText(title);
        }
        return this;
    }


    public CommonSuccessAlert setConfirmClick(CharSequence text, View.OnClickListener onClickListener) {
        if (TextUtils.isEmpty(text)) {
            text = "";
        }
        if (tvAlertConfirm != null) {
            tvAlertConfirm.setText(text);
            tvAlertConfirm.setOnClickListener(onClickListener);
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

    public CommonSuccessAlert setContent(CharSequence charSequence) {
        if (charSequence != null && tvAlertContent != null) {
            tvAlertContent.setText(charSequence);
        }
        return this;
    }

    public CommonSuccessAlert setCancelTouchOutSide(boolean cancelTouchOutSide) {
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(cancelTouchOutSide);
        }
        return this;
    }
}
