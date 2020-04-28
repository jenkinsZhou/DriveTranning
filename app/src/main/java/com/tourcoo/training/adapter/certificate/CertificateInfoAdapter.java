package com.tourcoo.training.adapter.certificate;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tourcoo.training.R;
import com.tourcoo.training.core.manager.GlideManager;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.entity.certificate.CertificateInfo;

import java.util.List;


/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月15日9:30
 * @Email: 971613168@qq.com
 */
public class CertificateInfoAdapter extends BaseMultiItemQuickAdapter<CertificateInfo, BaseViewHolder> {
    /*标题*/
    public final static int ITEM_TYPE_HEADER = 0;
    /*item*/
    public final static int ITEM_TYPE_CONTENT = 1;

    public CertificateInfoAdapter(List<CertificateInfo> data) {
        super(data);
        addItemType(ITEM_TYPE_HEADER, R.layout.item_study_certificate_title);
        //内容布局
        addItemType(ITEM_TYPE_CONTENT, R.layout.item_study_certificate_content);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, CertificateInfo item) {
        switch (helper.getItemViewType()) {
            case ITEM_TYPE_HEADER:
                helper.setText(R.id.tvCertificateMonth, item.getHeaderContent());
                break;
            case ITEM_TYPE_CONTENT:
                helper.setText(R.id.tvCertificateName, item.getTrainingPlanName());
                helper.setText(R.id.tvCertificateTime, CommonUtil.getNotNullValue(item.getCertificateTime()));
                RoundedImageView ivCertificateImage = helper.getView(R.id.rivCertificateImage);
                GlideManager.loadImg(CommonUtil.getUrl(item.getUrl()), ivCertificateImage);


                break;
        }


    }


}
