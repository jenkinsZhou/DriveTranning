package com.tourcoo.training.widget.dialog.exam;

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
import com.tourcoo.training.core.util.ToastUtil;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月07日11:16
 * @Email: 971613168@qq.com
 */
public class ExamCommonDialog {

    private Context mContext;
    private Dialog dialog;
    private TextView tvPositive;
    private TextView tvContent;
    private TextView tvNegative;
    private OnNegativeClickListener negativeClickListener;

    public ExamCommonDialog(Context context) {
        this.mContext = context;
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
    }

    public ExamCommonDialog create() {
        // 获取Dialog布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_exam_answer_commit, null);
        // 设置Dialog最小宽度为屏幕宽度
//        view.setMinimumWidth(width);
        // 定义Dialog布局和参数
        dialog = new Dialog(mContext, R.style.AlertDialogStyle);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        tvPositive = view.findViewById(R.id.tvPositive);
        tvContent = view.findViewById(R.id.tvContent);
        tvNegative =   view.findViewById(R.id.tvNegative);
        tvNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(negativeClickListener != null){
                    negativeClickListener.onClick(v);
                }
            }
        });
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

    public ExamCommonDialog setPositiveButtonListener(View.OnClickListener onClickListener) {
        if (tvPositive != null) {
            tvPositive.setOnClickListener(onClickListener);
        }
        return this;
    }

    public ExamCommonDialog setNegativeButtonListener(OnNegativeClickListener onNegativeClickListener) {
       this.negativeClickListener = onNegativeClickListener;
        return this;
    }

    public ExamCommonDialog setContent(String content) {
        if (tvContent != null) {
            tvContent.setText(content);
        }
        return this;
    }


}
