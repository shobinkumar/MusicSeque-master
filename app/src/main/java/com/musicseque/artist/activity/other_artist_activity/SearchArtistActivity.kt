package com.musicseque.artist.activity.other_artist_activity

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.view.View
import butterknife.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.artist.artist_adapters.SearchArtistAdapter
import com.musicseque.artist.artist_models.ArtistModel
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.GET_ARTIST_LIST
import com.musicseque.utilities.Constants.SEARCH_ARTIST
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_search_artist.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SearchArtistActivity : BaseActivity(), MyInterface, View.OnClickListener {

    var layoutManager: RecyclerView.LayoutManager? = null
    var searchArtistAdapter: SearchArtistAdapter? = null

    var arrayList = ArrayList<ArtistModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_artist)
        initOtherViews()
        initViews()
        listeners()
    }

    private fun listeners() {
        img_first_icon!!.setOnClickListener(this)
    }

    private fun initOtherViews() {
        tv_title!!.text = "Artist"
        img_first_icon!!.visibility = View.VISIBLE
        img_right_icon!!.visibility = View.GONE
    }

    private fun initViews() {
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerArtist!!.layoutManager = layoutManager
        hitAPI(GET_ARTIST_LIST,"")
    }

    private fun hitAPI(TYPE: Int, args: String) {
        if (Utils.isNetworkConnected(this)) {
            Utils.initializeAndShow(this@SearchArtistActivity)
            if(TYPE==GET_ARTIST_LIST)
            {
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("ProfileTypeId", "1")
                    jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.GET_ARTIST_LIST, this@SearchArtistActivity)

            }
            else if(TYPE==SEARCH_ARTIST)
            {
                RetrofitAPI.callAPI(args, Constants.SEARCH_ARTIST, this@SearchArtistActivity)

            }
        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }
    }

    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.GET_ARTIST_LIST ->
                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                        val jsonArray = jsonObject.getJSONArray("result")
                        if (jsonArray.length() > 0) {
                            arrayList = Gson().fromJson(jsonArray.toString(), object : TypeToken<ArrayList<ArtistModel?>?>() {}.type)
                            searchArtistAdapter = SearchArtistAdapter(this@SearchArtistActivity, arrayList, SharedPref.getString(Constants.USER_ID, ""))
                            recyclerArtist!!.adapter = searchArtistAdapter
                        }
                    } else {
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            Constants.SEARCH_ARTIST -> try {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                    tvNoRecord!!.visibility = View.GONE
                    recyclerArtist!!.visibility = View.VISIBLE
                    arrayList.clear()
                    val jsonArray = jsonObject.getJSONArray("result")
                    if (jsonArray.length() > 0) {
                        arrayList = Gson().fromJson(jsonArray.toString(), object : TypeToken<ArrayList<ArtistModel?>?>() {}.type)
                        searchArtistAdapter = SearchArtistAdapter(this@SearchArtistActivity, arrayList, SharedPref.getString(Constants.USER_ID, ""))
                        recyclerArtist!!.adapter = searchArtistAdapter
                    }
                } else {
                    recyclerArtist!!.visibility = View.GONE
                    tvNoRecord!!.visibility = View.VISIBLE
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    @OnTextChanged(R.id.etSearch)
    protected fun onTextChanged(text: CharSequence?) {
    }

    @OnTextChanged(value = [R.id.etSearch], callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    fun afterEditTextInput(editable: Editable) {
        Handler().postDelayed({
            if (Utils.isNetworkConnected(this@SearchArtistActivity)) {
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("SearchTypeValue", editable.toString())
                    jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                    jsonObject.put("ProfileTypeId", "1")
                    hitAPI(SEARCH_ARTIST,jsonObject.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Utils.showToast(this@SearchArtistActivity, resources.getString(R.string.err_no_internet))
            }
        }, 1000)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.img_first_icon -> finish()
        }
    }
}