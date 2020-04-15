package com.tourcoo.training.core.base.entity;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :
 * @company :途酷科技
 * @date 2020年04月15日9:06
 * @Email: 971613168@qq.com
 */
public class PageData<T> {
    private int Total;
    private int Size;
    private List<T> Rows;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int total) {
        Total = total;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public List<T> getRows() {
        return Rows;
    }

    public void setRows(List<T> rows) {
        Rows = rows;
    }
}
