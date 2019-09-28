package com.musicseque.event_manager.activity

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.ScrollView
import butterknife.ButterKnife
import butterknife.OnClick
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.musicseque.Fonts.Noyhr
import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.artist.fragments.*
import com.musicseque.interfaces.MyInterface
import com.musicseque.models.ImageModel
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.Constants
import com.musicseque.utilities.SharedPref
import com.musicseque.utilities.Utils
import kotlinx.android.synthetic.main.toolbar_top.*
import kotlinx.android.synthetic.main.activity_other_artist_event_manager.*
import kotlinx.android.synthetic.main.bottom_bar.*

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class OtherArtistActivityEventManager : BaseActivity(), MyInterface {

    private var isFollow: Boolean = false
    private var mFollowerCount: String = ""
    var mUSerId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_artist_event_manager)
        initOtherViews()
        initViews()
        hitAPI("profile")
    }

    private fun initOtherViews() {
        ButterKnife.bind(this)
        tv_title.setText("Profile")
        img_first_icon.setVisibility(View.VISIBLE)
        img_right_icon.setVisibility(View.GONE)
        mUSerId = intent.getIntExtra("id", 0)
    }

    private fun initViews() {
        changeBackgroundColor(ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs_active), resources.getString(R.string.txt_gigs), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivCollaborators, tvCollaborators, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_collaborators))
        changeFragment(GigsFragment())
    }


    private fun hitAPI(type: String) {
        if (Utils.isNetworkConnected(this)) {
            Utils.initializeAndShow(this)

            try {
                if (type.equals("profile")) {
                    val jsonObject = JSONObject()
                    jsonObject.put("ArtistUserId", mUSerId)
                    jsonObject.put("LoginUserId", SharedPref.getString(Constants.USER_ID, ""))
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_OTHER_PROFILE, this)
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }

        } else {
            Utils.showToast(this, resources.getString(R.string.err_no_internet))
        }

    }

    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_OTHER_PROFILE -> try {
                val jsonObject = JSONObject(response.toString())
                if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                    val jsonArray = jsonObject.getJSONArray("result")
                    val obj = jsonArray.getJSONObject(0)


                    tvUserNameDetail.setText(obj.getString("FirstName") + " " + obj.getString("LastName"))
                    tvUserType.setText(obj.getString("Expertise"))
                    tvUserLocation.setText(obj.getString("City") + ", " + obj.getString("CountryName"))
                    tvFollowersCount.setText(obj.getString("Followers"))
                    val mCount = Integer.parseInt(obj.getString("Followers"))
                    mFollowerCount = mCount.toString() + ""
                    tvUserID.setText(SharedPref.getString(Constants.UNIQUE_CODE, ""))

                    tvReviews.setText("(" + obj.getString("Reviews") + " reviews" + ")")
                    tvGenre.setText(obj.getString("GenreTypeName"))
                    tvExperience.setText(obj.getString("ExperienceYear"))
                    tvBio.setText(obj.getString("Bio"))

                    if (obj.getString("NewStatus").equals("Available", ignoreCase = true)) {
                        ivIndicator.setImageDrawable(resources.getDrawable(R.drawable.icon_green))
                    } else if (obj.getString("NewStatus").equals("Offline", ignoreCase = true)) {
                        ivIndicator.setImageDrawable(resources.getDrawable(R.drawable.icon_invisible))
                    } else {
                        ivIndicator.setImageDrawable(resources.getDrawable(R.drawable.icon_red))
                    }


                    if (obj.getInt("FollowersTag") == 1) {
                        isFollow = true
                        btnFollow.setText("Following")
                    } else {
                        btnFollow.setText("Follow")
                        isFollow = false
                    }


                    var mProfilePic = ""

                    if (obj.getString("SocialId").equals("", ignoreCase = true)) {
                        mProfilePic = obj.getString("ProfilePic")
                        if (mProfilePic.equals("", ignoreCase = true)) {

                            ivProfilePic.setImageDrawable(resources.getDrawable(R.drawable.icon_img_dummy))
                            pBar.setVisibility(View.GONE)

                        } else {
                            mProfilePic = obj.getString("ImgUrl") + obj.getString("ProfilePic")
                            Glide.with(this)
                                    .load(mProfilePic)
                                    .listener(object : RequestListener<Drawable> {
                                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                            pBar.setVisibility(View.GONE)
                                            return false
                                        }

                                        override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                            pBar.setVisibility(View.GONE)
                                            return false
                                        }
                                    })
                                    .into(ivProfilePic)
                        }
                    } else {
                        mProfilePic = obj.getString("SocialImageUrl")
                        Glide.with(this)
                                .load(mProfilePic)
                                .listener(object : RequestListener<Drawable> {
                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                        pBar.setVisibility(View.GONE)
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        pBar.setVisibility(View.GONE)
                                        return false
                                    }
                                })
                                .into(ivProfilePic)

                    }

                    if (!obj.getString("BackgroundImage").equals("", ignoreCase = true)) {
                        val mCoverPic = obj.getString("BackgroundImageUrl") + obj.getString("BackgroundImage")
                        progressBar.setVisibility(View.VISIBLE)
                        Glide.with(this)
                                .load(mCoverPic)
                                .listener(object : RequestListener<Drawable> {

                                    override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                                        progressBar.setVisibility(View.GONE)
                                        return false
                                    }

                                    override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                        progressBar.setVisibility(View.GONE)
                                        return false
                                    }
                                })
                                .into(ivBackground)
                    } else {
                        progressBar.setVisibility(View.GONE)
                    }
                }

                sv.fullScroll(ScrollView.FOCUS_UP)

            } catch (e: JSONException) {
                e.printStackTrace()
            }

