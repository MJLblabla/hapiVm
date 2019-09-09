package com.hapi.base_mvvm.mvvm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.hapi.base_mvvm.fragment.BaseFrameFragment
import com.hipi.vm.VmBinding

abstract class BaseVmFragment: BaseFrameFragment() {



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        VmBinding.bindVm(this)
        observeLiveData()
        initViewData()
    }


    abstract fun observeLiveData()


    abstract fun initViewData()


}