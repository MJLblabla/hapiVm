package com.pince.happybasevm

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import com.hipi.vm.BaseViewModel

class TestVm (application: Application ,bundle: Bundle?): BaseViewModel(application,bundle) {

    var i = 0
    init {
        bundle?.let {
            i = it.getInt("number",1)
        }
    }
    val testLiveData by  lazy { MutableLiveData<Int>() }

    fun doBusiness(){
        i++
        testLiveData.value = i
    }
}