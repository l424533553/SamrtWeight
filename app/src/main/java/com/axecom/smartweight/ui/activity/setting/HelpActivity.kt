package com.axecom.smartweight.ui.activity.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.webkit.WebSettings
import android.webkit.WebView
import com.axecom.smartweight.R
import com.axecom.smartweight.base.SysApplication
import com.luofx.utils.MyPreferenceUtils
import com.luofx.utils.file.FileUtils
import org.apache.poi.hwpf.HWPFDocument
import org.apache.poi.hwpf.converter.PicturesManager
import org.apache.poi.hwpf.converter.WordToHtmlConverter
import java.io.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class HelpActivity : AppCompatActivity() {
    //    //SD卡在设备的目录为/mnt/sdcard，并不存在/mnt/asec/目录
    //    private var docPath = "/mnt/sdcard/"
    //文件名称
    private var docName = "download.doc"
    //html文件存储位置
    private var savePath: String? = null
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        initHandler()

        val isNotFirst = MyPreferenceUtils.getBoolean(this, "isNotFirst")
        name = docName.substring(0, docName.indexOf("."))
        savePath = FileUtils.getDownloadDir(this, FileUtils.DOWNLOAD_DIR)

        if (!isNotFirst) {//第一次
            val sysApplication = application as SysApplication
            sysApplication.threadPool.execute {
                convert2Html(docName, "$savePath$name.html")
                val myPreferenceUtils = MyPreferenceUtils.getSp(this) as SharedPreferences
                myPreferenceUtils.edit().putBoolean("isNotFirst", true).apply()
            }
        } else {//第二次
            showHelp()
        }
    }

    var handler: Handler? = null
    private fun initHandler() {
        handler = Handler(Handler.Callback {
            showHelp()
            false
        })
    }

    /**
     * 显示内容
     */
    private fun showHelp() {
//        var status = Environment.getExternalStorageState() //获取 外挂存储 状态
        //WebView加载显示本地html文件
        val webView = findViewById<WebView>(R.id.office)
        val webSettings = webView.settings as WebSettings
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webView.loadUrl("file://$savePath$name.html")//必须是//才可以找到file
    }

    /**
     * word文档转成html格式
     */
    private fun convert2Html(fileName: String, outPutFile: String) {
        try {
            val wordDocument = HWPFDocument(resources.assets.open(fileName))
            val wordToHtmlConverter = WordToHtmlConverter(
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument())
            //设置图片路径

            wordToHtmlConverter.picturesManager = PicturesManager { _, _, suggestedName, _, _ ->
                //                        String name = docName.substring(0, docName.indexOf("."));
//                val name = docName.substring(0, docName.indexOf("."));
                "$savePath/$suggestedName"
            }

            //保存图片
            val pics = wordDocument.picturesTable.allPictures
            if (pics != null) {
                for (item in pics) {
                    val file = savePath + item.suggestFullFileName()
                    item.writeImageContent(FileOutputStream(file))
                }
            }

            wordToHtmlConverter.processDocument(wordDocument)
            val htmlDocument = wordToHtmlConverter.document
            val out = ByteArrayOutputStream()
            val domSource = DOMSource(htmlDocument)
            val streamResult = StreamResult(out)

            val tf = TransformerFactory.newInstance()
            val serializer = tf.newTransformer()
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8")
            serializer.setOutputProperty(OutputKeys.INDENT, "yes")
            serializer.setOutputProperty(OutputKeys.METHOD, "html")
            serializer.transform(domSource, streamResult)
            out.close()
            //保存html文件
            writeFile(String(out.toByteArray()), outPutFile)
            handler!!.sendEmptyMessage(4431)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //TODO  文件中内容的替换   很重要
//    private void text()
//    {
//        String s=null;
//        File fileseg=new File("你的目录");
//        File  fileres=new File("你的输出目录");
//        BufferedReader readerseg=new BufferedReader(new FileReader(fileseg));
//        PrintStream ps=new PrintStream(new FileOutputStream(fileres));
//        while((s=readerseg.readLine())!=null){
//            s=s.replaceAll("@","");
//        }
//    }

    /**
     * 将html文件保存到sd卡
     */
    private fun writeFile(content: String, path: String) {
        var fos: FileOutputStream? = null
        var bw: BufferedWriter? = null
        try {
            val file = File(path)
            if (!file.exists()) {
                file.createNewFile()
            }
            if (file.exists()) {

            } else {

            }
            fos = FileOutputStream(file)
            bw = BufferedWriter(OutputStreamWriter(fos, "utf-8"))
            bw.write(content)
        } catch (fnfe: FileNotFoundException) {
            fnfe.printStackTrace()
        } catch (ioe: IOException) {
            ioe.printStackTrace()
        } finally {
            try {
                bw?.close()
                fos?.close()
            } catch (ie: IOException) {
                ie.printStackTrace()
            }
        }
    }

    //数据
}
