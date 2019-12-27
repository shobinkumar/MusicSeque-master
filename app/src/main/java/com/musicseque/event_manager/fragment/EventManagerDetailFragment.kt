package com.musicseque.event_manager.fragment

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
import com.musicseque.utilities.Constants.USER_ID
import com.musicseque.utilities.KotlinBaseFragment
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.fragment_event_manager_detail.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File

class EventManagerDetailFragment : KotlinBaseFragment(), MyInterface, View.OnClickListener {

    lateinit var img_right_icon: ImageView
    lateinit var tv_title: BoldNoyhr
    var imgFrom: Int = 0
    var FOR_BACKGROUND = 1
    var FOR_PROFILE = 2
    lateinit var v: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_event_manager_detail, null)

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
        img_right_icon = (activity as MainActivity?)!!.findViewById<View>(R.id.img_right_icon) as ImageView
        tv_title = (activity as MainActivity?)!!.findViewById<View>(R.id.tvHeading) as BoldNoyhr
        img_right_icon.setVisibility(View.VISIBLE)
        tv_title.setText("Profile")
        val tvDone = (activity as MainActivity?)!!.findViewById<View>(R.id.tvDone) as TextView
        tvDone.visibility = View.GONE
    }

    private fun initViews() {

    }

    private fun clickListeners() {
        ivCameraBackgroundDetailEventManager.setOnClickListener(this)
        ivCameraProfilePicDetailEventManager.setOnClickListener(this)
        img_right_icon.setOnClickListener(this)
        ivIconDropDetailEventManager.setOnClickListener(this)
        ivIconUpDetailEventManager.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        if (v?.id == R.id.ivCameraBackgroundDetailEventManager) {
            imgFrom = FOR_BACKGROUND
            checkPermissions("image", "com.musicseque.event_manager.fragment.EventManagerDetailFragment", this)

        } else if (v?.id == R.id.ivCameraProfilePicDetailEventManager) {
            imgFrom = FOR_PROFILE
            checkPermissions("image", "com.musicseque.event_manager.fragment.EventManagerDetailFragment", this)

        } else if (v.id == R.id.img_right_icon) {
            startActivity(Intent(activity, MainActivity::class.java).putExtra("profileTemp", true).putExtra("frag", "com.musicseque.event_manager.fragment.EventManagerFormFragment"))
        }
        else if (v.id == R.id.ivIconDropDetailEventManager) {
            tvBioDetailEventManager.setVisibility(View.VISIBLE);
            ivIconDropDetailEventManager.setVisibility(View.GONE);
            ivIconUpDetailEventManager.setVisibility(View.VISIBLE);
        }
        else if (v.id == R.id.ivIconUpDetailEventManager) {
            tvBioDetailEventManager.setVisibility(View.GONE);
            ivIconDropDetailEventManager.setVisibility(View.VISIBLE);
            ivIconUpDetailEventManager.setVisibility(View.GONE)
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
            Constants.FOR_USER_PROFILE -> {

                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", ignoreCase = true)) {
                    tvUserNameDetailDetailEventManager.setText(obj.getString("FirstName") + " " + obj.getString("LastName"))
                    tvUserTypeDetailEventManager.text = obj.getString("Expertise")
                    tvUserLocationDetailEventManager.text = obj.getString("City") + ", " + obj.getString("CountryName")
                    tvFollowersCountDetailEventManager.text = obj.getString("Followers")
                    tvUserIDDetailEventManager.text = SharedPref.getString(Constants.UNIQUE_CODE, "")
                    tvReviewsDetailEventManager.text = "(" + obj.getString("Reviews") + " reviews" + ")"
                    tvExperienceDetailEventManager.text = obj.getString("ExperienceYear")
                    tvBioDetailEventManager.text = obj.getString("Bio")
                    // tvAboutUser.setText("About " + obj.getString("FirstName") + " " + obj.getString("LastName"));
                    var mProfilePic = ""

                    if (obj.getString("SocialId").equals("", ignoreCase = true)) {
                        mProfilePic = obj.getString("ProfilePic")
                        if (mProfilePic.equals("", ignoreCase = true)) {

                            ivProfilePicDetailEventManager.setImageDrawable(resources.getDrawable(R.drawable.icon_img_dummy))
                            pBarDetailEventManager.visibility = View.GONE

                        } else {
                            mProfilePic = obj.getString("ImgUrl") + obj.getString("ProfilePic")
                            Glide.with(activity!!)
                                    .load(mProfilePic)
                                    .listener(object : RequestListener<Drawable> {
                                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                            pBarDetailEventManager.visibility = View.GONE
                                            return false
                                        }

                                        override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                            pBarDetailEventManager.visibility = View.GONE
                                            return false
                                        }
                                    })
                                    .into(ivProfilePicDetailEventManager)
                        }
                    } else {
                        mProfilePic = obj.getString("SocialImageUrl")

                       if(mProfilePic.equals(""))
                       {
                           Glide.with(activity!!)
                                   .load(R.drawable.icon_img_dummy)
                                   .listener(object : RequestListener<Drawable> {
                                       override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                           pBarDetailEventManager.visibility = View.GONE
                                           return false
                                       }

                                       override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                           pBarDetailEventManager.visibility = View.GONE
                                           return false
                                       }
                                   })
                                   .into(ivProfilePicDetailEventManager)
                       }
                        else
                       {
                           Glide.with(activity!!)
                                   .load(mProfilePic)
                                   .listener(object : RequestListener<Drawable> {
                                       override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                           pBarDetailEventManager.visibility = View.GONE
                                           return false
                                       }

                                       override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                           pBarDetailEventManager.visibility = View.GONE
                                           return false
                                       }
                                   })
                                   .into(ivProfilePicDetailEventManager)
                       }


                    }

                    if (!obj.getString("BackgroundImage").equals("", ignoreCase = true)) {
                        val mCoverPic = obj.getString("BackgroundImageUrl") + obj.getString("BackgroundImage")
                        progressBarDetailEventManager.setVisibility(View.VISIBLE)
                        Glide.with(activity!!)
                                .load(mCoverPic)
                                .listener(object : RequestListener<Drawable?> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                        progressBarDetailEventManager.setVisibility(View.GONE)
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        progressBarDetailEventManager.setVisibility(View.GONE)
                                        return false
                                    }
                                })
                                .into(ivBackgroundDetailEventManager)
                    } else {
                        progressBarDetailEventManager.setVisibility(View.GONE)
                    }
                    sv.fullScroll(ScrollView.FOCUS_UP)

                    if (SharedPref.getString(Constants.LOGIN_TYPE, "Simple")!!.equals("Simple", ignoreCase = true)) {
                        ivCameraProfilePicDetailEventManager.visibility = View.VISIBLE
                    } else {
                        ivCameraProfilePicDetailEventManager.visibility = View.GONE

                    }
                }


            }
            Constants.FOR_UPLOAD_PROFILE_IMAGE -> {
                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                        Utils.showToast(activity, "Profile Pic uploaded successfully")
                        //Glide.with(getActivity()).load(jsonObject.getString("imageurl") + jsonObject.getString("ImageName")).into(ivProfilePic);


                        pBarDetailEventManager.visibility = View.VISIBLE

                        Glide.with(this)
                                .load(jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                        pBarDetailEventManager.visibility = View.GONE
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        pBarDetailEventManager.visibility = View.GONE
                                        return false
                                    }
                                })
                                .into(ivProfilePicDetailEventManager)
                        SharedPref.putString(Constants.PROFILE_IMAGE, jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))


                    } else {
                        pBarDetailEventManager.visibility = View.GONE

                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
            Constants.FOR_UPLOAD_COVER_PIC -> {
                try {
                    val jsonObject1 = JSONObject(response.toString())
                    if (jsonObject1.getString("Status").equals("Success", ignoreCase = true)) {
                        progressBarDetailEventManager.visibility = View.VISIBLE
                        Utils.showToast(activity, "Cover Pic uploaded successfully")

                        Glide.with(this)
                                .load(jsonObject1.getString("imageurl") + jsonObject1.getString("ImageName"))
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                        progressBarDetailEventManager.visibility = View.GONE
                                        //   ivBackgroundDetailEventManager.setVisibility(View.VISIBLE);
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        progressBarDetailEventManager.visibility = View.GONE
                                        // ivBackgroundDetailEventManager.setVisibility(View.VISIBLE);
                                        return false
                                    }
                                })
                                .into(ivBackgroundDetailEventManager)

                    } else {
                        progressBarDetailEventManager.visibility = View.GONE
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
        val mUSerId = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getString(USER_ID, ""))
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