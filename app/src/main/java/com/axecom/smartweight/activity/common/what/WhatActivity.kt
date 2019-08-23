package com.axecom.smartweight.activity.common.what

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.widget.Toast
import com.axecom.smartweight.R
import com.axecom.smartweight.activity.common.what.dummy.DummyContent
import com.axecom.smartweight.config.IIntent
import com.axecom.smartweight.config.IIntent.*

class WhatActivity : AppCompatActivity(), IIntent, ItemFragment.OnListFragmentInteractionListener {
    /**
     * 一种 Activity 和  Fragment的通信方式
     */
    override
    fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
        // To change body of created functions use File | Settings | File Templates.
        Toast.makeText(this,item?.content,Toast.LENGTH_SHORT).show()
    }

    private lateinit var fragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_what)
        fragmentManager = supportFragmentManager
        val type= intent.getStringExtra(INTENT_EXTRA_TYPE)
        decideJumpFragment(type)
    }


    /**
     * 其他类型的可以通用
     * 决定到底调到那个Fragment中去 ,
     */
    private fun decideJumpFragment(type: String?) {
        if(type==null){
            return
        }
        var fragment: Fragment? = null
        when (type) {
            //临时变量
            INTENT_UP_DATA_TEMP_ACTIVITY -> fragment = UpDataFragment()
            //其他的 Activity
            INTENT_OTHER -> fragment = ItemFragment()
            else -> {
            }
        }
        if (fragment != null) {
            val transaction = fragmentManager.beginTransaction()
            transaction.add(R.id.fragmentLayout, fragment)
            transaction.commit()
        }
    }

}
