package com.musicseque.artist.activity.other_artist_activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.musicseque.Fonts.Noyhr
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.artist.fragments.*
import com.musicseque.interfaces.MyInterface
import com.musicseque.models.ImageModel
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.Constants.FOR_OTHER_PROFILE
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.activity_other_artist.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class OtherProfileActivity : BaseActivity(), MyInterface {

    @JvmField
    @BindView(R.id.ivMusic)
    var ivMusic: ImageView? = null
    @JvmField
    @BindView(R.id.tvMusic)
    var tvMusic: Noyhr? = null
    @JvmField
    @BindView(R.id.ivVideo)
    var ivVideo: ImageView? = null
    @JvmField
    @BindView(R.id.tvVideo)
    var tvVideo: Noyhr? = null
    @JvmField
    @BindView(R.id.ivImage)
    var ivImage: ImageView? = null
    @JvmField
    @BindView(R.id.tvImage)
    var tvImage: Noyhr? = null
    @JvmField
    @BindView(R.id.ivGigs)
    var ivGigs: ImageView? = null
    @JvmField
    @BindView(R.id.tvGigs)
    var tvGigs: Noyhr? = null
    @JvmField
    @BindView(R.id.ivCollaborators)
    var ivCollaborators: ImageView? = null
    @JvmField
    @BindView(R.id.tvCollaborators)
    var tvCollaborators: Noyhr? = null

    var arrayList = ArrayList<ImageModel>()
    var view: View? = null
    var mUSerId = 0
    @JvmField
    @BindView(R.id.img_first_icon)
    var img_first_icon: ImageView? = null
    @JvmField
    @BindView(R.id.img_right_icon)
    var img_right_icon: ImageView? = null
    @JvmField
    @BindView(R.id.tv_title)
    var tv_title: TextView? = null
    var mFollowerCount: String? = null
    var isFollow = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_artist)
        initOtherViews()
        initViews()
        hitAPI(FOR_OTHER_PROFILE)
    }

    private fun initOtherViews() {
        ButterKnife.bind(this)
        tv_title!!.text = "Profile"
        img_first_icon!!.visibility = View.VISIBLE
        img_right_icon!!.visibility = View.GONE
        mUSerId = intent.getIntExtra("id", 0)
    }

    private fun initViews() {
        changeBackgroundColor(ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs_active), resources.getString(R.string.txt_gigs), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivCollaborators, tvCollaborators, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_collaborators))
        changeFragment(GigsFragment())
    }


    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_OTHER_PROFILE -> try {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                    val jsonArray = jsonObject.getJSONArray("result")
                    val obj = jsonArray.getJSONObject(0)
                    tvUserNameDetail!!.text = obj.getString("FirstName") + " " + obj.getString("LastName")
                    tvUserType!!.text = obj.getString("Expertise")
                    tvUserLocation!!.text = obj.getString("City") + ", " + obj.getString("CountryName")
                    tvFollowersCount!!.text = obj.getString("Followers")
                    val mCount = obj.getString("Followers").toInt()
                    mFollowerCount = mCount.toString() + ""
                    tvUserID!!.text = SharedPref.getString(Constants.UNIQUE_CODE, "")
                    tvReviews!!.text = "(" + obj.getString("Reviews") + " reviews" + ")"
                    tvGenre!!.text = obj.getString("GenreTypeName")
                    tvExperience!!.text = obj.getString("ExperienceYear")
                    tvBio!!.text = obj.getString("Bio")
                    tvUserID!!.text = obj.getString("UniqueCode")
                    if (obj.getString("NewStatus").equals("Available", ignoreCase = true)) {
                        ivIndicator!!.setImageDrawable(resources.getDrawable(R.drawable.icon_green))
                    } else if (obj.getString("NewStatus").equals("Offline", ignoreCase = true)) {
                        ivIndicator!!.setImageDrawable(resources.getDrawable(R.drawable.icon_invisible))
                    } else {
                        ivIndicator!!.setImageDrawable(resources.getDrawable(R.drawable.icon_red))
                    }
                    if (obj.getInt("FollowersTag") == 1) {
                        isFollow = true
                        btnFollow!!.text = "Following"
                    } else {
                        btnFollow!!.text = "Follow"
                        isFollow = false
                    }
                    var mProfilePic = ""
                    if (obj.getString("SocialId").equals("", ignoreCase = true)) {
                        mProfilePic = obj.getString("ProfilePic")
                        if (mProfilePic.equals("", ignoreCase = true)) {
                            ivProfilePic!!.setImageDrawable(resources.getDrawable(R.drawable.icon_img_dummy))
                            pBar!!.visibility = View.GONE
                        } else {
                            mProfilePic = obj.getString("ImgUrl") + obj.getString("ProfilePic")
                            Glide.with(this)
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
                    } else {
                        mProfilePic = obj.getString("SocialImageUrl")
                        Glide.with(this)
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
                    if (!obj.getString("BackgroundImage").equals("", ignoreCase = true)) {
                        val mCoverPic = obj.getString("BackgroundImageUrl") + obj.getString("BackgroundImage")
                        progressBar!!.visibility = View.VISIBLE
                        Glide.with(this)
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
                        Glide.with(this)
                                .load(R.drawable.icon_img_dummy)
                                .into(ivBackground!!)
                    }
                }
                sv!!.fullScroll(ScrollView.FOCUS_UP)
            } catch (e: JSONException) {
                e.printStackTrace()
            }


            Constants.FOR_FOLLOW_UNFOLLOW_ARTIST -> try {
                val mCount: Int
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", ignoreCase = true)) {
                    if (obj.getInt("FollowerTag") == 1) {
                        isFollow = true
                        btnFollow!!.text = "Following"
                        mCount = mFollowerCount!!.toInt() + 1
                        mFollowerCount = mCount.toString() + ""
                        tvFollowersCount!!.text = mFollowerCount
                    } else {
                        btnFollow!!.text = "Follow"
                        mCount = mFollowerCount!!.toInt() - 1
                        mFollowerCount = mCount.toString() + ""
                        tvFollowersCount!!.text = mFollowerCount
                        isFollow = false
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

    @OnClick(R.id.tvBand, R.id.img_first_icon, R.id.ivCameraBackground, R.id.btnFollow, R.id.ivCameraProfilePic, R.id.tvMessage, R.id.ivIconDrop, R.id.ivIconUp, R.id.llMusic, R.id.llVideo, R.id.llPhotos, R.id.llGigs, R.id.llCollaborators)
    fun setViewOnClickEvent(view: View) {
        when (view.id) {
            R.id.img_first_icon -> finish()
            R.id.tvBand -> startActivity(Intent(this, ArtistBandListActivity::class.java).putExtra("id", mUSerId))
            R.id.btnFollow -> {
                changeFollowStatus()
                if (Utils.isNetworkConnected(this)) {
                    showDialog()
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put("ArtistId", mUSerId)
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
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_FOLLOW_UNFOLLOW_ARTIST, this)
                } else {
                    Utils.showToast(this, resources.getString(R.string.err_no_internet))
                }
            }
            R.id.tvMessage -> {
            }
            R.id.ivIconDrop -> {
                tvBio!!.visibility = View.VISIBLE
                ivIconDrop!!.visibility = View.GONE
                ivIconUp!!.visibility = View.VISIBLE
            }
            R.id.ivIconUp -> {
                tvBio!!.visibility = View.GONE
                ivIconDrop!!.visibility = View.VISIBLE
                ivIconUp!!.visibility = View.GONE
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

                bundle.putString("UserId",mUSerId.toString())
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
        ft.replace(R.id.frame_layout, fragment!!).commit()
    }

    private fun hitAPI(TYPE: Int) {
        if (Utils.isNetworkConnected(this))
        {
            showDialog()
            if (TYPE == FOR_OTHER_PROFILE) {
                val jsonObject = JSONObject()
                jsonObject.put("ArtistUserId", mUSerId)
                jsonObject.put("LoginUserId", SharedPref.getString(Constants.USER_ID, ""))
                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_OTHER_PROFILE, this)
            }
        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }
    }


    fun changeFollowStatus() {
        val mStatus = btnFollow!!.text.toString()
        if (mStatus.equals("Follow", ignoreCase = true)) {
            isFollow = true
            btnFollow!!.text = "Following"
        } else {
            btnFollow!!.text = "Follow"
            isFollow = false
        }
    }

    fun showDialog() {
        Utils.initializeAndShow(this@OtherProfileActivity)
    }

    companion object {
        private const val FOR_AUDIO = 101
        private const val FOR_IMAGE = 100
    }
}