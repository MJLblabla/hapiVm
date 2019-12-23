package com.pince.happybasevm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.hapi.happy_dialog.BaseDialogFragment
import com.hipi.vm.BaseViewModel
import com.pince.happybasevm.been.User

class LoginVm(application: Application, bundle: Bundle?): BaseViewModel(application,bundle) {

    val loginLiveData by lazy { MutableLiveData<User>() }

    init {
        checkAutoLogin()
    }


    //检查是不是可以自动登录
    private fun checkAutoLogin(){
        // load sp
        //...
        // get user  from sp
        val user = User(0)
        //show loading
        showLoadingCall?.invoke(true)
        // ....do auto login api and get userInfo

        loginLiveData.value = user

        showLoadingCall?.invoke(false)
    }


    /**
     * 账号密码登录
     */
    fun loginByPwd(){
        //show loading
        showLoadingCall?.invoke(true)
        //

        val user = User(0)
        //从服务器获用户
        // ....do auto login api and get userInfo

        if(false){

            //提示一个弹窗
            showDialog("Login"){
                MyDialogFragment().apply {
                    setDefaultListener(object : BaseDialogFragment.BaseDialogListener() {
                        override fun onDialogNegativeClick(dialog: androidx.fragment.app.DialogFragment, any: Any) {
                            super.onDialogNegativeClick(dialog, any)
                        }

                        override fun onDialogPositiveClick(dialog: androidx.fragment.app.DialogFragment, any: Any) {
                            super.onDialogPositiveClick(dialog, any)
                        }

                        override fun onDismiss(dialog: androidx.fragment.app.DialogFragment, any: Any) {
                            super.onDismiss(dialog, any)
                        }
                    })
                }
            }
        }

        toast("登录成功")
        //保存　sp 用户信息
        // send to ui
        loginLiveData.value = user

        showLoadingCall?.invoke(false)

    }
}

