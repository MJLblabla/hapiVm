package com.pince.happybasevm

import android.support.v4.app.Fragment
import com.hapi.base_mvvm.fragment.BaseFrameFragment
import com.hapi.base_mvvm.mvvm.BaseVmFragment
import com.hapi.vmannotation.VmType
import com.hapi.vmannotation.vm

class MyFragment : BaseVmFragment() {

    //使用　父fragment  vm对象　　和父fragment通信
    @vm(vmType = VmType.FROM_PARENT)
    lateinit var testVm: TestVm

    //自己的vm
    @vm(vmType = VmType.FROM_NEW)
    lateinit var loginVm: LoginVm


    override fun observeLiveData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initViewData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading(toShow: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}