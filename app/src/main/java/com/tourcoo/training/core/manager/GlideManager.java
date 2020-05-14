package com.tourcoo.training.core.manager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.tourcoo.training.R;
import com.tourcoo.training.core.log.TourCooLogUtil;

import java.security.MessageDigest;


/**
 * @Author: JenkinsZhou on 2018/7/23 10:53
 * @E-Mail: 971613168@qq.com
 * Function: Glide 工具类支持加载常规、圆角、圆形图片
 * Description:
 */
public class GlideManager {

    private static int sCommonPlaceholder = -1;
    private static int sCirclePlaceholder = -1;
    private static int sRoundPlaceholder = -1;

    private static Drawable sCommonPlaceholderDrawable;
    private static Drawable sCirclePlaceholderDrawable;
    private static Drawable sRoundPlaceholderDrawable;
    @ColorInt
    private static int mPlaceholderColor = Color.LTGRAY;
    private static float mPlaceholderRoundRadius = 4f;

    private static void setDrawable(GradientDrawable gd, float radius) {
        gd.setColor(mPlaceholderColor);
        gd.setCornerRadius(radius);
    }

    /**
     * 设置默认颜色
     *
     * @param placeholderColor
     */
    public static void setPlaceholderColor(@ColorInt int placeholderColor) {
        mPlaceholderColor = placeholderColor;
        sCommonPlaceholderDrawable = new GradientDrawable();
        sCirclePlaceholderDrawable = new GradientDrawable();
        sRoundPlaceholderDrawable = new GradientDrawable();
        setDrawable((GradientDrawable) sCommonPlaceholderDrawable, 0);
        setDrawable((GradientDrawable) sCirclePlaceholderDrawable, 10000);
        setDrawable((GradientDrawable) sRoundPlaceholderDrawable, mPlaceholderRoundRadius);
    }

    /**
     * 设置圆角图片占位背景图圆角幅度
     *
     * @param placeholderRoundRadius
     */
    public static void setPlaceholderRoundRadius(float placeholderRoundRadius) {
        mPlaceholderRoundRadius = placeholderRoundRadius;
        setPlaceholderColor(mPlaceholderColor);
    }

    /**
     * 设置圆形图片的占位图
     *
     * @param circlePlaceholder
     */
    public static void setCirclePlaceholder(int circlePlaceholder) {
        sCirclePlaceholder = circlePlaceholder;
    }

    public static void setCirclePlaceholder(Drawable circlePlaceholder) {
        sCirclePlaceholderDrawable = circlePlaceholder;
    }

    /**
     * 设置正常图片的占位符
     *
     * @param commonPlaceholder
     */
    public static void setCommonPlaceholder(int commonPlaceholder) {
        sCommonPlaceholder = commonPlaceholder;
    }

    public static void setCommonPlaceholder(Drawable commonPlaceholder) {
        sCommonPlaceholderDrawable = commonPlaceholder;
    }

    /**
     * 设置圆角图片的占位符
     *
     * @param roundPlaceholder
     */
    public static void setRoundPlaceholder(int roundPlaceholder) {
        sRoundPlaceholder = roundPlaceholder;
    }

    public static void setRoundPlaceholder(Drawable roundPlaceholder) {
        sRoundPlaceholderDrawable = roundPlaceholder;
    }

