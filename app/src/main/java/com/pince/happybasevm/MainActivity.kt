package com.pince.happybasevm

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hapi.base_mvvm.mvvm.BaseVmActivity
import com.hipi.vm.createVm

class MainActivity : BaseVmActivity() {

    val vm by createVm<MainVm>()

    override fun observeLiveData() {

    }

    override fun getToolBarTitle(): String {
        return "阿三的撒"
    }
    override fun initViewData() {

     mToolbar?.setBackgroundColor(Color.YELLOW)
        setTittleColor(Color.RED)
        mToolbar?.setPadding(0,60,0,0)
    }

    override fun isTitleCenter(): Boolean {
        return true
    }

    override fun requestNavigationIcon(): Int {
        return R.mipmap.back_white_temp
    }
    override fun isToolBarEnable(): Boolean {
        return true
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_main2
    }

    override fun showLoading(toShow: Boolean) {
    }

}