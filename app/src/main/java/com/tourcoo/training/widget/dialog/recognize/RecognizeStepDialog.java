package com.tourcoo.training.widget.dialog.recognize;

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
import com.tourcoo.training.core.util.ResourceUtil;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月15日16:48
 * @Email: 971613168@qq.com
 */
public class RecognizeStepDialog  {
    private static final int STEP_ONE = 1;
    private static final int STEP_TWO = 2;
    private Context mContext;
    private Dialog dialog;
    private int currentStep = STEP_ONE;
    private int width = 0;
    private TextView tvPositive;
    private TextView tvStepNum1;
    private TextView tvStepOne;
    private TextView tvStepNum2;
    private TextView tvStepTwo;
    public RecognizeStepDialog(Context context) {
        this.mContext = context;
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
    }


    public RecognizeStepDialog create() {
        // 获取Dialog布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_recognize_step, null);
        tvPositive = view.findViewById(R.id.tvPositive);
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(width);
        tvStepNum1 = view.findViewById(R.id.tvStepNum1);
        tvStepOne = view.findViewById(R.id.tvStepOne);
        tvStepNum2 = view.findViewById(R.id.tvStepNum2);
        tvStepTwo = view.findViewById(R.id.tvStepTwo);
        // 定义Dialog布局和参数
        dialog = new Dialog(mContext, R.style.Theme_dialog);
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
            // 宽度设置为屏幕的0.8
            p.width = (int) (width * 0.85);
            window.setAttributes(p);
            view.findViewById(R.id.ivCloseRect) .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
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

    public RecognizeStepDialog setPositiveButton(View.OnClickListener onClickListener){
        tvPositive.setOnClickListener(onClickListener);
        return this;
    }


    public void showStepOneSuccess(){
        if(dialog.isShowing()){
            tvStepNum1.setBackgroundResource(R.drawable.shape_circle_grayb3b3b3);
            tvStepOne.setTextColor(ResourceUtil.getColor(R.color.grayB3B3B3));
            tvStepNum2.setBackgroundResource(R.drawable.shape_circle_blue_5087ff);
            tvStepTwo.setTextColor(ResourceUtil.getColor(R.color.blue5087FF));
            tvPositive.setText("身份证采集");
            currentStep = STEP_TWO;
        }
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }
}
