package com.hapi.base_mvvm.activity

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.*
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.hapi.base_mvvm.R
import com.hapi.base_mvvm.uitil.ClickUtil
import com.hapi.base_mvvm.uitil.SoftInputUtil
import kotlinx.android.synthetic.main.frame_base_container.*


/**
 * activity 基础类  封装toolbar
 *
 */
abstract class BaseFrameActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener {


    /**
     * 根布局
     */
    private lateinit var mRootView: View

    var mToolbar: Toolbar? = null
        private set
    private var centerTitle: TextView? = null

    private lateinit var mContainer: FrameLayout
    private var mContentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mInflater = getLayInflater()
        mContentView = createContainerView(mInflater, getLayoutId())
        if (mContentView == null) {
            finish()
            return
        }
        onActivityCreateStart()
        changeTheme()
        setContentView(R.layout.frame_base_container)
        mRootView = findViewById(R.id.frame_base_root)
        val viewStubToolbar = frame_base_container_toolbar
        if (isToolBarEnable()) {
            var toolbarLayoutId = 0
            if (isTitleCenter()) {
                toolbarLayoutId = R.layout.frame_base_toolbar_center
            } else {
                toolbarLayoutId = R.layout.frame_base_toolbar_normal
            }
            viewStubToolbar.layoutResource = toolbarLayoutId
            mToolbar = viewStubToolbar.inflate() as Toolbar

            if (isTitleCenter()) {
                centerTitle = mToolbar?.findViewById(R.id.toolbar_title_tv)
            }
        } else {
            viewStubToolbar.visibility = View.GONE
        }

        setupToolbar()

        mContainer = findViewById(R.id.frame_base_container)

        val params = mContainer.layoutParams as RelativeLayout.LayoutParams

        val mode = requestToolbarMode()
        if (mode == ToolbarMode.Layer && params != null) {
            params.addRule(RelativeLayout.BELOW, R.id.frame_base_toolbar)
            mContainer.layoutParams = params
        }
        // 设置ContentView
        mContainer.addView(
            mContentView,
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        )
        setToolbarTitle(title)
        init()
    }

    abstract fun init()
    abstract fun getLayoutId(): Int
    abstract fun showLoading(toShow: Boolean)


    @ToolbarMode
    protected fun requestToolbarMode(): Int {
        return ToolbarMode.Layer
    }

    /**
     * 是否展示toolbar
     *
     * @return
     */
    open fun isToolBarEnable(): Boolean {
        return true
    }

    /**
     * 设置是否toolbar标题居中
     *
     * @return
     */
    open fun isTitleCenter(): Boolean {
        return false
    }

    /**
     * 是否显示返回键。默认显示
     *
     * @return
     */
    open fun homeAsUpEnable(): Boolean {
        return true
    }

    /**
     * 开始创建activity界面
     */
    open fun onActivityCreateStart() {}

    /**
     * menu layout id
     *
     * @return
     */
    open fun requestMenuId(): Int {
        return 0
    }

    open fun changeTheme() {}


    /**
     * 返回键
     *
     * @return
     */
    @DrawableRes
    open fun requestNavigationIcon(): Int {
        return -1
    }


    /**
     * 设置toolbar 背景样式
     */
    open fun requestToolBarBackground(): Drawable? {
        return null
    }


    private fun getLayInflater(): LayoutInflater {
        return LayoutInflater.from(this)
    }

    /**
     * 创建containerview
     *
     * @param inflater
     * @param layoutResID 你自己的布局id
     * @return
     */
    open fun createContainerView(inflater: LayoutInflater, layoutResID: Int): View? {
        return if (layoutResID <= 0) {
            null
        } else inflater.inflate(layoutResID, null)
    }


    /**
     * replace the inner toolbar, if the new toolbar is same as inner's , it will do nothing
     */
    protected fun setToolbar(toolbar: Toolbar) {
        if (mToolbar !== toolbar) {
            if (mToolbar != null) {
                mToolbar?.visibility = View.GONE
            }
            this.mToolbar = toolbar
            setupToolbar()
        }
    }

    /**
     * setup  toolbar
     */

    private fun setupToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
            //显示返回键
            supportActionBar?.setDisplayHomeAsUpEnabled(homeAsUpEnable())
            //可响应返回键点击事件
            supportActionBar?.setDisplayShowHomeEnabled(true)
            //返回键
            requestNavigationIcon().let {
                if (it > 0) {
                    supportActionBar?.setHomeAsUpIndicator(it)
                }
            }

            //是否展示默认标题
            if (isTitleCenter()) {
                supportActionBar?.setDisplayShowTitleEnabled(false)
            }

            val toolbarBg = requestToolBarBackground()
            mToolbar?.background = toolbarBg
            mToolbar?.setOnTouchListener(toolbarTouch)
            mToolbar?.setOnMenuItemClickListener(this)
            mToolbar?.setNavigationOnClickListener(View.OnClickListener {
                if (ClickUtil.isClickAvalible()) {
                    onBackPressed()
                }
            })
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuId = requestMenuId()
        if (menuId > 0) {
            menuInflater.inflate(menuId, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }


    /**
     * 监听menu的点击事件
     */
    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> {
            }
        }
        return false
    }

    /**
     * 监听toolbar的touch事件，隐藏键盘
     */
    private val toolbarTouch = View.OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            hideSoftInputView()
        }
        false
    }


    private fun setToolbarTitle(title: CharSequence) {
        if (mToolbar != null) {
            if (isTitleCenter()) {
                centerTitle?.text = title
            } else {
                if (mToolbar != null) {
                    mToolbar?.title = title
                }
            }
        }
    }

    /**
     * 隐藏软键盘
     */
    fun hideSoftInputView() {
        SoftInputUtil.hideSoftInputView(this)
    }


}