package com.tourcoo.training.widget.dialog.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tourcoo.training.R;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年02月20日15:50
 * @Email: 971613168@qq.com
 */
public class BottomShareDialog {
    private Context mContext;
    private Dialog dialog;
    private TextView tvTitle;
    private TextView tvCancel;
    private LinearLayout content;
    private boolean showTitle = false;
    private List<ShareEntity> sheetItemList;
    private Display display;
    private RecyclerView rvShare;
    private ShareAdapter adapter;


    public BottomShareDialog(Context context) {
        this.mContext = context;
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public static class ShareItem {

        String itemName;
        SheetItemTextStyle textStyle;
        OnShareItemClickListener itemClickListener;

        public ShareItem(String itemName, OnShareItemClickListener itemClickListener) {
            this(itemName, new SheetItemTextStyle(), itemClickListener);
        }

        public ShareItem(String itemName, SheetItemTextStyle textStyle, OnShareItemClickListener itemClickListener) {
            this.itemName = itemName;
            this.textStyle = textStyle;
            this.itemClickListener = itemClickListener;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public SheetItemTextStyle getTextStyle() {
            return textStyle;
        }

        public void setTextStyle(SheetItemTextStyle textStyle) {
            this.textStyle = textStyle;
        }

        public OnShareItemClickListener getItemClickListener() {
            return itemClickListener;
        }

        public void setItemClickListener(OnShareItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }
    }


    public interface OnShareItemClickListener {
        void onClick(int which);
    }

    public static class SheetItemTextStyle {
        public final static String Blue = "#037BFF";
        public final static String Red = "#FD4A2E";
        public final static int DEFAULT_TEXT_SIZE = 16;

        int textColor;
        int textSize;
        Typeface typeface;   //加粗，倾斜，等

        public SheetItemTextStyle() {
            this(Blue);
        }

        public SheetItemTextStyle(String textColor) {
            this(textColor, DEFAULT_TEXT_SIZE);
        }

        public SheetItemTextStyle(String textColor, int textSize) {
            this(textColor, textSize, Typeface.defaultFromStyle(Typeface.NORMAL));
        }

        public SheetItemTextStyle(String textColor, int textSize, Typeface typeface) {
            this(Color.parseColor(textColor), textSize, typeface);
        }

        public SheetItemTextStyle(int textColor, int textSize, Typeface typeface) {
            this.textColor = textColor;
            this.textSize = textSize;
            this.typeface = typeface;
        }

        public Typeface getTypeface() {
            return typeface;
        }

        public void setTypeface(Typeface typeface) {
            this.typeface = typeface;
        }

        public int getTextColor() {
            return textColor;
        }

        public void setTextColor(int textColor) {
            this.textColor = textColor;
        }

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }
    }


    public BottomShareDialog create() {
        // 获取Dialog布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_bottom_grid_share, null);
        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth(display.getWidth());
        rvShare = view.findViewById(R.id.rvShare);
        rvShare.setLayoutManager(new GridLayoutManager(mContext,4));
        // 获取自定义Dialog布局中的控件
        tvCancel =  view.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 定义Dialog布局和参数
        dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        adapter =new ShareAdapter() ;
        adapter.bindToRecyclerView(rvShare);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public BottomShareDialog setData(List<ShareEntity> itemList){
        adapter.setNewData(itemList);
        return this;
    }

    public BottomShareDialog addData(ShareEntity item){
        adapter.getData().add(item);
        adapter.notifyDataSetChanged();
        return this;
    }

    public BottomShareDialog setItemClickListener(BaseQuickAdapter.OnItemClickListener listener){
        adapter.setOnItemClickListener(listener);
        return this;
    }
}
