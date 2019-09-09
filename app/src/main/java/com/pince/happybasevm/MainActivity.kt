package com.pince.happybasevm

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hapi.base_mvvm.mvvm.BaseVmActivity
import com.hapi.vmannotation.vm

class MainActivity : BaseVmActivity() {


    @vm
    lateinit var mTestVm:TestVm


    @vm
    lateinit var loginVm: LoginVm

    override fun observeLiveData() {

    }

    override fun initViewData() {
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun showLoading(toShow: Boolean) {
    }



}
