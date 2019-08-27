package com.musicseque.event_manager.activity

import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.artist.artist_models.ArtistModel
import com.musicseque.event_manager.adapter.SearchArtistAdapterEventManager
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Utils
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import kotlinx.android.synthetic.main.activity_search_artist.etSearch
import kotlinx.android.synthetic.main.activity_search_artist.recyclerArtist
import kotlinx.android.synthetic.main.activity_search_artist.tvNoRecord
import kotlinx.android.synthetic.main.toolbar_top.*

class SearchArtistActivityEventManager : BaseActivity(), MyInterface {
    lateinit private var arrayList: ArrayList<ArtistModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_artist)
        ButterKnife.bind(this)
        initOtherViews()
        initViews()
        listeners()
        hitAPI("get_list", "")
    }


    private fun initOtherViews() {
        tv_title.setText("Artist")
        img_first_icon.setVisibility(View.VISIBLE)
        img_right_icon.setVisibility(View.GONE)
    }

    private fun initViews() {
        recyclerArtist.layoutManager = LinearLayoutManager(this)


    }

    private fun listeners() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable: Editable) {

                Handler().postDelayed({
                    if (Utils.isNetworkConnected(this@SearchArtistActivityEventManager)) {
                        val jsonObject = JSONObject()
                        try {
                            jsonObject.put("SearchTypeValue", editable.toString())
                            jsonObject.put("UserId", sharedPreferences.getString(Constants.USER_ID, ""))

                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }

                        hitAPI("search", jsonObject.toString())

                    } else {
                        Utils.showToast(this@SearchArtistActivityEventManager, R.string.err_no_internet.toString())
                    }
                }, 1000)


            }
        })
    }

    private fun hitAPI(type: String, args: String) {
        if (Utils.isNetworkConnected(this)) {
            Utils.initializeAndShow(this@SearchArtistActivityEventManager)
            if (type.equals("get_list")) {
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("UserId", sharedPreferences.getString(Constants.USER_ID, ""))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

                RetrofitAPI.callAPI(jsonObject.toString(), Constants.GET_ARTIST_LIST, this@SearchArtistActivityEventManager)
            } else if (type.equals("search")) {
                RetrofitAPI.callAPI(args.toString(), Constants.SEARCH_ARTIST, this@SearchArtistActivityEventManager)

            }

        } else {
            Utils.showToast(this, R.string.err_no_internet.toString())
        }


    }

    override fun sendResponse(response: Any, TYPE: Int) {

        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.GET_ARTIST_LIST ->
                //                searchArtistAdapter = new SearchArtistAdapter(SearchArtistActivityEventManager.this, arrayList,sharedPreferences.getString(Constants.USER_ID,""));
                //                recyclerArtist.setAdapter(searchArtistAdapter);


                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                        val jsonArray = jsonObject.getJSONArray("result")

                        if (jsonArray.length() > 0) {
                            arrayList = Gson().fromJson<ArrayList<ArtistModel>>(jsonArray.toString(), object : TypeToken<ArrayList<ArtistModel>>() {

                            }.type)

                            recyclerArtist.adapter = SearchArtistAdapterEventManager(this, arrayList, sharedPreferences.getString(Constants.USER_ID, ""))
                        }

                    } else {

                    }

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            Constants.SEARCH_ARTIST -> try {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                    tvNoRecord.setVisibility(View.GONE)
                    recyclerArtist.setVisibility(View.VISIBLE)

                    arrayList.clear()
                    val jsonArray = jsonObject.getJSONArray("result")
                    if (jsonArray.length() > 0) {
                        arrayList = Gson().fromJson<ArrayList<ArtistModel>>(jsonArray.toString(), object : TypeToken<ArrayList<ArtistModel>>() {

                        }.type)
                        recyclerArtist.adapter = SearchArtistAdapterEventManager(this, arrayList, sharedPreferences.getString(Constants.USER_ID, ""))

                    }

                } else {
                    recyclerArtist.setVisibility(View.GONE)
                    tvNoRecord.setVisibility(View.VISIBLE)

                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }


    @OnClick(R.id.img_first_icon)
    fun onClick(view: View) {
        when (view.id) {

            R.id.img_first_icon -> finish()
        }
    }
}