package com.hapi.base_mvvm.mvvm

import com.hapi.base_mvvm.activity.BaseFrameActivity
import com.hipi.vm.LoadingObserverView
import com.hipi.vm.VmBinding


/**
 * @author manjiale
 * mvvm activity
 */
abstract class BaseVmActivity: BaseFrameActivity(),LoadingObserverView {





    override fun init() {
        VmBinding.bindVm(this)
        observeLiveData()
        initViewData()
    }



    /**
     * 订阅vm
     */
    abstract fun observeLiveData()

    abstract fun initViewData()

}