//            Constants.FOR_ARTIST_UPLOADED_IMAGES ->
//
//
//                try {
//                    val jsonArray = JSONArray(response.toString())
//                    arrayList.clear()
//                    if (jsonArray.length() > 0) {
//                        for (i in 0 until jsonArray.length()) {
//                            val jsonObject = jsonArray.getJSONObject(i)
//                            arrayList.add(ImageModel(jsonObject.getString("ImgUrl"), jsonObject.getString("Image"), true, -1, false))
//
//                            val fragment = ImagesFragment()
//                            val bundle = Bundle()
//                            val gson = Gson()
//                            bundle.putString("data", gson.toJson(arrayList))
//                            fragment.arguments = bundle
//                            changeFragment(fragment)
//                        }
//
//                    } else {
//                        val fragment = ImagesFragment()
//                        val bundle = Bundle()
//                        val gson = Gson()
//                        bundle.putString("data", "")
//                        fragment.arguments = bundle
//                        changeFragment(fragment)
//                    }
//                    //  arrayList.add(new ImageModel("", "", false));
//
//
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }


            Constants.FOR_UPLOADED_AUDIO -> try {
                val jsonArray = JSONArray(response.toString())
                if (jsonArray.length() > 0) {

                } else {
                    val fragment = MusicFragment()
                    val bundle = Bundle()
                    val gson = Gson()
                    bundle.putString("data", "")
                    fragment.arguments = bundle
                    changeFragment(fragment)
                }
                //  arrayList.add(new ImageModel("", "", false));


            } catch (e: JSONException) {
                e.printStackTrace()
            }


            Constants.FOR_FOLLOW_UNFOLLOW_ARTIST -> try {
                val mCount: Int
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", ignoreCase = true)) {
                    if (obj.getInt("FollowerTag") == 1) {
                        isFollow = true
                        btnFollow.setText("Following")
                        mCount = Integer.parseInt(mFollowerCount) + 1
                        mFollowerCount = mCount.toString() + ""
                        tvFollowersCount.setText(mFollowerCount)
                    } else {
                        btnFollow.setText("Follow")
                        mCount = Integer.parseInt(mFollowerCount) - 1
                        mFollowerCount = mCount.toString() + ""
                        tvFollowersCount.setText(mFollowerCount)
                        isFollow = false
                    }
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
    }

    @OnClick(R.id.tvCheckAvailability,R.id.img_first_icon, R.id.ivCameraBackground, R.id.btnFollow, R.id.ivCameraProfilePic, R.id.tvMessage, R.id.ivIconDrop, R.id.ivIconUp, R.id.llMusic, R.id.llVideo, R.id.llPhotos, R.id.llGigs, R.id.llCollaborators)
    fun setViewOnClickEvent(view: View) {
        when (view.id) {
            R.id.img_first_icon -> finish()
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


                tvBio.setVisibility(View.VISIBLE)
                ivIconDrop.setVisibility(View.GONE)
                ivIconUp.setVisibility(View.VISIBLE)
            }
            R.id.ivIconUp -> {
                tvBio.setVisibility(View.GONE)
                ivIconDrop.setVisibility(View.VISIBLE)
                ivIconUp.setVisibility(View.GONE)
            }
            R.id.tvCheckAvailability -> {
               startActivity(Intent(this,CheckArtistAvailabilityActivity::class.java))
            }


            R.id.llMusic -> {
//                changeBackgroundColor(ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music_active), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_collaborators))
//                changeFragment(MusicFragment())
            }
            R.id.llVideo -> {
//                changeBackgroundColor(ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos_active), resources.getString(R.string.txt_video), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_collaborators))
//                changeFragment(VideoFragment())
            }

            R.id.llPhotos -> {

//                changeBackgroundColor(ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos_active), resources.getString(R.string.txt_image), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs), ivCollaborators, tvCollaborators, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_collaborators))
//                hitAPI(FOR_IMAGE)
            }
            R.id.llGigs -> {
//                changeBackgroundColor(ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs_active), resources.getString(R.string.txt_gigs), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivCollaborators, tvCollaborators, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_collaborators))
//                changeFragment(GigsFragment())
            }
            R.id.llCollaborators -> {
//                changeBackgroundColor(ivCollaborators, tvCollaborators, resources.getDrawable(R.drawable.icon_collaborators_active), resources.getString(R.string.txt_collaborators), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs), resources.getString(R.string.txt_gigs))
//                changeFragment(CollaboratorsFragment())
            }
        }//hitAPI(FOR_IMAGE);
        //hitAPI(FOR_IMAGE);
    }

    private fun changeBackgroundColor(ivSelected: ImageView, tvSelected: Noyhr, selectedDrawable: Drawable, textSelected: String, iv1: ImageView, tv1: Noyhr, drawable1: Drawable, text1: String, iv2: ImageView, tv2: Noyhr, drawable2: Drawable, text2: String, iv3: ImageView, tv3: Noyhr, drawable3: Drawable, text3: String, iv4: ImageView, tv4: Noyhr, drawable4: Drawable, text4: String) {


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

        iv4.setImageDrawable(drawable4)
        tv4.text = text4
        tv4.setTextColor(resources.getColor(R.color.color_white))


    }

    internal fun changeFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.frame_layout, fragment).commit()

    }

