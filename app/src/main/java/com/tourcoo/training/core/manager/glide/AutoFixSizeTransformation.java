package com.tourcoo.training.core.manager.glide;

import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.bumptech.glide.request.target.ImageViewTarget;

/**
 * @author :JenkinsZhou
 * @description :自适应宽高
 * @company :途酷科技
 * @date 2020年03月06日17:13
 * @Email: 971613168@qq.com
 */
public class AutoFixSizeTransformation extends ImageViewTarget<Bitmap> {
    private ImageView target;

    public AutoFixSizeTransformation(ImageView target) {
        super(target);
        this.target = target;
    }

    @Override
    protected void setResource(Bitmap resource) {
        view.setImageBitmap(resource);

        //获取原图的宽高
        int width = resource.getWidth();
        int height = resource.getHeight();

        //获取imageView的宽
        int imageViewWidth = target.getWidth();

        //计算缩放比例
        float sy = (float) (imageViewWidth * 0.1) / (float) (width * 0.1);

        //计算图片等比例放大后的高
        int imageViewHeight = (int) (height * sy);
        ViewGroup.LayoutParams params = target.getLayoutParams();
        params.height = imageViewHeight;
        target.setLayoutParams(params);
    }
}
