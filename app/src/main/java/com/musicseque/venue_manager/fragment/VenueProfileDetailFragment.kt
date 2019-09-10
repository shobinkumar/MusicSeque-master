package com.musicseque.venue_manager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.musicseque.Fonts.BoldNoyhr
import com.musicseque.Fonts.Noyhr
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.KotlinBaseFragment
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_venue_profile_detail.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class VenueProfileDetailFragment : KotlinBaseFragment(), View.OnClickListener, MyInterface {
    private var imgRight: ImageView? = null
    private var tvHeading: BoldNoyhr? = null
    lateinit var v: View
    var isProfilePIc = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_venue_profile_detail, container, false)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initOtherViews()
        initViews()
        listeners()
        hitAPI("profile")
    }


    private fun initOtherViews() {
        imgRight = activity?.findViewById<ImageView>(R.id.img_right_icon)
        tvHeading = activity?.findViewById<BoldNoyhr>(R.id.tvHeading)

        imgRight?.setVisibility(View.VISIBLE)
        tvHeading?.setText("Profile")
    }

    private fun initViews() {
    }

    private fun listeners() {
        imgRight?.setOnClickListener { startActivity(Intent(activity, MainActivity::class.java).putExtra("profileTemp", true).putExtra("frag", "com.musicseque.venue_manager.fragment.CreateVenueFragment")) }
        ivCameraBackground.setOnClickListener(this)
        ivCameraProfilePic.setOnClickListener(this)
        ivIconDrop.setOnClickListener(this)
        ivIconUp.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.ivCameraBackground -> {
                isProfilePIc = false
                checkPermissions("image", "com.musicseque.venue_manager.fragment.VenueProfileDetailFragment", this)

            }
            R.id.ivCameraProfilePic -> {
                isProfilePIc = true
                checkPermissions("image", "com.musicseque.venue_manager.fragment.VenueProfileDetailFragment", this)

            }
            R.id.ivIconDrop -> {
                tvBio.visibility = View.VISIBLE
                ivIconDrop.visibility = View.GONE
                ivIconUp.visibility = View.VISIBLE
            }
            //hide

            R.id.ivIconUp -> {
                tvBio.visibility = View.GONE
                ivIconDrop.visibility = View.VISIBLE
                ivIconUp.visibility = View.GONE
            }
        }
    }


    fun hitAPI(type: String) {

        if (KotlinUtils.isNetConnected(activity!!.applicationContext)) {
            Utils.initializeAndShow(requireContext())
            if (type.equals("profile")) {
                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("UserId", getSharedPref().getString(Constants.USER_ID, ""))
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_USER_PROFILE, this)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }

    }


    public fun getImage(file: File) {
        val mFile = RequestBody.create(MediaType.parse("image/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
        val mUSerId = RequestBody.create(MediaType.parse("text/plain"), sharedPref.getString(Constants.USER_ID, "")!!)

        if (isProfilePIc) {

            ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_PROFILE_IMAGE, this);
            Glide.with(this).load(file).into(ivProfilePic)

        } else {
            Glide.with(this).load(file).into(ivBackground)

            ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_COVER_PIC, this);
        }
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        val jsonObj = JSONObject(response.toString())
        if (jsonObj.getString("Status").equals("Success", true)) {
            when (TYPE) {


                Constants.FOR_USER_PROFILE -> {
                    try {
                        tvUserNameDetail.setText(jsonObj.getString("FirstName") + " " + jsonObj.getString("LastName"))
                        tvUserID.text = sharedPref.getString(Constants.UNIQUE_CODE, "")
                        tvUserLocation.setText(jsonObj.getString("City") + ", " + jsonObj.getString("CountryName"))
                        tvReviews.text = "(" + jsonObj.getString("Reviews") + " reviews" + ")"
                        tvCapacity.text = jsonObj.getString("VenueCapacity")
                        tvFollowersCount.text = jsonObj.getString("Followers")
                        tvBio.text = jsonObj.getString("Bio")

                        if (jsonObj.getString("ProfilePic").equals("", true)) {

                        } else {
                            Glide.with(this).load(jsonObj.getString("ImgUrl") + jsonObj.getString("ProfilePic")).into(ivProfilePic)

                        }

                        if (jsonObj.getString("BackgroundImage").equals("", true)) {
                            progressBar.visibility = View.GONE

                        } else {
                            Glide.with(this).load(jsonObj.getString("BackgroundImageUrl") + jsonObj.getString("BackgroundImage")).into(ivBackground)
                            progressBar.visibility = View.GONE

                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }


                }
                Constants.FOR_UPLOAD_PROFILE_IMAGE -> {

                }
                Constants.FOR_UPLOAD_COVER_PIC -> {

                }
            }
        }


    }
}