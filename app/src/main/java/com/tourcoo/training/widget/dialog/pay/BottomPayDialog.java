package com.tourcoo.training.widget.dialog.pay;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tourcoo.training.R;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月16日13:46
 * @Email: 971613168@qq.com
 */
public class BottomPayDialog {
    private Context mContext;
    private Dialog dialog;
    private int width = 0;
    private TextView tvConfirm;
    private RelativeLayout rlPayAli;
    private RelativeLayout rlPayWeChat;
    private ImageView ivAliPay;
    private ImageView ivWeChatPay;
    private int payType = 1;
    public BottomPayDialog(Context context) {
        this.mContext = context;
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return;
        }
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
    }


    public BottomPayDialog create() {
        // 获取Dialog布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialpg_pay_bottom, null);
        tvConfirm = view.findViewById(R.id.tvConfirm);
        ivAliPay = view.findViewById(R.id.ivAliPay);
        ivWeChatPay = view.findViewById(R.id.ivWeChatPay);
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(width);
       /* tvStepNum1 = view.findViewById(R.id.tvStepNum1);
        tvStepOne = view.findViewById(R.id.tvStepOne);
        tvStepNum2 = view.findViewById(R.id.tvStepNum2);
        tvStepTwo = view.findViewById(R.id.tvStepTwo);*/
        // 定义Dialog布局和参数
        dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager m = window.getWindowManager();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.BOTTOM;
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
            // 宽度设置为屏幕
            p.width = (int) (width * 1.0);
            window.setAttributes(p);
            rlPayAli = view.findViewById(R.id.rlPayAli);
            rlPayWeChat = view.findViewById(R.id.rlPayWeChat);
            showAliPay();
            rlPayAli.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAliPay();
                }
            });
        }
        rlPayWeChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWeChatPay();
            }
        });

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

    public BottomPayDialog setPositiveButton(View.OnClickListener onClickListener) {
        tvConfirm.setOnClickListener(onClickListener);
        return this;
    }

    private void showAliPay() {
        payType = 1;
        ivAliPay.setImageResource(R.mipmap.ic_pay_type_checked);
        ivWeChatPay.setImageResource(R.mipmap.ic_pay_type_un_checked);
    }

    private void showWeChatPay() {
        payType = 2;
        ivWeChatPay.setImageResource(R.mipmap.ic_pay_type_checked);
        ivAliPay.setImageResource(R.mipmap.ic_pay_type_un_checked);
    }

    public int getPayType(){
        return payType;
    }
}
