package com.pince.happybasevm

import android.app.Application
import android.os.Bundle
import android.util.Log
import com.hipi.vm.BaseViewModel

class MainVm(application: Application,bundle: Bundle?):BaseViewModel(application,bundle) {

    init {
        Log.d("mjl","MainVm init")
    }
}