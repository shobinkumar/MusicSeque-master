package com.musicseque.venue_manager.fragment

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.musicseque.Fonts.BoldNoyhr
import com.musicseque.Fonts.Noyhr
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.*
import kotlinx.android.synthetic.main.fragment_create_venue.*
import kotlinx.android.synthetic.main.fragment_venue_profile_detail.*
import kotlinx.android.synthetic.main.fragment_venue_profile_detail.pBar
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
        val tvDone = (activity as MainActivity).findViewById<View>(R.id.tvDone) as TextView
        tvDone.visibility = View.GONE
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
                    jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_USER_PROFILE, this)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }

    }


     fun getImage(file: File) {
        val mFile = RequestBody.create(MediaType.parse("image/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
        val mUSerId = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getString(Constants.USER_ID, "")!!)

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
                        tvUserID.text = SharedPref.getString(Constants.UNIQUE_CODE, "")
                        tvUserLocation.setText(jsonObj.getString("City") + ", " + jsonObj.getString("CountryName"))
                        tvReviews.text = "(" + jsonObj.getString("Reviews") + " reviews" + ")"
                        tvCapacity.text = jsonObj.getString("VenueCapacity")
                        tvFollowersCount.text = jsonObj.getString("Followers")
                        tvBio.text = jsonObj.getString("Bio")




                        if (jsonObj.getString("SocialId").equals("")) {
                            ivCameraProfilePic.visibility = View.VISIBLE
                            if (jsonObj.getString("ProfilePic").equals("")) {
                                Glide.with(this).load(R.drawable.icon_img_dummy).into(ivProfilePic)

                                pBar.visibility = View.GONE
                            } else {
                                Glide.with(this).load(jsonObj.getString("ImgUrl") + jsonObj.getString("ProfilePic")).into(ivProfilePic)
                                pBar.visibility = View.GONE

                            }
                        } else {
                            ivCameraProfilePic.visibility = View.GONE
                            val sUrl = jsonObj.getString("SocialImageUrl")
                            if (sUrl.equals(""))
                                Glide.with(this).load(R.drawable.icon_img_dummy).into(ivProfilePic)
                            else
                                Glide.with(this).load(sUrl).into(ivProfilePic)
                            pBar.visibility = View.GONE
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
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                        SharedPref.putString(Constants.PROFILE_IMAGE, jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))
                        Utils.showToast(activity, "Profile Pic uploaded successfully")
                        //Glide.with(getActivity()).load(jsonObject.getString("imageurl") + jsonObject.getString("ImageName")).into(ivProfilePic);


                        pBar.visibility = View.VISIBLE

//                        Glide.with(this)
//                                .load(jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))
//                                .listener(object : RequestListener<Drawable> {
//                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
//                                        pBar.visibility = View.GONE
//                                        return false
//                                    }
//
//                                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
//                                        pBar.visibility = View.GONE
//                                        return false
//                                    }
//                                })
//                                .into(ivProfilePic)

                        SharedPref.putString(Constants.PROFILE_IMAGE, jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))

                    } else {

                    }
                }
                Constants.FOR_UPLOAD_COVER_PIC -> {

                }
            }
        }


    }
}