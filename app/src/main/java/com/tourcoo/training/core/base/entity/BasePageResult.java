package com.tourcoo.training.core.base.entity;

import java.util.List;

/**
 * @author :JenkinsZhou
 * @description :分页实体基类
 * @company :途酷科技
 * @date 2019年12月04日17:46
 * @Email: 971613168@qq.com
 */
public class BasePageResult<T> {


    /**
     * currentPage : 0
     * elements : [{"bankCard":{"bank":"","cardNum":""},"failReason":"","id":"","name":"","withdrawGold":0,"withdrawMoney":0,"withdrawStatus":""}]
     * pages : 0
     * totalElements : 0
     */

    private int currentPage;
    private int pages;
    private int totalElements;
    private List<T> elements;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public List<T> getElements() {
        return elements;
    }

    public void setElements(List<T> elements) {
        this.elements = elements;
    }


}
