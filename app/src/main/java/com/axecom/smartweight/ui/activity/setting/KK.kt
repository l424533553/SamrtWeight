package com.axecom.smartweight.ui.activity.setting

import android.os.Handler
import org.apache.poi.hwpf.converter.PicturesManager
import org.apache.poi.hwpf.converter.WordToHtmlConverter
import org.apache.poi.hwpf.usermodel.PictureType
import javax.xml.parsers.DocumentBuilderFactory

class KK {

    private var handler: Handler? = null

    private fun fsfa() {


        val wordToHtmlConverter = WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument())
        //设置图片路径


        wordToHtmlConverter.setPicturesManager(

                object : PicturesManager {
                    public override fun savePicture(content: ByteArray,
                                                    pictureType: PictureType, suggestedName: String,
                                                    widthInches: Float, heightInches: Float): String {
                        //                        String name = docName.substring(0, docName.indexOf("."));
                        val name = ""
                        return name + "/" + suggestedName
                    }
                })
    }

//    private fun initHandler() {
//        handler = Handler(object : Handler.Callback {
//            public override fun handleMessage(message: Message): Boolean {
//                return false
//            }
//        })
//    }
}
