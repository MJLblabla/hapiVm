package com.hipi.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import android.os.Bundle
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import android.text.TextUtils
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception


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
    var getFragmentManagrCall: (() -> androidx.fragment.app.FragmentManager)? = null

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
    fun showDialog(tag: String, call: () -> androidx.fragment.app.DialogFragment) {
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



    fun BaseViewModel.bgDefault(
        block: suspend CoroutineScope.() -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                block.invoke(this)
            } catch (e: Exception) {
                e.printStackTrace()
                toast(e.message)
            } finally {

            }
        }
    }

    class CoroutineScopeWrap {
        var work:(  suspend CoroutineScope.() -> Unit )= {}
        var error: (e: Exception)->Unit ={}
        var complete : ()->Unit ={}

        fun doWork(call:  suspend CoroutineScope.() -> Unit ){
            this.work = call
        }

        fun catchError(error: (e: Exception)->Unit ){
            this.error=error
        }

        fun onFinally(call:()->Unit){
            this.complete=call
        }
    }

    fun BaseViewModel.bg(c:  CoroutineScopeWrap.() -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val block = CoroutineScopeWrap()
            c.invoke(block)
            try {
                block.work.invoke(this)

            } catch (e: Exception) {
                block.error.invoke( e)
            } finally {
                block.complete.invoke()
            }
        }
    }
}