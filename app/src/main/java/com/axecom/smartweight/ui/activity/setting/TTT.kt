package com.axecom.smartweight.ui.activity.setting

import android.app.Activity

import com.axecom.smartweight.base.SysApplication

class TTT : Activity() {

    private fun ffa() {
        val sysApplication = application as SysApplication
        sysApplication.threadPool.execute { }
    }


}
