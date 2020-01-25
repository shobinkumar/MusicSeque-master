package com.musicseque.firebase_notification

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import butterknife.ButterKnife
import butterknife.OnClick
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.adapters.NotificationAdapter
import com.musicseque.interfaces.CommonInterface
import com.musicseque.interfaces.MyInterface
import com.musicseque.models.NotificationModel
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class NotificationActivity : BaseActivity(), MyInterface, CommonInterface {

    var arrayList: ArrayList<NotificationModel> = ArrayList<NotificationModel>()
    var notificationAdapter: NotificationAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        initOtherViews()
        initViews()
        hitAPI()
    }

    private fun initOtherViews() {
        ButterKnife.bind(this)
        tv_title!!.text = "Notifications"
        img_first_icon!!.visibility = View.VISIBLE
        img_right_icon!!.visibility = View.GONE
    }

    private fun initViews() {
        recyclerNotification!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun hitAPI() {
        if (Utils.isNetworkConnected(this)) {
            showDialog()
            try {
                val jsonObject = JSONObject()
                jsonObject.put("LoggedInUserId", SharedPref.getString(Constants.USER_ID, ""))
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_NOTIFICATION, this@NotificationActivity)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }
    }

    fun showDialog() {
        Utils.initializeAndShow(this@NotificationActivity)
    }

    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        if (TYPE == Constants.FOR_NOTIFICATION) {
            try {
                arrayList.clear()
                val `object` = JSONObject(response.toString())
                if (`object`.getString("Status").equals("Success", ignoreCase = true)) {
                    recyclerNotification!!.visibility = View.VISIBLE
                    tvNoNotification!!.visibility = View.GONE
                    val jsonArray = `object`.getJSONArray("result")
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val notificationModel = NotificationModel()
                        try {
                            notificationModel.loggedInUserId = jsonObject.getInt("LoggedInUserId")
                            notificationModel.artist_id = jsonObject.getInt("ArtistId")
                            notificationModel.event_id = jsonObject.getInt("EventId")
                            notificationModel.venue_band_id = jsonObject.getInt("Venue_BandId")
                            notificationModel.artistFullName = jsonObject.getString("ArtistFullName")
                            notificationModel.venue_band_name = jsonObject.getString("Venue_BandName")
                            notificationModel.event_title = jsonObject.getString("EventTitle")
                            notificationModel.sender = jsonObject.getInt("Sender")
                            notificationModel.isRequestStatus = jsonObject.getInt("IsRequestStatus")
                            notificationModel.created_date = jsonObject.getString("CreatedDate")
                            arrayList.add(notificationModel)
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                    //                arrayList = new Gson().fromJson(jsonArray.toString(), new TypeToken<ArrayList<NotificationModel>>() {
//                }.getType());
                    notificationAdapter = NotificationAdapter(this@NotificationActivity, arrayList, this@NotificationActivity)
                    recyclerNotification!!.adapter = notificationAdapter
                } else {
                    recyclerNotification!!.visibility = View.GONE
                    tvNoNotification!!.visibility = View.VISIBLE
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else if (TYPE == Constants.FOR_ACCEPT_BAND_REQUEST) {
            try {
                val `object` = JSONObject(response.toString())
                if (`object`.getString("Status").equals("Success", ignoreCase = true)) {
                    Utils.showToast(this@NotificationActivity, `object`.getString("MesOutput"))
                    hitAPI()
                } else {
                    Utils.showToast(this@NotificationActivity, `object`.getString("MesOutput"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else if (TYPE == Constants.FOR_REJECT_BAND_REQUEST) {
            try {
                val `object` = JSONObject(response.toString())
                if (`object`.getString("Status").equals("Success", ignoreCase = true)) {
                    Utils.showToast(this@NotificationActivity, `object`.getString("MesOutput"))
                    hitAPI()
                } else {
                    Utils.showToast(this@NotificationActivity, `object`.getString("MesOutput"))
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    override fun commonMethod(obj: Any, status: String) {
        val model = obj as NotificationModel
        if (status.equals("accept", ignoreCase = true)) {
            val `object` = JSONObject()
            try {
                `object`.put("LoggedInArtistId", SharedPref.getString(Constants.USER_ID, ""))
                `object`.put("BandId", model.venue_band_id)
                `object`.put("BandManagerId", model.artist_id)
                `object`.put("RequestStatus", "Y")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            RetrofitAPI.callAPI(`object`.toString(), Constants.FOR_ACCEPT_BAND_REQUEST, this@NotificationActivity)
        } else if (status.equals("reject", ignoreCase = true)) {
            val `object` = JSONObject()
            try {
                `object`.put("LoggedInArtistId", SharedPref.getString(Constants.USER_ID, ""))
                `object`.put("BandId", model.venue_band_id)
                `object`.put("BandManagerId", model.artist_id)
                `object`.put("RequestStatus", "N")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            RetrofitAPI.callAPI(`object`.toString(), Constants.FOR_REJECT_BAND_REQUEST, this@NotificationActivity)
        }
    }

    @OnClick(R.id.img_first_icon)
    fun onClick(view: View) {
        when (view.id) {
            R.id.img_first_icon -> finish()
        }
    }
}