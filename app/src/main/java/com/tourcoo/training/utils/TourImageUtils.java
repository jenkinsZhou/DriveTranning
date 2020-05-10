package com.tourcoo.training.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;

public class TourImageUtils {

    public static void saveBitmap2Gallery(Activity mActivity, Bitmap bitmap) {
        ThreadPoolUtils.executeByIo(new ThreadPoolUtils.Task<Object>() {
            @Nullable
            @Override
            public Object doInBackground() throws Throwable {
                String fileName = "anpei_" + System.currentTimeMillis() + ".jpg";

                //系统相册目录
                String galleryPath = SDCardUtils.getSDCardPath() + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator;
                // 声明文件对象
                File file = null;
                //声明输出流
                FileOutputStream fos = null;
                try {
                    // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
                    file = new File(galleryPath, fileName);
                    // 获得文件相对路径
                    fileName = file.toString();
                    // 获得输出流，如果文件中有内容，追加内容
                    fos = new FileOutputStream(fileName);
                    if (null != fos) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        fos.close();
                    }
                }

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = mActivity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(uri);
                mActivity.sendBroadcast(intent);

                return null;
            }

            @Override
            public void onSuccess(@Nullable Object result) {
                ToastUtils.showShort("图片已保存");
            }

            @Override
            public void onCancel() {
                ToastUtils.showShort("保存取消");
            }

            @Override
            public void onFail(Throwable t) {
                ToastUtils.showShort("保存失败");
            }
        });
    }

}
