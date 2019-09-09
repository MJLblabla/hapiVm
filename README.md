# hapiVm
hapi基础库，快速开启ViewModel　livedata  业务组件化，业务逻辑复用

![Alt text](https://github.com/MJLblabla/hapiVm/blob/latest_branch/img/ic.png "optional title")
**hapivm**

　
     
      //actvity fragment　基础库
    implementation 'com.github.MJLblabla.hapiVm:base_frame:tag'
     // dialogfragment 基础库
    implementation 'com.github.MJLblabla.hapiVm:happy_dialog:tag'
    
    //如果不使用hapi组件的view(BaseVmActvity,BaseVmFragment,BaseVmDialogFragment)
    implementation 'com.github.MJLblabla.hapiVm:happy_vm:tag'
    //vm 注解 如果需要注解绑定 vm
    kapt 'com.github.MJLblabla.hapiVm:vmBinding:tag'
    
    
    
    
    
　
**VM 某个业务组件**
  　　*业务组件不以页面为单，考虑复用以单一业务为单位*
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
