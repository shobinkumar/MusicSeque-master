package com.musicseque.test

import android.app.Activity
import android.os.Bundle
import com.musicseque.R
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.KotlinHitAPI
import com.musicseque.utilities.Constants
import org.json.JSONObject

class TestActivity: Activity(), MyInterface {
    override fun sendResponse(response: Any?, TYPE: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_layout)
        hitAPI()
    }

    private fun hitAPI() {

       val json=JSONObject()
        json.put("fvUserName","2")
        json.put("fvPassword","123")
        KotlinHitAPI.callAPI(json.toString(), Constants.FOR_TEST,this)

    }
}