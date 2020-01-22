package com.musicseque.artist.fragments

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.musicseque.Fonts.Noyhr
import com.musicseque.R
import com.musicseque.artist.artist_adapters.ShowPhotosAdapter
import com.musicseque.artist.artist_adapters.UploadPhotosAdapter
import com.musicseque.interfaces.MyInterface
import com.musicseque.models.ImageModel
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_ARTIST_UPLOADED_IMAGES
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_images.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ImagesFragment : BaseFragment(), UploadPhotosAdapter.UploadImage, MyInterface {
    private var  mUserId: String=""
    var v: View? = null

    var arrayList = ArrayList<ImageModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_images, null)
        initOtherViews()
        initViews()
        listeners()
        hitAPI(FOR_ARTIST_UPLOADED_IMAGES)
        return v
    }

    private fun initOtherViews() {}
    private fun initViews() {
        recyclerImages!!.layoutManager = GridLayoutManager(activity, 4)
        mUserId=arguments!!.getString("UserId")


    }

    private fun listeners() {}
    override fun callMethod() {}
    fun hitAPI(TYPE: Int) {
        if (Utils.isNetworkConnected(activity)) {
            Utils.initializeAndShow(activity)
            if (TYPE == FOR_ARTIST_UPLOADED_IMAGES) {
                val jsonObject = JSONObject()
                try {
                    jsonObject.put("UserId", mUserId)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_ARTIST_UPLOADED_IMAGES, this)

            }
        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }


    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()

        if (TYPE == FOR_ARTIST_UPLOADED_IMAGES) {
            val jsonArray = JSONArray(response.toString())
            arrayList.clear()
            if (jsonArray.length() > 0) {
                var i = 0
                while (i < jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    arrayList.add(ImageModel(jsonObject.getString("ImgUrl"), jsonObject.getString("Image"), true, -1, false))

                    i++
                }
                if (arrayList.size > 0) {
                    tvNoImages!!.visibility = View.GONE
                    recyclerImages!!.visibility = View.VISIBLE
                    recyclerImages!!.adapter = ShowPhotosAdapter(activity, arrayList)
                } else {
                    recyclerImages!!.visibility = View.GONE
                    tvNoImages!!.visibility = View.VISIBLE
                }
            } else {

            }
        }


        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}