package com.musicseque.venue_manager.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.musicseque.Fonts.Noyhr
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_USER_PROFILE
import com.musicseque.utilities.KotlinUtils
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import com.musicseque.venue_manager.fragment.ImagesVenueFragment
import com.musicseque.venue_manager.fragment.VenueGigsFragment
import kotlinx.android.synthetic.main.activity_other_venue_profile.*
import kotlinx.android.synthetic.main.bottom_bar_venues.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONException
import org.json.JSONObject

class OtherVenueProfileActivity : BaseActivity(), MyInterface, View.OnClickListener {
    override fun onClick(view: View) {
        when (view.id) {

            R.id.llGigs->
            {
                openFragment(VenueGigsFragment())
                changeBackgroundColor(ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs_active), resources.getString(R.string.txt_gigs), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video),ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivMember, tvMember, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_members))

            }

            R.id.llVideo->
            {
                openFragment(ImagesVenueFragment())
                changeBackgroundColor(ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos_active), resources.getString(R.string.txt_video),  ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs), ivMember, tvMember, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_members))

            }
            R.id.llPhotos->
            {
                openFragment(ImagesVenueFragment())
                changeBackgroundColor(ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos_active), resources.getString(R.string.txt_image),ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video),ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs), ivMember, tvMember, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_members))

            }
            R.id.llMember->
            {
                openFragment(ImagesVenueFragment())
                changeBackgroundColor(ivMember, tvMember, resources.getDrawable(R.drawable.icon_collaborators_active), resources.getString(R.string.txt_members),ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image),ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video),ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs))

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
            R.id.img_first_icon -> {
                finish()
            }
            R.id.tvAvailability -> {
                val intent = Intent(this, CheckVenueAvailabilityActivity::class.java).putExtra("venue_id", mVenueId).putExtra("venue_name",mVenueName)
                startActivity(intent)
            }
        }
    }

    private var mVenueName: String=""
    lateinit var mVenueId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_venue_profile)
        initViews()
        listeners()


    }

    private fun initViews() {
        tv_title.setText("Profile")
        img_right_icon.visibility = View.GONE
        img_first_icon.visibility = View.VISIBLE
        mVenueId = intent.getStringExtra("venue_id")
        hitAPI(FOR_USER_PROFILE)

    }

    private fun openFragment(fragment: Fragment) {
        if (fragment is VenueGigsFragment) {
            val args = Bundle()
            args.putString("venue_id", mVenueId)
            args.putString("venue_name", mVenueName)

            fragment.setArguments(args)
        }
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    private fun listeners() {
        img_first_icon.setOnClickListener(this)
        ivIconDrop.setOnClickListener(this)
        ivIconUp.setOnClickListener(this)
        tvAvailability.setOnClickListener(this)
        llGigs.setOnClickListener(this)
        llVideo.setOnClickListener(this)
        llPhotos . setOnClickListener (this)
        llMember . setOnClickListener (this)


    }

    fun hitAPI(type: Int) {

        if (KotlinUtils.isNetConnected(this)) {
            Utils.initializeAndShow(this)
            if (type == FOR_USER_PROFILE) {
                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("UserId", mVenueId)
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_USER_PROFILE, this)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }

    }


    override fun sendResponse(response: Any?, TYPE: Int) {
        Utils.hideProgressDialog()
        val jsonObj = JSONObject(response.toString())
        if (jsonObj.getString("Status").equals("Success", true)) {
            when (TYPE) {


                Constants.FOR_USER_PROFILE -> {
                    try {


                        mVenueName=jsonObj.getString("FirstName") + " " + jsonObj.getString("LastName")
                        tvUserNameDetail.setText(jsonObj.getString("FirstName") + " " + jsonObj.getString("LastName"))
                        tvUserID.text = SharedPref.getString(Constants.UNIQUE_CODE, "")
                        tvUserLocation.setText(jsonObj.getString("City") + ", " + jsonObj.getString("CountryName"))
                        tvReviews.text = "(" + jsonObj.getString("Reviews") + " reviews" + ")"
                        tvCapacity.text = jsonObj.getString("VenueCapacity")
                        tvFollowersCount.text = jsonObj.getString("Followers")
                        tvBio.text = jsonObj.getString("Bio")
                        tvUserID.text = jsonObj.getString("UniqueCode")

                        if (jsonObj.getString("ProfilePic").equals("", true)) {
                            Glide.with(this).load(R.drawable.icon_img_dummy).into(ivProfilePic)
                        } else {
                            Glide.with(this).load(jsonObj.getString("ImgUrl") + jsonObj.getString("ProfilePic")).into(ivProfilePic)

                        }

                        if (jsonObj.getString("BackgroundImage").equals("", true)) {
                            progressBar.visibility = View.GONE
                            Glide.with(this).load(jsonObj.getString("BackgroundImageUrl") + jsonObj.getString("BackgroundImage")).into(ivBackground)

                            R.drawable.icon_img_dummy

                        } else {
                            Glide.with(this).load(R.drawable.icon_img_dummy).into(ivBackground)
                            progressBar.visibility = View.GONE

                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    openFragment(VenueGigsFragment())
                    changeBackgroundColor(ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs_active), resources.getString(R.string.txt_gigs), ivVideo, tvVideo, getResources().getDrawable(R.drawable.icon_videos), getResources().getString(R.string.txt_video),ivImage, tvImage, getResources().getDrawable(R.drawable.icon_photos), getResources().getString(R.string.txt_image), ivMember, tvMember, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_members))

                }


            }

        }


    }


    private fun changeBackgroundColor(ivSelected: ImageView, tvSelected: Noyhr, selectedDrawable: Drawable, textSelected: String, iv1: ImageView, tv1: Noyhr, drawable1: Drawable, text1: String, iv2: ImageView, tv2: Noyhr, drawable2: Drawable, text2: String, iv3: ImageView, tv3: Noyhr, drawable3: Drawable, text3: String) {
        ivSelected.setImageDrawable(selectedDrawable)
        tvSelected.text = textSelected
        tvSelected.setTextColor(resources.getColor(R.color.color_orange))
        iv1.setImageDrawable(drawable1)
        tv1.text = text1
        tv1.setTextColor(resources.getColor(R.color.color_white))
        iv2.setImageDrawable(drawable2)
        tv2.text = text2
        tv2.setTextColor(resources.getColor(R.color.color_white))
        iv3.setImageDrawable(drawable3)
        tv3.text = text3
        tv3.setTextColor(resources.getColor(R.color.color_white))

    }


}

