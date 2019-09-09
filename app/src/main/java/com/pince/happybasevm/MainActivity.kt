package com.pince.happybasevm

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hapi.base_mvvm.mvvm.BaseVmActivity
import com.hapi.vmannotation.vm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseVmActivity() {
    /**
     * 某个业务组件
     */
    @vm
    lateinit var mTestVm:TestVm
    /**
     * 登录业务组件
     */
    @vm
    lateinit var loginVm: LoginVm

     //组合自己该页面需要的业务组件
    //.....

    override fun observeLiveData() {
        loginVm.loginLiveData.observe(this, Observer {
            //　登录成功　监听
        })

        mTestVm.testLiveData.observe(this, Observer {

            // 干某事跟新ui
        })
    }

    override fun initViewData() {
        tvHelloWord.setOnClickListener {
            // 模拟处理某业务
            mTestVm.doBusiness()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun showLoading(toShow: Boolean) {
    }
}
