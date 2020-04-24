package com.tourcoo.training.widget.aliplayer.utils.database;


import com.tourcoo.training.widget.aliplayer.utils.download.AliyunDownloadMediaInfo;

import java.util.List;

/**
 * 数据库加载数据监听
 *
 * @author hanyu
 */
public interface LoadDbDatasListener {

    /**
     * 数据加载成功
     */
    public void onLoadSuccess(List<AliyunDownloadMediaInfo> dataList);
}
