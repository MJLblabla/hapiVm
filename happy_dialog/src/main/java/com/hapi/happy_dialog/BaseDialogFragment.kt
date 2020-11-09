package com.hapi.happy_dialog

import android.os.Bundle
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import android.view.*

abstract class BaseDialogFragment : DialogFragment() {


    var mDefaultListener: BaseDialogListener? = null
    private val INVALID_LAYOUT_ID = -1

    private var mCancelable: Boolean = true
    private var mGravityEnum:Int = Gravity.CENTER
    @StyleRes
    private var animationStyleresId: Int? = null

    fun setDefaultListener(defaultListener: BaseDialogListener): BaseDialogFragment {
        mDefaultListener = defaultListener
        return this
    }


    fun applyCancelable(cancelable: Boolean): BaseDialogFragment {
        mCancelable = cancelable
        return this
    }

    fun applyGravityStyle(gravity: Int): BaseDialogFragment {
        mGravityEnum = gravity
        return this
    }

    fun applyAnimationStyle(@StyleRes resId: Int): BaseDialogFragment {
        animationStyleresId = resId
        return this
    }


    /** The system calls this to get the DialogFragment's layout, regardless
     * of whether it's being displayed as a dialog or an embedded fragment.  */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView: View?
        // Inflate the layout to use as dialog or embedded fragment
        if (getViewLayoutId() != INVALID_LAYOUT_ID) {
            rootView = inflater.inflate(getViewLayoutId(), container, false)
        } else {
            rootView = super.onCreateView(inflater, container, savedInstanceState)
        }

        return rootView
    }


    override fun onStart() {
        super.onStart()
        //STYLE_NO_FRAME设置之后会调至无法自动点击外部自动消失，因此添加手动控制
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.window?.applyGravityStyle(mGravityEnum, animationStyleresId)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //取消系统对dialog样式上的干扰，防止dialog宽度无法全屏
        setStyle(androidx.fragment.app.DialogFragment.STYLE_NO_FRAME, R.style.dialogFullScreen)
    }

    override fun onResume() {
        super.onResume()
        dialog?.setCanceledOnTouchOutside(mCancelable)
        if(!mCancelable){
            dialog?.setOnKeyListener { v, keyCode, event -> keyCode == KeyEvent.KEYCODE_BACK }
        }
    }

    abstract fun getViewLayoutId(): Int


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        init()
    }

    abstract fun init()


    open class BaseDialogListener {
        /**
         * 点击确定，并携带指定类型参数
         */
        open fun onDialogPositiveClick(dialog: androidx.fragment.app.DialogFragment, any: Any = Any()) {}

        open fun onDialogNegativeClick(dialog: androidx.fragment.app.DialogFragment, any: Any = Any()) {}
        open fun onDismiss(dialog: androidx.fragment.app.DialogFragment, any: Any = Any()) {}
    }



}