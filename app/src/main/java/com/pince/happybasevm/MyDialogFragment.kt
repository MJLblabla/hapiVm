package com.pince.happybasevm

import android.support.v4.app.DialogFragment
import com.hapi.vmannotation.VmType
import com.hapi.vmannotation.vm

class MyDialogFragment : DialogFragment() {

    @vm(vmType = VmType.FROM_PARENT)
    lateinit var testVm: TestVm
}