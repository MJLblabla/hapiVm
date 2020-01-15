package com.hapi.base_mvvm.wigdt.refresh

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView
import com.hapi.base_mvvm.wigdt.refresh.IEmptyView.*

import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * 上拉下拉帮助类
 *
 *
 */
open class SmartRefreshHelper<T>(val adapter: BaseQuickAdapter<T, *>,
                                 private val recycler_view: RecyclerView,
                                 private val refresh_layout: SmartRefreshLayout,
                                 private val emptyCustomView: IEmptyView?,
                                 /**
                                  * 静默加载数量
                                  */
                                 private val preLoadNumber: Int,
                                 /**
                                  * 是否需要加载更多
                                  */
                                 private val isNeedLoadMore: Boolean=true,
                                 private val refreshNeed:Boolean=true,
                                 /**
                                  * 刷新回调
                                  */
                                 private val fetcherFuc: (page: Int) -> Unit) {

    private var isLoadMoreing: Boolean = false
    private var isRefreshing: Boolean = false
    private var currentPage = 0

    private var eachPageSize: Int = 0


    init {
        adapter.setEnableLoadMore(isNeedLoadMore)
        if (isNeedLoadMore) {
            adapter.setPreLoadNumber(preLoadNumber)
            val simpleLoadMoreView = SimpleLoadMoreView()
            adapter.setLoadMoreView(simpleLoadMoreView)
            simpleLoadMoreView.setLoadMoreEndGone(true)
            adapter.setOnLoadMoreListener({ loadMore() }, recycler_view)
        }
        refresh_layout.setEnableLoadMore(refreshNeed)
        if(refreshNeed){
            refresh_layout.setOnRefreshListener {
                refresh()
            }
        }
    }


    /**
     * 获取到分页数据 设置下拉刷新和上拉的状态
     */
    fun onFetchDataFinish(data: List<T>?) {
        onFetchDataFinish(data, true)
    }


    fun onFetchDataFinish(data: List<T>?, goneIfNoData: Boolean) {

        refresh_layout.finishRefresh(true)
        if (data != null) {
            if (currentPage == 0 && isRefreshing) {
                eachPageSize = data.size
            }

            if (isLoadMoreing) {
                currentPage++
                adapter.addData(data)

            } else {
                adapter.setNewData(data)

            }

            if (data.size < eachPageSize) {
                adapter.loadMoreEnd(goneIfNoData)
            } else {
                adapter.loadMoreComplete()
            }
        }
        refreshEmptyView(NODATA)
        isLoadMoreing = false
        isRefreshing = false
    }


    /**
     * 加载数据失败
     */
    fun onFetchDataError() {
        if (isRefreshing) {
            refresh_layout.finishRefresh(false)
        } else {
            adapter.loadMoreFail()
        }
        val disconnected = !NetUtil.isNetworkAvailable(recycler_view.context)
        if (disconnected) {
            refreshEmptyView(NETWORK_ERROR)
        } else {
            refreshEmptyView(NODATA)
        }
        isLoadMoreing = false
        isRefreshing = false
    }

    private fun refreshEmptyView(type: Int) {

        if (adapter.data.isEmpty() && recycler_view.childCount == 0) {
            emptyCustomView?.setErrorType(type)
        } else {
            emptyCustomView?.setErrorType(HIDE_LAYOUT)
        }
    }

    private fun loadMore() {
        if (isRefreshing || isLoadMoreing) {
            if (isLoadMoreing) {
                isLoadMoreing = false
            }
            return
        }
        isLoadMoreing = true
        fetcherFuc(currentPage + 1)
    }


    fun refresh() {
        if (isRefreshing || isLoadMoreing) {
            if (isRefreshing) {
                isRefreshing = false
            }
            refresh_layout.finishRefresh(true)
            return
        }
        isRefreshing = true
        currentPage = 0
        fetcherFuc(0)
    }
}