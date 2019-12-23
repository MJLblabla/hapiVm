package com.pince.happybasevm

import androidx.lifecycle.Observer
import android.view.Gravity
import com.hapi.happy_dialog.BaseVmDialogFragment
import com.hapi.vmannotation.vm
import kotlinx.android.synthetic.main.dialog1.*

class Dialog2 : BaseVmDialogFragment()  {


    init {
        applyGravityStyle(Gravity.CENTER)
        applyCancelable(true)
    }

    @vm
    lateinit var mTestVm: TestVm

    override fun observeLiveData() {
        mTestVm.testLiveData.observe(this, Observer {
            textView.text = "点我变变变 我使用新的vm 的数据　${it}"
        })
    }

    override fun initViewData() {
        textView.setOnClickListener {
            // 模拟处理某业务
            mTestVm.doBusiness()
        }
    }

    override fun getViewLayoutId(): Int {
        return R.layout.dialog1
    }

    override fun showLoading(toShow: Boolean) {
    }
}