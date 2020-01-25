package com.musicseque.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.musicseque.R

class UploadFragment : Fragment(), View.OnClickListener {
    var v: View? = null
    var llPhotos: LinearLayout? = null
    var llAudio: LinearLayout? = null
    var llVideos: LinearLayout? = null
    var ivUploadVideo: ImageView? = null
    var ivUploadAudio: ImageView? = null
    var ivUploadPhoto: ImageView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_upload, container, false)
        initOtherViews()
        initViews()
        listeners()
        return v
    }

    private fun initOtherViews() {}
    private fun initViews() {
        llPhotos = view!!.findViewById<View>(R.id.llPhotos) as LinearLayout
        llAudio = view!!.findViewById<View>(R.id.llAudio) as LinearLayout
        llVideos = view!!.findViewById<View>(R.id.llVideos) as LinearLayout
        ivUploadVideo = view!!.findViewById<View>(R.id.ivUploadVideo) as ImageView
        ivUploadAudio = view!!.findViewById<View>(R.id.ivUploadAudio) as ImageView
        ivUploadPhoto = view!!.findViewById<View>(R.id.ivUploadPhoto) as ImageView
    }

    private fun listeners() {
        llPhotos!!.setOnClickListener(this)
        llAudio!!.setOnClickListener(this)
        llVideos!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.llPhotos -> {
            }
            R.id.llAudio -> {
            }
            R.id.llVideos -> {
            }
        }
    }
}