package com.xuanyuan.library.apk_update

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import com.xuanyuan.library.MyToast
import com.xuanyuan.library.R
import com.xuanyuan.library.apk_update.download.DownloadIntentService

/**
 * APK 更新测试
 *
 */
@Suppress("DEPRECATION")
class ApkUpdateActivity : AppCompatActivity() {
    private val downLoadApkId = 12
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apk_update)

        findViewById<TextView>(R.id.tv_download).setOnClickListener {
            //            MyToast.toastShort(this, "正在点击")
            if (isServiceRunning(DownloadIntentService::class.java.name)) {// 服务已经打开了
                MyToast.toastShort(this, "正在下载")
                Toast.makeText(this, "正在下载", Toast.LENGTH_SHORT).show()
            } else {
                //String downloadUrl = http://sqdd.myapp.com/myapp/qqteam/tim/down/tim.apk;
//                var downloadUrl = "/qqmi/aphone_p2p/TencentVideo_V6.0.0.14297_848.apk"
                val downloadUrl = "/wapdl/android/apk/SogouInput_android_v8.25_sweb.apk?frm=new_pcjs_index/SogouInput_android_v8.25_sweb.apk"
                val intent = Intent(this, DownloadIntentService::class.java)
                intent.putExtra("download_url", downloadUrl)
                intent.putExtra("download_id", downLoadApkId)
                intent.putExtra("download_file", downloadUrl.substring(downloadUrl.lastIndexOf('/') + 1))
                startService(intent)
            }
        }
    }

    /**
     * 用来判断服务是否运行,这一步很重要
     *
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    private fun isServiceRunning(className: String): Boolean {
        var isRunning = false
        val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val serviceList = activityManager.getRunningServices(Int.MAX_VALUE)
        if (serviceList.size <= 0)// 无服务则肯定返回0
            return false

        for (item in serviceList) {
            if (item.service.className == className) {
                isRunning = true
                break
            }
        }
        return isRunning
    }
}
