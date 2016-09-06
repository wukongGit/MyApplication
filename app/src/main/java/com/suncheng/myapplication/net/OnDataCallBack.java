package com.suncheng.myapplication.net;

public interface OnDataCallBack<T extends Object> {

    /**
     * 注意，这里的 statasCode 是泰九自己定的，跟http一点关系都没有
     * @param result
     * @param statusCode
     */
    void onSuccess(T result, int statusCode, String message);

    void onFailed(Exception e, int responseCode);

}