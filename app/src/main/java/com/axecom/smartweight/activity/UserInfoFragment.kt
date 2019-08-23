package com.axecom.smartweight.activity

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.alibaba.fastjson.JSON
import com.android.volley.VolleyError
import com.axecom.smartweight.R
import com.axecom.smartweight.base.SysApplication
import com.axecom.smartweight.entity.dao.UserInfoDao
import com.axecom.smartweight.entity.netresult.ResultInfo
import com.axecom.smartweight.entity.project.UserInfo
import com.axecom.smartweight.helper.HttpHelper
import com.xuanyuan.library.MyToast
import com.xuanyuan.library.listener.VolleyListener
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [UserInfoFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [UserInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class UserInfoFragment : Fragment() ,VolleyListener{
    override fun onResponse(volleyError: VolleyError?, flag: Int) {
        tvWebData?.text = "未获取到信息"
    }

    override fun onResponse(jsonObject: JSONObject?, flag: Int) {
        val resultInfo = JSON.parseObject(jsonObject.toString(), ResultInfo::class.java)
        if (resultInfo != null) {
            if (resultInfo.status == 0) {
                val userInfo = JSON.parseObject(resultInfo.data, UserInfo::class.java)
                if (userInfo != null) {
                    userInfo.id = 1
                    tvWebData?.text = userInfo.toString()
                }
            } else {
                MyToast.toastLong(context, "未获取到秤的配置信息")
            }
        } else {
            MyToast.toastLong(context, "未获取到秤的配置信息")
        }
    }

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_user_info, container, false)
        initView(view)
        initData()
        return view
    }


    private var tvData: TextView? = null
    private var tvWebData: TextView? = null
    private var userInfoDao: UserInfoDao? = null

    fun initView(view: View) {
        tvData = view.findViewById(R.id.tvData)
        tvWebData = view.findViewById(R.id.tvWebData)
        view.findViewById<Button>(R.id.btnTest).setOnClickListener {
            //TODO 用户信息请求
            val sysApplication: SysApplication = activity?.application as SysApplication
            HttpHelper.getmInstants(sysApplication).getUserInfoEx( this, 1)

        }
    }

    private fun initData() {
        userInfoDao = UserInfoDao()
        val userInfo: UserInfo?= userInfoDao?.queryById(1)
        tvData?.text = userInfo.toString()

    }

//    fun onButtonPressed(uri: Uri) {
//        listener?.onFragmentInteraction(uri)
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                UserInfoFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

}
