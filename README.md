#
 新版base框架　解决旧版　livedata爆炸　vm功能不清晰不完善　业务逻辑不能复用


  implementation "com.pince.maven:lib-vmannotation:version"

  kapt "com.pince.maven:lib-vmbinding:version"

  implementation "com.pince.maven:lib-vm:version"

  implementation "com.pince.maven:lib-vmdialog:version"

  implementation "com.pince.maven:lib-vmbaseframe:version"



![此处输入图片的描述][1]

**业务层vm**
　   业务组件拆分，如登录业务，视频通话呼叫业务，视频通话房间业务，送礼物业务组件
　   相比于mvp中的p presenter需要定义大量interface ,presenter只为单页面服务，业务组件不考虑页面只考虑某个业务，业务逻辑处理需要跟新ui的地方通过livedate回调给ui
　
**ui层**
　　　ui一个页面可以依赖多个业务组件
　　　actvity:使用new 一个object依赖
　　　fragment和dialog:
　　　

    enum class VmType(val type:Int) {
    //以new 一个对象依赖
    FROM_NEW(-1),
    /***
     *  复用使用activity的对象 vm对象　，
     *   　ui发起业务处理　activity fragment都能感知到业务处理结果处理ui ,
     *     activity和fragment共享vm里的数据,
     *     activity和fragment之间通信
     */
    FROM_ACTIVITY(0),
    /**
     * 复用使用父fragment的对象 vm对象　，
     */
    FROM_PARENT(1)








　
**VM 某个业务组件**
  　　*业务组件不以页面为单，以单一业务为单位*
  　　比如登录业务

    class LoginVm(application: Application, bundle: Bundle?): BaseViewModel(application,bundle) {

    val loginLiveData by lazy { MutableLiveData<User>() }

    init {
        checkAutoLogin()
    }


    //检查是不是可以自动登录
    private fun checkAutoLogin(){
        // load sp
        //...
        // get user  from sp
        val user = User(0)
        //show loading
        showLoadingCall?.invoke(true)
        // ....do auto login api and get userInfo
        loginLiveData.value = user
        showLoadingCall?.invoke(false)
    }
    /**
     * 账号密码登录
     */
    fun loginByPwd(){
        //show loading
        showLoadingCall?.invoke(true)
        //
        val user = User(0)
        //从服务器获用户
        // ....do auto login api and get userInfo
        if(false){
            //提示一个弹窗
            showDialog("Login"){
                MyDialogFragment().apply {
                    setDefaultListener(object : BaseDialogFragment.BaseDialogListener() {
                        override fun onDialogNegativeClick(dialog: DialogFragment, any: Any) {
                            super.onDialogNegativeClick(dialog, any)
                        }

                        override fun onDialogPositiveClick(dialog: DialogFragment, any: Any) {
                            super.onDialogPositiveClick(dialog, any)
                        }

                        override fun onDismiss(dialog: DialogFragment, any: Any) {
                            super.onDismiss(dialog, any)
                        }
                    })
                }
            }
        }

        toast("登录成功")
        //保存　sp 用户信息
        // send to ui
        loginLiveData.value = user

        showLoadingCall?.invoke(false)

    }
}



**activity**


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
            // 某个业务处理结果跟新ui
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


　
**fragment**


    class MyFragment : BaseVmFragment() {

    //使用　父fragment  vm对象　　和父fragment通信
    @vm(vmType = VmType.FROM_PARENT)
    lateinit var testVm: TestVm

    //自己的vm
    @vm(vmType = VmType.FROM_NEW)
    lateinit var loginVm: LoginVm


    override fun observeLiveData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initViewData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLayoutId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoading(toShow: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

**dialogFragment**

    class MyDialogFragment : BaseVmDialogFragment() {

    //使用　父fragment  vm对象　　和父fragment通信
    @vm(vmType = VmType.FROM_PARENT)
    lateinit var testVm: TestVm

    //自己的vm
    @vm(vmType = VmType.FROM_NEW)
    lateinit var loginVm: LoginVm


    override fun observeLiveData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initViewData() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getViewLayoutId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


**BaseVm**



    var mData: Bundle? = null
        private set

    /**
     * 获取activity fm
     */
    var getFragmentManagrCall: (() -> FragmentManager)? = null

    /**
     * 回调showloading
     */
    var showLoadingCall: ((show: Boolean) -> Unit)? = null

    /**
     * 接受activity 回调
     */
    var finishedActivityCall: (() -> Unit)? = null

    constructor(application: Application) : super(application)
    constructor(application: Application, data: Bundle?) : super(application) {
        mData = data
    }


    /**
     * 显示弹窗
     */
    fun showDialog(tag: String, call: () -> DialogFragment) {
        getFragmentManagrCall?.invoke()?.let {
            call().show(it, tag)
        }
    }


    override fun onCleared() {
        super.onCleared()
        removeCall()
    }
    /**
     * 页面销毁
     */
    private fun removeCall() {
        finishedActivityCall = null
        getFragmentManagrCall = null
        showLoadingCall = null
    }

    fun getAppContext(): Application {
        return getApplication<Application>()
    }

    fun toast(@StringRes msgRes: Int) {
        Toast.makeText(getAppContext(), getAppContext().resources.getString(msgRes), Toast.LENGTH_SHORT).show()
    }

    fun toast(msg: String?) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(getAppContext(), msg, Toast.LENGTH_SHORT).show()
        }
    }


  [1]: http://git.7guoyouxi.com/android_repo/base-frame-new/raw/latest_branch/img/ic.png