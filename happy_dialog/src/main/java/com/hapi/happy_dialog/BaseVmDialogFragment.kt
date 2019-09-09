package com.hapi.happy_dialog

import com.hipi.vm.VmBinding


abstract class BaseVmDialogFragment: BaseDialogFragment() {


    override fun init() {
        VmBinding.bindVm(this)
        observeLiveData()
        initViewData()
    }


    /**
     * 订阅vm
     * 如果使用自己的vm 也可以单独订阅一个fragment
     */
    abstract fun observeLiveData()

    abstract fun initViewData()


}