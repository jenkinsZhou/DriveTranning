package com.tourcoo.training.widget.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tourcoo.training.R;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月18日9:23
 * @Email: 971613168@qq.com
 */
public class CommonListDialog<T> {
    private Context context;
    private Dialog dialog;
    private Display display;
    private RecyclerView recyclerView;

    private TextView tvBottom;
    private ImageView ivCloseRect;

    private BaseQuickAdapter adapter;

    public CommonListDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert windowManager != null;
        display = windowManager.getDefaultDisplay();
    }

    public CommonListDialog create() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_list, null);
        ivCloseRect = view.findViewById(R.id.ivCloseRect);
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());
        recyclerView = view.findViewById(R.id.rvCommon);
        // 获取自定义Dialog布局中的控件
        tvBottom = view.findViewById(R.id.tvBottom);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.Theme_dialog);
        dialog.setContentView(view);
        ivCloseRect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        Window dialogWindow = dialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            Point size = new Point();
            display.getSize(size);
            lp.width = (int) (size.x * 0.85);
            lp.height = (int) (size.y * 0.55);
            dialogWindow.setAttributes(lp);
        }
//        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        return this;
    }

    public CommonListDialog setBottomButtonClick(View.OnClickListener onClickListener) {
        if (tvBottom != null) {
            tvBottom.setOnClickListener(onClickListener);
        }
        return this;
    }

    public CommonListDialog setDataAdapter(BaseQuickAdapter adapter) {
        this.adapter = adapter;
        if (adapter != null && recyclerView != null) {
            adapter.bindToRecyclerView(recyclerView);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public CommonListDialog setDataList(List<T> dataList) {
        adapter.setNewData(dataList);
        return this;
    }


    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }
}
