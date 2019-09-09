package com.hipi.vm

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.hapi.vmannotation.VmType


fun  <T : BaseViewModel> FragmentActivity.createVm(modelClass: Class<T>):T{
    val vm = getViewModel(this,modelClass,intent.extras)
    vm.finishedActivityCall = {finish()}
    vm.getFragmentManagrCall = {supportFragmentManager}
    if(this is LoadingObserverView){
        vm.showLoadingCall={
               showLoading(it)
        }
    }
    return vm
}


fun  <T : BaseViewModel> Fragment.createVm(modelClass: Class<T> , vmType: VmType):T{
    when(vmType){
        VmType.FROM_ACTIVITY ->{
            val vm = ViewModelProviders.of(activity!!)
                .get(modelClass)
            return vm
        }

        VmType.FROM_PARENT ->{
            val vm = ViewModelProviders.of(parentFragment!!)
                .get(modelClass)
            return vm
        }
        else ->{

            val vm = getViewModel(this,modelClass,arguments)
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