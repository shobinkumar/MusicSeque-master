package com.musicseque.event_manager.activity

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
import com.musicseque.event_manager.model.EventModelParcelable
import com.musicseque.interfaces.MyInterface
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.*
import com.musicseque.venue_manager.activity.CheckVenueAvailabilityActivity
import com.musicseque.venue_manager.fragment.ImagesVenueFragment
import com.musicseque.venue_manager.fragment.VenueGigsFragment
import kotlinx.android.synthetic.main.activity_venue_detail_event_manager.*
import kotlinx.android.synthetic.main.bottom_bar_venues.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONException
import org.json.JSONObject

class VenueDetailEventManagerActivity:BaseActivity(), MyInterface, View.OnClickListener { override fun onClick(view: View) {
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
        R.id.ivIconDropVenueEventManager -> {
            tvBioVenueEventManager.visibility = View.VISIBLE
            ivIconDropVenueEventManager.visibility = View.GONE
            ivIconUpVenueEventManager.visibility = View.VISIBLE
        }
        //hide

        R.id.ivIconUpVenueEventManager -> {
            tvBioVenueEventManager.visibility = View.GONE
            ivIconDropVenueEventManager.visibility = View.VISIBLE
            ivIconUpVenueEventManager.visibility = View.GONE
        }
        R.id.img_first_icon -> {
            finish()
        }
        R.id.tvAvailabilityVenueEventManager -> {
            val intent = Intent(this, CheckVenueAvailabilityActivity::class.java).putExtra("venue_id", mVenueId).putExtra("venue_name",mVenueName)
            startActivity(intent)
        }
        R.id.tvSendRequestVenueEventManager->
        {
            val obj = JSONObject();
            obj.put("EventId",model!!.EventId)
            obj.put("EventTitle", model!!.EventTitle)
            obj.put("EventDescription", model!!.EventDescription)
            obj.put("EventTypeId",  model!!.EventTypeId)

            obj.put("EventDateFrom", model!!.EventDateFrom)
            obj.put("EventDateTo",  model!!.EventDateTo)
            obj.put("EventTimeFrom",  model!!.EventTimeFrom)
            obj.put("EventTimeTo",  model!!.EventTimeTo)
            obj.put("EventGatheringCapacity",  model!!.EventGatheringCapacity)
            obj.put("EventChargesPayCurrencyId",  model!!.EventChargesPayCurrencyId)
            obj.put("EventBudget",  model!!.EventBudget)
            obj.put("EventManagerId", SharedPref.getString(Constants.USER_ID, ""))
            obj.put("EventVenueId", mVenueId)

            hitAPI(Constants.FOR_SAVE_UPDATE_EVENT_DETAIL, obj.toString())
        }
    }
}

    private var model: EventModelParcelable?=null
    private var mVenueName: String=""
    lateinit var mVenueId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_detail_event_manager)
        initViews()
        listeners()
        model = intent.extras.getParcelable<EventModelParcelable>("data")




    }

    private fun initViews() {
        tv_title.setText("Profile")
        img_right_icon.visibility = View.GONE
        img_first_icon.visibility = View.VISIBLE
        mVenueId = intent.getStringExtra("venue_id")
        hitAPI(Constants.FOR_USER_PROFILE,"")

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
        ivIconDropVenueEventManager.setOnClickListener(this)
        ivIconUpVenueEventManager.setOnClickListener(this)
        tvAvailabilityVenueEventManager.setOnClickListener(this)
        llGigs.setOnClickListener(this)
        llVideo.setOnClickListener(this)
        llPhotos . setOnClickListener (this)
        llMember . setOnClickListener (this)
        tvSendRequestVenueEventManager.setOnClickListener(this)


    }

    fun hitAPI(type: Int,args:String) {


            if (type == Constants.FOR_USER_PROFILE) {
                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("UserId", mVenueId)
                    APIHit.sendPostData(jsonObject.toString(), Constants.FOR_USER_PROFILE, this,this)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
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
                        tvUserNameDetailVenueEventManager.setText(jsonObj.getString("FirstName") + " " + jsonObj.getString("LastName"))
                        tvUserIDVenueEventManager.text = SharedPref.getString(Constants.UNIQUE_CODE, "")
                        tvUserLocationVenueEventManager.setText(jsonObj.getString("City") + ", " + jsonObj.getString("CountryName"))
                        tvReviewsVenueEventManager.text = "(" + jsonObj.getString("Reviews") + " reviews" + ")"
                        tvCapacityVenueEventManager.text = jsonObj.getString("VenueCapacity")
                        tvFollowersCountVenueEventManager.text = jsonObj.getString("Followers")
                        tvBioVenueEventManager.text = jsonObj.getString("Bio")
                        tvUserIDVenueEventManager.text = jsonObj.getString("UniqueCode")

                        if (jsonObj.getString("ProfilePic").equals("", true)) {
                            Glide.with(this).load(R.drawable.icon_img_dummy).into(ivProfilePicVenueEventManager)
                        } else {
                            Glide.with(this).load(jsonObj.getString("ImgUrl") + jsonObj.getString("ProfilePic")).into(ivProfilePicVenueEventManager)

                        }

                        if (jsonObj.getString("BackgroundImage").equals("", true)) {
                            progressBarVenueEventManager.visibility = View.GONE
                            Glide.with(this).load(jsonObj.getString("BackgroundImageUrl") + jsonObj.getString("BackgroundImage")).into(ivBackgroundVenueEventManager)

                            R.drawable.icon_img_dummy

                        } else {
                            Glide.with(this).load(R.drawable.icon_img_dummy).into(ivBackgroundVenueEventManager)
                            progressBarVenueEventManager.visibility = View.GONE

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