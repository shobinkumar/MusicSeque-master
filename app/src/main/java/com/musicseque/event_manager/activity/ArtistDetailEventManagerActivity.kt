package com.musicseque.event_manager.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.musicseque.Fonts.Noyhr
import com.musicseque.MainActivity
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.artist.activity.other_artist_activity.ArtistBandListActivity
import com.musicseque.artist.fragments.*
import com.musicseque.interfaces.MyInterface
import com.musicseque.utilities.APIHit
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_OTHER_PROFILE
import com.musicseque.utilities.Constants.FOR_SEND_REQ_ARTIST
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_artist_detail_event_manager.*
import kotlinx.android.synthetic.main.bottom_bar.*
import kotlinx.android.synthetic.main.toolbar_top.*
import org.json.JSONException
import org.json.JSONObject

class ArtistDetailEventManagerActivity : BaseActivity(), MyInterface, View.OnClickListener {
    private var isFollow: Boolean = false
    private var mFollowerCount: String = ""
    private var mUserId: Int = 0
    private var mEventId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist_detail_event_manager)
        initOtherViews()
        initViews()
        listeneres()
    }

    private fun listeneres() {
        ButterKnife.bind(this)
        tvSendRequestArtistEventManager.setOnClickListener(this)
        //tvBand.setOnClickListener(this)
        img_first_icon.setOnClickListener(this)
        ivCameraBackgroundArtistEventManager.setOnClickListener(this)
        btnFollowArtistEventManager.setOnClickListener(this)
        ivCameraProfilePicArtistEventManager.setOnClickListener(this)
        tvMessageArtistEventManager.setOnClickListener(this)
        ivIconDropArtistEventManager.setOnClickListener(this)
        ivIconUpArtistEventManager.setOnClickListener(this)
        llMusic.setOnClickListener(this)
        llVideo.setOnClickListener(this)
        llPhotos.setOnClickListener(this)
        llGigs.setOnClickListener(this)
        llCollaborators.setOnClickListener(this)
    }

    private fun initOtherViews() {
        tv_title!!.text = "Profile"
        img_first_icon!!.visibility = View.VISIBLE
        img_right_icon!!.visibility = View.GONE
        mUserId = intent.getIntExtra("id", 0)
        mEventId = intent.getStringExtra("event_id")

    }

    private fun initViews() {
        changeBackgroundColor(ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs_active), resources.getString(R.string.txt_gigs), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivCollaborators, tvCollaborators, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_collaborators))

        val jsonObject = JSONObject()
        jsonObject.put("ArtistUserId", mUserId)
        jsonObject.put("LoginUserId", SharedPref.getString(Constants.USER_ID, ""))

        hitAPI(FOR_OTHER_PROFILE, jsonObject.toString())


    }

    private fun hitAPI(TYPE: Int, args: String) {

            if (TYPE == FOR_OTHER_PROFILE) {
                try {

                    APIHit.sendPostData(args, Constants.FOR_OTHER_PROFILE, this@ArtistDetailEventManagerActivity,this)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else if (TYPE == Constants.FOR_FOLLOW_UNFOLLOW_ARTIST) {
                APIHit.sendPostData(args, Constants.FOR_FOLLOW_UNFOLLOW_ARTIST, this@ArtistDetailEventManagerActivity,this)

            } else if (TYPE == FOR_SEND_REQ_ARTIST) {
                APIHit.sendPostData(args, Constants.FOR_SEND_REQ_ARTIST, this@ArtistDetailEventManagerActivity,this)

            }


    }


    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_OTHER_PROFILE ->
                try {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                    val jsonArray = jsonObject.getJSONArray("result")
                    val obj = jsonArray.getJSONObject(0)
                    tvUserNameDetailArtistEventManager!!.text = obj.getString("FirstName") + " " + obj.getString("LastName")
                    tvUserTypeArtistEventManager!!.text = obj.getString("Expertise")
                    tvUserLocationArtistEventManager!!.text = obj.getString("City") + ", " + obj.getString("CountryName")
                    tvFollowersCountArtistEventManager!!.text = obj.getString("Followers")
                    val mCount = obj.getString("Followers").toInt()
                    mFollowerCount = mCount.toString() + ""
                    tvUserIDArtistEventManager!!.text = SharedPref.getString(Constants.UNIQUE_CODE, "")
                    tvReviewsArtistEventManager!!.text = "(" + obj.getString("Reviews") + " reviews" + ")"
                    tvGenreArtistEventManager!!.text = obj.getString("GenreTypeName")
                    tvExperienceArtistEventManager!!.text = obj.getString("ExperienceYear")
                    tvBioArtistEventManager!!.text = obj.getString("Bio")
                    tvUserIDArtistEventManager!!.text = obj.getString("UniqueCode")
                    if (obj.getString("NewStatus").equals("Available", ignoreCase = true)) {
                        ivIndicatorArtistEventManager!!.setImageDrawable(resources.getDrawable(R.drawable.icon_green))
                    } else if (obj.getString("NewStatus").equals("Offline", ignoreCase = true)) {
                        ivIndicatorArtistEventManager!!.setImageDrawable(resources.getDrawable(R.drawable.icon_invisible))
                    } else {
                        ivIndicatorArtistEventManager!!.setImageDrawable(resources.getDrawable(R.drawable.icon_red))
                    }
                    if (obj.getInt("FollowersTag") == 1) {
                        isFollow = true
                        btnFollowArtistEventManager!!.text = "Following"
                    } else {
                        btnFollowArtistEventManager!!.text = "Follow"
                        isFollow = false
                    }
                    showProfileBackgroundImage(obj)

                }
                sv!!.fullScroll(ScrollView.FOCUS_UP)
                    changeFragment(GigsFragment())
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            Constants.FOR_FOLLOW_UNFOLLOW_ARTIST -> try {
                val mCount: Int
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", ignoreCase = true)) {
                    if (obj.getInt("FollowerTag") == 1) {
                        isFollow = true
                        btnFollowArtistEventManager!!.text = "Following"
                        mCount = mFollowerCount!!.toInt() + 1
                        mFollowerCount = mCount.toString() + ""
                        tvFollowersCountArtistEventManager!!.text = mFollowerCount
                    } else {
                        btnFollowArtistEventManager!!.text = "Follow"
                        mCount = mFollowerCount!!.toInt() - 1
                        mFollowerCount = mCount.toString() + ""
                        tvFollowersCountArtistEventManager!!.text = mFollowerCount
                        isFollow = false
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            Constants.FOR_SEND_REQ_ARTIST -> {
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", ignoreCase = true)) {
                    Utils.showToast(this,obj.getString("Message"))
                    startActivity(Intent(this, MainActivity::class.java))

                }
                else{
                    Utils.showToast(this,obj.getString("Message"))

                }
            }
        }
    }


    override fun onClick(view: View) {
        when (view.id) {

            R.id.tvSendRequestArtistEventManager -> {

                val json = JSONObject()
                json.put("ArtistId", mUserId)
                json.put("EventId", mEventId)
                json.put("RequestStatus", "P")

                hitAPI(FOR_SEND_REQ_ARTIST, json.toString())

            }

            R.id.img_first_icon -> finish()
            R.id.tvBand -> startActivity(Intent(this, ArtistBandListActivity::class.java).putExtra("id", mUserId))
            R.id.btnFollow -> {
                changeFollowStatus()

                val jsonObject = JSONObject()
                try {
                    jsonObject.put("ArtistId", mUserId)
                    jsonObject.put("FollowerId", SharedPref.getString(Constants.USER_ID, ""))
                    if (isFollow) {
                        jsonObject.put("Status", "Y")
                        jsonObject.put("FollowerRemarks", "F")
                    } else {
                        jsonObject.put("Status", "N")
                        jsonObject.put("FollowerRemarks", "U")
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                hitAPI(Constants.FOR_FOLLOW_UNFOLLOW_ARTIST, jsonObject.toString())
            }
            R.id.tvMessage -> {
            }
            R.id.ivIconDrop -> {
                tvBioArtistEventManager!!.visibility = View.VISIBLE
                ivIconDropArtistEventManager!!.visibility = View.GONE
                ivIconUpArtistEventManager!!.visibility = View.VISIBLE
            }
            R.id.ivIconUp -> {
                tvBioArtistEventManager!!.visibility = View.GONE
                ivIconDropArtistEventManager!!.visibility = View.VISIBLE
                ivIconUpArtistEventManager!!.visibility = View.GONE
            }
            R.id.llMusic -> {
                changeBackgroundColor(ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music_active), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_collaborators))
                changeFragment(MusicFragment())


            }
            R.id.llVideo -> {
                changeBackgroundColor(ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos_active), resources.getString(R.string.txt_video), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_collaborators))
                changeFragment(VideoFragment())
            }
            R.id.llPhotos -> {
                changeBackgroundColor(ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos_active), resources.getString(R.string.txt_image), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_collaborators))
                val fragment = ImagesFragment()
                val bundle = Bundle()

                bundle.putString("UserId", mUserId.toString())
                fragment.arguments = bundle
                changeFragment(fragment)

            }
            R.id.llGigs -> {
                changeBackgroundColor(ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs_active), resources.getString(R.string.txt_gigs), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivCollaborators, tvCollaborators, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_collaborators))
                changeFragment(GigsFragment())
            }
            R.id.llCollaborators -> {
                changeBackgroundColor(ivCollaborators, tvCollaborators, resources.getDrawable(R.drawable.icon_collaborators_active), resources.getString(R.string.txt_collaborators), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs))
                changeFragment(CollaboratorsFragment())
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

    fun changeFragment(fragment: Fragment?) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frame_layout_ArtistEventManager, fragment!!).commit()
    }


    fun changeFollowStatus() {
        val mStatus = btnFollowArtistEventManager!!.text.toString()
        if (mStatus.equals("Follow", ignoreCase = true)) {
            isFollow = true
            btnFollowArtistEventManager!!.text = "Following"
        } else {
            btnFollowArtistEventManager!!.text = "Follow"
            isFollow = false
        }
    }

    fun showDialog() {
        Utils.initializeAndShow(this@ArtistDetailEventManagerActivity)
    }


    fun showProfileBackgroundImage(obj: JSONObject) {
        var mProfilePic = ""


        if (obj.getString("SocialId").equals("", ignoreCase = true)) {
            mProfilePic = obj.getString("ProfilePic")
            if (mProfilePic.equals("", ignoreCase = true)) {
                ivProfilePicArtistEventManager!!.setImageDrawable(resources.getDrawable(R.drawable.icon_img_dummy))
                pBarArtistEventManager!!.visibility = View.GONE
            } else {
                mProfilePic = obj.getString("ImgUrl") + obj.getString("ProfilePic")
                Glide.with(this)
                        .load(mProfilePic)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                pBarArtistEventManager!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                pBarArtistEventManager!!.visibility = View.GONE
                                return false
                            }
                        })
                        .into(ivProfilePicArtistEventManager!!)
            }
        } else {
            mProfilePic = obj.getString("SocialImageUrl")
            if (mProfilePic.equals("")) {
                pBarArtistEventManager!!.visibility = View.GONE
                ivProfilePicArtistEventManager!!.setImageDrawable(resources.getDrawable(R.drawable.icon_img_dummy))

            } else {
                Glide.with(this)
                        .load(mProfilePic)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                                pBarArtistEventManager!!.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                pBarArtistEventManager!!.visibility = View.GONE
                                return false
                            }
                        })
                        .into(ivProfilePicArtistEventManager!!)
            }


        }
        if (!obj.getString("BackgroundImage").equals("", ignoreCase = true)) {
            val mCoverPic = obj.getString("BackgroundImageUrl") + obj.getString("BackgroundImage")
            progressBarArtistEventManager!!.visibility = View.VISIBLE
            Glide.with(this)
                    .load(mCoverPic)
                    .listener(object : RequestListener<Drawable?> {
                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable?>, isFirstResource: Boolean): Boolean {
                            progressBarArtistEventManager!!.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(resource: Drawable?, model: Any, target: Target<Drawable?>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                            progressBarArtistEventManager!!.visibility = View.GONE
                            return false
                        }
                    })
                    .into(ivBackgroundArtistEventManager!!)
        } else {
            progressBarArtistEventManager!!.visibility = View.GONE
            Glide.with(this)
                    .load(R.drawable.icon_img_dummy)
                    .into(ivBackgroundArtistEventManager!!)
        }
    }


}