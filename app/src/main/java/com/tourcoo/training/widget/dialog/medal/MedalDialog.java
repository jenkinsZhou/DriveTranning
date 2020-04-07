package com.tourcoo.training.widget.dialog.medal;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
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
 * @description :勋章相关dialog
 * @company :途酷科技
 * @date 2020年04月07日10:37
 * @Email: 971613168@qq.com
 */
public class MedalDialog {
    private Context mContext;
    private Dialog dialog;
    private TextView tvPositive;
    private TextView tvTitle;

    public MedalDialog(Context context) {
        this.mContext = context;
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
    }

    public MedalDialog create() {
        // 获取Dialog布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_medal_commen, null);
        // 设置Dialog最小宽度为屏幕宽度
//        view.setMinimumWidth(width);
        // 定义Dialog布局和参数
        dialog = new Dialog(mContext, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        tvPositive = view.findViewById(R.id.tvPositive);
        tvTitle = view.findViewById(R.id.tvTitle);
        view.findViewById(R.id.ivCloseRect).setOnClickListener(v -> dismiss());
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

    public MedalDialog setPositiveButtonListener(View.OnClickListener onClickListener) {
        if (tvPositive != null) {
            tvPositive.setOnClickListener(onClickListener);
        }
        return this;
    }


    public MedalDialog setTitle(View.OnClickListener onClickListener) {
        if (tvPositive != null) {
            tvPositive.setOnClickListener(onClickListener);
        }
        return this;
    }

    public MedalDialog setTitle(String title) {
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
        return this;
    }
}