//    private fun hitAPI(TYPE: Int) {
//        if (TYPE == FOR_IMAGE) {
//            if (Utils.isNetworkConnected(this)) {
//                showDialog()
//                val jsonObject = JSONObject()
//                try {
//                    jsonObject.put("UserId", mUSerId)
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//
//                RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_ARTIST_UPLOADED_IMAGES, this)
//            } else {
//                Utils.showToast(this, resources.getString(R.string.err_no_internet))
//            }
//
//        } else if (TYPE == FOR_AUDIO) {
//            if (Utils.isNetworkConnected(this)) {
//                showDialog()
//                val jsonObject = JSONObject()
//                var requestBody = ""
//                try {
//                    jsonObject.put("UserId", mUSerId)
//                    jsonObject.put("FileType", "Audio")
//
//                    requestBody = jsonObject.toString()
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//
//
//                RetrofitAPI.callAPI(requestBody, Constants.FOR_UPLOADED_AUDIO, this)
//            }
//        } else {
//            Utils.showToast(this, resources.getString(R.string.err_no_internet))
//        }
//
//    }

    internal fun changeFollowStatus() {
        val mStatus = btnFollow.getText().toString()
        if (mStatus.equals("Follow", ignoreCase = true)) {
            isFollow = true
            btnFollow.setText("Following")
        } else {
            btnFollow.setText("Follow")
            isFollow = false
        }

    }

    internal fun showDialog() {
    }

}
