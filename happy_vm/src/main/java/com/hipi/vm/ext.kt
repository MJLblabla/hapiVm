package com.hipi.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hapi.vmannotation.VmType


fun  <T : BaseViewModel> FragmentActivity.createVm(modelClass: Class<T>):T{
    val vm = getViewModel(this, modelClass, intent.extras)
    vm.finishedActivityCall = {finish()}
    vm.getFragmentManagrCall = {supportFragmentManager}
    if(this is LoadingObserverView){
        vm.showLoadingCall={
            showLoading(it)
        }
    }
    return vm
}


fun  <T : BaseViewModel> Fragment.createVm(modelClass: Class<T>, vmType: VmType):T{
    when(vmType){
        VmType.FROM_ACTIVITY ->{
            val vm = getViewModel(activity!!, modelClass, arguments)
            return vm
        }

        VmType.FROM_PARENT ->{
            val vm = getViewModel(parentFragment!!, modelClass, arguments)
            return vm
        }
        else ->{

            val vm = getViewModel(this, modelClass, arguments)
            vm.finishedActivityCall = {activity?.finish()}
            vm.getFragmentManagrCall = {childFragmentManager}
            if(this is LoadingObserverView){
                vm.showLoadingCall={
                    showLoading(it)
                }
            }
            return vm
        }
    }
}


private fun <T : ViewModel> getViewModel(context: Fragment,modelClass: Class<T>, bundle: Bundle?): T {
    return ViewModelProviders.of(context, ViewModelFactory(context.activity!!.application, bundle)).get(modelClass)
}

private fun <T : ViewModel> getViewModel(context: FragmentActivity,modelClass: Class<T>, bundle: Bundle?): T {
    return ViewModelProviders.of(context, ViewModelFactory(context.application, bundle)).get(modelClass)
}