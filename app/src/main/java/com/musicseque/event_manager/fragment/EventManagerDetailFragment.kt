package com.musicseque.event_manager.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.musicseque.R
import com.musicseque.artist.fragments.BaseFragment
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.USER_ID
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_event_manager_detail.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class EventManagerDetailFragment : BaseFragment(), MyInterface, View.OnClickListener {


    var imgFrom: Int = 0
    var FOR_BACKGROUND = 1
    var FOR_PROFILE = 2
    lateinit var v: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_event_manager_detail, container)
        initOtherViews()
        initViews()
        clickListeners()
        hitAPI("detail")
        return v;
    }


    private fun initOtherViews() {

    }

    private fun initViews() {

    }

    private fun clickListeners() {
        ivCameraBackground.setOnClickListener(this)
        ivCameraProfilePic.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        if (v?.id == R.id.ivCameraBackground) {
            imgFrom = FOR_BACKGROUND
            openDialog("image", this)
        } else if (v?.id == R.id.ivCameraProfilePic) {
            imgFrom = FOR_PROFILE
            openDialog("image", this)
        }
    }


    private fun hitAPI(value:String) {
        if (Utils.isNetworkConnected(activity!!)) {
            initializeLoader()
            if(value.equals("detail",true))
            {
                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("UserId", sharedPref.getString(Constants.USER_ID, ""))
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_EVENT_MANAGER_DETAIL, this)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }



        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }
    }

    private fun initializeLoader() {
        Utils.initializeAndShow(activity)
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_EVENT_MANAGER_DETAIL -> {

                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", ignoreCase = true)) {
                    tvUserNameDetail.setText(obj.getString("FirstName") + " " + obj.getString("LastName"))
                    tvUserType.text = obj.getString("Expertise")
                    tvUserLocation.text = obj.getString("City") + ", " + obj.getString("CountryName")
                    tvFollowersCount.text = obj.getString("Followers")
                    tvUserID.text = sharedPref.getString(Constants.UNIQUE_CODE, "")
                    tvReviews.text = "(" + obj.getString("Reviews") + " reviews" + ")"
                    tvExperience.text = obj.getString("ExperienceYear")
                    tvBio.text = obj.getString("Bio")
                    // tvAboutUser.setText("About " + obj.getString("FirstName") + " " + obj.getString("LastName"));
                    var mProfilePic = ""

                    if (obj.getString("SocialId").equals("", ignoreCase = true)) {
                        mProfilePic = obj.getString("ProfilePic")
                        if (mProfilePic.equals("", ignoreCase = true)) {

                            ivProfilePic.setImageDrawable(resources.getDrawable(R.drawable.icon_photo_upload_circle))
                            pBar.visibility = View.GONE

                        } else {
                            mProfilePic = obj.getString("ImgUrl") + obj.getString("ProfilePic")
                            Glide.with(activity!!)
                                    .load(mProfilePic)
                                    .listener(object : RequestListener<Drawable> {
                                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                            pBar.visibility = View.GONE
                                            return false
                                        }

                                        override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                            pBar.visibility = View.GONE
                                            return false
                                        }
                                    })
                                    .into(ivProfilePic)
                        }
                    } else {
                        mProfilePic = obj.getString("SocialImageUrl")
                        Glide.with(activity!!)
                                .load(mProfilePic)
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                        pBar.visibility = View.GONE
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        pBar.visibility = View.GONE
                                        return false
                                    }
                                })
                                .into(ivProfilePic)

                    }


                    if (sharedPref.getString(Constants.LOGIN_TYPE, "Simple")!!.equals("Simple", ignoreCase = true)) {
                        ivCameraProfilePic.visibility = View.VISIBLE
                    } else {
                        ivCameraProfilePic.visibility = View.GONE

                    }
                }


            }
            Constants.FOR_UPLOAD_BAND_PROFILE_IMAGE -> {
                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                        Utils.showToast(activity, "Profile Pic uploaded successfully")
                        //Glide.with(getActivity()).load(jsonObject.getString("imageurl") + jsonObject.getString("ImageName")).into(ivProfilePic);


                        pBar.visibility = View.VISIBLE

                        Glide.with(this)
                                .load(jsonObject.getString("BandImgUrl") + jsonObject.getString("BandImage"))
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                        pBar.visibility = View.GONE
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        pBar.visibility = View.GONE
                                        return false
                                    }
                                })
                                .into(ivProfilePic)

                    } else {

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            Constants.FOR_UPLOAD_BAND_BACKGROUND_IMAGE -> {
                try {
                    val jsonObject1 = JSONObject(response.toString())
                    if (jsonObject1.getString("Status").equals("Success", ignoreCase = true)) {
                        progressBar.visibility = View.VISIBLE
                        Utils.showToast(activity, "Cover Pic uploaded successfully")

                        Glide.with(this)
                                .load(jsonObject1.getString("BandImgUrl") + jsonObject1.getString("BandImage"))
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                        progressBar.visibility = View.GONE
                                        //   ivBackground.setVisibility(View.VISIBLE);
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        progressBar.visibility = View.GONE
                                        // ivBackground.setVisibility(View.VISIBLE);
                                        return false
                                    }
                                })
                                .into(ivBackground)

                    } else {
                        progressBar.visibility = View.GONE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        }

    }


    public fun getImageFile(file: File) {
        val mFile = RequestBody.create(MediaType.parse("image/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
        val mUSerId = RequestBody.create(MediaType.parse("text/plain"), sharedPref.getString(USER_ID, ""))
        if (Utils.isNetworkConnected(activity!!)) {
            initializeLoader()

            if (imgFrom == FOR_BACKGROUND)
                ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_BAND_BACKGROUND_IMAGE, this)
            else
                ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_BAND_PROFILE_IMAGE, this)
        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }


    }

}