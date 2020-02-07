package com.musicseque.utilities


import android.content.Context
import com.musicseque.R
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI


class APIHit {

    companion object {
        fun sendPostData(params: String, Type: Int, myInterface: MyInterface, context: Context) {
            if (Utils.isNetworkConnected(context)) {
                Utils.initializeAndShow(context)
                RetrofitAPI.callAPI(params, Type, myInterface)
            } else {
                Utils.showToast(context, context.resources.getString(R.string.err_no_internet))
            }
        }

        fun sendGetData(Type: Int, myInterface: MyInterface, context: Context) {
            if (Utils.isNetworkConnected(context)) {
                Utils.initializeAndShow(context)
                RetrofitAPI.callGetAPI(Type, myInterface)
            } else {
                Utils.showToast(context, context.resources.getString(R.string.err_no_internet))
            }
        }


    }
    }