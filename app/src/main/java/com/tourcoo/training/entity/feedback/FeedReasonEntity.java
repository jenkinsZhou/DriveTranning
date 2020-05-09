package com.tourcoo.training.entity.feedback;

import com.contrarywind.interfaces.IPickerViewData;
import com.tourcoo.training.core.util.CommonUtil;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :翼迈科技股份有限公司
 * @date 2020年05月08日23:06
 * @Email: 971613168@qq.com
 */
public class FeedReasonEntity implements IPickerViewData {


    /**
     * ID : 1
     * Name : 产品优化
     */

    private int ID;
    private String Name;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    @Override
    public String getPickerViewText() {
        return CommonUtil.getNotNullValue(Name);
    }
}
