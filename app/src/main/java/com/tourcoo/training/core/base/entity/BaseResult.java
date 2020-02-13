package com.tourcoo.training.core.base.entity;

/***
 * 基础数据结构
 */
public class BaseResult<T> {

    private int status;
    private String errorMsg;
    private T data;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "status=" + status +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}