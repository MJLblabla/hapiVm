package com.pince.happybasevm

import android.support.v4.app.DialogFragment
import com.hapi.happy_dialog.BaseDialogFragment
import com.hapi.happy_dialog.BaseVmDialogFragment
import com.hapi.vmannotation.VmType
import com.hapi.vmannotation.vm

class MyDialogFragment : BaseVmDialogFragment() {

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

    override fun getViewLayoutId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}