package com.hipi.vm

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.support.annotation.IntegerRes
import android.support.annotation.StringRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.text.TextUtils
import android.widget.Toast


/**
 * @author manjiale
 *
 */
open class BaseViewModel : AndroidViewModel {

    var mData: Bundle? = null
        private set

    /**
     * 获取activity fm
     */
    var getFragmentManagrCall: (() -> FragmentManager)? = null

    /**
     * 回调showloading
     */
    var showLoadingCall: ((show: Boolean) -> Unit)? = null

    /**
     * 接受activity 回调
     */
    var finishedActivityCall: (() -> Unit)? = null

    constructor(application: Application) : super(application)
    constructor(application: Application, data: Bundle?) : super(application) {
        mData = data
    }


    /**
     * 显示弹窗
     */
    fun showDialog(tag: String, call: () -> DialogFragment) {
        getFragmentManagrCall?.invoke()?.let {
            call().show(it, tag)
        }
    }


    override fun onCleared() {
        super.onCleared()
        removeCall()
    }
    /**
     * 页面销毁
     */
    private fun removeCall() {
        finishedActivityCall = null
        getFragmentManagrCall = null
        showLoadingCall = null
    }

    fun getAppContext(): Application {
        return getApplication<Application>()
    }

    fun toast(@StringRes msgRes: Int) {
        Toast.makeText(getAppContext(), getAppContext().resources.getString(msgRes), Toast.LENGTH_SHORT).show()
    }

    fun toast(msg: String?) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(getAppContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }

}