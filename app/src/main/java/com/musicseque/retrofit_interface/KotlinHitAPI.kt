package com.musicseque.retrofit_interface

import android.util.Log
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.KotlinHitAPI.Companion.myInterfaces
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.musicseque.retrofit_interface.callRetrofitKotlin as callRetrofitKotlin

class KotlinHitAPI {


    companion object {
        lateinit var myInterfaces: MyInterface

        fun callAPI(params: String, TYPE: Int, myInterface: MyInterface) {

            myInterfaces = myInterface
            val apis = KotlinRetrofitClientInstance.createService(KotlinCommonInterface::class.java)
            lateinit var calls: Call<String>
            if (TYPE == FOR_EVENT_DETAIL) {
               calls = apis!!.methodEventDetail(params)
            }
           else if (TYPE == FOR_SAVE_UPDATE_EVENT_DETAIL) {
                calls = apis!!.methodEventSaveUpdateDetail(params)
            }

            else if(TYPE== FOR_SEARCH_VENUE_LIST)
            {
                calls=apis!!.methodForVenueList(params)
            }

//            else if(TYPE== FOR_VENUE_PROFILE)
//            {
//                calls=apis!!.methodForVenueProfile(params)
//            }
            else if(TYPE== FOR_SHOW_EVENTS_LIST)
            {
                calls=apis!!.getEventsListMethod(params)
            }

            else if(TYPE== FOR_CREATE_UPDATE_VENUE_PROFILE)
            {
                calls=apis!!.methodForVenueProfileSaveUpdate(params)
            }

            callRetrofitKotlin(calls, TYPE)

        }

        fun callGetAPI(TYPE: Int, myInterface: MyInterface) {
            myInterfaces = myInterface
            val api = KotlinRetrofitClientInstance.createService(KotlinCommonInterface::class.java)
            lateinit var call: Call<String>
            if (TYPE == Constants.FOR_EVENT_TYPE_LIST) {
                call = api!!.methodEvents()
            } else if (TYPE == Constants.FOR_CURRENCY_LIST) {
                call = api!!.methodCurrency()

            }
            callRetrofitKotlin(call, TYPE)

        }

    }


}

fun <T> callRetrofitKotlin(call: Call<T>, TYPE: Int) {

    call.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (TYPE == Constants.FOR_EVENT_TYPE_LIST) {
                myInterfaces.sendResponse(response.body(), TYPE)
                Log.e("FOR_EVENT_LIST", response.body()!!.toString())


            } else if (TYPE == Constants.FOR_CURRENCY_LIST) {
                Log.e("FOR_CURRENCY_LIST", response.body()!!.toString())
                myInterfaces.sendResponse(response.body(), TYPE)

            } else if (TYPE == FOR_EVENT_DETAIL) {
                Log.e("FOR_EVENT_DETAIL", response.body()!!.toString())
                myInterfaces.sendResponse(response.body(), TYPE)
            }
            else if(TYPE==FOR_SAVE_UPDATE_EVENT_DETAIL)
            {
                Log.e("FOR_SAVE_UPDATE_EVENT", response.body()!!.toString())
                myInterfaces.sendResponse(response.body(), TYPE)
            }
            else if(TYPE== FOR_SEARCH_VENUE_LIST)
            {
                Log.e("FOR_SEARCH_VENUE_LIST", response.body()!!.toString())
                myInterfaces.sendResponse(response.body(), TYPE)
            }
            else if(TYPE== FOR_VENUE_PROFILE)
            {
                Log.e("FOR_VENUE_PROFILE", response.body()!!.toString())
                myInterfaces.sendResponse(response.body(), TYPE)
            }
            else if(TYPE== FOR_SHOW_EVENTS_LIST)
            {
                Log.e("FOR_SHOW_EVENTS", response.body()!!.toString())
                myInterfaces.sendResponse(response.body(), TYPE)
            }
            else if(TYPE==FOR_CREATE_UPDATE_VENUE_PROFILE)
            {
                Log.e("FOR_SAVE_VENUE", response.body()!!.toString())
                myInterfaces.sendResponse(response.body(), TYPE)
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.e("Re", "")

        }
    })

}


