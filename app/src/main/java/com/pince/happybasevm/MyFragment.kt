package com.pince.happybasevm

import android.support.v4.app.Fragment
import com.hapi.base_mvvm.fragment.BaseFrameFragment
import com.hapi.vmannotation.VmType
import com.hapi.vmannotation.vm

class MyFragment : BaseFrameFragment() {

    @vm(vmType = VmType.FROM_ACTIVITY)
    lateinit var testVm: TestVm


    @vm
    lateinit var loginVm: LoginVm

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun showLoading(toShow: Boolean) {
    }

}