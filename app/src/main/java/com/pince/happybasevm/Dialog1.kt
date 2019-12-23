package com.pince.happybasevm

import androidx.lifecycle.Observer
import android.view.Gravity
import com.hapi.base_mvvm.mvvm.BaseVmFragment
import com.hapi.happy_dialog.BaseDialogFragment
import com.hapi.happy_dialog.BaseVmDialogFragment
import com.hapi.vmannotation.VmType
import com.hapi.vmannotation.vm
import kotlinx.android.synthetic.main.dialog1.*

class Dialog1 : BaseVmDialogFragment()  {


    override fun getViewLayoutId(): Int {
        return R.layout.dialog1
    }

    init {
        applyGravityStyle(Gravity.CENTER)
        applyCancelable(true)
    }

    @vm(vmType = VmType.FROM_ACTIVITY)
    lateinit var mTestVm: TestVm

    override fun observeLiveData() {
        mTestVm.testLiveData.observe(this, Observer {
            textView.text = "点我变变变　我共用activity 的数据　${it}"
        })
    }

    override fun initViewData() {
        textView.setOnClickListener {
            // 模拟处理某业务
            mTestVm.doBusiness()
        }
    }


    override fun showLoading(toShow: Boolean) {

    }

}