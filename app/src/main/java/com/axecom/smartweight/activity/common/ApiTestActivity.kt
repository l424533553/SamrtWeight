package com.axecom.smartweight.activity.common

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ListView
import com.axecom.smartweight.R
import com.axecom.smartweight.activity.AdImageFragment
import com.axecom.smartweight.activity.UserInfoFragment


/**
 * luofaxin
 * Api 调试测试
 */
class ApiTestActivity : AppCompatActivity(), UserInfoFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
    }

    private lateinit var listView: ListView
    private lateinit var fragment: Fragment
    fun initView() {
        listView = findViewById(R.id.listView)
        findViewById<FrameLayout>(R.id.frameLayout)
        fragment = UserInfoFragment()

    }

    fun initData() {
        val userInfo = "用户信息"
        val otherInfo = "其他信息"
        val apiName = arrayOf(userInfo, otherInfo, "北京", "上海", "香港", "澳门", "天津")  //定义一个数组，作为数据源
        val arrayAdapter: ArrayAdapter<String>    //定义一个数组适配器对象
        //创建数组适配器对象，并且通过参数设置类item项的布局样式和数据源
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, apiName)
        //把数组适配器加载到ListView控件中

        listView.adapter = arrayAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

            var transaction = fragmentManager.beginTransaction()
            transaction.remove(fragment)
            when (apiName[position]) {
                userInfo -> fragment = UserInfoFragment()
                otherInfo -> fragment = AdImageFragment()
            }
            transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment)
            transaction.commit()
        }
    }

    // 支持者
    private val fragmentManager: FragmentManager = supportFragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_test)

        initView()
        initData()
    }


//    private var listView: ListView? = null    //定义ListView用来获取到，布局文件中的ListView控件
//    private val city = arrayOf("广州", "深圳", "北京", "上海", "香港", "澳门", "天津")  //定义一个数组，作为数据源
//    private var arrayAdapter: ArrayAdapter<String>? = null    //定义一个数组适配器对象
//
//     fun onCreate111(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        listView = findViewById<View>(R.id.listView) as ListView //获取布局文件中的ListView控件对象
//
//
//        /*
//         * Context context,  上下文对象
//         * int resource,    items项显示的布局样式，一般是系统的布局文  android.R.layout.** (但是需要选择和ListView相适合的布局文件否则运行报错)
//         * String[] objects  数组对象（数据源）
//         *
//         * */
//
//        //创建数组适配器对象，并且通过参数设置类item项的布局样式和数据源
//        arrayAdapter = ArrayAdapter(this@MyBuglyActivity, android.R.layout.simple_list_item_1, city)
//
//        //把数组适配器加载到ListView控件中
//        listView!!.setAdapter(arrayAdapter)
//
//    }

    /**
     * 获取  设备的唯一标识
     *
     * @param context 上下文
     * @return 唯一标识 mac
     */

    @SuppressLint("HardwareIds")
    fun getIMEI(context: Context): String {
        // 94:a1:a2:a4:70:66
        val wm = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wm.connectionInfo.macAddress
    }


}
