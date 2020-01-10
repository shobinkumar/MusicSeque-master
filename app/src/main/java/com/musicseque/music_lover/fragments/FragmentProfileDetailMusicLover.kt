package com.musicseque.music_lover.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.musicseque.Fonts.BoldNoyhr
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_USER_PROFILE
import com.musicseque.utilities.KotlinBaseFragment
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_profile_detail_music_lover.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class FragmentProfileDetailMusicLover : KotlinBaseFragment(), MyInterface, View.OnClickListener {


    var imgFrom: Int = 0
    var FOR_BACKGROUND = 1
    var FOR_PROFILE = 2
    lateinit var v: View
   lateinit var img_right_icon: ImageView
   lateinit var tv_title: BoldNoyhr
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_profile_detail_music_lover, null)

        return v;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initOtherViews()
        initViews()
        clickListeners()
        hitAPI(FOR_USER_PROFILE)
    }

    private fun initOtherViews() {
        //  View views = view.findViewById(R.id.toolbarTop);
// img_first_icon = (ImageView) views.findViewById(R.id.img_first_icon);
        img_right_icon = (activity as MainActivity?)!!.findViewById<View>(R.id.img_right_icon) as ImageView
        tv_title = (activity as MainActivity?)!!.findViewById<View>(R.id.tvHeading) as BoldNoyhr
        img_right_icon.visibility = View.VISIBLE
        tv_title.setText("Profile")
        val tvDone = (activity as MainActivity?)!!.findViewById<View>(R.id.tvDone) as TextView
        tvDone.visibility = View.GONE

        img_right_icon.visibility = View.VISIBLE
    }

    private fun initViews() {

    }

    private fun clickListeners() {
        ivCameraBackgroundProfileMusicLover.setOnClickListener(this)
        ivCameraProfilePicProfileMusicLover.setOnClickListener(this)
        ivIconDropProfileMusicLover.setOnClickListener(this)
        ivIconUpProfileMusicLover.setOnClickListener(this)
        img_right_icon.setOnClickListener(this)

    }


    override fun onClick(v: View?) {
        if (v?.id == R.id.ivCameraBackgroundProfileMusicLover) {
            imgFrom = FOR_BACKGROUND
            checkPermissions("image", "com.musicseque.music_lover.fragments.FragmentProfileDetailMusicLover", this)

        } else if (v?.id == R.id.ivCameraProfilePicProfileMusicLover) {
            imgFrom = FOR_PROFILE
            checkPermissions("image", "com.musicseque.music_lover.fragments.FragmentProfileDetailMusicLover", this)
        } else if (v?.id == R.id.ivIconDropProfileMusicLover) {
            tvBioProfileMusicLover.setVisibility(View.VISIBLE);
            ivIconDropProfileMusicLover.setVisibility(View.GONE);
            ivIconUpProfileMusicLover.setVisibility(View.VISIBLE);
        } else if (v?.id == R.id.ivIconUpProfileMusicLover) {
            tvBioProfileMusicLover.setVisibility(View.GONE);
            ivIconDropProfileMusicLover.setVisibility(View.VISIBLE);
            ivIconUpProfileMusicLover.setVisibility(View.GONE);
        }
        else if(v?.id==R.id.img_right_icon)
        {
            startActivity(Intent(activity, MainActivity::class.java).putExtra("profileTemp", true).putExtra("frag", "com.musicseque.music_lover.fragments.FragmentProfileMusicLover"))

        }

    }


    private fun hitAPI(value: Int) {
        if (Utils.isNetworkConnected(activity!!)) {
            initializeLoader()
            if (value == FOR_USER_PROFILE) {
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

    private fun initializeLoader() {
        Utils.initializeAndShow(activity)
    }

    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            FOR_USER_PROFILE -> {

                try {
                    val obj = JSONObject(response.toString())
                    if (obj.getString("Status").equals("Success", ignoreCase = true)) {

                        if (!obj.getString("BackgroundImage").equals("", ignoreCase = true)) {
                            val mCoverPic = obj.getString("BackgroundImageUrl") + obj.getString("BackgroundImage")
                            progressBarProfileMusicLover.setVisibility(View.VISIBLE)
                            Glide.with(activity!!)
                                    .load(mCoverPic)
                                    .listener(object : RequestListener<Drawable?> {
                                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                            progressBarProfileMusicLover.setVisibility(View.GONE)
                                            return false
                                        }

                                        override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                            progressBarProfileMusicLover.setVisibility(View.GONE)
                                            return false
                                        }
                                    })
                                    .into(ivBackgroundProfileMusicLover)
                        } else {
                            progressBarProfileMusicLover.setVisibility(View.GONE)
                        }

                        var mProfilePic = ""
                        if (obj.getString("SocialId").equals("", ignoreCase = true)) {
                            ivCameraProfilePicProfileMusicLover.setVisibility(View.VISIBLE)
                            mProfilePic = obj.getString("ProfilePic")
                            if (mProfilePic.equals("", ignoreCase = true)) {
                                ivProfilePicProfileMusicLover.setImageDrawable(resources.getDrawable(R.drawable.icon_img_dummy))
                                pBarProfileMusicLover.setVisibility(View.GONE)
                            } else {
                                mProfilePic = obj.getString("ImgUrl") + obj.getString("ProfilePic")
                                Glide.with(activity!!)
                                        .load(mProfilePic)
                                        .listener(object : RequestListener<Drawable?> {
                                            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                                pBarProfileMusicLover.setVisibility(View.GONE)
                                                return false
                                            }

                                            override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                                pBarProfileMusicLover.setVisibility(View.GONE)
                                                return false
                                            }
                                        })
                                        .into(ivProfilePicProfileMusicLover)
                            }
                        } else {
                            ivCameraProfilePicProfileMusicLover.setVisibility(View.GONE)
                            mProfilePic = obj.getString("SocialImageUrl")
                            if (mProfilePic.equals("", ignoreCase = true)) {
                                Glide.with(activity!!)
                                        .load(R.drawable.icon_img_dummy).into(ivProfilePicProfileMusicLover)
                            } else {
                                Glide.with(activity!!)
                                        .load(mProfilePic)
                                        .listener(object : RequestListener<Drawable?> {
                                            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                                pBarProfileMusicLover.setVisibility(View.GONE)
                                                return false
                                            }

                                            override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                                pBarProfileMusicLover.setVisibility(View.GONE)
                                                return false
                                            }
                                        })
                                        .into(ivProfilePicProfileMusicLover)
                            }
                        }



                        tvUserNameDetailProfileMusicLover.setText(obj.getString("FirstName") + " " + obj.getString("LastName"))
                        tvUserTypeProfileMusicLover.setText(obj.getString("Expertise"))
                        tvUserLocationProfileMusicLover.setText(obj.getString("City") + ", " + obj.getString("CountryName"))
                        tvFollowersCountProfileMusicLover.setText(obj.getString("Followers"))
                        tvUserIDProfileMusicLover.setText(SharedPref.getString(Constants.UNIQUE_CODE, ""))
                        tvBioProfileMusicLover.setText(obj.getString("Bio"))


                    }

                    sv.fullScroll(ScrollView.FOCUS_UP)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


            }
            Constants.FOR_UPLOAD_PROFILE_IMAGE -> {

                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                        SharedPref.putString(Constants.PROFILE_IMAGE, jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))
                        Utils.showToast(activity, "Profile Pic uploaded successfully")
                        //Glide.with(getActivity()).load(jsonObject.getString("imageurl") + jsonObject.getString("ImageName")).into(ivProfilePic);
                        pBarProfileMusicLover.setVisibility(View.VISIBLE)
                        Glide.with(this)
                                .load(jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))
                                .listener(object : RequestListener<Drawable?> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                        pBarProfileMusicLover.setVisibility(View.GONE)
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        pBarProfileMusicLover.setVisibility(View.GONE)
                                        return false
                                    }
                                })
                                .into(ivProfilePicProfileMusicLover)
                        SharedPref.putString(Constants.PROFILE_IMAGE, jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))
                    } else {
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


            }
            Constants.FOR_UPLOAD_COVER_PIC -> {
                try {
                    val jsonObject1 = JSONObject(response.toString())
                    if (jsonObject1.getString("Status").equals("Success", ignoreCase = true)) {
                        SharedPref.putString(Constants.COVER_IMAGE, jsonObject1.getString("imageurl") + jsonObject1.getString("ImageName"))
                        progressBarProfileMusicLover.setVisibility(View.VISIBLE)
                        Utils.showToast(activity, "Cover Pic uploaded successfully")
                        Glide.with(this)
                                .load(jsonObject1.getString("imageurl") + jsonObject1.getString("ImageName"))
                                .listener(object : RequestListener<Drawable?> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                        progressBarProfileMusicLover.setVisibility(View.GONE)
                                        //   ivBackground.setVisibility(View.VISIBLE);
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        progressBarProfileMusicLover.setVisibility(View.GONE)
                                        // ivBackground.setVisibility(View.VISIBLE);
                                        return false
                                    }
                                })
                                .into(ivBackgroundProfileMusicLover)
                    } else {
                        progressBarProfileMusicLover.setVisibility(View.GONE)
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
        val mUSerId = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getString(Constants.USER_ID, ""))
        if (Utils.isNetworkConnected(activity!!)) {
            initializeLoader()

            if (imgFrom == FOR_BACKGROUND)
                ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_COVER_PIC, this)
            else
                ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_PROFILE_IMAGE, this)
        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }


    }
}