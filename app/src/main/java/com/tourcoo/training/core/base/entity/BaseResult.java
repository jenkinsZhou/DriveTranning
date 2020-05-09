package com.tourcoo.training.core.base.entity;

/***
 * 基础数据结构
 */
public class BaseResult<T> {

    public int code;
    public String msg;
    public T data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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
                "code=" + code +
                ", message='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}