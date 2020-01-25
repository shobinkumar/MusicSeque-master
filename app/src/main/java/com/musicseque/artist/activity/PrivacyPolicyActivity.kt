package com.musicseque.artist.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.musicseque.R

class PrivacyPolicyActivity : AppCompatActivity() {
    @BindView(R.id.img_first_icon)
    var img_first_icon: ImageView? = null
    @BindView(R.id.img_right_icon)
    var img_right_icon: ImageView? = null
    @BindView(R.id.tv_title)
    var tv_title: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plocy)
        ButterKnife.bind(this)
        tv_title!!.text = "Privacy Policy"
        img_first_icon!!.visibility = View.VISIBLE
        img_right_icon!!.visibility = View.GONE
        img_first_icon!!.setOnClickListener { finish() }
    }
}