package com.tourcoo.training.widget.banner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.tourcoo.training.R;
import com.tourcoo.training.core.manager.GlideManager;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年03月10日16:34
 * @Email: 971613168@qq.com
 */
public class ImageBannerAdapter  extends BannerAdapter<BannerEntity,ImageHolder> {
    public ImageBannerAdapter(List<BannerEntity> bannerEntityList) {
        super(bannerEntityList);
    }

    @Override
    public ImageHolder onCreateHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_layout , parent ,false);
        RoundedImageView roundImageView = item.findViewById(R.id.roundImageView);
        roundImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new ImageHolder(item);
    }

    @Override
    public void onBindView(ImageHolder holder, BannerEntity data, int position, int size) {
       /* Glide.with(holder.itemView)
                .load(data.imageUrl)
                .into(holder.imageView);*/
        GlideManager.loadImg(data.imageUrl,holder.imageView);
    }

}
