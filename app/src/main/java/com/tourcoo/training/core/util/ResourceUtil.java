package com.tourcoo.training.core.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import com.tourcoo.training.core.app.MyApplication;

/**
 * @Author: JenkinsZhou on 2018/7/19 9:49
 * @E-Mail: 971613168@qq.com
 * Function: 资源文件获取帮助类
 * Description:
 */
public class ResourceUtil {

    private Context mContext;

    public ResourceUtil(Context context) {
        this.mContext = context;
    }

    public CharSequence getText(int res) {
        CharSequence txt = null;
        try {
            txt = mContext.getText(res);
        } catch (Exception e) {

        }
        return txt;
    }

    public CharSequence[] getTextArray(int res) {
        CharSequence[] result = new CharSequence[0];
        try {
            result = mContext.getResources().getTextArray(res);
        } catch (Exception e) {
        }
        return result;
    }

    public Drawable getDrawable(int res) {
        Drawable drawable = null;
        try {
            drawable = mContext.getResources().getDrawable(res);
        } catch (Exception e) {

        }
        return drawable;
    }

   /* public int getColor(int res) {
        int result = 0;
        try {
            result = mContext.getResources().getColor(res);
        } catch (Exception e) {
        }
        return result;
    }*/

    public static int getColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor(MyApplication.getContext(), colorId);
        } else {
            return MyApplication.getContext().getResources().getColor(colorId);
        }
    }

    public ColorStateList getColorStateList(int res) {
        ColorStateList color = null;
        try {
            color = mContext.getResources().getColorStateList(res);
        } catch (Exception e) {

        }
        return color;
    }

    public float getDimension(int res) {
        float result = 0;
        try {
            result = mContext.getResources().getDimension(res);
        } catch (Exception e) {
        }
        return result;
    }

    public int getDimensionPixelSize(int res) {
        int result = 0;
        try {
            result = mContext.getResources().getDimensionPixelSize(res);
        } catch (Exception e) {
        }
        return result;
    }

    public String[] getStringArray(int res) {
        String[] result = new String[0];
        try {
            result = mContext.getResources().getStringArray(res);
        } catch (Exception e) {
        }
        return result;
    }

    public int getAttrColor(int attrRes) {
        int result = 0;
        try {
            TypedValue typedValue = new TypedValue();
            mContext.getTheme().resolveAttribute(attrRes, typedValue, true);
            result = typedValue.data;
        } catch (Exception e) {

        }
        return result;
    }

    public float getAttrFloat(int attrRes) {
        return getAttrFloat(attrRes, 1.0f);
    }

    public float getAttrFloat(int attrRes, float def) {
        float result = def;
        try {
            TypedValue typedValue = new TypedValue();
            mContext.getTheme().resolveAttribute(attrRes, typedValue, true);
            result = typedValue.getFloat();
        } catch (Exception e) {

        }
        return result == 0 ? def : result;
    }
}
