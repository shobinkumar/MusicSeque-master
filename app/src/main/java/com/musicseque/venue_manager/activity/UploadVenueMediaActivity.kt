package com.musicseque.venue_manager.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View

import com.musicseque.R
import com.musicseque.activities.BaseActivity
import com.musicseque.artist.fragments.UploadPhotoFragment
import com.musicseque.artist.fragments.UploadVideoFragment
import com.musicseque.venue_manager.fragment.VenueImageUploadFragment
import kotlinx.android.synthetic.main.activity_upload_venue_media.*
import kotlinx.android.synthetic.main.toolbar_top.*

import java.util.ArrayList

import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadVenueMediaActivity : BaseActivity(), View.OnClickListener {


    lateinit var fragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_venue_media)
        initOtherViews()
        initViews()
        listeners()

        fragment = UploadPhotoFragment()
        openFragment(fragment)


    }


    private fun initOtherViews() {
        tv_title.text = "Upload"
        img_right_icon.visibility = View.GONE


    }

    private fun initViews() {


    }

    private fun listeners() {
        img_first_icon.setOnClickListener(this)
        llPhotos.setOnClickListener(this)
        llVideos.setOnClickListener(this)

    }


    override fun onClick(view: View) {
        when (view.id) {

            R.id.img_first_icon -> {
                finish()
            }
            R.id.llPhotos -> {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayoutUpload)
                if (currentFragment is UploadPhotoFragment)
                    Log.e("", "")
                else {
                    fragment = VenueImageUploadFragment()
                    openFragment(fragment)
                    changeBackgroundColor(R.drawable.uploadphoto_active, R.drawable.uploadvideo)
                }
            }
            R.id.llVideos -> {
                val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayoutUpload)
                if (currentFragment is UploadVideoFragment) {

                } else {
                    changeBackgroundColor(R.drawable.uploadphoto, R.drawable.uploadvideo_active)

                    fragment = UploadVideoFragment()
                    openFragment(fragment)
                }
            }
        }


    }


    private fun changeBackgroundColor(uploadphoto: Int, uploadvideo: Int) {
        ivUploadPhoto!!.setImageResource(uploadphoto)
        ivUploadVideo!!.setImageResource(uploadvideo)

    }

    fun openDialogForPic() {
        openDialog("pic")
    }


    fun uploadPic(fileToUpload: ArrayList<MultipartBody.Part>, mUSerId: RequestBody) {

        val uploadPhotoFragment = fragment as UploadPhotoFragment
        uploadPhotoFragment.uploadImage(fileToUpload, mUSerId)


    }

    private fun openFragment(fragment: Fragment) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.frameLayoutUpload, fragment)
        transaction.addToBackStack(null)
        transaction.commit()


    }
}

