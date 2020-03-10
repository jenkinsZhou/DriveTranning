package com.tourcoo.training.widget.banner;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.tourcoo.training.R;

public class ImageHolder extends RecyclerView.ViewHolder {
    public RoundedImageView imageView;

    public ImageHolder(@NonNull View view) {
        super(view);
        this.imageView = view.findViewById(R.id.roundImageView);
    }




}