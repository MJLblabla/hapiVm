package com.hipi.vm

import android.app.Application
import android.os.Bundle
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import java.lang.reflect.InvocationTargetException


class FragmentVmFac(
    private val application: Application,
    private val bundle: Bundle?,
    private val f: Fragment
) : AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return try {
            val constructor1 = modelClass.getConstructor(
                Application::class.java,
                Bundle::class.java
            )
            val vm: T =
                if (constructor1 == null) {
                    val constructor2 =
                        modelClass.getConstructor(Application::class.java)
                    constructor2.newInstance(application)
                } else {
                    constructor1.newInstance(application, bundle)
                }
            if (vm is BaseViewModel) {
                vm.finishedActivityCall = { f.activity?.finish() }
                vm.getFragmentManagrCall = { f.childFragmentManager }
                if (f is LoadingObserverView) {
                    vm.showLoadingCall = {
                        f.showLoading(it)
                    }
                }
            }
            vm

        } catch (e: NoSuchMethodException) {
            super.create(modelClass)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        }
    }
}


class ActivityVmFac(
    private val application: Application,
    private val bundle: Bundle?,
    private val act: FragmentActivity
) : AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return try {
            val constructor1 = modelClass.getConstructor(
                Application::class.java,
                Bundle::class.java
            )
            val vm: T =
                if (constructor1 == null) {
                    val constructor2 =
                        modelClass.getConstructor(Application::class.java)
                    constructor2.newInstance(application)
                } else {
                    constructor1.newInstance(application, bundle)
                }
            if (vm is BaseViewModel) {
                vm.finishedActivityCall = { act.finish() }
                vm.getFragmentManagrCall = { act.supportFragmentManager }
                if (act is LoadingObserverView) {
                    vm.showLoadingCall = {
                        act.showLoading(it)
                    }
                }
            }
            vm

        } catch (e: NoSuchMethodException) {
            super.create(modelClass)
        } catch (e: IllegalAccessException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: InstantiationException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: InvocationTargetException) {
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        }
    }
}


inline fun <reified VM : BaseViewModel> FragmentActivity.createVm(
    factoryProducer: AndroidViewModelFactory? = null
): VM {
    val factoryPromise = factoryProducer ?: ActivityVmFac(application, intent.extras, this);
    val vm = ViewModelProviders.of(this, factoryPromise).get(VM::class.java)
    return vm
}



inline fun <reified VM : BaseViewModel> Fragment.createVm(
    factoryProducer: AndroidViewModelFactory? = null
): VM {
    val act = requireActivity()
    val factoryPromise = factoryProducer ?: FragmentVmFac(act.application,   arguments, this);
    val vm = ViewModelProviders.of(this, factoryPromise).get(VM::class.java)
    return vm
}


inline fun <reified VM : BaseViewModel> FragmentActivity.lazyVm(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): Lazy<VM> {
    val factoryPromise = factoryProducer ?: {
        ActivityVmFac(application, intent.extras, this);
    }
    val vm = ViewModelLazy(VM::class, { viewModelStore }, factoryPromise)
    return vm
}

@MainThread
inline fun <reified VM : BaseViewModel> Fragment.lazyVm(
    noinline ownerProducer: () -> ViewModelStoreOwner = { this },
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = {
        FragmentVmFac(
            activity!!.application,
            arguments,
            this
        )
    }
): Lazy<VM> {
    val vm = createViewModelLazy(VM::class, { ownerProducer().viewModelStore }, factoryProducer);

    return vm
}


@MainThread
inline fun <reified VM : BaseViewModel> Fragment.activityVm(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
) = createViewModelLazy(VM::class, { requireActivity().viewModelStore },
    factoryProducer ?: {

        val act = requireActivity()
        ActivityVmFac(act.application, act.intent.extras, act);
    }
)

