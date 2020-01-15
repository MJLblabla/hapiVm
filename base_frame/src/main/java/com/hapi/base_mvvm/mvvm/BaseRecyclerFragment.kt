package com.hapi.base_mvvm.mvvm

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.hapi.base_mvvm.R
import com.hapi.base_mvvm.wigdt.refresh.IEmptyView
import com.hapi.base_mvvm.wigdt.refresh.SmartRecyclerView

import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.layout_smartrecycler.*

abstract class BaseRecyclerFragment<T> : BaseVmFragment() {

    override fun getLayoutId(): Int {
        return R.layout.layout_smartrecycler
    }

    protected val preLoadNumber: Int = 1
    protected val mSmartRecycler: SmartRecyclerView
        get() {
            return smartRecycler
        }

    protected val mRecyclerView: RecyclerView
        get() {
            return smartRecycler.recyclerView
        }

    abstract val adapter: BaseQuickAdapter<T, *>
    abstract val layoutManager: RecyclerView.LayoutManager
    /**
     * 刷新回调
     */
    abstract val fetcherFuc: ((page: Int) -> Unit)

    open fun loadMoreNeed(): Boolean {
        return true
    }

    open fun refreashNeed(): Boolean {
        return true
    }

    /**
     * 通用emptyView
     */
    abstract fun getEmptyView(): IEmptyView?
    open fun getGetRefreshHeader(): RefreshHeader {
        return ClassicsHeader(context)
    }
    open fun isRefreshAtOnStart():Boolean{
        return true
    }

    override fun initViewData() {
        mRecyclerView.layoutManager = layoutManager
        smartRecycler.setReFreshHearfer(getGetRefreshHeader())
        smartRecycler.setUp(
            adapter,
            getEmptyView(),
            preLoadNumber,
            loadMoreNeed(),
            refreashNeed(),
            fetcherFuc
        )
        if(isRefreshAtOnStart()){
            smartRecycler.startRefresh()
        }
    }
}