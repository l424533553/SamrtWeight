package com.axecom.smartweight.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.android.volley.VolleyError
import com.axecom.smartweight.R
import com.axecom.smartweight.base.SysApplication
import com.axecom.smartweight.config.IConstants
import com.axecom.smartweight.helper.HttpHelper
import com.axecom.smartweight.adapter.ImageTestAdapter
import com.axecom.smartweight.entity.secondpresent.*
import com.xuanyuan.library.utils.MyDateUtils
import com.xuanyuan.library.utils.FileUtils
import com.xuanyuan.library.MyPreferenceUtils
import com.xuanyuan.library.listener.VolleyStringListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*

/**
 *
 *作者：罗发新
 *时间：2018/12/29 0029    11:21
 *邮件：424533553@qq.com
 *说明：进行广告图片的测试
 */
class AdImageFragment : Fragment(), IConstants, VolleyStringListener {
    override fun onStringResponse(response: String?, flag: Int) {

        val netImageInfo: AdUserInfo = JSON.parseObject(response, AdUserInfo::class.java)
        if (netImageInfo.status == 0) {
            val adUserBean: AdUserBean = netImageInfo.data
            val screenImageState = MyPreferenceUtils.getSp(context).getString(IConstants.IMAGE_STATE, "default")!!
            if (screenImageState != adUserBean.status) {
                tvWebData?.text = adUserBean.toString()
            }

            if (response != null) {
                ImageDownThread(response).start()
            }
        }
    }


    //为了下载图片资源，开辟一个新的子线程
    private inner class ImageDownThread internal constructor(private val response: String) : Thread() {

        override fun run() {

            val netImageInfo = JSON.parseObject(response, AdUserInfo::class.java)
            if (netImageInfo != null) {
                if (netImageInfo.status == 0) {
                    val adUserBean = netImageInfo.data
                    if (adUserBean != null) {
                        val screenImageState = MyPreferenceUtils.getSp(context).getString(IConstants.IMAGE_STATE, "default")!!
                        if (screenImageState != adUserBean.status) {
                            downImage(adUserBean)
                            MyPreferenceUtils.getSp(context).edit().putString(IConstants.IMAGE_STATE, screenImageState).apply()
                        }
                    }
                }
            }
        }

        private fun downImage(adUserBean: AdUserBean) {
            val dir = FileUtils.getDownloadDir(context, FileUtils.DOWNLOAD_DIR)
            //下载图片的路径
            var fileOutputStream: FileOutputStream? = null
            var inputStream: InputStream? = null
            try {
                val baseUrl = adUserBean.baseurl
                val imageInfos = ArrayList<AdImageInfo>()
                val prefix = MyDateUtils.getSampleNo()


                val ads = adUserBean.ad
                if (ads != null) {
                    val adArray = ads.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    if (adArray.isNotEmpty()) {
                        for (i in adArray.indices) {
                            val netUrl = baseUrl + adArray[i]
                            val url = URL(netUrl)
                            //再一次打开
                            inputStream = url.openStream()
                            val localPath = dir + prefix + "ad" + i + ".png"
                            val file = File(localPath)
                            fileOutputStream = FileOutputStream(file)
                            var hasRead: Int

//                            while ((hasRead = inputStream?.read()) != -1) {
//                                fileOutputStream.write(hasRead)
//                            }
                            hasRead = inputStream.read()
                            while (hasRead != -1) {
                                fileOutputStream.write(hasRead)
                                hasRead = inputStream.read()
                            }

                            val adImageInfo = AdImageInfo()
                            adImageInfo.netPath = netUrl
                            adImageInfo.localPath = localPath
                            adImageInfo.type = 1
                            imageInfos.add(adImageInfo)
                        }
                    }
                }

                val photo = adUserBean.photo
                if (photo != null) {
                    val netUrl = baseUrl + photo
                    val url = URL(netUrl)
                    //再一次打开
                    inputStream = url.openStream()
                    val localPath = dir + prefix + "photo.png"
                    val file = File(localPath)
                    fileOutputStream = FileOutputStream(file)
                    var hasRead: Int
                    hasRead = inputStream.read()

                    while (hasRead != -1) {
                        fileOutputStream.write(hasRead)
                        hasRead = inputStream.read()
                    }

//                    while ((hasRead = inputStream.read()) != -1) {
//                        fileOutputStream.write(hasRead)
//                    }

                    val adImageInfo = AdImageInfo()
                    adImageInfo.netPath = netUrl
                    adImageInfo.localPath = localPath
                    adImageInfo.type = 0
                    imageInfos.add(adImageInfo)
                }

                val imageDao = ImageDao(context)
                val adUserDao = AdUserDao(context)
                imageDao.deleteAll()
                imageDao.inserts(imageInfos)

                adUserDao.deleteAll()
                adUserBean.id = 1
                adUserDao.insert(adUserBean)

            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    fileOutputStream?.close()
                    inputStream?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onStringResponse(volleyError: VolleyError?, flag: Int) {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_adimage, container, false)
        initView(view)
        initData()
        return view
    }

    private var tvData: TextView? = null
    private var tvphotoData: TextView? = null
    private var tvWebData: TextView? = null
    private  var tvAdvertiseData: TextView? = null
    private var listView: ListView? = null

    private var imageTestAdapter: ImageTestAdapter? = null

    private var imageDao: ImageDao? = null
    private var adUserDao: AdUserDao? = null

    fun initView(view: View) {
        tvData = view.findViewById(R.id.tvData)
        tvphotoData = view.findViewById(R.id.tvphotoData)
        tvWebData = view.findViewById(R.id.tvWebData)
        tvAdvertiseData = view.findViewById(R.id.tvAdvertiseData)
        listView = view.findViewById(R.id.listView)

        imageTestAdapter = ImageTestAdapter(context)
        this.listView?.adapter = imageTestAdapter

        view.findViewById<Button>(R.id.btnTest).setOnClickListener {
            val sysApplication: SysApplication = activity?.application as SysApplication

            sysApplication.userInfo ?: return@setOnClickListener
            val shellerid = sysApplication.userInfo.sellerid
            if (shellerid == 0) {
                return@setOnClickListener
            }
            if (shellerid > 0) {
                HttpHelper.getmInstants(sysApplication).httpQuestImageEx(this, shellerid, 11)
            }
        }
    }

    private fun initData() {
        imageDao = ImageDao(context)
        adUserDao = AdUserDao(context)
        val adUserBean: AdUserBean? = adUserDao?.queryById(1)
        tvData?.text = adUserBean.toString()

        //图片 头像文件
        val photos: List<AdImageInfo> = imageDao?.queryPhoto() as List<AdImageInfo>
        if (photos.isNotEmpty()) {
            tvphotoData?.text = photos[0].toString()
        }


        val photos1: List<AdImageInfo> = imageDao?.queryAll() as List<AdImageInfo>
        var adString = ""
        for (item in photos1) {
            adString += "  $item"
        }
        tvAdvertiseData?.text = adString

        val photos2: List<AdImageInfo> = imageDao?.queryAll2() as List<AdImageInfo>
        imageTestAdapter?.setData(photos2)


    }


}