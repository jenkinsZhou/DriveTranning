package com.tourcoo.training.core.impl;

import android.accounts.AccountsException;
import android.accounts.NetworkErrorException;


import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ResourceUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.JsonParseException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.tourcoo.training.BuildConfig;
import com.tourcoo.training.R;
import com.tourcoo.training.config.AppConfig;
import com.tourcoo.training.core.interfaces.HttpRequestControl;
import com.tourcoo.training.core.interfaces.IHttpRequestControl;
import com.tourcoo.training.core.interfaces.OnHttpRequestListener;
import com.tourcoo.training.core.log.TourCooLogUtil;
import com.tourcoo.training.core.util.ResourceUtil;
import com.tourcoo.training.core.util.ToastUtil;
import com.tourcoo.training.utils.network.NetworkUtil;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;
import retrofit2.HttpException;

import static com.tourcoo.training.config.RequestConfig.START_PAGE;

/**
 * @Author: JenkinsZhou on 2018/12/4 18:08
 * @E-Mail: 971613168@qq.com
 * @Function: 网络请求成功/失败全局处理
 * @Description:
 */
public class HttpRequestControlImpl implements HttpRequestControl {

    private static String TAG = "HttpRequestControlImpl";

    @Override
    public void httpRequestSuccess(IHttpRequestControl httpRequestControl, List<?> list, OnHttpRequestListener listener) {
        if (httpRequestControl == null) {
            return;
        }
        SmartRefreshLayout smartRefreshLayout = httpRequestControl.getRefreshLayout();
        BaseQuickAdapter adapter = httpRequestControl.getRecyclerAdapter();
        StatusLayoutManager statusLayoutManager = httpRequestControl.getStatusLayoutManager();
        int page = httpRequestControl.getCurrentPage();
        int size = httpRequestControl.getPageSize();

        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }
        if (adapter == null) {
            if (statusLayoutManager != null) {
                statusLayoutManager.showEmptyLayout();
            }
            return;
        }
        adapter.loadMoreComplete();
        if (list == null || list.size() == 0) {
            //第一页没有
            if (page == START_PAGE || page == 0) {
                adapter.setNewData(new ArrayList());
                statusLayoutManager.showEmptyLayout();
                if (listener != null) {
                    listener.onEmpty();
                }
            } else {
                adapter.loadMoreEnd();
                if (listener != null) {
                    listener.onNoMore();
                }
            }
            return;
        }
        statusLayoutManager.showSuccessLayout();
        if (smartRefreshLayout.getState() == RefreshState.Refreshing || page == START_PAGE || page == 0) {
            adapter.setNewData(new ArrayList());
        }
        adapter.addData(list);
        if (listener != null) {
            listener.onNext();
        }
        if (list.size() < size) {
            adapter.loadMoreEnd();
            if (listener != null) {
                listener.onNoMore();
            }
        }
    }

    @Override
    public void httpRequestError(IHttpRequestControl httpRequestControl, Throwable e) {
        TourCooLogUtil.e("httpRequestError:" + e.getMessage());
        int reason = R.string.frame_exception_other_error;
//        int code = FastError.EXCEPTION_OTHER_ERROR;
        if (!NetworkUtils.isConnected()) {
            reason = R.string.frame_exception_network_not_connected;
        } else {
            //网络异常--继承于AccountsException
            if (e instanceof NetworkErrorException) {
                reason = R.string.frame_exception_network_error;
                //账户异常
            } else if (e instanceof AccountsException) {
                reason = R.string.frame_exception_accounts;
                //连接异常--继承于SocketException
            } else if (e instanceof ConnectException) {
                reason = R.string.frame_exception_connect;
                //socket异常
            } else if (e instanceof SocketException) {
                reason = R.string.frame_exception_socket;
                // http异常
            } else if (e instanceof HttpException) {
                reason = R.string.frame_exception_http;
                //DNS错误
            } else if (e instanceof UnknownHostException) {
                reason = R.string.frame_exception_unknown_host;
            } else if (e instanceof JsonParseException) {
                //数据格式化错误
                reason = R.string.frame_exception_json_syntax;
            } else if (e instanceof SocketTimeoutException || e instanceof TimeoutException) {
                reason = R.string.frame_exception_time_out;
            } else if (e instanceof ClassCastException) {
                reason = R.string.frame_exception_class_cast;
            }
        }
        if (httpRequestControl == null) {
            TourCooLogUtil.e(TAG, "httpRequestControl==null!");
        } else if (httpRequestControl.getStatusLayoutManager() == null) {
            TourCooLogUtil.e(TAG, "getStatusLayoutManager==null!");
        }
        if (httpRequestControl == null || httpRequestControl.getStatusLayoutManager() == null) {
//            ToastUtil.show(reason);
            if (AppConfig.DEBUG_MODE) {
                ToastUtil.show(reason);
            } else {
                handleRequestErrorTips();
            }

            return;
        }
        SmartRefreshLayout smartRefreshLayout = httpRequestControl.getRefreshLayout();
        BaseQuickAdapter adapter = httpRequestControl.getRecyclerAdapter();
        StatusLayoutManager statusLayoutManager = httpRequestControl.getStatusLayoutManager();
        int page = httpRequestControl.getCurrentPage();
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh(false);
        }
        if (adapter != null) {
            adapter.loadMoreComplete();
            if (statusLayoutManager == null) {
                return;
            }
            //初始页
            if (page == START_PAGE || page == 0) {
//                if (!NetworkUtils.isConnected()) {
////                    //可自定义网络错误页面展示
//                    statusLayoutManager.showCustomLayout(R.layout.layout_status_layout_manager_error);
//                } else {
                statusLayoutManager.showErrorLayout();
//                }
                return;
            }
            //可根据不同错误展示不同错误布局  showCustomLayout(R.layout.xxx);
            statusLayoutManager.showErrorLayout();
        }
    }


    private void handleRequestErrorTips() {
        if (!NetworkUtils.isConnected()) {
            ToastUtil.show(R.string.frame_exception_network_not_connected);
            return;
        }
        ToastUtil.show("服务器开了点小差 请稍后再试");
    }
}
