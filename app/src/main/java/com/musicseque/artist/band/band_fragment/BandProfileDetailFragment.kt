package com.musicseque.artist.band.band_fragment

import android.content.Intent

import android.graphics.drawable.Drawable

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.ScrollView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.musicseque.Fonts.BoldNoyhr
import com.musicseque.Fonts.Noyhr
import com.musicseque.MainActivity
import com.musicseque.R

import com.musicseque.artist.fragments.ImagesFragment
import com.musicseque.artist.fragments.MusicFragment
import com.musicseque.interfaces.MyInterface
import com.musicseque.models.ImageModel
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.KotlinBaseFragment
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.bottom_bar_band.*
import kotlinx.android.synthetic.main.fragment_band_profile_detail.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*

class BandProfileDetailFragment : KotlinBaseFragment(), View.OnClickListener, MyInterface {

    var IMAGE_FOR = 0
    var FOR_PROFILE = 1
    var FOR_BACKGROUND = 2

    var img_right_icon: ImageView? = null
    var tv_title: BoldNoyhr? = null
    var arrayList = ArrayList<ImageModel>()
    var views: View? = null
    private var mBandId: String? = null
    var isPicAPIHit = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        views = inflater.inflate(R.layout.fragment_band_profile_detail, null)

        return views
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initOtherViews()
        initViews()
        listeners()
    }
    private fun initOtherViews() {
        mBandId = arguments!!.getString("band_id")
        img_right_icon = (activity as MainActivity?)!!.findViewById<View>(R.id.img_right_icon) as ImageView
        tv_title = (activity as MainActivity?)!!.findViewById<View>(R.id.tvHeading) as BoldNoyhr
        img_right_icon!!.visibility = View.VISIBLE
        tv_title!!.text = "Band Profile"

    }

    private fun initViews() {
        btnFollow!!.visibility = View.GONE
    }

    private fun listeners() {
        ivCameraBackground.setOnClickListener(this)
        ivCameraProfilePic.setOnClickListener(this)
        tvMessage.setOnClickListener(this)
        ivIconDrop.setOnClickListener(this)
        ivIconUp.setOnClickListener(this)
        llMusic.setOnClickListener(this)
        llVideo.setOnClickListener(this)
        llPhotos.setOnClickListener(this)
        llGigs.setOnClickListener(this)
        llMember.setOnClickListener(this)
        btnFollow.setOnClickListener(this)


        img_right_icon!!.setOnClickListener { startActivity(Intent(activity, MainActivity::class.java).putExtra("band_id", mBandId).putExtra("frag", "com.musicseque.artist.fragments.BandFormFragment")) }
    }

    override fun onResume() {
        super.onResume()
        if (!isPicAPIHit) {
            bandProfile
            changeBackgroundColor(ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs_active), resources.getString(R.string.txt_gigs), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivMember, tvMember, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_members))
            changeFragment(BandGigsFragment())
        }
    }

   override fun onClick(view: View) {
        when (view.id) {
            R.id.ivCameraBackground -> {
                IMAGE_FOR = FOR_BACKGROUND
                checkPermissions("image", "com.musicseque.artist.band.band_fragment.BandProfileDetailFragment", this)

            }
            R.id.btnFollow -> {
            }
            R.id.ivCameraProfilePic -> {
                IMAGE_FOR = FOR_PROFILE
                checkPermissions("image", "com.musicseque.artist.band.band_fragment.BandProfileDetailFragment", this)
            }
            R.id.tvMessage -> {
            }
            R.id.ivIconDrop -> {
                //hide
                tvBio!!.visibility = View.VISIBLE
                ivIconDrop!!.visibility = View.GONE
                ivIconUp!!.visibility = View.VISIBLE
            }
            R.id.ivIconUp -> {
                //visible
                tvBio!!.visibility = View.GONE
                ivIconDrop!!.visibility = View.VISIBLE
                ivIconUp!!.visibility = View.GONE
            }
            R.id.llMusic ->
            {
                changeBackgroundColor(ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music_active), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs), ivMember, tvMember, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_members))

            }
            R.id.llVideo ->{
                changeBackgroundColor(ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos_active), resources.getString(R.string.txt_video), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs), ivMember, tvMember, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_members))

            }
            R.id.llPhotos ->
                changeBackgroundColor(ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos_active), resources.getString(R.string.txt_image), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs), ivMember, tvMember, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_members))

            R.id.llGigs ->
            {
                changeBackgroundColor(ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs_active), resources.getString(R.string.txt_gigs), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivMember, tvMember, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_members))

            }
            R.id.llMember -> {
                changeBackgroundColor(ivMember, tvMember, resources.getDrawable(R.drawable.icon_collaborators_active), resources.getString(R.string.txt_members), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs))
                val bundle = Bundle()
                bundle.putString("band_id", mBandId)
                val fragment = BandMemberStatusFragment()
                fragment.arguments = bundle
                changeFragment(fragment)
            }
        }
    }

    private fun changeBackgroundColor(ivSelected: ImageView?, tvSelected: Noyhr?, selectedDrawable: Drawable, textSelected: String, iv1: ImageView?, tv1: Noyhr?, drawable1: Drawable, text1: String, iv2: ImageView?, tv2: Noyhr?, drawable2: Drawable, text2: String, iv3: ImageView?, tv3: Noyhr?, drawable3: Drawable, text3: String, iv4: ImageView?, tv4: Noyhr?, drawable4: Drawable, text4: String) {
        ivSelected!!.setImageDrawable(selectedDrawable)
        tvSelected!!.text = textSelected
        tvSelected.setTextColor(resources.getColor(R.color.color_orange))
        iv1!!.setImageDrawable(drawable1)
        tv1!!.text = text1
        tv1.setTextColor(resources.getColor(R.color.color_white))
        iv2!!.setImageDrawable(drawable2)
        tv2!!.text = text2
        tv2.setTextColor(resources.getColor(R.color.color_white))
        iv3!!.setImageDrawable(drawable3)
        tv3!!.text = text3
        tv3.setTextColor(resources.getColor(R.color.color_white))
        iv4!!.setImageDrawable(drawable4)
        tv4!!.text = text4
        tv4.setTextColor(resources.getColor(R.color.color_white))
    }

    private val bandProfile: Unit
        private get() {
            if (Utils.isNetworkConnected(activity)) {
                initializeLoader()
                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("BandId", mBandId)
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_BAND_PROFILE, this@BandProfileDetailFragment)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Utils.showToast(activity, resources.getString(R.string.err_no_internet))
            }
        }



    fun initializeLoader() {
        Utils.initializeAndShow(activity)
    }



    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_BAND_PROFILE -> try {
                val `object` = JSONObject(response.toString())
                if (`object`.getString("Status").equals("Success", ignoreCase = true)) {
                    val jsonArray = `object`.getJSONArray("result")
                    val obj = jsonArray.getJSONObject(0)
                    tvUserNameDetail!!.text = obj.getString("BandName")
                    tvUserLocation!!.text = obj.getString("BandCity") + ", " + obj.getString("CountryName")
                    tvFollowersCount!!.text = obj.getString("Followers")
                    tvUserID!!.text = obj.getString("UniqueCode")
                    tvReviews!!.text = "(" + obj.getString("Reviews") + " reviews" + ")"
                    tvGenre!!.text = obj.getString("GenreTypeName")
                    tvExperience!!.text = obj.getString("ExperienceYear")
                    tvBio!!.text = obj.getString("Bio")
                    var mProfilePic = ""
                    mProfilePic = obj.getString("BandImg")
                    if (mProfilePic.equals("", ignoreCase = true)) {
                        ivProfilePic!!.setImageDrawable(resources.getDrawable(R.drawable.icon_img_dummy))
                        pBar!!.visibility = View.GONE
                    } else {
                        mProfilePic = obj.getString("BandImgPath") + obj.getString("BandImg")
                        Glide.with(activity!!)
                                .load(mProfilePic)
                                .listener(object : RequestListener<Drawable?> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                        pBar!!.visibility = View.GONE
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        pBar!!.visibility = View.GONE
                                        return false
                                    }
                                })
                                .into(ivProfilePic!!)
                    }
                    if (!obj.getString("BandBackgroundImg").equals("", ignoreCase = true)) {
                        val mCoverPic = obj.getString("BandBackgroundImgPath") + obj.getString("BandBackgroundImg")
                        progressBar!!.visibility = View.VISIBLE
                        Glide.with(activity!!)
                                .load(mCoverPic)
                                .listener(object : RequestListener<Drawable?> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                        progressBar!!.visibility = View.GONE
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        progressBar!!.visibility = View.GONE
                                        return false
                                    }
                                })
                                .into(ivBackground!!)
                    } else {
                        progressBar!!.visibility = View.GONE
                        Glide.with(activity!!)
                                .load(R.drawable.icon_img_dummy)
                                .into(ivBackground!!)
                    }
                    if (obj.getString("NewStatus").equals("Available", ignoreCase = true)) {
                        ivIndicator!!.setImageDrawable(resources.getDrawable(R.drawable.greenindicator))
                    } else if (obj.getString("NewStatus").equals("Offline", ignoreCase = true)) {
                        ivIndicator!!.setImageDrawable(resources.getDrawable(R.drawable.icon_invisible))
                    } else {
                        ivIndicator!!.setImageDrawable(resources.getDrawable(R.drawable.icon_red))
                    }
                } else {
                    progressBar!!.visibility = View.GONE
                }
                sv!!.fullScroll(ScrollView.FOCUS_UP)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            Constants.FOR_UPLOAD_BAND_PROFILE_IMAGE -> {
                isPicAPIHit = false
                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                        Utils.showToast(activity, "Profile Pic uploaded successfully")
                        //Glide.with(getActivity()).load(jsonObject.getString("imageurl") + jsonObject.getString("ImageName")).into(ivProfilePic);
                        pBar!!.visibility = View.VISIBLE
                        Glide.with(this)
                                .load(jsonObject.getString("BandImgUrl") + jsonObject.getString("BandImage"))
                                .listener(object : RequestListener<Drawable?> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                        pBar!!.visibility = View.GONE
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        pBar!!.visibility = View.GONE
                                        return false
                                    }
                                })
                                .into(ivProfilePic!!)
                    } else {
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            Constants.FOR_UPLOAD_BAND_BACKGROUND_IMAGE -> {
                isPicAPIHit = false
                try {
                    val jsonObject1 = JSONObject(response.toString())
                    if (jsonObject1.getString("Status").equals("Success", ignoreCase = true)) {
                        progressBar!!.visibility = View.VISIBLE
                        Utils.showToast(activity, "Cover Pic uploaded successfully")
                        Glide.with(this)
                                .load(jsonObject1.getString("BandImgUrl") + jsonObject1.getString("BandImage"))
                                .listener(object : RequestListener<Drawable?> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                        progressBar!!.visibility = View.GONE
                                        //   ivBackground.setVisibility(View.VISIBLE);
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        progressBar!!.visibility = View.GONE
                                        // ivBackground.setVisibility(View.VISIBLE);
                                        return false
                                    }
                                })
                                .into(ivBackground!!)
                    } else {
                        progressBar!!.visibility = View.GONE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun changeFragment(fragment: Fragment?) {
        val ft = fragmentManager!!.beginTransaction()
        ft.replace(R.id.frame_layout, fragment!!).commit()
    }















    fun getImageFile(file: File) {
        val mFile = RequestBody.create(MediaType.parse("image/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
        val mUSerId = RequestBody.create(MediaType.parse("text/plain"), mBandId)
        if (Utils.isNetworkConnected(activity!!)) {
            initializeLoader()
            if(IMAGE_FOR==FOR_BACKGROUND)
                ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_BAND_BACKGROUND_IMAGE, this@BandProfileDetailFragment)
                else
                    ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_BAND_PROFILE_IMAGE, this@BandProfileDetailFragment)

        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }








    }
}