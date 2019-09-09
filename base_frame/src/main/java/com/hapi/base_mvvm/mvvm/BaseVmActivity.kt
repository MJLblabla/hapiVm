package com.hapi.base_mvvm.mvvm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import com.hapi.base_mvvm.activity.BaseFrameActivity
import com.hipi.vm.VmBinding


/**
 * @author manjiale
 * mvvm activity
 */
abstract class BaseVmActivity: BaseFrameActivity() {





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