package com.tourcoo.training.ui.message;


import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tourcoo.training.R;
import com.tourcoo.training.core.util.CommonUtil;
import com.tourcoo.training.entity.message.MessageEntity;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年05月08日18:05
 * @Email: 971613168@qq.com
 */
public class MessageAdapter  extends BaseQuickAdapter<MessageEntity, BaseViewHolder> {
    public MessageAdapter() {
        super(R.layout.item_system_msg);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageEntity item) {
        helper.setText(R.id.tvMsgTime, item.getCreateTime());
        helper.setText(R.id.tvMsgContent, CommonUtil.getNotNullValue(item.getTitle()));
        if (item.getIsRead() == 1) {
            helper.setVisible(R.id.tvRedDot, false);
        } else {
            helper.setVisible(R.id.tvRedDot, true);
        }

    }
}
