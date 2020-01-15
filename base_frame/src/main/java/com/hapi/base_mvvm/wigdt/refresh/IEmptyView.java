package com.hapi.base_mvvm.wigdt.refresh;

import android.view.View;

public interface IEmptyView {

    /**
     * 隐藏
     */
    int HIDE_LAYOUT = 3;
    /**
     * 网络异常
     */
    int NETWORK_ERROR = 1;
    /**
     * 服务器数据空
     */
    int NODATA = 2;

    View getContentView();


    void setErrorType(int type);
}