    /**
     * 普通加载图片
     *
     * @param obj
     * @param iv
     * @param placeholder
     */
    public static void loadImg(Object obj, ImageView iv, Drawable placeholder) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptions()
                .placeholder(placeholder)
                .fallback(placeholder)
                .dontAnimate()
                .error(placeholder)
        ).into(iv);
    }


    public static void loadImg(Object obj, ImageView iv, int placeholderResource) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        loadImg(obj, iv, drawable != null ? drawable : sCommonPlaceholderDrawable);
    }


    public static void loadGrayImg(Object obj, ImageView iv, int placeholderResource) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        drawable = drawable != null ? drawable : sCommonPlaceholderDrawable;
        drawable.setColorFilter(Color.parseColor("#D2D2D2"), PorterDuff.Mode.MULTIPLY);
        loadImg(obj, iv, drawable);
    }


    public static void loadImg(Object obj, ImageView iv) {
        loadImg(obj, iv, sCommonPlaceholder);
    }

    /**
     * 加载圆形图片
     *
     * @param obj
     * @param iv
     * @param placeholder 占位图
     */
    public static void loadCircleImg(Object obj, ImageView iv, Drawable placeholder) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptions()
                .error(placeholder)
                .placeholder(placeholder)
                .fallback(placeholder)
                .dontAnimate()
                .transform(new CircleCrop())).into(iv);
    }

    public static void loadImageAuto(Object obj, ImageView iv, int placeholderResource) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        loadImageAuto(obj, iv, drawable != null ? drawable : sCirclePlaceholderDrawable);
    }

    public static void loadImageAuto(Object obj, ImageView iv) {
        loadImageAuto(obj, iv, sCirclePlaceholder);
    }

    public static void loadImageAuto(Object obj, ImageView iv, Drawable placeholder) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptionsAuto()
                .error(placeholder)
                .placeholder(placeholder)
                .fallback(placeholder)
                .dontAnimate()).into(new ImageViewTarget<Drawable>(iv) {
            @Override
            protected void setResource(@Nullable Drawable resource) {
                if (resource == null) {
                    return;
                }
                view.setImageDrawable(resource);

                //获取原图的宽高
                int width = resource.getIntrinsicWidth();
                int height = resource.getIntrinsicHeight();

                //获取imageView的宽
                int imageViewWidth = iv.getWidth();
                //计算缩放比例
                float sy = (float) (imageViewWidth) / (float) (width);
                //计算图片等比例放大后的高
                int imageViewHeight = (int) (height * sy);
                ViewGroup.LayoutParams params = iv.getLayoutParams();
                params.height = imageViewHeight;
                iv.setLayoutParams(params);
                TourCooLogUtil.i("GlideManager", "比例:" + sy + "width=" + width + "" + "height=" + height + "放大后的高:" + imageViewHeight);
            }
        });

    }


    public static void loadCircleImg(Object obj, ImageView iv, int placeholderResource) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        loadCircleImg(obj, iv, drawable != null ? drawable : sCirclePlaceholderDrawable);
    }

    public static void loadCircleImg(Object obj, ImageView iv) {
        loadCircleImg(obj, iv, sCirclePlaceholder);
    }

    /**
     * 加载圆角图片
     *
     * @param obj                 加载的图片资源
     * @param iv
     * @param dp                  圆角尺寸-dp
     * @param placeholder         -占位图
     * @param isOfficial-是否官方模式圆角
     */
    public static void loadRoundImg(Object obj, ImageView iv, float dp, Drawable placeholder, boolean isOfficial) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptions()
                .error(placeholder)
                .placeholder(placeholder)
                .fallback(placeholder)
                .dontAnimate()
                .transform(isOfficial ? new RoundedCorners(dp2px(dp)) : new GlideRoundTransform(dp2px(dp)))).into(iv);
    }

    public static void loadRoundImg(Object obj, ImageView iv, float dp, int placeholderResource, boolean isOfficial) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        loadRoundImg(obj, iv, dp, drawable != null ? drawable : sRoundPlaceholderDrawable, isOfficial);
    }

    public static void loadRoundImg(Object obj, ImageView iv, float dp, boolean isOfficial) {
        loadRoundImg(obj, iv, dp, sRoundPlaceholder, isOfficial);
    }

    public static void loadRoundImg(Object obj, ImageView iv, float dp) {
        loadRoundImg(obj, iv, dp, true);
    }

    public static void loadRoundImg(Object obj, ImageView iv, boolean isOfficial) {
        loadRoundImg(obj, iv, mPlaceholderRoundRadius, isOfficial);
    }

    public static void loadRoundImg(Object obj, ImageView iv) {
        loadRoundImg(obj, iv, true);
    }

    private static RequestOptions getRequestOptions() {
        RequestOptions requestOptions = new RequestOptions()
                // 填充方式
                .centerCrop()
                //优先级
                .priority(Priority.HIGH)
                //缓存策略
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        return requestOptions;
    }

    private static RequestOptions getRequestOptionsAuto() {
        RequestOptions requestOptions = new RequestOptions()
                //优先级
                .priority(Priority.HIGH)
                //缓存策略
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        return requestOptions;
    }

    private static int dp2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private static Drawable getDrawable(Context context, @DrawableRes int res) {
        Drawable drawable = null;
        try {
            drawable = context.getResources().getDrawable(res);
        } catch (Exception e) {

        }
        return drawable;
    }

    private static class GlideRoundTransform extends BitmapTransformation {
        int radius;

        public GlideRoundTransform(int dp) {
            super();
            this.radius = dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {

        }
    }


    public static void loadImageByXml(Object obj, ImageView iv, int placeholderResource) {
        Drawable drawable = getDrawable(iv.getContext(), placeholderResource);
        loadImgXml(obj, iv, drawable != null ? drawable : sCirclePlaceholderDrawable);
    }

    /**
     * 更加xml scanType显示
     *
     * @param obj
     * @param iv
     * @param placeholder
     */
    public static void loadImgXml(Object obj, ImageView iv, Drawable placeholder) {
        Glide.with(iv.getContext()).load(obj).apply(getRequestOptionsAuto()
                .placeholder(placeholder)
                .fallback(placeholder)
                .dontAnimate()
                .error(placeholder)
        ).into(iv);
    }
}