package com.musicseque.artist.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
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
import com.musicseque.interfaces.MyInterface
import com.musicseque.models.ImageModel
import com.musicseque.retrofit_interface.ImageUploadClass
import com.musicseque.retrofit_interface.RetrofitAPI
import com.musicseque.utilities.*
import kotlinx.android.synthetic.main.fragment_artist_profile_detail.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.util.*

class ProfileDetailFragment : KotlinBaseFragment(), View.OnClickListener, MyInterface {
    private var selectedImagePath = ""
    //  private File myDirectory;
    var IMAGE_FOR = 0
    var FOR_PROFILE = 1
    var FOR_BACKGROUND = 2


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


    var img_right_icon: ImageView? = null
    var tv_title: BoldNoyhr? = null
    var arrayList = ArrayList<ImageModel>()
    var v: View? = null
    var isPicAPIHit = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_artist_profile_detail, null)

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initOtherViews()
        initViews()
        listeners()
        showDefaultData()
    }
    override fun onResume() {
        super.onResume()
        if (!isPicAPIHit) {
            userProfile
            changeBackgroundColor(ivGigs, tvGigs, resources.getDrawable(R.drawable.icon_gigs_active), resources.getString(R.string.txt_gigs), ivImage, tvImage, resources.getDrawable(R.drawable.icon_photos), resources.getString(R.string.txt_image), ivMusic, tvMusic, resources.getDrawable(R.drawable.icon_music), resources.getString(R.string.txt_music), ivVideo, tvVideo, resources.getDrawable(R.drawable.icon_videos), resources.getString(R.string.txt_video), ivCollaborators, tvCollaborators, resources.getDrawable(R.drawable.icon_collaborators), resources.getString(R.string.txt_collaborators))
            changeFragment(GigsFragment())
        }
    }

    private fun showDefaultData() {
        if (SharedPref.getString(Constants.VISIBILITY_STATUS, "Available").equals("Available", ignoreCase = true)) {
            ivIndicator!!.setImageDrawable(resources.getDrawable(R.drawable.icon_green))
        } else if (SharedPref.getString(Constants.VISIBILITY_STATUS, "Available").equals("Offline", ignoreCase = true)) {
            ivIndicator!!.setImageDrawable(resources.getDrawable(R.drawable.icon_invisible))
        } else {
            ivIndicator!!.setImageDrawable(resources.getDrawable(R.drawable.icon_red))
        }
    }

    private fun initOtherViews() { //  View views = view.findViewById(R.id.toolbarTop);
// img_first_icon = (ImageView) views.findViewById(R.id.img_first_icon);
        img_right_icon = (activity as MainActivity?)!!.findViewById<View>(R.id.img_right_icon) as ImageView
        tv_title = (activity as MainActivity?)!!.findViewById<View>(R.id.tvHeading) as BoldNoyhr
        img_right_icon!!.visibility = View.VISIBLE
        tv_title!!.text = "Profile"
        val tvDone = (activity as MainActivity?)!!.findViewById<View>(R.id.tvDone) as TextView
        tvDone.visibility = View.GONE
        //        iv_home = (ImageView) view.findViewById(R.id.iv_home);
//        iv_profile = (ImageView) view.findViewById(R.id.iv_profile);
//        iv_feature = (ImageView) view.findViewById(R.id.iv_feature);
//        iv_chat = (ImageView) view.findViewById(R.id.iv_chat);
//        iv_settings = (ImageView) view.findViewById(R.id.iv_settings);
        ButterKnife.bind(this, v!!)
    }

    private fun initViews() {
        btnFollow!!.visibility = View.GONE
    }

    private fun listeners() { /*  iv_home.setOnClickListener(this);
        iv_profile.setOnClickListener(this);
        iv_feature.setOnClickListener(this);
        iv_chat.setOnClickListener(this);
        iv_settings.setOnClickListener(this);*/
        img_right_icon!!.setOnClickListener { startActivity(Intent(activity, MainActivity::class.java).putExtra("profileTemp", true).putExtra("frag", "com.musicseque.artist.fragments.ProfileFragment")) }
    }

    @OnClick(R.id.ivCameraBackground, R.id.btnFollow, R.id.ivCameraProfilePic, R.id.tvMessage, R.id.ivIconDrop, R.id.ivIconUp, R.id.llMusic, R.id.llVideo, R.id.llPhotos, R.id.llGigs, R.id.llCollaborators)
    fun setViewOnClickEvent(view: View) {
        when (view.id) {
            R.id.ivCameraBackground -> {
                IMAGE_FOR = FOR_BACKGROUND
                checkPermissions("image", "com.musicseque.artist.fragments.ProfileDetailFragment", this)

            }
            R.id.btnFollow -> {
            }
            R.id.ivCameraProfilePic -> {
                IMAGE_FOR = FOR_PROFILE
                checkPermissions("image", "com.musicseque.artist.fragments.ProfileDetailFragment", this)

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

                bundle.putString("UserId",SharedPref.getString(Constants.USER_ID,""))
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

    private val userProfile: Unit
        private get() {
            if (Utils.isNetworkConnected(activity)) {
                initializeLoader()
                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("UserId", SharedPref.getString(Constants.USER_ID, ""))
                    RetrofitAPI.callAPI(jsonObject.toString(), Constants.FOR_USER_PROFILE, this@ProfileDetailFragment)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                Utils.showToast(activity, resources.getString(R.string.err_no_internet))
            }
        }



    fun initializeLoader() {
        Utils.initializeAndShow(activity)
        //        Utils.initializeProgressDialog(getActivity());
//        Utils.showProgressDialog();
    }


    override fun sendResponse(response: Any, TYPE: Int) {
        Utils.hideProgressDialog()
        when (TYPE) {
            Constants.FOR_USER_PROFILE -> try {
                val obj = JSONObject(response.toString())
                if (obj.getString("Status").equals("Success", ignoreCase = true)) {
                    tvUserNameDetail!!.text = obj.getString("FirstName") + " " + obj.getString("LastName")
                    tvUserType!!.text = obj.getString("Expertise")
                    tvUserLocation!!.text = obj.getString("CityName") + ", " + obj.getString("CountryName")
                    tvFollowersCount!!.text = obj.getString("Followers")
                    tvUserID!!.text = SharedPref.getString(Constants.UNIQUE_CODE, "")
                    // rBar.setRating(Float.parseFloat(obj.getString("Rating")));
// rBar.setNumStars(5);
//                        rBar.setIsIndicator(true);
//                          rBar.setRating(Float.parseFloat("4.0"));
                    tvReviews!!.text = "(" + obj.getString("Reviews") + " reviews" + ")"
                    tvGenre!!.text = obj.getString("GenreTypeName")
                    tvExperience!!.text = obj.getString("ExperienceYear")
                    tvBio!!.text = obj.getString("Bio")
                    var mProfilePic = ""
                    if (obj.getString("SocialId").equals("", ignoreCase = true)) {
                        ivCameraProfilePic!!.visibility = View.VISIBLE
                        mProfilePic = obj.getString("ProfilePic")
                        if (mProfilePic.equals("", ignoreCase = true)) {
                            ivProfilePic!!.setImageDrawable(resources.getDrawable(R.drawable.icon_img_dummy))
                            pBar!!.visibility = View.GONE
                        } else {
                            mProfilePic = obj.getString("ImgUrl") + obj.getString("ProfilePic")
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
                    } else {
                        ivCameraProfilePic!!.visibility = View.GONE
                        mProfilePic = obj.getString("SocialImageUrl")
                        if (mProfilePic.equals("", ignoreCase = true)) {
                            Glide.with(activity!!)
                                    .load(R.drawable.icon_img_dummy).into(ivProfilePic!!)
                        } else {
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
                    }
                    //                        if (SharedPref.getString(Constants.LOGIN_TYPE, "Simple").equalsIgnoreCase("Simple")) {
//                            ivCameraProfilePic.setVisibility(View.VISIBLE);
//                        } else {
//                            ivCameraProfilePic.setVisibility(View.GONE);
//
//                        }
                }
                if (!obj.getString("BackgroundImage").equals("", ignoreCase = true)) {
                    val mCoverPic = obj.getString("BackgroundImageUrl") + obj.getString("BackgroundImage")
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
                }
                sv!!.fullScroll(ScrollView.FOCUS_UP)
            } catch (e: JSONException) {
                e.printStackTrace()
            }

            Constants.FOR_UPLOAD_PROFILE_IMAGE -> {
                isPicAPIHit = false
                try {
                    val jsonObject = JSONObject(response.toString())
                    if (jsonObject.getString("Status").equals("Success", ignoreCase = true)) {
                        SharedPref.putString(Constants.PROFILE_IMAGE, jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))
                        Utils.showToast(activity, "Profile Pic uploaded successfully")
                        //Glide.with(getActivity()).load(jsonObject.getString("imageurl") + jsonObject.getString("ImageName")).into(ivProfilePic);
                        pBar!!.visibility = View.VISIBLE
                        Glide.with(this)
                                .load(jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))
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
                        SharedPref.putString(Constants.PROFILE_IMAGE, jsonObject.getString("imageurl") + jsonObject.getString("ImageName"))
                    } else {
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            Constants.FOR_UPLOAD_COVER_PIC -> {
                isPicAPIHit = false
                try {
                    val jsonObject1 = JSONObject(response.toString())
                    if (jsonObject1.getString("Status").equals("Success", ignoreCase = true)) {
                        SharedPref.putString(Constants.COVER_IMAGE, jsonObject1.getString("imageurl") + jsonObject1.getString("ImageName"))
                        progressBar!!.visibility = View.VISIBLE
                        Utils.showToast(activity, "Cover Pic uploaded successfully")
                        Glide.with(this)
                                .load(jsonObject1.getString("imageurl") + jsonObject1.getString("ImageName"))
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









    public fun getImageFile(file: File) {
        val mFile = RequestBody.create(MediaType.parse("image/*"), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
        val mUSerId = RequestBody.create(MediaType.parse("text/plain"), SharedPref.getString(Constants.USER_ID, ""))
        if (Utils.isNetworkConnected(activity!!)) {
            initializeLoader()

            if (IMAGE_FOR == FOR_BACKGROUND)
                ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_COVER_PIC, this)
            else
                ImageUploadClass.imageUpload(fileToUpload, mUSerId, null, Constants.FOR_UPLOAD_PROFILE_IMAGE, this)
        } else {
            Utils.showToast(activity, resources.getString(R.string.err_no_internet))
        }


    }

    override fun onClick(p0: View?) {
    }


}